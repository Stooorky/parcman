package database.exceptions;

/**
 * Il database o alcune sue parti non possono essere create.
 * Ad esempio la directory radice del DB e' priva dei
 * permessi di scrittura.
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

