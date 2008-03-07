package loginserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.rmi.activation.*;

import plog.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServer;

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
    private RemoteParcmanServer remoteParcmanServerStub;

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
       
        this.remoteParcmanServerStub = null;
        this.activationsCount = 1;
 
        PLog.debug("LoginServer", "Inizializzo il LoginServer.");
        if (atDate != null)
        {
            PLog.debug("LoginServer", "Ripristino i dati di sessione.");
            LoginServerAtDate onAtDate = (LoginServerAtDate)(atDate.get());
            this.remoteParcmanServerStub = onAtDate.getRemoteParcmanServerStub();
            this.activationsCount = onAtDate.getActivationsCount();
        }
        else
        {
            PLog.err("LoginServer", "Impossibile ripristinare la sessione.");
        }

        // Creo un nuovo LoginServerAtDate con i dati di sessione aggiornati
        PLog.debug("LoginServer", "Aggiorno i dati di sessione");
        LoginServerAtDate newAtDate = new LoginServerAtDate(this.activationsCount+1, this.remoteParcmanServerStub);
        ActivationDesc newActDesc = new ActivationDesc(actDesc.getGroupID(), actDesc.getClassName(), actDesc.getLocation(), new MarshalledObject(newAtDate));
        actDesc = actSystem.setActivationDesc(id, newActDesc);
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

