package remoteclient;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

import plog.*;
import loginserver.RemoteLoginServer;
import parcmanclient.RemoteParcmanClient;

/**
 * Client remoto per utenti.
 *
 * @author Parcman Tm
 */
public class RemoteClientUser implements RemoteClient
{
	private static final String LOGIN_SERVER_ADRESS = "//:1098/LoginServer";
	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Avvia il Client remoto.
	 */
	public void run()
	{
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader myInput = new BufferedReader(input);
		String userName = null;
		String password = null;

		try
		{
			// Inserimento del nome utente
			System.out.println("Inserisci il nome utente:");
			userName = new String(myInput.readLine());

			// Inserimento della password
			System.out.println("Inserisci la Password: ");
			password = new String(myInput.readLine());
		}
		catch(IOException e)
		{
			PLog.err(e, "RemoteClientUser.run", "Impossibile leggere dallo STDIN.");
			System.exit(1);
		}

		try
		{
			// Faccio la lookup al server remoto di login
			RemoteLoginServer loginServer = (RemoteLoginServer)Naming.lookup(this.LOGIN_SERVER_ADRESS);
			RemoteParcmanClient parcmanServer = (RemoteParcmanClient)loginServer.login(userName, password);
			
			if (parcmanServer == null)
			{
				System.out.println("Nome utente o password errati.");
				return;	
			}

			// Avvio il MobileServer
			parcmanServer.startConnection();
		}
		catch(Exception e)
		{
			PLog.err(e, "RemoteClientUser.run", "Impossibile eseguire il Login.");
		}
	}
}

