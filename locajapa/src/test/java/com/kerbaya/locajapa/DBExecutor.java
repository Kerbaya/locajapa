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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DBExecutor implements AutoCloseable
{
	private final Connection con;
	private final EntityManagerFactory emf;
	
	public interface JpaRun
	{
		void run(EntityManager em);
	}
	
	public interface JpaCall<T>
	{
		T run(EntityManager em);
	}
	
	public interface JdbcRun
	{
		void run(Connection con) throws SQLException;
	}
	
	public interface JdbcCall<T>
	{
		T run(Connection con) throws SQLException;
	}
	
	public DBExecutor(String persistenceUnitName, String jdbcUrl) 
			throws SQLException
	{
		boolean ok = false;
		con = DriverManager.getConnection(jdbcUrl);
		try
		{
			con.setAutoCommit(false);
			emf = Persistence.createEntityManagerFactory(persistenceUnitName);
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
	
	public void runJpa(final JpaRun run)
	{
		callJpa(new JpaCall<Void>() {
			@Override
			public Void run(EntityManager em)
			{
				run.run(em);
				return null;
			}
		});
	}
	
	public <T> T callJpa(JpaCall<T> run)
	{
		final T result;
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
	
	public void runJdbc(final JdbcRun run) throws SQLException
	{
		callJdbc(new JdbcCall<Void>() {
			@Override
			public Void run(Connection con) throws SQLException
			{
				run.run(con);
				return null;
			}
		});
	}
	
	public <T> T callJdbc(JdbcCall<T> run) throws SQLException
	{
		final T result;
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
