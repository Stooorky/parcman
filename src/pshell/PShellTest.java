/*
 * Parcman Project, the Italian Arcade Network
 * Copyright (C) 2008-2009 Parcman Tm (Marchi Sirio, Marcantoni Francesco, Emanuele Dona')
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 *
 * E-Mail:	ramyel [at] gmail [dot] com
 * 			sirio.marchi [at] gmail [dot] com
 * 			emanuele.dona [at] gmail [dot] com
 */

package pshell;

import java.lang.*;
import java.io.*;
import java.io.BufferedInputStream;
import java.io.PrintWriter;

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
        super(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out));
    }

    @PShellDataAnnotation(
        method = "commandYes",
        name = "yes",
        info = "Stampa a video yes",
        help = "\tstampa a video la stringa yes.\n\tuse: yes" 
    )
    public void commandYes (String param)
    {
        io.println("yes"); 
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
        io.println("hello " + param);
    }
}

