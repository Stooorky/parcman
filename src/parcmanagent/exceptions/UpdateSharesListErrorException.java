package parcmanagent.exceptions;

/**
 * Impossibile eseguire l'Update, errore di versione.
 * 
 * @author Parcman Tm
 */
public class UpdateSharesListErrorException
	extends Exception
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public UpdateSharesListErrorException (String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public UpdateSharesListErrorException ()
	{

	}
}

