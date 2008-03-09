package loginserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.rmi.activation.*;
import java.lang.*;

import plog.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServer;
import parcmanclient.*;
import database.beans.UserBean;
import databaseserver.RemoteDBServer;

/**
 * Server di login.
 *
 * @author Parcman Tm
 */
public class LoginServer
	extends Activatable 
	implements RemoteLoginServer, Unreferenced
{
	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 42L;

    /**
     * Stub del ParcmanServer.
     */
    private RemoteParcmanServer parcmanServerStub;

	/**
	 * Stub del DBServer.
	 */
	private RemoteDBServer dBServerStub;

    /**
     * Contatore del numero di attivazioni.
     */
    private int activationsCount;

	/**
	 * Costruttore.
     *
     * @param id Id del server, assegnato dal sistema di attivazione
     * @param atDate Dati utente
     * @throws ActivationException Impossibile attivare il server
     * @throws IOException Errore generico di IO
     * @throws ClassNotFoundException Impossibile trovare la definizione della classe
	 */
	public LoginServer(ActivationID id, MarshalledObject atDate) throws
        ActivationException,
        IOException,
        ClassNotFoundException
	{
        super(id, 35000);

        // Ricavo l'ActivationSystem
        ActivationSystem actSystem = ActivationGroup.getSystem();
        // Ricavo l'ActivationDesc dall'ActivationSystem
        ActivationDesc actDesc = actSystem.getActivationDesc(id);
       
        this.parcmanServerStub = null;
		this.dBServerStub = null;
        this.activationsCount = 1;
 
        PLog.debug("LoginServer", "Inizializzo il LoginServer.");
        if (atDate != null)
        {
            PLog.debug("LoginServer", "Ripristino i dati di sessione.");
            LoginServerAtDate onAtDate = (LoginServerAtDate)(atDate.get());
            this.parcmanServerStub = onAtDate.getParcmanServerStub();
			this.dBServerStub = onAtDate.getDBServerStub();
            this.activationsCount = onAtDate.getActivationsCount();
        }
        else
        {
            PLog.err("LoginServer", "Impossibile ripristinare la sessione.");
        }

        // Creo un nuovo LoginServerAtDate con i dati di sessione aggiornati
        PLog.debug("LoginServer", "Aggiorno i dati di sessione");
        LoginServerAtDate newAtDate = new LoginServerAtDate(this.activationsCount+1, this.parcmanServerStub, this.dBServerStub);
        ActivationDesc newActDesc = new ActivationDesc(actDesc.getGroupID(), actDesc.getClassName(), actDesc.getLocation(), new MarshalledObject(newAtDate));
        actDesc = actSystem.setActivationDesc(id, newActDesc);
	}

	/**
	 *
	 */
	public RemoteParcmanClient login(String name, String password) throws
		RemoteException
	{
		try
		{
			PLog.debug("LoginServer.login", "E' stata ricevuta una richiesta di login dall'host " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
            PLog.err(e, "LoginServer.login", "E' stata ricevuta una richiesta di login ma l'Host risulta irraggiungibile.");
			return null;
		}

		UserBean user = this.dBServerStub.getUser(name);

		if (user == null || !(user.getPassword().equals(password)))
		{
			PLog.debug("LoginServer.login", "Richiesta rifiutata, password o nome utente errati.");
			return null;
		}

		// Creo un'istanza di ParcmanClient da passare al Client
		ParcmanClient parcmanClient = new ParcmanClient(this.parcmanServerStub);
		// Deesporto il server appena creato
		unexportObject(parcmanClient, true);

		return parcmanClient;
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
            PLog.debug("LoginServer.ping", "E' stata ricevuta una richiesta di ping da " + this.getClientHost());
        }
        catch(ServerNotActiveException e)
        {
            PLog.err(e, "LoginServer.ping", "Errore di rete, ClientHost irraggiungibile.");
        }
    }

    public void unreferenced()
    {
        try
        {
            PLog.debug("LoginServer.unreferenced", "Disattivazione del LoginServer in corso");
			// Rendo inattivo il LoginServer
			this.inactive(getID());

			// Invoco il Garbage Collector
            System.gc();

            PLog.debug("LoginServer.unreferenced", "Disattivazione avvenuta con succeso");
        }
        catch (Exception e)
        {
            PLog.err(e, "LoginServer.unreferenced", "Impossibile disattivare il LoginServer");
            System.out.close();
        }
    }
}

