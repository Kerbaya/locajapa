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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle.Control;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.persistence.EntityManager;

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
		return Collections.unmodifiableList(
				CONTROL.getCandidateLocales("", locale));
	}
	
	private static final Map<Class<?>, Boolean> WRAP_COL_PARAM = 
			new WeakHashMap<>();
	private static final Lock READ_LOCK;
	private static final Lock WRITE_LOCK;
	
	static
	{
		ReadWriteLock rwl = new ReentrantReadWriteLock();
		READ_LOCK = rwl.readLock();
		WRITE_LOCK = rwl.writeLock();
	}
	
	private static boolean calcWrapColParam(EntityManager em)
	{
		Class<?> delType = em.getDelegate().getClass();
		String delTypeName = delType.getName();
		if ("org.hibernate.impl.SessionImpl".equals(delTypeName))
		{
			return true;
		}
		if ("org.apache.openjpa.persistence.EntityManagerImpl".equals(
				delTypeName))
		{
			return true;
		}
		if (!"org.hibernate.internal.SessionImpl".equals(delTypeName))
		{
			return false;
		}
		try
		{
			String verStr = (String) delType.getClassLoader()
					.loadClass("org.hibernate.Version")
					.getMethod("getVersionString").invoke(null);
			return verStr.startsWith("4.");
		}
		catch (Throwable t)
		{
			return false;
		}
	}
	
	/**
	 * Determines if a provided entity manager has a bug where JPQL collection
	 * parameters need to be surrounded by parenthesis
	 *   
	 * @param em
	 * the entity manager that will be tested for the bug
	 * 
	 * @return
	 * {@code true} if JPQL collection parameters must be surrounded by 
	 * parenthesis, otherwise {@code false}
	 */
	public static boolean wrapColParam(EntityManager em)
	{
		Class<? extends EntityManager> emType = em.getClass();
		Boolean wrapColParam;
		READ_LOCK.lock();
		try
		{
			wrapColParam = WRAP_COL_PARAM.get(emType);
		}
		finally
		{
			READ_LOCK.unlock();
		}
		if (wrapColParam == null)
		{
			WRITE_LOCK.lock();
			try
			{
				if ((wrapColParam = WRAP_COL_PARAM.get(emType)) == null)
				{
					wrapColParam = calcWrapColParam(em);
					WRAP_COL_PARAM.put(emType, wrapColParam);
				}
			}
			finally
			{
				WRITE_LOCK.unlock();
			}
		}
		return wrapColParam.booleanValue();
	}
}
