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

import java.io.Serializable;

public class LoggerConstants implements Serializable
{
	private static final long serialVersionUID = 42L;

	public static final int LEVELS = 5;

	public static final String LEVEL_ERROR 		= "ERROR";
	public static final String LEVEL_WARNING 	= "WARNING";
	public static final String LEVEL_DEBUG		= "DEBUG";
	public static final String LEVEL_INFO 		= "INFO";
	public static final String LEVEL_LOG 		= "LOG";
}
