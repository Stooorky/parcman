package remoteexceptions;

import java.rmi.*;

/**
 * Tentativo di Hacking.
 *
 * @author Parcman Tm
 */
public class ParcmanServerHackWarningRemoteException 
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public ParcmanServerHackWarningRemoteException(String message)
	{
		super(message);	
	}

	/**
	 * Costruttore.
	 */
	public ParcmanServerHackWarningRemoteException()
	{

	}
}

