
package remoteexceptions;

import java.rmi.*;

/**
 * Utente non autorizzato.
 *
 * @author Parcman Tm
 */
public class LoginServerUserNotAuthorizedRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public LoginServerUserNotAuthorizedRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public LoginServerUserNotAuthorizedRemoteException()
	{
		super();
	}
}

