package io.exceptions;

import java.io.Serializable;

/**
 * Eccezione generica di input/output.
 * 
 * @author Parcman Tm
 */
public class InOutException 
	extends Exception
	implements Serializable
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

