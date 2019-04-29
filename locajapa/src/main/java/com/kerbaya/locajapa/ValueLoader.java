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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Provides localized values of localizable entities.  Supports batch loading of 
 * multiple instances in one query
 * 
 * @author Glenn.Lane@kerbaya.com
 *
 */
public class ValueLoader
{
	/**
	 * The default maximum number of instances to load per query
	 */
	public static final int DEFAULT_BATCH_SIZE = 1000;

	private static final Memoization<String> PAREN_JPQL_PATTERN = 
			new JpqlGenerator(true);

	private static final Memoization<String> NON_PAREN_JPQL_PATTERN = 
			new JpqlGenerator(false);
	
	private static final String ENTITY_NAME_TOKEN = "${entityName}";
	private static final String ID_SET_PARAM_TOKEN = "${idSetParam}";
	private static final String LANGUAGE_TAG_SET_PARAM_TOKEN = 
			"${languageTagSetParam}";
	private static final String ID_SET_PARAM = "idSet";
	private static final String LANGUAGE_TAG_SET_PARAM = "languageTagSet";
	
	private static final class JpqlGenerator extends Memoization<String>
	{
		private final boolean wrapParen;

		public JpqlGenerator(boolean wrapParen)
		{
			this.wrapParen = wrapParen;
		}

		@Override
		protected String create()
		{
			String idSetParamStr = ":" + ID_SET_PARAM;
			String languageTagSetParamStr = ":" + LANGUAGE_TAG_SET_PARAM;
			if (wrapParen)
			{
				idSetParamStr = "(" + idSetParamStr + ")";
				languageTagSetParamStr = "(" + languageTagSetParamStr + ")";
			}
			return Utils.loadResource(ValueLoader.class, "ValueLoader.jpql")
					.replace(ID_SET_PARAM_TOKEN, idSetParamStr)
					.replace(
							LANGUAGE_TAG_SET_PARAM_TOKEN, 
							languageTagSetParamStr);
		}
	}
	
	private final Map<String, Map<Object, ValueLoaderEntry<?>>> entityNameMap = 
			new HashMap<>();

	private final Set<String> candidateLanguageTags;
	private final EntityNameResolver entityNameResolver;
	private final int batchSize;

	/**
	 * Creates an instance where values will be returned for a particular 
	 * locale.  Shorthand for:
	 * <pre>new ValueLoader(locale, null, false, 0)</pre>
	 * 
	 * @see #ValueLoader(Locale, EntityNameResolver, boolean, int)
	 * 
	 * @param locale
	 * The local for which values will be localized
	 */
	public ValueLoader(Locale locale)
	{
		this(locale, null, 0);
	}
	
	/**
	 * Creates an instance where values will be returned for a particular 
	 * locale.
	 * 
	 * @param locale
	 * The local for which values will be localized
	 * 
	 * @param entityNameResolver
	 * The resolver that will translate classes to entity names.  Providing 
	 * {@code null} will use {@link EntityNameResolver#DEFAULT}
	 * 
	 * @param batchSize
	 * The maximum number of localizables for which to search in one query.  
	 * Providing 0 or a negative number will use {@link #DEFAULT_BATCH_SIZE}.  
	 * This value should not exceed the maximum number of elements allowed by 
	 * the JPA implementation (or its underlying database) for a single JPQL 
	 * collection parameter.
	 */
	public ValueLoader(
			Locale locale, 
			EntityNameResolver entityNameResolver, 
			int batchSize)
	{
		List<Locale> candidateLocales = Utils.getCandidateLocales(
				new Locale.Builder().setLocale(locale).build());
		Set<String> candidateLanguageTags = new HashSet<>(
				candidateLocales.size());
		for (Locale candidateLocale: candidateLocales)
		{
			candidateLanguageTags.add(candidateLocale.toLanguageTag());
		}
		this.candidateLanguageTags = 
				Collections.unmodifiableSet(candidateLanguageTags);
		this.entityNameResolver = entityNameResolver == null ? 
				EntityNameResolver.DEFAULT : entityNameResolver;
		this.batchSize = batchSize <= 0 ? DEFAULT_BATCH_SIZE : batchSize;
	}
	
	private <V> Supplier<V> getValue(
			Class<?> type, 
			Object id,
			Localizable<? extends V> instance)
	{
		String entityName = entityNameResolver.resolveEntityName(type);
		if (entityName == null)
		{
			throw new IllegalArgumentException(
					"Could not resolve entity name: " + type);
		}
		
		Map<Object, ValueLoaderEntry<?>> entityIdMap = entityNameMap.get(
				entityName);
		
		if (entityIdMap == null)
		{
			entityIdMap = new HashMap<>();
			entityNameMap.put(entityName, entityIdMap);
		}
		else
		{
			@SuppressWarnings("unchecked")
			ValueLoaderEntry<V> entry = 
					(ValueLoaderEntry<V>) entityIdMap.get(id);
			if (entry != null)
			{
				return entry;
			}
		}
		
		ValueLoaderEntry<V> entry = instance == null ?
				new ValueLoaderEntry<V>()
				: new ValueLoaderEntry<V>(candidateLanguageTags, instance);
		entityIdMap.put(id, entry);
		return entry;

	}
	
	/**
	 * Returns a reference to a value that will be later loaded with 
	 * {@link #load(EntityManager)}.  If {@link Supplier#get()} is called
	 * on the returned instance before {@link #load(EntityManager)} is called,
	 * {@link Supplier#get()} will throw {@link IllegalStateException}.
	 * 
	 * @param type
	 * the entity type
	 * 
	 * @param id
	 * the entity ID
	 * 
	 * @return
	 * the value reference
	 */
	public <V> Supplier<V> getValue(
			Class<? extends Localizable<? extends V>> type, Object id)
	{
		if (id == null)
		{
			return null;
		}
		return getValue(type, id, null);
	}
	
	/**
	 * Returns a deferred (i.e. "lazy") localized value for the provided 
	 * localizable entity.  If calling {@link Supplier#get()} is avoided
	 * until after {@link #load(EntityManager)} is called, all references 
	 * previously returned by this method are loaded in a single query.
	 * 
	 * @param localizable
	 * The localizable entity for which to return a localized value reference
	 * 
	 * @param <V>
	 * The localized value type
	 * 
	 * @return
	 * a localized value reference
	 */
	public <V> Supplier<V> getValue(Localizable<? extends V> localizable)
	{
		if (localizable == null)
		{
			return null;
		}
		return getValue(
				localizable.getClass(), localizable.getId(), localizable);
	}
	
	/**
	 * Loads all values previously returned by the getValue methods in one query
	 * 
	 * @param em
	 * The entity manager on which the query will be executed
	 */
	public void load(EntityManager em)
	{
		final String queryPattern = Utils.wrapColParam(em) ? 
				PAREN_JPQL_PATTERN.get()
				: NON_PAREN_JPQL_PATTERN.get();
		Map<Object, ValueLoaderEntry<?>> batch = new HashMap<>();
		for (Entry<String, Map<Object, ValueLoaderEntry<?>>> entityNameEntry: 
				entityNameMap.entrySet())
		{
			Query q = em.createQuery(queryPattern.replace(
							ENTITY_NAME_TOKEN, entityNameEntry.getKey()))
					.setParameter(
							LANGUAGE_TAG_SET_PARAM, candidateLanguageTags);
			Iterator<Entry<Object, ValueLoaderEntry<?>>> entityEntryIter = 
					entityNameEntry.getValue().entrySet().iterator();
			do
			{
				Entry<Object, ValueLoaderEntry<?>> next = 
						entityEntryIter.next();
				ValueLoaderEntry<?> supplier = next.getValue();
				if (supplier.isLoaded())
				{
					continue;
				}
				batch.put(next.getKey(), supplier);
				if (batch.size() == batchSize)
				{
					flushBatch(q, batch);
				}
			} while (entityEntryIter.hasNext());
			
			if (!batch.isEmpty())
			{
				flushBatch(q, batch);
			}
		}
	}
	
	private static void flushBatch(
			Query q, Map<Object, ValueLoaderEntry<?>> batch)
	{
		@SuppressWarnings("unchecked")
		List<Object[]> queryResults = q.setParameter(
				ID_SET_PARAM, batch.keySet())
						.getResultList();
		for (Object[] queryResult: queryResults)
		{
			ValueLoaderEntry<?> vs = batch.remove(queryResult[0]);
			if (vs == null)
			{
				continue;
			}
			vs.set(queryResult[1]);
		}
		
		for (ValueLoaderEntry<?> batchEntry: batch.values())
		{
			batchEntry.setNotExists();
		}
		
		batch.clear();
	}

}
