package remoteexceptions;

import java.rmi.*;

/**
 * Errore interno del database.
 *
 * @author Parcman Tm
 */
public class ParcmanDBServerErrorRemoteException extends RemoteException
{
	private static final long serialVersionUID = 42L;
	
    /**
     * Costruttore.
     *
     * @param message Messaggio di errore.
     */
	public ParcmanDBServerErrorRemoteException(String message)
	{
		super(message);	
	}

    /**
     * Costruttore.
     */
	public ParcmanDBServerErrorRemoteException()
	{

	}
}

