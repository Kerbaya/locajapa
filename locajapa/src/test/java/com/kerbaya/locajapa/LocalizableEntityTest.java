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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kerbaya.locajapa.DBExecutor.Run;
import com.kerbaya.jdbcspy.CallableStatementInterceptor;
import com.kerbaya.jdbcspy.CallableStatementInterceptorSupport;
import com.kerbaya.jdbcspy.ConnectionInterceptorSupport;
import com.kerbaya.jdbcspy.DriverImpl;
import com.kerbaya.jdbcspy.StatementInterceptorSupport;
import com.kerbaya.locajapa.DBExecutor.Call;

public class LocalizableEntityTest
{
	private static final StatementInterceptorSupport<Statement> STMT_IX = new StatementInterceptorSupport<Statement>() {
	};
	
	private static DBExecutor EX;
	private static ExecMonStats STATS;
	
	
	
	@BeforeClass
	public static void createDb() throws Exception
	{
		if (EX != null)
		{
			throw new IllegalStateException();
		}
		CallableStatementInterceptor<CallableStatement> ix = new CallableStatementInterceptorSupport() {
			
		};
		STATS = new ExecMonStats();
		DriverManager.registerDriver(new DriverImpl(
				"jdbc:spy:",
				null,
				ix,
				ix,
				ix));
		DBExecutor ex = new DBExecutor(
				"locajapa", 
				"com.kerbaya.locajapa.DriverImpl", 
				"jdbc:h2:mem:" + UUID.randomUUID().toString());
		try
		{
			ex.jdbc(new Run<Connection>() {

				@Override
				public void run(Connection db) throws Exception
				{
					try (Statement stmt = db.createStatement())
					{
						stmt.executeUpdate(
								"CREATE TABLE LocalizableString ("
								+ "id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY, "
								+ "PRIMARY KEY (id))");
						stmt.executeUpdate(
								"CREATE TABLE LocalizedString ("
								+ "localizable_id BIGINT NOT NULL, "
								+ "languageTag VARCHAR(35) NOT NULL, "
								+ "languageLevel INT NOT NULL, "
								+ "value CLOB NOT NULL, "
								+ "PRIMARY KEY (localizable_id, languageTag), "
								+ "FOREIGN KEY (localizable_id) REFERENCES LocalizableString (id))");
					}
				}
			});
			EX = ex;
		}
		finally
		{
			if (EX == null)
			{
				ex.close();
			}
		}
	}
	
	@AfterClass
	public static void closeDb() throws SQLException
	{
		if (EX != null)
		{
			try
			{
				EX.close();
			}
			finally
			{
				EX = null;
			}
		}
	}
	
	@Test
	public void test() throws Exception
	{
		final Long id;
		{
			final LocalizableString ls = new LocalizableString();
			LocalizedString l = new LocalizedString();
			l.setLocalizable(ls);
			l.setLocale(Locale.ROOT);
			l.setValue("Root value");
			LocalizedString l_fr = new LocalizedString();
			l_fr.setLocalizable(ls);
			l_fr.setLocale(Locale.FRENCH);
			l_fr.setValue("French value");
			ls.setLocalized(Arrays.asList(l, l_fr));
			id = EX.jpa(new Call<EntityManager, Long>() {

				@Override
				public Long run(EntityManager db) throws Exception
				{
					db.persist(ls);
					Long lsId = ls.getId();
					if (lsId == null)
					{
						System.out.println("Lame: JPA didn't return IDENTITY value");
						lsId = (Long) db.createQuery(
								"SELECT MAX(ls.id) FROM LocalizableString ls")
								.getSingleResult();
					}
					return lsId;
				}
			});
		}
		EX.jpa(new Run<EntityManager>() {

			@Override
			public void run(EntityManager db) throws Exception
			{
				final LocalizableString ls = db.find(LocalizableString.class, id);
				Assert.assertEquals(2, ls.getLocalized().size());
			}
		});
		EX.jpa(new Run<EntityManager>() {
			@Override
			public void run(EntityManager db) throws Exception
			{
				ValueLoader vl = new ValueLoader(Locale.FRENCH);
				ValueSupplier<String> v = vl.getValue(
						db.getReference(LocalizableString.class, id));
				System.out.println("Before load");
				vl.load(db);
				System.out.println(v.get());
				System.out.println("After load");
			}
		});
	}
}
