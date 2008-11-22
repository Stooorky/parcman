package parcmanagent;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del ParmanAgent
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanAgentClient
	extends Remote
{
	/**
	 * Avvia l'esecuzione del ParcmanAgent.
	 */
	public void run() 
		throws RemoteException;
}
