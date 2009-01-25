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

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.LinkedHashMap;

public class LoggerTest
{
	public static void main(String[] args)
	{
		IO io = new IO(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out));
		Logger log = Logger.getLogger("test");
		LoggerTest.testException(io, log);
		LoggerTest.testPrintTable(io, log);
	}

	public static void testException(IO io, Logger log)
	{
		try
		{
			throw new Exception();
		}
		catch (Exception e)
		{
			log.error("Questo e` un errore", e);
		}
	}

	public static void testPrintTable(IO io, Logger log)
	{
		LinkedHashMap<String, Vector<String>> table = new LinkedHashMap<String, Vector<String>>();
		Vector<String> c1 = new Vector<String>();
		Vector<String> c2 = new Vector<String>();
		Vector<String> c3 = new Vector<String>();
		int i=0; int j=10; int k=100;
		while (i < 10)
		{
			c1.add(""+i); c2.add(""+j); c3.add(""+k);
			i++; j++; k++; 
		}
		table.put("COL1", c1); table.put("COL2", c2); table.put("COL3", c3);
		io.printTable(table, "TEST");
	}
}
