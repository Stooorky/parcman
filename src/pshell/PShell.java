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
    protected BufferedReader in;

    /**
     * Output di Shell.
     */
    protected PrintStream out;

    /**
     * Costruttore.
     *
     * @param shell Dati della Shell
     */
    public PShell(PrintStream out, BufferedReader in, PShellData shell)
    {
        this.shell = shell;
        this.out = out;
        this.in = in;
        this.commands = new Vector<CommandBean>();

        PShellDataAnnotation ant;

        PLog.debug("Pshell", "Avvio compilazione Shell...");
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
                }
            }
        }
    }

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

            if (this.isCommand(inputsp[0]))
                if (inputsp.length > 1)
                    this.runCommand(inputsp[0], inputsp[1]);
                else
                    this.runCommand(inputsp[0], "");
            else
                out.println("Command not found: " + inputsp[0]);
        }
    }

    private boolean isCommand(String c)
    {
        for (int i=0; i<commands.size(); i++)
            if (commands.get(i).name.equals(c))
                return true;
        return false;
    }

    private String getMethodCommand(String c)
    {
        for (int i=0; i<commands.size(); i++)
            if (commands.get(i).name.equals(c))
                return commands.get(i).method;
        return null;
    }

    public void runCommand(String c, String arg)
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
                out.println("Command not found: " + c);
                return;
            }

            Object[] args = {arg};
			// TODO Warning in fase di compilazione. Controllare se e' evitabile.
			Method m = shellClass.getMethod(getMethodCommand(c), argsTypes);
			m.invoke(shell, args);
			return;

		}
		catch (SecurityException e)
		{
			PLog.err(e, "Shell.runCommand", "Impossibile richiamare il metodo richiesto.");
            out.println("Command not found: " + c);
		}
		catch (NullPointerException e)
		{
			PLog.err(e, "Shell.runCommand", "Impossibile richiamare il metodo richiesto.");
            out.println("Command not found: " + c);
		}
		catch (NoSuchMethodException e)
		{
			PLog.err(e, "Shell.runCommand", "Impossibile richiamare il metodo richiesto.");
            out.println("Command not found: " + c);
		}
		catch (InvocationTargetException e)
		{
            out.println("Command not found: " + c);
		}
        catch (IllegalAccessException e)
		{
            out.println("Command not found: " + c);
		}
    }
}

class CommandBean
{
    public String method;
    public String name;
    public String info;
    public String help;
}

