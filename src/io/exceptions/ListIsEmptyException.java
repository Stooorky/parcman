package io.exceptions;

import java.io.Serializable;

/**
 * La lista e` vuota.
 * 
 * @author Parcman Tm
 */
public class ListIsEmptyException
	extends InOutException
	implements Serializable
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

