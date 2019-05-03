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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Objects;

import javax.persistence.EntityManager;

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

	private static final ValueQueryBuilder QUERY_BUILDER = 
			new ValueQueryBuilder() 
					.setIdProperty("id")
					.setLanguageLevelProperty("languageLevel")
					.setLanguageTagProperty("languageTag")
					.setLocalizedProperty("localized")
					.setValueProperty("value")
					.asReadOnly();
	
	private final Map<String, Map<Object, LazyValueReference<?>>> entityNameMap = 
			new HashMap<>();

	private final Set<String> candidateLanguageTags;
	private final EntityNameResolver entityNameResolver;
	private final int maxBatchSize;
	private final ValueQueryBuilder query;
			

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
	 * @param maxBatchSize
	 * The maximum number of localizables for which to search in one query.  
	 * Providing 0 or a negative number will use {@link #DEFAULT_BATCH_SIZE}.  
	 * This value should not exceed the maximum number of elements allowed by 
	 * the JPA implementation (or its underlying database) for a single JPQL 
	 * collection parameter.
	 */
	public ValueLoader(
			Locale locale, 
			EntityNameResolver entityNameResolver, 
			int maxBatchSize)
	{
		candidateLanguageTags = Utils.getCandidateLanguageTags(locale); 
		this.entityNameResolver = entityNameResolver == null ? 
				EntityNameResolver.DEFAULT : entityNameResolver;
		this.maxBatchSize = maxBatchSize <= 0 ? DEFAULT_BATCH_SIZE : maxBatchSize;
		query = QUERY_BUILDER.copy()
				.setLanguageTagParams(1, candidateLanguageTags.size())
				.asReadOnly();
	}
	
	private <V> ValueReference<V> getRef(
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
		
		Map<Object, LazyValueReference<?>> entityIdMap = entityNameMap.get(
				entityName);
		
		if (entityIdMap == null)
		{
			entityIdMap = new HashMap<>();
			entityNameMap.put(entityName, entityIdMap);
		}
		else
		{
			@SuppressWarnings("unchecked")
			LazyValueReference<V> entry = 
					(LazyValueReference<V>) entityIdMap.get(id);
			if (entry != null)
			{
				return entry;
			}
		}
		
		LazyValueReference<V> entry = instance == null ?
				new LazyValueReference<>(NonResolvable.<V>instance())
				: new LazyValueReference<>(
						new ValueResolver<>(candidateLanguageTags, instance));
		entityIdMap.put(id, entry);
		return entry;

	}
	
	/**
	 * Returns a reference to a value that will be later loaded with 
	 * {@link #load(EntityManager)}.  If {@link ValueReference#get()} is called
	 * on the returned instance before {@link #load(EntityManager)} is called,
	 * {@link ValueReference#get()} will throw {@link IllegalStateException}.
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
	public <V> ValueReference<V> getRef(
			Class<? extends Localizable<? extends V>> type, Object id)
	{
		if (id == null)
		{
			return null;
		}
		return getRef(type, id, null);
	}
	
	/**
	 * Returns a deferred (i.e. "lazy") localized value for the provided 
	 * localizable entity.  If calling {@link ValueReference#get()} is avoided
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
	public <V> ValueReference<V> getRef(Localizable<? extends V> localizable)
	{
		if (localizable == null)
		{
			return null;
		}
		return getRef(
				localizable.getClass(), localizable.getId(), localizable);
	}
	
	public <V> V getValue(Localizable<? extends V> localizable)
	{
		return ValueResolver.resolve(
				candidateLanguageTags, Objects.requireNonNull(localizable));
	}
	
	/**
	 * Loads all values previously returned by the getValue methods in one query
	 * 
	 * @param em
	 * The entity manager on which the query will be executed
	 */
	public void load(EntityManager em)
	{
		Map<Object, LazyValueReference<?>> batch = new HashMap<>();
		for (Entry<String, Map<Object, LazyValueReference<?>>> entityNameEntry: 
				entityNameMap.entrySet())
		{
			ValueLoaderSupport.load(
					em, 
					entityNameEntry.getValue(), 
					batch, 
					maxBatchSize, 
					query.copy().setEntityName(entityNameEntry.getKey()), 
					candidateLanguageTags);
		}
	}
	
}
