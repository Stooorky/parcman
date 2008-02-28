package plog;

/**
 * Gestore messaggi di errore e di debug.
 *
 * @author Parcman Tm
 */
public class PLog
{
	/**
	 * Log dei messaggi di errore con gestione delle eccezioni.
	 *
	 * @param e Eccezione da gestire
	 * @param str Messaggio di errore
	 */
	public static void err(Exception e, String str)
	{
		System.err.println("[ERROR] " + str);
		System.err.println(e);
	}

	/**
	 * Log dei messaggi di errore.
	 *
	 * @param str Messaggio di errore
	 */
	public static void err(String str)
	{
		System.err.println("[ERROR] " + str);
	}

	/**
	 * Log dei messaggi di debug.
	 *
	 * @param str Messaggio di debug
	 */
	public static void debug(String str)
	{
		System.out.println("[DEBUG] " + str);
	}

	/**
	 * Log generico.
	 *
	 * @param str Messaggio
	 */
	public static void log(String str)
	{
		System.out.println("[LOG] " + str);
	}
}