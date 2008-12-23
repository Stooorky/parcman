package parcmanclient;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.System;

import pshell.*;
import io.Logger;
import io.PropertyManager;
import io.IO;
import io.IOColor;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServerUser;
import database.beans.ShareBean;
import database.beans.SearchBean;

public class ShellData extends PShellData
{
	/**
	 * Stub del ParcmanServer.
	 */
	protected RemoteParcmanServerUser parcmanServerStub;

	/**
	 * Nome utente.
	 */
	protected String userName;

	/**
	 * ParcmanClient.
	 */
	protected ParcmanClient parcmanClient;

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
		super(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out));

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
			io.println("Il MainServer della rete Parcman ha risposto con successo");
		}
		catch (RemoteException e)
		{
			io.println("Impossibile eseguire il ping al MainServer", IOColor.RED);
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
				io.println("Nessun file condiviso.");
			else
			{
				io.println("Lista dei file condivisi:");
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
			io.print("Inviata la richiesta di ricerca, attendere... ");
			Vector<SearchBean> result = parcmanServerStub.search(this.parcmanClient.getStub(), this.userName, param);
			io.println("done.");

			if (result == null || result.size() == 0)
			{
				io.println("La ricerca non ha prodotto risultati.");
				return;
			}

			io.println("Risultato della ricerca:");
			for (int i=0; i<result.size(); i++)
				this.writeSearch(result.elementAt(i));

		}
		catch (RemoteException e)
		{
			io.println("Fallito. Si sono verificati degli errori di rete. Ritenta.", IOColor.RED);
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
		io.print("Avvio la scansione della Directory dei file condivisi... ");
		this.parcmanClient.scanSharingDirectory();
		io.println("Done.");
	}

	/**
	 * Stampa i dati di un file.
	 *
	 * @param index Indice del file
	 * @param share ShareBean del file
	 */
	private void writeShare(int index, ShareBean share)
	{
		io.println(index + ") " + share.getName() +
				"\n\tID: " + share.getId() +
				"\n\tURL: " + share.getUrl() +
				"\n\tProprietario: " + share.getOwner() +
				"\n\tHash: " + share.getHash() +
				"\n\tKeywords: " + share.getKeywordsToString());
	}

	/**
	 * Stampa i dati di un SearchBean.
	 *
	 * @param search SearchBean del file
	 */
	private void writeSearch(SearchBean search)
	{
		io.println("ID:" + search.getId() +
				" Proprietario: " + search.getOwner() +
				"\n\tNome: " + search.getName() +
				" Keywords: " + search.getKeywordsToString());
	}

	/**
	 * Metodo per il comando scaninfo
	 *
	 * @param param Stringa dei parametri per il comando
	 */
	@PShellDataAnnotation(
			method = "commandScanInfo",
			name = "scaninfo",
			info = "Restituisce informazioni sullo scan dei file condivisi",
			help = "\tRestituisce informazioni sullo scan dei file condivisi.\n\tuse: scaninfo"
			)
	public void commandScanInfo (String param)
	{
		Date date = new Date(this.parcmanClient.getScanDirectoryTimerTask().scheduledExecutionTime());
		io.println("Ultima scansione effettuata: " + date.toString());
	}

	/**
	 * Stampa il prompt della shell.
	 */
	public void writePrompt()
	{
		io.print(this.userName + ">> ", IOColor.GREEN);
	}

	/**
	 * Metodo per il comando di shell get.
	 *
	 * @param param Stringa dei parametri per il comando
	 */
	@PShellDataAnnotation(
			method = "commandGetFile",
			name = "get",
			info = "Esegue il download di un file dalla rete parcman",
			help = "\tEsegue il download di un file dalla rete parcman.\n\tuse: get <owner>@<ID>"
			)
	public void commandGetFile(String param)
	{
		String[] params = new String[2];		
		try
		{
			params = param.split("/");
		}
		catch (Exception e)
		{
			io.println("Fallito. '" + param + "' non e` valido.", IOColor.RED);
			return; 
		}

		io.println("Inviata la richiesta di download, attendere...");
		io.print("Download of file with ID '" + params[1] + "' from user '" + params[0] + "' in corso.");

		Timer timer = new Timer();
		timer.schedule(new DownloadTimerTask(io, this.parcmanServerStub, this.parcmanClient, this.userName, params), 0);
	}

}

class DownloadTimerTask extends TimerTask
{
	private RemoteParcmanServerUser server;
	private ParcmanClient client;
	private String username;
	private String[] data;
	private IO io;

	public DownloadTimerTask(IO io, RemoteParcmanServerUser server, ParcmanClient client, String username, String[] data)
	{
		this.io= io;
		this.server = server;
		this.client = client;
		this.username = username;
		this.data = data;
	}

	public void run()
	{
		try 
		{
			DownloadData downData = server.startDownload(client.getStub(), username, data);
			io.println("Richiesta di download inviata al server...");
			RemoteParcmanClientUser remoteClient = downData.getStub();
			byte[] bytes = remoteClient.getFile(downData.getShareBean().getId());
			saveFile(downData.getShareBean().getName(), client.getSharingDirectory(), bytes);
			io.println("Download terminato con successo.");

		}
		catch (ParcmanServerRequestErrorRemoteException e)
		{
			io.println("Download fallito. " + e.getCause().getMessage(), IOColor.RED);
		}
		catch (RemoteException e)
		{
			io.println("Download fallito. Si sono verificati degli errori di rete. Ritenta.", IOColor.RED);
		}
		catch (FileNotFoundException e)
		{
			io.println("Download fallito. Non e` stato possibile creare il file. Constrollare di avere i persmessi necessari sulla propria cartella di condivisione.", IOColor.RED);
		}
		catch (IOException e)
		{
			io.println("Download fallito. Si e` verificato un errore durante la fase di scrittura su disco.", IOColor.RED);
		}
	}

	private void saveFile(String filename, String pathToSave, byte[] bytes) throws 
		FileNotFoundException, 
		IOException
	{
		File f = new File(makeFileName(filename, pathToSave));
		FileOutputStream fout = new FileOutputStream(f);
		fout.write(bytes, 0, bytes.length);
		fout.close();
	}

	private String makeFileName(String filename, String pathToSearch)
	{
		File dir = new File(pathToSearch);
		String[] files = dir.list();
		int count=0; 
		Pattern pattern = Pattern.compile("("+filename+")"+"(__)?([0-9]+)?");

		for (int i=0; i<files.length; i++)
		{
			Matcher matcher = pattern.matcher(files[i]);
			if (matcher.matches())
				count++;
		}
		if (count > 0)
			filename = filename+"__"+count;
		filename = pathToSearch+"/"+filename;
		return filename;
	}
}
