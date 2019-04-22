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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class LoggingProxy implements InvocationHandler
{
	private static final Object[] EMPTY = new Object[0];
	
	private final Object subject;
	
	public LoggingProxy(Object subject)
	{
		this.subject = subject;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable
	{
		System.out.println(method + " " + Arrays.toString(args == null ? EMPTY : args));
		return method.invoke(subject, args);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T wrap(Class<T> iface, T subject)
	{
		return (T) Proxy.newProxyInstance(
				subject.getClass().getClassLoader(), 
				new Class<?>[] {iface}, 
				new LoggingProxy(subject));
	}
}
