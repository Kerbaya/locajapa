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
import java.util.Objects;

import javax.persistence.Column;

public class LocalizedStringId implements Serializable
{
	private static final long serialVersionUID = 2951991303475177055L;

	private Long localizableId;
	private String languageTag;
	
	@Column(name="localizable_id")
	public Long getLocalizableId()
	{
		return localizableId;
	}
	public void setLocalizableId(Long localizableId)
	{
		this.localizableId = localizableId;
	}
	
	public String getLanguageTag()
	{
		return languageTag;
	}
	public void setLanguageTag(String languageTag)
	{
		this.languageTag = languageTag;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(languageTag, localizableId);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!(obj instanceof LocalizedStringId))
		{
			return false;
		}
		LocalizedStringId other = (LocalizedStringId) obj;
		return Objects.equals(languageTag, other.languageTag)
				&& Objects.equals(localizableId, other.localizableId);
	}
	
	
	
}
