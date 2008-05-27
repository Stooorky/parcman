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
	extends Remote, Serializable
{
	/**
	 * Lancia la connessione alla rete Parcman.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void startConnection() throws
		RemoteException;

	/**
	 * Ping.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void ping() throws
		RemoteException;

	/**
	 * Disconnessione dalla rete Parcman.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void exit() throws
		RemoteException;

    /**
     * Ritorna il nome utente del proprietario della sessione.
     *
     * @return Nome utente del proprietario della sessione
     * @throws RemoteException Eccezione remota
     */
    public String getUserName() throws
        RemoteException;
}


