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

abstract class Forwarding implements Serializable
{
	private static final long serialVersionUID = -3545096005005180655L;

	protected abstract Object delegate();
	
	@Override
	public final String toString()
	{
		return delegate().toString();
	}
	
	@Override
	public final int hashCode()
	{
		return delegate().hashCode();
	}
	
	@Override
	public final boolean equals(Object obj)
	{
		return delegate().equals(obj);
	}
}
