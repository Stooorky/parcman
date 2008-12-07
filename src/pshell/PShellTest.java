package pshell;

import java.lang.*;
import java.io.*;
import java.io.BufferedInputStream;

import pshell.*;

public class PShellTest
{
    public static void main (String[] args)
    {
        PShell s = new PShell(new PShellTestClass());
        s.run();
    }
}

class PShellTestClass extends PShellData
{
    public PShellTestClass()
    {
        super(System.out, new BufferedReader(new InputStreamReader(System.in)));
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

