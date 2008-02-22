package clientbootstrap;
import java.net.*;
import java.rmi.server.*;
import java.rmi.RMISecurityManager;

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
			// TODO Gestire l'errore con un messaggio opportuno
			System.err.println(e);	
		}
		catch(ClassNotFoundException e)
		{
			// TODO Gestire l'errore con un messaggio opportuno
			System.err.println(e);
		}
		catch(Exception e)
		{
			// TODO Gestire l'errore con un messaggio opportuno
			// Aggiungere eventualmente la gestione delle eccezioni per Class.newInstance
			System.err.println(e);
		}
	}
}
