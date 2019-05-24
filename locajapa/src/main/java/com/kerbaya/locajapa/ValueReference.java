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

import javax.persistence.EntityNotFoundException;

/**
 * A reference to a localized value
 *  
 * @author Glenn.Lane@kerbaya.com
 *
 * @param <V>
 * The type of localized value
 */
public interface ValueReference<V>
{
	
	/**
	 * <p>Returns the localized value.  How the value is obtained depends on if
	 * {@link #get()} is called before or after 
	 * {@link ValueLoader#load(javax.persistence.EntityManager)} is called on 
	 * the {@link ValueLoader} that generated this instance.</p>
	 * <dl>
	 * <dt>Before {@link ValueLoader#load(javax.persistence.EntityManager)}</dt>
	 * <dd>The value is obtained using the associated {@link EntityHandler}.  If
	 * the {@link ValueLoader} was not configured with entity handling, an
	 * {@link IllegalStateException} is thrown.</dd>
	 * <dt>After {@link ValueLoader#load(javax.persistence.EntityManager)}<dt>
	 * <dd>The value discovered during the load process is returned</dd>
	 * </dl>
	 * <p>In either case, if the localizable instance referenced to 
	 * {@link ValueLoader#getRef(Object)} or 
	 * {@link ValueLoader#getRefById(Object)} does not exist, {@link #get()} 
	 * throws {@link EntityNotFoundException}.</p>
	 * 
	 * @return
	 * <p>the localized value.  may return {@code null} to indicate that for the
	 * locale provided to 
	 * {@link LoaderFactory#createValueLoader(java.util.Locale)}:</p>
	 * <ul>
	 * <li>The localized value is {@code null}</li>
	 * <li>There is no localized value for the provided locale</li>
	 * </ul>
	 * 
	 * @throws IllegalStateException
	 * the {@link ValueLoader} that generated this instance has not had
	 * {@link ValueLoader#load(javax.persistence.EntityManager)} called yet,
	 * and this instance does not support self-loading (it was created without
	 * entity handling).
	 * 
	 * @throws EntityNotFoundException
	 * a non-existent localizable was referenced for this instance
	 */
	V get();
}
