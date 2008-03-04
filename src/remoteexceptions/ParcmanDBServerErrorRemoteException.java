package remoteexceptions;

import java.rmi.*;

public class ParcmanDBServerErrorRemoteException extends RemoteException
{
	private static final long serialVersionUID = 42L;
	
	public ParcmanDBServerErrorRemoteException(String message)
	{
		super(message);	
	}

	public ParcmanDBServerErrorRemoteException()
	{

	}
}

