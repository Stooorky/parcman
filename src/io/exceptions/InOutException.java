package io.exceptions;

/**
 * Eccezione generica di input/output.
 * 
 * @author Parcman Tm
 */
public class InOutException
	extends Exception
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public InOutException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public InOutException()
	{
		super();
	}
}

