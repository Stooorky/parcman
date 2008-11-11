package databaseserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import database.beans.*;
import remoteexceptions.*;

/**
 * Interfaccia remota del DBServer.
 *
 * @author Parcman Tm
 */
public interface RemoteDBServer
	extends Remote
{
	/**
	* Restituisce i dati di un utente a partire dal nome.
	*
	* @param name Nome Utente
	* @return UserBean contenente i dati utente se esiste, null altrimenti
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerUserNotExistRemoteException Utente non presente nel database
	* @throws RemoteException Eccezione remota
	*/
	public UserBean getUser(String name) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerUserNotExistRemoteException,
		RemoteException;

	/**
	* Aggiunge un utente al database.
	* Se l'utente e' gia' presente all'interno del database solleva
	* l'eccezione remota ParcmanDBServerUserExistRemoteException.
	* Se i dati forniti non sono coerenti, cioe' non superano il test
	* UserBean.validate(), solleva l'eccezione ParcmanDBUserNotValidException.
	*
	* @param user UserBean contenente i dati dell'utente
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerUserExistRemoteException L'utente e' gia' presente all'interno del database
	* @throws ParcmanDBServerUserNotValidRemoteException I dati forniti per l'utente non sono validi
	* @throws RemoteException Eccezione remota
	*/
	public void addUser(UserBean user) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerUserExistRemoteException,
		ParcmanDBServerUserNotValidRemoteException,
		RemoteException;

	/**
	* Restituisce la lista Sharings di un utente.
	*
	* @param userName Nome utente
	* @return Vettore di ShareBean contenente la lista dei file condivisi dell'utente
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerUserNotExistRemoteException Utente non presente nel database
	* @throws RemoteException Eccezione remota
	*/
	public Vector<ShareBean> getSharings(String userName) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerUserNotExistRemoteException,
		RemoteException;

	/**
	* Aggiunge un file condiviso al database.
	* Se il file e` gia` presente all'interno del database solleva l'eccezione
	* remota ParcmanDBShareExistException. Se i dati forniti non sono coerenti
	* solleva l'eccezione ParcmanDBShareNotValidException. 
	*
	* @param share ShareBean contenente i dai del file condiviso.
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerShareExistRemoteException L'utente e` gia` presente all'interno del database.
	* @throws ParcmanDBServerShareNotValidException I dati forniti per il file da aggiundere non sono validi.
	* @throws RemoteException Eccezione remota.
	*/
	public void addShare(ShareBean share) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerShareExistRemoteException,
		ParcmanDBServerShareNotValidRemoteException,
		RemoteException;

	/**
	* Rimuove un file condiviso dal database.
	* Se il file non e` presente all'interno del database solleva l'eccezione
	* remota ParcmanDBShareNotExistException.
	*
	* @param id Id del file condiviso
    * @param owner Proprietario del file
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerShareNotExistRemoteException File non presente all'interno del database
	* @throws RemoteException Eccezione remota
	*/
	public void removeShare(int id, String owner) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerShareNotExistRemoteException,
		RemoteException;

	/**
	* Esegue una ricerca di file sul database.
	*
	* @param keywords Keywords per la ricerca
	* @return Vettore di SearchBean contenente il risultato della ricerca
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws RemoteException Eccezione remota
	*/
	public Vector<SearchBean> searchFiles(String keywords) throws
		ParcmanDBServerErrorRemoteException,
		RemoteException;

	/**
	 * Ping.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void ping() throws 
		RemoteException;

}
