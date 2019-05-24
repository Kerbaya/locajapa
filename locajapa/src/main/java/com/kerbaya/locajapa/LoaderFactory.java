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

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.kerbaya.locajapa.QueryBuilder.MapQueryBuilder;
import com.kerbaya.locajapa.QueryBuilder.ValueQueryBuilder;

/**
 * An immutable thread-safe factory that can create instances of 
 * {@link ValueLoader} and {@link MapLoader} for specific pair of localizable
 * and localized entity types
 * 
 * @author Glenn.Lane@kerbaya.com
 *
 * @param <T>
 * the localizable type handled by this factory
 * 
 * @param <V>
 * the localized value handled by this factory
 */
public final class LoaderFactory<T, V>
{
	/**
	 * <p>Creates an instance of {@link LoaderFactory}. An instance of 
	 * {@link LoaderFactory} must support either entity-handling, query-loading, 
	 * or both.</p>
	 * 
	 * <p>To create a {@link LoaderFactory} that supports entity-handling, only
	 * {@link #setEntityHandler(EntityHandler)} needs to be called.  To support
	 * query-loading, the following initialization methods must be called:</P.
	 * <ul>
	 * <li>{@link #setEntityName(String)}</li>
	 * <li>{@link #setIdProperty(String)}</li>
	 * <li>{@link #setLanguageLevelProperty(String)}</li>
	 * <li>{@link #setLocalizedProperty(String)}</li>
	 * <li>One of:<ul>
	 *     <li>{@link #setValueProperty(String)}</li>
	 *     <li>{@link #setValueCtor(Class, List)}</li>
	 *     <li>{@link #setValueCtor(Class, String...)}</li>
	 *     <li>{@link #setValueCtor(Constructor, List)}</li>
	 *     <li>{@link #setValueCtor(Constructor, String...)}</li>
	 * </li></ul>
	 * </ul>
	 * 
	 * <p>Create an instance of {@link Builder} with 
	 * {@link LoaderFactory#builder()}</p>
	 * 
	 * @author Glenn.Lane@kerbaya.com
	 *
	 * @param <T>
	 * The localizable type to be handled by the {@link LoaderFactory} instance
	 * 
	 * @param <V>
	 * The localized type to be handled by the {@link LoaderFactory} instance 
	 */
	public static final class Builder<T, V>
	{
		private QueryBuilder<V> queryBuilder;
		private EntityHandler<? super T, ?, ? extends V> entityHandler;

		private boolean readOnly;
		
		private Builder()
		{
		}
		
		private Builder(Builder<T, V> source)
		{
			queryBuilder = source.queryBuilder == null ? 
					null : source.queryBuilder.copy();
			entityHandler = source.entityHandler;
		}
		
		private QueryBuilder<V> getQueryBuilder()
		{
			if (queryBuilder == null)
			{
				assertWritable();
				queryBuilder = QueryBuilder.getInstance();
			}
			return queryBuilder;
		}
		
		/**
		 * Creates a writable duplicate of this instance
		 * 
		 * @return
		 * a writable duplicate of this instance
		 */
		public Builder<T, V> copy()
		{
			return new Builder<>(this);
		}
		
		/**
		 * Makes this instance read-only (any attempts to modify this instance
		 * afterward will throw {@link IllegalStateException}
		 * 
		 * @return
		 * this instance
		 */
		public Builder<T, V> asReadOnly()
		{
			if (!readOnly)
			{
				readOnly = true;
				if (queryBuilder != null)
				{
					queryBuilder.asReadOnly();
				}
			}
			return this;
		}
		
		/**
		 * Returns a read-only version of this instance, without causing this
		 * instance itself to become read-only (a copy is created if necessary)
		 * 
		 * @return
		 * A read-only version of this instance (may be the same instance if 
		 * this instance was already read-only)
		 */
		public Builder<T, V> copyAsReadOnly()
		{
			return readOnly ? this : copy().asReadOnly();
		}
		
		/**
		 * Sets the name of the entity ID property for the localizable entity
		 * (specifically: the JPQL property name that provides the localizable
		 * entity's ID)
		 * 
		 * @param idProperty
		 * the name of the entity ID property
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or {@link #setIdProperty(String)}
		 * was called previously on this instance.
		 */
		public Builder<T, V> setIdProperty(String idProperty)
		{
			getQueryBuilder().setIdProperty(idProperty);
			return this;
		}
		
		/**
		 * Specifies how the localized value should be constructed using the
		 * localized entity JPQL properties
		 *
		 * @param valueType
		 * The localized value type
		 * 
		 * @param valueProperties
		 * The localized entity properties that will be passed to the localized
		 * value type's constructor.
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalArgumentException
		 * thrown when any of the following apply:
		 * <ul>
		 * <li>{@code valueType} did not have a public constructor with a number
		 * of arguments matching the size of {@code valueProperties}</li>
		 * <li>{@code valueType} had more than one public constructor with a
		 * number of arguments matching the size of {@code valueProperties}</li>
		 * <ul>
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or one of the following methods
		 * were called previously on this instance:
		 * <ul>
		 * <li>{@link #setValueProperty(String)}</li>
		 * <li>{@link #setValueCtor(Class, List)}</li>
		 * <li>{@link #setValueCtor(Class, String...)}</li>
		 * <li>{@link #setValueCtor(Constructor, List)}</li>
		 * <li>{@link #setValueCtor(Constructor, String...)}</li>
		 * </ul>
		 */
		public Builder<T, V> setValueCtor(
				Class<? extends V> valueType, List<String> valueProperties)
		{
			getQueryBuilder().setValueCtor(valueType, valueProperties);
			return this;
		}
		
		/**
		 * Specifies how the localized value should be constructed using the
		 * localized entity JPQL properties
		 *
		 * @param valueType
		 * The localized value type
		 * 
		 * @param valueProperties
		 * The localized entity properties that will be passed to the localized
		 * value type's constructor.
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalArgumentException
		 * thrown when any of the following apply:
		 * <ul>
		 * <li>{@code valueType} did not have a public constructor with a number
		 * of arguments matching the length of {@code valueProperties}</li>
		 * <li>{@code valueType} had more than one public constructor with a
		 * number of arguments matching the length of 
		 * {@code valueProperties}</li>
		 * <ul>
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or one of the following methods
		 * were called previously on this instance:
		 * <ul>
		 * <li>{@link #setValueProperty(String)}</li>
		 * <li>{@link #setValueCtor(Class, List)}</li>
		 * <li>{@link #setValueCtor(Class, String...)}</li>
		 * <li>{@link #setValueCtor(Constructor, List)}</li>
		 * <li>{@link #setValueCtor(Constructor, String...)}</li>
		 * </ul>
		 */
		public Builder<T, V> setValueCtor(
				Class<? extends V> valueType, String... valueProperties)
		{
			return setValueCtor(valueType, Arrays.asList(valueProperties));
		}
		
		/**
		 * Specifies how the localized value should be constructed using the
		 * localized entity JPQL properties
		 *
		 * @param valueCtor
		 * The localized value constructor
		 * 
		 * @param valueProperties
		 * The localized entity properties that will be passed to the localized
		 * value constructor
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalArgumentException
		 * thrown when {@code valueCtor} has a number of arguments that does not 
		 * match the size of {@code valueProperties}
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or one of the following methods
		 * were called previously on this instance:
		 * <ul>
		 * <li>{@link #setValueProperty(String)}</li>
		 * <li>{@link #setValueCtor(Class, List)}</li>
		 * <li>{@link #setValueCtor(Class, String...)}</li>
		 * <li>{@link #setValueCtor(Constructor, List)}</li>
		 * <li>{@link #setValueCtor(Constructor, String...)}</li>
		 * </ul>
		 */
		public Builder<T, V> setValueCtor(
				Constructor<? extends V> valueCtor, 
				List<String> valueProperties)
		{
			getQueryBuilder().setValueCtor(valueCtor, valueProperties);
			return this;
		}
		
		/**
		 * Specifies how the localized value should be constructed using the
		 * localized entity JPQL properties
		 *
		 * @param valueCtor
		 * The localized value constructor
		 * 
		 * @param valueProperties
		 * The localized entity properties that will be passed to the localized
		 * value constructor
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalArgumentException
		 * thrown when {@code valueCtor} has a number of arguments that does not 
		 * match the length of {@code valueProperties}
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or one of the following methods
		 * were called previously on this instance:
		 * <ul>
		 * <li>{@link #setValueProperty(String)}</li>
		 * <li>{@link #setValueCtor(Class, List)}</li>
		 * <li>{@link #setValueCtor(Class, String...)}</li>
		 * <li>{@link #setValueCtor(Constructor, List)}</li>
		 * <li>{@link #setValueCtor(Constructor, String...)}</li>
		 * </ul>
		 */
		public Builder<T, V> setValueCtor(
				Constructor<? extends V> valueCtor, String... valueProperties)
		{
			return setValueCtor(valueCtor, Arrays.asList(valueProperties));
		}
		
		/**
		 * Specifies which localized entity JPQL property will be used as the 
		 * localized value
		 * 
		 * @param valueProperty
		 * the localized entity JPQL property that will be used as the localized 
		 * value
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or one of the following methods
		 * were called previously on this instance:
		 * <ul>
		 * <li>{@link #setValueProperty(String)}</li>
		 * <li>{@link #setValueCtor(Class, List)}</li>
		 * <li>{@link #setValueCtor(Class, String...)}</li>
		 * <li>{@link #setValueCtor(Constructor, List)}</li>
		 * <li>{@link #setValueCtor(Constructor, String...)}</li>
		 * </ul>
		 */
		public Builder<T, V> setValueProperty(String valueProperty)
		{
			getQueryBuilder().setValueProperty(valueProperty);
			return this;
		}
		
		/**
		 * Specifies the localizable entity JPQL property that holds the 
		 * {@code @OneToMany} association to its localized entities
		 * 
		 * @param localizedProperty
		 * the localizable entity JPQL property that holds the 
		 * {@code @OneToMany} association to its localized entities
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or this method was already called
		 * previously
		 */
		public Builder<T, V> setLocalizedProperty(String localizedProperty)
		{
			getQueryBuilder().setLocalizedProperty(localizedProperty);
			return this;
		}
		
		/**
		 * Specifies the localized entity JPQL property for the entry's language 
		 * tag
		 * 
		 * @param languageTagProperty
		 * the localized entity JPQL property for the entry's language tag
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or this method was already called
		 * previously
		 */
		public Builder<T, V> setLanguageTagProperty(
				String languageTagProperty)
		{
			getQueryBuilder().setLanguageTagProperty(languageTagProperty);
			return this;
		}
		
		/**
		 * Specifies the localized entity JPQL property for the entry's language 
		 * level
		 * 
		 * @param languageLevelProperty
		 * the localized entity JPQL property for the entry's language level
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or this method was already called
		 * previously
		 */
		public Builder<T, V> setLanguageLevelProperty(
				String languageLevelProperty)
		{
			getQueryBuilder().setLanguageLevelProperty(languageLevelProperty);
			return this;
		}
		
		/**
		 * Specifies the JPQL entity name of the localizable entity
		 * 
		 * @param entityName
		 * the JPQL entity name of the localizable entity
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or this method was already called
		 * previously
		 */
		public Builder<T, V> setEntityName(String entityName)
		{
			getQueryBuilder().setEntityName(entityName);
			return this;
		}
		
		private void assertWritable()
		{
			if (readOnly)
			{
				throw new IllegalStateException("read-only");
			}
		}
		
		/**
		 * Configures the {@link LoaderFactory} for entity-handling, so that
		 * {@link ValueLoader#getRef(Object)} and 
		 * {@link ValueLoader#getValue(Object)} are supported.
		 * 
		 * @param entityHandler
		 * the entity handler
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or this method was already called
		 * previously
		 */
		public Builder<T, V> setEntityHandler(
				EntityHandler<? super T, ?, ? extends V> entityHandler)
		{
			assertWritable();
			if (this.entityHandler != null)
			{
				throw new IllegalStateException("already set: entityHandler");
			}
			this.entityHandler = Objects.requireNonNull(entityHandler);
			return this;
		}
		
		/**
		 * Specifies the maximum number of localizable entities that will be 
		 * loaded per query execution.  Default value is 1000
		 * 
		 * @param maxBatchSize
		 * the maximum number of localizable entities that will be loaded per 
		 * query execution
		 * 
		 * @return
		 * this instance
		 * 
		 * @throws IllegalStateException
		 * Either the instance is read-only, or this method was already called
		 * previously
		 * 
		 * @throws IllegalArgumentException
		 * The provided {@code maxBatchSize} was zero or a negative number
		 */
		public Builder<T, V> setMaxBatchSize(int maxBatchSize)
		{
			getQueryBuilder().setMaxBatchSize(maxBatchSize);
			return this;
		}
		
		/**
		 * Creates the {@link LoaderFactory} instance
		 * 
		 * @return
		 * the {@link LoaderFactory} instance
		 * 
		 * @throws IllegalStateException
		 * thrown when any of the following apply:
		 * <ul>
		 * <li>The builder is neither configured for entity-handling nor 
		 * query-loading</li>
		 * <li>The builder was partially configured for query-loading, some
		 * query-loading setters were called, but not all that were required<li>
		 * </ul>
		 */
		public LoaderFactory<T, V> build()
		{
			if (queryBuilder == null && entityHandler == null)
			{
				throw new IllegalStateException("incomplete configuration");
			}
			return new LoaderFactory<>(
					entityHandler, 
					queryBuilder);
		}
	}
	
	/**
	 * Creates a new instance of {@link Builder}
	 * 
	 * @return
	 * a new instance of {@link Builder}
	 */
	public static <T, V> Builder<T, V> builder()
	{
		return new Builder<>();
	}
	
	private final EntityHandler<? super T, ?, ? extends V> entityHandler;
	private final ValueQueryBuilder<V> vqb;
	private final MapQueryBuilder<V> mqb;
	
	private LoaderFactory(
			EntityHandler<? super T, ?, ? extends V> entityHandler,
			QueryBuilder<V> queryBuilder)
	{
		this.entityHandler = entityHandler;
		if (queryBuilder == null)
		{
			vqb = null;
			mqb = null;
		}
		else
		{
			vqb = queryBuilder.createValueQueryBuilder().asReadOnly();
			mqb = queryBuilder.createMapQueryBuilder().asReadOnly();
		}
	}
	
	/**
	 * Creates a new instance of {@link ValueLoader} for a specified locale
	 * 
	 * @param locale
	 * the locale for which the new {@link ValueLoader} instance will return
	 * values/references
	 * 
	 * @return
	 * a new instance of {@link ValueLoader}
	 */
	public ValueLoader<T, V> createValueLoader(Locale locale)
	{
		return new ValueLoaderImpl<>(locale, entityHandler, vqb);
	}
	
	/**
	 * Creates a new instance of {@link MapLoader}
	 * 
	 * @return
	 * a new instance of {@link MapLoader}
	 */
	public MapLoader<T, V> createMapLoader()
	{
		return new MapLoaderImpl<>(entityHandler, mqb);
	}
}
