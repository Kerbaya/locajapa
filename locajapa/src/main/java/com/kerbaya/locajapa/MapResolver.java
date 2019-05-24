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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

final class MapResolver<T, V> implements Resolver<Map<Locale, V>>
{
	private static final long serialVersionUID = -7192160315373661342L;

	private final EntityHandler<? super T, ?, ? extends V> entityHandler;
	private final T localizable;
	
	public MapResolver(
			EntityHandler<? super T, ?, ? extends V> entityHandler,
			T localizable)
	{
		this.entityHandler = entityHandler;
		this.localizable = localizable;
	}
	
	@Override
	public Map<Locale, V> get()
	{
		Iterator<Localized<V>> iter = 
				LocalizedIterator.create(entityHandler, localizable);
		if (iter == null || !iter.hasNext())
		{
			return Collections.emptyMap();
		}
		Localized<V> localized = iter.next();
		Locale key = Locale.forLanguageTag(localized.getLanguageTag());
		V value = localized.getValue();
		if (!iter.hasNext())
		{
			return Collections.singletonMap(key, value);
		}
		Map<Locale, V> r = new HashMap<>();
		r.put(key, value);
		do
		{
			localized = iter.next();
			r.put(
					Locale.forLanguageTag(localized.getLanguageTag()),
					localized.getValue());
		} while (iter.hasNext());
		return Collections.unmodifiableMap(r);
	}

}
