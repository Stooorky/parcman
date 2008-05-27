package parcmanclient;

import java.lang.*;
import java.io.*;
import java.rmi.*;

import pshell.*;
import plog.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServerUser;

public class ShellData extends PShellData
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
	* Stub del ParcmanClient.
	*/
	private RemoteParcmanClient parcmanClientStub;

	/**
	* Costruttore.
	*
	* @param parcmanServerStub Stub del MainServer della rete Parcman
	* @param userName Nome utente
	*/
	public ShellData(RemoteParcmanServerUser parcmanServerStub, RemoteParcmanClient parcmanClientStub, String userName)
	{
		super(System.out, new BufferedReader(new InputStreamReader(System.in)));

		this.parcmanServerStub = parcmanServerStub;
		this.parcmanClientStub = parcmanClientStub;
		this.userName = userName;
	}

	/**
	* Metodo per il comando di shell ping.
	*
	* @param param Stringa dei parametri per il comando
	*/
	@PShellDataAnnotation(
		method = "commandPing",
		name = "ping",
		info = "Esegue un ping sul main server della rete Parcman",
		help = "\tEsegue un ping sul main server della rete Parcman.\n\tuse: ping"
	)
	public void commandPing (String param)
	{
		try
		{
			parcmanServerStub.ping();
			out.println("Il MainServer della rete Parcman ha risposto con successo");
		}
		catch (RemoteException e)
		{
			out.println("Impossibile eseguire il ping al MainServer");
			return;
		}
	}

	/**
	* Metodo per il comando di shell exit.
	*
	* @param param Stringa dei parametri per il comando
	*/
	@PShellDataAnnotation(
		method = "commandExit",
		name = "exit",
		info = "Esegue la disconnessione dalla rete",
		help = "\tEsegue la disconnessione dalla rete Parcman.\n\tuse: exit"
	)
	public void commandExit (String param)
	{
		parcmanServerStub = null;

		try
		{
			parcmanClientStub.exit();
		}
		catch (RemoteException e)
		{
			PLog.err(e, "ShellData.commandPing", "Impossibile eseguire la disconnessione dalla rete.");
			return;
		}
	}

	/**
	* Stampa il prompt della shell.
	*/
	public void writePrompt()
	{
        out.print(this.userName + "> ");
	}
}
