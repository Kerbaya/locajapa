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

public class CallableStatementInterceptorSupport 
		extends PreparedStatementInterceptorSupport<CallableStatement>
		implements CallableStatementInterceptor<CallableStatement>
{

	@Override
	public void registerOutParameter(CallableStatement subject,
			int parameterIndex, int sqlType) throws SQLException
	{
		subject.registerOutParameter(parameterIndex, sqlType);
	}

	@Override
	public void registerOutParameter(CallableStatement subject,
			int parameterIndex, int sqlType, int scale) throws SQLException
	{
		subject.registerOutParameter(parameterIndex, sqlType, scale);
	}

	@Override
	public boolean wasNull(CallableStatement subject) throws SQLException
	{
		return subject.wasNull();
	}

	@Override
	public String getString(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getString(parameterIndex);
	}

	@Override
	public boolean getBoolean(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getBoolean(parameterIndex);
	}

	@Override
	public byte getByte(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getByte(parameterIndex);
	}

	@Override
	public short getShort(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getShort(parameterIndex);
	}

	@Override
	public int getInt(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getInt(parameterIndex);
	}

	@Override
	public long getLong(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getLong(parameterIndex);
	}

	@Override
	public float getFloat(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getFloat(parameterIndex);
	}

	@Override
	public double getDouble(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getDouble(parameterIndex);
	}

	@Deprecated
	@Override
	public BigDecimal getBigDecimal(CallableStatement subject,
			int parameterIndex, int scale) throws SQLException
	{
		return subject.getBigDecimal(parameterIndex, scale);
	}

	@Override
	public byte[] getBytes(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getBytes(parameterIndex);
	}

	@Override
	public Date getDate(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getDate(parameterIndex);
	}

	@Override
	public Time getTime(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getTime(parameterIndex);
	}

	@Override
	public Timestamp getTimestamp(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getTimestamp(parameterIndex);
	}

	@Override
	public Object getObject(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getObject(parameterIndex);
	}

	@Override
	public BigDecimal getBigDecimal(CallableStatement subject,
			int parameterIndex) throws SQLException
	{
		return subject.getBigDecimal(parameterIndex);
	}

	@Override
	public Object getObject(CallableStatement subject, int parameterIndex,
			Map<String, Class<?>> map) throws SQLException
	{
		return subject.getObject(parameterIndex, map);
	}

	@Override
	public Ref getRef(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getRef(parameterIndex);
	}

	@Override
	public Blob getBlob(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getBlob(parameterIndex);
	}

	@Override
	public Clob getClob(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getClob(parameterIndex);
	}

	@Override
	public Array getArray(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getArray(parameterIndex);
	}

	@Override
	public Date getDate(CallableStatement subject, int parameterIndex,
			Calendar cal) throws SQLException
	{
		return subject.getDate(parameterIndex, cal);
	}

	@Override
	public Time getTime(CallableStatement subject, int parameterIndex,
			Calendar cal) throws SQLException
	{
		return subject.getTime(parameterIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(CallableStatement subject, int parameterIndex,
			Calendar cal) throws SQLException
	{
		return subject.getTimestamp(parameterIndex, cal);
	}

	@Override
	public void registerOutParameter(CallableStatement subject,
			int parameterIndex, int sqlType, String typeName)
			throws SQLException
	{
		subject.registerOutParameter(parameterIndex, sqlType, typeName);
	}

	@Override
	public void registerOutParameter(CallableStatement subject,
			String parameterName, int sqlType) throws SQLException
	{
		subject.registerOutParameter(parameterName, sqlType);
	}

	@Override
	public void registerOutParameter(CallableStatement subject,
			String parameterName, int sqlType, int scale) throws SQLException
	{
		subject.registerOutParameter(parameterName, sqlType);
	}

	@Override
	public void registerOutParameter(CallableStatement subject,
			String parameterName, int sqlType, String typeName)
			throws SQLException
	{
		subject.registerOutParameter(parameterName, sqlType, typeName);
	}

	@Override
	public URL getURL(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getURL(parameterIndex);
	}

	@Override
	public void setURL(CallableStatement subject, String parameterName, URL val)
			throws SQLException
	{
		subject.setURL(parameterName, val);
	}

	@Override
	public void setNull(CallableStatement subject, String parameterName,
			int sqlType) throws SQLException
	{
		subject.setNull(parameterName, sqlType);
	}

	@Override
	public void setBoolean(CallableStatement subject, String parameterName,
			boolean x) throws SQLException
	{
		subject.setBoolean(parameterName, x);
	}

	@Override
	public void setByte(CallableStatement subject, String parameterName, byte x)
			throws SQLException
	{
		subject.setByte(parameterName, x);
	}

	@Override
	public void setShort(CallableStatement subject, String parameterName,
			short x) throws SQLException
	{
		subject.setShort(parameterName, x);
	}

	@Override
	public void setInt(CallableStatement subject, String parameterName, int x)
			throws SQLException
	{
		subject.setInt(parameterName, x);
	}

	@Override
	public void setLong(CallableStatement subject, String parameterName, long x)
			throws SQLException
	{
		subject.setLong(parameterName, x);
	}

	@Override
	public void setFloat(CallableStatement subject, String parameterName,
			float x) throws SQLException
	{
		subject.setFloat(parameterName, x);
	}

	@Override
	public void setDouble(CallableStatement subject, String parameterName,
			double x) throws SQLException
	{
		subject.setDouble(parameterName, x);
	}

	@Override
	public void setBigDecimal(CallableStatement subject, String parameterName,
			BigDecimal x) throws SQLException
	{
		subject.setBigDecimal(parameterName, x);
	}

	@Override
	public void setString(CallableStatement subject, String parameterName,
			String x) throws SQLException
	{
		subject.setString(parameterName, x);
	}

	@Override
	public void setBytes(CallableStatement subject, String parameterName,
			byte[] x) throws SQLException
	{
		subject.setBytes(parameterName, x);
	}

	@Override
	public void setDate(CallableStatement subject, String parameterName, Date x)
			throws SQLException
	{
		subject.setDate(parameterName, x);
	}

	@Override
	public void setTime(CallableStatement subject, String parameterName, Time x)
			throws SQLException
	{
		subject.setTime(parameterName, x);
	}

	@Override
	public void setTimestamp(CallableStatement subject, String parameterName,
			Timestamp x) throws SQLException
	{
		subject.setTimestamp(parameterName, x);
	}

	@Override
	public void setAsciiStream(CallableStatement subject, String parameterName,
			InputStream x, int length) throws SQLException
	{
		subject.setAsciiStream(parameterName, x, length);
	}

	@Override
	public void setBinaryStream(CallableStatement subject, String parameterName,
			InputStream x, int length) throws SQLException
	{
		subject.setBinaryStream(parameterName, x, length);
	}

	@Override
	public void setObject(CallableStatement subject, String parameterName,
			Object x, int targetSqlType, int scale) throws SQLException
	{
		subject.setObject(parameterName, x, targetSqlType, scale);
	}

	@Override
	public void setObject(CallableStatement subject, String parameterName,
			Object x, int targetSqlType) throws SQLException
	{
		subject.setObject(parameterName, x, targetSqlType);
	}

	@Override
	public void setObject(CallableStatement subject, String parameterName,
			Object x) throws SQLException
	{
		subject.setObject(parameterName, x);
	}

	@Override
	public void setCharacterStream(CallableStatement subject,
			String parameterName, Reader reader, int length) throws SQLException
	{
		subject.setCharacterStream(parameterName, reader, length);
	}

	@Override
	public void setDate(CallableStatement subject, String parameterName, Date x,
			Calendar cal) throws SQLException
	{
		subject.setDate(parameterName, x, cal);
	}

	@Override
	public void setTime(CallableStatement subject, String parameterName, Time x,
			Calendar cal) throws SQLException
	{
		subject.setTime(parameterName, x, cal);
	}

	@Override
	public void setTimestamp(CallableStatement subject, String parameterName,
			Timestamp x, Calendar cal) throws SQLException
	{
		subject.setTimestamp(parameterName, x, cal);
	}

	@Override
	public void setNull(CallableStatement subject, String parameterName,
			int sqlType, String typeName) throws SQLException
	{
		subject.setNull(parameterName, sqlType, typeName);
	}

	@Override
	public String getString(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getString(parameterName);
	}

	@Override
	public boolean getBoolean(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getBoolean(parameterName);
	}

	@Override
	public byte getByte(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getByte(parameterName);
	}

	@Override
	public short getShort(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getShort(parameterName);
	}

	@Override
	public int getInt(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getInt(parameterName);
	}

	@Override
	public long getLong(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getLong(parameterName);
	}

	@Override
	public float getFloat(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getFloat(parameterName);
	}

	@Override
	public double getDouble(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getDouble(parameterName);
	}

	@Override
	public byte[] getBytes(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getBytes(parameterName);
	}

	@Override
	public Date getDate(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getDate(parameterName);
	}

	@Override
	public Time getTime(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getTime(parameterName);
	}

	@Override
	public Timestamp getTimestamp(CallableStatement subject,
			String parameterName) throws SQLException
	{
		return subject.getTimestamp(parameterName);
	}

	@Override
	public Object getObject(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getObject(parameterName);
	}

	@Override
	public BigDecimal getBigDecimal(CallableStatement subject,
			String parameterName) throws SQLException
	{
		return subject.getBigDecimal(parameterName);
	}

	@Override
	public Object getObject(CallableStatement subject, String parameterName,
			Map<String, Class<?>> map) throws SQLException
	{
		return subject.getObject(parameterName, map);
	}

	@Override
	public Ref getRef(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getRef(parameterName);
	}

	@Override
	public Blob getBlob(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getBlob(parameterName);
	}

	@Override
	public Clob getClob(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getClob(parameterName);
	}

	@Override
	public Array getArray(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getArray(parameterName);
	}

	@Override
	public Date getDate(CallableStatement subject, String parameterName,
			Calendar cal) throws SQLException
	{
		return subject.getDate(parameterName, cal);
	}

	@Override
	public Time getTime(CallableStatement subject, String parameterName,
			Calendar cal) throws SQLException
	{
		return subject.getTime(parameterName, cal);
	}

	@Override
	public Timestamp getTimestamp(CallableStatement subject,
			String parameterName, Calendar cal) throws SQLException
	{
		return subject.getTimestamp(parameterName, cal);
	}

	@Override
	public URL getURL(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getURL(parameterName);
	}

	@Override
	public RowId getRowId(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getRowId(parameterIndex);
	}

	@Override
	public RowId getRowId(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getRowId(parameterName);
	}

	@Override
	public void setRowId(CallableStatement subject, String parameterName,
			RowId x) throws SQLException
	{
		subject.setRowId(parameterName, x);
	}

	@Override
	public void setNString(CallableStatement subject, String parameterName,
			String value) throws SQLException
	{
		subject.setNString(parameterName, value);
	}

	@Override
	public void setNCharacterStream(CallableStatement subject,
			String parameterName, Reader value, long length) throws SQLException
	{
		subject.setNCharacterStream(parameterName, value, length);
	}

	@Override
	public void setNClob(CallableStatement subject, String parameterName,
			NClob value) throws SQLException
	{
		subject.setNClob(parameterName, value);
	}

	@Override
	public void setClob(CallableStatement subject, String parameterName,
			Reader reader, long length) throws SQLException
	{
		subject.setClob(parameterName, reader, length);
	}

	@Override
	public void setBlob(CallableStatement subject, String parameterName,
			InputStream inputStream, long length) throws SQLException
	{
		subject.setBlob(parameterName, inputStream, length);
	}

	@Override
	public void setNClob(CallableStatement subject, String parameterName,
			Reader reader, long length) throws SQLException
	{
		subject.setNClob(parameterName, reader, length);
	}

	@Override
	public NClob getNClob(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getNClob(parameterIndex);
	}

	@Override
	public NClob getNClob(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getNClob(parameterName);
	}

	@Override
	public void setSQLXML(CallableStatement subject, String parameterName,
			SQLXML xmlObject) throws SQLException
	{
		subject.setSQLXML(parameterName, xmlObject);
	}

	@Override
	public SQLXML getSQLXML(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getSQLXML(parameterIndex);
	}

	@Override
	public SQLXML getSQLXML(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getSQLXML(parameterName);
	}

	@Override
	public String getNString(CallableStatement subject, int parameterIndex)
			throws SQLException
	{
		return subject.getNString(parameterIndex);
	}

	@Override
	public String getNString(CallableStatement subject, String parameterName)
			throws SQLException
	{
		return subject.getNString(parameterName);
	}

	@Override
	public Reader getNCharacterStream(CallableStatement subject,
			int parameterIndex) throws SQLException
	{
		return subject.getNCharacterStream(parameterIndex);
	}

	@Override
	public Reader getNCharacterStream(CallableStatement subject,
			String parameterName) throws SQLException
	{
		return subject.getNCharacterStream(parameterName);
	}

	@Override
	public Reader getCharacterStream(CallableStatement subject,
			int parameterIndex) throws SQLException
	{
		return subject.getCharacterStream(parameterIndex);
	}

	@Override
	public Reader getCharacterStream(CallableStatement subject,
			String parameterName) throws SQLException
	{
		return subject.getCharacterStream(parameterName);
	}

	@Override
	public void setBlob(CallableStatement subject, String parameterName, Blob x)
			throws SQLException
	{
		subject.setBlob(parameterName, x);
	}

	@Override
	public void setClob(CallableStatement subject, String parameterName, Clob x)
			throws SQLException
	{
		subject.setClob(parameterName, x);
	}

	@Override
	public void setAsciiStream(CallableStatement subject, String parameterName,
			InputStream x, long length) throws SQLException
	{
		subject.setAsciiStream(parameterName, x, length);
	}

	@Override
	public void setBinaryStream(CallableStatement subject, String parameterName,
			InputStream x, long length) throws SQLException
	{
		subject.setBinaryStream(parameterName, x, length);
	}

	@Override
	public void setCharacterStream(CallableStatement subject,
			String parameterName, Reader reader, long length)
			throws SQLException
	{
		subject.setCharacterStream(parameterName, reader, length);
	}

	@Override
	public void setAsciiStream(CallableStatement subject, String parameterName,
			InputStream x) throws SQLException
	{
		subject.setAsciiStream(parameterName, x);
	}

	@Override
	public void setBinaryStream(CallableStatement subject, String parameterName,
			InputStream x) throws SQLException
	{
		subject.setBinaryStream(parameterName, x);
	}

	@Override
	public void setCharacterStream(CallableStatement subject,
			String parameterName, Reader reader) throws SQLException
	{
		subject.setCharacterStream(parameterName, reader);
	}

	@Override
	public void setNCharacterStream(CallableStatement subject,
			String parameterName, Reader value) throws SQLException
	{
		subject.setNCharacterStream(parameterName, value);
	}

	@Override
	public void setClob(CallableStatement subject, String parameterName,
			Reader reader) throws SQLException
	{
		subject.setClob(parameterName, reader);
	}

	@Override
	public void setBlob(CallableStatement subject, String parameterName,
			InputStream inputStream) throws SQLException
	{
		subject.setBlob(parameterName, inputStream);
	}

	@Override
	public void setNClob(CallableStatement subject, String parameterName,
			Reader reader) throws SQLException
	{
		subject.setNClob(parameterName, reader);
	}

	@Override
	public <T> T getObject(CallableStatement subject, int parameterIndex,
			Class<T> type) throws SQLException
	{
		return subject.getObject(parameterIndex, type);
	}

	@Override
	public <T> T getObject(CallableStatement subject, String parameterName,
			Class<T> type) throws SQLException
	{
		return subject.getObject(parameterName, type);
	}

}
