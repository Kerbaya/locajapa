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
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class PreparedStatementInterceptorSupport<S extends PreparedStatement> 
		extends StatementInterceptorSupport<S>
		implements PreparedStatementInterceptor<S>
{

	@Override
	public ResultSet executeQuery(S subject) throws SQLException
	{
		return subject.executeQuery();
	}

	@Override
	public int executeUpdate(S subject) throws SQLException
	{
		return subject.executeUpdate();
	}

	@Override
	public void setNull(S subject, int parameterIndex, int sqlType)
			throws SQLException
	{
		subject.setNull(parameterIndex, sqlType);
	}

	@Override
	public void setBoolean(S subject, int parameterIndex, boolean x)
			throws SQLException
	{
		subject.setBoolean(parameterIndex, x);
	}

	@Override
	public void setByte(S subject, int parameterIndex, byte x)
			throws SQLException
	{
		subject.setByte(parameterIndex, x);
	}

	@Override
	public void setShort(S subject, int parameterIndex, short x)
			throws SQLException
	{
		subject.setShort(parameterIndex, x);
	}

	@Override
	public void setInt(S subject, int parameterIndex, int x) throws SQLException
	{
		subject.setInt(parameterIndex, x);
	}

	@Override
	public void setLong(S subject, int parameterIndex, long x)
			throws SQLException
	{
		subject.setLong(parameterIndex, x);
	}

	@Override
	public void setFloat(S subject, int parameterIndex, float x)
			throws SQLException
	{
		subject.setFloat(parameterIndex, x);
	}

	@Override
	public void setDouble(S subject, int parameterIndex, double x)
			throws SQLException
	{
		subject.setDouble(parameterIndex, x);
	}

	@Override
	public void setBigDecimal(S subject, int parameterIndex, BigDecimal x)
			throws SQLException
	{
		subject.setBigDecimal(parameterIndex, x);
	}

	@Override
	public void setString(S subject, int parameterIndex, String x)
			throws SQLException
	{
		subject.setString(parameterIndex, x);
	}

	@Override
	public void setBytes(S subject, int parameterIndex, byte[] x)
			throws SQLException
	{
		subject.setBytes(parameterIndex, x);
	}

	@Override
	public void setDate(S subject, int parameterIndex, Date x)
			throws SQLException
	{
		subject.setDate(parameterIndex, x);
	}

	@Override
	public void setTime(S subject, int parameterIndex, Time x)
			throws SQLException
	{
		subject.setTime(parameterIndex, x);
	}

	@Override
	public void setTimestamp(S subject, int parameterIndex, Timestamp x)
			throws SQLException
	{
		subject.setTimestamp(parameterIndex, x);
	}

	@Override
	public void setAsciiStream(S subject, int parameterIndex, InputStream x,
			int length) throws SQLException
	{
		subject.setAsciiStream(parameterIndex, x, length);
	}

	@Deprecated
	@Override
	public void setUnicodeStream(S subject, int parameterIndex, InputStream x,
			int length) throws SQLException
	{
		subject.setUnicodeStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(S subject, int parameterIndex, InputStream x,
			int length) throws SQLException
	{
		subject.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void clearParameters(S subject) throws SQLException
	{
		subject.clearParameters();
	}

	@Override
	public void setObject(S subject, int parameterIndex, Object x,
			int targetSqlType) throws SQLException
	{
		subject.setObject(parameterIndex, x, targetSqlType);
	}

	@Override
	public void setObject(S subject, int parameterIndex, Object x)
			throws SQLException
	{
		subject.setObject(parameterIndex, x);
	}

	@Override
	public boolean execute(S subject) throws SQLException
	{
		return subject.execute();
	}

	@Override
	public void addBatch(S subject) throws SQLException
	{
		subject.addBatch();
	}

	@Override
	public void setCharacterStream(S subject, int parameterIndex, Reader reader,
			int length) throws SQLException
	{
		subject.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setRef(S subject, int parameterIndex, Ref x) throws SQLException
	{
		subject.setRef(parameterIndex, x);
	}

	@Override
	public void setBlob(S subject, int parameterIndex, Blob x)
			throws SQLException
	{
		subject.setBlob(parameterIndex, x);
	}

	@Override
	public void setClob(S subject, int parameterIndex, Clob x)
			throws SQLException
	{
		subject.setClob(parameterIndex, x);
	}

	@Override
	public void setArray(S subject, int parameterIndex, Array x)
			throws SQLException
	{
		subject.setArray(parameterIndex, x);
	}

	@Override
	public ResultSetMetaData getMetaData(S subject) throws SQLException
	{
		return subject.getMetaData();
	}

	@Override
	public void setDate(S subject, int parameterIndex, Date x, Calendar cal)
			throws SQLException
	{
		subject.setDate(parameterIndex, x, cal);
	}

	@Override
	public void setTime(S subject, int parameterIndex, Time x, Calendar cal)
			throws SQLException
	{
		subject.setTime(parameterIndex, x, cal);
	}

	@Override
	public void setTimestamp(S subject, int parameterIndex, Timestamp x,
			Calendar cal) throws SQLException
	{
		subject.setTimestamp(parameterIndex, x, cal);
	}

	@Override
	public void setNull(S subject, int parameterIndex, int sqlType,
			String typeName) throws SQLException
	{
		subject.setNull(parameterIndex, sqlType, typeName);
	}

	@Override
	public void setURL(S subject, int parameterIndex, URL x) throws SQLException
	{
		subject.setURL(parameterIndex, x);
	}

	@Override
	public ParameterMetaData getParameterMetaData(S subject) throws SQLException
	{
		return subject.getParameterMetaData();
	}

	@Override
	public void setRowId(S subject, int parameterIndex, RowId x)
			throws SQLException
	{
		subject.setRowId(parameterIndex, x);
	}

	@Override
	public void setNString(S subject, int parameterIndex, String value)
			throws SQLException
	{
		subject.setNString(parameterIndex, value);
	}

	@Override
	public void setNCharacterStream(S subject, int parameterIndex, Reader value,
			long length) throws SQLException
	{
		subject.setNCharacterStream(parameterIndex, value, length);
	}

	@Override
	public void setNClob(S subject, int parameterIndex, NClob value)
			throws SQLException
	{
		subject.setNClob(parameterIndex, value);
	}

	@Override
	public void setClob(S subject, int parameterIndex, Reader reader,
			long length) throws SQLException
	{
		subject.setClob(parameterIndex, reader, length);
	}

	@Override
	public void setBlob(S subject, int parameterIndex, InputStream inputStream,
			long length) throws SQLException
	{
		subject.setBlob(parameterIndex, inputStream, length);
	}

	@Override
	public void setNClob(S subject, int parameterIndex, Reader reader,
			long length) throws SQLException
	{
		subject.setNClob(parameterIndex, reader, length);
	}

	@Override
	public void setSQLXML(S subject, int parameterIndex, SQLXML xmlObject)
			throws SQLException
	{
		subject.setSQLXML(parameterIndex, xmlObject);
	}

	@Override
	public void setObject(S subject, int parameterIndex, Object x,
			int targetSqlType, int scaleOrLength) throws SQLException
	{
		subject.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void setAsciiStream(S subject, int parameterIndex, InputStream x,
			long length) throws SQLException
	{
		subject.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(S subject, int parameterIndex, InputStream x,
			long length) throws SQLException
	{
		subject.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setCharacterStream(S subject, int parameterIndex, Reader reader,
			long length) throws SQLException
	{
		subject.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setAsciiStream(S subject, int parameterIndex, InputStream x)
			throws SQLException
	{
		subject.setAsciiStream(parameterIndex, x);
	}

	@Override
	public void setBinaryStream(S subject, int parameterIndex, InputStream x)
			throws SQLException
	{
		subject.setBinaryStream(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(S subject, int parameterIndex, Reader reader)
			throws SQLException
	{
		subject.setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setNCharacterStream(S subject, int parameterIndex, Reader value)
			throws SQLException
	{
		subject.setNCharacterStream(parameterIndex, value);
	}

	@Override
	public void setClob(S subject, int parameterIndex, Reader reader)
			throws SQLException
	{
		subject.setClob(parameterIndex, reader);
	}

	@Override
	public void setBlob(S subject, int parameterIndex, InputStream inputStream)
			throws SQLException
	{
		subject.setBlob(parameterIndex, inputStream);
	}

	@Override
	public void setNClob(S subject, int parameterIndex, Reader reader)
			throws SQLException
	{
		subject.setNClob(parameterIndex, reader);
	}

}
