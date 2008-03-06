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
public interface RemoteDBServer extends Remote
{
	/**
	 * Restituisce i dati di un utente registrato nel database.
	 *
	 * @param name Nome dell'utente da ricercare
	 * @return UserBean dell'utente se esiste, null altrimenti
     * @throws RemoteException Eccezione remota
     * @throws ParcmanDBServerErrorRemoteException Errore interno del database
	 */

	public UserBean getUser(String name) throws
        RemoteException,
        ParcmanDBServerErrorRemoteException;

    /**
     * Aggiunge un utente al database.
     *
     * @param user UserBean dell'utente da aggiungere.
     * @throws RemoteException Eccezione remota
     * @throws ParcmanDBServerUserExistRemoteException L'utente e' gia' presente all'interno del database
     * @throws ParcmanDBServerUserNotValidRemoteException I dati forniti per l'utente non sono validi
     */

    public void addUser(UserBean user) throws
        RemoteException,
        ParcmanDBServerUserExistRemoteException,
        ParcmanDBServerUserNotValidRemoteException;

}

