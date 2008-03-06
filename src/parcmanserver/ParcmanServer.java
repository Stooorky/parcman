package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;

import plog.*;
import remoteexceptions.*;

/**
 * Server centrale per la gestione degli utenti.
 *
 * @author Parcman Tm
 */
public class ParcmanServer
	extends UnicastRemoteObject
	implements RemoteParcmanServer
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
	public ParcmanServer() throws
        RemoteException
	{

	}

    /**
     * Ritorna la stringa "Pong".
     *
     * @return Stringa "Pong"
     * @throws RemoteException Eccezione remota
     */
    public String ping() throws
        RemoteException
    {
        return "Pong";
    }
}

