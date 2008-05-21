package remoteexceptions;

import java.rmi.*;

/**
 * Utente non presente all'interno del database.
 *
 * @author Parcman Tm
 */
public class ParcmanDBServerUserNotExistRemoteException 
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public ParcmanDBServerUserNotExistRemoteException(String message)
	{
		super(message);	
	}

	/**
	 * Costruttore.
	 */
	public ParcmanDBServerUserNotExistRemoteException()
	{

	}
}

