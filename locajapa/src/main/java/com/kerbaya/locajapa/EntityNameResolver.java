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

import javax.persistence.Entity;

/**
 * Translates subclasses of {@link Localizable} to their respective JPQL entity 
 * names
 * 
 * @author Glenn.Lane@kerbaya.com
 *
 */
public interface EntityNameResolver
{
	/**
	 * Resolves to the FQDN of the nearest class annotated with {@code @Entity} 
	 * in the provided class' hierarchy
	 */
	public static final EntityNameResolver DEFAULT = new EntityNameResolver() {
		@Override
		public String resolveEntityName(Class<?> type)
		{
			do
			{
				if (type.getAnnotation(Entity.class) != null)
				{
					return type.getName();
				}
			} while ((type = type.getSuperclass()) != null);
			return null;
		}
	};
	
	/**
	 * Returns the JPQL entity name of the provided entity type
	 * 
	 * @param type
	 * the entity type for which to resolve a JPQL entity name
	 * 
	 * @return
	 * the JPQL entity name of the provided entity type
	 */
	String resolveEntityName(Class<?> type);
}
