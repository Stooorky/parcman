package pshell;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import plog.*;
import pshell.*;


/**
* Gestore di Shell
*
* @author Parcman Tm
*/
public class PShell
{
	/**
	* Dati della shell da gestire.
	*/
	private PShellData shell;

	/**
	* Comandi di Shell.
	*/
	private Vector<CommandBean> commands;

	/**
	* Input di Shell.
	*/
	private BufferedReader in;

	/**
	* Output di Shell.
	*/
	private PrintStream out;

	/**
	* Messaggio "Comando non trovato"
	*/
	private final String MESSAGE_COMMAND_NOT_FOUND = "Command not found";

	/**
	* Costruttore.
	*
	* @param shell Dati della Shell
	*/
	public PShell(PShellData shell)
	{
		this.shell = shell;
		this.out = shell.getOut();
		this.in = shell.getIn();
		this.commands = new Vector<CommandBean>();

		PShellDataAnnotation ant;

		PLog.debug("Pshell", "Avvio compilazione Shell...");

		boolean err = false;

		// Inizializzo il vettore comandi
		for (Method m : shell.getClass().getMethods())
		{
			if (m.isAnnotationPresent(PShellDataAnnotation.class))
			{
				try
				{
					ant = m.getAnnotation(PShellDataAnnotation.class);

					PLog.debug("PShell", "Nuovo comando: " + ant.name());

					CommandBean newc = new CommandBean();
					newc.method = ant.method();
					newc.name = ant.name();
					newc.info = ant.info();
					newc.help = ant.help();
					commands.add(newc);
				}
				catch(NullPointerException e)
				{
					PLog.err(e, "PShell", "Impossibile aggiungere il comando per il metodo " + m.toString());
					err = true;
				}
			}
		}

		if (!err)
			PLog.debug("Pshell", "Done.");
		else
			PLog.debug("Pshell", "Si sono verificati errori durante la compilazione.");
	}

	/**
	* Lancia l'esecuzione della Shell
	*/
	public void run()
	{
		String input = "";

		while(true)
		{
			shell.writePrompt();
			try
			{
				input = in.readLine();
			}
			catch(IOException e)
			{
				PLog.err(e, "PShell.run", "Impossibile leggere dal BufferedReader");
			}

			String[] inputsp = input.split(" ", 2);

			if (inputsp[0].equals("help"))
				if (inputsp.length > 1)
					this.runHelp(inputsp[1]);
				else
					this.runHelp(null);
			else
			{
				if (this.isCommand(inputsp[0]))
					if (inputsp.length > 1)
						this.runCommand(inputsp[0], inputsp[1]);
					else
						this.runCommand(inputsp[0], "");
				else if (!inputsp[0].equals(""))
					out.println(MESSAGE_COMMAND_NOT_FOUND + ": " + inputsp[0]);
			}
		}
	}

	/**
	* Controlla se la stringa data rappresenta un comando.
	*
	* @param c Stringa del comando
	* @return true se c e' un comando, false altrimenti
	*/
	private boolean isCommand(String c)
	{
		for (int i=0; i<commands.size(); i++)
			if (commands.get(i).name.equals(c))
				return true;
		return false;
	}

	/**
	* Restituisce il nome del Metodo per il comando c
	*
	* @param c Stringa del comando
	* @return Nome del metodo per il comando c
	*/
	private String getMethodCommand(String c)
	{
		for (int i=0; i<commands.size(); i++)
			if (commands.get(i).name.equals(c))
				return commands.get(i).method;
		return null;
	}

	private void runHelp(String arg)
	{
		if (arg != null)
		{
			for (int i=0; i<commands.size(); i++)
				if (commands.get(i).name.equals(arg))
				{
					out.println("Help per il comando \"" + commands.get(i).name + "\"\n" +
							commands.get(i).help);
					return;
				}

			out.println("Nessun help per " + arg);
			return;
		}

		out.println("Elenco comandi:");
		for (int i=0; i<commands.size(); i++)
		out.println("\t" + commands.get(i).name + "-> " + commands.get(i).info);

		out.println("Per vedere l'help di un comando specifico digita \"help <nome comando>\"");
	}

	/**
	* Esegue il comando c con parametri arg
	*
	* @param c Stringa del comando
	* @param arg Stringa dei parametri
	*/
	private void runCommand(String c, String arg)
	{
		Class shellClass = shell.getClass();
		Class[] argsTypes;

		argsTypes = new Class[1];
		argsTypes[0] = arg.getClass();

		try
		{
			String method = getMethodCommand(c);

			if (method == null)
			{
				PLog.debug("PShell.runCommand", "Comando non trovato");
				out.println(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
				return;
			}

			Object[] args = {arg};
			// TODO Warning in fase di compilazione. Controllare se e' evitabile.
			Method m = shellClass.getMethod(method, argsTypes);
			m.invoke(shell, args);
			return;

		}
		catch (SecurityException e)
		{
			PLog.err(e, "Shell.runCommand", "Impossibile richiamare il metodo richiesto.");
			out.println(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
		}
		catch (NullPointerException e)
		{
			PLog.err(e, "Shell.runCommand", "Impossibile richiamare il metodo richiesto.");
			out.println(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
		}
		catch (NoSuchMethodException e)
		{
			PLog.err(e, "Shell.runCommand", "Impossibile richiamare il metodo richiesto.");
			out.println(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
		}
		catch (InvocationTargetException e)
		{
			PLog.err(e, "Shell.runCommand", "Impossibile richiamare il metodo richiesto.");
			out.println(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
		}
		catch (IllegalAccessException e)
		{
			PLog.err(e, "Shell.runCommand", "Impossibile richiamare il metodo richiesto.");
			out.println(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
		}
	}
}

/**
* Classe Bean per un Comando
*
* @author Parcman Tm
*/
class CommandBean
{
	/**
	* Nome del metodo.
	*/
	public String method;

	/**
	* Nome del comando.
	*/
	public String name;

	/**
	* Info del comando.
	*/
	public String info;

	/**
	* Help per il comando.
	*/
	public String help;
}

