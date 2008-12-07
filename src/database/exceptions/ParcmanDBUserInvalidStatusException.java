package database.exceptions;

/**
 * Status utente non valido.
 *
 * @author Parcman Tm
 */
public class ParcmanDBUserInvalidStatusException
	extends Exception
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public ParcmanDBUserInvalidStatusException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public ParcmanDBUserInvalidStatusException()
	{
		super();
	}
}

