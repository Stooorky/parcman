package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;

import plog.*;
import remoteexceptions.*;
import databaseserver.RemoteDBServer;
import parcmanclient.RemoteParcmanClient;

/**
 * Server centrale per la gestione degli utenti.
 *
 * @author Parcman Tm
 */
public class ParcmanServer
	extends UnicastRemoteObject
	implements RemoteParcmanServer
{
    /**
     * Mappa degli utenti connessi.
     * Nome utente, Dati utente 
     */
    private Map<String, ClientData> connectUsers;

    /**
     * Mappa degli utenti attemp.
     * Nome utente, host
     */
    private Map<String, String> attempUsers;

    /**
     * Stub del DBServer.
     */
	private RemoteDBServer dBServer;

    /**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param dbServer Stub del DBServer
	 * @throws RemoteException Eccezione remota
	 */
	public ParcmanServer(RemoteDBServer dBServer) throws
		RemoteException
	{
		this.dBServer = dBServer;
        this.connectUsers = new HashMap<String, ClientData>();
        this.attempUsers = new HashMap<String, String>();
	}

    /**
     * esegue l'aggiunta di un nuovo client alla lista dei tentativi di connessione.
     *
     * @param username nome utente
     * @param host host del client
     * @throws remoteexception eccezione remota
     */
    public void connectAttemp(String username, String host) throws
        RemoteException
    {
        if (attempUsers.containsValue(username))
        {
            PLog.debug("ParcmanServer.connectAttemp", "l'utente " + username + " e' gia' nella lista di attemp.");
            throw new RemoteException();
        }

        // aggiungo l'host in attemp
        attempUsers.put(username, host);
        PLog.debug("ParcmanServer.connectAttemp", "Aggiunto l'utente " + username + " (" + host + ") nella lista di attemp.");
    }

    /**
     * Esegue la connessione di un nuovo RemoteParcmanClient alla rete Parcman.
     *
     * @param parcmanClientStub Stub del MobileServer
     * @throws RemoteException Eccezione Remota
     */
    public void connect(RemoteParcmanClient parcmanClientStub, String userName) throws
        RemoteException
    {
        try
        {
            PLog.debug("ParcmanServer.connect", "Nuovo tentativo di connessione alla rete parcman.");
            // Controllo che l'host sia nella lista di attemp
            if (attempUsers.containsKey(userName))
            {
                // Rimuovo l'utente dalla lista di attemp
                String host = attempUsers.remove(userName);
                if (!this.getClientHost().equals(host))
                {
                    PLog.debug("ParcmanServer.connet", "Tentativo di connessione non autorizzata dall'host " + this.getClientHost());
                    throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
                }

                PLog.debug("ParcmanServer.connect", "Rimosso " + userName + " (" + host + ") dalla lista di attemp.");

                // Aggiungo l'utente alla lista connectUsers
                ClientData user = new ClientData(host, parcmanClientStub);
                connectUsers.put(userName, user);
                PLog.debug("ParcmanServer.connect", "Aggiunto " + userName + " (" + host + ") alla lista dei client connessi.");
            }
            else
            {
                PLog.debug("ParcmanServer.connet", "Tentativo di connessione non autorizzata dall'host " + this.getClientHost());
                throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
            }
        }
 		catch(ServerNotActiveException e)
		{
			PLog.err(e, "ParcmanServer.connet", "Errore di rete, ClientHost irraggiungibile.");
            throw new RemoteException();
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
			PLog.debug("ParcmanServer.ping", "E' stata ricevuta una richiesta di ping da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "ParcmanServer.ping", "Errore di rete, ClientHost irraggiungibile.");
		}
	}
}
