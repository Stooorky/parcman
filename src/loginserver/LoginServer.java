package loginserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.rmi.activation.*;
import java.lang.*;
import java.security.*;

import plog.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServer;
import parcmanserver.RemoteParcmanServerUser;
import parcmanclient.*;
import database.beans.UserBean;
import databaseserver.RemoteDBServer;
import privilege.Privilege;

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

		PLog.debug("LoginServer", "Inizializzo il LoginServer.");
		PLog.debug("LoginServer", "Ripristino e aggiornamento dei dati di sessione.");

        // Ricavo dall'atDate i dati della sessione
		LoginServerAtDate onAtDate = (LoginServerAtDate)(atDate.get());
		this.parcmanServerStub = onAtDate.getParcmanServerStub();
		this.dBServerStub = onAtDate.getDBServerStub();
		this.activationsCount = onAtDate.getActivationsCount();

		// Creo un nuovo LoginServerAtDate con i dati di sessione aggiornati
		LoginServerAtDate newAtDate = new LoginServerAtDate(this.activationsCount+1, this.parcmanServerStub, this.dBServerStub);
		ActivationDesc newActDesc = new ActivationDesc(actDesc.getGroupID(), actDesc.getClassName(), actDesc.getLocation(), new MarshalledObject(newAtDate));
		actDesc = actSystem.setActivationDesc(id, newActDesc);
	}

	/**
	 * Esegue il login di un Client alla rete Parcman.
	 * Ritorna un MobileServer esportato nel caso in cui il login abbia successo.
	 * 
	 * @param name Nome utente
	 * @param password Password utente
	 * @return Un MobileServer di tipo ParcmanClient se il login ha successo, null altrimenti.
	 * @throws RemoteException Eccezione Remota.
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

		// cripto la password
		String encryptedPassword = PasswordService.getInstance().encrypt(password);

		if (user == null || !(encryptedPassword.equals(user.getPassword())))
		{
			PLog.debug("LoginServer.login", "Richiesta rifiutata, password o nome utente errati.");
			return null;
		}

        if (user.getPrivilege().equals(Privilege.getUserPrivilege())) // Utente
        {
		    // Creo un'istanza di ParcmanClient da passare al Client
		    RemoteParcmanClient parcmanClient = new ParcmanClient(((RemoteParcmanServerUser)this.parcmanServerStub), user.getName());
		    // Deesporto il server appena creato
		    unexportObject(parcmanClient, true);

            try
            {
                // Aggiungo il Client alla lista di attemp del Parcmanserver
                parcmanServerStub.connectAttemp(name, this.getClientHost());
		    }
		    catch(ServerNotActiveException e)
		    {
			    PLog.err(e, "LoginServer.login", "Errore di rete, ClientHost irraggiungibile.");
                return null;
		    }
            catch(RemoteException e)
            {
 		    	PLog.err(e, "LoginServer.login", "Errore interno del ParcmanServer.");
                return null;
            }

		    PLog.debug("LoginServer.login", "Richiesta accettata, e' stato inviato il ParcmanClient.");

		    return parcmanClient;
        }
        else
        {
            PLog.err("LoginServer.login", "Privilegi dell'utente " + user.getName() + " errati (" + user.getPrivilege() + ")");
            return null;
        }
	}

	/**
	 * Esegue la registrazione di un nuovo account.
	 * 
	 * @param name Nome utente
	 * @param password Password utente
	 * @throws ParcmanDBServerUserExistRemoteException Utente gia' presente nel database
	 * @throws RemoteException Eccezione Remota
	 */
	public void createAccount(String name, String password) throws
		RemoteException,
		ParcmanDBServerUserExistRemoteException,
		ParcmanDBServerUserNotValidRemoteException
	{
		try
		{
			PLog.debug("LoginServer.createAccount", "E' stata ricevuta una richiesta di creazione account da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "LoginServer.createAccount", "E' stata ricevuta una richiesta di creazione account ma l'Host risulta irraggiungibile.");
			throw new RemoteException();
		}

		// cripto la password
		String encryptedPassword = PasswordService.getInstance().encrypt(password);

		// Creo lo UserBean con i dati utente
		// TODO Sistemare i privilegi.
		UserBean user = new UserBean();
		user.setName(name);
		user.setPassword(encryptedPassword);
		user.setPrivilege(Privilege.getUserPrivilege());

		try
		{
			this.dBServerStub.addUser(user);
		}
		catch(ParcmanDBServerUserExistRemoteException e)
		{
			PLog.debug("LoginServer.createAccount", "Richiesta rifiutata, il nome utente fornito e' gia' in uso.");
			throw new ParcmanDBServerUserExistRemoteException();
		}
		catch(ParcmanDBServerUserNotValidRemoteException e)
		{
			PLog.debug("LoginServer.createAccount", "Richiesta rifiutata, i dati forniti non sono validi.");
			throw new ParcmanDBServerUserNotValidRemoteException();
		}
		catch(RemoteException e)
		{
			PLog.err(e, "LoginServer.createAccount", "Impossibile soddisfare la richiesta.");
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
			PLog.debug("LoginServer.ping", "E' stata ricevuta una richiesta di ping da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			PLog.err(e, "LoginServer.ping", "Errore di rete, ClientHost irraggiungibile.");
		}
	}

    /**
     * Dereferenziazione del Server.
     * Chiamato dal sistema Rmid.
     */
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

