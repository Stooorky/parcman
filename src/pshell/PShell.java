package pshell;

import java.io.*;
import java.lang.reflect.*;

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

    private Vector<CommandBean> commands;

    /**
     * Stream di output.
     */
    private BufferedOutputStream out;

    /**
     * Costruttore.
     *
     * @param out OutputStream per la Shell
     * @param shell Dati della Shell
     */
    public PShell(BufferedOutputStream out, PShellData shell)
    {
        this.out = out;
        this.shell = shell;

    }

    /**
     * Stampa il prompt di shell
     */
    private void printPrompt()
    {
        out.println(shell.getPrompt());
    }
}

/**
 * Bean dati di shell
 *
 * @author Parcman Tm
 */
private class CommandBean
{
    private String name;
    private String desc;
    private String help;
}

