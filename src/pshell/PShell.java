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

        PLog.debug("Pshell", "Prova");
        for (Method m : shell.getClass().getMethods())
        {
            PLog.debug("PShell", m.toString());
            if (m.isAnnotationPresent(PShellDataAnnotation.class))
            {
                PLog.debug("PShell", "Nuova Annotazione");
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

            }

            out.println("Hai scritto " + input);          
        }
    }
}

class CommandBean
{

}
