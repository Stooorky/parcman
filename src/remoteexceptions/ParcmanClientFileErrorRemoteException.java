package remoteexceptions;

import java.rmi.*;

/**
 * Errore nella creazione del buffer.
 *
 * @author Parcman Tm
 */
public class ParcmanClientFileErrorRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public ParcmanClientFileErrorRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public ParcmanClientFileErrorRemoteException()
	{

	}
}

