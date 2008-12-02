package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;

import plog.*;
import remoteexceptions.*;
import database.beans.ShareBean;
import database.beans.SearchBean;
import database.beans.UserBean;
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
	* @param dBServer Stub del DBServer
	* @throws RemoteException Eccezione remota
	*/
	public ParcmanServer(RemoteDBServer dBServer) throws
		RemoteException
	{
		this.dBServer = dBServer;
		this.connectUsers = new HashMap<String, ClientData>();
		this.attempUsers = new HashMap<String, String>();

        PLog.debug("ParcmanServer", "Avvio ControlUsersTimerTask");
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
		    PLog.debug("ParcmanServer.setShareListVersionOfUser", "Modificata la versione dei file condivisi dell'utente " + username + " (Versione: " + version + ")");
            return;
        }

		PLog.err("ParcmanServer.setShareListVersionOfUser", "Impossibile modificare la versione dei file condivisi dell'utente " + username + " (Versione: " + version + ")");
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
			    PLog.debug("ParcmanServer.forceUserToReconnect", "Riconnessione forzata di " + userName);
                client.reconnect();
            }
            catch (RemoteException e)
            {
    			PLog.err(e, "ParcmanServer.forceUserToReconnect", "Impossibile forzare la riconnessione di " + userName);
                return;
            }
		}
		else
	    {
			PLog.debug("ParcmanServer.forceUserToReconnect", "Impossibile forzare la riconnessione di " + userName + ", utente non connesso");
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
			PLog.debug("ParcmanServer.connectAttemp", "l'utente " + username + " e' gia' nella lista di attemp.");
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

        		UserBean userBean;

	        	try
        		{
		        	userBean = this.dBServer.getUser(userName);
	        	}
	        	catch(ParcmanDBServerUserNotExistRemoteException e)
	        	{
		        	PLog.debug("LoginServer.login", "Richiesta rifiutata, nome utente errato.");
		        	throw new RemoteException("Errore interno del database");
	        	}

				// Aggiungo l'utente alla lista connectUsers
				ClientData user = new ClientData(host, userName, parcmanClientStub, userBean.getPrivilege().equals(Privilege.getAdminPrivilege()));
                user.setVersion(0);
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
			PLog.debug("ParcmanServer.getSharings", "Richiesta la lista dei file condivisi dell'utente " + userName + ".");
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
				PLog.debug("ParcmanServer.getSharings", "Richiesta non valida dall'host " + this.getClientHost());
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
			PLog.debug("ParcmanServer.getSharings", "Richiesto codice di versione della lista dei file condivisi dell'utente " + userName + ".");
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
				PLog.debug("ParcmanServer.getSharings", "Richiesta non valida dall'host " + this.getClientHost());
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
				PLog.debug("ParcmanServer.search", "Richiesta non valida dall'host " + this.getClientHost());
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
			    PLog.debug("ParcmanServer.checkUsers", "l'utente " + name + " non e' piu' raggiungibile... disconnessione effettuata.");
			    iter.remove();
		    }
        }
    }

    /**
     * Restituisce lla lista degli utenti connessi.
     *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
     * @return Vettore contenente la lista dei nomi utente
     * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi
     * errati
     * @throws RemoteException Eccezione remota
     */
    public Vector<String> getConnectUsersList(RemoteParcmanClient parcmanClientStub, String userName)  throws
        RemoteException
    {
        try
		{
			PLog.debug("ParcmanServer.getConnectUsersList", "Richiesta la lista degli utenti connessi da parte di " + userName);
			// Controllo che l'utente sia connesso
			if (connectUsers.containsKey(userName))
			{
				ClientData user = connectUsers.get(userName);

				if (!this.getClientHost().equals(user.getHost()) || !user.getStub().equals(parcmanClientStub))
				{
					PLog.debug("ParcmanServer.getConnectUsersList", "Richiesta non valida, host " + this.getClientHost());
					throw new ParcmanServerHackWarningRemoteException
							("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
				}

                if (!user.getIsAdmin())
                {
                    PLog.debug("ParcmanServer.getConnectUsersList", "Impossibile esaudire la richiesta, privilegi non sufficienti");
                    throw new ParcmanServerWrongPrivilegesRemoteException();
                }


                return new Vector<String>(this.connectUsers.keySet());
			}
			else
			{
				PLog.debug("ParcmanServer.getSharings", "Richiesta non valida dall'host " + this.getClientHost());
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
		String owner = fileData[0];
		String id = fileData[1];
		try
		{
			PLog.debug("ParcmanServer.startDownload", "Richiesto nuovo download" + userName + "@" + owner + "/" + id);
			// Controllo che l'utente sia connesso
			if (this.connectUsers.containsKey(userName))
			{
				ClientData user = this.connectUsers.get(userName);

				if (!this.getClientHost().equals(user.getHost()) || !user.getStub().equals(parcmanClientStub))
				{
					PLog.debug("ParcmanServer.startDownload", "Richiesta non valida, host " + this.getClientHost());
					throw new ParcmanServerHackWarningRemoteException
							("Il ParcmanServer ha rilevato un tentativo di Hacking proveniente da questo Host.");
				}
				if (this.connectUsers.containsKey(owner))
				{
					RemoteParcmanClientUser rclient = this.connectUsers.get(owner).getStub();
					PLog.debug("ParcmanServer.startDownload", "RemoteParcmanClientUser ottenuto");
					ShareBean bean = null;
					try 
					{
						bean = this.dBServer.getShare(owner, id);
						PLog.debug("ParcmanServer.startDownload", "ShareBean Ottenuto");
					} 
					catch (ParcmanDBServerShareNotExistRemoteException e)
					{
						PLog.err(e, "ParcmanServer.startDownload", "Il file richiesto non e` esiste.");
						throw new ParcmanServerRequestErrorRemoteException();
					}
					catch (ParcmanDBServerErrorRemoteException e)
					{
						PLog.err(e, "ParcmanServer.startDownload", "Si e` verificato un errore interno.");
						throw new ParcmanServerRequestErrorRemoteException();
					}
					DownloadData downData = new DownloadData(rclient, bean);
					return downData;
				}
				else
				{
					// TODO: Aggiungere eccezione dedicata.
					PLog.err("ParcmanServer.startDownload", "Proprietario '" + owner + "' non connesso.");
					throw new ParcmanServerRequestErrorRemoteException("Proprietario '" + owner + "' non connesso.");
				}
			}
			else
			{
				PLog.debug("ParcmanServer.startDownload", "Richiesta di disconnessione non valida dall'host " + this.getClientHost());
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

