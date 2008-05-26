package pshell;

import java.lang.*;
import java.io.*;
import java.io.BufferedInputStream;

import pshell.*;

public class PShellTest
{
    public static void main (String[] args)
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PShell s = new PShell(System.out, in, new PShellTestClass(in));
        s.run();
    }
}

class PShellTestClass extends PShellData
{
    public PShellTestClass(BufferedReader in)
    {
        super(System.out, in);
    }

    public void writePrompt()
    {
        out.print("> ");
    }

    @PShellDataAnnotation(
        method = "commandYes",
        name = "yes",
        info = "use: yes",
        help = "stampa la stringa yes"
    )
    public void commandYes (String param)
    {
        out.println("yes"); 
    }
}
