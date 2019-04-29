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
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

final class LocalizedMap<V> extends AbstractMap<Locale, V>
		implements Serializable
{
	private static final long serialVersionUID = 2671994980755667519L;

	private final Collection<? extends Localized<? extends V>> localized;
	
	private transient SetView setView;

	public LocalizedMap(Collection<? extends Localized<? extends V>> localized)
	{
		this.localized = localized;
	}

	private static abstract class EntryImpl<V, E extends Localized<? extends V>>
			implements Entry<Locale, V>
	{
		protected final E localized;

		public EntryImpl(E localized)
		{
			this.localized = localized;
		}

		@Override
		public final V getValue()
		{
			return localized.getValue();
		}

		@Override
		public final V setValue(V value)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public final int hashCode()
		{
			return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
		}

		@Override
		public final boolean equals(Object obj)
		{
			if (obj == this)
			{
				return true;
			}
			if (!(obj instanceof Entry))
			{
				return false;
			}
			Entry<?, ?> other = (Entry<?, ?>) obj;
			return Objects.equals(getKey(), other.getKey())
					&& Objects.equals(getValue(), other.getValue());
		}

		@Override
		public final String toString()
		{
			return getKey() + "=" + getValue();
		}
	}

	private static final class MappedEntryImpl<V>
			extends EntryImpl<V, MappedLocalized<? extends V>>
	{
		public MappedEntryImpl(MappedLocalized<? extends V> localized)
		{
			super(localized);
		}

		@Override
		public Locale getKey()
		{
			return localized.getLocale();
		}
	}

	private static final class UnmappedEntryImpl<V>
			extends EntryImpl<V, Localized<? extends V>>
	{
		private Locale key;

		public UnmappedEntryImpl(Localized<? extends V> localized)
		{
			super(localized);
		}

		@Override
		public Locale getKey()
		{
			if (key == null)
			{
				key = Locale.forLanguageTag(localized.getLanguageTag());
			}
			return key;
		}
	}

	private static final class IteratorImpl<V>
			implements Iterator<Entry<Locale, V>>
	{
		private final Iterator<? extends Localized<? extends V>> iter;

		public IteratorImpl(Iterator<? extends Localized<? extends V>> iter)
		{
			this.iter = iter;
		}

		@Override
		public boolean hasNext()
		{
			return iter.hasNext();
		}

		@Override
		public Entry<Locale, V> next()
		{
			Localized<? extends V> next = iter.next();
			return next instanceof MappedLocalized ? new MappedEntryImpl<V>(
					(MappedLocalized<? extends V>) next
			) : new UnmappedEntryImpl<V>(next);
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	private final class SetView extends AbstractSet<Entry<Locale, V>>
	{
		@Override
		public Iterator<Entry<Locale, V>> iterator()
		{
			return new IteratorImpl<>(localized.iterator());
		}

		@Override
		public int size()
		{
			return localized.size();
		}
	}

	@Override
	public Set<Entry<Locale, V>> entrySet()
	{
		if (setView == null)
		{
			setView = new SetView();
		}
		return setView;
	}

}
