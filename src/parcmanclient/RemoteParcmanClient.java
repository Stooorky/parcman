package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del ParcmanClient.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanClient
	extends RemoteParcmanClientUser
{
	/**
	 * Lancia la connessione alla rete Parcman.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void startConnection() throws
		RemoteException;

	/**
	 * Ping.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void ping() throws
		RemoteException;

	/**
	 * Disconnessione dalla rete Parcman.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void exit() throws
		RemoteException;
}


