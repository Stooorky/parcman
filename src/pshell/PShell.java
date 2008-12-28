package pshell;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import io.Logger;
import io.IO;
import io.PropertyManager;
import io.IOProperties;
import io.IOColor;
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
	* input/output di Shell.
	*/
	private IO io;

	/**
	 * Logger
	 */
	private Logger logger;

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
		//this.out = shell.getOut();
		//this.in = shell.getIn();
		this.io = shell.getIO();
		this.commands = new Vector<CommandBean>();

		this.logger = Logger.getLogger("client-side");

		PShellDataAnnotation ant;

		logger.info("Avvio compilazione Shell...");

		boolean err = false;

		// Inizializzo il vettore comandi
		for (Method m : shell.getClass().getMethods())
		{
			if (m.isAnnotationPresent(PShellDataAnnotation.class))
			{
				try
				{
					ant = m.getAnnotation(PShellDataAnnotation.class);

					logger.debug("Nuovo comando: " + ant.name());

					CommandBean newc = new CommandBean();
					newc.method = ant.method();
					newc.name = ant.name();
					newc.info = ant.info();
					newc.help = ant.help();
					commands.add(newc);
				}
				catch(NullPointerException e)
				{
					logger.error("Impossibile aggiungere il comando per il metodo " + m.toString(), e);
					err = true;
				}
			}
		}

		if (!err)
			logger.info("Done.");
		else
			logger.error("Si sono verificati errori durante la compilazione.");
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
				input = io.readLine();
			}
			catch(IOException e)
			{
				logger.error("Impossibile leggere dal BufferedReader", e);
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
					error(MESSAGE_COMMAND_NOT_FOUND + ": " + inputsp[0]);
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
					println("Help per il comando \"" 
						+ commands.get(i).name + "\"\n" 
						+ commands.get(i).help);
					return;
				}

			println("Nessun help per " + arg);
			return;
		}

		LinkedHashMap<String, Vector<String>> table = new LinkedHashMap<String, Vector<String>>();
		Vector<String> cmdList = new Vector<String>();
		Vector<String> descList = new Vector<String>();
		for (int i=0; i<commands.size(); i++)
		{
			CommandBean c = commands.get(i);
			descList.add(c.info);
			cmdList.add(c.name);
		}
		table.put("NOME COMANDO", cmdList);
		table.put("DESCRIZIONE", descList);
		io.printTable(table, "Elenco comandi: ");

		println("Per vedere l'help di un comando specifico digita \"help <nome comando>\"");
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
				logger.info("Comando non trovato");
				error(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
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
			logger.error("Impossibile richiamare il metodo richiesto. (0)");
			error(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
		}
		catch (NullPointerException e)
		{
			logger.error("Impossibile richiamare il metodo richiesto. (1)");
			error(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
		}
		catch (NoSuchMethodException e)
		{
			logger.error("Impossibile richiamare il metodo richiesto. (2)");
			error(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
		}
		catch (InvocationTargetException e)
		{
			logger.error("Impossibile richiamare il metodo richiesto. (3)", e);
			error(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
		}
		catch (IllegalAccessException e)
		{
			logger.error("Impossibile richiamare il metodo richiesto. (4)");
			error(MESSAGE_COMMAND_NOT_FOUND + ": " + c);
		}
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

