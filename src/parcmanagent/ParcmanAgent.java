package parcmanagent;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import plog.*;
import remoteexceptions.*;
import indexingserver.RemoteIndexingServer;

/**
 * Parcman Agent.
 *
 * @author Parcman Tm
 */
public class ParcmanAgent
	extends UnicastRemoteObject
	implements RemoteParcmanAgentServer, Serializable
{
	public ParcmanAgent(RemoteIndexingServer iserver, long validity, Vector<ClientDataForAgent> clients)
		throws RemoteException
	{
		// empty for now
	}

}

