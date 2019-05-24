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

import java.sql.DriverManager;
import java.sql.SQLException;

public class ITSupport
{
	private static final class Counter
	{
		private int count;
	}
	
	private static final ThreadLocal<Counter> COUNTER_TL = new ThreadLocal<Counter>() {
		@Override
		protected Counter initialValue()
		{
			return new Counter();
		}
	};
	
	public static int getExCount()
	{
		return COUNTER_TL.get().count;
	}
	
	public static final DBExecutor EX;
	
	static
	{
		ExecuteTriggerDriver.setUrlPrefix("jdbc:excount:");
		ExecuteTriggerDriver.setTrigger(new Runnable() {
			@Override
			public void run()
			{
				COUNTER_TL.get().count++;
			}
		});
		
		try
		{
			Class.forName("org.h2.Driver");
			DriverManager.registerDriver(new ExecuteTriggerDriver());
			EX = new DBExecutor(
					"locajapa", 
					"jdbc:excount:h2:mem:locajapa;DB_CLOSE_DELAY=-1");
		}
		catch (SQLException | ClassNotFoundException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
}
