package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;

import plog.*;
import remoteexceptions.*;
import database.beans.ShareBean;
import database.beans.SearchBean;
import databaseserver.RemoteDBServer;
import indexingserver.RemoteIndexingServer;
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
	* Esegue l'aggiunta di un nuovo client alla lista dei tentativi di connessione.
	*
	* @param username nome utente
	* @param host host del client
	* @throws ParcmanServerUserIsConnectRemoteException Utente gia' connesso
	* @throws RemoteException eccezione remota
	*/
	public void connectAttemp(String username, String host) throws
		ParcmanServerUserIsConnectRemoteException,
		RemoteException
	{
		if (this.attempUsers.containsKey(username))
		{
			PLog.debug("ParcmanServer.connectAttemp", "l'utente " + username + " e' gia' nella lista di attemp.");
			throw new ParcmanServerUserIsConnectRemoteException("Esiste gia' un tentativo di connessione con questo nome utente");
		}

		if (this.connectUsers.containsKey(username))
		{
			RemoteParcmanClient parcmanClient = (RemoteParcmanClient) this.connectUsers.get(username).getStub();

			boolean pingOk = true;
			try
			{
				parcmanClient.ping();
				pingOk=true;

			}
			catch (Exception e)
			{
				pingOk = false;
				PLog.debug("ParcmanServer.connectAttemp", "l'utente " + username + " non e' piu' raggiungibile... disconnessione effettuata.");
				this.connectUsers.remove(username);
			}
	
			if (pingOk)
			{
				PLog.debug("ParcmanServer.connectAttemp", "l'utente " + username + " e' gia' connesso alla rete.");
				throw new ParcmanServerUserIsConnectRemoteException("Utente gia' connesso alla rete");
			}
		}

		// aggiungo l'host in attemp
		attempUsers.put(username, host);
		PLog.debug("ParcmanServer.connectAttemp", "Aggiunto l'utente " + username + " (" + host + ") nella lista di attemp.");
	}


	/**
	* Esegue la connessione di un nuovo RemoteParcmanClient alla rete Parcman.
	*
	* @param parcmanClientStub Stub del MobileServer
	* @param userName Nome utente proprietario della sessione
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
					PLog.debug("ParcmanServer.connect", "Tentativo di connessione non autorizzata dall'host " + this.getClientHost());
					throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo ost.");
				}

				PLog.debug("ParcmanServer.connect", "Rimosso " + userName + " (" + host + ") dalla lista di attemp.");

				// Aggiungo l'utente alla lista connectUsers
				ClientData user = new ClientData(host, parcmanClientStub);
				connectUsers.put(userName, user);
				PLog.debug("ParcmanServer.connect", "Aggiunto " + userName + " (" + host + ") alla lista dei client connessi.");
			}
			else
			{
				PLog.debug("ParcmanServer.connect", "Tentativo di connessione non autorizzata dall'host " + this.getClientHost());
				throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
			}
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "ParcmanServer.connect", "Errore di rete, ClientHost irraggiungibile.");
			throw new RemoteException();
		}
	}

	/**
	* Esegue la disconnessione di RemoteParcmanClient dalla rete Parcman.
	*
	* @param parcmanClientStub Stub del MobileServer
	* @param userName Nome utente proprietario della sessione
	* @throws RemoteException Eccezione Remota
	*/
	public void disconnect(RemoteParcmanClient parcmanClientStub, String userName) throws
		RemoteException
	{
		try
		{
			PLog.debug("ParcmanServer.disconnect", "Nuova richiesta di disconnessione.");
			// Controllo che l'utente sia connesso
			if (connectUsers.containsKey(userName))
			{
				ClientData user = connectUsers.get(userName);

				if (!this.getClientHost().equals(user.getHost()) || !user.getStub().equals(parcmanClientStub))
				{
					PLog.debug("ParcmanServer.disconnect", "Richiesta di disconnessione non valida dall'host " + this.getClientHost());
					throw new ParcmanServerHackWarningRemoteException
							("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
				}

				//Rimuovo l'utente dalla lista dei Client Connessi
				user.setStub(null);
				connectUsers.remove(userName);
				

				PLog.debug("ParcmanServer.disconnect", "Rimosso " + userName + " (" + this.getClientHost() + ") dalla lista dei Client Connessi.");
			}
			else
			{
				PLog.debug("ParcmanServer.disconnect", "Richiesta di disconnessione non valida dall'host " + this.getClientHost());
				throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
			}
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "ParcmanServer.disconnet", "Errore di rete, ClientHost irraggiungibile.");
			throw new RemoteException();
		}
	}

	/**
	* Restituisce la lista file condivisi dell'utente.
	* E' necessario possedere lo stub dell'utente per poter fare questa richiesta.
	*
	* @param parcmanClientStub Stub del MobileServer
	* @param userName Nome utente proprietario della sessione
	* @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	* @throws RemoteException Eccezione Remota
	*/
	public Vector<ShareBean> getSharings(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerRequestErrorRemoteException,
		RemoteException
	{
		try
		{
			PLog.debug("ParcmanServer.getSharings", "Rishiesta lista dei file condivisi dell'utente " + userName + ".");
			// Controllo che l'utente sia connesso
			if (connectUsers.containsKey(userName))
			{
				ClientData user = connectUsers.get(userName);

				if (!this.getClientHost().equals(user.getHost()) || !user.getStub().equals(parcmanClientStub))
				{
					PLog.debug("ParcmanServer.getSharings", "Richiesta non valida, host " + this.getClientHost());
					throw new ParcmanServerHackWarningRemoteException
							("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
				}

				try
				{
					Vector<ShareBean> shares = dBServer.getSharings(userName);
					PLog.debug("ParcmanServer.getSharings", "Spedita la lista file condivisi (" + shares.size() + " file)");
					return shares;
				}
				catch (ParcmanDBServerErrorRemoteException e)
				{
					PLog.debug("ParcmanServer.getSharings", "Richiesta non esaudita, errore interno del database.");
					throw new ParcmanServerRequestErrorRemoteException();
				}
				catch (ParcmanDBServerUserNotExistRemoteException e)
				{
					PLog.debug("ParcmanServer.getSharings", "Richiesta non esaudita, Nome utente non presente nel database.");
					throw new ParcmanServerRequestErrorRemoteException();
				}
				catch (RemoteException e)
				{
					PLog.debug("ParcmanServer.getSharings", "Richiesta non esaudita, errore interno del database.");
					throw new ParcmanServerRequestErrorRemoteException();
				}
			}
			else
			{
				PLog.debug("ParcmanServer.getSharings", "Richiesta di disconnessione non valida dall'host " + this.getClientHost());
				throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
			}
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "ParcmanServer.getSharings", "Errore di rete, ClientHost irraggiungibile.");
			throw new RemoteException();
		}
	}

	/**
	* Restituisce il numero di versione dei file condivisi dell'utente.
	* E' necessario possedere lo stub dell'utente per poter fare questa richiesta.
	*
	* @param parcmanClientStub Stub del MobileServer
	* @param userName Nome utente proprietario della sessione
	* @return intero che rappresenta il numero di versione della
	* lista dei file condivisi.
	* @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	* @throws RemoteException Eccezione Remota
	*/
	public int getSharingsVersion(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerRequestErrorRemoteException,
		RemoteException
	{
		try
		{
			PLog.debug("ParcmanServer.getSharings", "Rishiesta codice di versione della lista dei file condivisi dell'utente " + userName + ".");
			// Controllo che l'utente sia connesso
			if (connectUsers.containsKey(userName))
			{
				ClientData user = connectUsers.get(userName);

				if (!this.getClientHost().equals(user.getHost()) || !user.getStub().equals(parcmanClientStub))
				{
					PLog.debug("ParcmanServer.getSharings", "Richiesta non valida, host " + this.getClientHost());
					throw new ParcmanServerHackWarningRemoteException
							("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
				}

			    return user.getVersion();
			}
			else
			{
				PLog.debug("ParcmanServer.getSharings", "Richiesta di disconnessione non valida dall'host " + this.getClientHost());
				throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
			}
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "ParcmanServer.getSharings", "Errore di rete, ClientHost irraggiungibile.");
			throw new RemoteException();
		}
	}

	/**
	* Restituisce la lista degli utenti connessi al sistema
	* E' necessario possedere lo stub del server di indicizzazione per poter fare questa richiesta.
	*
	* @param RemoteIndexingServer Stub del server di indicizzazione
	*/
	public Map<String, ClientData> getConnectedUsers(RemoteIndexingServer ris)
		throws RemoteException
	{
		return this.connectUsers;
	}

	/**
	* Restituisce il risultato di una ricerca sul database.
	* E' necessario possedere lo stub dell'utente per poter fare questa richiesta.
	*
	* @param parcmanClientStub Stub del MobileServer
	* @param userName Nome utente proprietario della sessione
	* @param keywords Lista di Keyword per la ricerca
	* @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	* @throws RemoteException Eccezione Remota
	*/
	public Vector<SearchBean> search(RemoteParcmanClient parcmanClientStub, String userName, String keywords) throws
		ParcmanServerRequestErrorRemoteException,
		RemoteException
	{
		try
		{
			PLog.debug("ParcmanServer.search", "Richiesta nuova ricerca " + userName + "@" + keywords);
			// Controllo che l'utente sia connesso
			if (connectUsers.containsKey(userName))
			{
				ClientData user = connectUsers.get(userName);

				if (!this.getClientHost().equals(user.getHost()) || !user.getStub().equals(parcmanClientStub))
				{
					PLog.debug("ParcmanServer.search", "Richiesta non valida, host " + this.getClientHost());
					throw new ParcmanServerHackWarningRemoteException
							("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
				}

				try
				{
					Vector<SearchBean> searchList = dBServer.searchFiles(keywords);
					PLog.debug("ParcmanServer.search", "Ricerca effettuata (" + searchList.size() + " file trovati)");
					return searchList;
				}
				catch (ParcmanDBServerErrorRemoteException e)
				{
					PLog.debug("ParcmanServer.search", "Richiesta non esaudita, errore interno del database.");
					throw new ParcmanServerRequestErrorRemoteException();
				}
				catch (RemoteException e)
				{
					PLog.debug("ParcmanServer.search", "Richiesta non esaudita, errore interno del database.");
					throw new ParcmanServerRequestErrorRemoteException();
				}
			}
			else
			{
				PLog.debug("ParcmanServer.search", "Richiesta di disconnessione non valida dall'host " + this.getClientHost());
				throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
			}
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "ParcmanServer.search", "Errore di rete, ClientHost irraggiungibile.");
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
