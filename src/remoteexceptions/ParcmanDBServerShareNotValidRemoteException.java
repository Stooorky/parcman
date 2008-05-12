package remoteexceptions;

import java.rmi.*;

/**
 * I dati del file non sono validi.
 *
 * @author Parcman Tm
 */
public class ParcmanDBServerShareNotValidRemoteException 
	extends RemoteException
{
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore.
	 */
	public ParcmanDBServerShareNotValidRemoteException(String message)
	{
		super(message);	
	}

	/**
	 * Costruttore.
	 */
	public ParcmanDBServerShareNotValidRemoteException()
	{

	}
}

