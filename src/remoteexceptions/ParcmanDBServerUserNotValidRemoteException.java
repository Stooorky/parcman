package remoteexceptions;

import java.rmi.*;

public class ParcmanDBServerUserNotValidRemoteException extends RemoteException
{
	private static final long serialVersionUID = 42L;
	
	public ParcmanDBServerUserNotValidRemoteException(String message)
	{
		super(message);	
	}

	public ParcmanDBServerUserNotValidRemoteException()
	{

	}
}

