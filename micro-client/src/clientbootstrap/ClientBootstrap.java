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

package clientbootstrap;

import java.net.*;
import java.rmi.server.*;
import java.rmi.RMISecurityManager;

/**
 * Client Minimale per il BootStrap.
 * Specificare:
 * remoteclient.loginserveradress, indirizzo del registro RMI su cui trovare il LoginServer
 * @author Parcman Tm
 */
public class ClientBootstrap
{
	public static void main(String[] args) throws 
		Exception
	{
		// Controllo la correttezza dei parametri da linea di comando
		if (args.length != 2)
		{
			System.out.println("USE: ClientBootstrap <URL codebase> <client class>");
			return;
		}

		// Istanzio il security manager
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
        
		try
		{
			// Carico il codice client dal codebase e lancio l'esecuzione
			Class classClient = RMIClassLoader.loadClass(args[0], args[1]);
			Runnable client = (Runnable)classClient.newInstance();
			client.run();
		}
		catch(MalformedURLException e)
		{
			System.out.println("L'URL fornita non e' corretta.");
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Caricamento della classe remota fallito.");
		}
		catch(Exception e)
		{
			// Aggiungere eventualmente la gestione delle eccezioni per Class.newInstance
			System.out.println("Impossibile eseguire il BootStrap del Client");
		}
	}
}

