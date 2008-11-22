package parcmanagent;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import plog.*;
import remoteexceptions.*;
//import indexingserver.RemoteIndexingServer;
import parcmanserver.ClientData;
import indexingserver.RemoteIndexingServer;
import parcmanclient.RemoteParcmanClientAgent;

/**
 * Parcman Agent.
 *
 * @author Parcman Tm
 */
public class ParcmanAgent
	implements RemoteParcmanAgent, Serializable
{
    RemoteIndexingServer iServer;
    private Map<ClientDataForAgent, UpdateList> clientsData;
	private ClientDataForAgent current;
    long validity;
	
	public ParcmanAgent(RemoteIndexingServer iServer, long validity, Vector<ClientDataForAgent> clients)
		throws RemoteException
	{
        this.clientsData = new HashMap<ClientDataForAgent, UpdateList>();
        this.iServer = iServer;
        this.validity = validity;

        if (clients.size() == 0)
        {
            PLog.debug("ParcmanAgent", "Nessun client da visitare");
            throw new RemoteException();
        }

        for (int i=0; i<clients.size(); i++)
            this.clientsData.put(clients.get(i), null);

		Iterator iter = clientsData.keySet().iterator();

		if (iter.hasNext())
        {
            current = (ClientDataForAgent) iter.next();
			transfer();
        }
	}

	/**
	 * Avvia l'esecuzione del ParcmanAgent.
	 */
	public void run() throws
		RemoteException
	{    
		RemoteParcmanClientAgent client = current.getStub();

        try
        {
		    if (client.haveAnUpdate(current.getVersion()))
		    {
			    UpdateList list = client.getUpdateList(current.getVersion());
			    saveUpdateList(list);
		    }
		    else 
		    {
			    PLog.log("ParcmanAgent.run", "Il client "+current.getName() + " non ha aggiornamenti da consegnare.");
			    current.setStatus(ClientDataForAgentStatus.NO_NEED_UPDATE);
		    }
        }
        catch (RemoteException e)
        {
            PLog.debug("ParcmanAgent.run", "Eccezione Remota");
            /*TODO Gestire tutte le eccezioni del caso */
        }

        this.goNext();
	}

    private void goNext()
    {
		Iterator iter = clientsData.keySet().iterator();

        boolean test = false;

		while (iter.hasNext())
        {
            current = (ClientDataForAgent) iter.next();
            if (current.getStatus() == ClientDataForAgentStatus.READY)
            {
                test = true;
		    	transfer();
            }
        }

        if (!test)
        {
            try
            {
                iServer.sendUpdateLists(this.clientsData, this.validity);
            }
            catch (RemoteException e)
            {
                PLog.debug("ParcmanAgent.run", "Impossibile inviare la lista di update all'IndexingServer");
                return;
            }
        }

    }

	private void transfer()
	{
        current.setStatus(ClientDataForAgentStatus.IN_UPDATE);

		RemoteParcmanClientAgent client = current.getStub();

		try
		{
			client.transferAgent((RemoteParcmanAgentClient) this);
		}
		catch (RemoteException e)
		{
			PLog.err("ParcmanAgent.transfer", "Non posso raggiungere il client " + current.getName() + ".");
			current.setStatus(ClientDataForAgentStatus.NOT_AVAILABLE);
            goNext();
		}
	}

	private void saveUpdateList(UpdateList list)
	{
		current.setStatus(ClientDataForAgentStatus.UPDATED);

		if (list == null)
		{
			PLog.err("ParcmanAgent.saveUpdateList", "Errore nel tentativo di ottenere l'aggiornamento di " + current.getName() + ".");
			current.setStatus(ClientDataForAgentStatus.UPDATE_FAILURE);
            return;
		}

        clientsData.put(current, list);
	}
}

