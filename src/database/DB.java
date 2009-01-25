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
import java.lang.reflect.*;

import database.*;
import database.beans.*;
import database.exceptions.*;
import io.Logger;
import io.PropertyManager;

/**
 * Gestore del database globale.
 *
 * @author Parcman Tm
 */
public class DB
{
	/**
	 * Logger
	 */
	private Logger logger;

	/**
	 * Path della directory contenente i file del database.
	 */
	private String dbDirectory;

	/**
	 * Nome del file xml del database utenti.
	 */
	private static String DB_USERS_FILE = "dbUsers.xml";

	/**
	 * Nome del file xml del database lista files.
	 */
	private static String DB_SHARINGS_FILE= "dbSharings.xml";

	/**
	 * Costruttore.
	 *
	 * @param dbDirectory Directory radice del DataBase
	 * @throws ParcmanDBNotCreateException Errore durante la creazione dell'istanza del database
	 */
	public DB(String dbDirectory) throws
		ParcmanDBNotCreateException
	{
		this.logger = Logger.getLogger("database", PropertyManager.getInstance().get("logger-server.properties"));
		this.dbDirectory = dbDirectory;

		// Ottengo l'istanza di DBManager
		DBManager dbManager = DBManager.getInstance();
		// Aggiungo il DataBase Utenti al DBManager
		dbManager.add("USERS", new DBUsers(this.dbDirectory + "/" + this.DB_USERS_FILE));
		dbManager.add("SHARINGS", new DBSharings(this.dbDirectory + "/" + this.DB_SHARINGS_FILE));

		// Fixo la Directory del DataBase
		try
		{
			this.fixDirectory();
		}
		catch (ParcmanDBDirectoryMalformedException e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBNotCreateException();
		}
	}

	public void updateUsers() throws 
		ParcmanDBErrorException
	{
		Object[] args = new Object[] {};
		try
		{
			DBManager.getInstance().call("USERS", "save", args);
		}
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}
		logger.info("Aggiornamento del database utenti completato.");
	}

	/**
	 * Aggiunge un utente al DataBase Utenti.
	 * Esegue una validazione dei dati presenti all'interno dello UserBean
	 * attraverso la funzione UserBean.validate().
	 * 
	 * @param user Dati dell'utente
	 * @throws ParcmanDBErrorException Errore interno del database utenti
	 * @throws ParcmanDBUserExistException L'utente e' gia' presente nel database
	 * @throws ParcmanDBUserNotValidException I dati forniti per l'utente non sono validi
	 */
	public void addUser(UserBean user) throws
		ParcmanDBErrorException,
		ParcmanDBUserExistException,
		ParcmanDBUserNotValidException
	{
		Object[] args = { user };

		// Controllo la validita' dei dati contenuti nello UserBean
		if (!user.validate())
			throw new ParcmanDBUserNotValidException();

		DBManager dbManager = DBManager.getInstance();

		try
		{
			dbManager.call("USERS", "addUser", args);
		}
		catch (InvocationTargetException e)
		{
			// Utente gia' presente nel DataBase
			if (e.getTargetException() instanceof ParcmanDBUserExistException)
			{
				logger.error(DBConstants.E_DB_USER_EXISTS);
				throw new ParcmanDBUserExistException();
			}

			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			logger.error("Errore interno al database");
			throw new ParcmanDBErrorException();
		}

		// Salvo il DB Utenti
		dbManager.save("USERS");

		logger.info("Nuovo utente aggiunto al DB Utenti: " + user.getName());
	}

	/**
	 * Restituisce i dati di un utente a partire dal nome.
	 * Se l'utente non e' presente all'interno del database solleva l'eccezione
	 * ParcmanDBUserNotExistException.
	 *
	 * @param name Nome utente
	 * @return UserBean contenente i dati dell'utente
	 * @throws ParcmanDBErrorException Errore interno del database utenti
	 * @throws ParcmanDBUserNotExistException Utente non presente all'interno del database utenti
	 */
	public UserBean getUser(String name) throws
		ParcmanDBErrorException,
		ParcmanDBUserNotExistException
	{
		Object[] args = { name };

		DBManager dbManager = DBManager.getInstance();

		try
		{
			return (UserBean)dbManager.call("USERS", "getUser", args);
		}
		catch (InvocationTargetException e)
		{
			// Utente gia' presente nel DataBase
			if (e.getTargetException() instanceof ParcmanDBUserNotExistException)
			{
				logger.error(DBConstants.E_DB_USER_NOEXISTS);
				throw (ParcmanDBUserNotExistException) e.getTargetException();
			}

			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}
	}

	/**
	 * Aggiunge un nuovo file condiviso alla lista file.
	 *
	 * @param share Dati del file
	 * @throws ParcmanDBErrorException Errore interno del database dei file condivisi
	 * @throws ParcmanDBShareExistException File gia' presente nel database
	 * @throws ParcmanDBShareNotValidException Elementi del file non validi
	 */
	public void addShare(ShareBean share) throws
		ParcmanDBErrorException,
		ParcmanDBShareNotValidException,
		ParcmanDBShareExistException
	{
		Object[] args = { share };

		// Controllo la validita' dei dati contenuti nello ShareBean
		if (!share.validate())
			throw new ParcmanDBShareNotValidException();

		DBManager dbManager = DBManager.getInstance();

		try
		{
			dbManager.call("SHARINGS", "addShare", args);
		}
		catch (InvocationTargetException e)
		{
			// File gia' presente nel DataBase
			if (e.getTargetException() instanceof ParcmanDBShareExistException)
			{
				logger.error(DBConstants.E_DB_FILE_EXISTS);
				throw (ParcmanDBShareExistException) e.getTargetException();
			}

			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}

		// Salvo il DB Sharings
		dbManager.save("SHARINGS");

		logger.info("File aggiunto al database dei file condivisi: " + share.getName());
	}

	/**
	 * Aggiorna il database degli utenti.
	 *
	 * @throws ParcmanDBErrorException Errore interno del database degli utenti
	 */
	public void reloadUsers() throws 
		ParcmanDBErrorException
	{
		try
		{
			Object[] args = new Object[] { };
			DBManager.getInstance().call("USERS", "load", args);
		}
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}

		logger.info("Il database degli utenti e` stato aggiornato.");
	}

	/**
	 * Aggiorna il database dei file condivisi.
	 *
	 * @throws ParcmanDBErrorException Errore interno del database dei file condivisi
	 */
	public void reloadShares() throws 
		ParcmanDBErrorException
	{
		try
		{
			Object[] args = new Object[] { };
			DBManager.getInstance().call("SHARINGS", "load", args);
		}
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}

		logger.info("Il database dei file condivisi e` stato aggiornato.");
	}

	/**
	 * Ritorna la lista degli utenti registrati nel database.
	 *
	 * @throws ParcmanDBErrorException Errore interno del database degli utenti. 
	 * @return un vettore di <tt>UserBean</tt> che rappresenta la lista degli utenti registrati sul database.
	 */
	public Vector<UserBean> getUsers() throws
		ParcmanDBErrorException	
	{
		try
		{
			Object[] args = new Object[] { };
			return ((Vector<UserBean>) DBManager.getInstance().call("USERS", "getUsers", args));
		} 
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}
	}
	/**
	 * Rimuove un file condiviso dalla lista file.
	 *
	 * @param id Id del file condiviso
	 * @param owner Proprietario del file
	 * @throws ParcmanDBErrorException Errore interno del database dei file condivisi
	 * @throws ParcmanDBShareNotExistException File non presente nel database
	 */
	public void removeShare(int id, String owner) throws
		ParcmanDBErrorException,
		ParcmanDBShareNotExistException
	{
		Object[] args = { id, owner };

		// Controllo la validita' dei dati contenuti nello ShareBean
		DBManager dbManager = DBManager.getInstance();

		try
		{
			dbManager.call("SHARINGS", "removeShare", args);
		}
		catch (InvocationTargetException e)
		{
			// File non presente nel DataBase
			if (e.getTargetException() instanceof ParcmanDBShareNotExistException)
			{
				logger.error(DBConstants.E_DB_FILE_NOEXISTS);
				throw (ParcmanDBShareNotExistException) e.getTargetException();
			}

			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}

		// Salvo il DB Sharings
		dbManager.save("SHARINGS");

		logger.info("Il file (id:" + id + ", owner:" + owner + ") e` stato rimosso.");
	}

	/**
	 * Ritorna un ShareBean contenente i dati di un file condiviso.
	 *
	 * @param owner del file.
	 * @param id ID del file.
	 * @throws ParcmanDBErrorException Errore interno del database dei file condivisi
	 * @throws ParcmanDBShareNotExistException File non presente all'interno del DB
	 */
	public ShareBean getShare(String owner, String id) throws
		ParcmanDBErrorException,
		ParcmanDBShareNotExistException
	{
		ShareBean share = null;
		Object[] args = { owner, id };

		DBManager dbManager = DBManager.getInstance();

		try
		{
			share = (ShareBean) dbManager.call("SHARINGS", "getShare", args);
		}
		catch (InvocationTargetException e)
		{
			if (e.getTargetException() instanceof ParcmanDBShareNotExistException)
			{
				logger.error(DBConstants.E_DB_FILE_NOEXISTS);
				throw (ParcmanDBShareNotExistException) e.getTargetException();
			}

			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}

		return share;
	}

	/**
	 * Restituisce la lista Sharings di un utente.
	 *
	 * @param userName Nome utente
	 * @return Vettore di ShareBean contenente la lista dei file condivisi dell'utente
	 * @throws ParcmanDBErrorException Errore interno del database dei file condivisi
	 */
	public Vector<ShareBean> getSharings(String userName) throws
		ParcmanDBErrorException
	{
		Vector<ShareBean> shares;
		Object[] args = { userName };

		DBManager dbManager = DBManager.getInstance();

		try
		{
			shares = (Vector<ShareBean>) dbManager.call("SHARINGS", "getSharings", args);
		}
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}

		return shares;
	}

	/**
	 * Esegue una ricerca di file sul database.
	 *
	 * @param keywords Keywords per la ricerca
	 * @return Vettore di SearchBean contenente il risultato della ricerca
	 * @throws ParcmanDBErrorException Errore interno del database dei file condivisi
	 */
	public Vector<SearchBean> searchFiles(String keywords) throws
		ParcmanDBErrorException
	{
		Vector<SearchBean> searchList;
		Object[] args = { keywords };

		DBManager dbManager = DBManager.getInstance();

		try
		{
			searchList = (Vector<SearchBean>) dbManager.call("SHARINGS", "searchFiles", args);
		}
		catch (InvocationTargetException e)
		{
			logger.error(DBConstants.E_DB_INTERNAL + "(0)");
			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL + "(1)");
			throw new ParcmanDBErrorException();
		}

		return searchList;
	}

	/**
	 * Rimuove un file condiviso dalla lista file.
	 *
	 * @param id Id del file
	 * @throws ParcmanDBErrorException Errore interno del database dei file condivisi
	 * @throws ParcmanDBShareNotExistException File non presente all'interno del DB
	 */
	public void removeShare(String id) throws
		ParcmanDBErrorException,
		ParcmanDBShareNotExistException
	{
		Object[] args = { id };

		DBManager dbManager = DBManager.getInstance();

		try
		{
			dbManager.call("SHARINGS", "removeShare", args);
		}
		catch (InvocationTargetException e)
		{
			// File gia' presente nel DataBase
			if (e.getTargetException() instanceof ParcmanDBShareNotExistException)
			{
				logger.error(DBConstants.E_DB_INTERNAL);
				throw (ParcmanDBShareNotExistException) e.getTargetException();
			}

			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			logger.error(DBConstants.E_DB_INTERNAL);
			throw new ParcmanDBErrorException();
		}

		// Salvo il DB Sharings
		dbManager.save("SHARINGS");

		logger.info("Il file (id:" + id + ") e` stato rimosso con successo.");
	}



	/**
	 * Controlla e Fixa la Directory radice del DataBase.
	 * Crea la directory se non esiste e la popola con i file necessari ai database.
	 *
	 * @throws ParcmanDBDirectoryMalformedException Il path fornito e' errato o i permessi non sono adeguati
	 */
	private void fixDirectory() throws
		ParcmanDBDirectoryMalformedException
	{
		File dir = new File(this.dbDirectory);

		// Controllo l'esistenza della directory
		if (!dir.exists()) // La Directory non esiste
		{
			logger.debug("Creazione della Directory " + dbDirectory);
			// Creo la directory, comprese le directory nel PATH
			dir.mkdirs();

			logger.debug("Creo i file \n\t" + DB_USERS_FILE + "\n\t" + DB_SHARINGS_FILE + "\nnella directory " + this.dbDirectory);
			// Creo il file XML del DB Utenti invocando save della classe DBUsers
			DBManager dbManager = DBManager.getInstance();

			dbManager.save();

		}
		else // Esiste un file di nome this.dbDirectory
		{
			if (dir.isDirectory()) // E' una Directory
			{
				File dbUsers = new File(this.dbDirectory + "/" + DB_USERS_FILE);
				File dbSharings = new File(this.dbDirectory + "/" + DB_SHARINGS_FILE);

				if (!dbUsers.exists()) // Il file XML per il DB Utenti non esiste
				{
					logger.debug("Creo il file " + DB_USERS_FILE + " nella directory " + this.dbDirectory);
					// Creo il file XML del DB Utenti invocando save della classe DBUsers
					DBManager dbManager = DBManager.getInstance();

					dbManager.save("USERS");
				}
				else // Il file XML per il DB Utenti esiste
				{
					logger.debug("Carico il DB Utenti");
					// Carico il file XML del DB Utenti
					DBManager dbManager = DBManager.getInstance();

					dbManager.load("USERS");
				}

				if (!dbSharings.exists()) // Il file XML per il DB Utenti non esiste
				{
					logger.debug("Creo il file " + DB_SHARINGS_FILE + " nella directory " + this.dbDirectory);
					// Creo il file XML del DB Utenti invocando save della classe DBUsers
					DBManager dbManager = DBManager.getInstance();

					dbManager.save("SHARINGS");
				}
				else // Il file XML per il DB Utenti esiste
				{
					logger.debug("Carico il DB dei file condivisi");
					// Carico il file XML del DB Utenti
					DBManager dbManager = DBManager.getInstance();

					dbManager.load("SHARINGS");
				}

			}
			else // Non e' una Directory
			{
				logger.error(this.dbDirectory + " non e' una directory.");
				throw new ParcmanDBDirectoryMalformedException(this.dbDirectory + " non e' una directory.");
			}
		}
	}
}

