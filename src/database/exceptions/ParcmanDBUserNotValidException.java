package database.exceptions;

/**
 * I dati dell'utente non sono validi.
 *
 * @author Parcman Tm
 */
public class ParcmanDBUserNotValidException
    extends Exception
{
    private static final long serialVersionUID = 42L;

    /**
     * Costruttore.
     *
     * @param message Messaggio di errore.
     */
    public ParcmanDBUserNotValidException (String message)
    {
        super(message);
    }

    /**
     * Costruttore.
     */
	public ParcmanDBUserNotValidException ()
	{

	}
}

