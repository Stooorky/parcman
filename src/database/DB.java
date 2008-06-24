package database;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import database.*;
import database.beans.*;
import plog.*;
import database.exceptions.*;

/**
 * Gestore del database globale.
 *
 * @author Parcman Tm
 */
public class DB
{
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
			PLog.err(e, "DB", "Impossibile creare un'istanza di DB.");
			throw new ParcmanDBNotCreateException();
		}
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
				throw new ParcmanDBUserExistException();

			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			throw new ParcmanDBErrorException();
		}

		// Salvo il DB Utenti
		dbManager.save("USERS");

		PLog.debug("DB.addUser", "Nuovo utente aggiunto al DB Utenti: " + user.getName());
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
					throw (ParcmanDBUserNotExistException) e.getTargetException();

			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
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
				throw (ParcmanDBShareExistException) e.getTargetException();

			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			throw new ParcmanDBErrorException();
		}

		// Salvo il DB Sharings
		dbManager.save("SHARINGS");
	}

	/**
	 * Ritorna un ShareBean contenente i dati di un file condiviso.
	 *
	 * @param id Id del file
	 * @throws ParcmanDBErrorException Errore interno del database dei file condivisi
	 * @throws ParcmanDBShareNotExistException File non presente all'interno del DB
	 */
	public ShareBean getShareById(String id) throws
		ParcmanDBErrorException,
		ParcmanDBShareNotExistException
	{
		ShareBean share = null;
		Object[] args = { id };

		DBManager dbManager = DBManager.getInstance();

		try
		{
			share = (ShareBean) dbManager.call("SHARINGS", "getShareById", args);
		}
		catch (InvocationTargetException e)
		{
			// File gia' presente nel DataBase
			if (e.getTargetException() instanceof ParcmanDBShareNotExistException)
				throw (ParcmanDBShareNotExistException) e.getTargetException();

			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			throw new ParcmanDBErrorException();
		}

		return share;
	}

	/**
	 * Ritorna un ShareBean contenente i dati di un file condiviso.
	 *
	 * @param name Nome del file
	 * @throws ParcmanDBErrorException Errore interno del database dei file condivisi
	 * @throws ParcmanDBShareNotExistException File non presente all'interno del DB
	 */
	public ShareBean getShareByName(String name) throws
		ParcmanDBErrorException,
		ParcmanDBShareNotExistException
	{
		ShareBean share = null;
		Object[] args = { name };

		DBManager dbManager = DBManager.getInstance();

		try
		{
			share = (ShareBean) dbManager.call("SHARINGS", "getShareByName", args);
		}
		catch (InvocationTargetException e)
		{
			// File gia' presente nel DataBase
			if (e.getTargetException() instanceof ParcmanDBShareNotExistException)
				throw (ParcmanDBShareNotExistException) e.getTargetException();

			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
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
		catch (InvocationTargetException e)
		{
			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
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
			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
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
				throw (ParcmanDBShareNotExistException) e.getTargetException();

			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			throw new ParcmanDBErrorException();
		}

		// Salvo il DB Sharings
		dbManager.save("SHARINGS");
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
			PLog.debug("DB.fixDirectory", "Creazione della Directory " + dbDirectory);
			// Creo la directory, comprese le directory nel PATH
			dir.mkdirs();

			PLog.debug("DB.fixDirectory", "Creo i file \n\t" + DB_USERS_FILE + "\n\t" + DB_SHARINGS_FILE + "\nnella directory " + this.dbDirectory);
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
					PLog.debug("DB.fixDirectory", "Creo il file " + DB_USERS_FILE + " nella directory " + this.dbDirectory);
					// Creo il file XML del DB Utenti invocando save della classe DBUsers
					DBManager dbManager = DBManager.getInstance();

					dbManager.save("USERS");
				}
				else // Il file XML per il DB Utenti esiste
				{
					PLog.debug("DB.fixDirectory", "Carico il DB Utenti");
					// Carico il file XML del DB Utenti
					DBManager dbManager = DBManager.getInstance();

					dbManager.load("USERS");
				}

				if (!dbSharings.exists()) // Il file XML per il DB Utenti non esiste
				{
					PLog.debug("DB.fixDirectory", "Creo il file " + DB_SHARINGS_FILE + " nella directory " + this.dbDirectory);
					// Creo il file XML del DB Utenti invocando save della classe DBUsers
					DBManager dbManager = DBManager.getInstance();

					dbManager.save("SHARINGS");
				}
				else // Il file XML per il DB Utenti esiste
				{
					PLog.debug("DB.fixDirectory", "Carico il DB dei file condivisi");
					// Carico il file XML del DB Utenti
					DBManager dbManager = DBManager.getInstance();

					dbManager.load("SHARINGS");
				}

			}
			else // Non e' una Directory
			{
				throw new ParcmanDBDirectoryMalformedException(this.dbDirectory + " non e' una directory.");
			}
		}
	}
}

