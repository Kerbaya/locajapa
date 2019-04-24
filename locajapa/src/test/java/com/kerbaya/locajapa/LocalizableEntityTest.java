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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.kerbaya.locajapa.DBExecutor.JdbcRun;
import com.kerbaya.locajapa.DBExecutor.JpaCall;

public class LocalizableEntityTest
{
	private static final List<Locale> LOCALES;
	
	static
	{
		Locale[] availableLocales = Locale.getAvailableLocales();
		Set<String> tags = new HashSet<>(availableLocales.length);
		List<Locale> locales = new ArrayList<>(availableLocales.length);
		for (Locale l: availableLocales)
		{
			if (tags.add(l.toLanguageTag()))
			{
				locales.add(l);
			}
		}
		LOCALES = Collections.unmodifiableList(locales);
	}
	
	private DBExecutor ex;
	private static final ExecMonStats STATS = new ExecMonStats();
	
	static
	{
		try
		{
			DriverManager.registerDriver(new ExecuteTriggerDriver(
					"jdbc:spy:", 
					() -> {
						STATS.incrementExecCount();
					}));
		}
		catch (SQLException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	@Before
	public void createDb() throws Exception
	{
		if (ex != null)
		{
			throw new IllegalStateException();
		}
		DBExecutor ex = new DBExecutor(
				"locajapa", 
				"jdbc:spy:h2:mem:" + UUID.randomUUID().toString());
		try
		{
			ex.runJdbc(new JdbcRun() {

				@Override
				public void run(Connection con) throws SQLException
				{
					try (Statement stmt = con.createStatement())
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
			this.ex = ex;
		}
		finally
		{
			if (this.ex == null)
			{
				ex.close();
			}
		}
	}
	
	@After
	public void closeDb() throws SQLException
	{
		if (ex != null)
		{
			try
			{
				ex.close();
			}
			finally
			{
				ex = null;
			}
		}
	}
	
	private List<Long> createLocalizable(
			int localizableCount, int localizedCount) throws Exception
	{
		List<Long> idList = new ArrayList<>(localizableCount);
		if (localizedCount > LOCALES.size())
		{
			throw new IllegalArgumentException("Not enough available locales");
		}
		List<Locale> localeList = new ArrayList<>(LOCALES);
		for (int i = 0; i < localizableCount; i++)
		{
			final LocalizableString localizable = new LocalizableString();
			Collection<LocalizedString> localizedList = 
					new ArrayList<>(localizedCount);
			Collections.shuffle(localeList);
			Iterator<Locale> localeIter = localeList.iterator();
			for (int j = 0; j < localizedCount; j++)
			{
				LocalizedString localized = new LocalizedString();
				localized.setLocalizable(localizable);
				localized.setLocale(localeIter.next());
				localized.setValue(UUID.randomUUID().toString());
				localizedList.add(localized);
			}
			localizable.setLocalized(localizedList);
			idList.add(ex.callJpa(new JpaCall<Long>(){
				@Override
				public Long run(EntityManager em)
				{
					em.persist(localizable);
					Long lsId = localizable.getId();
					if (lsId == null)
					{
						System.out.println("Lame: JPA didn't return IDENTITY value");
						lsId = (Long) em.createQuery(
								"SELECT MAX(ls.id) FROM LocalizableString ls")
								.getSingleResult();
					}
					return lsId;
				}
			}));
		}
		return idList;
	}
	
	private List<Map<Locale, String>> iterateMaps(final List<Long> idList, final boolean batch) throws Exception
	{
		return ex.callJpa(new JpaCall<List<Map<Locale, String>>>() {
			@Override
			public List<Map<Locale, String>> run(EntityManager em)
			{
				List<Map<Locale, String>> result = new ArrayList<>(idList.size());
				MapLoader ml = new MapLoader();
				for (Long id: idList)
				{
					result.add(ml.getMap(em.getReference(LocalizableString.class, id)));
				}
				if (batch)
				{
					ml.load(em);
				}
				for (Map<Locale, String> m: result)
				{
					m.size();
				}
				return result;
			}
		});
	}
	
	private List<String> iterateValues(final List<Long> idList, final Locale locale, final boolean batch) throws Exception
	{
		return ex.callJpa(new JpaCall<List<String>>() {
			@Override
			public List<String> run(EntityManager em)
			{
				List<ValueSupplier<String>> values = new ArrayList<>(idList.size());
				ValueLoader vl = new ValueLoader(locale);
				for (Long id: idList)
				{
					values.add(vl.getValue(em.getReference(LocalizableString.class, id)));
				}
				if (batch)
				{
					vl.load(em);
				}
				List<String> result = new ArrayList<>(idList.size());
				for (ValueSupplier<String> vs: values)
				{
					result.add(vs.get());
				}
				return result;
			}
		});
	}
	
	private int lastExecCount = 0;
	
	private void printExecCount(String label)
	{
		System.out.println(String.format(
				"%s: %d executions", 
				label, 
				STATS.getExecCount() - lastExecCount));
		lastExecCount = STATS.getExecCount();
	}
	
	@Test
	public void test() throws Exception
	{
		final int localizableCount = 10;
		printExecCount("Tables created");
		List<Long> idList = createLocalizable(localizableCount, 5);
		printExecCount("Entities created");
		List<String> unbatchedValues = iterateValues(idList, Locale.ENGLISH, false);
		printExecCount("Iterated unbatched values");
		Assert.assertEquals(localizableCount, unbatchedValues.size());
		int execCount = STATS.getExecCount();
		List<String> batchedValues = iterateValues(idList, Locale.ENGLISH, true);
		printExecCount("Iterated batched values");
		Assert.assertEquals(execCount + 1, STATS.getExecCount());
		Assert.assertEquals(unbatchedValues, batchedValues);
		
		List<Map<Locale, String>> unbatchedMaps = iterateMaps(idList, false);
		printExecCount("Iterated unbatched maps");
		
		execCount = STATS.getExecCount();
		List<Map<Locale, String>> batchedMaps = iterateMaps(idList, true);
		printExecCount("Iterated batched maps");
		Assert.assertEquals(execCount + 1, STATS.getExecCount());
		Assert.assertEquals(unbatchedMaps, batchedMaps);
	}
}
