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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ConnectionInterceptorSupport 
		extends WrapperInterceptorSupport<Connection>
		implements ConnectionInterceptor<Connection>
{

	@Override
	public Statement createStatement(Connection subject) throws SQLException
	{
		return subject.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(Connection subject, String sql)
			throws SQLException
	{
		return subject.prepareStatement(sql);
	}

	@Override
	public CallableStatement prepareCall(Connection subject, String sql)
			throws SQLException
	{
		return subject.prepareCall(sql);
	}

	@Override
	public String nativeSQL(Connection subject, String sql) throws SQLException
	{
		return subject.nativeSQL(sql);
	}

	@Override
	public void setAutoCommit(Connection subject, boolean autoCommit)
			throws SQLException
	{
		subject.setAutoCommit(autoCommit);
	}

	@Override
	public boolean getAutoCommit(Connection subject) throws SQLException
	{
		return subject.getAutoCommit();
	}

	@Override
	public void commit(Connection subject) throws SQLException
	{
		subject.commit();
	}

	@Override
	public void rollback(Connection subject) throws SQLException
	{
		subject.rollback();
	}

	@Override
	public void close(Connection subject) throws SQLException
	{
		subject.close();
	}

	@Override
	public boolean isClosed(Connection subject) throws SQLException
	{
		return subject.isClosed();
	}

	@Override
	public DatabaseMetaData getMetaData(Connection subject) throws SQLException
	{
		return subject.getMetaData();
	}

	@Override
	public void setReadOnly(Connection subject, boolean readOnly)
			throws SQLException
	{
		subject.setReadOnly(readOnly);
	}

	@Override
	public boolean isReadOnly(Connection subject) throws SQLException
	{
		return subject.isReadOnly();
	}

	@Override
	public void setCatalog(Connection subject, String catalog)
			throws SQLException
	{
		subject.setCatalog(catalog);
	}

	@Override
	public String getCatalog(Connection subject) throws SQLException
	{
		return subject.getCatalog();
	}

	@Override
	public void setTransactionIsolation(Connection subject, int level)
			throws SQLException
	{
		subject.setTransactionIsolation(level);
	}

	@Override
	public int getTransactionIsolation(Connection subject) throws SQLException
	{
		return subject.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings(Connection subject) throws SQLException
	{
		return subject.getWarnings();
	}

	@Override
	public void clearWarnings(Connection subject) throws SQLException
	{
		subject.clearWarnings();
	}

	@Override
	public Statement createStatement(Connection subject, int resultSetType,
			int resultSetConcurrency) throws SQLException
	{
		return subject.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(Connection subject, String sql,
			int resultSetType, int resultSetConcurrency) throws SQLException
	{
		return subject.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(Connection subject, String sql,
			int resultSetType, int resultSetConcurrency) throws SQLException
	{
		return subject.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap(Connection subject)
			throws SQLException
	{
		return subject.getTypeMap();
	}

	@Override
	public void setTypeMap(Connection subject, Map<String, Class<?>> map)
			throws SQLException
	{
		subject.setTypeMap(map);
	}

	@Override
	public void setHoldability(Connection subject, int holdability)
			throws SQLException
	{
		subject.setHoldability(holdability);
	}

	@Override
	public int getHoldability(Connection subject) throws SQLException
	{
		return subject.getHoldability();
	}

	@Override
	public Savepoint setSavepoint(Connection subject) throws SQLException
	{
		return subject.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(Connection subject, String name)
			throws SQLException
	{
		return subject.setSavepoint(name);
	}

	@Override
	public void rollback(Connection subject, Savepoint savepoint)
			throws SQLException
	{
		subject.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Connection subject, Savepoint savepoint)
			throws SQLException
	{
		subject.releaseSavepoint(savepoint);
	}

	@Override
	public Statement createStatement(Connection subject, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException
	{
		return subject.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(Connection subject, String sql,
			int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException
	{
		return subject.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public CallableStatement prepareCall(Connection subject, String sql,
			int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException
	{
		return subject.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(Connection subject, String sql,
			int autoGeneratedKeys) throws SQLException
	{
		return subject.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(Connection subject, String sql,
			int[] columnIndexes) throws SQLException
	{
		return subject.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(Connection subject, String sql,
			String[] columnNames) throws SQLException
	{
		return subject.prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob(Connection subject) throws SQLException
	{
		return subject.createClob();
	}

	@Override
	public Blob createBlob(Connection subject) throws SQLException
	{
		return subject.createBlob();
	}

	@Override
	public NClob createNClob(Connection subject) throws SQLException
	{
		return subject.createNClob();
	}

	@Override
	public SQLXML createSQLXML(Connection subject) throws SQLException
	{
		return subject.createSQLXML();
	}

	@Override
	public boolean isValid(Connection subject, int timeout) throws SQLException
	{
		return subject.isValid(timeout);
	}

	@Override
	public void setClientInfo(Connection subject, String name, String value)
			throws SQLClientInfoException
	{
		subject.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Connection subject, Properties properties)
			throws SQLClientInfoException
	{
		subject.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(Connection subject, String name)
			throws SQLException
	{
		return subject.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo(Connection subject) throws SQLException
	{
		return subject.getClientInfo();
	}

	@Override
	public Array createArrayOf(Connection subject, String typeName,
			Object[] elements) throws SQLException
	{
		return subject.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(Connection subject, String typeName,
			Object[] attributes) throws SQLException
	{
		return subject.createStruct(typeName, attributes);
	}

	@Override
	public void setSchema(Connection subject, String schema) throws SQLException
	{
		subject.setSchema(schema);
	}

	@Override
	public String getSchema(Connection subject) throws SQLException
	{
		return subject.getSchema();
	}

	@Override
	public void abort(Connection subject, Executor executor) throws SQLException
	{
		subject.abort(executor);
	}

	@Override
	public void setNetworkTimeout(Connection subject, Executor executor,
			int milliseconds) throws SQLException
	{
		subject.setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout(Connection subject) throws SQLException
	{
		return subject.getNetworkTimeout();
	}

}
