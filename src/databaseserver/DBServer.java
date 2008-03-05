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
     * @param dbDirectory Directory radice del DataBase
     */
	public DBServer(String dbDirectory)
		throws ParcmanDBServerErrorRemoteException, RemoteException
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
     *
     * @param name Nome Utente
     * @return UserBean contenente i dati utente se esiste, null altrimenti
     */
	public UserBean getUser(String name)
		throws ParcmanDBServerErrorRemoteException,
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

    public void addUser(UserBean user)
        throws ParcmanDBServerUserExistRemoteException,
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

