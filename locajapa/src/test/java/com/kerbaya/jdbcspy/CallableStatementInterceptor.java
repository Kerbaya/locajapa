/*
 * Copyright 2019 Kerbaya Software
 * 
 * This file is part of locajapa. 
 * 
 * locajapa is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (S subject, at your option) any later version.
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
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public interface CallableStatementInterceptor<S>
		extends PreparedStatementInterceptor<S>
{

	void registerOutParameter(S subject, int parameterIndex, int sqlType)
			throws SQLException;

	void registerOutParameter(S subject, int parameterIndex, int sqlType, int scale)
			throws SQLException;

	boolean wasNull(S subject) throws SQLException;

	String getString(S subject, int parameterIndex) throws SQLException;

	boolean getBoolean(S subject, int parameterIndex) throws SQLException;

	byte getByte(S subject, int parameterIndex) throws SQLException;

	short getShort(S subject, int parameterIndex) throws SQLException;

	int getInt(S subject, int parameterIndex) throws SQLException;

	long getLong(S subject, int parameterIndex) throws SQLException;

	float getFloat(S subject, int parameterIndex) throws SQLException;

	double getDouble(S subject, int parameterIndex) throws SQLException;

	BigDecimal getBigDecimal(S subject, int parameterIndex, int scale) throws SQLException;

	byte[] getBytes(S subject, int parameterIndex) throws SQLException;

	Date getDate(S subject, int parameterIndex) throws SQLException;

	Time getTime(S subject, int parameterIndex) throws SQLException;

	Timestamp getTimestamp(S subject, int parameterIndex) throws SQLException;

	Object getObject(S subject, int parameterIndex) throws SQLException;

	BigDecimal getBigDecimal(S subject, int parameterIndex) throws SQLException;

	Object getObject(S subject, int parameterIndex, Map<String, Class<?>> map)
			throws SQLException;

	Ref getRef(S subject, int parameterIndex) throws SQLException;

	Blob getBlob(S subject, int parameterIndex) throws SQLException;

	Clob getClob(S subject, int parameterIndex) throws SQLException;

	Array getArray(S subject, int parameterIndex) throws SQLException;

	Date getDate(S subject, int parameterIndex, Calendar cal) throws SQLException;

	Time getTime(S subject, int parameterIndex, Calendar cal) throws SQLException;

	Timestamp getTimestamp(S subject, int parameterIndex, Calendar cal)
			throws SQLException;

	void registerOutParameter(S subject, int parameterIndex, int sqlType, String typeName)
			throws SQLException;

	void registerOutParameter(S subject, String parameterName, int sqlType)
			throws SQLException;

	void registerOutParameter(S subject, String parameterName, int sqlType, int scale)
			throws SQLException;

	void registerOutParameter(S subject, String parameterName, int sqlType,
			String typeName) throws SQLException;

	URL getURL(S subject, int parameterIndex) throws SQLException;

	void setURL(S subject, String parameterName, URL val) throws SQLException;

	void setNull(S subject, String parameterName, int sqlType) throws SQLException;

	void setBoolean(S subject, String parameterName, boolean x) throws SQLException;

	void setByte(S subject, String parameterName, byte x) throws SQLException;

	void setShort(S subject, String parameterName, short x) throws SQLException;

	void setInt(S subject, String parameterName, int x) throws SQLException;

	void setLong(S subject, String parameterName, long x) throws SQLException;

	void setFloat(S subject, String parameterName, float x) throws SQLException;

	void setDouble(S subject, String parameterName, double x) throws SQLException;

	void setBigDecimal(S subject, String parameterName, BigDecimal x) throws SQLException;

	void setString(S subject, String parameterName, String x) throws SQLException;

	void setBytes(S subject, String parameterName, byte[] x) throws SQLException;

	void setDate(S subject, String parameterName, Date x) throws SQLException;

	void setTime(S subject, String parameterName, Time x) throws SQLException;

	void setTimestamp(S subject, String parameterName, Timestamp x) throws SQLException;

	void setAsciiStream(S subject, String parameterName, InputStream x, int length)
			throws SQLException;

	void setBinaryStream(S subject, String parameterName, InputStream x, int length)
			throws SQLException;

	void setObject(S subject, String parameterName, Object x, int targetSqlType, int scale)
			throws SQLException;

	void setObject(S subject, String parameterName, Object x, int targetSqlType)
			throws SQLException;

	void setObject(S subject, String parameterName, Object x) throws SQLException;

	void setCharacterStream(S subject, String parameterName, Reader reader, int length)
			throws SQLException;

	void setDate(S subject, String parameterName, Date x, Calendar cal)
			throws SQLException;

	void setTime(S subject, String parameterName, Time x, Calendar cal)
			throws SQLException;

	void setTimestamp(S subject, String parameterName, Timestamp x, Calendar cal)
			throws SQLException;

	void setNull(S subject, String parameterName, int sqlType, String typeName)
			throws SQLException;

	String getString(S subject, String parameterName) throws SQLException;

	boolean getBoolean(S subject, String parameterName) throws SQLException;

	byte getByte(S subject, String parameterName) throws SQLException;

	short getShort(S subject, String parameterName) throws SQLException;

	int getInt(S subject, String parameterName) throws SQLException;

	long getLong(S subject, String parameterName) throws SQLException;

	float getFloat(S subject, String parameterName) throws SQLException;

	double getDouble(S subject, String parameterName) throws SQLException;

	byte[] getBytes(S subject, String parameterName) throws SQLException;

	Date getDate(S subject, String parameterName) throws SQLException;

	Time getTime(S subject, String parameterName) throws SQLException;

	Timestamp getTimestamp(S subject, String parameterName) throws SQLException;

	Object getObject(S subject, String parameterName) throws SQLException;

	BigDecimal getBigDecimal(S subject, String parameterName) throws SQLException;

	Object getObject(S subject, String parameterName, Map<String, Class<?>> map)
			throws SQLException;

	Ref getRef(S subject, String parameterName) throws SQLException;

	Blob getBlob(S subject, String parameterName) throws SQLException;

	Clob getClob(S subject, String parameterName) throws SQLException;

	Array getArray(S subject, String parameterName) throws SQLException;

	Date getDate(S subject, String parameterName, Calendar cal) throws SQLException;

	Time getTime(S subject, String parameterName, Calendar cal) throws SQLException;

	Timestamp getTimestamp(S subject, String parameterName, Calendar cal)
			throws SQLException;

	URL getURL(S subject, String parameterName) throws SQLException;

	RowId getRowId(S subject, int parameterIndex) throws SQLException;

	RowId getRowId(S subject, String parameterName) throws SQLException;

	void setRowId(S subject, String parameterName, RowId x) throws SQLException;

	void setNString(S subject, String parameterName, String value) throws SQLException;

	void setNCharacterStream(S subject, String parameterName, Reader value, long length)
			throws SQLException;

	void setNClob(S subject, String parameterName, NClob value) throws SQLException;

	void setClob(S subject, String parameterName, Reader reader, long length)
			throws SQLException;

	void setBlob(S subject, String parameterName, InputStream inputStream, long length)
			throws SQLException;

	void setNClob(S subject, String parameterName, Reader reader, long length)
			throws SQLException;

	NClob getNClob(S subject, int parameterIndex) throws SQLException;

	NClob getNClob(S subject, String parameterName) throws SQLException;

	void setSQLXML(S subject, String parameterName, SQLXML xmlObject) throws SQLException;

	SQLXML getSQLXML(S subject, int parameterIndex) throws SQLException;

	SQLXML getSQLXML(S subject, String parameterName) throws SQLException;

	String getNString(S subject, int parameterIndex) throws SQLException;

	String getNString(S subject, String parameterName) throws SQLException;

	Reader getNCharacterStream(S subject, int parameterIndex) throws SQLException;

	Reader getNCharacterStream(S subject, String parameterName) throws SQLException;

	Reader getCharacterStream(S subject, int parameterIndex) throws SQLException;

	Reader getCharacterStream(S subject, String parameterName) throws SQLException;

	void setBlob(S subject, String parameterName, Blob x) throws SQLException;

	void setClob(S subject, String parameterName, Clob x) throws SQLException;

	void setAsciiStream(S subject, String parameterName, InputStream x, long length)
			throws SQLException;

	void setBinaryStream(S subject, String parameterName, InputStream x, long length)
			throws SQLException;

	void setCharacterStream(S subject, String parameterName, Reader reader, long length)
			throws SQLException;

	void setAsciiStream(S subject, String parameterName, InputStream x)
			throws SQLException;

	void setBinaryStream(S subject, String parameterName, InputStream x)
			throws SQLException;

	void setCharacterStream(S subject, String parameterName, Reader reader)
			throws SQLException;

	void setNCharacterStream(S subject, String parameterName, Reader value)
			throws SQLException;

	void setClob(S subject, String parameterName, Reader reader) throws SQLException;

	void setBlob(S subject, String parameterName, InputStream inputStream)
			throws SQLException;

	void setNClob(S subject, String parameterName, Reader reader) throws SQLException;

	<T> T getObject(S subject, int parameterIndex, Class<T> type) throws SQLException;

	<T> T getObject(S subject, String parameterName, Class<T> type) throws SQLException;

}
