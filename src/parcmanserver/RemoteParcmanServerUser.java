package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;
import parcmanclient.RemoteParcmanClient;
import database.beans.SearchBean;
import database.beans.ShareBean;
import parcmanclient.DownloadData;

/**
 * Interfaccia remota del ParcmanServer per gli utenti.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanServerUser
	extends Remote, Serializable
{
	/**
	* Esegue la connessione di un nuovo RemoteParcmanClient alla rete Parcman.
	* E' necessario possedere lo Stub dell'utente per poter fare questa richiesta.
	*
	* @param parcmanClientStub Stub del MobileServer
	* @param userName Nome utente proprietario della sessione
	* @throws RemoteException Eccezione Remota
	*/
	public void connect(RemoteParcmanClient parcmanClientStub, String userName) throws
		RemoteException;

	/**
	* Esegue la disconnessione di RemoteParcmanClient dalla rete Parcman.
	* E' necessario possedere lo Stub dell'utente per poter fare questa richiesta.
	*
	* @param parcmanClientStub Stub del MobileServer
	* @param userName Nome utente proprietario della sessione
	* @throws RemoteException Eccezione Remota
	*/
	public void disconnect(RemoteParcmanClient parcmanClientStub, String userName) throws
		RemoteException;

	/**
	* Restituisce la lista file condivisi dell'utente.
	* E' necessario possedere lo stub dell'utente per poter fare questa richiesta.
	*
	* @param parcmanClientStub Stub del MobileServer
	* @param userName Nome utente proprietario della sessione
	* @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	* @throws RemoteException Eccezione Remota
	*/
	public Vector<ShareBean> getSharings(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

	/**
	* Restituisce il numero di versione dei file condivisi dell'utente.
	* E' necessario possedere lo stub dell'utente per poter fare questa richiesta.
	*
	* @param parcmanClientStub Stub del MobileServer
	* @param userName Nome utente proprietario della sessione
	* @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	* @throws RemoteException Eccezione Remota
	*/
	public int getSharingsVersion(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

	/**
	* Restituisce il risultato di una ricerca sul database.
	* E' necessario possedere lo stub dell'utente per poter fare questa richiesta.
	*
	* @param parcmanClientStub Stub del MobileServer
	* @param userName Nome utente proprietario della sessione
	* @param keywords Lista di Keyword per la ricerca
	* @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	* @throws RemoteException Eccezione Remota
	*/
	public Vector<SearchBean> search(RemoteParcmanClient parcmanClientStub, String userName, String keywords) throws
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

    /**
     * Restituisce lla lista degli utenti connessi.
     *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
     * @return Vettore contenente la lista dei nomi utente
     * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi
     * errati
     * @throws RemoteException Eccezione remota
     */
    public Vector<String> getConnectUsersList(RemoteParcmanClient parcmanClientStub, String userName)  throws
        ParcmanServerWrongPrivilegesRemoteException,
        RemoteException;

	/**
	 * Inizializza il download di un file.
	 * E` necessario possedere lo stub dell'utente per poter fare questa richiesta.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @param data array di 2 elementi. Il primo e` il proprietario del file, il secondo e` l'ID del file.	
	 * @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	 * @throws RemoteException Eccezione Remota
	 */
	public DownloadData startDownload(RemoteParcmanClient parcmanClientStub, String userName, String[] fileData) throws 
		ParcmanServerRequestErrorRemoteException,
		ParcmanServerHackWarningRemoteException,
		RemoteException;

	/**
	* Ping.
	*
	* @throws RemoteException Eccezione Remota
	*/
	public void ping() throws
		RemoteException;
}

