package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import parcmanclient.RemoteParcmanClient;

/**
 * Interfaccia remota del ParcmanServer per gli utenti.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanServerUser 
	extends Remote, Serializable
{
    /**
     * Esegue la connessione di un nuovo RemoteParcmanClient alla rete Parcman.
     *
     * @param parcmanClientStub Stub del MobileServer
     * @param userName Nome utente proprietario della sessione
     * @throws RemoteException Eccezione Remota
     */
    public void connect(RemoteParcmanClient parcmanClientStub, String userName) throws
        RemoteException;

    /**
     * Ping.
     *
     * @throws RemoteException Eccezione Remota
     */
    public void ping() throws 
	    RemoteException;
}

