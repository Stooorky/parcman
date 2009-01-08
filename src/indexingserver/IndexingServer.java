package indexingserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.activation.*;
import java.io.*;
import java.lang.*;
import java.util.Timer;
import java.util.TimerTask;

import io.Logger;
import remoteexceptions.*;
import databaseserver.RemoteDBServer;
import parcmanserver.RemoteParcmanServer;
import parcmanagent.*;
import parcmanagent.ClientDataForAgent;
import parcmanserver.ClientData;
import parcmanagent.UpdateList;
import database.beans.ShareBean;
import parcmanclient.RemoteParcmanClientAgent;
import logserver.RemoteLogServer;

/**
 * Server di indicizzazione.
 *
 * @author Parcman Tm
 */
public class IndexingServer
	extends Activatable
	implements RemoteIndexingServer, Unreferenced
{
	/**
	 * Logger
	 */
	private Logger logger;

	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Stub del DBServer.
	 */
	private RemoteDBServer dBServer;

	/**
	 * Stub del ParcmanServer.
	 */
	private RemoteParcmanServer parcmanServer;

	/**
	 * Stub del LogServer.
	 */
	private RemoteLogServer logServer;

	/**
	 * Periodo che indica ogni quanti millisecondi il server invia una
	 * nuova squadra di agenti <tt>ParcmanAgent</tt>.
	 */
	private int agentTeamLaunchPeriod = 1000 * 10; // imposto il periodo a 1 minuto.

	/**
	 * Intero che indica con quanto ritardo (espresso in
	 * millisecondi) il server invia una nuova squadra di agenti 
	 * <tt>ParcmanAgent</tt> ad ogni periodo <tt>agentTeamLaunchPeriod</tt>
	 */
	private int agentTeamLaunchDelay = 1000 * 10; // imposto il ritardo a 1 minuto.

	/**
	 * Double che rappresenta la percentuale di <tt>agentTeamLaunchPeriod</tt> con
	 * cui si calcola il tempo di validita` di ogni
	 * <tt>ParcmanAgent</tt>.
	 */
	private double agentPeriodLaunchPercent = 0.9;


	/**
	 * Costruttore.
	 *
	 * @param dBServer Stub del DBServer
	 * @param parcmanServer Stub del server centrale
	 * @throws RemoteException Eccezione remota
	 */
	public IndexingServer(ActivationID id, MarshalledObject atDate) throws
		ActivationException,
		IOException,
		ClassNotFoundException
	{
		super(id, 38997);

		this.logger = Logger.getLogger("server-side");

		// Ricavo l'ActivationSystem
		ActivationSystem actSystem = ActivationGroup.getSystem();
		// Ricavo l'ActivationDesc dall'ActivationSystem
		ActivationDesc actDesc = actSystem.getActivationDesc(id);

		logger.info("Inizializzo l'IndexingServer.");

		// Ricavo dall'atDate i dati della sessione
		IndexingServerAtDate onAtDate = (IndexingServerAtDate)(atDate.get());
		this.parcmanServer = onAtDate.getParcmanServerStub();
		this.dBServer = onAtDate.getDBServerStub();
		this.logServer = onAtDate.getLogServerStub();
		boolean isRun = onAtDate.getIsRun();
		logger.info("Ho ripristinato e aggiornato i dati di sessione.");

		if (!isRun)
			this.run();

		// Creo un nuovo IndexingServerAtDate con i dati di sessione aggiornati
		IndexingServerAtDate newAtDate = new IndexingServerAtDate(this.parcmanServer, this.dBServer, this.logServer, true);
		logger.info("Ho creato un nuovo IndexingServerAtDate.");
		ActivationDesc newActDesc = new ActivationDesc(actDesc.getGroupID(), actDesc.getClassName(), actDesc.getLocation(), new MarshalledObject(newAtDate));
		logger.info("Ho creato un nuovo activation descriptor.");
		actDesc = actSystem.setActivationDesc(id, newActDesc);
		logger.info("Ho impostato il nuovo activation descriptor.");
	}

	/**
	 * Getter per <tt>agentTeamLaunchPeriod</tt>
	 */
	public int getAgentTeamLaunchPeriod()
	{
		return this.agentTeamLaunchPeriod;
	}

	/**
	 * Getter per <tt>agentTeamLaunchDelay</tt>
	 */
	public int getAgentTeamLaunchDelay()
	{
		return this.agentTeamLaunchDelay;
	}

	/**
	 * Getter per <tt>agentPeriodLaunchPercent</tt>
	 */
	public double getAgentPeriodLaunchPercent()
	{
		return this.agentPeriodLaunchPercent;
	}

	/**
	 * Forza la disconnessione di un utente.
	 *
	 * @param userName Nome utente
	 */
	private void forceUserToReconnect(String userName)
	{
		try
		{
			parcmanServer.forceUserToReconnect(userName);
		}
		catch (RemoteException e)
		{
			logger.error("Impossibile forzare la riconnessione dell'utente " + userName + ", il server risulta irraggiungibile", e);
		}
	}

	/**
	 * Esegue l'update della lista di file condivisi a partire dalla
	 * mappa updateLists.
	 *
	 * @param updateLists contiene i dati per l'aggiornamento delle
	 * liste di file condivisi dagli utenti
	 * @param validity Periodo di validita' del ParcmanAgent chiamante
	 * @throws IndexingServerRequestAfterTimeOutRemoteException
	 * Richiesta inviata dopo il TimeOut
	 * @throws RemoteException Eccezione Remota
	 */
	public void sendUpdateLists(Map<String, ClientDataForAgent> updateLists, long validity, int identify) throws
		IndexingServerRequestAfterTimeOutRemoteException,
		RemoteException
	{
		// Controllo che l'agente sia arrivato prima del timeOut
		if (validity < System.currentTimeMillis())
		{
			logger.info("Richiesta da parte di un ParcmanAgent arrivata oltre il TimeOut. Nessun aggiornamento eseguito");
			throw new IndexingServerRequestAfterTimeOutRemoteException(); 
		}

		String key;
		ClientDataForAgent data;

		logger.info("Nuova richiesta di aggiornamento da parte dell'agent " + identify);

		for (Iterator<String> iter = updateLists.keySet().iterator(); iter.hasNext(); )
		{
			key = iter.next();
			data = updateLists.get(key);

			if (data == null || data.getUpdateList() == null)
			{
				continue;
			}

			Vector<ShareBean> addShares = data.getUpdateList().getAddList();
			Vector<Integer> removeShares = data.getUpdateList().getRemoveList();

			logger.info("Aggiorno la lista share dell'utente " + key);
			for (int i = 0; i < addShares.size(); i++)
			{
				if (!addShares.get(i).getOwner().equals(key))
				{
					logger.info("Lista Shares non coerente con i dati utente "
							+ addShares.get(i).getName() + "@" + addShares.get(i).getOwner());
					this.forceUserToReconnect(key);
					break;
				}

				try
				{
					dBServer.addShare(addShares.get(i));
				}
				catch (ParcmanDBServerErrorRemoteException e)
				{
					logger.error("Impossibile aggiungere il file " + addShares.get(i).getName() + "@" + addShares.get(i).getOwner(), e);
					this.forceUserToReconnect(addShares.get(i).getOwner());
					break;
				}
				catch (ParcmanDBServerShareExistRemoteException e)
				{
					logger.error("File gia' presente nel database " + addShares.get(i).getName() + "@" + addShares.get(i).getOwner(), e);
					this.forceUserToReconnect(addShares.get(i).getOwner());
					break;
				}
				catch (ParcmanDBServerShareNotValidRemoteException e)
				{
					logger.error("ShareBean non valido " + addShares.get(i).getName() + "@" + addShares.get(i).getOwner(), e);
					this.forceUserToReconnect(addShares.get(i).getOwner());
					break;
				}
				catch (RemoteException e)
				{
					logger.error("Impossibile aggiungere il file " + addShares.get(i).getName() + "@" + addShares.get(i).getOwner(), e);
					this.forceUserToReconnect(addShares.get(i).getOwner());
					break;
				}
			}

			for (int i = 0; i < removeShares.size(); i++)
			{
				try
				{
					dBServer.removeShare(removeShares.get(i).intValue(), key);
				}
				catch (ParcmanDBServerErrorRemoteException e)
				{
					logger.error("Impossibile rimuovere il file " + removeShares.get(i).intValue() + "@" + key, e);
					this.forceUserToReconnect(key);
					break;
				}
				catch (ParcmanDBServerShareNotExistRemoteException e)
				{
					logger.error("File non presente nel database " + removeShares.get(i).intValue() + "@" + key, e);
					this.forceUserToReconnect(key);
					break;
				}
				catch (RemoteException e)
				{
					logger.error("Impossibile rimuovere il file " + removeShares.get(i).intValue() + "@" + key, e);
					this.forceUserToReconnect(key);
					break;
				}
			}

			try
			{
				parcmanServer.setShareListVersionOfUser(key, data.getUpdateList().getVersion());
			}
			catch (RemoteException e)
			{
				logger.error("Impossibile settare la versione della lista di file condivisi " + data.getUpdateList().getVersion() + "@" + key, e);
				this.forceUserToReconnect(key);
				continue;
			}
		}
	}


	/**
	 * Metodo ping.
	 *
	 * @throws RemoteException Eccezione remota
	 */
	public void ping() throws
		RemoteException
	{
		try
		{
			logger.info("E' stata ricevuta una richiesta di ping da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			logger.error("Errore di rete, ClientHost irraggiungibile.", e);
		}
	}

	/**
	 * Inizia l'indicizzazione dei client. L'indicizzazione e`
	 * gestita attraverso un <tt>Timer</tt>. I parametri di
	 * gestione del Timer sono:
	 * <ul>
	 * 	<li><tt>agentTeamLaunchPeriod</tt>: il server invia una
	 * 	squadra di agent ogni <tt>agentTeamLaunchPeriod</tt>
	 * 	millisecondi.</li>
	 * 	<li><tt>agentTeamLaunchDelay</tt>: il server aspetta
	 * 	<tt>agentTeamLaunchDelay</tt> millisecondi prima di
	 * 	lanciare una nuova squadra di agent.</li>
	 * 	<li><tt>agentPeriodLaunchPercent</tt>: il server imposta
	 * 	la validita` di ogni agent lanciato al
	 * 	<tt>agentPeriodLaunchPercent</tt> per cento.</li>
	 * </ul>
	 *
	 */
	private void run() throws 
		RemoteException
	{
		logger.debug("Avvio SendAgentTimerTask");
		Timer timer = new Timer();
		timer.schedule(new SendAgentTimerTask(this.parcmanServer, this, this.logServer), agentTeamLaunchDelay, agentTeamLaunchPeriod);
	}

	/**
	 * Dereferenziazione del Server.
	 * Chiamato dal sistema Rmid.
	 */
	public void unreferenced()
	{
		try
		{
			logger.info("Disattivazione dell'IndexingServer in corso");
			// Rendo inattivo l'IndexingServer
			this.inactive(getID());

			// Invoco il Garbage Collector
			System.gc();

			logger.info("Disattivazione avvenuta con successo");
		}
		catch (Exception e)
		{
			logger.error("Impossibile disattivare l'IndexingServer");
			System.out.close();
		}
	}

}


class SendAgentTimerTask 
extends TimerTask
{
	private RemoteParcmanServer parcmanServer;
	private IndexingServer is;
	private RemoteLogServer logServer;
	private Map<String, ClientData> connectedClients;
	private int identify;
	private Logger logger;

	public SendAgentTimerTask(RemoteParcmanServer parcmanServer, IndexingServer is, RemoteLogServer logServer)
	{
		this.logger = Logger.getLogger("server-side");
		this.parcmanServer = parcmanServer;
		this.is = is;
		this.logServer = logServer;
		this.identify = 0;
	}

	/**
	 * Metodo che implementa l'algoritmo per scegliere quanti gruppi
	 * di indirizzi ip generare.
	 *
	 * @param numConnectedClient Numero dei client connessi.
	 * @return restituisce un int che rappresenta il numero di
	 * ParcmanAgent da creare.
	 */
	private int ipSplitRate(int numConnectedClient)
	{
		// so stupid
		return 10;
	}

	public void run() 
	{
		// controllo lo stato del sistema. se false non faccio partire gli agent.
		try
		{
			if (!this.parcmanServer.getAgentSystemStatus((RemoteIndexingServer) is))
				return;
		}
		catch (RemoteException e)
		{
			logger.error("Impossibile verificare lo stato del sistema di gestione degli agenti remoti.", e);
			return;
		}

		try
		{
			connectedClients = new HashMap<String, ClientData>(this.parcmanServer.getConnectedUsers(is));
		}
		catch (RemoteException e)
		{
			logger.error("Impossibile ottenere la lista degli utenti connessi dal ParcmanServer");
			return;
		}

		logger.info("Invio degli agenti in corso");

		int x=0;
		Vector<ClientDataForAgent> clients = new Vector<ClientDataForAgent>();
		Iterator iter = connectedClients.keySet().iterator();

		while (iter.hasNext())
		{
			String name = (String) iter.next();
			clients.add(new ClientDataForAgent(connectedClients.get(name).getName(),
						(RemoteParcmanClientAgent) connectedClients.get(name).getStub(),
						connectedClients.get(name).getVersion(), 6));

			if (x>=this.ipSplitRate((int)this.connectedClients.size()) || !iter.hasNext())
			{
				long validity = (long) (System.currentTimeMillis() + this.is.getAgentTeamLaunchPeriod() * this.is.getAgentPeriodLaunchPercent());
				RemoteParcmanAgent rpa;
				try 
				{
					rpa = new ParcmanAgent(this.is, validity, clients, this.identify, this.logServer);
					logger.debug("Inviato un nuovo agente Identify:" + this.identify);
					UnicastRemoteObject.unexportObject(rpa, true);
					rpa.start();
					this.identify++;
				} 
				catch (RemoteException e)
				{
					logger.error("Non e` possibile creare il ParcmanAgent.", e);
				}

				clients = new Vector<ClientDataForAgent>();
				x=0;
			}
			x++;
		}
		/*
		   while (this.numberOfAgent != 0)
		   {
		   long validity = (long) (System.currentTimeMillis() + this.is.getAgentTeamLaunchPeriod() * this.is.getAgentPeriodLaunchPercent());
		   Vector<ClientData> _clients = new Vector<ClientData>(
		   Arrays.asList(
		   (List<ClientData>) (Arrays.copyOfRange(
		   this.connectedClients.values().toArray(), 
		   last, 
		   interval)
		   )
		   )
		   );

		   Vector<ClientDataForAgent> clients = new Vector<ClientDataForAgent>();
		   for (int i=0; i < _clients.size(); i++)
		   clients.add(new ClientDataForAgent(_clients.get(i).getName(), (RemoteParcmanClientAgent) _clients.get(i).getStub(), _clients.get(i).getVersion())); 

		   ParcmanAgent rpa = null;
		   try 
		   {
		   logger.debug("Inviato un nuovo agente");
		   rpa = new ParcmanAgent(this.is, validity, clients);
		   } 
		   catch (RemoteException e)
		   {
		   logger.error("Non e` possibile creare il ParcmanAgent.", e);
		   continue;
		   }

		// RemoteParcmanAgentClient rpac = ... lookup ...
		// rpac.go();

		this.numberOfAgent--;
		}
		*/
	}
}

