package databaseserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import database.beans.*;

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
	 */
	public UserBean getUser(String name) throws RemoteException;

    /**
     * Aggiunge un utente al database.
     *
     * @param user UserBean dell'utente da aggiungere.
     */
    public void addUser(UserBean user) throws RemoteException;
}

