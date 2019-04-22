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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public abstract class DelegatingPreparedStatementInterceptor<S> implements PreparedStatementInterceptor<S>
{
	protected abstract PreparedStatementInterceptor<? super S> getDelegate();
	
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
	public ResultSet executeQuery(S subject) throws SQLException
	{
		return getDelegate().executeQuery(subject);
	}

	@Override
	public int executeUpdate(S subject) throws SQLException
	{
		return getDelegate().executeUpdate(subject);
	}

	@Override
	public int getQueryTimeout(S subject) throws SQLException
	{
		return getDelegate().getQueryTimeout(subject);
	}

	@Override
	public void setNull(S subject, int parameterIndex, int sqlType)
			throws SQLException
	{
		getDelegate().setNull(subject, parameterIndex, sqlType);
	}

	@Override
	public void setQueryTimeout(S subject, int seconds) throws SQLException
	{
		getDelegate().setQueryTimeout(subject, seconds);
	}

	@Override
	public void setBoolean(S subject, int parameterIndex, boolean x)
			throws SQLException
	{
		getDelegate().setBoolean(subject, parameterIndex, x);
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
	public void setByte(S subject, int parameterIndex, byte x)
			throws SQLException
	{
		getDelegate().setByte(subject, parameterIndex, x);
	}

	@Override
	public void clearWarnings(S subject) throws SQLException
	{
		getDelegate().clearWarnings(subject);
	}

	@Override
	public void setShort(S subject, int parameterIndex, short x)
			throws SQLException
	{
		getDelegate().setShort(subject, parameterIndex, x);
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
	public void setInt(S subject, int parameterIndex, int x) throws SQLException
	{
		getDelegate().setInt(subject, parameterIndex, x);
	}

	@Override
	public ResultSet getResultSet(S subject) throws SQLException
	{
		return getDelegate().getResultSet(subject);
	}

	@Override
	public void setLong(S subject, int parameterIndex, long x)
			throws SQLException
	{
		getDelegate().setLong(subject, parameterIndex, x);
	}

	@Override
	public int getUpdateCount(S subject) throws SQLException
	{
		return getDelegate().getUpdateCount(subject);
	}

	@Override
	public void setFloat(S subject, int parameterIndex, float x)
			throws SQLException
	{
		getDelegate().setFloat(subject, parameterIndex, x);
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
	public void setDouble(S subject, int parameterIndex, double x)
			throws SQLException
	{
		getDelegate().setDouble(subject, parameterIndex, x);
	}

	@Override
	public int getFetchDirection(S subject) throws SQLException
	{
		return getDelegate().getFetchDirection(subject);
	}

	@Override
	public void setBigDecimal(S subject, int parameterIndex, BigDecimal x)
			throws SQLException
	{
		getDelegate().setBigDecimal(subject, parameterIndex, x);
	}

	@Override
	public void setFetchSize(S subject, int rows) throws SQLException
	{
		getDelegate().setFetchSize(subject, rows);
	}

	@Override
	public void setString(S subject, int parameterIndex, String x)
			throws SQLException
	{
		getDelegate().setString(subject, parameterIndex, x);
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
	public void setBytes(S subject, int parameterIndex, byte[] x)
			throws SQLException
	{
		getDelegate().setBytes(subject, parameterIndex, x);
	}

	@Override
	public int getResultSetType(S subject) throws SQLException
	{
		return getDelegate().getResultSetType(subject);
	}

	@Override
	public void setDate(S subject, int parameterIndex, Date x)
			throws SQLException
	{
		getDelegate().setDate(subject, parameterIndex, x);
	}

	@Override
	public void addBatch(S subject, String sql) throws SQLException
	{
		getDelegate().addBatch(subject, sql);
	}

	@Override
	public void setTime(S subject, int parameterIndex, Time x)
			throws SQLException
	{
		getDelegate().setTime(subject, parameterIndex, x);
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
	public void setTimestamp(S subject, int parameterIndex, Timestamp x)
			throws SQLException
	{
		getDelegate().setTimestamp(subject, parameterIndex, x);
	}

	@Override
	public boolean getMoreResults(S subject, int current) throws SQLException
	{
		return getDelegate().getMoreResults(subject, current);
	}

	@Override
	public void setAsciiStream(S subject, int parameterIndex, InputStream x,
			int length) throws SQLException
	{
		getDelegate().setAsciiStream(subject, parameterIndex, x, length);
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
	public void setUnicodeStream(S subject, int parameterIndex, InputStream x,
			int length) throws SQLException
	{
		getDelegate().setUnicodeStream(subject, parameterIndex, x, length);
	}

	@Override
	public int executeUpdate(S subject, String sql, int[] columnIndexes)
			throws SQLException
	{
		return getDelegate().executeUpdate(subject, sql, columnIndexes);
	}

	@Override
	public void setBinaryStream(S subject, int parameterIndex, InputStream x,
			int length) throws SQLException
	{
		getDelegate().setBinaryStream(subject, parameterIndex, x, length);
	}

	@Override
	public int executeUpdate(S subject, String sql, String[] columnNames)
			throws SQLException
	{
		return getDelegate().executeUpdate(subject, sql, columnNames);
	}

	@Override
	public void clearParameters(S subject) throws SQLException
	{
		getDelegate().clearParameters(subject);
	}

	@Override
	public boolean execute(S subject, String sql, int autoGeneratedKeys)
			throws SQLException
	{
		return getDelegate().execute(subject, sql, autoGeneratedKeys);
	}

	@Override
	public void setObject(S subject, int parameterIndex, Object x,
			int targetSqlType) throws SQLException
	{
		getDelegate().setObject(subject, parameterIndex, x, targetSqlType);
	}

	@Override
	public boolean execute(S subject, String sql, int[] columnIndexes)
			throws SQLException
	{
		return getDelegate().execute(subject, sql, columnIndexes);
	}

	@Override
	public void setObject(S subject, int parameterIndex, Object x)
			throws SQLException
	{
		getDelegate().setObject(subject, parameterIndex, x);
	}

	@Override
	public boolean execute(S subject, String sql, String[] columnNames)
			throws SQLException
	{
		return getDelegate().execute(subject, sql, columnNames);
	}

	@Override
	public boolean execute(S subject) throws SQLException
	{
		return getDelegate().execute(subject);
	}

	@Override
	public void addBatch(S subject) throws SQLException
	{
		getDelegate().addBatch(subject);
	}

	@Override
	public int getResultSetHoldability(S subject) throws SQLException
	{
		return getDelegate().getResultSetHoldability(subject);
	}

	@Override
	public void setCharacterStream(S subject, int parameterIndex, Reader reader,
			int length) throws SQLException
	{
		getDelegate().setCharacterStream(subject, parameterIndex, reader, length);
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
	public void setRef(S subject, int parameterIndex, Ref x) throws SQLException
	{
		getDelegate().setRef(subject, parameterIndex, x);
	}

	@Override
	public boolean isPoolable(S subject) throws SQLException
	{
		return getDelegate().isPoolable(subject);
	}

	@Override
	public void setBlob(S subject, int parameterIndex, Blob x)
			throws SQLException
	{
		getDelegate().setBlob(subject, parameterIndex, x);
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

	@Override
	public void setClob(S subject, int parameterIndex, Clob x)
			throws SQLException
	{
		getDelegate().setClob(subject, parameterIndex, x);
	}

	@Override
	public void setArray(S subject, int parameterIndex, Array x)
			throws SQLException
	{
		getDelegate().setArray(subject, parameterIndex, x);
	}

	@Override
	public ResultSetMetaData getMetaData(S subject) throws SQLException
	{
		return getDelegate().getMetaData(subject);
	}

	@Override
	public void setDate(S subject, int parameterIndex, Date x, Calendar cal)
			throws SQLException
	{
		getDelegate().setDate(subject, parameterIndex, x, cal);
	}

	@Override
	public void setTime(S subject, int parameterIndex, Time x, Calendar cal)
			throws SQLException
	{
		getDelegate().setTime(subject, parameterIndex, x, cal);
	}

	@Override
	public void setTimestamp(S subject, int parameterIndex, Timestamp x,
			Calendar cal) throws SQLException
	{
		getDelegate().setTimestamp(subject, parameterIndex, x, cal);
	}

	@Override
	public void setNull(S subject, int parameterIndex, int sqlType,
			String typeName) throws SQLException
	{
		getDelegate().setNull(subject, parameterIndex, sqlType, typeName);
	}

	@Override
	public void setURL(S subject, int parameterIndex, URL x) throws SQLException
	{
		getDelegate().setURL(subject, parameterIndex, x);
	}

	@Override
	public ParameterMetaData getParameterMetaData(S subject) throws SQLException
	{
		return getDelegate().getParameterMetaData(subject);
	}

	@Override
	public void setRowId(S subject, int parameterIndex, RowId x)
			throws SQLException
	{
		getDelegate().setRowId(subject, parameterIndex, x);
	}

	@Override
	public void setNString(S subject, int parameterIndex, String value)
			throws SQLException
	{
		getDelegate().setNString(subject, parameterIndex, value);
	}

	@Override
	public void setNCharacterStream(S subject, int parameterIndex, Reader value,
			long length) throws SQLException
	{
		getDelegate().setNCharacterStream(subject, parameterIndex, value, length);
	}

	@Override
	public void setNClob(S subject, int parameterIndex, NClob value)
			throws SQLException
	{
		getDelegate().setNClob(subject, parameterIndex, value);
	}

	@Override
	public void setClob(S subject, int parameterIndex, Reader reader,
			long length) throws SQLException
	{
		getDelegate().setClob(subject, parameterIndex, reader, length);
	}

	@Override
	public void setBlob(S subject, int parameterIndex, InputStream inputStream,
			long length) throws SQLException
	{
		getDelegate().setBlob(subject, parameterIndex, inputStream, length);
	}

	@Override
	public void setNClob(S subject, int parameterIndex, Reader reader,
			long length) throws SQLException
	{
		getDelegate().setNClob(subject, parameterIndex, reader, length);
	}

	@Override
	public void setSQLXML(S subject, int parameterIndex, SQLXML xmlObject)
			throws SQLException
	{
		getDelegate().setSQLXML(subject, parameterIndex, xmlObject);
	}

	@Override
	public void setObject(S subject, int parameterIndex, Object x,
			int targetSqlType, int scaleOrLength) throws SQLException
	{
		getDelegate().setObject(
				subject, parameterIndex, x, targetSqlType, scaleOrLength
		);
	}

	@Override
	public void setAsciiStream(S subject, int parameterIndex, InputStream x,
			long length) throws SQLException
	{
		getDelegate().setAsciiStream(subject, parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(S subject, int parameterIndex, InputStream x,
			long length) throws SQLException
	{
		getDelegate().setBinaryStream(subject, parameterIndex, x, length);
	}

	@Override
	public void setCharacterStream(S subject, int parameterIndex, Reader reader,
			long length) throws SQLException
	{
		getDelegate().setCharacterStream(subject, parameterIndex, reader, length);
	}

	@Override
	public void setAsciiStream(S subject, int parameterIndex, InputStream x)
			throws SQLException
	{
		getDelegate().setAsciiStream(subject, parameterIndex, x);
	}

	@Override
	public void setBinaryStream(S subject, int parameterIndex, InputStream x)
			throws SQLException
	{
		getDelegate().setBinaryStream(subject, parameterIndex, x);
	}

	@Override
	public void setCharacterStream(S subject, int parameterIndex, Reader reader)
			throws SQLException
	{
		getDelegate().setCharacterStream(subject, parameterIndex, reader);
	}

	@Override
	public void setNCharacterStream(S subject, int parameterIndex, Reader value)
			throws SQLException
	{
		getDelegate().setNCharacterStream(subject, parameterIndex, value);
	}

	@Override
	public void setClob(S subject, int parameterIndex, Reader reader)
			throws SQLException
	{
		getDelegate().setClob(subject, parameterIndex, reader);
	}

	@Override
	public void setBlob(S subject, int parameterIndex, InputStream inputStream)
			throws SQLException
	{
		getDelegate().setBlob(subject, parameterIndex, inputStream);
	}

	@Override
	public void setNClob(S subject, int parameterIndex, Reader reader)
			throws SQLException
	{
		getDelegate().setNClob(subject, parameterIndex, reader);
	}
}
