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

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LocalizableEntityTest
{
	private static final List<Locale> LOCALE_LIST;
	
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
		LOCALE_LIST = Collections.unmodifiableList(locales);
	}
	
	private AtomicInteger exCount;
	private Driver driver;
	private DBExecutor ex;
	
	@Before
	public void createDb() throws SQLException
	{
		if (ex != null)
		{
			throw new IllegalStateException();
		}
		String uuid = UUID.randomUUID().toString();
		String urlPrefix = "jdbc:" + uuid + ":";
		boolean ok = false;
		exCount = new AtomicInteger();
		try
		{
			driver = new ExecuteTriggerDriver(urlPrefix, () -> {
				exCount.incrementAndGet();
			});
			DriverManager.registerDriver(driver);
			try
			{
				ex = new DBExecutor("locajapa", urlPrefix + "h2:mem:" + uuid);
				try
				{
					ex.runJdbc((con) -> {
						try (Statement stmt = con.createStatement())
						{
							stmt.executeUpdate(
									"CREATE TABLE LocalizableString ("
									+ "id BIGINT NOT NULL, "
									+ "PRIMARY KEY (id))");
							stmt.executeUpdate(
									"CREATE TABLE LocalizedString ("
									+ "id BIGINT NOT NULL, "
									+ "localizable_id BIGINT NOT NULL, "
									+ "languageTag VARCHAR(35) NOT NULL, "
									+ "languageLevel INT NOT NULL, "
									+ "value CLOB NOT NULL, "
									+ "PRIMARY KEY (id), "
									+ "UNIQUE (localizable_id, languageTag), "
									+ "FOREIGN KEY (localizable_id) "
											+ "REFERENCES LocalizableString (id))");
						}
					});
					ok = true;
				}
				finally
				{
					if (!ok)
					{
						ex.close();
						ex = null;
					}
				}
			}
			finally
			{
				if (!ok)
				{
					DriverManager.deregisterDriver(driver);
					driver = null;
				}
			}
		}
		finally
		{
			if (!ok)
			{
				exCount = null;
			}
		}
	}
	
	@After
	public void closeDb() throws SQLException
	{
		if (ex == null)
		{
			return;
		}
		try
		{
			ex.close();
		}
		finally
		{
			ex = null;
			try 
			{
				DriverManager.deregisterDriver(driver);
			}
			finally
			{
				driver = null;
				exCount = null;
			}
		}
	}
	
	private void createLocalizable(int localizableCount, int localizedCount)
	{
		if (localizedCount > LOCALE_LIST.size())
		{
			throw new IllegalArgumentException("Not enough available locales");
		}
		List<Locale> localeList = new ArrayList<>(LOCALE_LIST);
		ex.runJpa((em) -> {
			for (long id = 0; id < localizableCount; id++)
			{
				final LocalizableString localizable = new LocalizableString();
				localizable.setId(id);
				Collection<LocalizedString> localizedList = 
						new ArrayList<>(localizedCount);
				Collections.shuffle(localeList);
				for (int j = 0; j < localizedCount; j++)
				{
					LocalizedString localized = new LocalizedString();
					localized.setId(id * localizedCount + j);
					localized.setLocalizable(localizable);
					localized.setLocale(localeList.get(j));
					localized.setValue(UUID.randomUUID().toString());
					localizedList.add(localized);
				}
				localizable.setLocalized(localizedList);
				em.persist(localizable);
			}
		});
	}
	
	private List<Map<Locale, String>> iterateMaps(
			int localizableCount, boolean batch)
	{
		List<Map<Locale, String>> result = new ArrayList<>(localizableCount);
		ex.runJpa((em) -> {
			MapLoader ml = new MapLoader();
			for (long id = 0; id < localizableCount; id++)
			{
				LocalizableString ls = em.getReference(
						LocalizableString.class, id);
				result.add(ml.getMap(ls));
			}
			if (batch)
			{
				ml.load(em);
			}
			for (Map<Locale, String> m: result)
			{
				m.size();
			}
		});
		return result;
	}
	
	private List<String> iterateValues(
			int localizableCount, Locale locale, boolean batch)
	{
		List<String> result = new ArrayList<>(localizableCount);
		return ex.callJpa((em) -> {
			List<ValueSupplier<String>> values = new ArrayList<>(
					localizableCount);
			ValueLoader vl = new ValueLoader(locale);
			for (long id = 0; id < localizableCount; id++)
			{
				LocalizableString ls = em.getReference(
						LocalizableString.class, id);
				values.add(vl.getValue(ls));
			}
			if (batch)
			{
				vl.load(em);
			}
			for (ValueSupplier<String> vs: values)
			{
				result.add(vs.get());
			}
			return result;
		});
	}
	
	@Test
	public void valueLoader()
	{
		final int localizableCount = 100;
		
		createLocalizable(localizableCount, 5);

		exCount.set(0);
		List<String> unbatchedValues = iterateValues(
				localizableCount, Locale.ENGLISH, false);
		/*
		 * We expected 2 queries per localizable, because the localized 
		 * collection is lazy (query #1 for the localizable itself, query #2 for 
		 * its collection)
		 */
		Assert.assertEquals(localizableCount * 2, exCount.get());

		
		Assert.assertEquals(localizableCount, unbatchedValues.size());
		
		exCount.set(0);
		List<String> batchedValues = iterateValues(
				localizableCount, Locale.ENGLISH, true);
		/*
		 * we expect:
		 * localizableCount / (ValueLoader.DEFAULT_BATCH_SIZE + 1) + 1
		 * 
		 * As long as we're under the batch size, expect 1 query
		 */
		Assert.assertEquals(1, exCount.get());

		
		Assert.assertEquals(unbatchedValues, batchedValues);
		
	}
	
	@Test
	public void mapLoader()
	{
		final int localizableCount = 100;
		
		createLocalizable(localizableCount, 5);
		exCount.set(0);
		List<Map<Locale, String>> unbatchedMaps = iterateMaps(
				localizableCount, false);
		/*
		 * We expected 2 queries per localizable, because the localized 
		 * collection is lazy (query #1 for the localizable itself, query #2 for 
		 * its collection)
		 */
		Assert.assertEquals(localizableCount * 2, exCount.get());

		
		exCount.set(0);
		List<Map<Locale, String>> batchedMaps = iterateMaps(
				localizableCount, true);
		/*
		 * we expect:
		 * localizableCount / (ValueLoader.DEFAULT_BATCH_SIZE + 1) + 1
		 * 
		 * As long as we're under the batch size, expect 1 query
		 */
		Assert.assertEquals(1, exCount.get());

		
		Assert.assertEquals(unbatchedMaps, batchedMaps);
	}
}
