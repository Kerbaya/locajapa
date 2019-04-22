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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
@IdClass(LocalizedStringId.class)
public class LocalizedString
{
	private LocalizableString localizable;
	private String locale;
	
	private String value;
	
	@Id
	public Long getLocalizableId()
	{
		return localizable == null ? null : localizable.getId();
	}
	public void setLocalizableId(Long localizableId)
	{
	}
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(insertable=false, updatable=false)
	public LocalizableString getLocalizable()
	{
		return localizable;
	}
	public void setLocalizable(LocalizableString localizable)
	{
		this.localizable = localizable;
	}

	@Id
	public String getLocale()
	{
		return locale;
	}
	public void setLocale(String locale)
	{
		this.locale = locale;
	}
	
	@Basic(optional=false)
	@Lob
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
}
