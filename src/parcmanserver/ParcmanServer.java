package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.util.Iterator;
import java.util.Vector;
import java.rmi.*;
import java.io.EOFException;

import io.Logger;
import io.PropertyManager;
import remoteexceptions.*;
import database.beans.ShareBean;
import database.beans.SearchBean;
import database.beans.UserBean;
import database.exceptions.ParcmanDBUserInvalidStatusException;
import databaseserver.RemoteDBServer;
import indexingserver.RemoteIndexingServer;
import parcmanclient.RemoteParcmanClient;
import parcmanclient.RemoteParcmanClientUser;
import parcmanclient.DownloadData;
import privilege.*;

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
	 * Logger
	 */
	private Logger logger;

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
	private RemoteDBServer dbServer;

	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Boolean che indica se il sistema di gestione e invio degli agenti remoti e` 
	 * attivo (true) oppure no (false).
	 */
	private boolean agentSystemStatus = true;

	/**
	 * Costruttore.
	 *
	 * @param dbServer Stub del DBServer
	 * @throws RemoteException Eccezione remota
	 */
	public ParcmanServer(RemoteDBServer dbServer) throws
		RemoteException
	{
		this.dbServer = dbServer;
		this.connectUsers = new HashMap<String, ClientData>();
		this.attempUsers = new HashMap<String, String>();
		this.logger = Logger.getLogger("server-side", PropertyManager.getInstance().get("logger"));

		logger.debug("Avvio ControlUsersTimerTask");
		Timer timer = new Timer();
		timer.schedule(new ControlUsersTimerTask(this), 10000, 30000);
	}

	/**
	 * Setta la versione dei file condivisi di un utente.
	 *
	 * @param username Nome utente
	 * @param version Versione
	 * @throws RemoteException Eccezione remota
	 */
	public void setShareListVersionOfUser(String username, int version) throws
		RemoteException
	{
		if (this.connectUsers.containsKey(username))
		{
			this.connectUsers.get(username).setVersion(version);
			logger.debug("Modificata la versione dei file condivisi dell'utente " + username + " (Versione: " + version + ")");
			return;
		}

		logger.error("Impossibile modificare la versione dei file condivisi dell'utente " + username + " (Versione: " + version + ")");
	}

	/**
	 * Imposta lo stato del sistema di gestione degli agenti remoti.
	 * Lo stato puo` essere attivo (true) oppure non attivo (false).
	 * 
	 * @param parcmanClientStub Stub del client.
	 * @param userName Stringa che rappresenta lo username del client.
	 * @param boolean un boolean che rappresenta lo stato. attivo = true, non attivo = false.
	 * @throws RemoteException 
	 */
	public void setAgentSystemStatus(RemoteParcmanClient parcmanClientStub, String userName, boolean status) throws 
		ParcmanServerHackWarningRemoteException,
		RemoteException 
	{
		checkHacking(parcmanClientStub, userName);
		checkAdminPrivileges(connectUsers.get(userName));
		this.agentSystemStatus = status;
	}

	/**
	 * Ritorna lo stato del sistema di gestione degli agenti remoti. 
	 * Lo stato puo` essere attivo (true) oppure non attivo (false).
	 * 
	 * @param parcmanClientStub Stub del client.
	 * @param userName Stringa che rappresenta lo username del client.
	 * @return un boolean che rappresenta lo stato. attivo = true, non attivo = false.
	 * @throws RemoteException 
	 */
	public boolean getAgentSystemStatus(RemoteParcmanClient parcmanClientStub, String userName) throws 
		RemoteException
	{
		checkHacking(parcmanClientStub, userName);
		checkAdminPrivileges(connectUsers.get(userName));
		return this.agentSystemStatus;
	}
	
	/**
	 * Ritorna lo stato del sistema di gestione degli agenti remoti. 
	 * Lo stato puo` essere attivo (true) oppure non attivo (false).
	 * 
	 * @param ris Stub dell'<tt>IndexingServer</tt>.
	 * @return un boolean che rappresenta lo stato. attivo = true, non attivo = false.
	 * @throws RemoteException 
	 */
	public boolean getAgentSystemStatus(RemoteIndexingServer ris) throws 
		RemoteException
	{
		return this.agentSystemStatus;
	}

	/**
	 * Forza la riconnessione di un utente.
	 *
	 * @param userName Nome utente
	 * @throws RemoteException Eccezione remota
	 */
	public void forceUserToReconnect(String userName) throws
		RemoteException
	{
		if (connectUsers.containsKey(userName))
		{
			try
			{
				ClientData user = connectUsers.get(userName);

				RemoteParcmanClient client = user.getStub();
				String host = user.getHost();
				connectUsers.remove(userName);
				this.connectAttemp(userName, host);
				logger.debug("Riconnessione forzata di " + userName);
				client.reconnect();
			}
			catch (RemoteException e)
			{
				logger.error("Impossibile forzare la riconnessione di " + userName, e);
				return;
			}
		}
		else
		{
			logger.error("Impossibile forzare la riconnessione di " + userName + ", utente non connesso");
			return;
		}

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
			logger.debug("l'utente " + username + " e' gia' nella lista di attemp.");
			throw new ParcmanServerUserIsConnectRemoteException("Esiste gia' un tentativo di connessione con questo nome utente");
		}

		if (this.connectUsers.containsKey(username))
		{
			RemoteParcmanClient parcmanClient = this.connectUsers.get(username).getStub();

			boolean pingOk = true;
			try
			{
				parcmanClient.ping();
				pingOk=true;

			}
			catch (Exception e)
			{
				pingOk = false;
				logger.error("l'utente " + username + " non e' piu' raggiungibile... disconnessione effettuata.");
				this.connectUsers.remove(username);
			}

			if (pingOk)
			{
				logger.error("l'utente " + username + " e' gia' connesso alla rete.");
				throw new ParcmanServerUserIsConnectRemoteException("Utente gia' connesso alla rete");
			}
		}

		// aggiungo l'host in attemp
		attempUsers.put(username, host);
		logger.info("Aggiunto l'utente " + username + " (" + host + ") nella lista di attemp.");
	}



	/**
	 * Esegue la connessione di un nuovo RemoteParcmanClient alla rete Parcman.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @throws RemoteException Eccezione Remota
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 */
	public void connect(RemoteParcmanClient parcmanClientStub, String userName) throws
	 	ParcmanServerHackWarningRemoteException,
		RemoteException
	{
		try
		{
			logger.info("Nuovo tentativo di connessione alla rete parcman.");

			// Controllo che l'host sia nella lista di attemp
			if (attempUsers.containsKey(userName))
			{
				// Rimuovo l'utente dalla lista di attemp
				String host = attempUsers.remove(userName);
				if (!this.getClientHost().equals(host))
				{
					logger.debug("Tentativo di connessione non autorizzata dall'host " + this.getClientHost());
					throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo ost.");
				}

				logger.debug("Rimosso " + userName + " (" + host + ") dalla lista di attemp.");

				UserBean userBean;

				try
				{
					userBean = this.dbServer.getUser(userName);
				}
				catch(ParcmanDBServerUserNotExistRemoteException e)
				{
					logger.error("Richiesta rifiutata, nome utente errato.");
					throw new ParcmanServerRequestErrorRemoteException("Errore interno del database");
				}

				// Aggiungo l'utente alla lista connectUsers
				ClientData user = new ClientData(host, userName, parcmanClientStub, userBean.getPrivilege().equals(Privilege.getAdminPrivilege()));
				user.setVersion(0);
				connectUsers.put(userName, user);
				logger.debug("Aggiunto " + userName + " (" + host + ") alla lista dei client connessi.");
			}
			else
			{
				logger.error("Tentativo di connessione non autorizzata dall'host " + this.getClientHost());
				throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
			}
		}
		catch(ServerNotActiveException e)
		{
			logger.error("Errore di rete, ClientHost irraggiungibile.", e);
			throw new RemoteException();
		}
	}

	/**
	 * Esegue la rimozione di un utente dalla lista degli utenti di attemp.
	 *
	 * @param username Nome dell'utente da rimuovere.
	 */
	private void removeUserFromAttempList(String username) 
	{
		logger.debug("Disconnessione di '" + username + "' in corso... ");
		this.attempUsers.remove(username);
		logger.debug("Done.");
	}

	/**
	 * Esegue la rimozione di un utente dalla lista degli utenti connessi.
	 *
	 * @param username Nome dell'utente connesso da rimuovere.
	 */
	private void removeUserFromConnectList(String username) 
	{
		logger.debug("Disconnessione di '" + username + "' in corso... ");
		this.connectUsers.get(username).setStub(null);
		this.connectUsers.remove(username);
		logger.debug("Done.");
	}

	/**
	 * Esegue la disconnessione di RemoteParcmanClient dalla rete Parcman.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @throws RemoteException Eccezione Remota
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 */
	public void disconnect(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerHackWarningRemoteException,
		RemoteException
	{
		logger.info("Nuova richiesta di disconnessione.");

		checkHacking(parcmanClientStub, userName);

		//Rimuovo l'utente dalla lista dei Client Connessi
		this.removeUserFromConnectList(userName);

		try
		{
			logger.debug("Rimosso " + userName + " (" + this.getClientHost() + ") dalla lista dei Client Connessi.");
		}
		catch(ServerNotActiveException e)
		{
			logger.error("Errore di rete, ClientHost irraggiungibile.", e);
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
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws RemoteException Eccezione Remota
	 */
	public Vector<ShareBean> getSharings(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerRequestErrorRemoteException,
		ParcmanServerHackWarningRemoteException,
		RemoteException
	{
		logger.info("Richiesta la lista dei file condivisi dell'utente " + userName + ".");

		checkHacking(parcmanClientStub, userName);
		try
		{
			Vector<ShareBean> shares = dbServer.getSharings(userName);
			logger.debug("Spedita la lista file condivisi (" + shares.size() + " file)");
			return shares;
		}
		catch (ParcmanDBServerErrorRemoteException e)
		{
			logger.error("Richiesta non esaudita, errore interno del database.");
			throw new ParcmanServerRequestErrorRemoteException();
		}
		catch (ParcmanDBServerUserNotExistRemoteException e)
		{
			logger.error("Richiesta non esaudita, Nome utente non presente nel database.");
			throw new ParcmanServerRequestErrorRemoteException();
		}
		catch (RemoteException e)
		{
			logger.error("Richiesta non esaudita, errore interno del database.");
			throw new ParcmanServerRequestErrorRemoteException();
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
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws RemoteException Eccezione Remota
	 */
	public int getSharingsVersion(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerRequestErrorRemoteException,
		ParcmanServerHackWarningRemoteException,
		RemoteException
	{
		logger.info("Richiesto codice di versione della lista dei file condivisi dell'utente " + userName + ".");
		checkHacking(parcmanClientStub, userName);
		return connectUsers.get(userName).getVersion();
	}

	/**
	 * Restituisce la lista degli utenti connessi al sistema
	 * E' necessario possedere lo stub del server di indicizzazione per poter fare questa richiesta.
	 *
	 * @param ris Stub del server di indicizzazione
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
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws RemoteException Eccezione Remota
	 */
	public Vector<SearchBean> search(RemoteParcmanClient parcmanClientStub, String userName, String keywords) throws
		ParcmanServerRequestErrorRemoteException,
		ParcmanServerHackWarningRemoteException,
		RemoteException
	{
		logger.info("Richiesta nuova ricerca " + userName + "@" + keywords);
		checkHacking(parcmanClientStub, userName);

		try
		{
			Vector<SearchBean> searchList = dbServer.searchFiles(keywords);
			logger.debug("Ricerca effettuata (" + searchList.size() + " file trovati)");
			return searchList;
		}
		catch (ParcmanDBServerErrorRemoteException e)
		{
			logger.error("Richiesta non esaudita, errore interno del database.");
			throw new ParcmanServerRequestErrorRemoteException();
		}
		catch (RemoteException e)
		{
			logger.error("Richiesta non esaudita, errore interno del database.");
			throw new ParcmanServerRequestErrorRemoteException();
		}
	}

	/**
	 * Controlla lo stato degli utenti connessi disconnettendo tutti
	 * quelli che risultano irraggiungibili.
	 */
	public void checkUsers()
	{
		Iterator<String> iter = this.connectUsers.keySet().iterator();

		while (iter.hasNext())
		{
			String name = "patata";

			try
			{
				name = iter.next();
				this.connectUsers.get(name).getStub().ping();
			}
			catch (Exception e)
			{
				logger.error("l'utente " + name + " non e' piu' raggiungibile... disconnessione effettuata.");
				iter.remove();
			}
		}

	}

	/**
	 * Restituisce la lista degli utenti connessi.
	 *
	 * @param parcmanClientStub Stub del MobileServer.
	 * @param userName Nome utente proprietario della sessione.
	 * @return Vettore contenente la lista dei nomi utente.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws RemoteException Eccezione remota.
	 */
	public Vector<ClientDataUser> getConnectUsersList(RemoteParcmanClient parcmanClientStub, String userName)  throws
		ParcmanServerWrongPrivilegesRemoteException,
		ParcmanServerHackWarningRemoteException,
		RemoteException
	{
		logger.info("Richiesta la lista degli utenti connessi da parte di " + userName);

		checkHacking(parcmanClientStub, userName);
		checkAdminPrivileges(connectUsers.get(userName));

		Vector<ClientDataUser> data = new Vector<ClientDataUser>();
		try 
		{
			Vector<ClientData> tmp = new Vector<ClientData>(this.connectUsers.values());
			Iterator<ClientData> i = tmp.iterator();
			while (i.hasNext())
			{
				ClientData cd = i.next();
			 	data.addElement(new ClientDataUser(cd.getHost(), cd.getName(), cd.getVersion(), cd.isAdmin()));
			}
			logger.debug("Estratti " + data.size() + " utenti.");
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Check if the client is an admin client.
	 *
	 * @param user Oggetto <tt>ClientData</tt> che contiene le informazioni sul client.
	 * @throws ParcmanServerWrongPrivilegesRemoteException se il client non possiede i privilegi di admin.
	 */
	private void checkAdminPrivileges(ClientData user) throws 
		ParcmanServerWrongPrivilegesRemoteException
	{
		if (!user.isAdmin())
		{
			logger.debug("Impossibile esaudire la richiesta, privilegi non sufficienti");
			throw new ParcmanServerWrongPrivilegesRemoteException();
		}
	}

	/**
	 * Check hacking attack!
	 * E` necessario possedere lo stub dell'utente per poter fare questa richiesta.
	 *
	 * @param stub Stub del ParcmanClient.
	 * @param userName Il nome del client.
	 * @throws ParcmanServerHackWarningRemoteException se si sta verificando un probabile attacco.
	 * @throws RemoteException eccezione remota.
	 */
	private void checkHacking(RemoteParcmanClient stub, String userName) throws 
		ParcmanServerHackWarningRemoteException,
		RemoteException
	{
		try
		{
			if (this.connectUsers.containsKey(userName))
			{
				ClientData user = this.connectUsers.get(userName);

				if (!this.getClientHost().equals(user.getHost()) || !user.getStub().equals(stub))
				{
					logger.error("Richiesta non valida, host " + this.getClientHost());
					throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
				}
			}
			else
			{
				logger.error("Richiesta di disconnessione non valida dall'host " + this.getClientHost());
				throw new ParcmanServerHackWarningRemoteException("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
			}
		}
		catch(ServerNotActiveException e)
		{
			logger.error("Errore di rete, ClientHost irraggiungibile.", e);
			throw new RemoteException();
		}
	}

	/**
	 * Inizializza il download di un file.
	 * E` necessario possedere lo stub dell'utente per poter fare questa richiesta.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @param data array di 2 elementi. Il primo e` il proprietario del file, il secondo e` l'ID del file.	
	 * @return oggetto <tt>DownloadData</tt> che contiene informazioni sul file data scaricare e il proprietario del file.
	 * @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	 * @throws RemoteException Eccezione Remota
	 */
	public DownloadData startDownload(RemoteParcmanClient parcmanClientStub, String userName, String[] fileData) throws 
		ParcmanServerRequestErrorRemoteException,
		ParcmanServerHackWarningRemoteException,
		RemoteException
	{
		logger.info("Richiesta di download da parte di " + userName + ".");
		checkHacking(parcmanClientStub, userName);

		String owner = null;
		String id = null;
		try
		{
			owner = fileData[0];
			id = fileData[1];
		}
		catch (NullPointerException e)
		{
			throw new ParcmanServerRequestErrorRemoteException("I dati necessari per rintracciare il file non sono completi.");
		}

		logger.debug("Dati download: owner='" + owner + "', id='" + id + "'.");

		if (this.connectUsers.containsKey(owner))
		{
			RemoteParcmanClientUser rclient = this.connectUsers.get(owner).getStub();
			logger.debug("RemoteParcmanClientUser ottenuto");
			ShareBean bean = null;
			try 
			{
				bean = this.dbServer.getShare(owner, id);
				logger.debug("ShareBean Ottenuto");
			} 
			catch (ParcmanDBServerShareNotExistRemoteException e)
			{
				logger.error("Il file richiesto non e` esiste.", e);
				throw new ParcmanServerRequestErrorRemoteException();
			}
			catch (ParcmanDBServerErrorRemoteException e)
			{
				logger.error("Si e` verificato un errore interno.", e);
				throw new ParcmanServerRequestErrorRemoteException();
			}
			DownloadData downData = new DownloadData(rclient, bean);
			return downData;
		}
		else
		{
			// TODO: Aggiungere eccezione dedicata.
			logger.error("Proprietario '" + owner + "' non connesso.");
			throw new ParcmanServerRequestErrorRemoteException("Proprietario '" + owner + "' non connesso.");
		}
	}

	/**
	 * Aggiunge un utente in blacklist.
	 * 
	 * @param parcmanClientStub Lo stub del <tt>ParcmanClient</tt>. 
	 * @param userName La stringa che rappresenta il nome del client che ha fatto la richiesta.
	 * @param userForBlacklist La stringa che rappresenta il nome del client che si vuole inserire nella blacklist.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws ParcmanServerRequestErrorRemoteException si e` verificato un errore nella procedura.
	 * @throws RemoteException Eccezione remota.
	 */
	public void addToBlacklist(RemoteParcmanClient parcmanClientStub, String userName, String userForBlacklist) throws
		ParcmanServerHackWarningRemoteException,
		ParcmanServerWrongPrivilegesRemoteException, 
		ParcmanServerRequestErrorRemoteException,
		RemoteException
	{
		logger.info("Richiesta di inserire in blacklist '"+ userForBlacklist +"' da parte di '" + userName + "'.");

		checkHacking(parcmanClientStub, userName);
		checkAdminPrivileges(connectUsers.get(userName));

		// imposto comunque il flag sul database. 
		try
		{
			UserBean userbean = dbServer.getUser(userForBlacklist);
			try
			{
				userbean.setStatus("BLACKLIST");
			} 
			catch (ParcmanDBUserInvalidStatusException e)
			{
				logger.error("Stato utente non valido");
			}
			dbServer.updateUsers(); // aggiorna il database.
		}
		catch (ParcmanDBServerErrorRemoteException e)
		{
			logger.error("Errore interno al database.", e);
			throw new ParcmanServerRequestErrorRemoteException();
		}
		catch (ParcmanDBServerUserNotExistRemoteException e)
		{
			logger.error("L'utente '" + userForBlacklist + "' non e` presente sul database.", e);
			throw new ParcmanServerRequestErrorRemoteException();
		}
		//catch (RemoteException e) 
		//{
		//	logger.error("Si sono verificati problemi di rete.", e);
		//}
		
		// se l'utente e` nella attemp list lo rimuovo.
		if (this.attempUsers.containsKey(userForBlacklist))
		{
			this.removeUserFromAttempList(userForBlacklist);
		}

		// se l'utente e` online lo disconetto.
		if (this.connectUsers.containsKey(userForBlacklist))
		{
			RemoteParcmanClientUser rclient = this.connectUsers.get(userForBlacklist).getStub();
			try
			{
			      rclient.disconnect("Utente inserito in blacklist.");
			} 
			catch (UnmarshalException e)
			{
				if (e.getCause() instanceof EOFException)
					logger.info("Client disconnesso.");
				else 
					logger.error("Probabilmente si sono verificati alcuni errori durante la disconnessione.", e);
			}
			catch (RemoteException e) 
			{
				logger.error("Si sono verificati problemi di rete.", e);
			}
			this.removeUserFromConnectList(userForBlacklist);
		}
	}

	/**
	 * Rimuove un utente dalla blacklist.
	 * 
	 * @param parcmanClientStub Lo stub del <tt>ParcmanClient</tt>. 
	 * @param userName La stringa che rappresenta il nome del client che ha fatto la richiesta.
	 * @param userForBlacklist La stringa che rappresenta il nome del client che si vuole togliere dalla blacklist.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws ParcmanServerRequestErrorRemoteException si e` verificato un errore nella procedura.
	 * @throws RemoteException Eccezione remota.
	 */
	public void delFromBlacklist(RemoteParcmanClient parcmanClientStub, String userName, String userForBlacklist) throws
		ParcmanServerHackWarningRemoteException,
		ParcmanServerWrongPrivilegesRemoteException, 
		ParcmanServerRequestErrorRemoteException,
		RemoteException
	{
		logger.info("Richiesta di togliere dalla blacklist '"+ userForBlacklist +"' da parte di '" + userName + "'.");

		checkHacking(parcmanClientStub, userName);
		checkAdminPrivileges(connectUsers.get(userName));

		// imposto comunque il flag sul database. 
		try
		{
			UserBean userbean = dbServer.getUser(userForBlacklist);
			try
			{
				userbean.setStatus("READY");
			} 
			catch (ParcmanDBUserInvalidStatusException e)
			{
				logger.error("Stato utente non valido");
			}
			dbServer.updateUsers(); // aggiorna il database.
		}
		catch (ParcmanDBServerErrorRemoteException e)
		{
			logger.error("Errore interno al database.", e);
			throw new ParcmanServerRequestErrorRemoteException();
		}
		catch (ParcmanDBServerUserNotExistRemoteException e)
		{
			logger.error("L'utente '" + userForBlacklist + "' non e` presente sul database.", e);
			throw new ParcmanServerRequestErrorRemoteException();
		}
		//catch (RemoteException e) 
		//{
		//	logger.error("Si sono verificati problemi di rete.", e);
		//}
	}


	/**
	 * Rimuove un utente dalla lista di waiting e lo attiva.
	 * 
	 * @param parcmanClientStub Lo stub del <tt>ParcmanClient</tt>. 
	 * @param userName La stringa che rappresenta il nome del client che ha fatto la richiesta.
	 * @param userForBlacklist La stringa che rappresenta il nome del client che si vuole togliere dalla lista di waiting.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws ParcmanServerRequestErrorRemoteException si e` verificato un errore nella procedura.
	 * @throws RemoteException Eccezione remota.
	 */
	public void delFromWaiting(RemoteParcmanClient parcmanClientStub, String userName, String userStillWaiting) throws
		ParcmanServerHackWarningRemoteException,
		ParcmanServerWrongPrivilegesRemoteException, 
		ParcmanServerRequestErrorRemoteException,
		RemoteException
	{
		logger.info("Richiesta di togliere dalla lista di waiting '"+ userStillWaiting+"' da parte di '" + userName + "'.");

		checkHacking(parcmanClientStub, userName);
		checkAdminPrivileges(connectUsers.get(userName));

		// imposto comunque il flag sul database. 
		try
		{
			UserBean userbean = dbServer.getUser(userStillWaiting);
			try
			{
				userbean.setStatus("READY");
			} 
			catch (ParcmanDBUserInvalidStatusException e)
			{
				logger.error("Stato utente non valido", e);
			}
			dbServer.updateUsers(); // aggiorna il database.
		}
		catch (ParcmanDBServerErrorRemoteException e)
		{
			logger.error("Errore interno al database.", e);
			throw new ParcmanServerRequestErrorRemoteException();
		}
		catch (ParcmanDBServerUserNotExistRemoteException e)
		{
			logger.error("L'utente '" + userStillWaiting+ "' non e` presente sul database.", e);
			throw new ParcmanServerRequestErrorRemoteException();
		}
		//catch (RemoteException e) 
		//{
		//	logger.error("Si sono verificati problemi di rete.", e);
		//}
	}

	/**
	 * Ritorna la lista degli utenti in blacklist.
	 *
	 * @param parcmanClientStub Lo stub del <tt>ParcmanClient</tt>. 
	 * @param userName La stringa che rappresenta il nome del client che ha fatto la richiesta.
	 * @return Vettore di <tt>UserBean</tt>.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws ParcmanServerRequestErrorRemoteException si e` verificato un errore nella procedura.
	 * @throws RemoteException Eccezione remota.
	 */
	public Vector<UserBean> blacklist(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerHackWarningRemoteException,
		ParcmanServerWrongPrivilegesRemoteException, 
		ParcmanServerRequestErrorRemoteException,
		RemoteException
	{
		checkHacking(parcmanClientStub, userName);
		checkAdminPrivileges(connectUsers.get(userName));

		Vector<UserBean> users = null;
		try
		{
			dbServer.reloadUsers();
			users = dbServer.getUsers();
			Iterator<UserBean> i = users.iterator();
			
			while (i.hasNext())
			{
				if (!"BLACKLIST".equals(i.next().getStatus()))
					i.remove();
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			logger.error("Errore durante la selezione degli utenti in blacklist.", e);
			throw new ParcmanServerRequestErrorRemoteException();
		}

		return users;
	}

	/**
	 * Ritorna la lista degli utenti in stato di waiting
	 *
	 * @param parcmanClientStub Lo stub del <tt>ParcmanClient</tt>. 
	 * @param userName La stringa che rappresenta il nome del client che ha fatto la richiesta.
	 * @return Vettore di <tt>UserBean</tt>.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws ParcmanServerRequestErrorRemoteException si e` verificato un errore nella procedura.
	 * @throws RemoteException Eccezione remota.
	 */
	public Vector<UserBean> waitinglist(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerHackWarningRemoteException,
		ParcmanServerWrongPrivilegesRemoteException, 
		ParcmanServerRequestErrorRemoteException,
		RemoteException
	{
		checkHacking(parcmanClientStub, userName);
		checkAdminPrivileges(connectUsers.get(userName));

		Vector<UserBean> users = null;
		try
		{
			dbServer.reloadUsers();
			users = dbServer.getUsers();
			Iterator<UserBean> i = users.iterator();

			while (i.hasNext())
			{
				if (!"WAITING".equals(i.next().getStatus()))
					i.remove();
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			logger.error("Errore durante la selezione degli utenti in waiting.", e);
			throw new ParcmanServerRequestErrorRemoteException();
		}

		return users;
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
}

/**
 * Task per il controllo degli utenti connessi.
 *
 * @author Parcman Tm
 */
class ControlUsersTimerTask extends TimerTask
{
	private ParcmanServer parcmanServer;

	/**
	 * Costruttore.
	 *
	 * @param parcmanServer puntatore al parcmanServer
	 */
	public ControlUsersTimerTask(ParcmanServer parcmanServer)
	{
		this.parcmanServer = parcmanServer;
	}

	/**
	 * Metodo run.
	 */
	public void run() 
	{
		parcmanServer.checkUsers();
	}
}

