package database.exceptions;

public class ParcmanDBUserNotExistException
    extends Exception
{
    private static final long serialVersionUID = 42L;

    /**
     * Costruttore.
     *
     * @param message Messaggio di errore.
     */
    public ParcmanDBUserNotExistException (String message)
    {
        super(message);
    }

    /**
     * Costruttore.
     */
	public ParcmanDBUserNotExistException ()
	{

	}
}

