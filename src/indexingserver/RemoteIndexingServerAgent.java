package indexingserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del ParcmanServer per il ParcmanAgent.
 *
 * @author Parcman Tm
 */
public interface RemoteIndexingServerAgent
extends Remote
{
	/**
	 * Ping.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void ping() throws 
		RemoteException;
}

