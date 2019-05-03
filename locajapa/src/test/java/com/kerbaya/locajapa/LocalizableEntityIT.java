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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import com.kerbaya.locajapa.DBExecutor.JdbcRun;
import com.kerbaya.locajapa.DBExecutor.JpaCall;
import com.kerbaya.locajapa.DBExecutor.JpaRun;

public class LocalizableEntityIT
{
	private static final int LOCALIZABLE_COUNT = 100;
	private static final Set<Locale> LOCALE_SET = Collections.unmodifiableSet(
			new HashSet<>(Arrays.asList(
					Locale.ROOT, 
					Locale.ENGLISH, 
					Locale.FRENCH, 
					Locale.CANADA, 
					Locale.CANADA_FRENCH)));

	private static final AtomicLong LOCALIZABLE_ID = new AtomicLong(1);
	private static final AtomicLong LOCALIZED_ID = new AtomicLong(1);
	
	private static final DBExecutor EX;
	private static final SortedMap<Long, Map<Locale, String>> LOCALIZABLES;
	
	static
	{
		ExecuteTriggerDriver.setUrlPrefix("jdbc:excount:");
		ExecuteTriggerDriver.setTrigger(ExCount.INSTANCE);
		try
		{
			Class.forName("org.h2.Driver");
			DriverManager.registerDriver(new ExecuteTriggerDriver());
			EX = new DBExecutor("locajapa", "jdbc:excount:h2:mem:locajapa;DB_CLOSE_DELAY=-1");
			EX.runJdbc(new JdbcRun() {
				@Override
				public void run(Connection con) throws SQLException
				{
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
				}
			});
		}
		catch (SQLException | ClassNotFoundException e)
		{
			throw new IllegalStateException(e);
		}
		LOCALIZABLES = Collections.unmodifiableSortedMap(createLocalizable(
				LOCALIZABLE_COUNT, LOCALE_SET));
	}
	
	private static SortedMap<Long, Map<Locale, String>> createLocalizable(
			final int localizableCount, Collection<Locale> locales)
	{
		final SortedMap<Long, Map<Locale, String>> result = new TreeMap<>();
		final List<Locale> localeList = new ArrayList<>(locales);
		final List<Long> localizableIds = new ArrayList<>(localizableCount);
		EX.runJpa(new JpaRun() {
			@Override
			public void run(EntityManager em)
			{
				for (long idx = 0; idx < localizableCount; idx++)
				{
					long localizableId = LOCALIZABLE_ID.getAndIncrement();
					LocalizableString localizable = new LocalizableString();
					localizable.setId(localizableId);
					localizable.setLocalized(new ArrayList<LocalizedString>());
					em.persist(localizable);
					localizableIds.add(localizableId);
				}
			}
		});
		EX.runJpa(new JpaRun() {
			@Override
			public void run(EntityManager em)
			{
				for (Long localizableId: localizableIds)
				{
					Map<Locale, String> localizedMap = 
							new HashMap<>(localeList.size());
					LocalizableString ls = em.find(
							LocalizableString.class, localizableId);
					Collections.shuffle(localeList);
					for (Locale locale: localeList)
					{
						long localizedId = LOCALIZED_ID.getAndIncrement();
						LocalizedString localized = new LocalizedString();
						localized.setId(localizedId);
						localized.setLocalizable(ls);
						localized.setLocale(locale);
						String value = UUID.randomUUID().toString();
						localized.setValue(value);
						em.persist(localized);
						ls.getLocalized().add(localized);
						localizedMap.put(locale, value);
					}
					result.put(
							localizableId, 
							Collections.unmodifiableMap(localizedMap));
				}
			}
		});
		return result;
	}
	
	private enum LoadType
	{
		REFERENCE(false, true, true),
		FIND(false, true, false),
		BATCH_FIND(true, true, false),
		BATCH_REFERENCE(true, true, true),
		BATCH_ID(true, false, false),
		;
		
		private final boolean batch;
		private final boolean useEm;
		private final boolean useRef;

		private LoadType(boolean batch, boolean useEm, boolean useRef)
		{
			this.batch = batch;
			this.useEm = useEm;
			this.useRef = useRef;
		}
	}
	
	private List<String> loadValues(
			final Collection<Long> localizableIdSet, final Locale locale, final LoadType loadType)
	{
		return EX.callJpa(new JpaCall<List<String>>() {

			@Override
			public List<String> run(EntityManager em)
			{
				List<ValueReference<String>> sups = new ArrayList<>(
						localizableIdSet.size());
				ValueLoader vl = new ValueLoader(locale);
				for (Long id: localizableIdSet)
				{
					final ValueReference<String> sup;
					if (loadType.useEm)
					{
						final LocalizableString ls = loadType.useRef ?
								em.getReference(LocalizableString.class, id)
								: em.find(LocalizableString.class, id);
						sup = vl.getValue(ls);
					}
					else
					{
						sup = vl.getValue(LocalizableString.class, id);
					}
					sups.add(sup);
				}
				int exPreIterate = ExCount.get();
				if (loadType.batch)
				{
					vl.load(em);
				}
				List<String> result = new ArrayList<>(sups.size());
				for (ValueReference<String> sup: sups)
				{
					result.add(sup.get());
				}
				if (loadType.batch)
				{
					Assert.assertEquals(
							loadType.name(), 1, ExCount.get() - exPreIterate); 
				}
				return result;
			}
		});
	}
	
	@Test
	public void values()
	{
		final Locale testLocale = Locale.ENGLISH;
		List<String> base = new ArrayList<>(LOCALIZABLES.size());
		for (Entry<Long, Map<Locale, String>> e: LOCALIZABLES.entrySet())
		{
			base.add(e.getValue().get(testLocale));
		}

		for (LoadType loadType: EnumSet.allOf(LoadType.class))
		{
			Assert.assertEquals(
					base, 
					loadValues(
							LOCALIZABLES.keySet(), testLocale, loadType));
		}
	}
}
