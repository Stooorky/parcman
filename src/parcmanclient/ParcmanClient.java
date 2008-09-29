package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import plog.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServerUser;
import pshell.*;
import database.beans.ShareBean;
import database.beans.SearchBean;

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
    * Vettore dei file condivisi. 
    */
    private Vector<ShareBean> shares;

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
        this.shares = new Vector<ShareBean>();
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
			this.shares = parcmanServerStub.getSharings(this, this.userName);
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

		PLog.debug("fixSharingDirectory", "Fix della directory dei file condivisi " + this.getSharingDirectory());

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

		this.scanSharingDirectory(mainDir);
	}

	/**
	* Restituisce la lista dei file condivisi.
	*
	* @return Vector di ShareBean contenente la lista dei file condivisi
	*/
    public Vector<ShareBean> getShares()
    {
        return this.shares;
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
	* Esegue la scansione ricorsiva della directory dei file condivisi.
	*
	* @param dir File directory condivisa
	*/
	private void scanSharingDirectory(File dir)
	{
		File[] content = dir.listFiles();

		for (int i=0; i<content.length; i++)
		{
			if (content[i].isDirectory())
				this.scanSharingDirectory(content[i]);
			else if (content[i].isFile())
				System.out.println(content[i].getPath());
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