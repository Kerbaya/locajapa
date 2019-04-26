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

	private static final String JPQL_PATTERN = Utils.loadResource(
			MapLoader.class, "ValueLoader.jpql");
	private static final String ENTITY_NAME_TOKEN = "${entityName}";
	private static final String ID_SET_PARAM = "idSet";
	private static final String LANGUAGE_TAG_SET_PARAM = "languageTagSet";
	
	private final Map<String, Map<Object, ValueSupplierImpl<?>>> entityNameMap = 
			new HashMap<>();

	private final Set<String> candidateLanguageTags;
	private final EntityNameResolver entityNameResolver;
	private final boolean nullAsSupplier;
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
		this(locale, null, false, 0);
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
	 * @param nullAsSupplier
	 * Controls behavior of {@link #getValue(Localizable)} when the provided
	 * {@code localizable} is {@code null}.  If {@code nullAsSupplier} is {@code false},
	 * {@link #getValue(Localizable)} returns {@code null}.  if 
	 * {@code nullAsSupplier} is {@code true}, {@link #getValue(Localizable)}
	 * returns an instance of {@link ValueSupplier}, whose 
	 * {@link ValueSupplier#get()} method returns {@code null}.
	 * 
	 * @param batchSize
	 * The maximum number of values returned per query.  Providing 0 or a 
	 * negative number will use {@link #DEFAULT_BATCH_SIZE}
	 * 
	 */
	public ValueLoader(
			Locale locale, 
			EntityNameResolver entityNameResolver, 
			boolean nullAsSupplier,
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
		this.nullAsSupplier = nullAsSupplier;
		this.batchSize = batchSize <= 0 ? DEFAULT_BATCH_SIZE : batchSize;
	}
	
	/**
	 * Returns a deferred (i.e. "lazy") localized value for the provided 
	 * localizable entity.  If calling {@link ValueSupplier#get()} is avoided
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
	public <V> ValueSupplier<V> getValue(Localizable<? extends V> localizable)
	{
		if (localizable == null)
		{
			return nullAsSupplier ? ValueSupplierImpl.<V>ofNull() : null;
		}
		
		String entityName = entityNameResolver.resolveEntityName(
				localizable.getClass());
		if (entityName == null)
		{
			throw new IllegalArgumentException(
					"Could not resolve entity name: " + localizable);
		}
		
		Object entityId = localizable.getId();
		Map<Object, ValueSupplierImpl<?>> entityIdMap = 
				entityNameMap.get(entityName);
		
		if (entityIdMap == null)
		{
			entityIdMap = new HashMap<>();
			entityNameMap.put(entityName, entityIdMap);
		}
		else
		{
			@SuppressWarnings("unchecked")
			ValueSupplierImpl<V> entry = 
					(ValueSupplierImpl<V>) entityIdMap.get(entityId);
			if (entry != null)
			{
				return entry;
			}
		}
		
		ValueSupplierImpl<V> entry = ValueSupplierImpl.of(
				candidateLanguageTags, localizable);
		entityIdMap.put(entityId, entry);
		return entry;
	}
	
	/**
	 * Loads all values previously returned by {@link #getValue(Localizable)} in
	 * one query
	 * 
	 * @param em
	 * The entity manager on which the query will be executed
	 */
	public void load(EntityManager em)
	{
		Map<Object, ValueSupplierImpl<?>> batch = new HashMap<>();
		for (Entry<String, Map<Object, ValueSupplierImpl<?>>> entityNameEntry: 
				entityNameMap.entrySet())
		{
			Query q = em.createQuery(JPQL_PATTERN.replace(
							ENTITY_NAME_TOKEN, entityNameEntry.getKey()))
					.setParameter(
							LANGUAGE_TAG_SET_PARAM, candidateLanguageTags);
			Iterator<Entry<Object, ValueSupplierImpl<?>>> entityEntryIter = 
					entityNameEntry.getValue().entrySet().iterator();
			do
			{
				Entry<Object, ValueSupplierImpl<?>> next = 
						entityEntryIter.next();
				ValueSupplierImpl<?> supplier = next.getValue();
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
			Query q, Map<Object, ValueSupplierImpl<?>> batch)
	{
		@SuppressWarnings("unchecked")
		List<Object[]> queryResults = q.setParameter(
						ID_SET_PARAM, batch.keySet())
				.getResultList();
		for (Object[] queryResult: queryResults)
		{
			ValueSupplierImpl<?> vs = batch.remove(queryResult[0]);
			if (vs == null)
			{
				continue;
			}
			vs.set(((Localized<?>) queryResult[1]).getValue());
		}
		
		for (ValueSupplierImpl<?> batchEntry: batch.values())
		{
			batchEntry.set(null);
		}
		
		batch.clear();
	}

}
