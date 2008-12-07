package database;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import plog.*;
import database.beans.ShareBean;
import database.beans.SearchBean;
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
		ps.println("<!ELEMENT FILE (ID, URL, OWNER, HASH, KEYWORDS)>");
		ps.println("<!ELEMENT ID (#PCDATA)>");
		ps.println("<!ELEMENT URL (#PCDATA)>");
		ps.println("<!ELEMENT OWNER (#PCDATA)>");
		ps.println("<!ELEMENT HASH (#PCDATA)>");
		ps.println("<!ELEMENT KEYWORDS (#PCDATA)>");
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
	 * Rimuove un file dal database dei file condivisi.
	 * Se il file non e' presente solleva l'eccezione 
	 * ParcmanDBSharedNotExistException.
	 *
     * @param id Id del file condiviso da eliminare
	 * @param owner Proprietario del file
	 * @throws ParcmanDBShareNotExistException Il file specificato non e' presente all'interno del database
	 */
	public void removeShare(Integer id, String owner) throws
		ParcmanDBShareNotExistException
	{
        for (int i = 0; i < this.sharings.size(); i++)
        {
            ShareBean share = this.sharings.get(i);
	        if (share.getId() == id.intValue() && share.getOwner().equals(owner))
			{
                this.sharings.remove(i);
                return;
			}
        }

        throw new ParcmanDBShareNotExistException();
	}

	/**
	 * Restituisce i dati di un file condiviso a partire dal proprietario e dall'id associato.
	 * Se il file non e` presente viene sollevata l'eccezione
	 * ParcmanDBShareNotExistException
	 * 
	 * @param owner Stringa che rappresenta il nome del proprietario del file.
	 * @param id Stringa che rappresenta l'ID del file.
	 * @return ShareBean che contiene i dati del file
	 * @throws ParcmanDBShareNotExistException File non presente nel database
	 */
	public ShareBean getShare(String owner, String id)
		throws ParcmanDBShareNotExistException
	{
		Iterator i = this.sharings.iterator();
		while (i.hasNext())
		{
			ShareBean s = (ShareBean) i.next();
			if (s.getOwner().equals(owner) && (s.getId()+"").equals(id))
			{
				return s;
			}
		}
		throw new ParcmanDBShareNotExistException();
	}

	/**
	* Restituisce la lista Sharings di un utente.
	*
	* @param userName Nome utente
	* @return Vettore di ShareBean contenente la lista dei file condivisi dell'utente
	*/
	public Vector<ShareBean> getSharings(String userName)
	{
		Vector<ShareBean> v = new Vector<ShareBean>();

		Iterator i = this.sharings.iterator();
		while (i.hasNext())
		{
			ShareBean s = (ShareBean) i.next();
			if (s.getOwner().equals(userName))
			{
				v.add(s);
			}
		}

		return v;
	}

	/**
	* Esegue una ricerca di file sul database.
	* TODO Attualmente viene usata la keywords pura
	* ma e' bene estrarre da keywords le parole chiave per una ricerca piu' efficace
	*
	* @param keywords Keywords per la ricerca
	* @return Vettore di SearchBean contenente il risultato della ricerca
	*/
	public Vector<SearchBean> searchFiles(String keywords)
		throws ParcmanDBShareNotExistException
	{
		//Vector<SearchBean> v = new Vector<SearchBean>();
		//String keySeparator = keywords.indexOf(",") != -1 ? "," : " ";
		//String[] keyArray = keywords.split(keySeparator);
		//for (int i=0; i<keyArray.length; i++)
		//{
		//	ShareBean s = getShareByTag(keyArray[i]);
		//	if (s != null)
		//		v.add(new SearchBean(s));
		//}

		//return v;
		Vector<SearchBean> v = new Vector<SearchBean>();
		String keySeparator = keywords.indexOf(",") != -1 ? "," : " ";
		String[] keyArray = keywords.split(keySeparator);
		
		// TODO: per il momento funziona solo con una tag. estendere a piu` tag
		Iterator i = this.sharings.iterator();
		while (i.hasNext())
		{
			ShareBean s = (ShareBean) i.next();
			if (s.hasKeyword(keyArray[0]))
			{
				v.add(new SearchBean(s));
			}
		}

		return v;
	}

	/**
	 * Rimuove un file condiviso.
	 * In caso contrario viene sollevata l'eccezione 
	 * ParcmanDBShareNotExistException
	 * 
	 * @param index indice dello share
	 * @throws ParcmanDBShareNotExistException File non presente nel database
	 */
	//public void removeShare(int index)
	//	throws ParcmanDBShareNotExistException
	//{
	//	try 
	//	{
	//		this.sharings.removeElementAt(index);
	//	}
	//	catch (ArrayIndexOutOfBoundsException e)
	//	{
	//		throw new ParcmanDBShareNotExistException(e);
	//	}
	//}

	/**
	 * Rimuove un file condiviso attraverso il suo id.
	 * In caso contrario viene sollevata l'eccezione
	 * ParcmanDBShareNotExistException.
	 *
	 * @param id id del file condiviso
	 * @throws ParcmanDBShareNotExistException File non presente nel database
	 */
	public void removeShare(int id)
		throws ParcmanDBShareNotExistException
	{
		Iterator i = this.sharings.iterator();
		while (i.hasNext())
		{
			ShareBean s = (ShareBean) i.next();
			if (s.getId() == id) 
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

