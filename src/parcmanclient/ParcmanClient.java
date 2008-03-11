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
	implements RemoteParcmanClient, Serializable
{
	/**
	 * Stub del ParcmanServer
	 */
	private RemoteParcmanServer parcmanServerStub;

	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 4242L;

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
	 * Lancia la connessione alla rete Parcman.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void startConnection() throws
		RemoteException
	{
		try
		{
			PLog.debug("ParcmanClient.startConnection", "Avvio della connessione in corso.");
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
		/*
			try
			{
			PLog.debug("ParcmanClient.ping", "E' stata ricevuta una richiesta di ping da " + this.getClientHost());
			}
			catch(ServerNotActiveException e)
			{
			PLog.err(e, "ParcmanClient.ping", "Errore di rete, ClientHost irraggiungibile.");
			}
			*/
	}

	public void setParcmanServerStub(RemoteParcmanServer parcmanServerStub) throws
		RemoteException
	{
		this.parcmanServerStub = parcmanServerStub;
	}
}
