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

	private static final long serialVersionUID = -1068930046826923113L;

	private static final class LocalizedMapResolver<V> 
			implements Resolver<Map<Locale, V>>
	{
		private static final long serialVersionUID = -3463630721962149937L;

		private final Localizable<? extends V> localizable;

		public LocalizedMapResolver(Localizable<? extends V> localizable)
		{
			this.localizable = localizable;
		}

		@Override
		public Map<Locale, V> get()
		{
			return new LocalizedMap<V>(localizable.getLocalized());
		}
	}
	
	private Resolver<Map<Locale, V>> preLoadResolver;
	private Map<Locale, V> map;
	
	public MapLoaderEntry(Localizable<? extends V> localizable)
	{
		preLoadResolver = new LocalizedMapResolver<>(localizable);
	}
	
	public MapLoaderEntry()
	{
		preLoadResolver = NonResolvable.instance();
	}
	
	@Override
	protected Map<Locale, V> build()
	{
		if (preLoadResolver != null)
		{
			try
			{
				map = preLoadResolver.get();
			}
			catch (EntityNotFoundException e)
			{
				map = null;
			}
			preLoadResolver = null;
		}
		if (map == null)
		{
			throw new EntityNotFoundException();
		}
		return map;
	}
	
	public boolean isLoaded()
	{
		return preLoadResolver == null;
	}
	
	public void setEmpty()
	{
		assert preLoadResolver != null;
		map = Collections.emptyMap();
	}
	
	@SuppressWarnings("unchecked")
	public void addFromBatch(Locale locale, Object value)
	{
		assert preLoadResolver != null;
		if (map == null)
		{
			map = new HashMap<>();
		}
		map.put(locale, (V) value);
	}
	
	public void finalizeBatch()
	{
		assert preLoadResolver != null;
		if (map != null && !map.isEmpty())
		{
			map = Collections.unmodifiableMap(map);
		}
		preLoadResolver = null;
	}
}
