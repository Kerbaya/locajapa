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
import java.util.Set;

abstract class ValueSupplierImpl<V> implements ValueSupplier<V>, Serializable
{
	private static final long serialVersionUID = -3382815395235086184L;

	private static final ValueSupplierImpl<Void> NULL = new ValueSupplierImpl<Void>() {
		private static final long serialVersionUID = 4927532974809276943L;

		@Override
		public Void get()
		{
			return null;
		}

		@Override
		public boolean isLoaded()
		{
			return true;
		}

		@Override
		public void set(Object value)
		{
			throw new IllegalStateException();
		}
	};
	
	private static final class Resolver<V> implements Serializable
	{
		private static final long serialVersionUID = 4496708481815555057L;

		private final Set<String> candidateLanguageTags;
		private final Localizable<? extends V> localizable;
		
		public Resolver(
				Set<String> candidateLanguageTags,
				Localizable<? extends V> localizable)
		{
			this.candidateLanguageTags = candidateLanguageTags;
			this.localizable = localizable;
		}
		
		public V resolve()
		{
			int matchLanguageLevel = -1;
			Localized<? extends V> match = null;
			for (Localized<? extends V> localized: localizable.getLocalized())
			{
				int languageLevel = localized.getLanguageLevel();
				if (languageLevel > matchLanguageLevel
						&& candidateLanguageTags.contains(
								localized.getLanguageTag()))
				{
					match = localized;
					matchLanguageLevel = languageLevel;
				}
			}
			return match == null ? null : match.getValue();
		}
	}
	
	private static final class Lazy<V> extends ValueSupplierImpl<V>
	{
		private static final long serialVersionUID = 2012629064443750892L;

		private Resolver<? extends V> resolver;
		private V value;
		
		public Lazy(
				Set<String> candidateLanguageTags, 
				Localizable<? extends V> localizable)
		{
			resolver = new Resolver<>(candidateLanguageTags, localizable);
		}

		@Override
		public V get()
		{
			if (resolver != null)
			{
				value = resolver.resolve();
				resolver = null;
			}
			return value;
		}

		@Override
		public boolean isLoaded()
		{
			return resolver == null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void set(Object value)
		{
			if (resolver == null)
			{
				throw new IllegalStateException();
			}
			this.value = (V) value;
			resolver = null;
		}
		
	}
	
	public abstract boolean isLoaded();
	public abstract void set(Object value);
	
	@SuppressWarnings("unchecked")
	public static <V> ValueSupplierImpl<V> ofNull()
	{
		return (ValueSupplierImpl<V>) NULL;
	}
	
	public static <V> ValueSupplierImpl<V> of(
			Set<String> candidateLanguageTags, 
			Localizable<? extends V> localizable)
	{
		return localizable == null ? 
				ValueSupplierImpl.<V>ofNull()
				: new Lazy<V>(candidateLanguageTags, localizable);
	}
}
