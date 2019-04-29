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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CallOverrider<T>
{
	public interface OverrideHandler<T>
	{
		Object invoke(T subject, Method method, Object[] args)
				throws Throwable;
	}
	
	private final Constructor<? extends T> proxyCtor;
	private final Map<Method, OverrideHandler<? super T>> overrides;
		
	private CallOverrider(
			Constructor<? extends T> proxyCtor, 
			Map<Method, OverrideHandler<? super T>> overrides)
	{
		this.proxyCtor = proxyCtor;
		this.overrides = Collections.unmodifiableMap(new HashMap<>(overrides));
	}

	public T create(final T t)
	{
		final InvocationHandler ih = new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable
			{
				OverrideHandler<? super T> oh = overrides.get(method);
				return oh == null ? 
						method.invoke(t, args) : oh.invoke(t, method, args);
			}
		};
		try
		{
			return proxyCtor.newInstance(ih);
		}
		catch (InstantiationException | IllegalAccessException
				| InvocationTargetException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	public static final class EntryBuilder<T>
	{
		private final Builder<T> builder;
		private final Method method;
		
		private EntryBuilder(Builder<T> builder, Method method)
		{
			this.builder = builder;
			this.method = method;
		}
		
		public Builder<T> with(OverrideHandler<? super T> oh)
		{
			builder.overrides.put(method, oh);
			return builder;
		}
	}
	
	public static final class Builder<T>
	{
		private final Map<Method, OverrideHandler<? super T>> overrides = 
				new HashMap<>();
		
		private final Class<T> iface;
		private final Constructor<? extends T> proxyCtor;
		
		public Builder(Class<T> iface)
		{
			this.iface = iface;
			
			@SuppressWarnings("unchecked")
			Class<? extends T> proxyType = 
					(Class<? extends T>) Proxy.getProxyClass(
							iface.getClassLoader(), iface);
			try
			{
				proxyCtor = proxyType.getConstructor(InvocationHandler.class);
			}
			catch (NoSuchMethodException e)
			{
				throw new IllegalStateException(e);
			}
		}
		
		private static Method findMethod(
				Set<Class<?>> dejaVu, 
				Class<?> type, 
				String methodName, 
				Class<?>... parameterTypes)
		{
			if (type == null)
			{
				return null;
			}
			if (!dejaVu.add(type))
			{
				return null;
			}
			try
			{
				return type.getDeclaredMethod(methodName, parameterTypes);
			}
			catch (NoSuchMethodException e)
			{
			}
			for (Class<?> iface: type.getInterfaces())
			{
				Method m = findMethod(
						dejaVu, iface, methodName, parameterTypes);
				if (m != null)
				{
					return m;
				}
			}
			return null;
		}
		
		public EntryBuilder<T> override(
				String methodName, Class<?>... parameterTypes) 
		{
			final Method method = findMethod(
					new HashSet<Class<?>>(), iface, methodName, parameterTypes);
			if (method == null)
			{
				throw new IllegalArgumentException(String.format(
						"Could not find method %s.%s(%s)", 
						iface, 
						methodName, 
						Arrays.toString(parameterTypes)));
			}
			if (overrides.containsKey(method))
			{
				throw new IllegalArgumentException(String.format(
						"Duplicate override for %s.%s(%s)", 
						iface, 
						methodName, 
						Arrays.toString(parameterTypes)));
			}
			return new EntryBuilder<>(this, method);
		}
		
		public CallOverrider<T> build()
		{
			return new CallOverrider<>(proxyCtor, overrides);
		}
	}

	public static <T> Builder<T> builder(Class<T> type)
	{
		return new Builder<>(type);
	}
}
