package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;
import parcmanclient.RemoteParcmanClient;
import database.beans.ShareBean;

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
	* Ping.
	*
	* @throws RemoteException Eccezione Remota
	*/
	public void ping() throws
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
}

