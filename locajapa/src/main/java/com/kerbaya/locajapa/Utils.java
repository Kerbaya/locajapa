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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle.Control;

final class Utils
{
	private static final Control CONTROL = Control.getControl(
			Control.FORMAT_DEFAULT);
	
	private Utils() {}
	
	public static String loadResource(Class<?> type, String name)
	{
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[128];
		int len;
		try (InputStream is = type.getResourceAsStream(name);
				Reader in = new InputStreamReader(is, StandardCharsets.UTF_8))
		{
			while ((len = in.read(buffer)) != -1)
			{
				sb.append(buffer, 0, len);
			}
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
		return sb.toString();
	}
	
	public static List<Locale> getCandidateLocales(Locale locale)
	{
		return CONTROL.getCandidateLocales("", locale);
	}
}
