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
import java.util.Iterator;
import java.util.Set;

final class ForwardingSet<V> implements Set<V>
{
	private final Resolver<Set<V>> supplier;

	public ForwardingSet(Resolver<Set<V>> supplier)
	{
		this.supplier = supplier;
	}
	
	@Override
	public int size()
	{
		return supplier.get().size();
	}

	@Override
	public boolean isEmpty()
	{
		return supplier.get().isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return supplier.get().contains(o);
	}

	@Override
	public Iterator<V> iterator()
	{
		return new ForwardingIterator<>(new Resolver<Iterator<V>>() {
			private static final long serialVersionUID = -7798751361551017021L;

			@Override
			public Iterator<V> get()
			{
				return supplier.get().iterator();
			}
		});
	}

	@Override
	public Object[] toArray()
	{
		return supplier.get().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return supplier.get().toArray(a);
	}

	@Override
	public boolean add(V e)
	{
		return supplier.get().add(e);
	}

	@Override
	public boolean remove(Object o)
	{
		return supplier.get().remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return supplier.get().containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends V> c)
	{
		return supplier.get().addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return supplier.get().retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return supplier.get().removeAll(c);
	}

	@Override
	public void clear()
	{
		supplier.get().clear();
	}

	@Override
	public boolean equals(Object o)
	{
		return supplier.get().equals(o);
	}

	@Override
	public int hashCode()
	{
		return supplier.get().hashCode();
	}
	
	@Override
	public String toString()
	{
		return supplier.get().toString();
	}
}
