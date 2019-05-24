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

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

final class QuerySupport
{
	protected interface Token
	{
		String getPattern();
		
		@Override
		String toString();
	}
	
	private final Set<? extends Token> remainingTokens;
	
	private String queryPattern;
	
	private boolean readOnly;
	
	public QuerySupport(
			String queryPattern, Collection<? extends Token> initialTokens)
	{
		remainingTokens = new HashSet<>(initialTokens);
		this.queryPattern = Objects.requireNonNull(queryPattern);
	}
	
	private QuerySupport(QuerySupport source)
	{
		remainingTokens = new HashSet<>(source.remainingTokens);
		queryPattern = source.queryPattern;
	}
	
	public QuerySupport asReadOnly()
	{
		if (!readOnly)
		{
			readOnly = true;
		}
		return this;
	}
	
	public boolean isReadOnly()
	{
		return readOnly;
	}
	
	public QuerySupport copy()
	{
		return new QuerySupport(this);
	}
	
	public QuerySupport copyAsReadOnly()
	{
		return readOnly ? this : copy().asReadOnly();
	}
	
	private void assertWritable()
	{
		if (readOnly)
		{
			throw new IllegalStateException("read-only");
		}
	}
	
	public void setToken(Token token, CharSequence replacement)
	{
		Objects.requireNonNull(replacement);
		assertWritable();
		if (!remainingTokens.remove(token))
		{
			throw new IllegalStateException("already set: " + token);
		}
		queryPattern = queryPattern.replace(token.getPattern(), replacement);
	}
	
	public final String createQueryText()
	{
		if (!remainingTokens.isEmpty())
		{
			throw new IllegalStateException(
					"missing properties: " + remainingTokens);
		}
		return queryPattern;
	}
}
