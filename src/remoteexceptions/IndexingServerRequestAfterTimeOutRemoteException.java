package remoteexceptions;

import java.rmi.*;

/**
 * Richiesta inviata oltre il TimeOut.
 *
 * @author Parcman Tm
 */
public class IndexingServerRequestAfterTimeOutRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public IndexingServerRequestAfterTimeOutRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public IndexingServerRequestAfterTimeOutRemoteException()
	{

	}
}

