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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

final class CallableStatementImpl 
		extends PreparedStatementImpl<CallableStatement, CallableStatementInterceptor<? super CallableStatement>>
		implements CallableStatement
{
	
	public CallableStatementImpl(
			CallableStatement wrapped,
			CallableStatementInterceptor<? super CallableStatement> ix)
	{
		super(wrapped, ix);
	}
	
	@Override
	public void registerOutParameter(int parameterIndex, int sqlType)
			throws SQLException
	{
		ix.registerOutParameter(wrapped, parameterIndex, sqlType);
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType, int scale)
			throws SQLException
	{
		ix.registerOutParameter(wrapped, parameterIndex, sqlType, scale);
	}

	@Override
	public boolean wasNull() throws SQLException
	{
		return ix.wasNull(wrapped);
	}

	@Override
	public String getString(int parameterIndex) throws SQLException
	{
		return ix.getString(wrapped, parameterIndex);
	}

	@Override
	public boolean getBoolean(int parameterIndex) throws SQLException
	{
		return ix.getBoolean(wrapped, parameterIndex);
	}

	@Override
	public byte getByte(int parameterIndex) throws SQLException
	{
		return ix.getByte(wrapped, parameterIndex);
	}

	@Override
	public short getShort(int parameterIndex) throws SQLException
	{
		return ix.getShort(wrapped, parameterIndex);
	}

	@Override
	public int getInt(int parameterIndex) throws SQLException
	{
		return ix.getInt(wrapped, parameterIndex);
	}

	@Override
	public long getLong(int parameterIndex) throws SQLException
	{
		return ix.getLong(wrapped, parameterIndex);
	}

	@Override
	public float getFloat(int parameterIndex) throws SQLException
	{
		return ix.getFloat(wrapped, parameterIndex);
	}

	@Override
	public double getDouble(int parameterIndex) throws SQLException
	{
		return ix.getDouble(wrapped, parameterIndex);
	}

	@Deprecated
	@Override
	public BigDecimal getBigDecimal(int parameterIndex, int scale)
			throws SQLException
	{
		return ix.getBigDecimal(wrapped, parameterIndex, scale);
	}

	@Override
	public byte[] getBytes(int parameterIndex) throws SQLException
	{
		return ix.getBytes(wrapped, parameterIndex);
	}

	@Override
	public Date getDate(int parameterIndex) throws SQLException
	{
		return ix.getDate(wrapped, parameterIndex);
	}

	@Override
	public Time getTime(int parameterIndex) throws SQLException
	{
		return ix.getTime(wrapped, parameterIndex);
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex) throws SQLException
	{
		return ix.getTimestamp(wrapped, parameterIndex);
	}

	@Override
	public Object getObject(int parameterIndex) throws SQLException
	{
		return ix.getObject(wrapped, parameterIndex);
	}

	@Override
	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException
	{
		return ix.getBigDecimal(wrapped, parameterIndex);
	}

	@Override
	public Object getObject(int parameterIndex, Map<String, Class<?>> map)
			throws SQLException
	{
		return ix.getObject(wrapped, parameterIndex, map);
	}

	@Override
	public Ref getRef(int parameterIndex) throws SQLException
	{
		return ix.getRef(wrapped, parameterIndex);
	}

	@Override
	public Blob getBlob(int parameterIndex) throws SQLException
	{
		return ix.getBlob(wrapped, parameterIndex);
	}

	@Override
	public Clob getClob(int parameterIndex) throws SQLException
	{
		return ix.getClob(wrapped, parameterIndex);
	}

	@Override
	public Array getArray(int parameterIndex) throws SQLException
	{
		return ix.getArray(wrapped, parameterIndex);
	}

	@Override
	public Date getDate(int parameterIndex, Calendar cal) throws SQLException
	{
		return ix.getDate(wrapped, parameterIndex, cal);
	}

	@Override
	public Time getTime(int parameterIndex, Calendar cal) throws SQLException
	{
		return ix.getTime(wrapped, parameterIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex, Calendar cal)
			throws SQLException
	{
		return ix.getTimestamp(wrapped, parameterIndex, cal);
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType,
			String typeName) throws SQLException
	{
		ix.registerOutParameter(wrapped, parameterIndex, sqlType, typeName);
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType)
			throws SQLException
	{
		ix.registerOutParameter(wrapped, parameterName, sqlType);
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType,
			int scale) throws SQLException
	{
		ix.registerOutParameter(wrapped, parameterName, sqlType, scale);
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType,
			String typeName) throws SQLException
	{
		ix.registerOutParameter(wrapped, parameterName, sqlType, typeName);
	}

	@Override
	public URL getURL(int parameterIndex) throws SQLException
	{
		return ix.getURL(wrapped, parameterIndex);
	}

	@Override
	public void setURL(String parameterName, URL val) throws SQLException
	{
		ix.setURL(wrapped, parameterName, val);
	}

	@Override
	public void setNull(String parameterName, int sqlType) throws SQLException
	{
		ix.setNull(wrapped, parameterName, sqlType);
	}

	@Override
	public void setBoolean(String parameterName, boolean x) throws SQLException
	{
		ix.setBoolean(wrapped, parameterName, x);
	}

	@Override
	public void setByte(String parameterName, byte x) throws SQLException
	{
		ix.setByte(wrapped, parameterName, x);
	}

	@Override
	public void setShort(String parameterName, short x) throws SQLException
	{
		ix.setShort(wrapped, parameterName, x);
	}

	@Override
	public void setInt(String parameterName, int x) throws SQLException
	{
		ix.setInt(wrapped, parameterName, x);
	}

	@Override
	public void setLong(String parameterName, long x) throws SQLException
	{
		ix.setLong(wrapped, parameterName, x);
	}

	@Override
	public void setFloat(String parameterName, float x) throws SQLException
	{
		ix.setFloat(wrapped, parameterName, x);
	}

	@Override
	public void setDouble(String parameterName, double x) throws SQLException
	{
		ix.setDouble(wrapped, parameterName, x);
	}

	@Override
	public void setBigDecimal(String parameterName, BigDecimal x)
			throws SQLException
	{
		ix.setBigDecimal(wrapped, parameterName, x);
	}

	@Override
	public void setString(String parameterName, String x) throws SQLException
	{
		ix.setString(wrapped, parameterName, x);
	}

	@Override
	public void setBytes(String parameterName, byte[] x) throws SQLException
	{
		ix.setBytes(wrapped, parameterName, x);
	}

	@Override
	public void setDate(String parameterName, Date x) throws SQLException
	{
		ix.setDate(wrapped, parameterName, x);
	}

	@Override
	public void setTime(String parameterName, Time x) throws SQLException
	{
		ix.setTime(wrapped, parameterName, x);
	}

	@Override
	public void setTimestamp(String parameterName, Timestamp x)
			throws SQLException
	{
		ix.setTimestamp(wrapped, parameterName, x);
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, int length)
			throws SQLException
	{
		ix.setAsciiStream(wrapped, parameterName, x, length);
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x, int length)
			throws SQLException
	{
		ix.setBinaryStream(wrapped, parameterName, x, length);
	}

	@Override
	public void setObject(String parameterName, Object x, int targetSqlType,
			int scale) throws SQLException
	{
		ix.setObject(wrapped, parameterName, x, targetSqlType, scale);
	}

	@Override
	public void setObject(String parameterName, Object x, int targetSqlType)
			throws SQLException
	{
		ix.setObject(wrapped, parameterName, x, targetSqlType);
	}

	@Override
	public void setObject(String parameterName, Object x) throws SQLException
	{
		ix.setObject(wrapped, parameterName, x);
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader,
			int length) throws SQLException
	{
		ix.setCharacterStream(wrapped, parameterName, reader, length);
	}

	@Override
	public void setDate(String parameterName, Date x, Calendar cal)
			throws SQLException
	{
		ix.setDate(wrapped, parameterName, x, cal);
	}

	@Override
	public void setTime(String parameterName, Time x, Calendar cal)
			throws SQLException
	{
		ix.setTime(wrapped, parameterName, x, cal);
	}

	@Override
	public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
			throws SQLException
	{
		ix.setTimestamp(wrapped, parameterName, x, cal);
	}

	@Override
	public void setNull(String parameterName, int sqlType, String typeName)
			throws SQLException
	{
		ix.setNull(wrapped, parameterName, sqlType, typeName);
	}

	@Override
	public String getString(String parameterName) throws SQLException
	{
		return ix.getString(wrapped, parameterName);
	}

	@Override
	public boolean getBoolean(String parameterName) throws SQLException
	{
		return ix.getBoolean(wrapped, parameterName);
	}

	@Override
	public byte getByte(String parameterName) throws SQLException
	{
		return ix.getByte(wrapped, parameterName);
	}

	@Override
	public short getShort(String parameterName) throws SQLException
	{
		return ix.getShort(wrapped, parameterName);
	}

	@Override
	public int getInt(String parameterName) throws SQLException
	{
		return ix.getInt(wrapped, parameterName);
	}

	@Override
	public long getLong(String parameterName) throws SQLException
	{
		return ix.getLong(wrapped, parameterName);
	}

	@Override
	public float getFloat(String parameterName) throws SQLException
	{
		return ix.getFloat(wrapped, parameterName);
	}

	@Override
	public double getDouble(String parameterName) throws SQLException
	{
		return ix.getDouble(wrapped, parameterName);
	}

	@Override
	public byte[] getBytes(String parameterName) throws SQLException
	{
		return ix.getBytes(wrapped, parameterName);
	}

	@Override
	public Date getDate(String parameterName) throws SQLException
	{
		return ix.getDate(wrapped, parameterName);
	}

	@Override
	public Time getTime(String parameterName) throws SQLException
	{
		return ix.getTime(wrapped, parameterName);
	}

	@Override
	public Timestamp getTimestamp(String parameterName) throws SQLException
	{
		return ix.getTimestamp(wrapped, parameterName);
	}

	@Override
	public Object getObject(String parameterName) throws SQLException
	{
		return ix.getObject(wrapped, parameterName);
	}

	@Override
	public BigDecimal getBigDecimal(String parameterName) throws SQLException
	{
		return ix.getBigDecimal(wrapped, parameterName);
	}

	@Override
	public Object getObject(String parameterName, Map<String, Class<?>> map)
			throws SQLException
	{
		return ix.getObject(wrapped, parameterName, map);
	}

	@Override
	public Ref getRef(String parameterName) throws SQLException
	{
		return ix.getRef(wrapped, parameterName);
	}

	@Override
	public Blob getBlob(String parameterName) throws SQLException
	{
		return ix.getBlob(wrapped, parameterName);
	}

	@Override
	public Clob getClob(String parameterName) throws SQLException
	{
		return ix.getClob(wrapped, parameterName);
	}

	@Override
	public Array getArray(String parameterName) throws SQLException
	{
		return ix.getArray(wrapped, parameterName);
	}

	@Override
	public Date getDate(String parameterName, Calendar cal) throws SQLException
	{
		return ix.getDate(wrapped, parameterName, cal);
	}

	@Override
	public Time getTime(String parameterName, Calendar cal) throws SQLException
	{
		return ix.getTime(wrapped, parameterName, cal);
	}

	@Override
	public Timestamp getTimestamp(String parameterName, Calendar cal)
			throws SQLException
	{
		return ix.getTimestamp(wrapped, parameterName, cal);
	}

	@Override
	public URL getURL(String parameterName) throws SQLException
	{
		return ix.getURL(wrapped, parameterName);
	}

	@Override
	public RowId getRowId(int parameterIndex) throws SQLException
	{
		return ix.getRowId(wrapped, parameterIndex);
	}

	@Override
	public RowId getRowId(String parameterName) throws SQLException
	{
		return ix.getRowId(wrapped, parameterName);
	}

	@Override
	public void setRowId(String parameterName, RowId x) throws SQLException
	{
		ix.setRowId(wrapped, parameterName, x);
	}

	@Override
	public void setNString(String parameterName, String value)
			throws SQLException
	{
		ix.setNString(wrapped, parameterName, value);
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value,
			long length) throws SQLException
	{
		ix.setNCharacterStream(wrapped, parameterName, value, length);
	}

	@Override
	public void setNClob(String parameterName, NClob value) throws SQLException
	{
		ix.setNClob(wrapped, parameterName, value);
	}

	@Override
	public void setClob(String parameterName, Reader reader, long length)
			throws SQLException
	{
		ix.setClob(wrapped, parameterName, reader, length);
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream,
			long length) throws SQLException
	{
		ix.setBlob(wrapped, parameterName, inputStream, length);
	}

	@Override
	public void setNClob(String parameterName, Reader reader, long length)
			throws SQLException
	{
		ix.setNClob(wrapped, parameterName, reader, length);
	}

	@Override
	public NClob getNClob(int parameterIndex) throws SQLException
	{
		return ix.getNClob(wrapped, parameterIndex);
	}

	@Override
	public NClob getNClob(String parameterName) throws SQLException
	{
		return ix.getNClob(wrapped, parameterName);
	}

	@Override
	public void setSQLXML(String parameterName, SQLXML xmlObject)
			throws SQLException
	{
		ix.setSQLXML(wrapped, parameterName, xmlObject);
	}

	@Override
	public SQLXML getSQLXML(int parameterIndex) throws SQLException
	{
		return ix.getSQLXML(wrapped, parameterIndex);
	}

	@Override
	public SQLXML getSQLXML(String parameterName) throws SQLException
	{
		return ix.getSQLXML(wrapped, parameterName);
	}

	@Override
	public String getNString(int parameterIndex) throws SQLException
	{
		return ix.getNString(wrapped, parameterIndex);
	}

	@Override
	public String getNString(String parameterName) throws SQLException
	{
		return ix.getNString(wrapped, parameterName);
	}

	@Override
	public Reader getNCharacterStream(int parameterIndex) throws SQLException
	{
		return ix.getNCharacterStream(wrapped, parameterIndex);
	}

	@Override
	public Reader getNCharacterStream(String parameterName) throws SQLException
	{
		return ix.getNCharacterStream(wrapped, parameterName);
	}

	@Override
	public Reader getCharacterStream(int parameterIndex) throws SQLException
	{
		return ix.getCharacterStream(wrapped, parameterIndex);
	}

	@Override
	public Reader getCharacterStream(String parameterName) throws SQLException
	{
		return ix.getCharacterStream(wrapped, parameterName);
	}

	@Override
	public void setBlob(String parameterName, Blob x) throws SQLException
	{
		ix.setBlob(wrapped, parameterName, x);
	}

	@Override
	public void setClob(String parameterName, Clob x) throws SQLException
	{
		ix.setClob(wrapped, parameterName, x);
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, long length)
			throws SQLException
	{
		ix.setAsciiStream(wrapped, parameterName, x, length);
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x,
			long length) throws SQLException
	{
		ix.setBinaryStream(wrapped, parameterName, x, length);
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader,
			long length) throws SQLException
	{
		ix.setCharacterStream(wrapped, parameterName, reader, length);
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x)
			throws SQLException
	{
		ix.setAsciiStream(wrapped, parameterName, x);
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x)
			throws SQLException
	{
		ix.setBinaryStream(wrapped, parameterName, x);
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader)
			throws SQLException
	{
		ix.setCharacterStream(wrapped, parameterName, reader);
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value)
			throws SQLException
	{
		ix.setNCharacterStream(wrapped, parameterName, value);
	}

	@Override
	public void setClob(String parameterName, Reader reader) throws SQLException
	{
		ix.setClob(wrapped, parameterName, reader);
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream)
			throws SQLException
	{
		ix.setBlob(wrapped, parameterName, inputStream);
	}

	@Override
	public void setNClob(String parameterName, Reader reader)
			throws SQLException
	{
		ix.setNClob(wrapped, parameterName, reader);
	}

	@Override
	public <T> T getObject(int parameterIndex, Class<T> type)
			throws SQLException
	{
		return ix.getObject(wrapped, parameterIndex, type);
	}

	@Override
	public <T> T getObject(String parameterName, Class<T> type)
			throws SQLException
	{
		return ix.getObject(wrapped, parameterName, type);
	}

}
