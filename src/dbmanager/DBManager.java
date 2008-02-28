package dbmanager;

import java.io.*;

import plog.*;

/**
 * Gestore del DataBase.
 *
 * @author Parcman Tm
 */
public class DBManager
{
	/**
	 * DataBase Utenti.
	 */
	private DBUsers dbUsers;

	/**
	 * Indice del DataBase Utenti.
	 */
	public static int DB_USERS = 1;

	/**
	 * Costruttore.
	 *
	 * @param db_file Path del file DB da gestire
	 */
	public DBManager(String dbFileUsers)
	{
		dbUsers = new DBUsers(dbFileUsers);
	}

	/**
	 * Esegue il salvataggio dei DataBase su file.
	 */
	public void save() throws IOException;
	{
		dbUsers.save();
	}

	/**
	 * Esegue il salvataggio di un DataBase specifico su file.
	 *
	 * @param db Indice del DB da salvare
	 */
	public void save(int db) throws IOException;
	{
		switch(db)
		{
			case DB_USERS: // Db Utenti
				PLog.debug("Salvataggio del DB Utenti in corso");
				dbUsers.save();
				break;
			default:
				PLog.err("Chiave DB non riconosciuta");
		}
	}

	/**
	 * Assegna il campo dbFile.
	 *
	 * @param dbFile Il Path del file DB su disco
	 */
	private void setDbFile(String dbFile)
	{
		this.dbFile = dbFile;
	}

	/**
	 * Restituisce il valore del campo dbFile.
	 *
	 * @return Il Path del file DB su disco
	 */
	private String getDbFile()
	{
		return dbFile;
	}
}
