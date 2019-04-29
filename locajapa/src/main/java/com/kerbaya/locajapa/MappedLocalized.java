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

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Provides a simple foundation superclass for localized entities.
 * 
 * @author Glenn.Lane@kerbaya.com
 *
 * @param <T>
 * The type of localized value returned by the entity
 */
@MappedSuperclass
public abstract class MappedLocalized<T> implements Localized<T>, Serializable
{
	private static final long serialVersionUID = -6020122449101759878L;

	private int languageLevel = -1;
	private String languageTag;
	
	private transient Locale locale;
	
	@Override
	public int getLanguageLevel()
	{
		return languageLevel;
	}
	public void setLanguageLevel(int languageLevel)
	{
		this.languageLevel = languageLevel;
	}
	
	@Override
	@Column(nullable=false, length=LANGUAGE_TAG_LENGTH)
	public String getLanguageTag()
	{
		return languageTag;
	}
	public void setLanguageTag(String languageTag)
	{
		this.languageTag = languageTag;
	}
	
	@Transient
	public abstract T getValue();
	
	@Transient
	public Locale getLocale()
	{
		String languageTag = getLanguageTag();
		if (locale == null && languageTag != null)
		{
			locale = Locale.forLanguageTag(languageTag);
		}
		return locale;
	}
	public void setLocale(Locale locale)
	{
		if (locale == null)
		{
			this.locale = null;
			setLanguageTag(null);
			setLanguageLevel(-1);
		}
		else
		{
			LocalizedSupport ls = new LocalizedSupport(locale);
			this.locale = ls.getLocale();
			setLanguageTag(ls.getLanguageTag());
			setLanguageLevel(ls.getLanguageLevel());
		}
	}
}
