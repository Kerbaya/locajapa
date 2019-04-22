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

final class ConnectionImpl extends WrapperImpl<Connection, ConnectionInterceptor<? super Connection>> 
		implements Connection
{
	private final StatementInterceptor<? super Statement> stmtIx;
	private final PreparedStatementInterceptor<? super PreparedStatement> psIx;
	private final CallableStatementInterceptor<? super CallableStatement> csIx;
	
	public ConnectionImpl(
			Connection wrapped,
			ConnectionInterceptor<? super Connection> ix,
			StatementInterceptor<? super Statement> stmtIx,
			PreparedStatementInterceptor<? super PreparedStatement> psIx,
			CallableStatementInterceptor<? super CallableStatement> csIx)
	{
		super(wrapped, ix);
		this.stmtIx = stmtIx;
		this.psIx = psIx;
		this.csIx = csIx;
	}

	@Override
	public Statement createStatement() throws SQLException
	{
		return new StatementImpl<>(ix.createStatement(wrapped), stmtIx, this);
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException
	{
		return new PreparedStatementImpl<>(
				ix.prepareStatement(wrapped, sql), psIx, this);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException
	{
		return new CallableStatementImpl(
				ix.prepareCall(wrapped, sql), csIx, this);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException
	{
		return ix.nativeSQL(wrapped, sql);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException
	{
		ix.setAutoCommit(wrapped, autoCommit);
	}

	@Override
	public boolean getAutoCommit() throws SQLException
	{
		return ix.getAutoCommit(wrapped);
	}

	@Override
	public void commit() throws SQLException
	{
		ix.commit(wrapped);
	}

	@Override
	public void rollback() throws SQLException
	{
		ix.rollback(wrapped);
	}

	@Override
	public void close() throws SQLException
	{
		ix.close(wrapped);
	}

	@Override
	public boolean isClosed() throws SQLException
	{
		return ix.isClosed(wrapped);
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException
	{
		return ix.getMetaData(wrapped);
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException
	{
		ix.setReadOnly(wrapped, readOnly);
	}

	@Override
	public boolean isReadOnly() throws SQLException
	{
		return ix.isReadOnly(wrapped);
	}

	@Override
	public void setCatalog(String catalog) throws SQLException
	{
		ix.setCatalog(wrapped, catalog);
	}

	@Override
	public String getCatalog() throws SQLException
	{
		return ix.getCatalog(wrapped);
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException
	{
		ix.setTransactionIsolation(wrapped, level);
	}

	@Override
	public int getTransactionIsolation() throws SQLException
	{
		return ix.getTransactionIsolation(wrapped);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException
	{
		return ix.getWarnings(wrapped);
	}

	@Override
	public void clearWarnings() throws SQLException
	{
		ix.clearWarnings(wrapped);
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency) throws SQLException
	{
		return new StatementImpl<>(
				ix.createStatement(wrapped, resultSetType, resultSetConcurrency),
				stmtIx,
				this);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException
	{
		return new PreparedStatementImpl<>(
				ix.prepareStatement(wrapped, sql, resultSetType, resultSetConcurrency),
				psIx,
				this);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException
	{
		return new CallableStatementImpl(
				ix.prepareCall(wrapped, sql, resultSetType, resultSetConcurrency),
				csIx,
				this);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException
	{
		return ix.getTypeMap(wrapped);
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException
	{
		ix.setTypeMap(wrapped, map);
	}

	@Override
	public void setHoldability(int holdability) throws SQLException
	{
		ix.setHoldability(wrapped, holdability);
	}

	@Override
	public int getHoldability() throws SQLException
	{
		return ix.getHoldability(wrapped);
	}

	@Override
	public Savepoint setSavepoint() throws SQLException
	{
		return ix.setSavepoint(wrapped);
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException
	{
		return ix.setSavepoint(wrapped, name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException
	{
		ix.rollback(wrapped, savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException
	{
		ix.releaseSavepoint(wrapped, savepoint);
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException
	{
		return new StatementImpl<>(
				ix.createStatement(wrapped,resultSetType, resultSetConcurrency, resultSetHoldability),
				stmtIx,
				this);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException
	{
		return new PreparedStatementImpl<>(
				ix.prepareStatement(wrapped, sql, resultSetType, resultSetConcurrency, resultSetHoldability),
				psIx,
				this);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException
	{
		return new CallableStatementImpl(
				ix.prepareCall(wrapped, sql, resultSetType, resultSetConcurrency, resultSetHoldability),
				csIx,
				this);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException
	{
		return new PreparedStatementImpl<>(
				ix.prepareStatement(wrapped, sql, autoGeneratedKeys),
				psIx,
				this);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException
	{
		return new PreparedStatementImpl<>(
				ix.prepareStatement(wrapped, sql, columnIndexes),
				psIx,
				this);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException
	{
		return new PreparedStatementImpl<>(
				ix.prepareStatement(wrapped, sql, columnNames),
				psIx,
				this);
	}

	@Override
	public Clob createClob() throws SQLException
	{
		return ix.createClob(wrapped);
	}

	@Override
	public Blob createBlob() throws SQLException
	{
		return ix.createBlob(wrapped);
	}

	@Override
	public NClob createNClob() throws SQLException
	{
		return ix.createNClob(wrapped);
	}

	@Override
	public SQLXML createSQLXML() throws SQLException
	{
		return ix.createSQLXML(wrapped);
	}

	@Override
	public boolean isValid(int timeout) throws SQLException
	{
		return ix.isValid(wrapped, timeout);
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException
	{
		ix.setClientInfo(wrapped, name, value);
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException
	{
		ix.setClientInfo(wrapped, properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException
	{
		return ix.getClientInfo(wrapped, name);
	}

	@Override
	public Properties getClientInfo() throws SQLException
	{
		return ix.getClientInfo(wrapped);
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException
	{
		return ix.createArrayOf(wrapped, typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException
	{
		return ix.createStruct(wrapped, typeName, attributes);
	}

	@Override
	public void setSchema(String schema) throws SQLException
	{
		ix.setSchema(wrapped, schema);
	}

	@Override
	public String getSchema() throws SQLException
	{
		return ix.getSchema(wrapped);
	}

	@Override
	public void abort(Executor executor) throws SQLException
	{
		ix.abort(wrapped, executor);
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException
	{
		ix.setNetworkTimeout(wrapped, executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout() throws SQLException
	{
		return ix.getNetworkTimeout(wrapped);
	}
	
}
