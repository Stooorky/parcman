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
     * Stampa il prompt shell.
     */
    public abstract void writePrompt();
}

