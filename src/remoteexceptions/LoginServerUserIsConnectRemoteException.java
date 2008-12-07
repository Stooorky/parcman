package remoteexceptions;

import java.rmi.*;

/**
 * Utente gia` connesso.
 *
 * @author Parcman Tm
 */
public class LoginServerUserIsConnectRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public LoginServerUserIsConnectRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public LoginServerUserIsConnectRemoteException()
	{
		super();
	}
}

