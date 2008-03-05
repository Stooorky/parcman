package database;

/**
 * Interfaccia database di gestione file.
 *
 * @author Parcman Tm
 */
public interface DBFile
{
    /**
     * Salva il database su file.
     */
	public void save();

    /**
     * Carica in memoria il database.
     */
	public void load();

    /**
     * Restituisce il file gestito dal database.
     *
     * @return Path del file
     */
	public String getDbFile();

    /**
     * Setta il file gestito dal database.
     *
     * @param dbFile Path del file 
     */
	public void setDbFile(String dbFile);
}
