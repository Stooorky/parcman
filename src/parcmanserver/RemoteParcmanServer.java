package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import indexingserver.RemoteIndexingServer;

/**
 * Interfaccia remota del ParcmanServer.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanServer
extends RemoteParcmanServerUser, Remote, Serializable
{
	/**
	 * esegue l'aggiunta di un nuovo client alla lista dei tentativi di connessione.
	 *
	 * @param username nome utente
	 * @param host host del client
	 * @throws remoteexception eccezione remota
	 */
	public void connectAttemp(String username, String host) throws
		RemoteException;

	/**
	 * Restituisce la lista degli utenti connessi al sistema
	 * E' necessario possedere lo stub del server di indicizzazione per poter fare questa richiesta.
	 *
	 * @param ris Stub del server di indicizzazione
	 * @throws remoteexception eccezione remota
	 */
	public Map<String, ClientData>  getConnectedUsers(RemoteIndexingServer ris) throws 
		RemoteException;
}

