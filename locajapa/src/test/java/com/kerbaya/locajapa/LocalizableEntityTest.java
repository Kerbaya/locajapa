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
import com.kerbaya.locajapa.DBExecutor.Call;

public class LocalizableEntityTest
{
	private static DBExecutor EX;
	
	@BeforeClass
	public static void createDb() throws Exception
	{
		if (EX != null)
		{
			throw new IllegalStateException();
		}
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
//								+ "lame BIGINT, "
								+ "PRIMARY KEY (id))");
						stmt.executeUpdate(
								"CREATE TABLE LocalizedString ("
//								+ "id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY, "
								+ "localizable_id BIGINT NOT NULL, "
								+ "locale VARCHAR(35) NOT NULL, "
								+ "value CLOB NOT NULL, "
								+ "PRIMARY KEY (localizable_id, locale), "
//								+ "PRIMARY KEY (id), "
//								+ "UNIQUE (localizable_id, locale), "
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
			l.setLocale(Locale.ROOT.toLanguageTag());
			l.setValue("Root value");
			LocalizedString l_fr = new LocalizedString();
			l_fr.setLocalizable(ls);
			l_fr.setLocale(Locale.FRENCH.toLanguageTag());
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
	}
}
