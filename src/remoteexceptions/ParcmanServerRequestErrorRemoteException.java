package remoteexceptions;

import java.rmi.*;

/**
 * Richiesta non esaudita.
 *
 * @author Parcman Tm
 */
public class ParcmanServerRequestErrorRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public ParcmanServerRequestErrorRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public ParcmanServerRequestErrorRemoteException()
	{

	}
}

