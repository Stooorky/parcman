package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;
import parcmanclient.RemoteParcmanClient;
import database.beans.SearchBean;
import database.beans.ShareBean;

/**
 * Interfaccia remota del ParcmanServer per gli utenti amministratori.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanServerAdmin
	extends RemoteParcmanServerUser, Remote, Serializable
{
    /**
     * Restituisce lla lista degli utenti connessi.
     *
     * @return Vettore contenente la lista dei nomi utente
     * @throws RemoteException Eccezione remota
     */
    public Vector<String> getConnectUsersList() throws
        RemoteException;
}

