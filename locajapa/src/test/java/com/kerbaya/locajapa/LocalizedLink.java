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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LocalizedLink
{
	private long id;
	private int languageLevel;
	private String languageTag;
	private String href;
	private String text;
	
	@Id
	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	
	public int getLanguageLevel()
	{
		return languageLevel;
	}
	public void setLanguageLevel(int languageLevel)
	{
		this.languageLevel = languageLevel;
	}
	
	@Column(nullable=false, length=LocalizedSupport.LANGUAGE_TAG_LENGTH)
	public String getLanguageTag()
	{
		return languageTag;
	}
	public void setLanguageTag(String languageTag)
	{
		this.languageTag = languageTag;
	}
	
	@Basic(optional=false)
	public String getHref()
	{
		return href;
	}
	public void setHref(String href)
	{
		this.href = href;
	}
	
	@Basic(optional=false)
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
}
