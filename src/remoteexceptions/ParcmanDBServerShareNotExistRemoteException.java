package remoteexceptions;

import java.rmi.*;

/**
 * File non presente all'interno del database.
 *
 * @author Parcman Tm
 */
public class ParcmanDBServerShareNotExistRemoteException 
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public ParcmanDBServerShareNotExistRemoteException(String message)
	{
		super(message);	
	}

	/**
	 * Costruttore.
	 */
	public ParcmanDBServerShareNotExistRemoteException()
	{

	}
}

