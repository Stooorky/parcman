package dbmanager;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import plog.*;
import dbmanager.beans.*;
import dbmanager.xmlhandlers.*;

public class NewDBUsers implements DBFile
{
	private String dbFile;
	private Vector<UserBean> users;

	public NewDBUsers(String dbFile)
	{
		this.dbFile = dbFile;
		this.users = new Vector<UserBean>();
	}

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
			PLog.err(e, "Errore durante il salvataggio del DB Utenti");
			return;
		}

		PrintStream ps = new PrintStream(fos);

		// DTD
		ps.println("<?xml version=\"1.0\"?>");
		ps.println("<!DOCTYPE DB_USERS [");
		ps.println("<!ELEMENT DB_USERS (USER)*>");
		ps.println("<!ELEMENT USER (NAME, PASSWORD, PRIVILEGE)>");
		ps.println("<!ELEMENT NAME (#PCDATA)>");
		ps.println("<!ELEMENT PASSWORD (#PCDATA)>");
		ps.println("<!ELEMENT PRIVILEGE (#PCDATA)>");
		ps.println("]>");

		// XML
		ps.println("<DB_USERS>");

		for (int i=0; i<this.getSize(); i++)
		{
			ps.println("<USER>");
			ps.println("<NAME>" + this.getUserName(i) + "</NAME>");
			ps.println("<PASSWORD>" + this.getUserPassword(i) + "</PASSWORD>");
			ps.println("<PRIVILEGE>" + this.getUserPrivilege(i) + "</PRIVILEGE>");
			ps.println("<USER>");
		}

		ps.println("</DB_USERS>");
	}

	/**
	 * Popola il vettore users caricando i dati da dbFile.
	 */
	public void load()
	{
		XMLReader parser;
		try 
		{
			parser = XMLReaderFactory.createXMLReader();
			parser.setContentHandler(new UserContentHandler(this.users));
			parser.parse(dbFile);
		}
		catch (SAXException e)
		{
			PLog.err("Errore durante il parsing del database.");
		}
		catch (IOException e)
		{
			PLog.err("Impossibile eseguire il parsing del database. File non leggibile.");
		}
	}

	/**
	 * Aggiunta di utente al DataBase Utenti.
	 *
	 * @param user Bean utente
	 */
	public void addUser(UserBean user)
	{
		users.add(user);
	}

	/**
	 * Rimuove un utente dal DataBase Utenti.
	 *
	 * @param index Index dell'utente da rimuovere
	 */
	public void removeUserAt(int index)
		throws ArrayIndexOutOfBoundsException
		{
			if (index >= 0 && index < getSize())
				users.removeElementAt(index);
			else
				throw new ArrayIndexOutOfBoundsException();
		}

	/**
	 * Assegna il campo dbFile.
	 *
	 * @param dbFile Il Path del file DB su disco
	 */
	public void setDbFile(String dbFile)
	{
		this.dbFile = dbFile;
	}

	/**
	 * Restituisce il valore del campo dbFile.
	 *
	 * @return Il Path del file DB su disco
	 */
	public String getDbFile()
	{
		return dbFile;
	}

	/**
	 * Restituisce il nome dell'Utente di indice index.
	 *
	 * @param index Indice dell'utente
	 * @return Nome utente
	 */
	private String getUserName(int index)
		throws ArrayIndexOutOfBoundsException
		{
			if (index >= 0 && index < this.getSize())
				return users.elementAt(index).getName();

			throw new ArrayIndexOutOfBoundsException();
		}

	/**
	 * Restituisce la password dell'Utente di indice index.
	 *
	 * @param index Indice dell'utente
	 * @return Password utente
	 */
	private String getUserPassword(int index)
		throws ArrayIndexOutOfBoundsException
		{
			if (index >= 0 && index < this.getSize())
				return users.elementAt(index).getPassword();

			throw new ArrayIndexOutOfBoundsException();
		}

	/**
	 * Restituisce i privilegi dell'Utente di indice index.
	 *
	 * @param index Indice dell'utente
	 * @return Privilegi utente
	 */
	private String getUserPrivilege(int index)
		throws ArrayIndexOutOfBoundsException
		{
			if (index >= 0 && index < this.getSize())
				return users.elementAt(index).getPrivilege();

			throw new ArrayIndexOutOfBoundsException();
		}

	/** Restituisce il numero di Utenti nel DB.
	 *
	 * @return Numero di Utenti registrati nel DataBase Utenti
	 */
	private int getSize()
	{
		return users.size();
	}
}	
