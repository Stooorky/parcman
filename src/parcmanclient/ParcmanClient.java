package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.net.*;

import io.PropertyManager;
import io.Logger;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServerUser;
import pshell.*;
import database.beans.ShareBean;
import database.beans.SearchBean;
import parcmanagent.UpdateList;
import parcmanagent.exceptions.*;
import parcmanagent.RemoteParcmanAgentClient;

/**
 * Mobile server in esecuzione presso il Client.
 *
 * @author Parcman Tm
 */
public class ParcmanClient
	extends UnicastRemoteObject
	implements RemoteParcmanClient, Serializable
{
	/**
	 * Logger
	 */
	private Logger logger;

	/**
	 * Stub del ParcmanServer.
	 */
	private RemoteParcmanServerUser parcmanServerStub;

	/**
	 * Nome utente.
	 */
	private String userName;

	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Vettore dei file condivisi sul server. 
	 */
	private Vector<ShareBean> sharesServer;

	/**
	 * Versione dei file condivisi sul server. 
	 */
	private int versionServer;

	/**
	 * Vettore dei file condivisi sull'agente.
	 */
	private Vector<ShareBean> sharesAgent;

	/**
	 * Versione dei file condivisi sull'agente. 
	 */
	private int versionAgent;

	/**
	 * UpdateList per sharesServer.
	 */
	private UpdateList sharesServerUpdateList;

	/**
	 * UpdateList per sharesAgent.
	 */
	private UpdateList sharesAgentUpdateList;

	/**
	 * Directory di condivisione.
	 */
	private static final String SHARE_DIRECTORY = "/ParcmanShare";

	/**
	 * TimerTask per la scansione della directory condivisa.
	 */
	private TimerTask scanDirectoryTimerTask;

	/**
	 * Stato del client.
	 */
	private boolean ready;

	/**
	 * Privilegi utente.
	 */
	private boolean isAdmin;

	/**
	 * Costruttore.
	 *
	 * @throws RemoteException Eccezione remota
	 */
	public ParcmanClient(RemoteParcmanServerUser parcmanServerStub, String userName, boolean isAdmin) throws
		RemoteException
	{
		this.parcmanServerStub = parcmanServerStub;
		this.userName = userName;
		this.sharesServer = new Vector<ShareBean>();
		this.sharesAgent = new Vector<ShareBean>();
		this.ready = false;
		this.isAdmin = isAdmin;
	}

	/**
	 * Restituisce true se il ParcmanClient ha bisogno di eseguire un
	 * update, cioe' l'UpdateList per la versione fornita non e' vuota.
	 *
	 * @param versione Versione della SharingList
	 * @return true se il ParcmanClient ha bisogno di eseguire un update
	 * della lista dei file condivisi
	 * @throws RemoteException Eccezione remota
	 */
	public boolean haveAnUpdate(int version) throws
		RemoteException
	{
		if (version != this.versionServer && version != this.versionAgent)
		{
			logger.debug("Codice di versione errato (" + version + ")");
			this.exit();
			return false;
		}

		if ((version == versionServer && sharesServerUpdateList != null && this.versionAgent == -1 && ready) ||
				(version == versionAgent && sharesAgentUpdateList != null && this.versionAgent != -1 && ready))
			return true;
		else if (version == versionAgent && ready)
		{
			this.sharesServer = this.sharesAgent;
			this.versionServer = this.versionAgent;
			this.sharesAgent = null;
			this.sharesAgentUpdateList = null;
			this.versionAgent = -1;
			logger.debug("Codici di versione attuali Server:" + this.versionServer + ", Agent:" + this.versionAgent);
			return false;
		}
		else if (version == versionServer && ready)
		{
			this.sharesAgent = null;
			this.sharesAgentUpdateList = null;
			this.versionAgent = -1;
			logger.debug("Codici di versione attuali Server:" + this.versionServer + ", Agent:" + this.versionAgent);
			return false;
		}
		else if (!ready)
			return false;

		logger.error("Codice di versione errato (" + version + ")");
		this.exit();
		return false;
	}

	/**
	 * Permette il trasferimento di un agente remoto.
	 * Questa funzione lancia il metodo run dell'agente ricevuto come
	 * parametro.
	 *
	 * @param parcmanAgent Stub dell'agente remoto
	 * @throws RemoteException Eccezione remota
	 */
	public void transferAgent(RemoteParcmanAgentClient parcmanAgent) throws
		RemoteException
	{
		try
		{
			parcmanAgent.run();
			parcmanAgent = null;
		}
		catch (RemoteException e)
		{
			logger.error("Eccezione remota nella chiamta al metodo run() del ParcmanAgent");
			return;
		}
	}

	/**
	 * Disconnette il client. 
	 *
	 * @throws RemoteException Eccezione Remota.
	 */
	public void disconnect(String message) throws 
		RemoteException
	{
		logger.info(new String[] { "Sei stato inserito in blacklist.", "Disconessione in corso..." });
		this.parcmanServerStub = null;
		System.exit(0);
	}

	/**
	 * Forza la riconnessione del client.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void reconnect() throws
		RemoteException
	{
		logger.info("\n\n----- Ricconnessione forzata in corso... -----");

		try
		{
			// Spedisco lo stub del ParcmanClient al ParcmanServer
			parcmanServerStub.connect(this, this.userName);
		}
		catch(RemoteException e)
		{
			logger.error("Autenticazione fallita.", e);
			System.exit(0);
		}

		logger.debug("Invio richiesta file condivisi.\n\n");

		try
		{
			this.sharesServer = parcmanServerStub.getSharings(this, this.userName);
			this.versionServer = parcmanServerStub.getSharingsVersion(this, this.userName);
			this.sharesAgentUpdateList = null;
			this.sharesServerUpdateList = null;
			this.sharesAgent = null;
			this.versionAgent = -1;
		}
		catch(ParcmanServerRequestErrorRemoteException e)
		{
			logger.error("Il server non e' in grado di esaudire la richiesta. Connessione terminata.");
			System.exit(0);
		}
		catch(RemoteException e)
		{
			logger.error("Il server non e' in grado di esaudire la richiesta. Connessione terminata.");
			System.exit(0);
		}
	}

	/**
	 * Lancia la connessione alla rete Parcman.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void startConnection() throws
		RemoteException
	{
		// recupero il logger lato client
		this.logger = Logger.getLogger("client-side");
		try
		{
			// Spedisco lo stub del ParcmanClient al ParcmanServer
			parcmanServerStub.connect(this, this.userName);
		}
		catch(RemoteException e)
		{
			logger.error("Autenticazione fallita.", e);
			System.exit(0);
		}

		logger.info("Autenticazione in corso...");
		logger.debug("Invio richiesta file condivisi.");

		try
		{
			this.sharesServer = parcmanServerStub.getSharings(this, this.userName);
			this.versionServer = parcmanServerStub.getSharingsVersion(this, this.userName);
			this.sharesAgentUpdateList = null;
			this.sharesServerUpdateList = null;
			this.sharesAgent = null;
			this.versionAgent = -1;
		}
		catch(ParcmanServerRequestErrorRemoteException e)
		{
			logger.error("Il server non e' in grado di esaudire la richiesta. Connessione terminata.");
			System.exit(0);
		}
		catch(RemoteException e)
		{
			logger.debug("Il server non e' in grado di esaudire la richiesta. Connessione terminata.");
			System.exit(0);
		}

		logger.info("Autenticazione alla rete Parcman avvenuta con successo. Benvenuto!.");

		// Fixo e ricontrollo la directory di condivisione.
		this.fixSharingDirectory();

		//Timer timer = new Timer();
		//this.scanDirectoryTimerTask = new ScanDirectoryTimerTask(this);
		// La scansione della directory condivisa viene effettuata ogni 60 secondi
		// con una attesa iniziale di 10 secondi.
		//timer.schedule(this.scanDirectoryTimerTask, 10000, 60000);

		ready = true;

		if (!isAdmin)
		{
			// Lancio la shell
			PShell shell = new PShell(new ShellData(this.parcmanServerStub, this, this.userName));
			shell.run();
		}
		else
		{
			// Lancio la shell
			PShell shell = new PShell(new ShellDataAdmin(this.parcmanServerStub, this, this.userName));
			shell.run();
		}
	}

	/**
	 * Esegue la disconnessione dalla rete.
	 *
	 * @throws RemoteException Eccezione remota
	 */
	public void exit()
	{
		logger.info("Disconnessione in corso.");

		try
		{
			this.parcmanServerStub.disconnect(this, this.userName);
			this.parcmanServerStub = null;
		}
		catch(RemoteException e)
		{
			logger.error("Disconnessione fallita. (force exit)", e);
			System.exit(1);
		}

		System.exit(0);
	}

	/**
	 * Ritorna il nome utente del proprietario della sessione.
	 *
	 * @return Nome utente del proprietario della sessione
	 * @throws RemoteException Eccezione remota
	 */
	public String getUserName() throws
		RemoteException
	{
		return this.userName;
	}

	/**
	 * Trasmette il file richiesto come stream di byte.
	 *
	 * @param id Id del file da trasmettere
	 * @return Stream di byte
	 * @throws ParcmanClientFileNotExistsRemoteException File non
	 * presente tra i file condivisi
	 * @throws ParcmanClientFileErrorRemoteException Errore nella creazione
	 * del buffer
	 * @throws RemoteException Eccezione remota
	 */
	public byte[] getFile(int id) throws
		ParcmanClientFileNotExistsRemoteException,
		ParcmanClientFileErrorRemoteException,
		RemoteException
	{
		ShareBean shareBean = null;

		for (int i=0; i < sharesServer.size(); i++)
			if (sharesServer.get(i).getId() == id)
				shareBean = sharesServer.get(i);

		if (shareBean == null)
			throw new ParcmanClientFileNotExistsRemoteException();

		try
		{
			File file = new File(shareBean.getUrl().toURI());

			if (!file.exists() || !file.canRead())
				throw new ParcmanClientFileNotExistsRemoteException();

			byte buffer[] = new byte[(int)file.length()];
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
			input.read(buffer, 0, buffer.length);
			input.close();
			return buffer;
		}
		catch (Exception e)
		{
			logger.error("Impossibile trasmettere il file con id" + id);
			throw new ParcmanClientFileErrorRemoteException();
		}
	}

	/**
	 * Restituisce il PATH assoluto della directory condivisa.
	 *
	 * @return PATH assoluto della directory condivisa
	 */
	public String getSharingDirectory()
	{
		return System.getenv("HOME") + SHARE_DIRECTORY;
	}

	/**
	 * Controlla l'esistenza/Crea la directory condivisa.
	 * In caso non possa essere creata la directory condivisa chiama la routine this.exit().
	 */
	private void fixSharingDirectory()
	{
		File mainDir = new File(this.getSharingDirectory());

		logger.debug("Fix della directory dei file condivisi " + this.getSharingDirectory());

		mainDir.mkdirs();

		if (!mainDir.isDirectory())
		{
			logger.error("Attenzione. La direcotory " + this.getSharingDirectory() + " non puo' essere creata.");

			this.exit();
		}
	}

	/**
	 * Esegue la scansione ricorsiva della directory dei file condivisi.
	 */
	public void scanSharingDirectory()
	{
		ready = false;
		File mainDir = new File(this.getSharingDirectory());
		Vector<ShareBean> newList = new Vector<ShareBean>();

		this.scanSharingDirectory(mainDir, newList);

		// Costruisco le UpdateList
		boolean find;
		boolean modifyServer = false;
		boolean modifyAgent = false;
		UpdateList updateServer = new UpdateList();
		UpdateList updateAgent = new UpdateList();

		// Aggiungo alle liste di update i file non presenti nelle
		// sharesList corrispondenti
		for (int i=0; i<newList.size(); i++)
		{
			find = false;
			for (int x=0; x<sharesServer.size(); x++)
				if (newList.get(i).getUrl().equals(sharesServer.get(x).getUrl()))
					find = true;

			if (!find)
			{
				modifyServer = true;
				updateServer.addShareBean(newList.get(i));
			}

			if (sharesAgent != null)
			{
				find = false;
				for (int x=0; x<sharesAgent.size(); x++)
					if (newList.get(i).getUrl().equals(sharesAgent.get(x).getUrl()))
						find = true;

				if (!find)
				{
					modifyAgent = true;
					updateAgent.addShareBean(newList.get(i));
				}
			}
		}

		// Aggiungo alle liste di update i file da eliminare, cioe' non
		// piu' presenti della cartella ParcmanShare
		for (int i=0; i<sharesServer.size(); i++)
		{
			find = false;
			for (int x=0; x<newList.size(); x++)
				if (newList.get(x).getUrl().equals(sharesServer.get(i).getUrl()))
					find = true;

			if (!find)
			{
				modifyServer = true;
				updateServer.addRemovableId(sharesServer.get(i).getId());
			}
		}

		if (sharesAgent != null)
		{
			for (int i=0; i<sharesAgent.size(); i++)
			{
				find = false;
				for (int x=0; x<newList.size(); x++)
					if (newList.get(x).getUrl().equals(sharesAgent.get(i).getUrl()))
						find = true;

				if (!find)
				{
					modifyAgent = true;
					updateAgent.addRemovableId(sharesAgent.get(i).getId());
				}
			}
		}

		if (sharesAgent != null && modifyAgent)
		{
			logger.debug("Aggiornata la versione Agent");
			updateAgent.setVersion(this.versionAgent+1);
			this.sharesAgentUpdateList = updateAgent;
		}
		else
			this.sharesAgentUpdateList = null;

		if (modifyServer)
		{
			logger.debug("Aggiornata la versione Server");
			updateServer.setVersion(this.versionServer+1);
			this.sharesServerUpdateList = updateServer;
		}
		else
			this.sharesServerUpdateList = null;

		/*
		   if (sharesAgentUpdateList != null)
		   System.out.println(this.sharesAgentUpdateList.toString());
		   System.out.println(this.sharesServerUpdateList.toString());
		   sharesServer = newList; */

		ready = true;
	}

	/**
	 * Restituisce la lista dei file condivisi.
	 *
	 * @return Vector di ShareBean contenente la lista dei file condivisi
	 */
	public Vector<ShareBean> getShares()
	{
		return this.sharesServer;
	}

	/**
	 * Restituisce il TimerTask dello scan della directory condivisa.
	 *
	 * @return TimerTask scansione della directory condivisa
	 */
	public TimerTask getScanDirectoryTimerTask()
	{
		return this.scanDirectoryTimerTask;
	}

	/**
	 * Restituisce lo stub del ParcmanClient.
	 *
	 * @return ParcmanClient Stub dell'oggetto locale
	 */
	public RemoteParcmanClient getStub()
	{
		return (RemoteParcmanClient)this;
	}

	/**
	 * Restituisce la lista di Update a partire dalla versione.
	 *
	 * @param version Versione della SharingList
	 * @return UpdateList per la versione data
	 * @throws RemoteException Eccezione remota
	 */
	public UpdateList getUpdateList(int version) throws
		RemoteException
	{
		if (version != versionServer && version != versionAgent)
		{
			logger.error("Codice di versione errato (" + version + ")");
			this.exit();
			return null;
		}

		if (!ready)
		{
			logger.debug("ParcmanClient non pronto");
			return null;
		}

		try
		{
			if (version == versionServer)
			{
				this.sharesAgent = this.sharesServerUpdateList.getUpdatedSharesList(this.sharesServer);
				this.versionAgent = this.sharesServerUpdateList.getVersion();
				this.sharesAgentUpdateList = null;
				UpdateList ret = this.sharesServerUpdateList;
				this.sharesServerUpdateList = null;
				logger.debug("Codici di versione attuali Server:" + this.versionServer + ", Agent:" + this.versionAgent);
				return ret;
			}
			else if (version == versionAgent)
			{
				this.sharesServer = this.sharesAgent;
				this.versionServer = this.versionAgent;
				this.sharesAgent = this.sharesAgentUpdateList.getUpdatedSharesList(this.sharesAgent);
				this.versionAgent = this.sharesAgentUpdateList.getVersion();
				this.sharesServerUpdateList = null;
				logger.debug("Codici di versione attuali Server:" + this.versionServer + ", Agent:" + this.versionAgent);
				UpdateList ret = this.sharesAgentUpdateList;
				this.sharesAgentUpdateList = null;
				return ret;
			}

			this.sharesAgentUpdateList = null;
			this.sharesServerUpdateList = null;

			logger.debug("Codici di versione attuali Server:" + this.versionServer + ", Agent:" + this.versionAgent);
		}
		catch (UpdateSharesListErrorException e)
		{
			logger.error("Errore durante l'avanzamento di versione", e);
			this.exit();
			return null;
		}

		return null;
	}

	private int getNewId(Vector<ShareBean> newList)
	{
		Vector<Integer> ids = new Vector<Integer>();

		for (int i=0; i<newList.size(); i++)
			ids.add(new Integer(newList.get(i).getId()));

		for (int i=0; i<sharesServer.size(); i++)
			ids.add(new Integer(sharesServer.get(i).getId()));

		if (sharesAgent != null)
			for (int i=0; i<sharesAgent.size(); i++)
				ids.add(new Integer(sharesAgent.get(i).getId()));

		Collections.sort(ids);

		if (ids.size() == 0 || ids.get(0).intValue() != 0)
			return 0;

		for (int i=1; i<ids.size(); i++)
			if (ids.get(i).intValue() != ids.get(i-1).intValue() &&
					ids.get(i).intValue() != ids.get(i-1).intValue()+1)
				return ids.get(i-1).intValue()+1;

		return ids.get(ids.size()-1).intValue()+1;
	}

	/**
	 * Esegue la scansione ricorsiva della directory dei file condivisi.
	 *
	 * @param dir File directory condivisa
	 * @param newList Lista dei nuovi file aggiunti
	 */
	private void scanSharingDirectory(File dir, Vector<ShareBean> newList)
	{
		File[] content = dir.listFiles();

		for (int i=0; i<content.length; i++)
		{
			if (content[i].isDirectory())
				this.scanSharingDirectory(content[i], newList);
			else if (content[i].isFile())
			{
				ShareBean newFile = new ShareBean();
				newFile.setId(this.getNewId(newList));

				try
				{
					newFile.setUrl(content[i].toURI().toURL());
				}
				catch (MalformedURLException e)
				{
					logger.error("URL del file " + content[i].getPath() + " errata");
					continue;
				}

				newFile.setOwner(this.userName);
				newFile.addKeyword(content[i].getName());
				newList.add(newFile);
				// System.out.println(newList.toString());
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
			logger.debug("E' stata ricevuta una richiesta di ping da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			logger.error("Errore di rete, ClientHost irraggiungibile.", e);
		}
	}
}


/**
 * TimerTask per la scansione della directory condivisa.
 *
 * @author Parcman Tm
 */
class ScanDirectoryTimerTask extends TimerTask
{
	/**
	 * Puntatore al ParcmanClient locale.
	 */
	ParcmanClient parcmanClient;

	/**
	 * Costruttore.
	 *
	 * @param parcmanClient ParcmanClient locale
	 */
	public ScanDirectoryTimerTask(ParcmanClient parcmanClient)
	{
		this.parcmanClient = parcmanClient;
	}

	/**
	 * Metodo run.
	 */
	public void run()
	{
		this.parcmanClient.scanSharingDirectory();
	}
}

