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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public final class ValueLoaderSupport<T, V>
{
	private static final int DEFAULT_BATCH_SIZE = 1000;
	
	public static final class Builder<T, V>
	{
		private final ValueQueryBuilder query;
		private IdResolver<? super T> idResolver;
		private LocalizedResolver<? super T, ?, ? extends V> localizedResolver;
		private boolean readOnly;
		private int maxBatchSize = -1;
		private Set<String> candidateLanguageTags;
		
		private Builder()
		{
			query = new ValueQueryBuilder();
		}
		
		private Builder(Builder<T, V> source)
		{
			this.query = source.query.copy();
			this.maxBatchSize = source.maxBatchSize;
			this.candidateLanguageTags = source.candidateLanguageTags;
		}
		
		private void assertWritable()
		{
			if (readOnly)
			{
				throw new UnsupportedOperationException("read-only");
			}
		}
		
		public Builder<T, V> setEntityName(String entityName)
		{
			query.setEntityName(entityName);
			return this;
		}
		
		public Builder<T, V> setIdProperty(String idProperty)
		{
			query.setIdProperty(idProperty);
			return this;
		}
		
		public Builder<T, V> setLanguageLevelProperty(String languageLevelProperty)
		{
			query.setLanguageLevelProperty(languageLevelProperty);
			return this;
		}
		
		public Builder<T, V> setLanguageTagProperty(String languageTagProperty)
		{
			query.setLanguageTagProperty(languageTagProperty);
			return this;
		}
		
		public Builder<T, V> setLocalizedProperty(String localizedProperty)
		{
			query.setLocalizedProperty(localizedProperty);
			return this;
		}
		
		public Builder<T, V> setValueProperty(String valueProperty)
		{
			query.setValueProperty(valueProperty);
			return this;
		}
		
		public Builder<T, V> setMaxBatchSize(int maxBatchSize)
		{
			assertWritable();
			this.maxBatchSize = maxBatchSize;
			return this;
		}
		
		public Builder<T, V> setLocale(Locale locale)
		{
			assertWritable();
			candidateLanguageTags = Utils.getCandidateLanguageTags(locale);
			return this;
		}
		
		public Builder<T, V> setIdResolver(IdResolver<? super T> idResolver)
		{
			assertWritable();
			this.idResolver = idResolver;
			return this;
		}
		
		public Builder<T, V> setLocalizedResolver(
				LocalizedResolver<? super T, ?, ? extends V> localizedResolver)
		{
			assertWritable();
			this.localizedResolver = localizedResolver;
			return this;
		}
		
		public Builder<T, V> asReadOnly()
		{
			if (!readOnly)
			{
				readOnly = true;
				query.asReadOnly();
			}
			return this;
		}
		
		public ValueLoaderSupport<T, V> build()
		{
			return new ValueLoaderSupport<>(this);
		}
		
		public Builder<T, V> copy()
		{
			return new Builder<>(this);
		}
	}
	
	private final Map<Object, LazyValueReference<?>> idMap;
	
	private final ValueQueryBuilder query;
	private final LocalizedResolver<? super T, ?, ? extends V> localizedResolver;
	private final IdResolver<? super T> idResolver;
	private final int maxBatchSize;
	private final Set<String> candidateLanguageTags;
	
	public static <T, V> Builder<T, V> builder()
	{
		return new Builder<>();
	}
	
	private ValueLoaderSupport(Builder<T, V> builder)
	{
		candidateLanguageTags = Objects.requireNonNull(
				builder.candidateLanguageTags, 
				"(candidateLanguageTags) must be set");
		this.idResolver = builder.idResolver;
		query = builder.query == null ? 
				null 
				: builder.query.copy()
						.setLanguageTagParams(1, candidateLanguageTags.size())
						.asReadOnly();
		if ((query == null) != (idResolver == null))
		{
			throw new IllegalStateException(
					"(query, idResolver) must be set together or not at all");
		}

		this.localizedResolver = builder.localizedResolver;
		if (query == null && localizedResolver == null)
		{
			throw new IllegalStateException(
					"At least one of (query, localizedResolver) must be set");
		}
		
		if (query != null)
		{
			query.assertPropertiesSet();
		}
		
		maxBatchSize = builder.maxBatchSize < 0 ?
				DEFAULT_BATCH_SIZE 
				: builder.maxBatchSize == 0 ? 
						Integer.MAX_VALUE : builder.maxBatchSize;
		idMap = query == null ? 
				null : new HashMap<Object, LazyValueReference<?>>();
	}
	
	public ValueReference<V> getRef(T localizable)
	{
		if (localizable == null)
		{
			return null;
		}
		if (idResolver == null)
		{
			return InstanceValueReference.of(ValueResolverSupport.resolve(
					candidateLanguageTags, localizable, localizedResolver));
		}
		return getRef(idResolver.getId(localizable), localizable);
	}
	
	public ValueReference<V> getRefById(Object id)
	{
		if (id == null)
		{
			return null;
		}
		return getRef(id, null);
	}
	
	public V getValue(T localizable)
	{
		Objects.requireNonNull(localizable);
		if (localizedResolver == null)
		{
			throw new UnsupportedOperationException(
					"non-batch lookup not supported");
		}
		return ValueResolverSupport.resolve(
				candidateLanguageTags, localizable, localizedResolver);
	}
	
	private ValueReference<V> getRef(Object id, T instance)
	{
		if (candidateLanguageTags.isEmpty())
		{
			return InstanceValueReference.ofNull();
		}
		
		@SuppressWarnings("unchecked")
		LazyValueReference<V> entry = (LazyValueReference<V>) idMap.get(id);
		if (entry != null)
		{
			return entry;
		}
		
		if (instance == null || localizedResolver == null)
		{
			entry = new LazyValueReference<>(NonResolvable.<V>instance());
		}
		else
		{
			entry = new LazyValueReference<>(new ValueResolverSupport<>(
					candidateLanguageTags, instance, localizedResolver));
		}
		idMap.put(id, entry);
		return entry;

	}
	
	/**
	 * Loads all values previously returned by the getValue methods in one query
	 * 
	 * @param em
	 * The entity manager on which the query will be executed
	 */
	public void load(EntityManager em)
	{
		if (query == null)
		{
			throw new UnsupportedOperationException(
					"batch loading not supported");
		}
		load(
				em, 
				idMap, 
				new HashMap<Object, 
				LazyValueReference<?>>(), 
				maxBatchSize, 
				query, 
				candidateLanguageTags);
	}
	
	static void load(
			EntityManager em, 
			Map<Object, LazyValueReference<?>> idMap,
			Map<Object, LazyValueReference<?>> batch,
			int maxBatchSize,
			ValueQueryBuilder query,
			Set<String> candidateLanguageTags)
	{
		int idParamsPosition = candidateLanguageTags.size() + 1;
		Query q = null;
		Iterator<Entry<Object, LazyValueReference<?>>> entityEntryIter = 
				idMap.entrySet().iterator();
		do
		{
			Entry<Object, LazyValueReference<?>> next = 
					entityEntryIter.next();
			LazyValueReference<?> ref = next.getValue();
			if (ref.isLoaded())
			{
				continue;
			}
			batch.put(next.getKey(), ref);
			if (batch.size() == maxBatchSize)
			{
				if (q == null)
				{
					q = em.createQuery(query.copy()
							.setIdParams(idParamsPosition, maxBatchSize)
							.build());
					setLanguageTagParameters(q, candidateLanguageTags);
				}
				flushBatch(q, batch, idParamsPosition);
			}
		} while (entityEntryIter.hasNext());
		
		if (!batch.isEmpty())
		{
			q = em.createQuery(query
					.setIdParams(idParamsPosition, batch.size())
					.build());
			setLanguageTagParameters(q, candidateLanguageTags);
			flushBatch(q, batch, idParamsPosition);
		}
	}
	
	private static void setLanguageTagParameters(
			Query q, Set<String> candidateLanguageTags)
	{
		int position = 1;
		for (String languageTag: candidateLanguageTags)
		{
			q.setParameter(position++, languageTag);
		}
	}
	
	private static void flushBatch(
			Query q, 
			Map<Object, LazyValueReference<?>> batch, 
			int idParamsPosition)
	{
		for (Object key: batch.keySet())
		{
			q.setParameter(idParamsPosition++, key);
		}
		@SuppressWarnings("unchecked")
		List<Object[]> rowList = q.getResultList();
		for (Object[] row: rowList)
		{
			LazyValueReference<?> vs = batch.remove(row[0]);
			if (vs == null)
			{
				continue;
			}
			vs.set(row[1]);
		}
		
		for (LazyValueReference<?> batchEntry: batch.values())
		{
			batchEntry.setNotExists();
		}
		
		batch.clear();
	}


}
