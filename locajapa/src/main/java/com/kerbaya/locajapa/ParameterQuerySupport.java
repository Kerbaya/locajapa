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

import javax.persistence.Query;

import com.kerbaya.locajapa.QuerySupport.Token;

final class ParameterQuerySupport
{
	public interface ParameterToken extends Token
	{
		String getParameterPrefix();
	}
	
	private final QuerySupport querySupport;
	
	public ParameterQuerySupport(
			String queryPattern,
			Collection<? extends ParameterToken> initialTokens)
	{
		querySupport = new QuerySupport(queryPattern, initialTokens);
	}
	
	private ParameterQuerySupport(ParameterQuerySupport source)
	{
		querySupport = source.querySupport.copy();
	}
	
	public boolean isReadOnly()
	{
		return querySupport.isReadOnly();
	}
	
	public ParameterQuerySupport asReadOnly()
	{
		querySupport.asReadOnly();
		return this;
	}
	
	public ParameterQuerySupport copy()
	{
		return new ParameterQuerySupport(this);
	}
	
	public ParameterQuerySupport copyAsReadOnly()
	{
		return querySupport.isReadOnly() ? this : copy().asReadOnly();
	}
	
	public void setToken(ParameterToken token, int paramCount)
	{
		if (paramCount < 1)
		{
			throw new IllegalArgumentException();
		}
		StringBuilder replacement = new StringBuilder();
		for (int i = 0; i < paramCount; i++)
		{
			if (i != 0)
			{
				replacement.append(", ");
			}
			replacement.append(':');
			replacement.append(token.getParameterPrefix());
			replacement.append(i);
		}
		querySupport.setToken(token, replacement);
	}
	
	public static void setQueryParameters(
			Query q, ParameterToken pt, Iterable<?> parameters)
	{
		int idx = 0;
		for (Object value: parameters)
		{
			q.setParameter(pt.getParameterPrefix() + (idx++), value);
		}
	}
	
	public String createQueryText()
	{
		return querySupport.createQueryText();
	}
}
