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

import javax.persistence.Transient;

/**
 * Specifies a general contract to which localizable entities must conform.
 * 
 * @author Glenn.Lane@kerbaya.com
 *
 * @param <T>
 * The value type that is localized
 */
public interface Localizable<T>
{
	/**
	 * Returns the entity ID.  Must be mapped in JPA (addressable in JPQL as 
	 * {@code entity.id})
	 * 
	 * @return
	 * Entity ID
	 */
	@Transient
	Object getId();

	/**
	 * Returns the localized versions.  The relationship must be mapped in JPA 
	 * (addressable in JPQL as {@code entity.localized})
	 * 
	 * @return
	 * The localized versions
	 */
	@Transient
	Iterable<? extends Localized<? extends T>> getLocalized();
}
