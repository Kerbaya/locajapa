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
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public interface PreparedStatementInterceptor<S> extends StatementInterceptor<S>
{

	ResultSet executeQuery(S subject) throws SQLException;

	int executeUpdate(S subject) throws SQLException;

	void setNull(S subject, int parameterIndex, int sqlType) throws SQLException;

	void setBoolean(S subject, int parameterIndex, boolean x) throws SQLException;

	void setByte(S subject, int parameterIndex, byte x) throws SQLException;

	void setShort(S subject, int parameterIndex, short x) throws SQLException;

	void setInt(S subject, int parameterIndex, int x) throws SQLException;

	void setLong(S subject, int parameterIndex, long x) throws SQLException;

	void setFloat(S subject, int parameterIndex, float x) throws SQLException;

	void setDouble(S subject, int parameterIndex, double x) throws SQLException;

	void setBigDecimal(S subject, int parameterIndex, BigDecimal x) throws SQLException;

	void setString(S subject, int parameterIndex, String x) throws SQLException;

	void setBytes(S subject, int parameterIndex, byte[] x) throws SQLException;

	void setDate(S subject, int parameterIndex, Date x) throws SQLException;

	void setTime(S subject, int parameterIndex, Time x) throws SQLException;

	void setTimestamp(S subject, int parameterIndex, Timestamp x) throws SQLException;

	void setAsciiStream(S subject, int parameterIndex, InputStream x, int length)
			throws SQLException;

	void setUnicodeStream(S subject, int parameterIndex, InputStream x, int length)
			throws SQLException;

	void setBinaryStream(S subject, int parameterIndex, InputStream x, int length)
			throws SQLException;

	void clearParameters(S subject) throws SQLException;

	void setObject(S subject, int parameterIndex, Object x, int targetSqlType)
			throws SQLException;

	void setObject(S subject, int parameterIndex, Object x) throws SQLException;

	boolean execute(S subject) throws SQLException;

	void addBatch(S subject) throws SQLException;

	void setCharacterStream(S subject, int parameterIndex, Reader reader, int length)
			throws SQLException;

	void setRef(S subject, int parameterIndex, Ref x) throws SQLException;

	void setBlob(S subject, int parameterIndex, Blob x) throws SQLException;

	void setClob(S subject, int parameterIndex, Clob x) throws SQLException;

	void setArray(S subject, int parameterIndex, Array x) throws SQLException;

	ResultSetMetaData getMetaData(S subject) throws SQLException;

	void setDate(S subject, int parameterIndex, Date x, Calendar cal) throws SQLException;

	void setTime(S subject, int parameterIndex, Time x, Calendar cal) throws SQLException;

	void setTimestamp(S subject, int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException;

	void setNull(S subject, int parameterIndex, int sqlType, String typeName)
			throws SQLException;

	void setURL(S subject, int parameterIndex, URL x) throws SQLException;

	ParameterMetaData getParameterMetaData(S subject) throws SQLException;

	void setRowId(S subject, int parameterIndex, RowId x) throws SQLException;

	void setNString(S subject, int parameterIndex, String value) throws SQLException;

	void setNCharacterStream(S subject, int parameterIndex, Reader value, long length)
			throws SQLException;

	void setNClob(S subject, int parameterIndex, NClob value) throws SQLException;

	void setClob(S subject, int parameterIndex, Reader reader, long length)
			throws SQLException;

	void setBlob(S subject, int parameterIndex, InputStream inputStream, long length)
			throws SQLException;

	void setNClob(S subject, int parameterIndex, Reader reader, long length)
			throws SQLException;

	void setSQLXML(S subject, int parameterIndex, SQLXML xmlObject) throws SQLException;

	void setObject(S subject, int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException;

	void setAsciiStream(S subject, int parameterIndex, InputStream x, long length)
			throws SQLException;

	void setBinaryStream(S subject, int parameterIndex, InputStream x, long length)
			throws SQLException;

	void setCharacterStream(S subject, int parameterIndex, Reader reader, long length)
			throws SQLException;

	void setAsciiStream(S subject, int parameterIndex, InputStream x) throws SQLException;

	void setBinaryStream(S subject, int parameterIndex, InputStream x) throws SQLException;

	void setCharacterStream(S subject, int parameterIndex, Reader reader)
			throws SQLException;

	void setNCharacterStream(S subject, int parameterIndex, Reader value)
			throws SQLException;

	void setClob(S subject, int parameterIndex, Reader reader) throws SQLException;

	void setBlob(S subject, int parameterIndex, InputStream inputStream)
			throws SQLException;

	void setNClob(S subject, int parameterIndex, Reader reader) throws SQLException;

}
