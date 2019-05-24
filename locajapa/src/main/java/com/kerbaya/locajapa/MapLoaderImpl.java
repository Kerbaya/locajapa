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
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.kerbaya.locajapa.QueryBuilder.MapQueryBuilder;

final class MapLoaderImpl<T, V> implements MapLoader<T, V>
{
	private final Map<Object, MapLoaderEntry<V>> idMap = new HashMap<>();
	
	private final EntityHandler<? super T, ?, ? extends V> entityHandler;
	private final MapQueryBuilder<V> mqb;
	
	public MapLoaderImpl(
			EntityHandler<? super T, ?, ? extends V> entityHandler,
			MapQueryBuilder<V> mqb)
	{
		this.entityHandler = entityHandler;
		this.mqb = mqb == null ? null : mqb.copyAsReadOnly();
	}

	@Override
	public Map<Locale, V> getMap(T localizable)
	{
		if (localizable == null)
		{
			return null;
		}
		if (entityHandler == null)
		{
			throw new UnsupportedOperationException();
		}
		return getMap(entityHandler.getId(localizable), localizable);
	}

	@Override
	public Map<Locale, V> getMapById(Object id)
	{
		if (id == null)
		{
			return null;
		}
		if (mqb == null)
		{
			throw new UnsupportedOperationException();
		}
		return getMap(id, null);
	}
	
	private Map<Locale, V> getMap(Object id, T localizable)
	{
		MapLoaderEntry<V> entry = idMap.get(id);
		if (entry != null)
		{
			return entry;
		}
		entry = new MapLoaderEntry<V>(
				localizable == null || entityHandler == null ?
						NonResolvable.<Map<Locale, V>>instance() 
						: new MapResolver<>(entityHandler, localizable));
		idMap.put(id, entry);
		return entry;
	}

	@Override
	public void load(EntityManager em)
	{
		if (mqb == null)
		{
			throw new UnsupportedOperationException();
		}
		Map<Object, MapLoaderEntry<V>> batch = new HashMap<>();
		
		int maxBatchSize = mqb.getMaxBatchSize();
		Query maxBatchQuery = null;
		for (Entry<Object, MapLoaderEntry<V>> mapEntry: idMap.entrySet())
		{
			MapLoaderEntry<V> entry = mapEntry.getValue();
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
		Query q = em.createQuery(
				mqb.copy().addIdParams(idBatchSize).createQueryText());
		return q;
	}
	
	@SuppressWarnings("unchecked")
	private void flushBatch(
			Query q, 
			Map<Object, MapLoaderEntry<V>> batch)
	{
		MapQueryBuilder.setIdParams(q, batch.keySet());
		for (Object[] row: (List<Object[]>) q.getResultList())
		{
			MapLoaderEntry<V> entry = batch.get(
					MapQueryBuilder.getLocalizableId(row));
			String languageTag = MapQueryBuilder.getLanguageTag(row);
			if (languageTag != null)
			{
				entry.addFromBatch(
						Locale.forLanguageTag(languageTag), 
						mqb.getValue(row));
			}
		}
		
		for (MapLoaderEntry<V> batchEntry: batch.values())
		{
			batchEntry.finalizeBatch();
		}
		
		batch.clear();
	}
	
	
}
