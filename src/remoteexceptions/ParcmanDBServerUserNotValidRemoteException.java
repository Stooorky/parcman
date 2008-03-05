package remoteexceptions;

import java.rmi.*;

/**
 * I dati dell'utente non sono validi.
 *
 * @author Parcman Tm
 */
public class ParcmanDBServerUserNotValidRemoteException extends RemoteException
{
	private static final long serialVersionUID = 42L;
	
    /**
     * Costruttore.
     *
     * @param message Messaggio di errore.
     */
	public ParcmanDBServerUserNotValidRemoteException(String message)
	{
		super(message);	
	}

    /**
     * Costruttore.
     */
	public ParcmanDBServerUserNotValidRemoteException()
	{

	}
}

