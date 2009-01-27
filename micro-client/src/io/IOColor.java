/*
 * Parcman Project, the Italian Arcade Network
 * Copyright (C) 2008-2009 Parcman Tm (Marchi Sirio, Marcantoni Francesco, Emanuele Dona')
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 *
 * E-Mail:	ramyel [at] gmail [dot] com
 * 			sirio.marchi [at] gmail [dot] com
 * 			emanuele.dona [at] gmail [dot] com
 */

package io;

import java.lang.reflect.Field;
import java.io.Serializable;

public class IOColor implements Serializable 
{
	private static final long serialVersionUID = 42L;

	public static final String NOCOLOR =		"\033[0m";
	public static final String BLACK =		"\033[30m";
	public static final String RED =		"\033[31m";
	public static final String GREEN =		"\033[32m";
	public static final String BROWN =		"\033[33m";
	public static final String BLUE =		"\033[34m";
	public static final String PURPLE =		"\033[35m";
	public static final String CYAN =		"\033[36m";
	public static final String LIGHT_GREY =		"\033[37m";
	public static final String DARK_GREY =		"\033[30;1m";
	public static final String LIGHT_RED =		"\033[31;1m";
	public static final String LIGHT_GREEN =	"\033[32;1m";
	public static final String YELLOW =		"\033[33;1m";
	public static final String LIGHT_BLUE =		"\033[34;1m";
	public static final String LIGHT_PURPLE =	"\033[35;1m";
	public static final String LIGHT_CYAN =		"\033[36;1m";
	public static final String WHITE =		"\033[37;1m";

	public static String getColor(String name)
	{
		try
		{
			Class<?> c = IOColor.class;
			Field constant = c.getDeclaredField(name);
			return (String) constant.get(new IOColor());
		}
		catch (Exception e)
		{
			return "\033[0m";
		}
	}
}
