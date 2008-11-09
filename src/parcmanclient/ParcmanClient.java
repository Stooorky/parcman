package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.net.*;

import plog.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServerUser;
import pshell.*;
import database.beans.ShareBean;
import database.beans.SearchBean;
import parcmanagent.UpdateList;
import parcmanagent.exceptions.*;

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
	private static final long serialVersionUID = 4242L;

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
	* Costruttore.
	*
	* @throws RemoteException Eccezione remota
	*/
	public ParcmanClient(RemoteParcmanServerUser parcmanServerStub, String userName) throws
		RemoteException
	{
		this.parcmanServerStub = parcmanServerStub;
		this.userName = userName;
        this.sharesServer = new Vector<ShareBean>();
        this.sharesAgent = new Vector<ShareBean>();
	}

	/**
	* Lancia la connessione alla rete Parcman.
	*
	* @throws RemoteException Eccezione Remota
	*/
	public void startConnection() throws
		RemoteException
	{
		try
		{
			// Spedisco lo stub del ParcmanClient al ParcmanServer
			parcmanServerStub.connect(this, this.userName);
		}
		catch(RemoteException e)
		{
			System.out.println(e.getMessage());
			System.out.println("Autenticazione fallita.");
			System.exit(0);
		}

		System.out.println("Autenticazione in corso...");
		System.out.println("Invio richiesta file condivisi.");

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
			System.out.println("Il server non e' in grado di esaudire la richiesta. Connessione terminata.");
			System.exit(0);
		}
		catch(RemoteException e)
		{
			System.out.println("Il server non e' in grado di esaudire la richiesta. Connessione terminata.");
			System.exit(0);
		}

		System.out.println("Autenticazione alla rete Parcman avvenuta con successo. Benvenuto!.");

		// Fixo e ricontrollo la directory di condivisione.
		this.fixSharingDirectory();

		Timer timer = new Timer();
		this.scanDirectoryTimerTask = new ScanDirectoryTimerTask(this);
		// La scansione della directory condivisa viene effettuata ogni 60 secondi
		// con una attesa iniziale di 10 secondi.
		timer.schedule(this.scanDirectoryTimerTask, 10000, 60000);

		// Lancio la shell
		PShell shell = new PShell(new ShellData(this.parcmanServerStub, this, this.userName));
		shell.run();
	}

	/**
	* Esegue la disconnessione dalla rete.
	*
	* @throws RemoteException Eccezione remota
	*/
	public void exit()
	{
		PLog.debug("ParcmanClient.exit", "Disconnessione in corso.");

		try
		{
			this.parcmanServerStub.disconnect(this, this.userName);
            this.parcmanServerStub = null;
		}
		catch(RemoteException e)
		{
			System.out.println(e.getMessage());
			System.out.println("Disconnessione fallita. (force exit)");
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
	* Restituisce il PATH assoluto della directory condivisa.
	*
	* @return PATH assoluto della directory condivisa
	*/
	private String getSharingDirectory()
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

		PLog.debug("ParcmanClient.fixSharingDirectory", "Fix della directory dei file condivisi " + this.getSharingDirectory());

		mainDir.mkdirs();
	
		if (!mainDir.isDirectory())
		{
			System.out.println("Attenzione. La direcotory " + this.getSharingDirectory() + " non puo' essere creata.");

			this.exit();
		}
	}

	/**
	* Esegue la scansione ricorsiva della directory dei file condivisi.
	*/
	public void scanSharingDirectory()
	{
		File mainDir = new File(this.getSharingDirectory());
        Vector<ShareBean> newList = new Vector<ShareBean>();

		this.scanSharingDirectory(mainDir, newList);

        // Costruisco le UpdateList
        boolean find;
        UpdateList updateServer = new UpdateList();
        UpdateList updateAgent = new UpdateList();

        for (int i=0; i<newList.size(); i++)
        {
            find = false;
            for (int x=0; x<sharesServer.size(); x++)
                if (newList.get(i).getUrl().equals(sharesServer.get(x).getUrl()))
                    find = true;

            if (!find)
                updateServer.addShareBean(newList.get(i));

            if (sharesAgent != null)
            {
                find = false;
                for (int x=0; x<sharesAgent.size(); x++)
                    if (newList.get(i).getUrl().equals(sharesAgent.get(x).getUrl()))
                        find = true;

                if (!find)
                    updateAgent.addShareBean(newList.get(i));
            }
        }

        for (int i=0; i<sharesServer.size(); i++)
        {
            find = false;
            for (int x=0; x<newList.size(); x++)
                if (newList.get(x).getUrl().equals(sharesServer.get(i).getUrl()))
                    find = true;

            if (!find)
                updateServer.addRemovableId(sharesServer.get(i).getId());
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
                    updateAgent.addRemovableId(sharesAgent.get(i).getId());
            }
        }

        if (sharesAgent  != null)
        {
            updateAgent.setVersion(this.versionAgent+1);
            this.sharesAgentUpdateList = updateAgent;
        }
        updateServer.setVersion(this.versionServer+1);
        this.sharesServerUpdateList = updateServer;

        /*
        if (sharesAgentUpdateList != null)
            System.out.println(this.sharesAgentUpdateList.toString());
        System.out.println(this.sharesServerUpdateList.toString());
        sharesServer = newList; */
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
            PLog.debug("ParcmanClient.getUpdateList", "Codice di versione errato (" + version + ")");
            this.exit();
            return null;
        }

        try
        {
            if (version == versionServer)
            {
                this.sharesAgent = this.sharesServerUpdateList.getUpdatedSharesList(this.sharesServer);
                this.versionAgent = this.sharesServerUpdateList.getVersion();
                return this.sharesServerUpdateList;
            }
            else if (version == versionAgent)
            {
                this.sharesAgent = this.sharesAgentUpdateList.getUpdatedSharesList(this.sharesAgent);
                this.sharesServer = this.sharesAgent;
                this.versionServer = this.versionAgent;
                this.versionAgent = this.sharesAgentUpdateList.getVersion();
                return this.sharesAgentUpdateList;
            }
        }
        catch (UpdateSharesListErrorException e)
        {
            PLog.err(e, "ParcmanClient.getUpdateList", "Errore durante l'avanzamento di versione");
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
                    PLog.debug("ParcmanClient.scanSharingDirectory", "URL del file " + content[i].getPath() + " errata");
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
			PLog.debug("ParcmanClient.ping", "E' stata ricevuta una richiesta di ping da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "ParcmanClient.ping", "Errore di rete, ClientHost irraggiungibile.");
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

