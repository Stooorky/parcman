package parcmanagent;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;
//import indexingserver.RemoteIndexingServer;
import parcmanserver.ClientData;
import indexingserver.RemoteIndexingServer;
import parcmanclient.RemoteParcmanClientAgent;
import logserver.RemoteLogServer;

/**
 * Parcman Agent.
 *
 * @author Parcman Tm
 */
public class ParcmanAgent
extends UnicastRemoteObject
implements RemoteParcmanAgent, Serializable
{
	private RemoteIndexingServer iServer;
	private Map<String, ClientDataForAgent> clientsData;
	private String current;
	private long validity;
	private int identify;
	private RemoteLogServer logger;

	private int READY = 6;
	private int IN_UPDATE = 1;
	private int UPDATED = 2;
	private int NO_NEED_UPDATE = 3;
	private int UPDATE_FAILURE = 4;
	private int NOT_AVAILABLE = 5;

	private String LOGGER_ERROR = "LogServer irraggiungibile";

	public ParcmanAgent(RemoteIndexingServer iServer, long validity, Vector<ClientDataForAgent> clients, int identify, RemoteLogServer logServer)
		throws RemoteException
	{
		this.clientsData = new HashMap<String, ClientDataForAgent>();
		this.iServer = iServer;
		this.validity = validity;
		this.identify = identify;
		this.logger = logServer;

		if (clients.size() == 0)
		{
			logger.info(log("Nessun client da visitare"));
			throw new RemoteException();
		}

		for (int i=0; i<clients.size(); i++)
			this.clientsData.put(clients.get(i).getName(), clients.get(i));
	}

	private String log(String str)
	{
		ClientDataForAgent data = this.clientsData.get(current);
		return "ParcmanAgent" + "#" + this.identify + "@" + current + "[" + data.getStatus() + "]: " + str;
	}

	public void start() throws
		RemoteException
	{
		Iterator<String> iter = clientsData.keySet().iterator();

		if (iter.hasNext())
		{
			current = iter.next();
			transfer();
		}
	}

	/**
	 * Avvia l'esecuzione del ParcmanAgent.
	 */
	public void run() throws
		RemoteException
	{
		ClientDataForAgent data = this.clientsData.get(current);
		data.setStatus(this.IN_UPDATE);
		RemoteParcmanClientAgent client = data.getStub();

		try
		{
			unexportObject(this, true);
			if (client.haveAnUpdate(data.getVersion()))
			{
				UpdateList list = client.getUpdateList(data.getVersion());
				saveUpdateList(list);
			}
			else
			{
				logger.info(log("Il client " + current + " non ha aggiornamenti da consegnare."));
				data.setStatus(this.NO_NEED_UPDATE);
			}
		}
		catch (RemoteException e)
		{
			try
			{
				logger.error(log("Impossibile comunicare con il client " + current));
			}
			catch (RemoteException re)
			{
				System.out.println(LOGGER_ERROR);
			}
			/*TODO Gestire tutte le eccezioni del caso */
			data.setStatus(this.NOT_AVAILABLE);
		}

		this.goNext();
	}

	private void goNext()
	{
		Iterator<String> iter = clientsData.keySet().iterator();

		while (iter.hasNext())
		{
			current = iter.next();
			ClientDataForAgent data = this.clientsData.get(current);

			if (data.getStatus() == this.READY)
			{
				transfer();
				return;
			}
		}
		/*
		   iter = clientsData.keySet().iterator();
		   while (iter.hasNext())
		   {
		   ClientDataForAgent data = this.clientsData.get(iter.next());
		   if (data.getStatus() == this.READY)
		   return;
		   }
		   */
		try
		{
			iServer.sendUpdateLists(this.clientsData, this.validity, this.identify);
		}
		catch (RemoteException e)
		{
			try
			{
				logger.error(log("Impossibile inviare la lista di update all'IndexingServer"));
			}
			catch (RemoteException re)
			{
				System.out.println(LOGGER_ERROR);
			}
			return;
		}
	}

	private void transfer()
	{
		ClientDataForAgent data = this.clientsData.get(current);
		RemoteParcmanClientAgent client = data.getStub();

		try
		{
			data.setStatus(this.IN_UPDATE);
			logger.info(log("Trasferimento verso " + current));
			client.transferAgent((RemoteParcmanAgentClient) this);
		}
		catch (RemoteException e)
		{
			try
			{
				logger.error(log("Non posso raggiungere il client " + current + "."));
			}
			catch (RemoteException re)
			{
				System.out.println(LOGGER_ERROR);
			}
			data.setStatus(this.NOT_AVAILABLE);
			goNext();
		}
	}

	private void saveUpdateList(UpdateList list)
	{
		ClientDataForAgent data = this.clientsData.get(current);
		RemoteParcmanClientAgent client = data.getStub();

		if (list == null)
		{
			try
			{
				logger.error(log("Errore nel tentativo di ottenere l'aggiornamento di " + current + "."));
			}
			catch (RemoteException re)
			{
				System.out.println(LOGGER_ERROR);
			}
			data.setStatus(this.UPDATE_FAILURE);
			return;
		}

		try
		{
			logger.info(log("Il client " + current + " ha consegnato la versione " + list.getVersion()));
		}
		catch (RemoteException e)
		{
			System.out.println(LOGGER_ERROR);
		}
		data.setUpdateList(list);
		data.setStatus(this.UPDATED);
	}
}

