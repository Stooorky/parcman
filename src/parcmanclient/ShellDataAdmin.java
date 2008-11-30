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

}
