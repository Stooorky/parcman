package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;
import java.io.*;

import plog.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServer;

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
	 * Stub del ParcmanServer
	 */
	private RemoteParcmanServer parcmanServerStub;

	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
     *
     * @throws RemoteException Eccezione remota
	 */
	public ParcmanClient() throws
        RemoteException
	{
	}

	/**
	 * Costruttore.
     *
	 * @param parcmanServerStub Stub del ParcmanServer
     * @throws RemoteException Eccezione remota
	 */
	public ParcmanClient(RemoteParcmanServer parcmanServerStub) throws
        RemoteException
	{
		this.parcmanServerStub = parcmanServerStub;
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
			PLog.debug("PArcmanClient.startConnection", "Avvio della connessione in corso.");
			this.parcmanServerStub.ping();
		}
		catch(Exception e)
		{
			PLog.err(e, "ParcmanClient.startConnection", "Impossibile contattare il ParcmanServer.");
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
        try
        {
            PLog.debug("ParcmanClient.ping", "E' stata ricevuta una richiesta di ping da " + this.getClientHost());
        }
        catch(ServerNotActiveException e)
        {
            PLog.err(e, "ParcmanClient.ping", "Errore di rete, ClientHost irraggiungibile.");
        }
    }
}

