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

final class InstanceValueReference<V> implements ValueReference<V>, Serializable
{
	private static final long serialVersionUID = -2221032715251547293L;
	
	private static final InstanceValueReference<Void> NULL = 
			new InstanceValueReference<Void>(null);
	
	private final V instance;
	
	private InstanceValueReference(V instance)
	{
		this.instance = instance;
	}

	@Override
	public V get()
	{
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public static <V> InstanceValueReference<V> ofNull()
	{
		return (InstanceValueReference<V>) NULL;
	}

	public static <V> InstanceValueReference<V> of(V instance)
	{
		return instance == null ? 
				InstanceValueReference.<V>ofNull() 
				: new InstanceValueReference<V>(instance);
	}
}
