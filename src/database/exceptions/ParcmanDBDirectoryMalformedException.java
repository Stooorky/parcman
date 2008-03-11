package database.exceptions;

/**
 * Il path della directory radice non e' valido.
 * 
 * @author Parcman Tm
 */
public class ParcmanDBDirectoryMalformedException
	extends Exception
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public ParcmanDBDirectoryMalformedException (String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public ParcmanDBDirectoryMalformedException ()
	{

	}
}

