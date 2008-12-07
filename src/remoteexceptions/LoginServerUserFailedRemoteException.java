package remoteexceptions;

import java.rmi.*;

/**
 * Username non corrisponde.
 *
 * @author Parcman Tm
 */
public class LoginServerUserFailedRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public LoginServerUserFailedRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public LoginServerUserFailedRemoteException()
	{
		super();
	}
}

