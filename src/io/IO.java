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
import java.io.IOException;
import java.util.regex.PatternSyntaxException;
import prova.exceptions.*;

public class IO 
{
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

	public void printList(	Vector<String> list, String caption, String type) throws
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

		StringBuffer sb = new StringBuffer();
		sb.append(IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_CAPTION)) + caption 
			+ IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_PROPERTIES))
			+ " [ count:" + list.size() + " ]" 
			+ IOColor.getColor(prop.getProperty(IOProperties.PROP_LIST_COLOR_CAPTION))
			+ ": " + IOColor.NOCOLOR);

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


		String rowSeparatorLine = makeRowSeparator(widths, prop.getProperty(IOProperties.PROP_TABLE_HORIZONTAL_SEPARATOR));
		String headRowSeparatorLine = makeRowSeparator(widths, prop.getProperty(IOProperties.PROP_TABLE_HEAD_HORIZONTAL_SEPARATOR));

		sb.append(IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_CAPTION)) + caption + IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_PROPERTIES)) 
			+ " [ cols:" + cols + ",rows:" + rows + " ]" + IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_CAPTION)) + ": " + IOColor.NOCOLOR + "\n");
		sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_HEAD_ROW)) + headRowSeparatorLine + "\n");
		sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + makeHeaders(table, widths) + "\n");
		sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + headRowSeparatorLine + IOColor.NOCOLOR + "\n");

		Set<String> keys = table.keySet();
		for (int i=0; i<rows; i++)
		{
			String color = IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_ROW_ODD));
			if ((i%2) == 0)
			{
				color = IOColor.getColor(prop.getProperty(IOProperties.PROP_TABLE_COLOR_ROW_EVEN));
			} 

			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + color + makeRow(table, keys, i, widths) + IOColor.NOCOLOR + "\n");
			sb.append(prop.getProperty(IOProperties.PROP_TAB_SPACE) + rowSeparatorLine + "\n");
		}
		println(sb.toString());
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
				+ fillCell(i.next(), widths[pos], prop.getProperty(IOProperties.PROP_FILL_WITH));
			if (!i.hasNext())
				headers += prop.getProperty(IOProperties.PROP_TABLE_VERTICAL_SEPARATOR);
			pos++;
		}
		return headers;
	}

	private String makeRowSeparator(int[] widths, String with)
	{
		String sep = prop.getProperty(IOProperties.PROP_TABLE_CROSS_SEPARATOR);
		for (int i=0; i<widths.length; i++)
		{
			sep += fillCell("", widths[i], with);
			if (i<widths.length)
				sep += prop.getProperty(IOProperties.PROP_TABLE_CROSS_SEPARATOR);
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
