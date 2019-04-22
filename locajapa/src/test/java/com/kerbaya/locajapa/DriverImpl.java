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

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class DriverImpl implements Driver 
{
	private final Driver subject;
	
	public DriverImpl()
	{
		try
		{
			subject = (Driver) Class.forName("org.h2.Driver").newInstance();
		}
		catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException
	{
		return LoggingProxy.wrap(Connection.class, subject.connect(url, info));
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException
	{
		return subject.acceptsURL(url);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException
	{
		return subject.getPropertyInfo(url, info);
	}

	@Override
	public int getMajorVersion()
	{
		return subject.getMajorVersion();
	}

	@Override
	public int getMinorVersion()
	{
		return subject.getMinorVersion();
	}

	@Override
	public boolean jdbcCompliant()
	{
		return subject.jdbcCompliant();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException
	{
		return subject.getParentLogger();
	}

}
