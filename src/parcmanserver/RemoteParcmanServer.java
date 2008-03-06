package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del ParcmanServer.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanServer extends Remote
{
    /**
     * Ping.
     *
     * @return Pong
     * @throws RemoteException Eccezione Remota
     */
    public String ping() throws RemoteException;
}

