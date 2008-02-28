package clientbootstrap;

import java.net.*;
import java.rmi.server.*;
import java.rmi.RMISecurityManager;

import plog.*;

/**
 * Client Minimale per il BootStrap.
 *
 * @author Parcman Tm
 */
public class ClientBootstrap
{
	public static void main(String[] args) throws Exception
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
			PLog.err(e, "L'URL fornita non e' corretta.");
		}
		catch(ClassNotFoundException e)
		{
			PLog.err(e, "Caricamento della classe remota fallito.");
		}
		catch(Exception e)
		{
			// Aggiungere eventualmente la gestione delle eccezioni per Class.newInstance
			PLog.err(e, "Impossibile eseguire il BootStrap del Client");
		}
	}
}