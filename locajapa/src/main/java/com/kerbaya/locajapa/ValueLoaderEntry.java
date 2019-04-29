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
import java.util.Set;

import javax.persistence.EntityNotFoundException;

final class ValueLoaderEntry<V> implements Supplier<V>, Serializable
{

	private static final long serialVersionUID = -3737835233743967663L;

	protected Resolver<V> preLoadResolver;
	protected V value;
	protected boolean exists;
	
	public ValueLoaderEntry()
	{
		preLoadResolver = NonResolvable.instance();
	}
	
	public ValueLoaderEntry(
			Set<String> candidateLanguageTags, 
			Localizable<? extends V> localizable)
	{
		preLoadResolver = new LazyValueResolver<>(
				candidateLanguageTags, localizable);
	}

	@Override
	public V get()
	{
		if (preLoadResolver != null)
		{
			try
			{
				value = preLoadResolver.get();
				exists = true;
			}
			catch (EntityNotFoundException e)
			{
				value = null;
				exists = false;
			}
		}
		if (!exists)
		{
			throw new EntityNotFoundException();
		}
		return value;
	}
	
	public boolean isLoaded()
	{
		return preLoadResolver == null;
	}
	
	@SuppressWarnings("unchecked")
	public void set(Object value)
	{
		assert preLoadResolver != null;
		this.value = (V) value;
		exists = true;
		preLoadResolver = null;
	}
	public void setNotExists()
	{
		assert preLoadResolver != null;
		value = null;
		exists = false;
		preLoadResolver = null;
	}

}
