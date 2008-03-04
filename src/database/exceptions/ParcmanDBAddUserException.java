package database.exceptions;

public class ParcmanDBAddUserException
    extends Exception
{
    private static final long serialVersionUID = 42L;

    /**
     * Costruttore.
     *
     * @param message Messaggio di errore.
     */
    public ParcmanDBAddUserException (String message)
    {
        super(message);
    }

    /**
     * Costruttore.
     */
	public ParcmanDBAddUserException ()
	{

	}
}

