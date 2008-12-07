package remoteexceptions;

import java.rmi.*;

/**
 * Username o password non corrispondono.
 *
 * @author Parcman Tm
 */
public class LoginServerUserOrPasswordFailedRemoteException 
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public LoginServerUserOrPasswordFailedRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public LoginServerUserOrPasswordFailedRemoteException()
	{
		super();
	}
}

