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

import javax.persistence.EntityManager;

/**
 * Generates localized values and value references for a particular combination
 * of localizable entity type, localized value type, and locale
 *  
 * @author Glenn.Lane@kerbaya.com
 *
 * @param <T>
 * the supported localizable entity type
 * 
 * @param <V>
 * the supported localized value
 */
public interface ValueLoader<T, V>
{
	/**
	 * Returns the localized value for a provided localizable
	 * 
	 * @param localizable
	 * the localizable for which to return a localized value
	 * 
	 * @return
	 * <p>the localized value for a provided localizable.  {@code null} is 
	 * returned when either:</p>
	 * <ul>
	 * <li>there was no value specified for the provided {@code localizable} for
	 * this instance's locale</li>
	 * <li>specified for the provided {@code localizable} was {@code null} for
	 * this instance's local</li>
	 * </ul>
	 * 
	 * @throws UnsupportedOperationException
	 * this instance does not support entity resolution
	 * 
	 * @throws NullPointerException
	 * {@code localizable} was {@code null}
	 */
	V getValue(T localizable);
	
	/**
	 * returns a lazily generated reference to the localized value for the 
	 * localizable of the provided {@code id} and this instance's locale
	 * 
	 * @param id
	 * the entity ID for which to generate the reference
	 * 
	 * @return
	 * a lazily generated reference to the localized value for the localizable 
	 * of the provided {@code id} and this instance's locale.  Returns 
	 * {@code null} if the provided {@code id} is {@code null}.
	 * 
	 * @throws UnsupportedOperationException
	 * this instance does not support query-loading
	 */
	ValueReference<V> getRefById(Object id);
	
	/**
	 * returns a lazily generated reference to the localized value for the 
	 * provided {@code localizable} and this instance's locale
	 * 
	 * @param localizable
	 * the localizable for which to generate the reference
	 * 
	 * @return
	 * a lazily generated reference to the localized value for the provided 
	 * {@code localizable} and this instance's locale
	 * 
	 * @throws UnsupportedOperationException
	 * this instance does not support entity handling
	 */
	ValueReference<V> getRef(T localizable);
	
	/**
	 * Populates all previously generated instances of {@link ValueReference}
	 * 
	 * @param em
	 * the entity manager associated with previously provided localizable 
	 * instances
	 * 
	 * @throws UnsupportedOperationException
	 * this instance does not support query-loading
	 */
	void load(EntityManager em);
}
