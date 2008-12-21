package io.exceptions;

/**
 * La lista e` vuota.
 * 
 * @author Parcman Tm
 */
public class ListIsEmptyException
	extends InOutException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public ListIsEmptyException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public ListIsEmptyException()
	{
		super();
	}
}

