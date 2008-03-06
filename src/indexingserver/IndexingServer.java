package indexingserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;

import plog.*;
import remoteexceptions.*;

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
	 * Costruttore.
     *
     * @throws RemoteException Eccezione remota
	 */
	public IndexingServer() throws
        RemoteException
	{

	}

    /**
     * Metodo ping.
     *
     * @throws RemoteException Eccezione remota
     */
    public void ping() throws
        RemoteException
    {
        PLog.debug("IndexingServer.ping", "E' stata ricevuta una richiesta di ping!");
    }
}

