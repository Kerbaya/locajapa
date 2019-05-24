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

import java.util.IllformedLocaleException;
import java.util.Locale;

/**
 * Provides information from locales that is required in Localized entities
 * 
 * @author Glenn.Lane@kerbaya.com
 *
 */
public class LocalizedSupport
{
	/**
	 * The minimum length that should be considered for 
	 * {@link #getLanguageTag()} (see RFC-5646 4.4.1)
	 */
	public static final int LANGUAGE_TAG_LENGTH = 35;

	private final Locale locale;
	private final String languageTag;
	private final int languageLevel;
	
	/**
     * @param locale 
     * the locale

     * @throws IllformedLocaleException 
     * if <code>locale</code> has any ill-formed fields.
     * 
     * @throws NullPointerException 
     * if <code>locale</code> is null.
     */
	public LocalizedSupport(Locale locale)
	{
		this.locale = new Locale.Builder().setLocale(locale).build();
		languageTag = this.locale.toLanguageTag();
		languageLevel = Utils.getCandidateLocales(this.locale).size() - 1;
	}
	
	/**
	 * Returns the normalized locale
	 * 
	 * @return
	 * the normalized locale
	 */
	public Locale getLocale()
	{
		return locale;
	}
	
	/**
	 * Returns the specificity of the locale (higher numbers are more specific)
	 * 
	 * @return
	 * the specificity of the locale
	 */
	public int getLanguageLevel()
	{
		return languageLevel;
	}
	
	/**
	 * Returns the BCP47 language tag representing the locale
	 * 
	 * @return
	 * the BCP47 language tag representing the locale
	 */
	public String getLanguageTag()
	{
		return languageTag;
	}
}
