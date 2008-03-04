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

public class DBServer implements RemoteDBServer
{
	private DB db;

	public DBServer(String dbDirectory)
		throws RemoteException
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

	public UserBean getUser(String name)
		throws RemoteException
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
}

