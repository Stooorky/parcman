package pshell;

import java.io.*;
import java.lang.reflect.*;
import java.util.Vector;

import plog.*;

/**
* Contenitore dati di Shell
*
* @author Parcman Tm
*/
public abstract class PShellData
{
	/**
	* Input di Shell.
	*/
	protected BufferedReader in;

	/**
	* Output di Shell.
	*/
	protected PrintStream out;

	/**
	* Costruttore.
	*
	* @param out Output di Shell
	* @param in Input di Shell
	*/
	public PShellData(PrintStream out, BufferedReader in)
	{
		this.out = out;
		this.in = in;
	}

	/**
	* Stampa il prompt della shell.
	*/
	public void writePrompt()
	{
	        out.print("\033[31;1m-->\033[0m ");
	}

	/**
	* Restituisce l'input di Shell.
	*
	* @return BufferedReader di input
	*/
	public BufferedReader getIn()
	{
		return in;
	}

	/**
	* Restituisce l'output di Shell.
	*
	* @return PrintStream di output
	*/
	public PrintStream getOut()
	{
		return out;
	}

	public void writeTable(Vector<Vector<String>> table, int cols, int rows, String header)
	{
		out.println();
		out.println(header);
		int[] widths = new int[cols];
		for (int i=0; i<widths.length; i++)
			widths[i] = 0;
		
		// calcolo la width delle colonne.
		for (int c=0; c<table.size(); c++) // ciclo le colonne
		{
			Vector<String> col = table.get(c);
			for (int r=0; r<col.size(); r++) // ciclo le righe
			{
				String row = col.get(r);
				if (row.length() > widths[c])
					widths[c] = row.length();
			}
		}
		

		String row_separator = make_rowseparator(widths);
		out.println(row_separator);

		for (int i=0; i<rows; i++)
		{
			String row = "|";
			for (int j=0; j<cols; j++)
			{
				Vector<String> col = table.get(j);
				row += fill_cell(col.get(i), widths[j], " ");
				if (j<cols)
					row += "|";
			}
			out.println(row);
			out.println(row_separator);
		}
	}

	private String fill_cell(String str, int max, String with)
	{
		int len=str.length();
		str = with + str;
		while ( (max--) > len)
			str += with;
		str += with;
		return str;
	}

	private String make_rowseparator(int[] widths)
	{
		String sep = "+";
		for (int i=0; i<widths.length; i++)
		{
			sep += fill_cell("", widths[i], "-");
			if (i<widths.length)
				sep += "+";
		}
		return sep;
	}
}

