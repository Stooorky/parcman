package databaseserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;

import databaseserver.*;
import database.*;
import plog.*;
import database.exceptions.*;
import database.beans.*;
import remoteexceptions.*;

/**
 * Server Database
 *
 * @author Parcman Tm
 */
public class DBServer implements RemoteDBServer
{
	private DB db;

    /**
     * Costruttore.
     *
     * @param dbDirectory Path della directory radice del database
     * @throws ParcmanDBServerErrorRemoteException Impossibile creare il DB
     */
	public DBServer(String dbDirectory) throws
        ParcmanDBServerErrorRemoteException
	{
		try
        {
            db = new DB(dbDirectory);
        }
        catch (ParcmanDBNotCreateException e)
        {
			PLog.err(e, "DBServer", "Impossibile istanziare il database.");
			throw new ParcmanDBServerErrorRemoteException();
        }
	}

    /**
     * Restituisce i dati di un utente a partire dal nome.
     * Se l'utente non e' presente all'interno del database restituisce null.
     *
     * @param name Nome Utente
     * @return UserBean contenente i dati utente se esiste, null altrimenti
     * @throws ParcmanDBServerErrorRemoteException Errore interno al database
     * @throws RemoteException Eccezione remota
     */
	public UserBean getUser(String name) throws
        ParcmanDBServerErrorRemoteException,
        RemoteException
	{
		try
		{
			return db.getUser(name);
		}
		catch(ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
		catch(ParcmanDBUserNotExistException e)
		{
			return null;
		}
	}

    /**
     * Aggiunge un utente al database.
     * Se l'utente e' gia' presente all'interno del database solleva
     * l'eccezione remota ParcmanDBServerUserExistRemoteException.
     * Se i dati forniti non sono coerenti, cioe' non superano il test
     * UserBean.validate(), solleva l'eccezione ParcmanDBUserNotValidException.
     *
     * @param user UserBean contenente i dati dell'utente
     * @throws ParcmanDBServerUserExistRemoteException L'utente e' gia' presente all'interno del database
     * @throws ParcmanDBServerUserNotValidRemoteException I dati forniti per l'utente non sono validi
     * @throws RemoteException Eccezione remota
     */
    public void addUser(UserBean user) throws
        ParcmanDBServerUserExistRemoteException,
        ParcmanDBServerUserNotValidRemoteException,
        RemoteException
    {
        try
        {
            db.addUser(user);
        }
        catch(ParcmanDBErrorException e)
        {
            throw new ParcmanDBServerErrorRemoteException();
        }
        catch(ParcmanDBUserExistException e)
        {
            throw new ParcmanDBServerUserExistRemoteException();
        }
        catch(ParcmanDBUserNotValidException e)
        {
            throw new ParcmanDBServerUserNotValidRemoteException();
        }
    }
}
