package indexingserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del ParcmanServer.
 *
 * @author Parcman Tm
 */
public interface RemoteIndexingServer extends Remote
{
    /**
     * Ping.
     *
     * @throws RemoteException Eccezione Remota
     */
    public void ping() throws RemoteException;
}

