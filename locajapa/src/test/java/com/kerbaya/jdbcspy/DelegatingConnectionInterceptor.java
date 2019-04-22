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

public abstract class DelegatingConnectionInterceptor<C> 
		implements ConnectionInterceptor<C>
{
	protected abstract ConnectionInterceptor<? super C> getDelegate();
	
	@Override
	public <T> T unwrap(C subject, Class<T> iface) throws SQLException
	{
		return getDelegate().unwrap(subject, iface);
	}

	@Override
	public boolean isWrapperFor(C subject, Class<?> iface) throws SQLException
	{
		return getDelegate().isWrapperFor(subject, iface);
	}

	@Override
	public Statement createStatement(C subject) throws SQLException
	{
		return getDelegate().createStatement(subject);
	}

	@Override
	public PreparedStatement prepareStatement(C subject, String sql)
			throws SQLException
	{
		return getDelegate().prepareStatement(subject, sql);
	}

	@Override
	public CallableStatement prepareCall(C subject, String sql)
			throws SQLException
	{
		return getDelegate().prepareCall(subject, sql);
	}

	@Override
	public String nativeSQL(C subject, String sql) throws SQLException
	{
		return getDelegate().nativeSQL(subject, sql);
	}

	@Override
	public void setAutoCommit(C subject, boolean autoCommit) throws SQLException
	{
		getDelegate().setAutoCommit(subject, autoCommit);
	}

	@Override
	public boolean getAutoCommit(C subject) throws SQLException
	{
		return getDelegate().getAutoCommit(subject);
	}

	@Override
	public void commit(C subject) throws SQLException
	{
		getDelegate().commit(subject);
	}

	@Override
	public void rollback(C subject) throws SQLException
	{
		getDelegate().rollback(subject);
	}

	@Override
	public void close(C subject) throws SQLException
	{
		getDelegate().close(subject);
	}

	@Override
	public boolean isClosed(C subject) throws SQLException
	{
		return getDelegate().isClosed(subject);
	}

	@Override
	public DatabaseMetaData getMetaData(C subject) throws SQLException
	{
		return getDelegate().getMetaData(subject);
	}

	@Override
	public void setReadOnly(C subject, boolean readOnly) throws SQLException
	{
		getDelegate().setReadOnly(subject, readOnly);
	}

	@Override
	public boolean isReadOnly(C subject) throws SQLException
	{
		return getDelegate().isReadOnly(subject);
	}

	@Override
	public void setCatalog(C subject, String catalog) throws SQLException
	{
		getDelegate().setCatalog(subject, catalog);
	}

	@Override
	public String getCatalog(C subject) throws SQLException
	{
		return getDelegate().getCatalog(subject);
	}

	@Override
	public void setTransactionIsolation(C subject, int level)
			throws SQLException
	{
		getDelegate().setTransactionIsolation(subject, level);
	}

	@Override
	public int getTransactionIsolation(C subject) throws SQLException
	{
		return getDelegate().getTransactionIsolation(subject);
	}

	@Override
	public SQLWarning getWarnings(C subject) throws SQLException
	{
		return getDelegate().getWarnings(subject);
	}

	@Override
	public void clearWarnings(C subject) throws SQLException
	{
		getDelegate().clearWarnings(subject);
	}

	@Override
	public Statement createStatement(C subject, int resultSetType,
			int resultSetConcurrency) throws SQLException
	{
		return getDelegate()
				.createStatement(subject, resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(C subject, String sql,
			int resultSetType, int resultSetConcurrency) throws SQLException
	{
		return getDelegate().prepareStatement(
				subject, sql, resultSetType, resultSetConcurrency
		);
	}

	@Override
	public CallableStatement prepareCall(C subject, String sql,
			int resultSetType, int resultSetConcurrency) throws SQLException
	{
		return getDelegate()
				.prepareCall(subject, sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap(C subject) throws SQLException
	{
		return getDelegate().getTypeMap(subject);
	}

	@Override
	public void setTypeMap(C subject, Map<String, Class<?>> map)
			throws SQLException
	{
		getDelegate().setTypeMap(subject, map);
	}

	@Override
	public void setHoldability(C subject, int holdability) throws SQLException
	{
		getDelegate().setHoldability(subject, holdability);
	}

	@Override
	public int getHoldability(C subject) throws SQLException
	{
		return getDelegate().getHoldability(subject);
	}

	@Override
	public Savepoint setSavepoint(C subject) throws SQLException
	{
		return getDelegate().setSavepoint(subject);
	}

	@Override
	public Savepoint setSavepoint(C subject, String name) throws SQLException
	{
		return getDelegate().setSavepoint(subject, name);
	}

	@Override
	public void rollback(C subject, Savepoint savepoint) throws SQLException
	{
		getDelegate().rollback(subject, savepoint);
	}

	@Override
	public void releaseSavepoint(C subject, Savepoint savepoint)
			throws SQLException
	{
		getDelegate().releaseSavepoint(subject, savepoint);
	}

	@Override
	public Statement createStatement(C subject, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException
	{
		return getDelegate().createStatement(
				subject, resultSetType, resultSetConcurrency,
				resultSetHoldability
		);
	}

	@Override
	public PreparedStatement prepareStatement(C subject, String sql,
			int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException
	{
		return getDelegate().prepareStatement(
				subject, sql, resultSetType, resultSetConcurrency,
				resultSetHoldability
		);
	}

	@Override
	public CallableStatement prepareCall(C subject, String sql,
			int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException
	{
		return getDelegate().prepareCall(
				subject, sql, resultSetType, resultSetConcurrency,
				resultSetHoldability
		);
	}

	@Override
	public PreparedStatement prepareStatement(C subject, String sql,
			int autoGeneratedKeys) throws SQLException
	{
		return getDelegate().prepareStatement(subject, sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(C subject, String sql,
			int[] columnIndexes) throws SQLException
	{
		return getDelegate().prepareStatement(subject, sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(C subject, String sql,
			String[] columnNames) throws SQLException
	{
		return getDelegate().prepareStatement(subject, sql, columnNames);
	}

	@Override
	public Clob createClob(C subject) throws SQLException
	{
		return getDelegate().createClob(subject);
	}

	@Override
	public Blob createBlob(C subject) throws SQLException
	{
		return getDelegate().createBlob(subject);
	}

	@Override
	public NClob createNClob(C subject) throws SQLException
	{
		return getDelegate().createNClob(subject);
	}

	@Override
	public SQLXML createSQLXML(C subject) throws SQLException
	{
		return getDelegate().createSQLXML(subject);
	}

	@Override
	public boolean isValid(C subject, int timeout) throws SQLException
	{
		return getDelegate().isValid(subject, timeout);
	}

	@Override
	public void setClientInfo(C subject, String name, String value)
			throws SQLClientInfoException
	{
		getDelegate().setClientInfo(subject, name, value);
	}

	@Override
	public void setClientInfo(C subject, Properties properties)
			throws SQLClientInfoException
	{
		getDelegate().setClientInfo(subject, properties);
	}

	@Override
	public String getClientInfo(C subject, String name) throws SQLException
	{
		return getDelegate().getClientInfo(subject, name);
	}

	@Override
	public Properties getClientInfo(C subject) throws SQLException
	{
		return getDelegate().getClientInfo(subject);
	}

	@Override
	public Array createArrayOf(C subject, String typeName, Object[] elements)
			throws SQLException
	{
		return getDelegate().createArrayOf(subject, typeName, elements);
	}

	@Override
	public Struct createStruct(C subject, String typeName, Object[] attributes)
			throws SQLException
	{
		return getDelegate().createStruct(subject, typeName, attributes);
	}

	@Override
	public void setSchema(C subject, String schema) throws SQLException
	{
		getDelegate().setSchema(subject, schema);
	}

	@Override
	public String getSchema(C subject) throws SQLException
	{
		return getDelegate().getSchema(subject);
	}

	@Override
	public void abort(C subject, Executor executor) throws SQLException
	{
		getDelegate().abort(subject, executor);
	}

	@Override
	public void setNetworkTimeout(C subject, Executor executor,
			int milliseconds) throws SQLException
	{
		getDelegate().setNetworkTimeout(subject, executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout(C subject) throws SQLException
	{
		return getDelegate().getNetworkTimeout(subject);
	}
}
