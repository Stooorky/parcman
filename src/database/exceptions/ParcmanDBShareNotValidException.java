package database.exceptions;

/**
 * I dati del file condiviso non sono validi.
 *
 * @author Parcman Tm
 */
public class ParcmanDBShareNotValidException
	extends Exception
{
	/**
	 * Serial Version UID per il check di compatibilita'.
	 */
	private static final long serialVersionUID = 42L;

	/** 
	 * Costruttore.
	 */
	public ParcmanDBShareNotValidException()
	{
		super();
	}

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public ParcmanDBShareNotValidException(String message)
	{
		super(message);
	}

	/** 
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 * @param cause La causa scatenante.
	 */
	public ParcmanDBShareNotValidException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Costruttore.
	 *
	 * @param cause La causa scatenante.
	 */
	public ParcmanDBShareNotValidException(Throwable cause)
	{
		super(cause);
	}
}
