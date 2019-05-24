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
import java.util.Iterator;

final class LocalizedIterator<L, V> implements Iterator<Localized<V>>
{
	private final EntityHandler<?, L, ? extends V> entityHandler;
	private final Iterator<? extends L> iter;
	
	private LocalizedIterator(
			EntityHandler<?, L, ? extends V> entityHandler,
			Iterator<? extends L> iter)
	{
		this.entityHandler = entityHandler;
		this.iter = iter;
	}

	private final class LocalizedImpl implements Localized<V>
	{
		private final L localized;
		
		public LocalizedImpl(L localized)
		{
			this.localized = localized;
		}

		@Override
		public String getLanguageTag()
		{
			return entityHandler.getLanguageTag(localized);
		}

		@Override
		public int getLanguageLevel()
		{
			return entityHandler.getLanguageLevel(localized);
		}

		@Override
		public V getValue()
		{
			return entityHandler.getValue(localized);
		}
	}

	@Override
	public boolean hasNext()
	{
		return iter.hasNext();
	}

	@Override
	public Localized<V> next()
	{
		return new LocalizedImpl(iter.next());
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
	
	public static <T, L, V> Iterator<Localized<V>> create(
			EntityHandler<? super T, L, ? extends V> entityHandler,
			T localizable)
	{
		Iterable<? extends L> col = entityHandler.getLocalized(localizable);
		return col == null ? 
				Collections.<Localized<V>>emptyIterator() 
				: new LocalizedIterator<>(entityHandler, col.iterator());
	}
}
