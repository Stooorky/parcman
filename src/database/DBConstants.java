package database;

public class DBConstants
{
	public static final String E_DB_INTERNAL = "Errore interno al database.";
	public static final String E_DB_NO_INSTANCE = "Impossibile creare un'istanza di DB.";
	public static final String E_DB_USER_EXISTS = "L'utente e` gia` presente nel database.";
	public static final String E_DB_USER_NOEXISTS = "L'utente non e` presente nel database.";
	public static final String E_DB_FILE_EXISTS = "Il file e` gia` presente nel database.";
	public static final String E_DB_FILE_NOEXISTS = "Il file non e` presente nel database.";

	public static final String E_DB_METHOD_CALL = "Impossibile richiemare il metodo richiesto.";
	public static final String E_SHDB_PARSING = "Errore durante il parsing del database dei file condivisi.";
	public static final String E_SHDB_FILE = "Impossibile eseguire il parsing del database dei file condivisi. File non leggibile.";
	public static final String E_USRDB_PARSING = "Errore durante il parsing del database degli utenti.";
	public static final String E_USRDB_FILE = "Impossibile eseguire il parsing del database degli utenti. File non leggibile.";
}
