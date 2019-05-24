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

abstract class ForwardingCollection<V> extends ForwardingIterable<V> 
		implements Collection<V>
{
	private static final long serialVersionUID = -7114061283362475043L;

	protected abstract Collection<V> delegate();
	
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
	public final boolean contains(Object o)
	{
		return delegate().contains(o);
	}

	@Override
	public final Object[] toArray()
	{
		return delegate().toArray();
	}

	@Override
	public final <T> T[] toArray(T[] a)
	{
		return delegate().toArray(a);
	}

	@Override
	public final boolean add(V e)
	{
		return delegate().add(e);
	}

	@Override
	public final boolean remove(Object o)
	{
		return delegate().remove(o);
	}

	@Override
	public final boolean containsAll(Collection<?> c)
	{
		return delegate().containsAll(c);
	}

	@Override
	public final boolean addAll(Collection<? extends V> c)
	{
		return delegate().addAll(c);
	}

	@Override
	public final boolean removeAll(Collection<?> c)
	{
		return delegate().removeAll(c);
	}

	@Override
	public final boolean retainAll(Collection<?> c)
	{
		return delegate().retainAll(c);
	}

	@Override
	public final void clear()
	{
		delegate().clear();
	}
}
