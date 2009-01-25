/*
 * Parcman Project, the Italian Arcade Network
 * Copyright (C) 2008-2009 Parcman Tm (Marchi Sirio, Marcantoni Francesco, Emanuele Dona')
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 *
 * E-Mail:	ramyel [at] gmail [dot] com
 * 			sirio.marchi [at] gmail [dot] com
 * 			emanuele.dona [at] gmail [dot] com
 */

package loginserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.rmi.activation.*;
import java.lang.*;
import java.security.*;
import javax.rmi.ssl.*;

import io.Logger;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServer;
import parcmanserver.RemoteParcmanServerUser;
import parcmanclient.*;
import database.beans.UserBean;
import database.exceptions.ParcmanDBUserInvalidStatusException;
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
	 * Logger 
	 */
	private Logger logger;

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
		super(id, 38990, 
			((LoginServerAtDate)(atDate.get())).getClientSocketFactory(),
			new SslRMIServerSocketFactory());


		this.logger = Logger.getLogger("server-side");

		// Ricavo l'ActivationSystem
		ActivationSystem actSystem = ActivationGroup.getSystem();
		// Ricavo l'ActivationDesc dall'ActivationSystem
		ActivationDesc actDesc = actSystem.getActivationDesc(id);

		logger.info("Inizializzo il LoginServer.");

		// Ricavo dall'atDate i dati della sessione
		LoginServerAtDate onAtDate = (LoginServerAtDate)(atDate.get());
		this.parcmanServerStub = onAtDate.getParcmanServerStub();
		this.dBServerStub = onAtDate.getDBServerStub();
		this.activationsCount = onAtDate.getActivationsCount();
		RMIClientSocketFactory csf = onAtDate.getClientSocketFactory();
		logger.info("Ho ripristinato e aggiornato i dati di sessione.");


		// Creo un nuovo LoginServerAtDate con i dati di sessione aggiornati
		LoginServerAtDate newAtDate = new LoginServerAtDate(this.activationsCount+1, this.parcmanServerStub, this.dBServerStub, csf);
		logger.info("Ho creato un nuovo LoginServerAtDate.");
		ActivationDesc newActDesc = new ActivationDesc(actDesc.getGroupID(), actDesc.getClassName(), actDesc.getLocation(), new MarshalledObject(newAtDate));
		logger.info("Ho creato un nuovo activation descriptop.");
		actDesc = actSystem.setActivationDesc(id, newActDesc);
		logger.info("Ho impostato il nuovo activation descriptor.");
	}

	/**
	 * Esegue il login di un Client alla rete Parcman.
	 * Ritorna un MobileServer esportato nel caso in cui il login abbia successo.
	 * 
	 * @param name Nome utente
	 * @param password Password utente
	 * @return Un MobileServer di tipo ParcmanClient se il login ha successo, null altrimenti.
	 * @throws RemoteException Eccezione Remota.
	 * @throws LoginServerUserInBlacklistRemoteException utente in blacklist.
	 * @throws LoginServerUserFailedRemoteException username non riconosciuto.
	 * @throws LoginServerUserOrPasswordFailedRemoteException username o password non validi.
	 * @throws LoginServerUserPrivilegeFailedRemoteException privilegi utente non validi.
	 * @throws LoginServerUserIsConnectRemoteException utente gia` connesso.
	 * @throws LoginServerClientHostUnreachableRemoteException client host irraggiungibile.
	 */
	public RemoteParcmanClient login(String name, String password) throws
		LoginServerUserInBlacklistRemoteException,
		LoginServerUserFailedRemoteException,
		LoginServerUserOrPasswordFailedRemoteException,
		LoginServerUserPrivilegeFailedRemoteException, 
		LoginServerUserIsConnectRemoteException,
		LoginServerClientHostUnreachableRemoteException,
		RemoteException
	{
		try
		{
			logger.info("E' stata ricevuta una richiesta di login dall'host " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			logger.error("E' stata ricevuta una richiesta di login ma l'Host risulta irraggiungibile.");
			throw new LoginServerClientHostUnreachableRemoteException();
		}

		UserBean user = null;

		try
		{
			user = this.dBServerStub.getUser(name);
		}
		catch(ServerException e)
		{
			if (e.getCause() instanceof ParcmanDBServerUserNotExistRemoteException)
			{
				logger.error("Richiesta rifiutata, nome utente errato.");
				throw new LoginServerUserFailedRemoteException();
			}
			logger.error("Richiesta rifiutata, " + e.getMessage());
		}

		// cripto la password
		String encryptedPassword = PasswordService.getInstance().encrypt(password);

		if (user == null || !(encryptedPassword.equals(user.getPassword())))
		{
			System.out.println(user.getName());
			logger.error("Richiesta rifiutata, password o nome utente errati.");
			throw new LoginServerUserOrPasswordFailedRemoteException();
		}

		if ("WAITING".equals(user.getStatus()))
		{
			logger.error("Richiesta rifiutata, utente non autorizzato.");
			throw new LoginServerUserNotAuthorizedRemoteException();
		}

		if ("BLACKLIST".equals(user.getStatus()))
		{
			logger.error("Richiesta rifiutata, utente in blacklist.");
			throw new LoginServerUserInBlacklistRemoteException();
		}

		if (!"READY".equals(user.getStatus()))
		{
			logger.error("Richiesta rifiutata, status utente non riconosciuto.");
			throw new LoginServerUserInvalidStatusRemoteException();
		}

		RemoteParcmanClient parcmanClient = null;

		// Creo un'istanza di ParcmanClient da passare al Client
		if (user.getPrivilege().equals(Privilege.getUserPrivilege())) // Utente
			parcmanClient = new ParcmanClient(((RemoteParcmanServerUser)this.parcmanServerStub), user.getName(), false);
		else if (user.getPrivilege().equals(Privilege.getAdminPrivilege()))
			parcmanClient = new ParcmanClient(((RemoteParcmanServerUser)this.parcmanServerStub), user.getName(), true);
		else
		{
			logger.error("Privilegi dell'utente " + user.getName() + " errati (" + user.getPrivilege() + ")");
			throw new LoginServerUserPrivilegeFailedRemoteException();
		}


		// Deesporto il server appena creato
		unexportObject(parcmanClient, true);

		try
		{
			// Aggiungo il Client alla lista di attemp del Parcmanserver
			parcmanServerStub.connectAttemp(name, this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			logger.error("Errore di rete, ClientHost irraggiungibile.");
			throw new LoginServerClientHostUnreachableRemoteException();
		}
		catch(RemoteException e)
		{
			if (e.getCause() instanceof ParcmanServerUserIsConnectRemoteException)
			{
				logger.error("Richiesta rifiutata, Utente gia' connesso.");
				throw new LoginServerUserIsConnectRemoteException(e.getMessage());
			}
			else
			{
				logger.error("Errore interno del ParcmanServer.");
				throw e;
			}
		}

		logger.info("Richiesta accettata, e' stato inviato il ParcmanClient.");

		return parcmanClient;
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
			logger.info("E' stata ricevuta una richiesta di creazione account da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			logger.error("E' stata ricevuta una richiesta di creazione account ma l'Host risulta irraggiungibile.");
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
			user.setStatus("WAITING");
		} 
		catch (ParcmanDBUserInvalidStatusException e)
		{
			logger.error("Status non valido.");
		}

		try
		{
			this.dBServerStub.addUser(user);
		}
		catch(ParcmanDBServerUserExistRemoteException e)
		{
			logger.error("Richiesta rifiutata, il nome utente fornito e' gia' in uso.");
			throw new ParcmanDBServerUserExistRemoteException();
		}
		catch(ParcmanDBServerUserNotValidRemoteException e)
		{
			logger.error("Richiesta rifiutata, i dati forniti non sono validi.");
			throw new ParcmanDBServerUserNotValidRemoteException();
		}
		catch(RemoteException e)
		{
			logger.error("Impossibile soddisfare la richiesta.");
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
			logger.info("E' stata ricevuta una richiesta di ping da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			logger.error("Errore di rete, ClientHost irraggiungibile.");
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
			logger.info("Disattivazione del LoginServer in corso");
			// Rendo inattivo il LoginServer
			this.inactive(getID());

			// Invoco il Garbage Collector
			System.gc();

			logger.info("Disattivazione avvenuta con successo");
		}
		catch (Exception e)
		{
			logger.error("Impossibile disattivare il LoginServer");
			System.out.close();
		}
	}
}

