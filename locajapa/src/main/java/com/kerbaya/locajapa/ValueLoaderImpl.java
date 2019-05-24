/*
 * Copyright 2019 Kerbaya Software
 * 
 * This file is part of locajapa. 
 * 
 * locajapa is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * locajapa is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with locajapa.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kerbaya.locajapa;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.kerbaya.locajapa.QueryBuilder.ValueQueryBuilder;

final class ValueLoaderImpl<T, V> implements ValueLoader<T, V>
{
	private final Map<Object, ValueLoaderEntry<V>> idMap = new HashMap<>();
	
	private final Set<String> languageTags;
	private final EntityHandler<? super T, ?, ? extends V> entityHandler;
	private final ValueQueryBuilder<V> vqb;
	
	public ValueLoaderImpl(
			Locale locale, 
			EntityHandler<? super T, ?, ? extends V> entityHandler,
			ValueQueryBuilder<V> vqb)
	{
		languageTags = Utils.getCandidateLanguageTags(locale);
		this.entityHandler = entityHandler;
		this.vqb = vqb == null ? 
				null 
				: vqb.copy()
						.addLanguageTagParams(languageTags.size())
						.asReadOnly();
	}
	
	@Override
	public V getValue(T localizable)
	{
		if (entityHandler == null)
		{
			throw new UnsupportedOperationException();
		}
		return ValueResolver.resolve(
				entityHandler, 
				Objects.requireNonNull(localizable), 
				languageTags);
	}

	public ValueReference<V> getRef(T localizable)
	{
		if (localizable == null)
		{
			return null;
		}
		if (entityHandler == null)
		{
			throw new UnsupportedOperationException();
		}
		return getRef(entityHandler.getId(localizable), localizable);
	}
	
	public ValueReference<V> getRefById(Object id)
	{
		if (id == null)
		{
			return null;
		}
		if (vqb == null)
		{
			throw new UnsupportedOperationException();
		}
		return getRef(id, null);
	}
	
	private ValueReference<V> getRef(Object id, T instance)
	{
		ValueLoaderEntry<V> entry = idMap.get(id);
		if (entry != null)
		{
			return entry;
		}
		
		if (instance == null || entityHandler == null)
		{
			entry = new ValueLoaderEntry<>(NonResolvable.<V>instance());
		}
		else
		{
			entry = new ValueLoaderEntry<>(new ValueResolver<>(
					entityHandler, instance, languageTags));
		}
		idMap.put(id, entry);
		return entry;

	}
	
	@Override
	public void load(EntityManager em)
	{
		if (vqb == null)
		{
			throw new UnsupportedOperationException();
		}
		int maxBatchSize = vqb.getMaxBatchSize();
		Map<Object, ValueLoaderEntry<V>> batch = new HashMap<>();
		
		Query maxBatchQuery = null;
		for (Entry<Object, ValueLoaderEntry<V>> mapEntry: idMap.entrySet())
		{
			ValueLoaderEntry<V> entry = mapEntry.getValue();
			if (entry.isLoaded())
			{
				continue;
			}
			batch.put(mapEntry.getKey(), entry);
			if (batch.size() == maxBatchSize)
			{
				if (maxBatchQuery == null)
				{
					maxBatchQuery = createQuery(em, maxBatchSize);
				}
				flushBatch(maxBatchQuery, batch);
			}
		}
		
		if (!batch.isEmpty())
		{
			flushBatch(createQuery(em, batch.size()), batch);
		}
	}
	
	private Query createQuery(EntityManager em, int idBatchSize)
	{
		Query q = em.createQuery(vqb.copy()
				.addIdParams(idBatchSize)
				.createQueryText());
		
		ValueQueryBuilder.setLanguageTagParams(q, languageTags);
		return q;
	}
	
	@SuppressWarnings("unchecked")
	private void flushBatch(
			Query q, 
			Map<Object, ValueLoaderEntry<V>> batch)
	{
		ValueQueryBuilder.setIdParams(q, batch.keySet());
		for (Object[] row: (List<Object[]>) q.getResultList())
		{
			
			ValueLoaderEntry<V> vs = batch.remove(
					ValueQueryBuilder.getLocalizableId(row));
			if (vs == null)
			{
				continue;
			}
			vs.set(vqb.getValue(row));
		}
		
		for (ValueLoaderEntry<V> batchEntry: batch.values())
		{
			batchEntry.set(null);
		}
		
		batch.clear();
	}
}
