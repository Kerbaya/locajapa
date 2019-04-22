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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

public abstract class DelegatingStatementInterceptor<S> implements StatementInterceptor<S>
{
	protected abstract StatementInterceptor<? super S> getDelegate();
	
	@Override
	public <T> T unwrap(S subject, Class<T> iface) throws SQLException
	{
		return getDelegate().unwrap(subject, iface);
	}

	@Override
	public boolean isWrapperFor(S subject, Class<?> iface) throws SQLException
	{
		return getDelegate().isWrapperFor(subject, iface);
	}

	@Override
	public ResultSet executeQuery(S subject, String sql) throws SQLException
	{
		return getDelegate().executeQuery(subject, sql);
	}

	@Override
	public int executeUpdate(S subject, String sql) throws SQLException
	{
		return getDelegate().executeUpdate(subject, sql);
	}

	@Override
	public void close(S subject) throws SQLException
	{
		getDelegate().close(subject);
	}

	@Override
	public int getMaxFieldSize(S subject) throws SQLException
	{
		return getDelegate().getMaxFieldSize(subject);
	}

	@Override
	public void setMaxFieldSize(S subject, int max) throws SQLException
	{
		getDelegate().setMaxFieldSize(subject, max);
	}

	@Override
	public int getMaxRows(S subject) throws SQLException
	{
		return getDelegate().getMaxRows(subject);
	}

	@Override
	public void setMaxRows(S subject, int max) throws SQLException
	{
		getDelegate().setMaxRows(subject, max);
	}

	@Override
	public void setEscapeProcessing(S subject, boolean enable)
			throws SQLException
	{
		getDelegate().setEscapeProcessing(subject, enable);
	}

	@Override
	public int getQueryTimeout(S subject) throws SQLException
	{
		return getDelegate().getQueryTimeout(subject);
	}

	@Override
	public void setQueryTimeout(S subject, int seconds) throws SQLException
	{
		getDelegate().setQueryTimeout(subject, seconds);
	}

	@Override
	public void cancel(S subject) throws SQLException
	{
		getDelegate().cancel(subject);
	}

	@Override
	public SQLWarning getWarnings(S subject) throws SQLException
	{
		return getDelegate().getWarnings(subject);
	}

	@Override
	public void clearWarnings(S subject) throws SQLException
	{
		getDelegate().clearWarnings(subject);
	}

	@Override
	public void setCursorName(S subject, String name) throws SQLException
	{
		getDelegate().setCursorName(subject, name);
	}

	@Override
	public boolean execute(S subject, String sql) throws SQLException
	{
		return getDelegate().execute(subject, sql);
	}

	@Override
	public ResultSet getResultSet(S subject) throws SQLException
	{
		return getDelegate().getResultSet(subject);
	}

	@Override
	public int getUpdateCount(S subject) throws SQLException
	{
		return getDelegate().getUpdateCount(subject);
	}

	@Override
	public boolean getMoreResults(S subject) throws SQLException
	{
		return getDelegate().getMoreResults(subject);
	}

	@Override
	public void setFetchDirection(S subject, int direction) throws SQLException
	{
		getDelegate().setFetchDirection(subject, direction);
	}

	@Override
	public int getFetchDirection(S subject) throws SQLException
	{
		return getDelegate().getFetchDirection(subject);
	}

	@Override
	public void setFetchSize(S subject, int rows) throws SQLException
	{
		getDelegate().setFetchSize(subject, rows);
	}

	@Override
	public int getFetchSize(S subject) throws SQLException
	{
		return getDelegate().getFetchSize(subject);
	}

	@Override
	public int getResultSetConcurrency(S subject) throws SQLException
	{
		return getDelegate().getResultSetConcurrency(subject);
	}

	@Override
	public int getResultSetType(S subject) throws SQLException
	{
		return getDelegate().getResultSetType(subject);
	}

	@Override
	public void addBatch(S subject, String sql) throws SQLException
	{
		getDelegate().addBatch(subject, sql);
	}

	@Override
	public void clearBatch(S subject) throws SQLException
	{
		getDelegate().clearBatch(subject);
	}

	@Override
	public int[] executeBatch(S subject) throws SQLException
	{
		return getDelegate().executeBatch(subject);
	}

	@Override
	public boolean getMoreResults(S subject, int current) throws SQLException
	{
		return getDelegate().getMoreResults(subject, current);
	}

	@Override
	public ResultSet getGeneratedKeys(S subject) throws SQLException
	{
		return getDelegate().getGeneratedKeys(subject);
	}

	@Override
	public int executeUpdate(S subject, String sql, int autoGeneratedKeys)
			throws SQLException
	{
		return getDelegate().executeUpdate(subject, sql, autoGeneratedKeys);
	}

	@Override
	public int executeUpdate(S subject, String sql, int[] columnIndexes)
			throws SQLException
	{
		return getDelegate().executeUpdate(subject, sql, columnIndexes);
	}

	@Override
	public int executeUpdate(S subject, String sql, String[] columnNames)
			throws SQLException
	{
		return getDelegate().executeUpdate(subject, sql, columnNames);
	}

	@Override
	public boolean execute(S subject, String sql, int autoGeneratedKeys)
			throws SQLException
	{
		return getDelegate().execute(subject, sql, autoGeneratedKeys);
	}

	@Override
	public boolean execute(S subject, String sql, int[] columnIndexes)
			throws SQLException
	{
		return getDelegate().execute(subject, sql, columnIndexes);
	}

	@Override
	public boolean execute(S subject, String sql, String[] columnNames)
			throws SQLException
	{
		return getDelegate().execute(subject, sql, columnNames);
	}

	@Override
	public int getResultSetHoldability(S subject) throws SQLException
	{
		return getDelegate().getResultSetHoldability(subject);
	}

	@Override
	public boolean isClosed(S subject) throws SQLException
	{
		return getDelegate().isClosed(subject);
	}

	@Override
	public void setPoolable(S subject, boolean poolable) throws SQLException
	{
		getDelegate().setPoolable(subject, poolable);
	}

	@Override
	public boolean isPoolable(S subject) throws SQLException
	{
		return getDelegate().isPoolable(subject);
	}

	@Override
	public void closeOnCompletion(S subject) throws SQLException
	{
		getDelegate().closeOnCompletion(subject);
	}

	@Override
	public boolean isCloseOnCompletion(S subject) throws SQLException
	{
		return getDelegate().isCloseOnCompletion(subject);
	}
}
