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
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

final class MapLoaderEntry<V> extends ForwardingMap<Locale, V>
{
	private static final long serialVersionUID = -974408044066794976L;

	private Resolver<Map<Locale, V>> preLoadResolver;
	private Map<Locale, V> map;
	
	public MapLoaderEntry(Resolver<Map<Locale, V>> preLoadResolver)
	{
		this.preLoadResolver = preLoadResolver;
	}
	
	@Override
	protected Map<Locale, V> delegate()
	{
		if (preLoadResolver != null)
		{
			try
			{
				map = preLoadResolver.get();
			}
			catch (EntityNotFoundException e)
			{
				map = Collections.emptyMap();
			}
			preLoadResolver = null;
		}
		return map;
	}
	
	public boolean isLoaded()
	{
		return preLoadResolver == null;
	}
	
	public void addFromBatch(Locale locale, V value)
	{
		assert preLoadResolver != null;
		if (map == null)
		{
			map = new HashMap<>();
		}
		map.put(locale, value);
	}
	
	public void finalizeBatch()
	{
		assert preLoadResolver != null;
		if (map == null)
		{
			map = Collections.emptyMap();
		}
		else
		{
			map = Collections.unmodifiableMap(map);
		}
		preLoadResolver = null;
	}
}
