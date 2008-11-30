package remoteexceptions;

import java.rmi.*;

/**
 * Privilegi errati.
 *
 * @author Parcman Tm
 */
public class ParcmanServerWrongPrivilegesRemoteException
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public ParcmanServerWrongPrivilegesRemoteException(String message)
	{
		super(message);
	}

	/**
	 * Costruttore.
	 */
	public ParcmanServerWrongPrivilegesRemoteException()
	{

	}
}

