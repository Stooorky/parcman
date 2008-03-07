package loginserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del LoginServer.
 *
 * @author Parcman Tm
 */
public interface RemoteLoginServer extends Remote
{
    /**
     * Ping.
     *
     * @throws RemoteException Eccezione Remota
     */
    public void ping() throws RemoteException;
}

