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

final class ValueResolverSupport<T, L, V> implements Resolver<V>
{
	
	private static final long serialVersionUID = -2843094047182274684L;
	
	private final Set<String> candidateLanguageTags;
	private final T localizable;
	private final LocalizedResolver<? super T, L, ? extends V> localizedResolver;
	
	public ValueResolverSupport(
			Set<String> candidateLanguageTags,
			T localizable,
			LocalizedResolver<? super T, L, ? extends V> localizedResolver)
	{
		this.candidateLanguageTags = candidateLanguageTags;
		this.localizable = localizable;
		this.localizedResolver = localizedResolver;
	}
	
	public static <T, L, V> V resolve(
			Set<String> candidateLanguageTags, 
			T localizable, 
			LocalizedResolver<? super T, L, ? extends V> localizedResolver)
	{
		if (candidateLanguageTags.isEmpty())
		{
			return null;
		}
		int matchLanguageLevel = -1;
		L match = null;
		Iterator<? extends L> iter = localizedResolver.getLocalized(
				localizable);
		while (iter.hasNext())
		{
			L next = iter.next();
			int languageLevel = localizedResolver.getLanguageLevel(next);
			if ((match == null || languageLevel > matchLanguageLevel)
					&& candidateLanguageTags.contains(
							localizedResolver.getLanguageTag(next)))
			{
				match = next;
				matchLanguageLevel = languageLevel;
			}
		}
		return match == null ? null : localizedResolver.getValue(match);
	}
	
	@Override
	public V get()
	{
		return resolve(candidateLanguageTags, localizable, localizedResolver);
	}
}