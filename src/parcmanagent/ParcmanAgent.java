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
	private Iterator<ClientDataForAgent> i;
	private ClientDataForAgent current;
	
	public ParcmanAgent(RemoteIndexingServer iserver, long validity, Vector<ClientDataForAgent> clients)
		throws RemoteException
	{
		i = clients.iterator();

		if (i.hasNext())
		{
			current = i.next();
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
		if (client.haveAnUpdate(current.getVersion()))
		{
			UpdateList list = client.getUpdateList(current.getVersion());
			/*
			 * TODO: salvataggio lista
			 */
			saveUpdateList(list);
		}
		else 
		{
			PLog.log("ParcmanAgent.run", "Il client "+current.getName() + " non ha aggiornamenti da consegnare.");
			current.setStatus(ClientDataForAgentStatus.NO_NEED_UPDATE);
		}

		if (i.hasNext())
		{
			current = i.next();
			transfer();
		}
	}


	private void transfer()
	{
		current.setStatus(ClientDataForAgentStatus.IN_UPDATE);
		RemoteParcmanClientAgent client = i.next().getStub();
		try
		{
			client.transferAgent((RemoteParcmanAgentClient) this);
		} 
		catch (RemoteException e)
		{
			PLog.err(e, "ParcmanAgent.transfer", "Non posso raggiungere il client " + current.getName() + ".");
			current.setStatus(ClientDataForAgentStatus.NOT_AVAILABLE);
		}
	}


	private void saveUpdateList(UpdateList list)
	{
		/* TODO: scrivere codice per salvare la lista */
		if (/* errore */ false)
		{
			PLog.err("ParcmanAgent.saveUpdateList", "Errore nel tentativo di ottenere l'aggiornamento di " + current.getName() + ".");
			current.setStatus(ClientDataForAgentStatus.UPDATE_FAILURE);
		}
	}
		
}

