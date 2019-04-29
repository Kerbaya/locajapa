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
import java.util.Collection;
import java.util.Map;
import java.util.Set;

abstract class ForwardingMap<K, V> implements Map<K, V>, Serializable
{
	private static final long serialVersionUID = -2248653975026920019L;

	protected abstract Map<K, V> build();
	
	@Override
	public int size()
	{
		return build().size();
	}

	@Override
	public boolean isEmpty()
	{
		return build().isEmpty();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return build().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return build().containsValue(value);
	}

	@Override
	public V get(Object key)
	{
		return build().get(key);
	}

	@Override
	public V put(K key, V value)
	{
		return build().put(key, value);
	}

	@Override
	public V remove(Object key)
	{
		return build().remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		build().putAll(m);
	}

	@Override
	public void clear()
	{
		build().clear();
	}

	@Override
	public Set<K> keySet()
	{
		return new ForwardingSet<>(new Resolver<Set<K>>() {
			private static final long serialVersionUID = 4637721532132253875L;

			@Override
			public Set<K> get()
			{
				return build().keySet();
			}
		});
	}

	@Override
	public Collection<V> values()
	{
		return new ForwardingCollection<>(new Resolver<Collection<V>>() {
			private static final long serialVersionUID = -812972076526896219L;

			@Override
			public Collection<V> get()
			{
				return build().values();
			}
		});
	}

	@Override
	public Set<Entry<K, V>> entrySet()
	{
		return new ForwardingSet<>(new Resolver<Set<Entry<K, V>>>() {
			private static final long serialVersionUID = 536537667892154886L;

			@Override
			public Set<Entry<K, V>> get()
			{
				return build().entrySet();
			}
		});
	}

	@Override
	public boolean equals(Object o)
	{
		return build().equals(o);
	}

	@Override
	public int hashCode()
	{
		return build().hashCode();
	}

	@Override
	public String toString()
	{
		return build().toString();
	}
}
