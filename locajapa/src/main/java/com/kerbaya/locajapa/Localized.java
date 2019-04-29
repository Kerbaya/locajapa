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
 * Specifies the general contract to which localized value entities must 
 * conform.  Provides a localized value for a {@link Localizable} instance.
 * Extending {@link MappedLocalized} may be adequate for most cases
 * 
 * @author Glenn.Lane@kerbaya.com
 *
 * @param <T>
 * The localized value type for this entity
 */
public interface Localized<T>
{
	/**
	 * The minimum length that should be considered for 
	 * {@link #getLanguageTag()} (see RFC-5646 4.4.1)
	 */
	int LANGUAGE_TAG_LENGTH = 35;
	
	/**
	 * Returns the BCP47 language tag representing the locale.  Use 
	 * {@link LocalizedSupport} to determine language tags and levels for a
	 * {@link java.util.Locale}.  Must be mapped in JPA (addressable in JPQL as
	 * {@code entity.languageTag}).
	 * 
	 * @return
	 * the BCP47 language tag representing the locale
	 */
	@Transient
	String getLanguageTag();
	
	/**
	 * Returns the specificity of the locale.   Use {@link LocalizedSupport} to 
	 * determine language tags and levels for a {@link java.util.Locale}.  Must
	 * be mapped in JPA (addressable in JPQL as {@code entity.languageLevel})
	 * 
	 * @return
	 * the specificity of the locale
	 */
	@Transient
	int getLanguageLevel();
	
	/**
	 * Returns the localized value.  Unlike {@link #getLanguageLevel()} and
	 * {@link #getLanguageTag()}, this property does not need to be mapped in 
	 * JPA (may be transient)
	 * 
	 * @return
	 * the localized value
	 */
	@Transient
	T getValue();
}
