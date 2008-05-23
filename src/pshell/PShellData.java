package pshell;

import java.io.*;
import java.lang.reflect.*;

import plog.*;

/**
 * Contenitore dati di Shell
 * 
 * @author Parcman Tm
 */
public interface PShellData
{
    /**
     * Restituisce il prompt di shell.
     *
     * @return Stringa contenente il prompt di shell
     */
    public String getPrompt();
}

