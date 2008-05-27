package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import plog.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServerUser;
import pshell.*;

/**
 * Mobile server in esecuzione presso il Client.
 *
 * @author Parcman Tm
 */
public class ParcmanClient
	extends UnicastRemoteObject
	implements RemoteParcmanClient, Serializable
{
	/**
	 * Stub del ParcmanServer.
	 */
	private RemoteParcmanServerUser parcmanServerStub;

	/**
	* Nome utente.
	*/
	private String userName;

	/**
	* SerialVersionUID.
	*/
	private static final long serialVersionUID = 4242L;

	/**
	* Costruttore.
	*
	* @throws RemoteException Eccezione remota
	*/
	public ParcmanClient(RemoteParcmanServerUser parcmanServerStub, String userName) throws
		RemoteException
	{
		this.parcmanServerStub = parcmanServerStub;
		this.userName = userName;
	}

	/**
	* Lancia la connessione alla rete Parcman.
	*
	* @throws RemoteException Eccezione Remota
	*/
	public void startConnection() throws
		RemoteException
	{
		try
		{
			// Spedisco lo stub del ParcmanClient al ParcmanServer
			parcmanServerStub.connect(this, this.userName);
		}
		catch(RemoteException e)
		{
			System.out.println(e.getMessage());
			System.out.println("Autenticazione fallita.");
			System.exit(0);
		}

		System.out.println("Autenticazione alla rete Parcman avvenuta con successo. Benvenuto!.");

		PShell shell = new PShell(new ShellData(this.parcmanServerStub, this, this.userName));
		shell.run();
	}

	/**
	* Metodo ping.
	*
	* @throws RemoteException Eccezione remota
	*/
	public void ping() throws
		RemoteException
	{
		try
		{
			PLog.debug("ParcmanClient.ping", "E' stata ricevuta una richiesta di ping da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "ParcmanClient.ping", "Errore di rete, ClientHost irraggiungibile.");
		}
	}

	/**
	* Esegue la disconnessione dalla rete.
	*
	* @throws RemoteException Eccezione remota
	*/
	public void exit() throws
		RemoteException
	{
		PLog.debug("ParcmanClient.exit", "Disconnessione in corso.");

		try
		{
			this.parcmanServerStub.disconnect(this, this.userName);
		}
		catch(RemoteException e)
		{
			System.out.println(e.getMessage());
			System.out.println("Disconnessione fallita. (force exit)");
			System.exit(1);
		}

		System.exit(0);
	}

	/**
	* Ritorna il nome utente del proprietario della sessione.
	*
	* @return Nome utente del proprietario della sessione
	* @throws RemoteException Eccezione remota
	*/
	public String getUserName() throws
		RemoteException
	{
		return this.userName;
	}
}
