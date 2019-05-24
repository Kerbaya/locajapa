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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class NonDbTest
{
	private static final class NonEntity implements Localizable<String>
	{
		@Override
		public Object getId()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterable<? extends Localized<? extends String>> getLocalized()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void mapUnsupportedType()
	{
		new LocalizableLoader().getMap(NonEntity.class, 1L);
	}
	
	@Test(expected=IllegalStateException.class)
	public void mapAccessedBeforeLoad()
	{
		new LocalizableLoader().getMap(LocalizableString.class, 1L).size();
	}
	
	@Test(expected=IllegalStateException.class)
	public void valueAccessedBeforeLoad()
	{
		new LocalizableLoader().getRef(LocalizableString.class, 1L).get();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void valueUnsupportedType()
	{
		new LocalizableLoader().getRef(NonEntity.class, 1L);
	}
	
	private static final Map<Locale, String> REFERENCE = ImmutableMap.<Locale, String>builder()
			.put(Locale.ROOT, "und-value")
			.put(Locale.ENGLISH, "en-value")
			.put(Locale.FRENCH, "fr-value")
			.put(Locale.CANADA, "en-CA-value")
			.put(Locale.CANADA_FRENCH, "fr-CA-value")
			.build();
	
	@SuppressWarnings("unchecked")
	private static <T> T serialize(T in) throws ClassNotFoundException, IOException
	{
		byte[] ba;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos))
		{
			oos.writeObject(in);
			oos.flush();
			ba = baos.toByteArray();
		}
		try (InputStream bais = new ByteArrayInputStream(ba);
				ObjectInputStream ois = new ObjectInputStream(bais))
		{
			return (T) ois.readObject();
		}

	}
	
	private void serializeMap(boolean preResolve)
			throws IOException, ClassNotFoundException
	{
		LocalizableString parent = new LocalizableString();
		LocalizedString und = new LocalizedString();
		und.setLocale(Locale.ROOT);
		und.setValue("und-value");
		LocalizedString en = new LocalizedString();
		en.setLocale(Locale.ENGLISH);
		en.setValue("en-value");
		LocalizedString fr = new LocalizedString();
		fr.setLocale(Locale.FRENCH);
		fr.setValue("fr-value");
		LocalizedString enCa = new LocalizedString();
		enCa.setLocale(Locale.CANADA);
		enCa.setValue("en-CA-value");
		LocalizedString frCa = new LocalizedString();
		frCa.setLocale(Locale.CANADA_FRENCH);
		frCa.setValue("fr-CA-value");
		parent.setLocalized(ImmutableList.of(und, en, fr, enCa, frCa));
		LocalizableLoader ll = new LocalizableLoader(Locale.ENGLISH);
		Assert.assertEquals(REFERENCE, serialize(ll.getMap(parent)));
	}
	
	private void serializeValue(boolean preResolve)
			throws IOException, ClassNotFoundException
	{
		LocalizableString parent = new LocalizableString();
		LocalizedString und = new LocalizedString();
		und.setLocale(Locale.ROOT);
		und.setValue("und-value");
		LocalizedString en = new LocalizedString();
		en.setLocale(Locale.ENGLISH);
		en.setValue("en-value");
		LocalizedString fr = new LocalizedString();
		fr.setLocale(Locale.FRENCH);
		fr.setValue("fr-value");
		LocalizedString enCa = new LocalizedString();
		enCa.setLocale(Locale.CANADA);
		enCa.setValue("en-CA-value");
		LocalizedString frCa = new LocalizedString();
		frCa.setLocale(Locale.CANADA_FRENCH);
		frCa.setValue("fr-CA-value");
		parent.setLocalized(ImmutableList.of(und, en, fr, enCa, frCa));
		LocalizableLoader ll = new LocalizableLoader(Locale.GERMAN);
		Assert.assertEquals("und-value", serialize(ll.getRef(parent)).get());
	}
	
	@Test
	public void serializability() throws IOException, ClassNotFoundException
	{
		serializeMap(false);
		serializeMap(true);
		serializeValue(false);
		serializeValue(true);
		ValueReference<String> vr = serialize(
				new LocalizableLoader().getRef(LocalizableString.class, 6));
		boolean thrown = false;
		try
		{
			vr.get();
		}
		catch (IllegalStateException e)
		{
			thrown = true;
		}
		Assert.assertTrue(thrown);
		Assert.assertSame(
				NonResolvable.instance(), 
				serialize(NonResolvable.instance()));
	}

}
