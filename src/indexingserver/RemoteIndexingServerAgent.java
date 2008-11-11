package indexingserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;
import parcmanserver.ClientData;
import parcmanagent.UpdateList;

/**
 * Interfaccia remota del ParcmanServer per il ParcmanAgent.
 *
 * @author Parcman Tm
 */
public interface RemoteIndexingServerAgent
extends Remote
{
    /**
     * Esegue l'update della lista di file condivisi a partire dalla
     * mappa updateLists.
     *
     * @param updateLists contiene i dati per l'aggiornamento delle
     * liste di file condivisi dagli utenti
     * @param validity Periodo di validità del ParcmanAgent chiamante
     * @throws RemoteException Eccezione Remota
     */
    public void sendUpdateLists(Map<ClientData, UpdateList> updateLists, long validity) throws
        RemoteException;

	/**
	 * Ping.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void ping() throws 
		RemoteException;
}

