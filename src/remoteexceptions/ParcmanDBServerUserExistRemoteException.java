package remoteexceptions;

import java.rmi.*;

/**
 * Utente presente all'interno del database.
 *
 * @author Parcman Tm
 */
public class ParcmanDBServerUserExistRemoteException 
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public ParcmanDBServerUserExistRemoteException(String message)
	{
		super(message);	
	}

	/**
	 * Costruttore.
	 */
	public ParcmanDBServerUserExistRemoteException()
	{

	}
}

