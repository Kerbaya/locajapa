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
import java.sql.SQLException;
import java.util.Properties;

import com.kerbaya.jdbcspy.DriverImpl;

public class ExecMonDriver extends DriverImpl
{
	private final ExecMonStats stats;
	
	public ExecMonDriver()
	{
		super("execmon:");
		stats = new ExecMonStats();
	}

	@Override
	protected Connection connect(Driver driver, String url, Properties info)
			throws SQLException
	{
		return new ExecMonConnection(stats, super.connect(driver, url, info));
	}
}
