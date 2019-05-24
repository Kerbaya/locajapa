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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import javax.persistence.Query;

import com.kerbaya.locajapa.ParameterQuerySupport.ParameterToken;
import com.kerbaya.locajapa.QuerySupport.Token;

final class QueryBuilder<V>
{
	private static final QueryBuilder<?> INSTANCE = 
			new QueryBuilder<>().asReadOnly();
	
	@SuppressWarnings("unchecked")
	public static final <V> QueryBuilder<V> getInstance()
	{
		return (QueryBuilder<V>) INSTANCE.copy();
	}
	
	private enum TokenImpl implements Token
	{
		ID_PROPERTY("idProperty"),
		LOCALIZED_PROPERTY("localizedProperty"),
		LANGUAGE_TAG_PROPERTY("languageTagProperty"),
		LANGUAGE_LEVEL_PROPERTY("languageLevelProperty"),
		VALUE_PROPERTIES("valueProperties"),
		ENTITY_NAME("entityName"),
		;
		
		private final String tokenName;
		private final String pattern;

		private TokenImpl(String tokenName)
		{
			this.tokenName = tokenName;
			pattern = "${" + tokenName + "}";
		}
		
		@Override
		public String getPattern()
		{
			return pattern;
		}
		
		@Override
		public String toString()
		{
			return tokenName;
		}
	}
	
	private enum ParameterTokenImpl implements ParameterToken
	{
		ID_PARAMS("idParams", "id"),
		LANGUAGE_TAG_PARAMS("languageTagParams", "lt"),
		;
		
		private final String tokenName;
		private final String pattern;
		private final String parameterPrefix;

		private ParameterTokenImpl(String tokenName, String parameterPrefix)
		{
			this.tokenName = tokenName;
			this.pattern = "${" + tokenName + "}";
			this.parameterPrefix = parameterPrefix;
		}
		
		@Override
		public String getParameterPrefix()
		{
			return parameterPrefix;
		}
		
		@Override
		public String getPattern()
		{
			return pattern;
		}
		
		@Override
		public String toString()
		{
			return tokenName;
		}
	}
	
	private static final int VALUE_QUERY_LOCALIZABLE_ID_IDX = 0;
	private static final int VALUE_QUERY_VALUE_IDX = 1;
	
	private static final int MAP_QUERY_LOCALIZABLE_ID_IDX = 0;
	private static final int MAP_QUERY_LANGUAGE_TAG_ID_IDX = 1;
	private static final int MAP_QUERY_VALUE_IDX = 2;
	
	private static final String LOCALIZED_ALIAS = "le";
	private static final int DEFAULT_MAX_BATCH_SIZE = 1000;
	private static final int INIT_MAX_BATCH_SIZE = -1;
	
	private final QuerySupport valueQuery;
	private final QuerySupport mapQuery;
	private int maxBatchSize;
	private Constructor<? extends V> valueCtor;
	
	private QueryBuilder()
	{
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[128];
		readQueryPattern(sb, buffer, "ValueQueryPattern.jpql");
		valueQuery = new QuerySupport(
				sb.toString(), EnumSet.allOf(TokenImpl.class));
		sb.setLength(0);
		readQueryPattern(sb, buffer, "MapQueryPattern.jpql");
		mapQuery = new QuerySupport(sb.toString(), EnumSet.of(
				TokenImpl.ENTITY_NAME,
				TokenImpl.ID_PROPERTY,
				TokenImpl.LANGUAGE_TAG_PROPERTY,
				TokenImpl.LOCALIZED_PROPERTY,
				TokenImpl.VALUE_PROPERTIES));
		maxBatchSize = INIT_MAX_BATCH_SIZE;
	}
	
	private QueryBuilder(QueryBuilder<V> source)
	{
		valueQuery = source.valueQuery.copy();
		mapQuery = source.mapQuery.copy();
		valueCtor = source.valueCtor;
		maxBatchSize = source.maxBatchSize;
	}
	
	private static void readQueryPattern(
			StringBuilder sb, char[] buffer, String resourceName)
	{
		try (InputStream is = 
						QueryBuilder.class.getResourceAsStream(resourceName);
				Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);)
		{
			int len;
			while ((len = in.read(buffer)) != -1)
			{
				sb.append(buffer, 0, len);
			}
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	public QueryBuilder<V> setEntityName(String entityName)
	{
		valueQuery.setToken(TokenImpl.ENTITY_NAME, entityName);
		mapQuery.setToken(TokenImpl.ENTITY_NAME, entityName);
		return this;
	}
	
	public QueryBuilder<V> setIdProperty(String idProperty)
	{
		valueQuery.setToken(TokenImpl.ID_PROPERTY, idProperty);
		mapQuery.setToken(TokenImpl.ID_PROPERTY, idProperty);
		return this;
	}
	
	public QueryBuilder<V> setLanguageLevelProperty(
			String languageLevelProperty)
	{
		valueQuery.setToken(
				TokenImpl.LANGUAGE_LEVEL_PROPERTY, languageLevelProperty);
		return this;
	}
	
	public QueryBuilder<V> setLanguageTagProperty(String languageTagProperty)
	{
		valueQuery.setToken(
				TokenImpl.LANGUAGE_TAG_PROPERTY, languageTagProperty);
		mapQuery.setToken(TokenImpl.LANGUAGE_TAG_PROPERTY, languageTagProperty);
		return this;
	}
	
	public QueryBuilder<V> setLocalizedProperty(String localizedProperty)
	{
		valueQuery.setToken(TokenImpl.LOCALIZED_PROPERTY, localizedProperty);
		mapQuery.setToken(TokenImpl.LOCALIZED_PROPERTY, localizedProperty);
		return this;
	}
	
	private static CharSequence createLocalizedPropertySequence(
			List<String> propertyNames)
	{
		StringBuilder sb = new StringBuilder();
		for (String propertyName: propertyNames)
		{
			sb.append(", ");
			sb.append(LOCALIZED_ALIAS);
			sb.append('.');
			sb.append(propertyName);
		}
		return sb;
	}
	
	public QueryBuilder<V> setValueProperty(String valueProperty)
	{
		CharSequence propertySequence = createLocalizedPropertySequence(
				Collections.singletonList(valueProperty));
		valueQuery.setToken(TokenImpl.VALUE_PROPERTIES, propertySequence);
		mapQuery.setToken(TokenImpl.VALUE_PROPERTIES, propertySequence);
		return this;
	}
	
	public QueryBuilder<V> setValueCtor(
			Constructor<? extends V> valueCtor, List<String> valueProperties)
	{
		if (valueCtor.getParameterTypes().length != valueProperties.size())
		{
			throw new IllegalArgumentException(
					"Constructor parameters / value properties count mismatch");
		}
		setValueProperties(valueCtor, valueProperties);
		return this;
	}
	
	private void setValueProperties(
			Constructor<? extends V> valueCtor, List<String> valueProperties)
	{
		CharSequence propertySequence = createLocalizedPropertySequence(
				valueProperties);
		valueQuery.setToken(TokenImpl.VALUE_PROPERTIES, propertySequence);
		mapQuery.setToken(TokenImpl.VALUE_PROPERTIES, propertySequence);
		this.valueCtor = valueCtor;
	}
	
	@SuppressWarnings("unchecked")
	public QueryBuilder<V> setValueCtor(
			Class<? extends V> type, List<String> valueProperties)
	{
		Constructor<? extends V> match = null;
		for (Constructor<?> ctor: type.getConstructors())
		{
			if (ctor.getParameterTypes().length == valueProperties.size())
			{
				if (match != null)
				{
					throw new IllegalArgumentException(
							"Multiple constructors matching property count");
				}
				match = (Constructor<? extends V>) ctor;
			}
		}
		if (match == null)
		{
			throw new IllegalArgumentException(
					"Could not find constructor with parameter count: " 
					+ valueProperties.size());
		}
		setValueProperties(match, valueProperties);
		return this;
	}
	
	public QueryBuilder<V> setMaxBatchSize(int maxBatchSize)
	{
		if (valueQuery.isReadOnly() || mapQuery.isReadOnly())
		{
			throw new IllegalStateException("read-only");
		}
		if (this.maxBatchSize != INIT_MAX_BATCH_SIZE)
		{
			throw new IllegalStateException("already set: maxBatchSize");
		}
		if (maxBatchSize < 1)
		{
			throw new IllegalArgumentException();
		}
		this.maxBatchSize = maxBatchSize;
		return this;
	}
	
	public boolean isReadOnly()
	{
		return valueQuery.isReadOnly() || mapQuery.isReadOnly();
	}
	
	public QueryBuilder<V> asReadOnly()
	{
		valueQuery.asReadOnly();
		mapQuery.asReadOnly();
		return this;
	}
	
	public QueryBuilder<V> copy()
	{
		return new QueryBuilder<>(this);
	}

	public QueryBuilder<V> copyAsReadOnly()
	{
		return isReadOnly() ? this : copy().asReadOnly();
	}
	
	public int getMaxBatchSize()
	{
		return maxBatchSize == INIT_MAX_BATCH_SIZE ? 
				DEFAULT_MAX_BATCH_SIZE : maxBatchSize;
	}
	
	@SuppressWarnings("unchecked")
	private static <V> V getValue(
			Constructor<? extends V> valueCtor, 
			Object[] queryResultRow, 
			int valueIdx)
	{
		if (valueCtor == null)
		{
			return (V) queryResultRow[valueIdx];
		}
		try
		{
			return valueCtor.newInstance(Arrays.copyOfRange(
					queryResultRow, 
					valueIdx, 
					queryResultRow.length));
		}
		catch (ReflectiveOperationException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	public static final class ValueQueryBuilder<V>
	{
		private final Constructor<? extends V> valueCtor;
		private final int maxBatchSize;
		private final ParameterQuerySupport querySupport;
		
		private ValueQueryBuilder(
				Constructor<? extends V> valueCtor, 
				int maxBatchSize, 
				String queryPattern)
		{
			this.valueCtor = valueCtor;
			this.maxBatchSize = maxBatchSize;
			querySupport = new ParameterQuerySupport(
					queryPattern, EnumSet.allOf(ParameterTokenImpl.class));
		}
		
		private ValueQueryBuilder(ValueQueryBuilder<V> source)
		{
			valueCtor = source.valueCtor;
			maxBatchSize = source.maxBatchSize;
			querySupport = source.querySupport.copy();
		}
		
		public int getMaxBatchSize()
		{
			return maxBatchSize;
		}
		
		public ValueQueryBuilder<V> addLanguageTagParams(int count)
		{
			querySupport.setToken(
					ParameterTokenImpl.LANGUAGE_TAG_PARAMS, count);
			return this;
		}
		
		public ValueQueryBuilder<V> addIdParams(int count)
		{
			querySupport.setToken(ParameterTokenImpl.ID_PARAMS, count);
			return this;
		}
		
		public static void setLanguageTagParams(
				Query query, Iterable<String> tagParams)
		{
			ParameterQuerySupport.setQueryParameters(
					query, ParameterTokenImpl.LANGUAGE_TAG_PARAMS, tagParams);
		}
		
		public static void setIdParams(Query query, Iterable<?> idParams)
		{
			ParameterQuerySupport.setQueryParameters(
					query, ParameterTokenImpl.ID_PARAMS, idParams);
		}
		
		public boolean isReadOnly()
		{
			return querySupport.isReadOnly();
		}
		
		public ValueQueryBuilder<V> asReadOnly()
		{
			querySupport.asReadOnly();
			return this;
		}
		
		public ValueQueryBuilder<V> copy()
		{
			return new ValueQueryBuilder<>(this);
		}
		
		public ValueQueryBuilder<V> copyAsReadOnly()
		{
			return isReadOnly() ? this : copy().asReadOnly();
		}
		
		public static Object getLocalizableId(Object[] queryResultRow)
		{
			return queryResultRow[VALUE_QUERY_LOCALIZABLE_ID_IDX];
		}
		
		public V getValue(Object[] queryResultRow)
		{
			return QueryBuilder.getValue(
					valueCtor, queryResultRow, VALUE_QUERY_VALUE_IDX);
		}
		
		public String createQueryText()
		{
			return querySupport.createQueryText();
		}
	}
	
	public static final class MapQueryBuilder<V>
	{
		private final Constructor<? extends V> valueCtor;
		private final int maxBatchSize;
		private final ParameterQuerySupport querySupport;
		
		private MapQueryBuilder(
				Constructor<? extends V> valueCtor, 
				int maxBatchSize, 
				String queryPattern)
		{
			this.valueCtor = valueCtor;
			this.maxBatchSize = maxBatchSize;
			querySupport = new ParameterQuerySupport(
					queryPattern, EnumSet.of(ParameterTokenImpl.ID_PARAMS));
		}
		
		private MapQueryBuilder(MapQueryBuilder<V> source)
		{
			valueCtor = source.valueCtor;
			maxBatchSize = source.maxBatchSize;
			querySupport = source.querySupport.copy();
		}
		
		public int getMaxBatchSize()
		{
			return maxBatchSize;
		}
		
		public MapQueryBuilder<V> addIdParams(int count)
		{
			querySupport.setToken(ParameterTokenImpl.ID_PARAMS, count);
			return this;
		}
		
		public static void setIdParams(Query q, Iterable<?> idParams)
		{
			ParameterQuerySupport.setQueryParameters(
					q, ParameterTokenImpl.ID_PARAMS, idParams);
		}
		
		public boolean isReadOnly()
		{
			return querySupport.isReadOnly();
		}
		
		public MapQueryBuilder<V> asReadOnly()
		{
			querySupport.asReadOnly();
			return this;
		}
		
		public MapQueryBuilder<V> copy()
		{
			return new MapQueryBuilder<>(this);
		}
		
		public MapQueryBuilder<V> copyAsReadOnly()
		{
			return isReadOnly() ? this : copy().asReadOnly();
		}
		
		public static Object getLocalizableId(Object[] queryResultRow)
		{
			return queryResultRow[MAP_QUERY_LOCALIZABLE_ID_IDX];
		}
		
		public static String getLanguageTag(Object[] queryResultRow)
		{
			return (String) queryResultRow[MAP_QUERY_LANGUAGE_TAG_ID_IDX];
		}
		
		public V getValue(Object[] queryResultRow)
		{
			return QueryBuilder.getValue(
					valueCtor, queryResultRow, MAP_QUERY_VALUE_IDX);
		}
		
		public String createQueryText()
		{
			return querySupport.createQueryText();
		}
	}
	
	
	public ValueQueryBuilder<V> createValueQueryBuilder()
	{
		return new ValueQueryBuilder<>(
				valueCtor, getMaxBatchSize(), valueQuery.createQueryText());
	}
	
	public MapQueryBuilder<V> createMapQueryBuilder()
	{
		return new MapQueryBuilder<>(
				valueCtor, getMaxBatchSize(), mapQuery.createQueryText());
	}

}
