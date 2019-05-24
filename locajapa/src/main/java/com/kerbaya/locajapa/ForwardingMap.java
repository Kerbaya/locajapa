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
import java.util.Map;
import java.util.Set;

abstract class ForwardingMap<K, V> extends Forwarding implements Map<K, V>
{
	private static final long serialVersionUID = 271691668964822187L;

	protected abstract Map<K, V> delegate();
	
	@Override
	public final int size()
	{
		return delegate().size();
	}

	@Override
	public final boolean isEmpty()
	{
		return delegate().isEmpty();
	}

	@Override
	public final boolean containsKey(Object key)
	{
		return delegate().containsKey(key);
	}

	@Override
	public final boolean containsValue(Object value)
	{
		return delegate().containsValue(value);
	}

	@Override
	public final V get(Object key)
	{
		return delegate().get(key);
	}

	@Override
	public final V put(K key, V value)
	{
		return delegate().put(key, value);
	}

	@Override
	public final V remove(Object key)
	{
		return delegate().remove(key);
	}

	@Override
	public final void putAll(Map<? extends K, ? extends V> m)
	{
		delegate().putAll(m);
	}

	@Override
	public final void clear()
	{
		delegate().clear();
	}

	@Override
	public final Set<K> keySet()
	{
		return new ForwardingSet<K>() {
			private static final long serialVersionUID = 437084226870007867L;

			@Override
			protected Set<K> delegate()
			{
				return ForwardingMap.this.delegate().keySet();
			}
		};
	}

	@Override
	public final Collection<V> values()
	{
		return new ForwardingCollection<V>() {
			private static final long serialVersionUID = -758563396594312230L;

			@Override
			protected Collection<V> delegate()
			{
				return ForwardingMap.this.delegate().values();
			}
		};
	}

	@Override
	public final Set<Entry<K, V>> entrySet()
	{
		return new ForwardingSet<Entry<K, V>>(){
			private static final long serialVersionUID = -6709545522290255290L;

			@Override
			protected Set<Entry<K, V>> delegate()
			{
				return ForwardingMap.this.delegate().entrySet();
			}
		};
	}
}
