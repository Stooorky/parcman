package remoteexceptions;

import java.rmi.*;

/**
 * client host irraggiungibile.
 *
 * @author Parcman Tm
 */
public class LoginServerClientHostUnreachableRemoteException 
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public LoginServerClientHostUnreachableRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public LoginServerClientHostUnreachableRemoteException()
	{
		super();
	}
}

