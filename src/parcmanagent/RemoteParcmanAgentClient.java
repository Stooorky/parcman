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
	public void run(ParcmanAgent agent) 
		throws RemoteException;
}
