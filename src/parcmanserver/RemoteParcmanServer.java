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
	 * Esegue l'aggiunta di un nuovo client alla lista dei tentativi di connessione.
	 *
	 * @param username Nome utente
	 * @param host Host del client
	 * @throws RemoteException Eccezione remota
	 */
	public void connectAttemp(String username, String host) throws
		RemoteException;

	/**
	 * Setta la versione dei file condivisi di un utente.
	 *
	 * @param username Nome utente
	 * @param version Versione
	 * @throws RemoteException Eccezione remota
	 */
	public void setShareListVersionOfUser(String username, int version) throws
		RemoteException;

	/**
	 * Forza la riconnessione di un utente.
	 *
	 * @param username Nome utente
	 * @throws RemoteException Eccezione remota
	 */
	public void forceUserToReconnect(String username) throws
		RemoteException;

	/**
	 * Restituisce la lista degli utenti connessi al sistema
	 * E' necessario possedere lo stub del server di indicizzazione per poter fare questa richiesta.
	 *
	 * @param ris Stub del server di indicizzazione
	 * @throws remoteexception Eccezione remota
	 */
	public Map<String, ClientData>  getConnectedUsers(RemoteIndexingServer ris) throws 
		RemoteException;

	/**
	 * Ritorna lo stato del sistema di gestione degli agenti remoti. 
	 * Lo stato puo` essere attivo (true) oppure non attivo (false).
	 * 
	 * @param ris Stub dell'<tt>IndexingServer</tt>.
	 * @return un boolean che rappresenta lo stato. attivo = true, non attivo = false.
	 * @throws RemoteException 
	 */
	public boolean getAgentSystemStatus(RemoteIndexingServer ris) throws 
		RemoteException;

}

