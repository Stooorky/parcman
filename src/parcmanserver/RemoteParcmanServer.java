package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;
import parcmanclient.RemoteParcmanClient;

/**
 * Interfaccia remota del ParcmanServer.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanServer 
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
     * esegue l'aggiunta di un nuovo client alla lista dei tentativi di connessione.
     *
     * @param username nome utente
     * @param host host del client
     * @throws remoteexception eccezione remota
     */
    public void connectAttemp(String username, String host) throws
        RemoteException;

    /**
     * Ping.
     *
     * @throws RemoteException Eccezione Remota
     */
    public void ping() throws 
	    RemoteException;
}

