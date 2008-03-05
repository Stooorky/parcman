package database.exceptions;

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

