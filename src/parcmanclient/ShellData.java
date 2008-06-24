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
    * Vettore dei file condivisi. 
    */
    private Vector<ShareBean> shares;

	/**
	* Costruttore.
	*
	* @param parcmanServerStub Stub del MainServer della rete Parcman
	* @param userName Nome utente
	*/
	public ShellData(RemoteParcmanServerUser parcmanServerStub,
            RemoteParcmanClient parcmanClientStub,
            String userName,
            Vector<ShareBean> shares)
	{
		super(System.out, new BufferedReader(new InputStreamReader(System.in)));

		this.parcmanServerStub = parcmanServerStub;
		this.parcmanClientStub = parcmanClientStub;
		this.userName = userName;
        this.shares = shares;
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
		this.parcmanServerStub = null;

		try
		{
			this.parcmanClientStub.exit();
		}
		catch (RemoteException e)
		{
			PLog.err(e, "ShellData.commandPing", "Impossibile eseguire la disconnessione dalla rete.");
			return;
		}
	}

	/**
	* Metodo per il comando di shell sharelist.
	*
	* @param param Stringa dei parametri per il comando
	*/
	@PShellDataAnnotation(
		method = "commandShareList",
		name = "sharelist",
		info = "Fornisce la lista dei propri file condivisi",
		help = "\tFornisce la lista dei propri file condivisi.\n\tuse: sharelist"
	)
	public void commandShareList (String param)
	{
        try
        {
            if (shares.size()==0)
                out.println("Nessun file condiviso.");
            else
            {
                out.println("Lista dei file condivisi:");
                for (int i=0; i<shares.size(); i++)
                    this.writeShare(i+1, shares.elementAt(i));
            }
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return;
        }
	}

    /**
     * Stampa i dati di un file.
     *
     * @param index Indice del file
     * @param share ShareBean del file
     */
    private void writeShare(int index, ShareBean share)
    {
        out.println(index + ") " + share.getName() +
            "\n\tID: " + share.getId() +
            "\n\tURL: " + share.getUrl() +
            "\n\tProprietario: " + share.getOwner() +
            "\n\tHash: " + share.getHash() +
            "\n\tKeywords: " + share.getKeywordsToString());
    }

	/**
	* Stampa il prompt della shell.
	*/
	public void writePrompt()
	{
        out.print(this.userName + "> ");
	}
}
