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

import java.util.EnumSet;
import java.util.Objects;

final class ValueQueryBuilder
{
	private static final String JPQL_PATTERN = "ValueQueryBuilder.jpql";
	
	private final EnumSet<Token> remainingTokens;
	
	private boolean readOnly;
	private String queryPattern;
	
	public ValueQueryBuilder()
	{
		remainingTokens = EnumSet.allOf(Token.class);
		queryPattern = Utils.loadResource(
				ValueQueryBuilder.class, JPQL_PATTERN);
	}
	
	private ValueQueryBuilder(ValueQueryBuilder source)
	{
		remainingTokens = EnumSet.copyOf(
				source.remainingTokens);
		queryPattern = source.queryPattern;
	}
	
	private void setParamsToken(Token token, int start, int count)
	{
		setToken(token, Utils.createParameterPlaceholders(start, count));
	}
	
	private void setToken(Token token, String replacement)
	{
		if (readOnly)
		{
			throw new UnsupportedOperationException("read-only");
		}
		if (!remainingTokens.contains(token))
		{
			throw new IllegalStateException(
					"already set: " + token.tokenName);
		}
		queryPattern = queryPattern.replace(
				"${" + token.tokenName + "}", 
				Objects.requireNonNull(replacement));
		remainingTokens.remove(token);
	}
	
	public ValueQueryBuilder asReadOnly()
	{
		if (!readOnly)
		{
			readOnly = true;
		}
		return this;
	}
	
	public ValueQueryBuilder copy()
	{
		return new ValueQueryBuilder(this);
	}
	
	public ValueQueryBuilder setEntityName(String entityName)
	{
		setToken(Token.ENTITY_NAME, entityName);
		return this;
	}
	
	public ValueQueryBuilder setIdProperty(String idProperty)
	{
		setToken(Token.ID_PROPERTY, idProperty);
		return this;
	}
	
	public ValueQueryBuilder setLanguageLevelProperty(String languageLevelProperty)
	{
		setToken(Token.LANGUAGE_LEVEL_PROPERTY, languageLevelProperty);
		return this;
	}
	
	public ValueQueryBuilder setLanguageTagProperty(String languageTagProperty)
	{
		setToken(Token.LANGUAGE_TAG_PROPERTY, languageTagProperty);
		return this;
	}
	
	public ValueQueryBuilder setLocalizedProperty(String localizedProperty)
	{
		setToken(Token.LOCALIZED_PROPERTY, localizedProperty);
		return this;
	}
	
	public ValueQueryBuilder setValueProperty(String valueProperty)
	{
		setToken(Token.VALUE_PROPERTY, valueProperty);
		return this;
	}
	
	public ValueQueryBuilder setIdParams(int start, int count)
	{
		setParamsToken(Token.ID_PARAMS, start, count);
		return this;
	}
	
	public ValueQueryBuilder setLanguageTagParams(int start, int count)
	{
		setParamsToken(Token.LANGUAGE_TAG_PARAMS, start, count);
		return this;
	}

	public String build()
	{
		if (!remainingTokens.isEmpty())
		{
			throw new IllegalStateException(
					"query parameters not set: " + remainingTokens);
		}
		return queryPattern;
	}
	
	public void assertPropertiesSet()
	{
		EnumSet<Token> remainingProperties = EnumSet.noneOf(Token.class);
		for (Token token: remainingTokens)
		{
			if (token.property)
			{
				remainingProperties.add(token);
			}
		}
		if (!remainingProperties.isEmpty())
		{
			throw new IllegalStateException(
					remainingProperties + " must be set");
		}
	}
	
	private enum Token
	{
		ID_PROPERTY("idProperty", true),
		LOCALIZED_PROPERTY("localizedProperty", true),
		LANGUAGE_TAG_PROPERTY("languageTagProperty", true),
		LANGUAGE_LEVEL_PROPERTY("languageLevelProperty", true),
		VALUE_PROPERTY("valueProperty", true),
		ENTITY_NAME("entityName", true),
		LANGUAGE_TAG_PARAMS("languageTagParams", false),
		ID_PARAMS("idParams", false),
		;
		
		private final String tokenName;
		private final boolean property;

		private Token(String tokenName, boolean property)
		{
			this.tokenName = tokenName;
			this.property = property;
		}
		
		@Override
		public String toString()
		{
			return tokenName;
		}
	}
	
}
