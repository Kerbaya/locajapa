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
import java.util.Map;

public abstract class DelegatingCallableStatementInterceptor<S> implements CallableStatementInterceptor<S>
{
	protected abstract CallableStatementInterceptor<? super S> getDelegate();
	
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
	public void registerOutParameter(S subject, int parameterIndex, int sqlType)
			throws SQLException
	{
		getDelegate().registerOutParameter(subject, parameterIndex, sqlType);
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
	public void registerOutParameter(S subject, int parameterIndex, int sqlType,
			int scale) throws SQLException
	{
		getDelegate().registerOutParameter(subject, parameterIndex, sqlType, scale);
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
	public boolean wasNull(S subject) throws SQLException
	{
		return getDelegate().wasNull(subject);
	}

	@Override
	public String getString(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getString(subject, parameterIndex);
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
	public boolean getBoolean(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getBoolean(subject, parameterIndex);
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
	public byte getByte(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getByte(subject, parameterIndex);
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
	public short getShort(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getShort(subject, parameterIndex);
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
	public int getInt(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getInt(subject, parameterIndex);
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
	public long getLong(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getLong(subject, parameterIndex);
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
	public float getFloat(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getFloat(subject, parameterIndex);
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
	public double getDouble(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getDouble(subject, parameterIndex);
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
	public BigDecimal getBigDecimal(S subject, int parameterIndex, int scale)
			throws SQLException
	{
		return getDelegate().getBigDecimal(subject, parameterIndex, scale);
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
	public byte[] getBytes(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getBytes(subject, parameterIndex);
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
	public Date getDate(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getDate(subject, parameterIndex);
	}

	@Override
	public int getResultSetType(S subject) throws SQLException
	{
		return getDelegate().getResultSetType(subject);
	}

	@Override
	public Time getTime(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getTime(subject, parameterIndex);
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
	public Timestamp getTimestamp(S subject, int parameterIndex)
			throws SQLException
	{
		return getDelegate().getTimestamp(subject, parameterIndex);
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
	public Object getObject(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getObject(subject, parameterIndex);
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
	public BigDecimal getBigDecimal(S subject, int parameterIndex)
			throws SQLException
	{
		return getDelegate().getBigDecimal(subject, parameterIndex);
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
	public Object getObject(S subject, int parameterIndex,
			Map<String, Class<?>> map) throws SQLException
	{
		return getDelegate().getObject(subject, parameterIndex, map);
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
	public Ref getRef(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getRef(subject, parameterIndex);
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
	public Blob getBlob(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getBlob(subject, parameterIndex);
	}

	@Override
	public int executeUpdate(S subject, String sql, String[] columnNames)
			throws SQLException
	{
		return getDelegate().executeUpdate(subject, sql, columnNames);
	}

	@Override
	public Clob getClob(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getClob(subject, parameterIndex);
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
	public Array getArray(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getArray(subject, parameterIndex);
	}

	@Override
	public void setObject(S subject, int parameterIndex, Object x,
			int targetSqlType) throws SQLException
	{
		getDelegate().setObject(subject, parameterIndex, x, targetSqlType);
	}

	@Override
	public Date getDate(S subject, int parameterIndex, Calendar cal)
			throws SQLException
	{
		return getDelegate().getDate(subject, parameterIndex, cal);
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
	public Time getTime(S subject, int parameterIndex, Calendar cal)
			throws SQLException
	{
		return getDelegate().getTime(subject, parameterIndex, cal);
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
	public Timestamp getTimestamp(S subject, int parameterIndex, Calendar cal)
			throws SQLException
	{
		return getDelegate().getTimestamp(subject, parameterIndex, cal);
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
	public void registerOutParameter(S subject, int parameterIndex, int sqlType,
			String typeName) throws SQLException
	{
		getDelegate().registerOutParameter(
				subject, parameterIndex, sqlType, typeName
		);
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
	public void registerOutParameter(S subject, String parameterName,
			int sqlType) throws SQLException
	{
		getDelegate().registerOutParameter(subject, parameterName, sqlType);
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
	public void registerOutParameter(S subject, String parameterName,
			int sqlType, int scale) throws SQLException
	{
		getDelegate().registerOutParameter(subject, parameterName, sqlType, scale);
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
	public void registerOutParameter(S subject, String parameterName,
			int sqlType, String typeName) throws SQLException
	{
		getDelegate().registerOutParameter(
				subject, parameterName, sqlType, typeName
		);
	}

	@Override
	public ResultSetMetaData getMetaData(S subject) throws SQLException
	{
		return getDelegate().getMetaData(subject);
	}

	@Override
	public URL getURL(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getURL(subject, parameterIndex);
	}

	@Override
	public void setDate(S subject, int parameterIndex, Date x, Calendar cal)
			throws SQLException
	{
		getDelegate().setDate(subject, parameterIndex, x, cal);
	}

	@Override
	public void setURL(S subject, String parameterName, URL val)
			throws SQLException
	{
		getDelegate().setURL(subject, parameterName, val);
	}

	@Override
	public void setTime(S subject, int parameterIndex, Time x, Calendar cal)
			throws SQLException
	{
		getDelegate().setTime(subject, parameterIndex, x, cal);
	}

	@Override
	public void setNull(S subject, String parameterName, int sqlType)
			throws SQLException
	{
		getDelegate().setNull(subject, parameterName, sqlType);
	}

	@Override
	public void setTimestamp(S subject, int parameterIndex, Timestamp x,
			Calendar cal) throws SQLException
	{
		getDelegate().setTimestamp(subject, parameterIndex, x, cal);
	}

	@Override
	public void setBoolean(S subject, String parameterName, boolean x)
			throws SQLException
	{
		getDelegate().setBoolean(subject, parameterName, x);
	}

	@Override
	public void setNull(S subject, int parameterIndex, int sqlType,
			String typeName) throws SQLException
	{
		getDelegate().setNull(subject, parameterIndex, sqlType, typeName);
	}

	@Override
	public void setByte(S subject, String parameterName, byte x)
			throws SQLException
	{
		getDelegate().setByte(subject, parameterName, x);
	}

	@Override
	public void setURL(S subject, int parameterIndex, URL x) throws SQLException
	{
		getDelegate().setURL(subject, parameterIndex, x);
	}

	@Override
	public void setShort(S subject, String parameterName, short x)
			throws SQLException
	{
		getDelegate().setShort(subject, parameterName, x);
	}

	@Override
	public ParameterMetaData getParameterMetaData(S subject) throws SQLException
	{
		return getDelegate().getParameterMetaData(subject);
	}

	@Override
	public void setInt(S subject, String parameterName, int x)
			throws SQLException
	{
		getDelegate().setInt(subject, parameterName, x);
	}

	@Override
	public void setRowId(S subject, int parameterIndex, RowId x)
			throws SQLException
	{
		getDelegate().setRowId(subject, parameterIndex, x);
	}

	@Override
	public void setLong(S subject, String parameterName, long x)
			throws SQLException
	{
		getDelegate().setLong(subject, parameterName, x);
	}

	@Override
	public void setNString(S subject, int parameterIndex, String value)
			throws SQLException
	{
		getDelegate().setNString(subject, parameterIndex, value);
	}

	@Override
	public void setFloat(S subject, String parameterName, float x)
			throws SQLException
	{
		getDelegate().setFloat(subject, parameterName, x);
	}

	@Override
	public void setNCharacterStream(S subject, int parameterIndex, Reader value,
			long length) throws SQLException
	{
		getDelegate().setNCharacterStream(subject, parameterIndex, value, length);
	}

	@Override
	public void setDouble(S subject, String parameterName, double x)
			throws SQLException
	{
		getDelegate().setDouble(subject, parameterName, x);
	}

	@Override
	public void setBigDecimal(S subject, String parameterName, BigDecimal x)
			throws SQLException
	{
		getDelegate().setBigDecimal(subject, parameterName, x);
	}

	@Override
	public void setNClob(S subject, int parameterIndex, NClob value)
			throws SQLException
	{
		getDelegate().setNClob(subject, parameterIndex, value);
	}

	@Override
	public void setString(S subject, String parameterName, String x)
			throws SQLException
	{
		getDelegate().setString(subject, parameterName, x);
	}

	@Override
	public void setClob(S subject, int parameterIndex, Reader reader,
			long length) throws SQLException
	{
		getDelegate().setClob(subject, parameterIndex, reader, length);
	}

	@Override
	public void setBytes(S subject, String parameterName, byte[] x)
			throws SQLException
	{
		getDelegate().setBytes(subject, parameterName, x);
	}

	@Override
	public void setBlob(S subject, int parameterIndex, InputStream inputStream,
			long length) throws SQLException
	{
		getDelegate().setBlob(subject, parameterIndex, inputStream, length);
	}

	@Override
	public void setDate(S subject, String parameterName, Date x)
			throws SQLException
	{
		getDelegate().setDate(subject, parameterName, x);
	}

	@Override
	public void setNClob(S subject, int parameterIndex, Reader reader,
			long length) throws SQLException
	{
		getDelegate().setNClob(subject, parameterIndex, reader, length);
	}

	@Override
	public void setTime(S subject, String parameterName, Time x)
			throws SQLException
	{
		getDelegate().setTime(subject, parameterName, x);
	}

	@Override
	public void setTimestamp(S subject, String parameterName, Timestamp x)
			throws SQLException
	{
		getDelegate().setTimestamp(subject, parameterName, x);
	}

	@Override
	public void setSQLXML(S subject, int parameterIndex, SQLXML xmlObject)
			throws SQLException
	{
		getDelegate().setSQLXML(subject, parameterIndex, xmlObject);
	}

	@Override
	public void setAsciiStream(S subject, String parameterName, InputStream x,
			int length) throws SQLException
	{
		getDelegate().setAsciiStream(subject, parameterName, x, length);
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
	public void setBinaryStream(S subject, String parameterName, InputStream x,
			int length) throws SQLException
	{
		getDelegate().setBinaryStream(subject, parameterName, x, length);
	}

	@Override
	public void setAsciiStream(S subject, int parameterIndex, InputStream x,
			long length) throws SQLException
	{
		getDelegate().setAsciiStream(subject, parameterIndex, x, length);
	}

	@Override
	public void setObject(S subject, String parameterName, Object x,
			int targetSqlType, int scale) throws SQLException
	{
		getDelegate().setObject(subject, parameterName, x, targetSqlType, scale);
	}

	@Override
	public void setBinaryStream(S subject, int parameterIndex, InputStream x,
			long length) throws SQLException
	{
		getDelegate().setBinaryStream(subject, parameterIndex, x, length);
	}

	@Override
	public void setObject(S subject, String parameterName, Object x,
			int targetSqlType) throws SQLException
	{
		getDelegate().setObject(subject, parameterName, x, targetSqlType);
	}

	@Override
	public void setCharacterStream(S subject, int parameterIndex, Reader reader,
			long length) throws SQLException
	{
		getDelegate().setCharacterStream(subject, parameterIndex, reader, length);
	}

	@Override
	public void setObject(S subject, String parameterName, Object x)
			throws SQLException
	{
		getDelegate().setObject(subject, parameterName, x);
	}

	@Override
	public void setAsciiStream(S subject, int parameterIndex, InputStream x)
			throws SQLException
	{
		getDelegate().setAsciiStream(subject, parameterIndex, x);
	}

	@Override
	public void setCharacterStream(S subject, String parameterName,
			Reader reader, int length) throws SQLException
	{
		getDelegate().setCharacterStream(subject, parameterName, reader, length);
	}

	@Override
	public void setBinaryStream(S subject, int parameterIndex, InputStream x)
			throws SQLException
	{
		getDelegate().setBinaryStream(subject, parameterIndex, x);
	}

	@Override
	public void setDate(S subject, String parameterName, Date x, Calendar cal)
			throws SQLException
	{
		getDelegate().setDate(subject, parameterName, x, cal);
	}

	@Override
	public void setCharacterStream(S subject, int parameterIndex, Reader reader)
			throws SQLException
	{
		getDelegate().setCharacterStream(subject, parameterIndex, reader);
	}

	@Override
	public void setTime(S subject, String parameterName, Time x, Calendar cal)
			throws SQLException
	{
		getDelegate().setTime(subject, parameterName, x, cal);
	}

	@Override
	public void setNCharacterStream(S subject, int parameterIndex, Reader value)
			throws SQLException
	{
		getDelegate().setNCharacterStream(subject, parameterIndex, value);
	}

	@Override
	public void setTimestamp(S subject, String parameterName, Timestamp x,
			Calendar cal) throws SQLException
	{
		getDelegate().setTimestamp(subject, parameterName, x, cal);
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
	public void setNull(S subject, String parameterName, int sqlType,
			String typeName) throws SQLException
	{
		getDelegate().setNull(subject, parameterName, sqlType, typeName);
	}

	@Override
	public void setNClob(S subject, int parameterIndex, Reader reader)
			throws SQLException
	{
		getDelegate().setNClob(subject, parameterIndex, reader);
	}

	@Override
	public String getString(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getString(subject, parameterName);
	}

	@Override
	public boolean getBoolean(S subject, String parameterName)
			throws SQLException
	{
		return getDelegate().getBoolean(subject, parameterName);
	}

	@Override
	public byte getByte(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getByte(subject, parameterName);
	}

	@Override
	public short getShort(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getShort(subject, parameterName);
	}

	@Override
	public int getInt(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getInt(subject, parameterName);
	}

	@Override
	public long getLong(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getLong(subject, parameterName);
	}

	@Override
	public float getFloat(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getFloat(subject, parameterName);
	}

	@Override
	public double getDouble(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getDouble(subject, parameterName);
	}

	@Override
	public byte[] getBytes(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getBytes(subject, parameterName);
	}

	@Override
	public Date getDate(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getDate(subject, parameterName);
	}

	@Override
	public Time getTime(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getTime(subject, parameterName);
	}

	@Override
	public Timestamp getTimestamp(S subject, String parameterName)
			throws SQLException
	{
		return getDelegate().getTimestamp(subject, parameterName);
	}

	@Override
	public Object getObject(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getObject(subject, parameterName);
	}

	@Override
	public BigDecimal getBigDecimal(S subject, String parameterName)
			throws SQLException
	{
		return getDelegate().getBigDecimal(subject, parameterName);
	}

	@Override
	public Object getObject(S subject, String parameterName,
			Map<String, Class<?>> map) throws SQLException
	{
		return getDelegate().getObject(subject, parameterName, map);
	}

	@Override
	public Ref getRef(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getRef(subject, parameterName);
	}

	@Override
	public Blob getBlob(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getBlob(subject, parameterName);
	}

	@Override
	public Clob getClob(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getClob(subject, parameterName);
	}

	@Override
	public Array getArray(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getArray(subject, parameterName);
	}

	@Override
	public Date getDate(S subject, String parameterName, Calendar cal)
			throws SQLException
	{
		return getDelegate().getDate(subject, parameterName, cal);
	}

	@Override
	public Time getTime(S subject, String parameterName, Calendar cal)
			throws SQLException
	{
		return getDelegate().getTime(subject, parameterName, cal);
	}

	@Override
	public Timestamp getTimestamp(S subject, String parameterName, Calendar cal)
			throws SQLException
	{
		return getDelegate().getTimestamp(subject, parameterName, cal);
	}

	@Override
	public URL getURL(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getURL(subject, parameterName);
	}

	@Override
	public RowId getRowId(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getRowId(subject, parameterIndex);
	}

	@Override
	public RowId getRowId(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getRowId(subject, parameterName);
	}

	@Override
	public void setRowId(S subject, String parameterName, RowId x)
			throws SQLException
	{
		getDelegate().setRowId(subject, parameterName, x);
	}

	@Override
	public void setNString(S subject, String parameterName, String value)
			throws SQLException
	{
		getDelegate().setNString(subject, parameterName, value);
	}

	@Override
	public void setNCharacterStream(S subject, String parameterName,
			Reader value, long length) throws SQLException
	{
		getDelegate().setNCharacterStream(subject, parameterName, value, length);
	}

	@Override
	public void setNClob(S subject, String parameterName, NClob value)
			throws SQLException
	{
		getDelegate().setNClob(subject, parameterName, value);
	}

	@Override
	public void setClob(S subject, String parameterName, Reader reader,
			long length) throws SQLException
	{
		getDelegate().setClob(subject, parameterName, reader, length);
	}

	@Override
	public void setBlob(S subject, String parameterName,
			InputStream inputStream, long length) throws SQLException
	{
		getDelegate().setBlob(subject, parameterName, inputStream, length);
	}

	@Override
	public void setNClob(S subject, String parameterName, Reader reader,
			long length) throws SQLException
	{
		getDelegate().setNClob(subject, parameterName, reader, length);
	}

	@Override
	public NClob getNClob(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getNClob(subject, parameterIndex);
	}

	@Override
	public NClob getNClob(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getNClob(subject, parameterName);
	}

	@Override
	public void setSQLXML(S subject, String parameterName, SQLXML xmlObject)
			throws SQLException
	{
		getDelegate().setSQLXML(subject, parameterName, xmlObject);
	}

	@Override
	public SQLXML getSQLXML(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getSQLXML(subject, parameterIndex);
	}

	@Override
	public SQLXML getSQLXML(S subject, String parameterName) throws SQLException
	{
		return getDelegate().getSQLXML(subject, parameterName);
	}

	@Override
	public String getNString(S subject, int parameterIndex) throws SQLException
	{
		return getDelegate().getNString(subject, parameterIndex);
	}

	@Override
	public String getNString(S subject, String parameterName)
			throws SQLException
	{
		return getDelegate().getNString(subject, parameterName);
	}

	@Override
	public Reader getNCharacterStream(S subject, int parameterIndex)
			throws SQLException
	{
		return getDelegate().getNCharacterStream(subject, parameterIndex);
	}

	@Override
	public Reader getNCharacterStream(S subject, String parameterName)
			throws SQLException
	{
		return getDelegate().getNCharacterStream(subject, parameterName);
	}

	@Override
	public Reader getCharacterStream(S subject, int parameterIndex)
			throws SQLException
	{
		return getDelegate().getCharacterStream(subject, parameterIndex);
	}

	@Override
	public Reader getCharacterStream(S subject, String parameterName)
			throws SQLException
	{
		return getDelegate().getCharacterStream(subject, parameterName);
	}

	@Override
	public void setBlob(S subject, String parameterName, Blob x)
			throws SQLException
	{
		getDelegate().setBlob(subject, parameterName, x);
	}

	@Override
	public void setClob(S subject, String parameterName, Clob x)
			throws SQLException
	{
		getDelegate().setClob(subject, parameterName, x);
	}

	@Override
	public void setAsciiStream(S subject, String parameterName, InputStream x,
			long length) throws SQLException
	{
		getDelegate().setAsciiStream(subject, parameterName, x, length);
	}

	@Override
	public void setBinaryStream(S subject, String parameterName, InputStream x,
			long length) throws SQLException
	{
		getDelegate().setBinaryStream(subject, parameterName, x, length);
	}

	@Override
	public void setCharacterStream(S subject, String parameterName,
			Reader reader, long length) throws SQLException
	{
		getDelegate().setCharacterStream(subject, parameterName, reader, length);
	}

	@Override
	public void setAsciiStream(S subject, String parameterName, InputStream x)
			throws SQLException
	{
		getDelegate().setAsciiStream(subject, parameterName, x);
	}

	@Override
	public void setBinaryStream(S subject, String parameterName, InputStream x)
			throws SQLException
	{
		getDelegate().setBinaryStream(subject, parameterName, x);
	}

	@Override
	public void setCharacterStream(S subject, String parameterName,
			Reader reader) throws SQLException
	{
		getDelegate().setCharacterStream(subject, parameterName, reader);
	}

	@Override
	public void setNCharacterStream(S subject, String parameterName,
			Reader value) throws SQLException
	{
		getDelegate().setNCharacterStream(subject, parameterName, value);
	}

	@Override
	public void setClob(S subject, String parameterName, Reader reader)
			throws SQLException
	{
		getDelegate().setClob(subject, parameterName, reader);
	}

	@Override
	public void setBlob(S subject, String parameterName,
			InputStream inputStream) throws SQLException
	{
		getDelegate().setBlob(subject, parameterName, inputStream);
	}

	@Override
	public void setNClob(S subject, String parameterName, Reader reader)
			throws SQLException
	{
		getDelegate().setNClob(subject, parameterName, reader);
	}

	@Override
	public <T> T getObject(S subject, int parameterIndex, Class<T> type)
			throws SQLException
	{
		return getDelegate().getObject(subject, parameterIndex, type);
	}

	@Override
	public <T> T getObject(S subject, String parameterName, Class<T> type)
			throws SQLException
	{
		return getDelegate().getObject(subject, parameterName, type);
	}
}
