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

package pshell;

import java.io.*;
import java.lang.reflect.*;
import java.util.Vector;

import io.IO;
import io.PropertyManager;
import io.IOProperties;
import io.IOColor;
import io.Logger;

/**
* Contenitore dati di Shell
*
* @author Parcman Tm
*/
public abstract class PShellData
{
	public static final String REQUEST_LOCAL_SENT = "Richiesta locale inviata... ";
	public static final String REQUEST_REMOTE_SENT = "Richiesta remota inviata... ";
	public static final String REQUEST_DONE = "DONE";
	public static final String REQUEST_FAILED = "FAILED";

	/**
	* Oggetto per gestione input/output.
	*/
	protected IO io;

	/**
	 * Logger
	 */
	protected Logger logger;

	/**
	* Costruttore.
	*
	* @param out Output di Shell
	* @param in Input di Shell
	*/
	public PShellData(BufferedReader in, PrintWriter out)
	{
		this.io = new IO(in, out, PropertyManager.getInstance().get("io"));
		this.logger = Logger.getLogger("client-side");
	}

	/**
	* Restituisce l'io di Shell.
	*
	* @return IO per input e output
	*/
	public IO getIO()
	{
		return io;
	}
//
//	/**
//	 * print message with color.
//	 *
//	 * @param color stringa che rappresenta il colore.
//	 */
//	public void println(String msg, String color)
//	{
//		out.println(color + msg + COLOR_NOCOLOR);
//	}
//
//	/**
//	 * print message inline with color.
//	 *
//	 * @param color stringa che rappresenta il colore.
//	 */
//	public void print(String msg, String color)
//	{
//		out.print(color + msg + COLOR_NOCOLOR);
//	}
//
	/**
	* Stampa il prompt della shell.
	*/
	public void writePrompt()
	{
		io.print(">> ");
	}

	/**
	 * Wrapper per il metodo print
	 */
	protected void print(String msg)
	{
		io.print(PropertyManager.getInstance().getProperty("io", IOProperties.PROP_TAB_SPACE) + msg);
	}

	/** 
	 * Wrapper per il metodo println
	 */
	protected void println(String msg)
	{
		io.println(PropertyManager.getInstance().getProperty("io", IOProperties.PROP_TAB_SPACE) + msg);
	}

	/**
	 * Wrapper per stampare un errore.
	 */
	protected void error(String msg)
	{
		io.println(	PropertyManager.getInstance().getProperty("io", IOProperties.PROP_TAB_SPACE) 
				+ msg, 
				IOColor.getColor(PropertyManager.getInstance().getProperty("io", IOProperties.PROP_COLOR_ERROR)));
	}

//	/**
//	* Restituisce l'input di Shell.
//	*
//	* @return BufferedReader di input
//	*/
//	public BufferedReader getIn()
//	{
//		return in;
//	}
//
//	/**
//	* Restituisce l'output di Shell.
//	*
//	* @return PrintStream di output
//	*/
//	public PrintStream getOut()
//	{
//		return out;
//	}
//
//	public void writeTable(Vector<Vector<String>> table, String[] headers, String caption, int cols, int rows)
//	{
//		out.println();
//		println(":: " + caption + " [ " + rows + " ]: ", COLOR_LIGHT_CYAN);
//
//		// inizializzo la width delle colonne
//		int[] widths = new int[cols];
//		for (int i=0; i<widths.length; i++)
//			widths[i] = 0;
//		
//		// calcolo la width delle colonne.
//		for (int c=0; c<table.size(); c++) // ciclo le colonne
//		{
//			Vector<String> col = table.get(c);
//			// aggiungo alla tabella gli headers
//			col.add(0, headers[c]);
//			for (int r=0; r<col.size(); r++) // ciclo le righe
//			{
//				String row = col.get(r);
//				if (row.length() > widths[c])
//					widths[c] = row.length();
//			}
//			// tolgo gli headers dalla tabella
//			col.remove(0);
//		}
//
//
//		String row_separator = make_rowseparator(widths);
//
//		println(row_separator, COLOR_LIGHT_GREY);
//		String h = "|";
//		for (int i=0; i<cols; i++)
//		{
//			h += fill_cell(headers[i], widths[i], " ");
//			if (i < cols)
//				h += "|";
//		}
//		println(h, COLOR_LIGHT_GREY);
//		println(row_separator, COLOR_LIGHT_GREY);
//
//		for (int i=0; i<rows; i++)
//		{
//			String color = COLOR_BLUE;
//			if ((i%2) == 0)
//			{
//				color = COLOR_GREEN;
//			} 
//
//			String row = color + "|"; // + COLOR_NOCOLOR;
//
//			for (int j=0; j<cols; j++)
//			{
//				Vector<String> col = table.get(j);
//				row += fill_cell(col.get(i), widths[j], " ");
//				if (j<cols)
//					row += color + "|"; // + COLOR_NOCOLOR;
//			}
//			out.println(row);
//			println(row_separator, color);
//		}
//	}
//
//	private String fill_cell(String str, int max, String with)
//	{
//		int len=str.length();
//		str = with + str;
//		while ( (max--) > len)
//			str += with;
//		str += with;
//		return str;
//	}
//
//	private String make_rowseparator(int[] widths)
//	{
//		String sep = "+";
//		for (int i=0; i<widths.length; i++)
//		{
//			sep += fill_cell("", widths[i], "-");
//			if (i<widths.length)
//				sep += "+";
//		}
//		return sep;
//	}
}

