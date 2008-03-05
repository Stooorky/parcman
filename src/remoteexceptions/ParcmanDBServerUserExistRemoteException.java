package remoteexceptions;

import java.rmi.*;

public class ParcmanDBServerUserExistRemoteException extends RemoteException
{
	private static final long serialVersionUID = 42L;
	
	public ParcmanDBServerUserExistRemoteException(String message)
	{
		super(message);	
	}

	public ParcmanDBServerUserExistRemoteException()
	{

	}
}

