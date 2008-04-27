package database.exceptions;

/**
 * Il file condviso e' gia' esistente.
 *
 * @author Parcman Tm
 */
public class ParcmanDBShareExistException
	extends Exception
{
	/**
	 * Serial Version UID per il check di compatibilita'.
	 */
	private static final long serialVersionUID = 42L;

	/** 
	 * Costruttore.
	 */
	public ParcmanDBShareExistException()
	{
		super();
	}

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public ParcmanDBShareExistException(String message)
	{
		super(message);
	}

	/** 
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 * @param cause La causa scatenante.
	 */
	public ParcmanDBShareExistException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Costruttore.
	 *
	 * @param cause La causa scatenante.
	 */
	public ParcmanDBShareExistException(Throwable cause)
	{
		super(cause);
	}
}
