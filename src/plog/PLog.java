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

package plog;

/**
 * Gestore messaggi di errore e di debug.
 *
 * @author Parcman Tm
 */
public class PLog
{
	/**
	 * Log dei messaggi di errore con gestione delle eccezioni.
	 *
	 * @param e Eccezione da gestire
	 * @param str Messaggio di errore
	 */
	public static void err(Exception e, String func, String str)
	{
		System.err.println("[ERROR]<" + func + "> " + str);
		e.printStackTrace();
	}

	/**
	 * Log dei messaggi di errore.
	 *
	 * @param str Messaggio di errore
	 */
	public static void err(String func, String str)
	{
		System.err.println("[ERROR]<" + func + "> " + str);
	}

	/**
	 * Log dei messaggi di debug.
	 *
	 * @param str Messaggio di debug
	 */
	public static void debug(String func, String str)
	{
		System.out.println("[DEBUG]<" + func + "> " + str);
	}

	/**
	 * Log di informazione.
	 *
	 * @param str Messaggio
	 */
	public static void info(String func, String str)
	{
		System.out.println("[LOG]<" + func + "> "  + str);
	}

	/**
	 * Log generico.
	 *
	 * @param str Messaggio
	 */
	public static void log(String func, String str)
	{
		System.out.println("[LOG]<" + func + "> "  + str);
	}
}

