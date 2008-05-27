package remoteexceptions;

import java.rmi.*;

/**
 * Utente gia' connesso alla rete.
 *
 * @author Parcman Tm
 */
public class ParcmanServerUserIsConnectRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public ParcmanServerUserIsConnectRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public ParcmanServerUserIsConnectRemoteException()
	{

	}
}

