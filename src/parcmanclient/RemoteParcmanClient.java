package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del ParcmanClient.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanClient
	extends RemoteParcmanClientUser, RemoteParcmanClientAgent, Remote, Serializable
{
    /**
	 * Lancia la connessione alla rete Parcman.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void startConnection() throws
		RemoteException;

    /**
     * Forza la riconnessione del client.
     *
     * @throws RemoteException Eccezione Remota
     */
    public void reconnect() throws
        RemoteException;

	/**
	 * Ping.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void ping() throws
		RemoteException;
}


