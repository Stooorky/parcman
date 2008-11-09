package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;
import parcmanagent.UpdateList;

/**
 * Interfaccia remota del ParcmanClient per l'agente remoto.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanClientAgent
	extends Remote, Serializable
{
    /**
     * Restituisce la lista di Update a partire dalla versione.
     *
     * @param version Versione della SharingList
     * @return UpdateList per la versione data
     * @throws RemoteException Eccezione remota
     */
    public UpdateList getUpdateList(int version) throws
        RemoteException;
}

