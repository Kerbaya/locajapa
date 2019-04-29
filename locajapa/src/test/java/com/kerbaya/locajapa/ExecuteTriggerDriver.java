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

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.kerbaya.locajapa.CallOverrider.Builder;
import com.kerbaya.locajapa.CallOverrider.OverrideHandler;

public class ExecuteTriggerDriver implements Driver
{
	private static String URL_PREFIX;
	private static Runnable TRIGGER;
	
	private static final String JDBC_PREFIX = "jdbc:";
	private static final Pattern URL_PATTERN = Pattern.compile(
			Pattern.quote(JDBC_PREFIX) + "[^:]+:");
	
	private final CallOverrider<Connection> conOx;
	
	public static void setUrlPrefix(String urlPrefix)
	{
		if (URL_PREFIX != null)
		{
			throw new IllegalStateException("urlPrefix already set");
		}
		if (!URL_PATTERN.matcher(urlPrefix).matches())
		{
			throw new IllegalArgumentException(String.format(
					"Invalid urlPrefix (\"%s\"), must match regex \"%s\"", 
					urlPrefix, 
					URL_PATTERN.pattern()));
		}
		URL_PREFIX = urlPrefix;
	}
	
	public static void setTrigger(Runnable trigger)
	{
		TRIGGER = trigger;
	}
	
	public ExecuteTriggerDriver()
	{
		if (URL_PREFIX == null)
		{
			throw new IllegalStateException(
					ExecuteTriggerDriver.class.getSimpleName() 
					+ ".setUrlPrefix() must be called prior to driver use");
		}
		
		OverrideHandler<Statement> execOverride = new OverrideHandler<Statement>() {
			@Override
			public Object invoke(Statement subject, Method method,
					Object[] args) throws Throwable
			{
				Runnable trigger = TRIGGER;
				if (trigger != null)
				{
					trigger.run();
				}
				return method.invoke(subject, args);
			}
		};
		
		Builder<Statement> stmtOxb = CallOverrider.builder(Statement.class);
		addStmtOverrides(stmtOxb, execOverride);
		final CallOverrider<Statement> stmtOx = stmtOxb.build();
		
		Builder<PreparedStatement> psOxb = CallOverrider.builder(
				PreparedStatement.class);
		addStmtOverrides(psOxb, execOverride);
		addPsOverrides(psOxb, execOverride);
		final CallOverrider<PreparedStatement> psOx = psOxb.build();
		
		Builder<CallableStatement> csOxb = CallOverrider.builder(
				CallableStatement.class);
		addStmtOverrides(csOxb, execOverride);
		addPsOverrides(csOxb, execOverride);
		final CallOverrider<CallableStatement> csOx = csOxb.build();
		
		OverrideHandler<Connection> wrapStmt = new OverrideHandler<Connection>() {
			@Override
			public Object invoke(Connection subject, Method method, Object[] args)
					throws Throwable
			{
				return stmtOx.create((Statement) method.invoke(subject, args));
			}
		}; 
		
		OverrideHandler<Connection> wrapPs = new OverrideHandler<Connection>() {
			
			@Override
			public Object invoke(Connection subject, Method method, Object[] args)
					throws Throwable
			{
				return psOx.create((PreparedStatement) method.invoke(subject, args));
			}
		}; 
		
		OverrideHandler<Connection> wrapCs = new OverrideHandler<Connection>() {
			
			@Override
			public Object invoke(Connection subject, Method method, Object[] args)
					throws Throwable
			{
				return csOx.create((CallableStatement) method.invoke(subject, args));
			}
		}; 
		
		conOx = CallOverrider.builder(Connection.class)
				.override("createStatement").with(wrapStmt)
				.override("createStatement", int.class, int.class).with(wrapStmt)
				.override("createStatement", int.class, int.class, int.class).with(wrapStmt)
				.override("prepareStatement", String.class).with(wrapPs)
				.override("prepareStatement", String.class, int.class).with(wrapPs)
				.override("prepareStatement", String.class, int.class, int.class).with(wrapPs)
				.override("prepareStatement", String.class, int.class, int.class, int.class).with(wrapPs)
				.override("prepareStatement", String.class, int[].class).with(wrapPs)
				.override("prepareStatement", String.class, String[].class).with(wrapPs)
				.override("prepareCall", String.class).with(wrapCs)
				.override("prepareCall", String.class, int.class, int.class).with(wrapCs)
				.override("prepareCall", String.class, int.class, int.class, int.class).with(wrapCs)
				.build();
	}
	
	private static void addPsOverrides(
			Builder<? extends PreparedStatement> oxb, 
			OverrideHandler<? super PreparedStatement> oh)
	{
		oxb.override("execute").with(oh)
				.override("executeQuery").with(oh)
				.override("executeUpdate").with(oh);
	}
	
	private static void addStmtOverrides(
			Builder<? extends Statement> oxb, 
			OverrideHandler<? super Statement> oh)
	{
		oxb.override("execute", String.class).with(oh)
				.override("execute", String.class, int.class).with(oh)
				.override("execute", String.class, int[].class).with(oh)
				.override("execute", String.class, String[].class).with(oh)
				.override("executeBatch").with(oh)
				.override("executeQuery", String.class).with(oh)
				.override("executeUpdate", String.class).with(oh)
				.override("executeUpdate", String.class, int.class).with(oh)
				.override("executeUpdate", String.class, int[].class).with(oh)
				.override("executeUpdate", String.class, String[].class).with(oh);
	}
	
	private String unwrapUrl(String url) throws SQLException
	{
		if (url == null || !url.startsWith(URL_PREFIX))
		{
			throw new SQLException("Invalid URL: " + url);
		}
		return JDBC_PREFIX + url.substring(URL_PREFIX.length());
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException
	{
		url = unwrapUrl(url);
		return conOx.create(DriverManager.getDriver(url).connect(url, info));
	}

	@Override
	public boolean acceptsURL(String url)
	{
		return url != null && url.startsWith(URL_PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException
	{
		url = unwrapUrl(url);
		return DriverManager.getDriver(url).getPropertyInfo(url, info);
	}

	@Override
	public int getMajorVersion()
	{
		return 1;
	}

	@Override
	public int getMinorVersion()
	{
		return 0;
	}

	@Override
	public boolean jdbcCompliant()
	{
		return true;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException
	{
		throw new SQLFeatureNotSupportedException();
	}

}
