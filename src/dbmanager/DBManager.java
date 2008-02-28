package dbmanager;

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
	 * Directory radice del DataBase
	 */
	private String dbDirectory;

	/**
	 * Indice del DataBase Utenti.
	 */
	public static int DB_USERS = 1;

	/**
	 * Nome del file del DataBase Utenti.
	 */
	private static String dbUsersFileName = "dbUsers.xml";

	/**
	 * Costruttore.
	 *
	 * @param db_file Path della directory radice del DataBase
	 */
	public DBManager(String dbDirectory)
	{
		this.dbDirectory = dbDirectory;

		dbUsers = new DBUsers(dbDirectory + "/" + dbUsersFileName);
	}

	/**
	 * Esegue il salvataggio dei DataBase su file.
	 */
	public void save()
	{
		dbUsers.save();
	}

	/**
	 * Esegue il salvataggio di un DataBase specifico su file.
	 *
	 * @param db Indice del DB da salvare
	 */
	public void save(int db)
	{
		if (db == DB_USERS)
		{
			PLog.debug("Salvataggio del DB Utenti in corso");
			dbUsers.save();
		}
		else
			PLog.err("Chiave DB errata");
	}

	/**
	 * Assegna il campo dbDirectory.
	 *
	 * @param dbFile Il Path della Directory radice del DataBase
	 */
	public void setDbDirectory(String dbDirectory)
	{
		this.dbDirectory = dbDirectory;

		// Riassegno il campo dbFile del DataBase Utenti
		dbUsers.setDbFile(dbDirectory + "/" + dbUsersFileName);
	}

	/**
	 * Restituisce il valore del campo dbDirectory.
	 *
	 * @return Il Path della directory radice del DataBase
	 */
	public String getDbDirectory()
	{
		return dbDirectory;
	}
}
