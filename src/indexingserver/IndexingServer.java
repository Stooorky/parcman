package indexingserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;
import java.util.Timer;
import java.util.TimerTask;

import plog.*;
import remoteexceptions.*;
import databaseserver.RemoteDBServer;
import parcmanserver.RemoteParcmanServer;
import parcmanagent.ParcmanAgent;
import parcmanserver.ClientData;

/**
 * Server di indicizzazione.
 *
 * @author Parcman Tm
 */
public class IndexingServer
	extends UnicastRemoteObject
	implements RemoteIndexingServer
{
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
	 * Periodo che indica ogni quanti millisecondi il server invia una
	 * nuova squadra di agenti <tt>ParcmanAgent</tt>.
	 */
	private int agentTeamLaunchPeriod = 1000 * 60; // imposto il periodo a 1 minuto.

	/**
	 * Intero che indica con quanto ritardo (espresso in
	 * millisecondi) il server invia una nuova squadra di agenti 
	 * <tt>ParcmanAgent</tt> ad ogni periodo <tt>agentTeamLaunchPeriod</tt>
	 */
	private int agentTeamLaunchDelay = 1000 * 60; // imposto il ritardo a 1 minuto.

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
	public IndexingServer(RemoteDBServer dBServer, RemoteParcmanServer parcmanServer) throws
		RemoteException
	{
		this.dBServer = dBServer;
		this.parcmanServer = parcmanServer;
		this.run();
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
	* Metodo ping.
	*
	* @throws RemoteException Eccezione remota
	*/
	public void ping() throws
		RemoteException
	{
		try
		{
			PLog.debug("IndexingServer.ping", "E' stata ricevuta una richiesta di ping da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "IndexingServer.ping", "Errore di rete, ClientHost irraggiungibile.");
		}
	}

	/**
	 * Metodo che implementa l'algoritmo per scegliere quanti gruppi
	 * di indirizzi ip generare.
	 *
	 * @param numConnectedClient Numero dei client connessi.
	 * @return restituisce un int che rappresenta il numero di
	 * ParcmanAgent da creare.
	 */
	protected int ipSplitRate(int numConnectedClient)
	{
		// so stupid
		return numConnectedClient / 10;
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
		Map<String, ClientData> connectedClients = new HashMap<String, ClientData>(this.parcmanServer.getConnectedUsers(this));
		int numberOfAgent = ipSplitRate(connectedClients.size());

		Timer timer = new Timer();
		timer.schedule(new SendAgentTimerTask(this, numberOfAgent, connectedClients), agentTeamLaunchDelay, agentTeamLaunchPeriod);
	}
}


class SendAgentTimerTask 
extends TimerTask
{
	private IndexingServer is;
	private int numberOfAgent;
	private Map<String, ClientData> connectedClients;

	public SendAgentTimerTask(IndexingServer is, int numberOfAgenti, Map<String, ClientData> connectedClients)
	{
		this.is = is;
		this.numberOfAgent = numberOfAgent;	
		this.connectedClients = connectedClients;
	}

	public void run() 
	{
		int interval = this.numberOfAgent;
		int last = 0;
		while (this.numberOfAgent != 0)
		{
			long validity = (long) (System.currentTimeMillis() + this.is.getAgentTeamLaunchPeriod() * this.is.getAgentPeriodLaunchPercent());
			Vector<ClientData> clients = new Vector<ClientData>(
				Arrays.asList(
					(ClientData[]) (Arrays.copyOfRange(
							this.connectedClients.values().toArray(), 
							last, 
							interval)
						)
					)
				);

			ParcmanAgent rpa = null;
			try 
			{
				rpa = new ParcmanAgent(this.is, validity, clients); 
			} 
			catch (RemoteException e)
			{
				PLog.err(e, "SendAgentTimerTask.run", "Non e` possibile creare il ParcmanAgent.");
				continue;
			}
			try
			{
				rpa.unexportObject(rpa, true);
			}
			catch (NoSuchObjectException e)
			{
				PLog.err(e, "SendAgentTimerTask.run", "Non e` possibile de-esportare il ParcmanAgent.");
			}

			// RemoteParcmanAgentClient rpac = ... lookup ...
			// rpac.go();

			this.numberOfAgent--;
		}
	}
}
