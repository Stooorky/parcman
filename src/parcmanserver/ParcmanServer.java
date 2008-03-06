package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;

import plog.*;
import remoteexceptions.*;
import databaseserver.RemoteDBServer;

/**
 * Server centrale per la gestione degli utenti.
 *
 * @author Parcman Tm
 */
public class ParcmanServer
	extends UnicastRemoteObject
	implements RemoteParcmanServer
{

	private RemoteDBServer dbServer;
	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
     *
	 * @param dbServer Stub del DBServer
     * @throws RemoteException Eccezione remota
	 */
	public ParcmanServer(RemoteDBServer dbServer) throws
        RemoteException
	{
		this.dbServer = dbServer;

		// Testo il DBServer
		try
		{
			PLog.debug("PArcmanServer", "Tentativo di Ping verso il dbServer.");
			dbServer.ping();
		}
		catch (Exception e)
		{
			PLog.err(e, "ParcmanServer", "Impossibile pingare il DBServer");
			throw new RemoteException();
		}
	}

    /**
     * Metodo ping.
     *
     * @throws RemoteException Eccezione remota
     */
    public void ping() throws
        RemoteException
    {
        PLog.debug("ParcmanServer.ping", "E' stata ricevuta una richiesta di ping!");
    }
}

