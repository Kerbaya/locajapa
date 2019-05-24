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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.kerbaya.locajapa.DBExecutor.JdbcRun;
import com.kerbaya.locajapa.DBExecutor.JpaCall;

public class LocalizableStringIT extends ITSupport
{
	/*
	 * ImmutableMap doesn't allow nulls
	 */
	private static final String NULL = UUID.randomUUID().toString();
	
	private static final Map<Long, Map<Locale, String>> REFERENCE = ImmutableMap.<Long, Map<Locale, String>>builder()
			.put(1L, ImmutableMap.<Locale, String>builder()
					.put(Locale.ROOT, "1#und")
					.put(Locale.ENGLISH, "1#en")
					.put(Locale.FRENCH, "1#fr")
					.put(Locale.CANADA, "1#en-CA")
					.put(Locale.CANADA_FRENCH, "1#fr-CA")
					.build())
			.put(2L, ImmutableMap.<Locale, String>builder()
					.put(Locale.ENGLISH, "2#en")
					.put(Locale.CANADA, "2#en-CA")
					.build())
			.put(3L, ImmutableMap.<Locale, String>builder()
					.put(Locale.ROOT, "3#und")
					.put(Locale.ENGLISH, "3#en")
					.put(Locale.CANADA, "3#en-CA")
					.build())
			.build();
	
	private static final Map<Locale, Map<Long, String>> VALUE_REFERENCE = ImmutableMap.<Locale, Map<Long, String>>builder()
			.put(Locale.ROOT, ImmutableMap.<Long, String>builder()
					.put(1L, "1#und")
					.put(2L, NULL)
					.put(3L, "3#und")
					.build())
			.put(Locale.ENGLISH, ImmutableMap.<Long, String>builder()
					.put(1L, "1#en")
					.put(2L, "2#en")
					.put(3L, "3#en")
					.build())
			.put(Locale.CANADA, ImmutableMap.<Long, String>builder()
					.put(1L, "1#en-CA")
					.put(2L, "2#en-CA")
					.put(3L, "3#en-CA")
					.build())
			.put(Locale.FRENCH, ImmutableMap.<Long, String>builder()
					.put(1L, "1#fr")
					.put(2L, NULL)
					.put(3L, "3#und")
					.build())
			.put(Locale.CANADA_FRENCH, ImmutableMap.<Long, String>builder()
					.put(1L, "1#fr-CA")
					.put(2L, NULL)
					.put(3L, "3#und")
					.build())
			.build();

	static
	{
		try
		{
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
			EX.runJdbc(new JdbcRun() {
				
				@Override
				public void run(Connection con) throws SQLException
				{
					try (PreparedStatement parent = con.prepareStatement(
									"INSERT INTO LocalizableString (id) VALUES (?)");
							PreparedStatement child = con.prepareStatement(
									"INSERT INTO LocalizedString (id, localizable_id, languageTag, languageLevel, value) VALUES (?, ?, ?, ?, ?)"))
					{
						long localizedId = 1;
						for (Entry<Long, Map<Locale, String>> e: 
								REFERENCE.entrySet())
						{
							parent.setLong(1, e.getKey());
							parent.executeUpdate();
							for (Entry<Locale, String> e2: e.getValue().entrySet())
							{
								LocalizedSupport ls = new LocalizedSupport(
										e2.getKey());
								child.setLong(1, localizedId++);
								child.setLong(2, e.getKey());
								child.setString(3, ls.getLanguageTag());
								child.setInt(4, ls.getLanguageLevel());
								child.setString(5, e2.getValue());
								child.executeUpdate();
							}
						}
					}
				}
			});
		}
		catch (SQLException e)
		{
			throw new IllegalStateException(e);
		}
	}

	private enum MapMethod
	{
		ID_BATCH(false, false, true),
		REFERENCE_BATCH(true, true, true),
		FIND_BATCH(true, false, true),
		REFERENCE_NO_BATCH(true, true, false),
		FIND_NO_BATCH(true, false, false),
		;
		
		private final boolean useEm;
		private final boolean useRef;
		private final boolean batch;
		private MapMethod(boolean useEm, boolean useRef, boolean batch)
		{
			this.useEm = useEm;
			this.useRef = useRef;
			this.batch = batch;
		}
	}
	
	private enum ValueMethod
	{
		IMMEDIATE_REFERENCE(true, true, true, false),
		IMMEDIATE_FIND(true, true, false, false),
		ID_BATCH(false, false, false, true),
		REFERENCE_BATCH(false, true, true, true),
		FIND_BATCH(false, true, false, true),
		REFERENCE_NO_BATCH(false, true, true, false),
		FIND_NO_BATCH(false, true, false, false),
		;
		private final boolean immediate;
		private final boolean useEm;
		private final boolean useRef;
		private final boolean batch;
		private ValueMethod(
				boolean immediate, boolean useEm, boolean useRef, boolean batch)
		{
			this.immediate = immediate;
			this.useEm = useEm;
			this.useRef = useRef;
			this.batch = batch;
		}
		
	}
	
	private static Map<Long, Map<Locale, String>> loadMapDirect()
	{
		return EX.callJpa(new JpaCall<Map<Long, Map<Locale, String>>>() {
			@Override
			public Map<Long, Map<Locale, String>> run(EntityManager em)
			{
				@SuppressWarnings("unchecked")
				List<LocalizableString> lsList = 
						em.createQuery("SELECT ls FROM LocalizableString ls")
								.getResultList();
				ImmutableMap.Builder<Long, Map<Locale, String>> b1 = 
						ImmutableMap.builder(); 
				for (LocalizableString ls: lsList)
				{
					ImmutableMap.Builder<Locale, String> b2 = 
							ImmutableMap.builder();
					for (LocalizedString l: ls.getLocalized())
					{
						b2.put(l.getLocale(), l.getValue());
					}
					b1.put(ls.getId(), b2.build());
				}
				return b1.build();
			}
		});
	}
	
	private static Map<Long, Map<Locale, String>> loadMap(final MapMethod lm)
	{
		return EX.callJpa(new JpaCall<Map<Long, Map<Locale, String>>>(){
			@Override
			public Map<Long, Map<Locale, String>> run(EntityManager em)
			{
				ImmutableMap.Builder<Long, Map<Locale, String>> b = 
						ImmutableMap.builder(); 
				LocalizableLoader ll = new LocalizableLoader();
				for (Long id: REFERENCE.keySet())
				{
					final Map<Locale, String> llEntry;
					if (lm.useEm)
					{
						if (lm.useRef)
						{
							llEntry = ll.getMap(em.getReference(
									LocalizableString.class, id));
						}
						else
						{
							llEntry = ll.getMap(em.find(
									LocalizableString.class, id));
						}
					}
					else
					{
						llEntry = ll.getMap(LocalizableString.class, id);
					}
					b.put(id, llEntry);
				}
				int beforeLoad = getExCount();
				if (lm.batch)
				{
					ll.load(em);
				}
				ImmutableMap.Builder<Long, Map<Locale, String>> rb = 
						ImmutableMap.builder();
				for (Entry<Long, Map<Locale, String>> e: b.build().entrySet())
				{
					rb.put(e.getKey(), ImmutableMap.copyOf(e.getValue()));
				}
				Map<Long, Map<Locale, String>> r = rb.build();
				if (lm.batch)
				{
					Assert.assertEquals(beforeLoad + 1, getExCount());
				}
				return r;
			}
		});
	}
	
	private static Map<Long, String> loadValues(
			final Locale locale, final ValueMethod vm)
	{
		return EX.callJpa(new JpaCall<Map<Long, String>>(){
			@Override
			public Map<Long, String> run(EntityManager em)
			{
				final ImmutableMap.Builder<Long, String> rb = 
						ImmutableMap.builder();
				LocalizableLoader ll = new LocalizableLoader(locale);
				if (vm.immediate)
				{
					for (Long id: REFERENCE.keySet())
					{
						final LocalizableString ls;
						if (vm.useRef)
						{
							ls = em.getReference(LocalizableString.class, id);
						}
						else
						{
							ls = em.find(LocalizableString.class, id);
						}
						String value = ll.getValue(ls);
						rb.put(id, value == null ? NULL : value);
					}
				}
				else
				{
					ImmutableMap.Builder<Long, ValueReference<String>> b = 
							ImmutableMap.builder();
					for (Long id: REFERENCE.keySet())
					{
						final ValueReference<String> vr;
						if (vm.useEm)
						{
							final LocalizableString ls;
							if (vm.useRef)
							{
								ls = em.getReference(
										LocalizableString.class, id);
							}
							else
							{
								ls = em.find(LocalizableString.class, id);
							}
							vr = ll.getRef(ls);
						}
						else
						{
							vr = ll.getRef(LocalizableString.class, id);
						}
						b.put(id, vr);
					}
					int beforeLoad = getExCount();
					if (vm.batch)
					{
						ll.load(em);
					}
					for (Entry<Long, ValueReference<String>> e: b.build().entrySet())
					{
						String value = e.getValue().get();
						rb.put(e.getKey(), value == null ? NULL : value);
					}
					if (vm.batch)
					{
						Assert.assertEquals(beforeLoad + 1, getExCount());
					}
				}
				return rb.build();
			}
		});
	}
	
	@Test
	public void mapLoading()
	{
		Assert.assertEquals(REFERENCE, loadMapDirect());
		for (MapMethod lm: EnumSet.allOf(MapMethod.class))
		{
			Assert.assertEquals(REFERENCE, loadMap(lm));
		}
	}
	
	@Test
	public void valueLoading()
	{
		for (Entry<Locale, Map<Long, String>> e: VALUE_REFERENCE.entrySet())
		{
			for (ValueMethod vm: EnumSet.allOf(ValueMethod.class))
			{
				Assert.assertEquals(e.getValue(), loadValues(e.getKey(), vm));
			}
		}
	}
}
