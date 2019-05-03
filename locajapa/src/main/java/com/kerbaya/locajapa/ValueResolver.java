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

import java.util.Set;

final class ValueResolver<V> implements Resolver<V>
{

	private static final long serialVersionUID = 5557722038223945717L;
	
	private final Set<String> candidateLanguageTags;
	private final Localizable<? extends V> localizable;
	
	public ValueResolver(
			Set<String> candidateLanguageTags,
			Localizable<? extends V> localizable)
	{
		this.candidateLanguageTags = candidateLanguageTags;
		this.localizable = localizable;
	}
	
	@Override
	public V get()
	{
		return resolve(candidateLanguageTags, localizable);
	}
	
	public static <V> V resolve(
			Set<String> candidateLanguageTags, 
			Localizable<? extends V> localizable)
	{
		int matchLanguageLevel = -1;
		Localized<? extends V> match = null;
		for (Localized<? extends V> localized: localizable.getLocalized())
		{
			int languageLevel = localized.getLanguageLevel();
			if ((match == null || languageLevel > matchLanguageLevel)
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