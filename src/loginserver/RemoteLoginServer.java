package loginserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;
import parcmanclient.RemoteParcmanClient;

/**
 * Interfaccia remota del LoginServer.
 *
 * @author Parcman Tm
 */
public interface RemoteLoginServer extends Remote
{
	
 	public RemoteParcmanClient login(String name, String password) throws
		RemoteException;

   /**
     * Ping.
     *
     * @throws RemoteException Eccezione Remota
     */
    public void ping() throws RemoteException;
}

