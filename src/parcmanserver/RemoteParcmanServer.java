package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

/**
 * Interfaccia remota del ParcmanServer.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanServer
	extends RemoteParcmanServerUser, Remote, Serializable
{
    /**
     * esegue l'aggiunta di un nuovo client alla lista dei tentativi di connessione.
     *
     * @param username nome utente
     * @param host host del client
     * @throws remoteexception eccezione remota
     */
    public void connectAttemp(String username, String host) throws
        RemoteException;
}

