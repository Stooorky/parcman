package indexingserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del ParcmanServer.
 *
 * @author Parcman Tm
 */
public interface RemoteIndexingServer 
extends Remote
{
	/**
	 * Ping.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void ping() throws 
		RemoteException;
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
	public void run() throws 
		RemoteException;
}
