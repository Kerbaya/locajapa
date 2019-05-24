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

import java.io.Serializable;

import javax.persistence.EntityNotFoundException;

final class ValueLoaderEntry<V> implements ValueReference<V>, Serializable
{

	private static final long serialVersionUID = -3737835233743967663L;

	protected Resolver<V> preLoadResolver;
	protected V value;
	
	public ValueLoaderEntry(Resolver<V> preLoadResolver)
	{
		this.preLoadResolver = preLoadResolver;
	}

	@Override
	public V get()
	{
		if (preLoadResolver != null)
		{
			try
			{
				value = preLoadResolver.get();
			}
			catch (EntityNotFoundException e)
			{
				value = null;
			}
			preLoadResolver = null;
		}
		return value;
	}
	
	public boolean isLoaded()
	{
		return preLoadResolver == null;
	}
	
	public void set(V value)
	{
		assert preLoadResolver != null;
		this.value = value;
		preLoadResolver = null;
	}
}
