package parcmanclient;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.rmi.*;

import pshell.*;
import plog.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServerUser;
import database.beans.ShareBean;
import database.beans.SearchBean;
import database.beans.UserBean;

public class ShellDataAdmin extends ShellData
{
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
	public void commandGetConnectUsersList (String param)
	{
		try
		{
			out.print("Inviata la richiesta...");
			Vector<String> result = parcmanServerStub.getConnectUsersList(this.parcmanClient.getStub(), this.userName);
			out.print("done.\n");

			if (result == null || result.size() == 0)
			{
				out.println("Nessun utente connesso.");
				return;
			}

			out.println("Lista degli utenti connessi:");
			for (int i=0; i<result.size(); i++)
				this.out.println(result.get(i));

		}
		catch (RemoteException e)
		{
			out.println("Fallito. Si sono verificati degli errori di rete. Ritenta.");
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
			out.print("Inviata la richiesta...");
			parcmanServerStub.setAgentSystemStatus(parcmanClient.getStub(), userName, true);
			out.println("Sistema degli agenti remoti attivato.");
		}
		catch (RemoteException e)
		{
			out.println("Fallito. Si sono verificati degli errori di rete. Ritenta.");
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
			out.print("Inviata la richiesta...");
			parcmanServerStub.setAgentSystemStatus(parcmanClient.getStub(), userName, false);
			out.println("Sistema degli agenti remoti disattivato.");
		}
		catch (RemoteException e)
		{
			out.println("Fallito. Si sono verificati degli errori di rete. Ritenta.");
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
			out.print("Inviata la richiesta...");
			parcmanServerStub.addToBlacklist(parcmanClient.getStub(), userName, param);
			out.println("Aggiunto utente '" + param + "' alla blacklist.");
		}
		catch (RemoteException e)
		{
			out.println("Fallito. Si sono verificati degli errori di rete. Ritenta.");
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
			out.println("Inviata la richiesta...");
			parcmanServerStub.delFromBlacklist(parcmanClient.getStub(), userName, param);
			out.println("Rimosso utente '" + param + "' alla blacklist.");
		}
		catch (RemoteException e)
		{
			out.println("Fallito. Si sono verificati degli errori di rete. Ritenta.");
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
 			out.print("Inviata la richiesta...");
 			Vector<UserBean> result = parcmanServerStub.blacklist(parcmanClient.getStub(), userName);
 			out.println("Done.");
 
			Vector<String> names = new Vector<String>();
			Vector<String> privileges = new Vector<String>();
			names.add("NOME UTENTE");
			privileges.add("PRIVILEGI");
			String header = "Blacklist:";

 			if (result == null || result.size() == 0)
 			{
 				out.println("Nessun utente in blacklist.");
 			}
 			else 
 			{
 				for (int i=0; i<result.size(); i++)
				{
					UserBean bean = result.get(i);
					names.add(bean.getName());
					privileges.add(bean.getPrivilege());
				}
				Vector<Vector<String>> table = new Vector<Vector<String>>();
				table.add(names);
				table.add(privileges);
				writeTable(table, 2, result.size()+1, header);
 			}
 		}
 		catch (RemoteException e)
 		{
 			out.println("Fallito. Si sono verificati degli errori di rete. Ritenta.");
 		}
 	}
 
 


}
