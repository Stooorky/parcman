package pshell;

import java.io.*;
import java.lang.reflect.*;

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
}

