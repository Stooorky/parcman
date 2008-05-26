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
        info = "Stampa a video yes",
        help = "\tstampa a video la stringa yes.\n\tuse: yes" 
    )
    public void commandYes (String param)
    {
        out.println("yes"); 
    }

    @PShellDataAnnotation(
        method = "commandHello",
        name = "hello",
        info = "Stampa a video Hello seguito da un nome",
        help = "\tstampa a video la stringa \"hello\" seguita dal nome dato come parametro.\n\t" +
            "use: hello <nome>"
    )
    public void commandHello (String param)
    {
        out.println("hello " + param);
    }
}
