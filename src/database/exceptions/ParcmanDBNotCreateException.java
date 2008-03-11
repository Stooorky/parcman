package database.exceptions;

/**
 * Il database o alcune sue parti non possono essere create.
 *
 * @author Parcman Tm
 */
public class ParcmanDBNotCreateException
	extends Exception
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public ParcmanDBNotCreateException (String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public ParcmanDBNotCreateException ()
	{

	}
}

