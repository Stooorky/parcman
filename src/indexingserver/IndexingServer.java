package indexingserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;

import plog.*;
import remoteexceptions.*;
import databaseserver.RemoteDBServer;
import parcmanserver.RemoteParcmanServer;

/**
 * Server di indicizzazione.
 *
 * @author Parcman Tm
 */
public class IndexingServer
	extends UnicastRemoteObject
	implements RemoteIndexingServer
{
	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 42L;

	/**
	* Stub del DBServer.
	*/
	private RemoteDBServer dBServer;

	/**
	* Stub del ParcmanServer.
	*/
	private RemoteParcmanServer parcmanServer;

	/**
	* Costruttore.
	*
	* @param dbServer Stub del DBServer
	* @param parcmanServer Stub del server centrale
	* @throws RemoteException Eccezione remota
	*/
	public IndexingServer(RemoteDBServer dBServer, RemoteParcmanServer parcmanServer) throws
		RemoteException
	{
		this.dBServer = dBServer;
		this.parcmanServer = parcmanServer;
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
			PLog.debug("IndexingServer.ping", "E' stata ricevuta una richiesta di ping da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "IndexingServer.ping", "Errore di rete, ClientHost irraggiungibile.");
		}
	}
}
