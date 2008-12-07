package remoteexceptions;

import java.rmi.*;

/**
 * Utente non ha i privilegi necessari.
 *
 * @author Parcman Tm
 */
public class LoginServerUserPrivilegeFailedRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public LoginServerUserPrivilegeFailedRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public LoginServerUserPrivilegeFailedRemoteException()
	{
		super();
	}
}

