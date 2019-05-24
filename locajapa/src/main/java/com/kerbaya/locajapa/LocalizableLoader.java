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

import javax.persistence.Entity;
import javax.persistence.EntityManager;

import com.kerbaya.locajapa.LoaderFactory.Builder;

/**
 * Provides localized value and map loading for instances of {@link Localizable}
 * 
 * @author Glenn.Lane@kerbaya.com
 *
 */
public class LocalizableLoader
{
	private static final LoaderFactory.Builder<Localizable<?>, ?> BUILDER = 
			LoaderFactory.<Localizable<?>, Object>builder()
					.setIdProperty("id")
					.setLanguageLevelProperty("languageLevel")
					.setLanguageTagProperty("languageTag")
					.setLocalizedProperty("localized")
					.setValueProperty("value")
					.setEntityHandler(LocalizableEntityHandler.INSTANCE)
					.asReadOnly();

	private final Map<Class<?>, LoaderFactory<?, ?>> factoryMap = 
			new HashMap<>();
	
	private Map<Class<?>, MapLoader<?, ?>> mapLoaderMap;
	private Map<Class<?>, ValueLoader<?, ?>> valueLoaderMap;
	
	private final Locale locale;
	private final LoaderFactory.Builder<?, ?> builder;
	
	/**
	 * Short-hand for:
	 * <pre>new LocalizableLoader(Locale.ROOT)</pre>
	 */
	public LocalizableLoader()
	{
		this(Locale.ROOT);
	}
	
	/**
	 * Creates an instance for a specified locale
	 * 
	 * @param locale
	 * the locale for which to generate localized values
	 */
	public LocalizableLoader(Locale locale)
	{
		this.locale = locale;
		builder = BUILDER.copyAsReadOnly();
	}
	
	/**
	 * Creates an instance for a specified locale and batch size
	 * 
	 * @param locale
	 * the locale for which to generate localized values
	 * 
	 * @param maxBatchSize
	 * the maximum number of localizable entities that will be included in one
	 * query
	 */
	public LocalizableLoader(Locale locale, int maxBatchSize)
	{
		this.locale = locale;
		builder = BUILDER.copy().setMaxBatchSize(maxBatchSize).asReadOnly();
	}
	
	private static Class<?> getEntityType(Class<?> localizableType)
	{
		Class<?> entityType = localizableType;
		Entity anno = null;
		do
		{
			anno = entityType.getAnnotation(Entity.class);
			if (anno != null)
			{
				break;
			}
		} while ((entityType = entityType.getSuperclass()) != null);
		
		if (anno == null)
		{
			throw new IllegalArgumentException(
					"@Entity not found on " + localizableType);
		}
		return entityType;
	}
	
	@SuppressWarnings("unchecked")
	private <V> LoaderFactory<Localizable<? extends V>, V> getLoaderFactory(
			Class<?> entityType)
	{
		LoaderFactory<Localizable<? extends V>, V> loaderFactory = 
				(LoaderFactory<Localizable<? extends V>, V>) factoryMap.get(
						entityType);
		if (loaderFactory != null)
		{
			return loaderFactory;
		}
		String entityName = entityType.getAnnotation(Entity.class).name();
		loaderFactory = ((Builder<Localizable<? extends V>, V>) builder).copy()
				.setEntityName(entityName.isEmpty() ? 
						entityType.getSimpleName() : entityName)
				.build();
		factoryMap.put(entityType, loaderFactory);
		return loaderFactory;
	}
	
	@SuppressWarnings("unchecked")
	private <V> MapLoader<Localizable<? extends V>, V> getMapLoader(
			Class<?> localizableType)
	{
		Class<?> entityType = getEntityType(localizableType);
		MapLoader<Localizable<? extends V>, V> mapLoader;
		if (mapLoaderMap == null)
		{
			mapLoaderMap = new HashMap<>();
		}
		else
		{
			mapLoader = 
					(MapLoader<Localizable<? extends V>, V>) mapLoaderMap.get(
							entityType);
			if (mapLoader != null)
			{
				return mapLoader;
			}
		}
		LoaderFactory<Localizable<? extends V>, V> loaderFactory = 
				getLoaderFactory(entityType);
		mapLoader = loaderFactory.createMapLoader();
		mapLoaderMap.put(entityType, mapLoader);
		return mapLoader;
	}
	
	@SuppressWarnings("unchecked")
	private <V> ValueLoader<Localizable<? extends V>, V> getValueLoader(
			Class<?> localizableType)
	{
		Class<?> entityType = getEntityType(localizableType);
		ValueLoader<Localizable<? extends V>, V> valueLoader;
		if (valueLoaderMap == null)
		{
			valueLoaderMap = new HashMap<>();
		}
		else
		{
			valueLoader = (ValueLoader<Localizable<? extends V>, V>) 
					valueLoaderMap.get(entityType);
			if (valueLoader != null)
			{
				return valueLoader;
			}
		}
		LoaderFactory<Localizable<? extends V>, V> factory = getLoaderFactory(
				entityType);
		valueLoader = factory.createValueLoader(locale);
		valueLoaderMap.put(entityType, valueLoader);
		return valueLoader;
	}
	
	/**
	 * returns a lazily generated reference to the localized value for the 
	 * provided {@code localizable} and this instance's locale
	 * 
	 * @param localizable
	 * the localizable for which to generate the reference
	 * 
	 * @return
	 * a lazily generated reference to the localized value for the provided 
	 * {@code localizable} and this instance's locale.  returns {@code null}
	 * if the provided {@code localizable} is {@code null}
	 * 
	 */
	public <V> ValueReference<V> getRef(
			Localizable<? extends V> localizable)
	{
		if (localizable == null)
		{
			return null;
		}
		return this.<V>getValueLoader(localizable.getClass()).getRef(
				localizable);
	}
	
	/**
	 * returns a lazily generated reference to the localized value for the 
	 * localizable of the provided {@code id} and this instance's locale
	 * 
	 * @param type
	 * the localizable type
	 * 
	 * @param id
	 * the entity ID for which to generate the reference
	 * 
	 * @return
	 * a lazily generated reference to the localized value for the localizable 
	 * of the provided {@code id} and this instance's locale.  Returns 
	 * {@code null} if the provided {@code id} is {@code null}.
	 */
	public <T extends Localizable<? extends V>, V> ValueReference<V> getRef(
			Class<T> type, Object id)
	{
		if (id == null)
		{
			return null;
		}
		return this.<V>getValueLoader(type).getRefById(id);
	}
	
	/**
	 * Returns the localized value for a provided localizable
	 * 
	 * @param localizable
	 * the localizable for which to return a localized value
	 * 
	 * @return
	 * <p>the localized value for a provided localizable.  {@code null} is 
	 * returned when either:</p>
	 * <ul>
	 * <li>there was no value specified for the provided {@code localizable} for
	 * this instance's locale</li>
	 * <li>specified for the provided {@code localizable} was {@code null} for
	 * this instance's local</li>
	 * </ul>
	 * 
	 * @throws NullPointerException
	 * {@code localizable} was {@code null}
	 */
	public <T extends Localizable<? extends V>, V> V getValue(T localizable)
	{
		return this.<V>getValueLoader(localizable.getClass()).getValue(
				localizable);
	}
	
	public <T extends Localizable<? extends V>, V> Map<Locale, V> getMap(
			T localizable)
	{
		if (localizable == null)
		{
			return null;
		}
		return this.<V>getMapLoader(localizable.getClass()).getMap(localizable);
	}
	
	public <T extends Localizable<? extends V>, V> Map<Locale, V> getMap(
			Class<T> type, Object id)
	{
		if (id == null)
		{
			return null;
		}
		return this.<V>getMapLoader(type).getMapById(id);
	}
	
	public void load(EntityManager em)
	{
		if (valueLoaderMap != null)
		{
			for (ValueLoader<?, ?> valueLoader: valueLoaderMap.values())
			{
				valueLoader.load(em);
			}
		}
		
		if (mapLoaderMap != null)
		{
			for (MapLoader<?, ?> mapLoader: mapLoaderMap.values())
			{
				mapLoader.load(em);
			}
		}
	}
	
}
