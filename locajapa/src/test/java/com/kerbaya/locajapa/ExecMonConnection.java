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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.kerbaya.jdbcspy.ConnectionImpl;

public class ExecMonConnection extends ConnectionImpl
{
	private final ExecMonStats stats;

	public ExecMonConnection(ExecMonStats stats, Connection wrapped)
	{
		super(wrapped);
		this.stats = stats;
	}
	
	@Override
	public Statement createStatement() throws SQLException
	{
		return new ExecMonStatement(stats, super.createStatement());
	}
	
	@Override
	public Statement createStatement(
			int resultSetType,
			int resultSetConcurrency) throws SQLException
	{
		return new ExecMonStatement(stats, super.createStatement(resultSetType, resultSetConcurrency));
	}
	
	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException
	{
		return new ExecMonStatement(stats, super.createStatement(
				resultSetType, resultSetConcurrency, resultSetHoldability));
	}
	
	@Override
	public CallableStatement prepareCall(String sql) throws SQLException
	{
		return new ExecMonCallableStatement(stats, super.prepareCall(sql));
	}
}
