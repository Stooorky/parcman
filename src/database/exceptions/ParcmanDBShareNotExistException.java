package database.exceptions;

/**
 * Il file condiviso non esiste all'interno del database.
 *
 * @author Parcman Tm
 */
public class ParcmanDBShareNotExistException
	extends Exception
{
	/**
	 * Serial Version UID per il check di compatibilita'.
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Serial Version UID per il check di compatibilita'.
	 */

	/** 
	 * Costruttore.
	 */
	public ParcmanDBShareNotExistException()
	{
		super();
	}

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public ParcmanDBShareNotExistException(String message)
	{
		super(message);
	}

	/** 
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 * @param cause La causa scatenante.
	 */
	public ParcmanDBShareNotExistException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Costruttore.
	 *
	 * @param cause La causa scatenante.
	 */
	public ParcmanDBShareNotExistException(Throwable cause)
	{
		super(cause);
	}
}

