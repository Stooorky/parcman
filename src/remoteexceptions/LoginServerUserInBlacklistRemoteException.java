package remoteexceptions;

import java.rmi.*;

/**
 * Utente in blacklist.
 *
 * @author Parcman Tm
 */
public class LoginServerUserInBlacklistRemoteException 
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public LoginServerUserInBlacklistRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public LoginServerUserInBlacklistRemoteException()
	{
		super();
	}
}

