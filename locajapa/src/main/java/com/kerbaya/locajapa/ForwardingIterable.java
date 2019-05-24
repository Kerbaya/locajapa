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

import java.util.Iterator;

abstract class ForwardingIterable<V> extends Forwarding implements Iterable<V>
{
	private static final long serialVersionUID = -3946569151288300366L;

	protected abstract Iterable<V> delegate();
	
	@Override
	public final Iterator<V> iterator()
	{
		return new ForwardingIterator<V>(){
			private static final long serialVersionUID = 6657376201242870234L;
			
			private Iterator<V> delegate;
			
			@Override
			protected Iterator<V> delegate()
			{
				if (delegate == null)
				{
					delegate = ForwardingIterable.this.delegate().iterator();
				}
				return delegate;
			}
		};
	}
}
