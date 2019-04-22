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
package com.kerbaya.locajapa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DBExecutor implements AutoCloseable
{
	private final Connection con;
	private final EntityManagerFactory emf;
	
	public interface Run<T>
	{
		void run(T db) throws Exception;
	}
	
	public interface Call<T, V>
	{
		V run(T db) throws Exception;
	}
	
	public DBExecutor(String persistenceUnitName, String jdbcUrl) 
			throws SQLException
	{
		boolean ok = false;
		con = DriverManager.getConnection(jdbcUrl);
		try
		{
			con.setAutoCommit(false);
			Map<String, String> props = new HashMap<>(2);
			props.put("javax.persistence.jdbc.url", jdbcUrl);
			props.put("hibernate.connection.url", jdbcUrl);
			emf = Persistence.createEntityManagerFactory(
					persistenceUnitName, 
					props);
			ok = true;
		}
		finally
		{
			if (!ok)
			{
				con.close();
			}
		}
	}
	
	@Override
	public void close() throws SQLException
	{
		try
		{
			emf.close();
		}
		finally
		{
			con.close();
		}
	}
	
	public void jpa(final Run<EntityManager> run) throws Exception
	{
		jpa(new Call<EntityManager, Void>() {
			@Override
			public Void run(EntityManager db) throws Exception
			{
				run.run(db);
				return null;
			}
		});
	}
	
	public <V> V jpa(Call<EntityManager, V> run) throws Exception
	{
		final V result;
		boolean committed = false;
		EntityManager em = emf.createEntityManager();
		try
		{
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			try
			{
				result = run.run(em);
				tx.commit();
				committed = true;
			}
			finally
			{
				if (!committed && tx.isActive())
				{
					tx.rollback();
				}
			}
		}
		finally
		{
			em.close();
		}
		return result;
	}
	
	public void jdbc(final Run<Connection> run) throws Exception
	{
		jdbc(new Call<Connection, Void>() {

			@Override
			public Void run(Connection db) throws Exception
			{
				run.run(db);
				return null;
			}
		});
	}
	
	public <V> V jdbc(Call<Connection, V> run) throws Exception
	{
		final V result;
		boolean committed = false;
		try
		{
			result = run.run(con);
			con.commit();
			committed = true;
		}
		finally
		{
			if (!committed)
			{
				con.rollback();
			}
		}
		return result;
	}
	
}
