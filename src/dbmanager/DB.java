package database;

import java.io.*;

import database.*;
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

        try
        {
            this.fixDirectory();
        }
        catch (ParcmanDBDirectoryMalformedException e)
        {
            PLog.err(e, "Impossibile creare un'istanza di DB.");
            throw new ParcmanDBNotCreateException("Directory non valida");
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
            PLog.debug("Creazione della Directory " + dbDirectory);
            // Creo la directory, comprese le directory nel PATH
            dir.mkdirs();

            PLog.debug("Creo il file " + DB_USERS_FILE + " nella directory " + this.dbDirectory);
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
                    PLog.debug("Creo il file " + DB_USERS_FILE + " nella directory " + this.dbDirectory);
                    // Creo il file XML del DB Utenti invocando save della classe DBUsers
                    DBManager dbManager = DBManager.getInstance();

                    dbManager.save("USERS");
                }
                else // Il file XML per il DB Utenti esiste
                {
                    PLog.debug("Carico il DB Utenti");
                    // Carico il file XML del DB Utenti
                    DBManager dbManager = DBManager.getInstance();

                    dbManager.load("USERS");
                }
            }
            else // Non e' una Directory
                throw new ParcmanDBDirectoryMalformedException(this.dbDirectory + " non e' una directory.");
        }
    }
}

