/*
 * Parcman Project, the Italian Arcade Network
 * Copyright (C) 2008-2009 Parcman Tm (Marchi Sirio, Marcantoni Francesco, Emanuele Dona')
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 *
 * E-Mail:	ramyel [at] gmail [dot] com
 * 			sirio.marchi [at] gmail [dot] com
 * 			emanuele.dona [at] gmail [dot] com
 */

package database;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import io.Logger;
import io.PropertyManager;
import database.beans.*;
import database.xmlhandlers.*;
import database.exceptions.*;

/**
 * Gestore DataBase Utenti.
 *
 * @author Parcman Tm
 */
public class DBUsers 
	implements DBFile
{
	/**
	 * Logger
	 */
	private Logger logger;

	/**
	 * Path del file XML.
	 */
	private String dbFile;

	/**
	 * Lista degli utenti presenti all'interno del database.
	 */
	private Vector<UserBean> users;

	/**
	 * Costruttore.
	 *
	 * @param dbFile File XML del DataBase Utenti
	 */
	public DBUsers(String dbFile)
	{
		this.logger = Logger.getLogger("database", PropertyManager.getInstance().get("logger-server.properties"));
		this.dbFile = dbFile;
		this.users = new Vector<UserBean>();
	}

	/**
	 * Esegue il salvataggio del DataBase Utenti su File.
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
			logger.error("Errore durante il salvataggio del DB Utenti", e);
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
		ps.println("<!ELEMENT STATUS (#PCDATA)>");
		ps.println("]>");

		// XML
		ps.println("<DB_USERS>");

		for (int i=0; i<this.getSize(); i++)
		{
			ps.println("<USER>");
			ps.println("<NAME>" + this.getUserName(i) + "</NAME>");
			ps.println("<PASSWORD>" + this.getUserPassword(i) + "</PASSWORD>");
			ps.println("<PRIVILEGE>" + this.getUserPrivilege(i) + "</PRIVILEGE>");
			ps.println("<STATUS>" + this.getUserStatus(i) + "</STATUS>");
			ps.println("</USER>");
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
			logger.error(DBConstants.E_USRDB_PARSING, e);
		}
		catch (IOException e)
		{
			logger.error(DBConstants.E_USRDB_FILE, e);
		}
		logger.info("Caricamento DBUsers completato.");
	}

	/**
	 * Aggiunge un utente al database utenti.
	 * Se l'utente e' gia' presente all'interno del database solleva
	 * l'eccezione ParcmanDBUserException.
	 *
	 * @param user Bean utente
	 * @throws ParcmanDBUserExistException L'utente specificato e' gia' presente all'interno del database
	 */
	public void addUser(UserBean user) throws
		ParcmanDBUserExistException
	{
		// Controllo che l'utente non sia gia' presente nel DataBase
		for (int i=0; i<this.getSize(); i++)
			if (this.getUserName(i).equals(user.getName()))
				throw new ParcmanDBUserExistException();

		users.add(user);
	}

	/**
	 * Restituisce i dati di un utente a partire dal nome.
	 * Se l'utente non e' presente all'interno del database solleva
	 * l'eccezione ParcmanDBUserNotExistException.
	 *
	 * @param name Nome Utente
	 * @return UserBean contenente i dati dell'utente
	 * @throws ParcmanDBUserNotExistException L'utente non e' presente all'interno del database
	 */
	public UserBean getUser(String name)
		throws ParcmanDBUserNotExistException
	{
		for (int i=0; i<this.getSize(); i++)
			if (this.getUserName(i).equals(name))
				return this.users.elementAt(i);

		throw new ParcmanDBUserNotExistException();
	}

	/**
	 * Rimuove un utente dal database utenti.
	 *
	 * @param index Index dell'utente da rimuovere
	 * @throws ArrayIndexOutOfBoundsException Indice non valido
	 */
	public void removeUserAt(int index) throws
		ArrayIndexOutOfBoundsException
	{
		if (index >= 0 && index < getSize())
			users.removeElementAt(index);
		else
			throw new ArrayIndexOutOfBoundsException();
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
	 * Restituisce il nome dell'utente di indice index.
	 *
	 * @param index Indice dell'utente
	 * @return Nome utente
	 * @throws ArrayIndexOutOfBoundsException Indice non valido
	 */
	private String getUserName(int index) throws
		ArrayIndexOutOfBoundsException
	{
		if (index >= 0 && index < this.getSize())
			return users.elementAt(index).getName();

		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Restituisce la password dell'utente di indice index.
	 *
	 * @param index Indice dell'utente
	 * @return Password utente
	 * @throws ArrayIndexOutOfBoundsException Indice non valido
	 */
	private String getUserPassword(int index) throws
		ArrayIndexOutOfBoundsException
	{
		if (index >= 0 && index < this.getSize())
			return users.elementAt(index).getPassword();

		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Restituisce i privilegi dell'utente di indice index.
	 *
	 * @param index Indice dell'utente
	 * @return Privilegi utente
	 * @throws ArrayIndexOutOfBoundsException Indice non valido
	 */
	private String getUserPrivilege(int index) throws
		ArrayIndexOutOfBoundsException
	{
		if (index >= 0 && index < this.getSize())
			return users.elementAt(index).getPrivilege();

		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Restituisce lo stato dell'utente.
	 * Gli stati possibili sono:
	 * <ul>
	 * <li><tt>READY</tt>: indica che le informazioni relative all'utente sono pronte per essere usate.</li>
	 * <li><tt>BLACKLIST</tt>: indica che l'utente e` stato inserito nella black-list.</li>
	 * <li><tt>WAITING</tt>: indica che l'utente non e` stato ancora autorizzato.</li>
	 * </ul>
	 *
	 * @param index Indice dell'utente.
	 * @return Una stringa che rappresenta lo stato dell'utente.
	 * @throws ArrayIndexOutOfBoundsException Indice non valido.
	 */
	private String getUserStatus(int index) throws 
		ArrayIndexOutOfBoundsException
	{
		if (index >= 0 && index < this.getSize())
			return users.elementAt(index).getStatus();

		throw new ArrayIndexOutOfBoundsException();
	}

	/** 
	 * Restituisce il numero di utenti nel database.
	 *
	 * @return Numero di Utenti registrati
	 */
	private int getSize()
	{
		return users.size();
	}

	/**
	 * Restituisce una copia della lista degli utenti nel database.
	 *
	 * @return Vector di UserBean contenente la lista utenti.
	 */
	public Vector<UserBean> getUsers()
	{
		return new Vector<UserBean>(this.users);
	}
}

