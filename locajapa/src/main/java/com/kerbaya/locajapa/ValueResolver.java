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
import java.util.Set;

final class ValueResolver<T, V> implements Resolver<V>
{
	private static final long serialVersionUID = 401859217065373792L;

	private final EntityHandler<? super T, ?, ? extends V> entityHandler;
	private final T localizable;
	private final Set<String> candidateLanguageTags;
	
	public ValueResolver(
			EntityHandler<? super T, ?, ? extends V> entityHandler,
			T localizable, 
			Set<String> candidateLanguageTags)
	{
		this.entityHandler = entityHandler;
		this.localizable = localizable;
		this.candidateLanguageTags = candidateLanguageTags;
	}

	public static <T, V> V resolve(
			EntityHandler<? super T, ?, ? extends V> entityHandler,
			T localizable,
			Set<String> candidateLanguageTags)
	{
		Iterator<Localized<V>> iter = LocalizedIterator.create(
				entityHandler, localizable);
		
		int matchLanguageLevel = -1;
		Localized<V> match = null;
		while (iter.hasNext())
		{
			Localized<V> next = iter.next();
			int languageLevel = next.getLanguageLevel();
			if ((match == null || languageLevel > matchLanguageLevel)
					&& candidateLanguageTags.contains(
							next.getLanguageTag()))
			{
				match = next;
				matchLanguageLevel = languageLevel;
			}
		}
		return match == null ? null : match.getValue();
	}
	
	@Override
	public V get()
	{
		return resolve(entityHandler, localizable, candidateLanguageTags);
	}
}