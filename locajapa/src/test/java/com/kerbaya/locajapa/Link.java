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

import java.util.Objects;

public final class Link
{
	private final String href;
	private final String text;
	
	public Link(String href, String text)
	{
		this.href = href;
		this.text = text;
	}

	public String getHref()
	{
		return href;
	}

	public String getText()
	{
		return text;
	}

	
	@Override
	public int hashCode()
	{
		return Objects.hash(href, text);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (!(obj instanceof Link))
		{
			return false;
		}
		Link other = (Link) obj;
		return Objects.equals(href, other.href)
				&& Objects.equals(text, other.text);
	}
	
	@Override
	public String toString()
	{
		return "href=" + href + ", text=" + text;
	}
}
