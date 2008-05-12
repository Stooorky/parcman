package remoteexceptions;

import java.rmi.*;

/**
 * File presente all'interno del database.
 *
 * @author Parcman Tm
 */
public class ParcmanDBServerShareExistRemoteException 
	extends RemoteException
{ 
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param message Messaggio di errore
	 */
	public ParcmanDBServerShareExistRemoteException(String message)
	{
		super(message);	
	}

	/**
	 * Costruttore.
	 */
	public ParcmanDBServerShareExistRemoteException()
	{

	}
}

