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

final class NonResolvable<V> implements Resolver<V>
{

	private static final long serialVersionUID = -1783637949795161847L;

	private static final NonResolvable<Void> INSTANCE = new NonResolvable<>();
	
	private NonResolvable() {}
	
	@Override
	public V get()
	{
		throw new IllegalStateException("Not resolvable");
	}
	
	@SuppressWarnings("unchecked")
	public static <V> NonResolvable<V> instance()
	{
		return (NonResolvable<V>) INSTANCE;
	}
	
	protected Object readResolve()
	{
		return INSTANCE;
	}

}
