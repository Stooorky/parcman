
package remoteexceptions;

import java.rmi.*;

/**
 * Stato utente non riconosciuto. Possibile corruzione del database.
 *
 * @author Parcman Tm
 */
public class LoginServerUserInvalidStatusRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public LoginServerUserInvalidStatusRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public LoginServerUserInvalidStatusRemoteException()
	{
		super();
	}
}

