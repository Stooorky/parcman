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
import java.lang.Math;
import java.lang.StringBuffer;
import java.util.Vector;
import java.util.Map;
import java.util.Set;
import java.util.Properties;
import java.util.Iterator;
import java.util.Arrays;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;
import io.exceptions.*;
import java.io.Serializable;

public class IO implements Serializable
{
	private static final long serialVersionUID = 42L;

	private BufferedReader in;
	private PrintWriter out;
	private Properties prop;

	public IO(BufferedReader in, PrintWriter out)
	{
		this.in = in;
		this.out = out;
		this.prop = IOProperties.getDefault();
	}

	public IO(BufferedReader in, PrintWriter out, Properties prop)
	{
		this.in = in;
		this.out = out;
		this.prop = prop;
	}

	public void print(String msg)
	{
		out.print(msg);
		out.flush();
	}

	public void println(String msg)
	{
		out.println(msg);
		out.flush();
	}

	public void print(String msg, String color)
	{
		out.print(color + msg + IOColor.NOCOLOR);
		out.flush();
	}

	public void println(String msg, String color)
	{
		out.println(color + msg + IOColor.NOCOLOR);
		out.flush();
	}

	public String[] readLine(String separator) throws 
		IOException,
		PatternSyntaxException
	{
		String line = readLine();
		return line.split(separator);
	}

	public String readLine() throws 
		IOException
	{
		return in.readLine(); 
	}

	public void printList(Vector<String> list, String caption, String type) throws
		InOutException 
	{
		try
		{
			checkList(list);
		}
		catch (ListIsEmptyException e)
		{
			println("La lista e` vuota.", prop.getProperty(IOProperties.PROP_COLOR_ERROR));
			throw e;
		}

		boolean showCaption = "true".equals(prop.getProperty(IOProperties.PROP_LIST_SHOW_CAPTION)) 
			&& caption != null ? true : false;
		boolean showProperties = "true".equals(prop.getProperty(IOProperties.PROP_LIST_SHOW_PROPERTIES)) ? true : false;

		StringBuffer sb = new StringBuffer();

		if (showCaption)
		{
			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE)
				+ IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_CAPTION)) + caption);

			if (showProperties)
				sb.append(IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_PROPERTIES))
					+ " [ count:" + list.size() + " ]");

			sb.append(IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_CAPTION))
				+ ": " + IOColor.NOCOLOR + "\n");
		}

		if (type.equals(IOConstants.LIST_TYPE_INCREMENTAL))
			printListIncremental(list, sb);
		else if(type.equals(IOConstants.LIST_TYPE_SYMBOL))
			printListSymbol(list, sb);
		else if(type.equals(IOConstants.LIST_TYPE_INLINE))
			printListInline(list, sb);
		else 
			printListInline(list, sb);

		println(sb.toString());
	}

	public void printTable(Map<String, Vector<String>> table, String caption)
	{
		// TODO: check if table is empty:  throw TableIsEmptyException

		int cols = table.size();

		StringBuffer sb = new StringBuffer();

		// get max row
		int rows = 0;
		Set<String> keyss = table.keySet();
		Iterator<String> k = keyss.iterator();
		while (k.hasNext())
		{
			int colsize = table.get(k.next()).size();
			rows = colsize > rows ? colsize : rows;
		}

		int[] widths = getColsWidth(table);


		String rowSeparatorLine = makeRowSeparator(widths, prop.getProperty(IOProperties.PROP_TABLE_HORIZONTAL_SEPARATOR), 
			prop.getProperty(IOProperties.PROP_TABLE_CROSS_SEPARATOR));
		String headRowSeparatorLine = makeRowSeparator(widths, prop.getProperty(IOProperties.PROP_TABLE_HEAD_HORIZONTAL_SEPARATOR), 
			prop.getProperty(IOProperties.PROP_TABLE_CROSS_SEPARATOR));

		boolean showHeaderLineSep = "true".equals(prop.getProperty(IOProperties.PROP_TABLE_SHOW_HEADER_LINE_SEPARATOR)) ? true : false;
		boolean showCaption = "true".equals(prop.getProperty(IOProperties.PROP_TABLE_SHOW_CAPTION)) 
			&& caption != null ? true : false;
		boolean showProperties = "true".equals(prop.getProperty(IOProperties.PROP_TABLE_SHOW_PROPERTIES)) ? true : false;
		boolean showRowLineSep= "true".equals(prop.getProperty(IOProperties.PROP_TABLE_SHOW_ROW_LINE_SEPARATOR)) ? true : false;

		if (showCaption)
		{
			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) 
				+ IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_CAPTION)) + caption);
			if (showProperties)
				sb.append(IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_PROPERTIES)) 
				+ " [ cols:" + cols + ",rows:" + rows + " ]");
			sb.append(IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_CAPTION)) + ": " + IOColor.NOCOLOR + "\n\n");
		}

		if (showHeaderLineSep)
		{
			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_HEAD_ROW)) + headRowSeparatorLine + "\n");
			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + makeHeaders(table, widths) + "\n");
			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + headRowSeparatorLine + IOColor.NOCOLOR + "\n");
		}
		else
		{
			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + makeHeaders(table, widths) + "\n");
		}

		Set<String> keys = table.keySet();
		for (int i=0; i<rows; i++)
		{
			String color = IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_ROW_ODD));
			if ((i%2) == 0)
			{
				color = IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_ROW_EVEN));
			} 

			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + color + makeRow(table, keys, i, widths) + IOColor.NOCOLOR + "\n");
			if (showRowLineSep)
				sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + rowSeparatorLine + "\n");
		}
		println(sb.toString());
	}

	public void printMap(Map<String, String> map, String caption) 
	{
		int[] w = new int[2];
		Arrays.fill(w, (int)0);
		Set<String> keys = map.keySet();
		for (Iterator<String> it=keys.iterator(); it.hasNext();)
		{
			String k = it.next();
			w[0] = k.length() > w[0] ? k.length() : w[0];
			String v = map.get(k);
			w[1] = v.length() > w[1] ? v.length() : w[1];
		}

		String rowSeparatorLine = makeRowSeparator(w, prop.getProperty(IOProperties.PROP_MAP_HORIZONTAL_SEPARATOR), 
			prop.getProperty(IOProperties.PROP_MAP_CROSS_SEPARATOR));

		boolean showLineSep = "true".equals(prop.getProperty(IOProperties.PROP_MAP_SHOW_LINE_SEPARATOR)) ? true : false;
		boolean showCaption = "true".equals(prop.getProperty(IOProperties.PROP_MAP_SHOW_CAPTION)) 
			&& caption != null ? true : false;
		String tab = prop.getProperty(IOProperties.PROP_TAB_SPACE);

		StringBuffer sb = new StringBuffer();
		
		if (showCaption)
			sb.append(tab + IOColor.getColor(prop.getProperty(IOProperties.PROP_MAP_COLOR_CAPTION)) 
				+ caption + IOColor.NOCOLOR + "\n\n");
		
		keys = map.keySet();
		if (showLineSep)
		{
			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + rowSeparatorLine + "\n");
		}
		String cleft = IOColor.getColor(prop.getProperty(IOProperties.PROP_MAP_COLOR_LEFT));
		String cright = IOColor.getColor(prop.getProperty(IOProperties.PROP_MAP_COLOR_RIGHT));
		int i=0;
		for (Iterator<String> it=keys.iterator(); it.hasNext(); i++)
		{
			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + makeRow(map, it, w, cleft, cright) + "\n");
			if (showLineSep)
			{
				sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + rowSeparatorLine + "\n");
			}
		}

		println(sb.toString());
	}

	private String makeRow(Map<String, String> map, Iterator<String> it, int[] widths, String cleft, String cright)
	{
		StringBuffer sb = new StringBuffer();
		String sepV = prop.getProperty(IOProperties.PROP_MAP_VERTICAL_SEPARATOR);
		sb.append(sepV + cleft);
		String k = it.next();
		String fillWith = prop.getProperty(IOProperties.PROP_FILL_WITH);
		sb.append(fillCell(k, widths[0], fillWith) + IOColor.NOCOLOR + sepV + cright);
		sb.append(fillCell(map.get(k), widths[1], fillWith) + IOColor.NOCOLOR + sepV);
		return sb.toString();
	}

	private void checkList(Vector<String> list) throws 
		ListIsEmptyException
	{
		if (list.size() == 0)
			throw new ListIsEmptyException();
	}

	private String fillCell(String str, int max, String with)
	{
		int len=str.length();
		str = with + str;
		while ( (max--) > len)
			str += with;
		str += with;
		return str;
	}

	private String makeRow(Map<String, Vector<String>> t, Set<String> keys, int index, int[] widths)
	{
		String row = "";
		Iterator<String> j = keys.iterator();
		int pos = 0;
		while (j.hasNext())
		{
			row += prop.getProperty(IOProperties.PROP_TABLE_VERTICAL_SEPARATOR) 
				+ fillCell(t.get(j.next()).get(index), widths[pos], prop.getProperty(IOProperties.PROP_FILL_WITH));
			if (!j.hasNext())
				row += prop.getProperty(IOProperties.PROP_TABLE_VERTICAL_SEPARATOR);
			pos++;
		}
		return row;
	}

	private String makeHeaders(Map<String, Vector<String>> table, int[] widths)
	{
		String headers = "";
		Set<String> keys = table.keySet();
		Iterator<String> i = keys.iterator();
		int pos = 0;
		while (i.hasNext())
		{
			headers += prop.getProperty(IOProperties.PROP_TABLE_VERTICAL_SEPARATOR)
				+ IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_HEAD_ROW)) 
				+ fillCell(i.next(), widths[pos], prop.getProperty(IOProperties.PROP_FILL_WITH));
			if (!i.hasNext())
				headers += prop.getProperty(IOProperties.PROP_TABLE_VERTICAL_SEPARATOR);
			pos++;
		}
		return headers;
	}

	private String makeRowSeparator(int[] widths, String with, String cross)
	{
		String sep = cross;
		for (int i=0; i<widths.length; i++)
		{
			sep += fillCell("", widths[i], with);
			if (i<widths.length)
				sep += cross;
		}
		return sep;
	}



	private int[] getColsWidth(Map<String, Vector<String>> table)
	{
		int[] widths = new int[table.size()];
		Set<String> keys = table.keySet();
		Iterator<String> i = keys.iterator();
		int k=0;
		while (i.hasNext())
		{
			String key = i.next();
			widths[k] = key.length();

			Vector<String> col = table.get(key);
			for (int r=0; r<col.size(); r++) // ciclo le righe
			{
				String row = col.get(r);
				if (row.length() > widths[k])
					widths[k] = row.length();
			}
			k++;
		}

		return widths;
	}

	private int numberOfDigits(int number)
	{
		if (number == 0)
			return 1;
		else
			return (int) (1 + Math.log10((double) number));
	}

	private String makeFill(int x, int max, String with)
	{
		int diff = max - x;
		String fill = "";
		for (int i=0; i<diff; i++)
		{
			fill += with;
		}
		return fill;
	}

	private String makeListIncrememtalRow(String content, int index, int maxIDSize)
	{
		//TODO: make incremental list with symbol also
		int IDSize = numberOfDigits(index);
		String scarto = makeFill(IDSize, maxIDSize, " ");
		return prop.getProperty(IOProperties.PROP_TAB_SPACE) + scarto + IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_SYMBOL)) 
			+ index + IOColor.NOCOLOR + ") " + content + IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_SEPARATOR)) 
			+ prop.getProperty(IOProperties.PROP_LIST_SEPARATOR_INCREMENTAL);
	}

	private void printListIncremental(Vector<String> list, StringBuffer sb)
	{
		int size = list.size();
		int maxIDSize = numberOfDigits(size);

		sb.append("\n");
	 	for (int i=0; i<list.size(); i++)
		{
			sb.append(makeListIncrememtalRow(list.get(i), i, maxIDSize) + "\n");
		}
	}

	private void printListSymbol(Vector<String> list, StringBuffer sb)
	{
		sb.append("\n");
		for (int i=0; i<list.size(); i++)
		{
			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_SYMBOL)) 
				+ prop.getProperty(IOProperties.PROP_LIST_SYMBOL) + IOColor.NOCOLOR + " " + list.get(i) 
				+ IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_SEPARATOR)) + prop.getProperty(IOProperties.PROP_LIST_SEPARATOR_SYMBOL) 
				+ IOColor.NOCOLOR + "\n");
		}
	}

	private void printListInline(Vector<String> list, StringBuffer sb)
	{
		for (int i=0; i<list.size(); i++)
		{
			sb.append(list.get(i));
			if ((i+1) < list.size())
				sb.append(IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_SEPARATOR)) 
				+ prop.getProperty(IOProperties.PROP_LIST_SEPARATOR_INLINE) + IOColor.NOCOLOR + " ");
		}
		sb.append("\n");
	}
}
