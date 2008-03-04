package database.exceptions;

public class ParcmanDBUserExistException
    extends Exception
{
    private static final long serialVersionUID = 42L;

    /**
     * Costruttore.
     *
     * @param message Messaggio di errore.
     */
    public ParcmanDBUserExistException (String message)
    {
        super(message);
    }

    /**
     * Costruttore.
     */
	public ParcmanDBUserExistException ()
	{

	}
}

