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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Provides translation of {@link Localizable} instances into maps that 
 * associate Locale to localized values.  Supports batch loading of multiple 
 * instances in one query
 * 
 * @author Glenn.Lane@kerbaya.com
 *
 */
public class MapLoader
{
	/**
	 * The default maximum number of instances to load per query
	 */
	public static final int DEFAULT_BATCH_SIZE = 1000;

	private static final Memoization<String> PAREN_JPQL_PATTERN = 
			new JpqlGenerator(true);
	private static final Memoization<String> NON_PAREN_JPQL_PATTERN = 
			new JpqlGenerator(false);
	
	private static final String ID_SET_PARAM_TOKEN = "${idSetParam}";
	private static final String ENTITY_NAME_TOKEN = "${entityName}";
	private static final String ID_SET_PARAM = "idSet";
	
	private final Map<String, Map<Object, MapLoaderEntry<?>>> entityNameMap = 
			new HashMap<>();
	
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
			String paramStr = ":" + ID_SET_PARAM;
			if (wrapParen)
			{
				paramStr = "(" + paramStr + ")";
			}
			return Utils.loadResource(MapLoader.class, "MapLoader.jpql")
					.replace(ID_SET_PARAM_TOKEN, paramStr);
		}
	}
	
	private final EntityNameResolver entityNameResolver;
	private final boolean nullAsEmptyMap;
	private final int batchSize;
	
	public MapLoader()
	{
		this(null, false, 0);
	}
	
	public MapLoader(
			EntityNameResolver enr, boolean nullAsEmptyMap, int batchSize)
	{
		this.entityNameResolver = enr == null ? 
				EntityNameResolver.DEFAULT : enr;
		this.nullAsEmptyMap = nullAsEmptyMap;
		this.batchSize = batchSize <= 0 ? DEFAULT_BATCH_SIZE : batchSize;
	}
	
	private <V> Map<Locale, V> getMap(
			Class<?> type, Object id, Localizable<? extends V> instance)
	{
		String entityName = entityNameResolver.resolveEntityName(type);
		if (entityName == null)
		{
			throw new IllegalArgumentException(
					"Could not resolve entity name: " + type);
		}
		
		Map<Object, MapLoaderEntry<?>> entityIdMap = entityNameMap.get(entityName);
		
		if (entityIdMap == null)
		{
			entityIdMap = new HashMap<>();
			entityNameMap.put(entityName, entityIdMap);
		}
		else
		{
			@SuppressWarnings("unchecked")
			MapLoaderEntry<V> entry = (MapLoaderEntry<V>) entityIdMap.get(id);
			if (entry != null)
			{
				return entry;
			}
		}
		
		MapLoaderEntry<V> entry = instance == null ? 
				new MapLoaderEntry<V>() : new MapLoaderEntry<V>(instance);
		entityIdMap.put(id, entry);
		return entry;
	}

	/**
	 * Translates a provided {@link Localizable} instance into a map.  Note that
	 * the returned map is an unmodifiable view of the {@link Localizable} 
	 * instance.  The collection required to produce map data will only be
	 * loaded when necessary (when calling {@link Map#size()} or iterating 
	 * entries for example).  All maps returned by this instance may be 
	 * loaded in a single query later, if load-triggering is avoided before 
	 * calling {@link #load(EntityManager)}.
	 * 
	 * @param localizable
	 * The instance to translate
	 * 
	 * @param <V>
	 * The localized value type
	 * 
	 * @return
	 * the translated map for the provided localizable.  returns {@code null} if
	 * the provided {@code localizable} is {@code null}
	 */
	public <V> Map<Locale, V> getMap(Localizable<? extends V> localizable)
	{
		if (localizable == null)
		{
			return nullAsEmptyMap ? Collections.<Locale, V>emptyMap() : null;
		}
		
		return getMap(localizable.getClass(), localizable.getId(), localizable);
	}
	
	/**
	 * Translates a reference to a {@link Localizable} into a map.  Note that if
	 * any methods from the returned map are called prior to calling 
	 * {@link #load(EntityManager)} on this instance, those map methods will 
	 * throw {@link IllegalStateException}
	 * 
	 * @param type
	 * the entity type
	 * 
	 * @param id
	 * the entity ID
	 * 
	 * @param <V>
	 * The localized value type
	 * 
	 * @return
	 * the translated map for the provided localizable.  If {@code id} is 
	 * {@code null} and {@code nullAsEmptyMap} is {@code true}, returns an empty
	 * map.  If {@code id} is {@code null} and {@code nullAsEmptyMap} is 
	 * {@code false}, returns {@code null} (see 
	 * {@link #MapLoader(EntityNameResolver, boolean, int)} regarding behavior
	 * of {@code nullAsEmptyMap} behavior)
	 */
	public <V> Map<Locale, V> getMap(
			Class<? extends Localizable<? extends V>> type, Object id)
	{
		if (id == null)
		{
			return nullAsEmptyMap ? Collections.<Locale, V>emptyMap() : null;
		}
		
		return getMap(type, id, null);
	}
	
	/**
	 * Loads all maps previously returned by {@link #getMap(Localizable)} in
	 * one query
	 * 
	 * @param em
	 * The entity manager on which the query will be executed
	 */
	public void load(EntityManager em)
	{
		final String queryPattern = Utils.wrapColParam(em) ? 
				PAREN_JPQL_PATTERN.get()
				: NON_PAREN_JPQL_PATTERN.get();
		Map<Object, MapLoaderEntry<?>> batch = new HashMap<>();
		for (Entry<String, Map<Object, MapLoaderEntry<?>>> entityNameEntry: 
				entityNameMap.entrySet())
		{
			Query q = em.createQuery(queryPattern.replace(
					ENTITY_NAME_TOKEN, entityNameEntry.getKey()));
			Iterator<Entry<Object, MapLoaderEntry<?>>> entityEntryIter = 
					entityNameEntry.getValue().entrySet().iterator();
			do
			{
				Entry<Object, MapLoaderEntry<?>> next = entityEntryIter.next();
				MapLoaderEntry<?> mapView = next.getValue();
				if (mapView.isLoaded())
				{
					continue;
				}
				batch.put(next.getKey(), mapView);
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
	
	private static void flushBatch(Query q, Map<Object, MapLoaderEntry<?>> batch)
	{
		@SuppressWarnings("unchecked")
		List<Object[]> queryResults = 
				q.setParameter(ID_SET_PARAM, batch.keySet())
						.getResultList();
		for (Object[] queryResult: queryResults)
		{
			MapLoaderEntry<?> entry = batch.get(queryResult[0]);
			if (entry != null)
			{
				if (queryResult[1] == null)
				{
					entry.setEmpty();
				}
				else
				{
					entry.addFromBatch(
							Locale.forLanguageTag((String) queryResult[1]),
							queryResult[2]);
				}
			}
		}
		
		for (MapLoaderEntry<?> batchEntry: batch.values())
		{
			batchEntry.finalizeBatch();
		}
		
		batch.clear();
	}
	
}
