package database;

import java.io.*;
import java.lang.reflect.*;

import database.*;
import database.beans.*;
import plog.*;
import database.exceptions.*;

/**
 * Gestore del DataBase globale.
 *
 * @author Parcman Tm
 */
public class DB
{
    /**
     * Path della directory contenente i file del DataBase.
     */
    private String dbDirectory;

    /**
     * Nome del DataBase Utenti.
     */
    private static String DB_USERS_FILE = "dbUsers.xml";

    /**
     * Costruttore.
     *
     * @param dbDirectory Directory radice del DataBase
     */
    public DB (String dbDirectory)
        throws ParcmanDBNotCreateException
    {
        this.dbDirectory = dbDirectory;

        // Ottengo l'istanza di DBManager
        DBManager dbManager = DBManager.getInstance();
        // Aggiungo il DataBase Utenti al DBManager
        dbManager.add("USERS", new DBUsers(this.dbDirectory + "/" + this.DB_USERS_FILE));

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
     * 
     * @param user Dati dell'utente
     */
    public void addUser(UserBean user)
	    throws ParcmanDBErrorException, ParcmanDBUserExistException, ParcmanDBUserNotValidException
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

	public UserBean getUser(String name)
		throws ParcmanDBErrorException, ParcmanDBUserNotExistException
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
				throw new ParcmanDBUserNotExistException();

			throw new ParcmanDBErrorException();
		}
		catch (Exception e)
		{
			throw new ParcmanDBErrorException();
		}
	}

    /**
     * Controlla e Fixa la Directory radice del DataBase
     */
    private void fixDirectory ()
        throws ParcmanDBDirectoryMalformedException
    {
        File dir = new File(this.dbDirectory);

        // Controllo l'esistenza della directory
        if (!dir.exists()) // La Directory non esiste
        {
            PLog.debug("DB.fixDirectory", "Creazione della Directory " + dbDirectory);
            // Creo la directory, comprese le directory nel PATH
            dir.mkdirs();

            PLog.debug("DB.fixDirectory", "Creo il file " + DB_USERS_FILE + " nella directory " + this.dbDirectory);
            // Creo il file XML del DB Utenti invocando save della classe DBUsers
            DBManager dbManager = DBManager.getInstance();

            dbManager.save("USERS");

        }
        else // Esiste un file di nome this.dbDirectory
        {
            if (dir.isDirectory()) // E' una Directory
            {
                File dbUsers = new File(this.dbDirectory + "/" + DB_USERS_FILE);

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
            }
            else // Non e' una Directory
			{
                throw new ParcmanDBDirectoryMalformedException(this.dbDirectory + " non e' una directory.");
			}
		}
    }
}

