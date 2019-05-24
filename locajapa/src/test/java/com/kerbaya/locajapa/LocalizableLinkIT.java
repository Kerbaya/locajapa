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
import java.util.Map.Entry;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.kerbaya.locajapa.DBExecutor.JdbcRun;
import com.kerbaya.locajapa.DBExecutor.JpaCall;

public class LocalizableLinkIT extends ITSupport
{
	/*
	 * ImmutableMap doesn't allow nulls
	 */
	private static final Link NULL = new Link("null", "null");

	private static final LoaderFactory<LocalizableLink, Link> LOADER_FACTORY = 
			LoaderFactory.<LocalizableLink, Link>builder()
					.setEntityName("LocalizableLink")
					.setIdProperty("id")
					.setLanguageLevelProperty("languageLevel")
					.setLanguageTagProperty("languageTag")
					.setLocalizedProperty("localized")
					.setValueCtor(Link.class, "href", "text")
					.setEntityHandler(new EntityHandler<LocalizableLink, LocalizedLink, Link>(){
						private static final long serialVersionUID =
								4969645037749916171L;

						@Override
						public Object getId(LocalizableLink localizable)
						{
							return localizable.getId();
						}

						@Override
						public Iterable<? extends LocalizedLink> getLocalized(
								LocalizableLink localizable)
						{
							return localizable.getLocalized();
						}

						@Override
						public int getLanguageLevel(LocalizedLink localized)
						{
							return localized.getLanguageLevel();
						}

						@Override
						public String getLanguageTag(LocalizedLink localized)
						{
							return localized.getLanguageTag();
						}

						@Override
						public Link getValue(LocalizedLink localized)
						{
							return new Link(
									localized.getHref(), localized.getText());
						}
					})
					.build();
					
	private static final Map<Long, Map<Locale, Link>> REFERENCE = ImmutableMap.<Long, Map<Locale, Link>>builder()
			.put(1L, ImmutableMap.<Locale, Link>builder()
					.put(Locale.ROOT, new Link("href-1#und", "text-1#und"))
					.put(Locale.ENGLISH, new Link("href-1#en", "text-1#en"))
					.put(Locale.FRENCH, new Link("href-1#fr", "text-1#fr"))
					.put(
							Locale.CANADA, 
							new Link("href-1#en-CA", "text-1#en-CA"))
					.put(
							Locale.CANADA_FRENCH, 
							new Link("href-1#fr-CA", "text-1#fr-CA"))
					.build())
			.put(2L, ImmutableMap.<Locale, Link>builder()
					.put(Locale.ENGLISH, new Link("href-2#en", "text-2#en"))
					.put(
							Locale.CANADA, 
							new Link("href-2#en-CA", "text-2#en-CA"))
					.build())
			.put(3L, ImmutableMap.<Locale, Link>builder()
					.put(Locale.ROOT, new Link("href-3#und", "text-3#und"))
					.put(Locale.ENGLISH, new Link("href-3#en", "text-3#en"))
					.put(
							Locale.CANADA, 
							new Link("href-3#en-CA", "text-3#en-CA"))
					.build())
			.build();
	
	private static final Map<Locale, Map<Long, Link>> VALUE_REFERENCE = ImmutableMap.<Locale, Map<Long, Link>>builder()
			.put(Locale.ROOT, ImmutableMap.<Long, Link>builder()
					.put(1L, new Link("href-1#und", "text-1#und"))
					.put(2L, NULL)
					.put(3L, new Link("href-3#und", "text-3#und"))
					.build())
			.put(Locale.ENGLISH, ImmutableMap.<Long, Link>builder()
					.put(1L, new Link("href-1#en", "text-1#en"))
					.put(2L, new Link("href-2#en", "text-2#en"))
					.put(3L, new Link("href-3#en", "text-3#en"))
					.build())
			.put(Locale.CANADA, ImmutableMap.<Long, Link>builder()
					.put(1L, new Link("href-1#en-CA", "text-1#en-CA"))
					.put(2L, new Link("href-2#en-CA", "text-2#en-CA"))
					.put(3L, new Link("href-3#en-CA", "text-3#en-CA"))
					.build())
			.put(Locale.FRENCH, ImmutableMap.<Long, Link>builder()
					.put(1L, new Link("href-1#fr", "text-1#fr"))
					.put(2L, NULL)
					.put(3L, new Link("href-3#und", "text-3#und"))
					.build())
			.put(Locale.CANADA_FRENCH, ImmutableMap.<Long, Link>builder()
					.put(1L, new Link("href-1#fr-CA", "text-1#fr-CA"))
					.put(2L, NULL)
					.put(3L, new Link("href-3#und", "text-3#und"))
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
								"CREATE TABLE LocalizableLink ("
								+ "id INT NOT NULL, "
								+ "PRIMARY KEY (id))");
						stmt.executeUpdate(
								"CREATE TABLE LocalizedLink ("
								+ "id INT NOT NULL, "
								+ "languageTag VARCHAR(35) NOT NULL, "
								+ "languageLevel INT NOT NULL, "
								+ "href CLOB NOT NULL, "
								+ "text CLOB NOT NULL, "
								+ "PRIMARY KEY (id))");
						stmt.executeUpdate(
								"CREATE TABLE LocalizableLink_LocalizedLink ("
								+ "LocalizableLink_id INT NOT NULL, "
								+ "localized_id INT NOT NULL, "
								+ "PRIMARY KEY (LocalizableLink_id, localized_id), "
								+ "FOREIGN KEY (LocalizableLink_id) "
										+ "REFERENCES LocalizableLink (id), "
								+ "FOREIGN KEY (localized_id) "
										+ "REFERENCES LocalizedLink (id))");
					}
				}
			});
			EX.runJdbc(new JdbcRun() {
				
				@Override
				public void run(Connection con) throws SQLException
				{
					try (PreparedStatement parent = con.prepareStatement(
									"INSERT INTO LocalizableLink (id) VALUES (?)");
							PreparedStatement child = con.prepareStatement(
									"INSERT INTO LocalizedLink (id, languageTag, languageLevel, href, text) VALUES (?, ?, ?, ?, ?)");
							PreparedStatement link = con.prepareStatement(
									"INSERT INTO LocalizableLink_LocalizedLink (localizableLink_id, localized_id) VALUES (?, ?)"))
					{
						long localizedId = 1;
						for (Entry<Long, Map<Locale, Link>> e: 
								REFERENCE.entrySet())
						{
							parent.setLong(1, e.getKey());
							parent.executeUpdate();
							for (Entry<Locale, Link> e2: e.getValue().entrySet())
							{
								LocalizedSupport locSup = new LocalizedSupport(
										e2.getKey());
								child.setLong(1, localizedId);
								child.setString(2, locSup.getLanguageTag());
								child.setInt(3, locSup.getLanguageLevel());
								child.setString(4, e2.getValue().getHref());
								child.setString(5, e2.getValue().getText());
								child.executeUpdate();
								link.setLong(1, e.getKey());
								link.setLong(2, localizedId);
								link.executeUpdate();
								localizedId++;
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
	
	private static Map<Long, Map<Locale, Link>> loadMapDirect()
	{
		return EX.callJpa(new JpaCall<Map<Long, Map<Locale, Link>>>() {
			@Override
			public Map<Long, Map<Locale, Link>> run(EntityManager em)
			{
				@SuppressWarnings("unchecked")
				List<LocalizableLink> lsList = 
						em.createQuery("SELECT ls FROM LocalizableLink ls")
								.getResultList();
				ImmutableMap.Builder<Long, Map<Locale, Link>> b1 = 
						ImmutableMap.builder(); 
				for (LocalizableLink ls: lsList)
				{
					ImmutableMap.Builder<Locale, Link> b2 = 
							ImmutableMap.builder();
					for (LocalizedLink l: ls.getLocalized())
					{
						b2.put(
								Locale.forLanguageTag(l.getLanguageTag()), 
								new Link(l.getHref(), l.getText()));
					}
					b1.put(ls.getId(), b2.build());
				}
				return b1.build();
			}
		});
	}
	
	private static Map<Long, Map<Locale, Link>> loadMap(final MapMethod lm)
	{
		return EX.callJpa(new JpaCall<Map<Long, Map<Locale, Link>>>(){
			@Override
			public Map<Long, Map<Locale, Link>> run(EntityManager em)
			{
				ImmutableMap.Builder<Long, Map<Locale, Link>> b = 
						ImmutableMap.builder(); 
				MapLoader<LocalizableLink, Link> ll = 
						LOADER_FACTORY.createMapLoader();
				for (Long id: REFERENCE.keySet())
				{
					final Map<Locale, Link> llEntry;
					if (lm.useEm)
					{
						if (lm.useRef)
						{
							llEntry = ll.getMap(em.getReference(
									LocalizableLink.class, id));
						}
						else
						{
							llEntry = ll.getMap(em.find(
									LocalizableLink.class, id));
						}
					}
					else
					{
						llEntry = ll.getMapById(id);
					}
					b.put(id, llEntry);
				}
				int beforeLoad = getExCount();
				if (lm.batch)
				{
					ll.load(em);
				}
				ImmutableMap.Builder<Long, Map<Locale, Link>> rb = 
						ImmutableMap.builder();
				for (Entry<Long, Map<Locale, Link>> e: b.build().entrySet())
				{
					rb.put(e.getKey(), ImmutableMap.copyOf(e.getValue()));
				}
				Map<Long, Map<Locale, Link>> r = rb.build();
				if (lm.batch)
				{
					Assert.assertEquals(beforeLoad + 1, getExCount());
				}
				return r;
			}
		});
	}
	
	private static Map<Long, Link> loadValues(
			final Locale locale, final ValueMethod vm)
	{
		return EX.callJpa(new JpaCall<Map<Long, Link>>(){
			@Override
			public Map<Long, Link> run(EntityManager em)
			{
				final ImmutableMap.Builder<Long, Link> rb = 
						ImmutableMap.builder();
				ValueLoader<LocalizableLink, Link> ll = 
						LOADER_FACTORY.createValueLoader(locale);
				if (vm.immediate)
				{
					for (Long id: REFERENCE.keySet())
					{
						final LocalizableLink ls;
						if (vm.useRef)
						{
							ls = em.getReference(LocalizableLink.class, id);
						}
						else
						{
							ls = em.find(LocalizableLink.class, id);
						}
						Link value = ll.getValue(ls);
						rb.put(id, value == null ? NULL : value);
					}
				}
				else
				{
					ImmutableMap.Builder<Long, ValueReference<Link>> b = 
							ImmutableMap.builder();
					for (Long id: REFERENCE.keySet())
					{
						final ValueReference<Link> vr;
						if (vm.useEm)
						{
							final LocalizableLink ls;
							if (vm.useRef)
							{
								ls = em.getReference(
										LocalizableLink.class, id);
							}
							else
							{
								ls = em.find(LocalizableLink.class, id);
							}
							vr = ll.getRef(ls);
						}
						else
						{
							vr = ll.getRefById(id);
						}
						b.put(id, vr);
					}
					int beforeLoad = getExCount();
					if (vm.batch)
					{
						ll.load(em);
					}
					for (Entry<Long, ValueReference<Link>> e: b.build().entrySet())
					{
						Link value = e.getValue().get();
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
	public void valueLoading() throws SQLException
	{
		for (Entry<Locale, Map<Long, Link>> e: VALUE_REFERENCE.entrySet())
		{
			for (ValueMethod vm: EnumSet.allOf(ValueMethod.class))
			{
				Assert.assertEquals(e.getValue(), loadValues(e.getKey(), vm));
			}
		}
	}
}
