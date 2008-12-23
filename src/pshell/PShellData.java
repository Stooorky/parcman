package pshell;

import java.io.*;
import java.lang.reflect.*;
import java.util.Vector;

import io.IO;
import io.PropertyManager;
import plog.*;

/**
* Contenitore dati di Shell
*
* @author Parcman Tm
*/
public abstract class PShellData
{
//	public static final String COLOR_BLACK=		"\033[30m";
//	public static final String COLOR_RED=		"\033[31m";
//	public static final String COLOR_GREEN=		"\033[32m";
//	public static final String COLOR_BORWN=		"\033[33m";
//	public static final String COLOR_BLUE=		"\033[34m";
//	public static final String COLOR_PURPLE=	"\033[35m";
//	public static final String COLOR_CYAN=		"\033[36m";
//	public static final String COLOR_LIGHT_GREY=	"\033[37m";
//	public static final String COLOR_DARK_GREY=	"\033[30;1m";
//	public static final String COLOR_LIGHT_RED=	"\033[31;1m";
//	public static final String COLOR_LIGHT_GREEN=	"\033[32;1m";
//	public static final String COLOR_YELLOW=	"\033[33;1m";
//	public static final String COLOR_LIGHT_BLUE=	"\033[34;1m";
//	public static final String COLOR_LIGHT_PURPLE=	"\033[35;1m";
//	public static final String COLOR_LIGHT_CYAN=	"\033[36;1m";
//	public static final String COLOR_WHITE=		"\033[37;1m";
//	public static final String COLOR_NOCOLOR=	"\033[0m";

	/**
	* Oggetto per gestione input/output.
	*/
	protected IO io;

	/**
	* Costruttore.
	*
	* @param out Output di Shell
	* @param in Input di Shell
	*/
	public PShellData(BufferedReader in, PrintWriter out)
	{
		this.io = new IO(in, out, PropertyManager.getInstance().get("io"));
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

