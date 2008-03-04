package database.exceptions;

/**
 * Errore interno al database.
 *
 * @author Parcman Tm
 */
public class ParcmanDBErrorException
    extends Exception
{
    private static final long serialVersionUID = 42L;

    /**
     * Costruttore.
     *
     * @param message Messaggio di errore.
     */
    public ParcmanDBErrorException (String message)
    {
        super(message);
    }

    /**
     * Costruttore.
     */
	public ParcmanDBErrorException ()
	{

	}
}

