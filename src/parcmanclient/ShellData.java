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
	private ParcmanClient parcmanClient;

	/**
	* Costruttore.
	*
	* @param parcmanServerStub Stub del MainServer della rete Parcman
    * @param parcmanClient Referenza al ParcmanClient
	* @param userName Nome utente
	*/
	public ShellData(RemoteParcmanServerUser parcmanServerStub,
            ParcmanClient parcmanClient,
            String userName)
	{
		super(System.out, new BufferedReader(new InputStreamReader(System.in)));

		this.parcmanServerStub = parcmanServerStub;
		this.parcmanClient = parcmanClient;
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
		this.parcmanServerStub = null;
		this.parcmanClient.exit();
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
        Vector<ShareBean> shares = this.parcmanClient.getShares();

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
	* Metodo per il comando di shell sharelist.
	*
	* @param param Stringa dei parametri per il comando
	*/
	@PShellDataAnnotation(
		method = "commandSearchFile",
		name = "search",
		info = "Esegue la ricerca di un file sulla rete parcman",
		help = "\tEsegue la ricerca di un file sulla rete parcman.\n\tuse: search <keywords>"
	)
	public void commandSearchFile (String param)
	{
		try
		{
			out.print("Inviata la richiesta di ricerca, attendere...");
			Vector<SearchBean> result = parcmanServerStub.search(this.parcmanClient.getStub(), this.userName, param);
			out.println("done.");

			if (result.size() == 0)
			{
				out.println("La ricerca non ha prodotto risultati.");
				return;
			}

			out.println("Risultato della ricerca:");
			for (int i=0; i<result.size(); i++)
				this.writeSearch(result.elementAt(i));
			
		}
		catch (RemoteException e)
		{
			out.println("Fallito. Si sono verificati degli errori di rete. Ritenta.");
			return;
		}
	}

	/**
	* Metodo per il comando di scancollection
	*
	* @param param Stringa dei parametri per il comando
	*/
	@PShellDataAnnotation(
		method = "commandScanCollection",
		name = "scancollection",
		info = "Esegue la scansione della cartella dei file condivisi",
		help = "\tEsegue la scansione della cartella dei file condivisi.\n\tuse: scancollection"
	)
	public void commandScanCollection (String param)
	{
        out.println("Avvio la scansione della Directory dei file condivisi...");
        this.parcmanClient.scanSharingDirectory();
        out.print("Done.");
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
	* Stampa i dati di un SearchBean.
	*
	* @param share SearchBean del file
	*/
	private void writeSearch(SearchBean search)
	{
		out.println("ID:" + search.getId() +
			" Proprietario: " + search.getOwner() +
			"\n\tNome: " + search.getName() +
			" Keywords: " + search.getKeywordsToString());
	}

	/**
	* Stampa il prompt della shell.
	*/
	public void writePrompt()
	{
        out.print(this.userName + "> ");
	}
}
