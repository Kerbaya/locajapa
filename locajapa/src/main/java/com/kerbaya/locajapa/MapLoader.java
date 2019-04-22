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
	private static final String JPQL_PATTERN = Utils.loadResource(
			MapLoader.class, "MapLoader.jpql");
	private static final String ENTITY_NAME_TOKEN = "${entityName}";
	private static final String ID_SET_PARAM = "idSet";
	private static final int MAX_BATCH_SIZE = 1000;
	
	private final Map<String, Map<Object, MapImpl<?>>> entityNameMap = 
			new HashMap<>();
	
	private final EntityNameResolver entityNameResolver;
	
	public MapLoader()
	{
		this(EntityNameResolver.DEFAULT);
	}
	
	public MapLoader(EntityNameResolver enr)
	{
		this.entityNameResolver = enr;
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
	 * @return
	 * the translated map for the provided localizable.  returns {@code null} if
	 * the provided {@code localizable} is {@code null}
	 */
	public <V> Map<Locale, V> getMap(Localizable<? extends V> localizable)
	{
		if (localizable == null)
		{
			return null;
		}
		
		String entityName = entityNameResolver.resolveEntityName(localizable.getClass());
		if (entityName == null)
		{
			throw new IllegalArgumentException(
					"Could not resolve entity name: " + localizable);
		}
		
		Object entityId = localizable.getId();
		Map<Object, MapImpl<?>> entityIdMap = entityNameMap.get(entityName);
		
		if (entityIdMap == null)
		{
			entityIdMap = new HashMap<>();
			entityNameMap.put(entityName, entityIdMap);
		}
		else
		{
			@SuppressWarnings("unchecked")
			MapImpl<V> entry = (MapImpl<V>) entityIdMap.get(entityId);
			if (entry != null)
			{
				return entry;
			}
		}
		
		MapImpl<V> entry = new MapImpl<>(localizable);
		entityIdMap.put(entityId, entry);
		return entry;
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
		Map<Object, MapImpl<?>> batch = new HashMap<>();
		for (Entry<String, Map<Object, MapImpl<?>>> entityNameEntry: 
				entityNameMap.entrySet())
		{
			Query q = em.createQuery(JPQL_PATTERN.replace(
					ENTITY_NAME_TOKEN, entityNameEntry.getKey()));
			Iterator<Entry<Object, MapImpl<?>>> entityEntryIter = 
					entityNameEntry.getValue().entrySet().iterator();
			do
			{
				Entry<Object, MapImpl<?>> next = entityEntryIter.next();
				MapImpl<?> mapView = next.getValue();
				if (mapView.isLoaded())
				{
					continue;
				}
				batch.put(next.getKey(), mapView);
				if (batch.size() == MAX_BATCH_SIZE)
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
	
	private static void flushBatch(Query q, Map<Object, MapImpl<?>> batch)
	{
		@SuppressWarnings("unchecked")
		List<? extends Localizable<?>> queryResults = 
				q.setParameter(ID_SET_PARAM, batch.keySet())
						.getResultList();
		for (Localizable<?> queryResult: queryResults)
		{
			batch.remove(queryResult.getId()).setLocalized(
					queryResult.getLocalized());
		}
		
		for (MapImpl<?> batchEntry: batch.values())
		{
			batchEntry.setLocalized(null);
		}
		
		batch.clear();
	}
	
}
