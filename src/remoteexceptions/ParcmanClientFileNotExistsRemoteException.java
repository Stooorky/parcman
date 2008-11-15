package remoteexceptions;

import java.rmi.*;

/**
 * File non presente.
 *
 * @author Parcman Tm
 */
public class ParcmanClientFileNotExistsRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public ParcmanClientFileNotExistsRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public ParcmanClientFileNotExistsRemoteException()
	{

	}
}

