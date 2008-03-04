package remoteexceptions;

import java.rmi.*;

public class ParcmanDBServerErrorRemoteException extends RemoteException
{
	public ParcmanDBServerErrorRemoteException(String message)
	{
		super(message);	
	}

	public ParcmanDBServerErrorRemoteException()
	{

	}
}

