/*
 * Copyright 2019 Kerbaya Software
 * 
 * This file is part of locajapa. 
 * 
 * locajapa is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General public final License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * locajapa is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General public final License for more details.
 * 
 * You should have received a copy of the GNU General public final License
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

class PreparedStatementImpl<S extends PreparedStatement, I extends PreparedStatementInterceptor<? super S>> 
		extends StatementImpl<S, I> implements PreparedStatement
{
	
	public PreparedStatementImpl(S wrapped, I ix)
	{
		super(wrapped, ix);
	}

	@Override
	public final ResultSet executeQuery() throws SQLException
	{
		return ix.executeQuery(wrapped);
	}

	@Override
	public final int executeUpdate() throws SQLException
	{
		return ix.executeUpdate(wrapped);
	}

	@Override
	public final void setNull(int parameterIndex, int sqlType) throws SQLException
	{
		ix.setNull(wrapped, parameterIndex, sqlType);
	}

	@Override
	public final void setBoolean(int parameterIndex, boolean x) throws SQLException
	{
		ix.setBoolean(wrapped, parameterIndex, x);
	}

	@Override
	public final void setByte(int parameterIndex, byte x) throws SQLException
	{
		ix.setByte(wrapped, parameterIndex, x);
	}

	@Override
	public final void setShort(int parameterIndex, short x) throws SQLException
	{
		ix.setShort(wrapped, parameterIndex, x);
	}

	@Override
	public final void setInt(int parameterIndex, int x) throws SQLException
	{
		ix.setInt(wrapped, parameterIndex, x);
	}

	@Override
	public final void setLong(int parameterIndex, long x) throws SQLException
	{
		ix.setLong(wrapped, parameterIndex, x);
	}

	@Override
	public final void setFloat(int parameterIndex, float x) throws SQLException
	{
		ix.setFloat(wrapped, parameterIndex, x);
	}

	@Override
	public final void setDouble(int parameterIndex, double x) throws SQLException
	{
		ix.setDouble(wrapped, parameterIndex, x);
	}

	@Override
	public final void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException
	{
		ix.setBigDecimal(wrapped, parameterIndex, x);
	}

	@Override
	public final void setString(int parameterIndex, String x) throws SQLException
	{
		ix.setString(wrapped, parameterIndex, x);
	}

	@Override
	public final void setBytes(int parameterIndex, byte[] x) throws SQLException
	{
		ix.setBytes(wrapped, parameterIndex, x);
	}

	@Override
	public final void setDate(int parameterIndex, Date x) throws SQLException
	{
		ix.setDate(wrapped, parameterIndex, x);
	}

	@Override
	public final void setTime(int parameterIndex, Time x) throws SQLException
	{
		ix.setTime(wrapped, parameterIndex, x);
	}

	@Override
	public final void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException
	{
		ix.setTimestamp(wrapped, parameterIndex, x);
	}

	@Override
	public final void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException
	{
		ix.setAsciiStream(wrapped, parameterIndex, x, length);
	}

	@Deprecated
	@Override
	public final void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException
	{
		ix.setUnicodeStream(wrapped, parameterIndex, x, length);
	}

	@Override
	public final void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException
	{
		ix.setBinaryStream(wrapped, parameterIndex, x, length);
	}

	@Override
	public final void clearParameters() throws SQLException
	{
		ix.clearParameters(wrapped);
	}

	@Override
	public final void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException
	{
		ix.setObject(wrapped, parameterIndex, x, targetSqlType);
	}

	@Override
	public final void setObject(int parameterIndex, Object x) throws SQLException
	{
		ix.setObject(wrapped, parameterIndex, x);
	}

	@Override
	public final boolean execute() throws SQLException
	{
		return ix.execute(wrapped);
	}

	@Override
	public final void addBatch() throws SQLException
	{
		ix.addBatch(wrapped);
	}

	@Override
	public final void setCharacterStream(int parameterIndex, Reader reader,
			int length) throws SQLException
	{
		ix.setCharacterStream(wrapped, parameterIndex, reader, length);
	}

	@Override
	public final void setRef(int parameterIndex, Ref x) throws SQLException
	{
		ix.setRef(wrapped, parameterIndex, x);
	}

	@Override
	public final void setBlob(int parameterIndex, Blob x) throws SQLException
	{
		ix.setBlob(wrapped, parameterIndex, x);
	}

	@Override
	public final void setClob(int parameterIndex, Clob x) throws SQLException
	{
		ix.setClob(wrapped, parameterIndex, x);
	}

	@Override
	public final void setArray(int parameterIndex, Array x) throws SQLException
	{
		ix.setArray(wrapped, parameterIndex, x);
	}

	@Override
	public final ResultSetMetaData getMetaData() throws SQLException
	{
		return ix.getMetaData(wrapped);
	}

	@Override
	public final void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException
	{
		ix.setDate(wrapped, parameterIndex, x, cal);
	}

	@Override
	public final void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException
	{
		ix.setTime(wrapped, parameterIndex, x, cal);
	}

	@Override
	public final void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException
	{
		ix.setTimestamp(wrapped, parameterIndex, x, cal);
	}

	@Override
	public final void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException
	{
		ix.setNull(wrapped, parameterIndex, sqlType, typeName);
	}

	@Override
	public final void setURL(int parameterIndex, URL x) throws SQLException
	{
		ix.setURL(wrapped, parameterIndex, x);
	}

	@Override
	public final ParameterMetaData getParameterMetaData() throws SQLException
	{
		return ix.getParameterMetaData(wrapped);
	}

	@Override
	public final void setRowId(int parameterIndex, RowId x) throws SQLException
	{
		ix.setRowId(wrapped, parameterIndex, x);
	}

	@Override
	public final void setNString(int parameterIndex, String value) throws SQLException
	{
		ix.setNString(wrapped, parameterIndex, value);
	}

	@Override
	public final void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException
	{
		ix.setNCharacterStream(wrapped, parameterIndex, value, length);
	}

	@Override
	public final void setNClob(int parameterIndex, NClob value) throws SQLException
	{
		ix.setNClob(wrapped, parameterIndex, value);
	}

	@Override
	public final void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException
	{
		ix.setClob(wrapped, parameterIndex, reader, length);
	}

	@Override
	public final void setBlob(int parameterIndex, InputStream inputStream,
			long length) throws SQLException
	{
		ix.setBlob(wrapped, parameterIndex, inputStream, length);
	}

	@Override
	public final void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException
	{
		ix.setNClob(wrapped, parameterIndex, reader, length);
	}

	@Override
	public final void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException
	{
		ix.setSQLXML(wrapped, parameterIndex, xmlObject);
	}

	@Override
	public final void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException
	{
		ix.setObject(wrapped, parameterIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public final void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException
	{
		ix.setAsciiStream(wrapped, parameterIndex, x, length);
	}

	@Override
	public final void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException
	{
		ix.setBinaryStream(wrapped, parameterIndex, x, length);
	}

	@Override
	public final void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException
	{
		ix.setCharacterStream(wrapped, parameterIndex, reader, length);
	}

	@Override
	public final void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException
	{
		ix.setAsciiStream(wrapped, parameterIndex, x);
	}

	@Override
	public final void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException
	{
		ix.setBinaryStream(wrapped, parameterIndex, x);
	}

	@Override
	public final void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException
	{
		ix.setCharacterStream(wrapped, parameterIndex, reader);
	}

	@Override
	public final void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException
	{
		ix.setNCharacterStream(wrapped, parameterIndex, value);
	}

	@Override
	public final void setClob(int parameterIndex, Reader reader) throws SQLException
	{
		ix.setClob(wrapped, parameterIndex, reader);
	}

	@Override
	public final void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException
	{
		ix.setBlob(wrapped, parameterIndex, inputStream);
	}

	@Override
	public final void setNClob(int parameterIndex, Reader reader) throws SQLException
	{
		ix.setNClob(wrapped, parameterIndex, reader);
	}

}
