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

final class LocalizableEntityHandler 
		implements EntityHandler<Localizable<?>, Localized<?>, Object>
{
	private static final long serialVersionUID = -8617850665771558270L;

	public static final LocalizableEntityHandler INSTANCE = 
			new LocalizableEntityHandler();

	private LocalizableEntityHandler()
	{
	}
	
	@Override
	public Object getId(Localizable<?> localizable)
	{
		return localizable.getId();
	}

	@Override
	public Iterable<? extends Localized<?>> getLocalized(
			Localizable<?> localizable)
	{
		return localizable.getLocalized();
	}

	@Override
	public int getLanguageLevel(Localized<?> localized)
	{
		return localized.getLanguageLevel();
	}

	@Override
	public String getLanguageTag(Localized<?> localized)
	{
		return localized.getLanguageTag();
	}

	@Override
	public Object getValue(Localized<?> localized)
	{
		return localized.getValue();
	}

	protected Object readResolve()
	{
		return INSTANCE;
	}
	
}
