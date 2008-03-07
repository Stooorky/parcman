package loginserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;

import plog.*;
import remoteexceptions.*;

/**
 * Server di login.
 *
 * @author Parcman Tm
 */
public class LoginServer
	extends UnicastRemoteObject
	implements RemoteLoginServer
{
	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
     *
     * @throws RemoteException Eccezione remota
	 */
	public LoginServer() throws
        RemoteException
	{

	}

    /**
     * Metodo ping.
     *
     * @throws RemoteException Eccezione remota
     */
    public void ping() throws
        RemoteException
    {
        try
        {
            PLog.debug("LoginServer.ping", "E' stata ricevuta una richiesta di ping da " + this.getClientHost());
        }
        catch(ServerNotActiveException e)
        {
            PLog.err(e, "LoginServer.ping", "Errore di rete, ClientHost irraggiungibile.");
        }
    }
}

