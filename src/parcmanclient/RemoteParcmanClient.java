package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del ParcmanClient.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanClient extends Remote
{
 	public void startConnection() throws
		RemoteException;

   /**
     * Ping.
     *
     * @throws RemoteException Eccezione Remota
     */
    public void ping() throws RemoteException;
}

