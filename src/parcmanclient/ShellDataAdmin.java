package parcmanclient;

import java.util.Vector;
import java.util.LinkedHashMap;
import java.lang.*;
import java.io.*;
import java.rmi.*;

import io.IOColor;
import io.IO;
import pshell.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServerUser;
import parcmanserver.ClientDataUser;
import database.beans.ShareBean;
import database.beans.SearchBean;
import database.beans.UserBean;

public class ShellDataAdmin extends ShellData
{
	/**
	 * Utility di input/output
	 */
	private IO io;

	/**
	 * Stub del ParcmanServer.
	 */
	//private RemoteParcmanServerUser parcmanServerStub;

	/**
	 * Nome utente.
	 */
	//private String userName;

	/**
	 * ParcmanClient.
	 */
	//private ParcmanClient parcmanClient;

	/**
	 * Costruttore.
	 *
	 * @param parcmanServerStub Stub del MainServer della rete Parcman
	 * @param parcmanClient Referenza al ParcmanClient
	 * @param userName Nome utente
	 */
	public ShellDataAdmin(RemoteParcmanServerUser parcmanServerStub,
			ParcmanClient parcmanClient,
			String userName)
	{
		super(parcmanServerStub, parcmanClient, userName);
		this.io = getIO();
	}

	/**
	 * Metodo per il comando di shell getConnectUsersList.
	 *
	 * @param param Stringa dei parametri per il comando
	 */
	@PShellDataAnnotation(
			method = "commandGetConnectUsersList",
			name = "connectusers",
			info = "Fornisce la lista dei client connessi alla rete parcman",
			help = "\tFornisce la lista dei client connessi alla rete parcman.\n\tuse: connectusers"
			)
	public void commandGetConnectUsersList(String param)
	{
		try
		{
			logger.debug(REQUEST_REMOTE_SENT);
			Vector<ClientDataUser> result = parcmanServerStub.getConnectUsersList(this.parcmanClient.getStub(), this.userName);
			logger.debug(REQUEST_DONE);

			if (result == null || result.size() == 0)
			{
				println("Nessun utente connesso.");
				return;
			}
			else
			{
				LinkedHashMap<String, Vector<String>> table = new LinkedHashMap<String, Vector<String>>();
				Vector<String> names = new Vector<String>();
				Vector<String> isAdmin = new Vector<String>();
				Vector<String> hosts= new Vector<String>();

 				for (int i=0; i<result.size(); i++)
				{
					ClientDataUser data = result.get(i);
					names.add(data.getName());
					isAdmin.add(Boolean.toString(data.isAdmin()));
					hosts.add(data.getHost());
				}

				table.put("NOME UTENTE", names);
				table.put("IS ADMIN", isAdmin);
				table.put("ADDRESS", hosts);

				io.printTable(table, "Utenti connessi");
			}
		}
		catch (RemoteException e)
		{
			logger.debug(REQUEST_FAILED);
			logger.error(NETWORK_ERROR);
			error(NETWORK_ERROR);
			return;
		}
	}

	/**
	 * Metodo per il comando di shell enableAgentSystem.
	 *
	 */
	@PShellDataAnnotation(
			method = "commandEnableAgentSystem",
			name = "enableagents",
			info = "Attiva il sistema di gestione degli agenti remoti.",
			help = "\tAttiva il sistema di gestione degli agenti remoti.\n\tuse: enableagents"
			)
	public void commandEnableAgentSystem(String param)
	{
		try
		{
			logger.debug(REQUEST_REMOTE_SENT);
			parcmanServerStub.setAgentSystemStatus(parcmanClient.getStub(), userName, true);
			logger.debug(REQUEST_DONE);
			println("Sistema degli agenti remoti attivato.");
		}
		catch (RemoteException e)
		{
			logger.debug(REQUEST_FAILED);
			logger.error(NETWORK_ERROR);
			error(NETWORK_ERROR);
			return;
		}
	}

	/**
	 * Metodo per il comando di shell disableAgentSystem.
	 *
	 */
	@PShellDataAnnotation(
			method = "commandDisableAgentSystem",
			name = "disableagents",
			info = "Disattiva il sistema di gestione degli agenti remoti.",
			help = "\tDisattiva il sistema di gestione degli agenti remoti.\n\tuse: disableagents"
			)
	public void commandDisableAgentSystem(String param)
	{
		try
		{
			logger.debug(REQUEST_REMOTE_SENT);
			parcmanServerStub.setAgentSystemStatus(parcmanClient.getStub(), userName, false);
			logger.debug(REQUEST_DONE);
			println("Sistema degli agenti remoti disattivato.");
		}
		catch (RemoteException e)
		{
			logger.debug(REQUEST_FAILED);
			logger.error(NETWORK_ERROR);
			error(NETWORK_ERROR);
			return;
		}
	}

	/**
	 * Metodo per il comando di shell addToBlacklist.
	 *
	 */
	@PShellDataAnnotation(
			method = "commandAddToBlacklist",
			name = "addtoblacklist",
			info = "Aggiunge un utente alla blacklist.",
			help = "\tAggiunge un utente alla blacklist.\n\tuse: addtoblacklist username"
			)
	public void commandAddToBlacklist(String param)
	{
		try
		{
			logger.debug(REQUEST_REMOTE_SENT);
			parcmanServerStub.addToBlacklist(parcmanClient.getStub(), userName, param);
			logger.debug(REQUEST_DONE);
			println("Aggiunto utente '" + param + "' alla blacklist.");
		}
		catch (RemoteException e)
		{
			logger.debug(REQUEST_FAILED);
			logger.error(NETWORK_ERROR);
			error(NETWORK_ERROR);
			return;
		}
	}

	/**
	 * Metodo per il comando di shell delFromBlacklist.
	 *
	 */
	@PShellDataAnnotation(
			method = "commandDelFromBlacklist",
			name = "delfromblacklist",
			info = "Toglie un utente dalla blacklist.",
			help = "\tToglie un utente dalla blacklist.\n\tuse: delfromblacklist username"
			)
	public void commandDelFromBlacklist(String param)
	{
		try
		{
			logger.debug(REQUEST_REMOTE_SENT);
			parcmanServerStub.delFromBlacklist(parcmanClient.getStub(), userName, param);
			logger.debug(REQUEST_DONE);
			println("Rimosso utente '" + param + "' alla blacklist.");
		}
		catch (RemoteException e)
		{
			logger.debug(REQUEST_FAILED);
			logger.error(NETWORK_ERROR);
			error(NETWORK_ERROR);
			return;
		}
	}

 	/**
 	 * Metodo per il comando di shell blacklist.
 	 *
 	 */
 	@PShellDataAnnotation(
 			method = "commandBlacklist",
 			name = "blacklist",
 			info = "Mostra la lista blacklist.",
 			help = "\tMosta la lista blacklist.\n\tuse: blacklist"
 			)
 	public void commandBlacklist(String param)
 	{
 		try
 		{
			logger.debug(REQUEST_REMOTE_SENT);
 			Vector<UserBean> result = parcmanServerStub.blacklist(parcmanClient.getStub(), userName);
			logger.debug(REQUEST_DONE);

 			if (result == null || result.size() == 0)
 			{
 				println("Nessun utente in blacklist.");
 			}
 			else 
 			{
				LinkedHashMap<String, Vector<String>> table = new LinkedHashMap<String, Vector<String>>();
				Vector<String> names = new Vector<String>();
				Vector<String> privileges = new Vector<String>();

 				for (int i=0; i<result.size(); i++)
				{
					UserBean bean = result.get(i);
					names.add(bean.getName());
					privileges.add(bean.getPrivilege());
				}

				table.put("NOME UTENTE", names);
				table.put("PRIVILEGI", privileges);

				io.printTable(table, "Waiting list");
 			}
 		}
 		catch (RemoteException e)
 		{
			logger.debug(REQUEST_FAILED);
			logger.error(NETWORK_ERROR);
			error(NETWORK_ERROR);
			return;
 		}
 	}
 
 	/**
 	 * Metodo per il comando di shell waitinglist.
 	 *
 	 */
 	@PShellDataAnnotation(
 			method = "commandWaitinglist",
 			name = "waitinglist",
 			info = "Mostra la lista waiting.",
 			help = "\tMosta la lista waiting.\n\tuse: waitinglist"
 			)
 	public void commandWaitinglist(String param)
 	{
 		try
 		{
			logger.debug(REQUEST_REMOTE_SENT);
 			Vector<UserBean> result = parcmanServerStub.waitinglist(parcmanClient.getStub(), userName);
			logger.debug(REQUEST_DONE);

 			if (result == null || result.size() == 0)
 			{
 				io.println("Nessun utente in waiting list.");
 			}
 			else 
 			{
				LinkedHashMap<String, Vector<String>> table = new LinkedHashMap<String, Vector<String>>();
				Vector<String> names = new Vector<String>();
				Vector<String> privileges = new Vector<String>();

 				for (int i=0; i<result.size(); i++)
				{
					UserBean bean = result.get(i);
					names.add(bean.getName());
					privileges.add(bean.getPrivilege());
				}

				table.put("NOME UTENTE", names);
				table.put("PRIVILEGI", privileges);

				io.printTable(table, "Waiting list");
 			}
 		}
 		catch (RemoteException e)
 		{
			logger.debug(REQUEST_FAILED);
			logger.error(NETWORK_ERROR);
			error(NETWORK_ERROR);
			return;
 		}
 	}

 	/**
 	 * Metodo per il comando di shell delfromwaiting.
 	 *
 	 */
 	@PShellDataAnnotation(
 			method = "commandDelFromWaiting",
 			name = "delfromwaiting",
 			info = "Toglie un utente dalla lista di waiting e lo attiva.",
 			help = "\tToglie un utente dalla lista di waiting e lo attiva.\n\tuse: delfromwaiting"
 			)
 	public void commandDelFromWaiting(String param)
 	{
		try
		{
			logger.debug(REQUEST_REMOTE_SENT);
			parcmanServerStub.delFromWaiting(parcmanClient.getStub(), userName, param);
			logger.debug(REQUEST_DONE);
			println("Rimosso utente '" + param + "' dalla lista di waiting.");
		}
		catch (RemoteException e)
		{
			logger.debug(REQUEST_FAILED);
			logger.error(NETWORK_ERROR);
			error(NETWORK_ERROR);
			return;
		}
 	}
}
