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
package com.kerbaya.jdbcspy;

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

public class DriverImpl implements Driver 
{
	private static final ConnectionInterceptorSupport DEFAULT_CON_IX = 
			new ConnectionInterceptorSupport();
	private static final StatementInterceptorSupport<Statement> DEFAULT_STMT_IX = 
			new StatementInterceptorSupport<>();
	private static final PreparedStatementInterceptorSupport<PreparedStatement> DEFAULT_PS_IX = 
			new PreparedStatementInterceptorSupport<>();
	private static final CallableStatementInterceptorSupport DEFAULT_CS_IX = 
			new CallableStatementInterceptorSupport();
	
	private static final String JDBC_PREFIX = "jdbc:";
	private static final Pattern URL_PATTERN = Pattern.compile(
			Pattern.quote(JDBC_PREFIX) + "[^:]+:");
	
	private final String urlPrefix;
	private final ConnectionInterceptor<? super Connection> conIx;
	private final StatementInterceptor<? super Statement> stmtIx;
	private final PreparedStatementInterceptor<? super PreparedStatement> psIx;
	private final CallableStatementInterceptor<? super CallableStatement> csIx;

	public DriverImpl(
			String urlPrefix,
			ConnectionInterceptor<? super Connection> conIx,
			StatementInterceptor<? super Statement> stmtIx,
			PreparedStatementInterceptor<? super PreparedStatement> psIx,
			CallableStatementInterceptor<? super CallableStatement> csIx)
	{
		if (!URL_PATTERN.matcher(urlPrefix).matches())
		{
			throw new IllegalArgumentException(String.format(
					"Invalid urlPrefix (\"%s\"), must match regex \"%s\"", 
					urlPrefix, 
					URL_PATTERN.pattern()));
		}
		this.urlPrefix = urlPrefix;
		if (conIx == null)
		{
			this.conIx = DEFAULT_CON_IX;
		}
		else
		{
			this.conIx = conIx;
		}
		if (stmtIx == null)
		{
			this.stmtIx = DEFAULT_STMT_IX;
		}
		else
		{
			this.stmtIx = stmtIx;
		}
		if (psIx == null)
		{
			this.psIx = DEFAULT_PS_IX;
		}
		else
		{
			this.psIx = psIx;
		}
		if (csIx == null)
		{
			this.csIx = DEFAULT_CS_IX;
		}
		else
		{
			this.csIx = csIx;
		}
	}

	private String getWrappedUrl(String url)
	{
		return url.startsWith(urlPrefix) ?
			JDBC_PREFIX + url.substring(urlPrefix.length()) : null;
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException
	{
		url = getWrappedUrl(url);
		return new ConnectionImpl(
				DriverManager.getDriver(url).connect(url, info),
				conIx,
				stmtIx,
				psIx,
				csIx);
	}
	
	@Override
	public boolean acceptsURL(String url) throws SQLException
	{
		return getWrappedUrl(url) != null;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException
	{
		url = getWrappedUrl(url);
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
		return null;
	}

}
