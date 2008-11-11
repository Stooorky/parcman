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
     * Restituisce true se il ParcmanClient ha bisogno di eseguire un
     * update, cioè l'UpdateList per la versione fornita non e' vuota.
     *
     * @param versione Versione della SharingList
     * @return true se il ParcmanClient ha bisogno di eseguire un update
     * della lista dei file condivisi
     * @throws RemoteException Eccezione remota
     */
    public boolean haveAnUpdate(int version) throws
        RemoteException;

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

