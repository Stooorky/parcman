package database;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import plog.*;
import database.beans.ShareBean;
import database.xmlhandlers.ShareContentHandler;
import database.exceptions.*;

/**
 * Gestore DataBase Shared files.
 *
 * @author Parcman Tm
 */
public class DBSharings
	implements DBFile
{
	/**
	 * Path del file XML.
	 */
	private String dbFile;

	/**
	 * Lista dei file presenti nel db.
	 */
	private Vector<ShareBean> sharings;

	/**
	 * Costruttore.
	 *
	 * @param dbFile File XML del DataBase Sharings
	 */
	public DBSharings(String dbFile)
	{
		this.dbFile = dbFile;
		this.sharings= new Vector<ShareBean>();
	}

	/**
	 * Esegue il salvataggio del DataBase Sharings su File.
	 */
	public void save() 
	{
		File f = new File(dbFile);
		FileOutputStream fos;

		try
		{
			fos = new FileOutputStream(f);
		}
		catch(FileNotFoundException e)
		{
			PLog.err(e, "DBSharings.save", "Errore durante il salvataggio del DB dei file condivisi.");
			return;
		}

		PrintStream ps = new PrintStream(fos);

		// DTD
		ps.println("<?xml version=\"1.0\"?>");
		ps.println("<!DOCTYPE DB_SHARINGS [");
		ps.println("<!ELEMENT DB_SHARINGS (FILE)*>");
		ps.println("<!ELEMENT FILE (ID, URL, OWNER, HASH, KEYWORD)>");
		ps.println("<!ELEMENT ID (#PCDATA)>");
		ps.println("<!ELEMENT URL (#PCDATA)>");
		ps.println("<!ELEMENT OWNER (#PCDATA)>");
		ps.println("<!ELEMENT HASH (#PCDATA)>");
		ps.println("<!ELEMENT KEYWORD (#PCDATA)>");
		ps.println("]>");

		// XML
		ps.println("<DB_SHARINGS>");

		for (int i=0; i<this.getSize(); i++)
		{
			ShareBean s = this.sharings.get(i);
			ps.println("<FILE>");
			ps.println("<ID>" + s.getId() + "</ID>");
			ps.println("<URL>" + s.getUrl() + "</URL>");
			ps.println("<OWNER>" + s.getOwner() + "</OWNER>");
			ps.println("<HASH>" + s.getHash() + "</HASH>");
			ps.println("<KEYWORDS>" + s.getKeywordsToString() + "</KEYWORDS>");
			ps.println("</FILE>");
		}

		ps.println("</DB_SHARINGS>");
	}

	/**
	 * Popola il vettore sharings caricando i dati da dbFile.
	 */
	public void load()
	{
		XMLReader parser;
		try 
		{
			parser = XMLReaderFactory.createXMLReader();
			this.sharings.clear();
			parser.setContentHandler(new ShareContentHandler(this.sharings));
			parser.parse(dbFile);
		}
		catch (SAXException e)
		{
			PLog.err(e, "DBUsers.load", "Errore durante il parsing del database dei file condivisi.");
		}
		catch (IOException e)
		{
			PLog.err(e, "DBUsers.load", "Impossibile eseguire il parsing del database dei file condivisi. File non leggibile.");
		}
	}

	/**
	 * Aggiunge un file al database dei file condivisi.
	 * Se il file e` gia` presente solleva l'eccezione 
	 * ParcmanDBSharedExistException.
	 *
	 * @param share Bean Share
	 * @throws ParcmanDBShareExistException Il file specificato e' gia' presente all'interno del database
	 */
	public void addShare(ShareBean share) throws
		ParcmanDBShareExistException
	{
		// Controllo che il file non sia gia' presente nel DataBase
		Iterator i = this.sharings.iterator();
		while (i.hasNext()) 
		{
			if (((ShareBean) i.next()).equals(share))
			{
				throw new ParcmanDBShareExistException();
			}
		}
		
		this.sharings.add(share);
	}

	/**
	 * Restituisce i dati di un file condiviso a partire dal nome del file.
	 * Se il file non e` presente viene sollevata l'eccezione
	 * ParcmanDBShareNotExistException
	 * 
	 * @param name Stringa che rappresenta il nome del file
	 * @return ShareBean che contiene i dati del file
	 * @throws ParcmanDBShareNotExistException
	 */
	public ShareBean getShareByName(String name)
		throws ParcmanDBShareNotExistException
	{
		Iterator i = this.sharings.iterator();
		while (i.hasNext())
		{
			ShareBean s = (ShareBean) i.next();
			if (s.getName().equals(name))
			{
				return s;
			}
		}
		throw new ParcmanDBShareNotExistException();
	}

	/**
	 * Restituisce i dati di un file condiviso a partire dall'id
	 * ad esso e` associato.
	 * In caso contrario viene sollevata l'eccezione
	 * ParcmanDBShareNotExistException
	 *
	 * @param id Stringa che rappresenta un id
	 * @return ShareBean che contiene i dati del file
	 * @throws ParcmanDBShareNotExistException 
	 */
	public ShareBean getShareById(String id)
		throws ParcmanDBShareNotExistException
	{
		Iterator i = this.sharings.iterator();
		while (i.hasNext())
		{
			ShareBean s = (ShareBean) i.next();
			if (s.getId().equals(id))
			{
				return s;
			}
		}
		throw new ParcmanDBShareNotExistException();
	}

	/**
	 * Restituisce i dati di un file condiviso a partire da un tag a cui 
	 * esso e` associato.
	 * In caso contrario viene sollevata l'eccezione
	 * ParcmanDBShareNotExistException
	 *
	 * @param tag Stringa che rappresenta una tag 
	 * @return ShareBean che contiene i dati del file
	 * @throws ParcmanDBShareNotExistException
	 */
	public ShareBean getShareByTag(String tag)
		throws ParcmanDBShareNotExistException
	{
		Iterator i = this.sharings.iterator();
		while (i.hasNext())
		{
			ShareBean s = (ShareBean) i.next();
			if (s.getKeywords().contains(tag))
			{
				return s;
			}
		}
		throw new ParcmanDBShareNotExistException();
	}
	
	/**
	 * Rimuove un file condiviso.
	 * In caso contrario viene sollevata l'eccezione 
	 * ParcmanDBShareNotExistException
	 * 
	 * @param index indice dello share
	 * @throws ParcmanDBShareNotExistException
	 */
	public void removeShare(int index)
		throws ParcmanDBShareNotExistException
	{
		try 
		{
			this.sharings.removeElementAt(index);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new ParcmanDBShareNotExistException(e);
		}
	}

	/**
	 * Rimuove un file condiviso attraverso il suo id.
	 * In caso contrario viene sollevata l'eccezione
	 * ParcmanDBShareNotExistException.
	 *
	 * @param id id del file condiviso
	 * @throws ParcmanDBSharedExistException
	 */
	public void removeShare(String id)
		throws ParcmanDBShareNotExistException
	{
		Iterator i = this.sharings.iterator();
		while (i.hasNext())
		{
			ShareBean s = (ShareBean) i.next();
			if (s.getId().equals(id)) 
			{
				this.sharings.remove(s);
				return;
			}
		}
		throw new ParcmanDBShareNotExistException();
	}

	/**
	 * Assegna il campo dbFile.
	 *
	 * @param dbFile Il Path del file su disco
	 */
	public void setDbFile(String dbFile)
	{
		this.dbFile = dbFile;
	}

	/**
	 * Restituisce il valore del campo dbFile.
	 *
	 * @return Il Path del file su disco
	 */
	public String getDbFile()
	{
		return dbFile;
	}

	/** 
	 * Restituisce il numero dei file condivisi attualmente disponibili.
	 *
	 * @return Numero dei file condivisi
	 */
	public int getSize()
	{
		return this.sharings.size();
	}

	/**
	 * Restituisce la lista dei file nel database.
	 *
	 * @return Vector di ShareBean contenente la lista dei file.
	 */
	public Vector<ShareBean> getSharings()
	{
		return this.sharings;
	}
}
