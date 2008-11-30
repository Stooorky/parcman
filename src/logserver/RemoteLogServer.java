package logserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del server di log.
 *
 * @author Parcman Tm
 */
public interface RemoteLogServer
	extends Remote, Serializable
{
    /**
     * 
     */
    public void log(String str) throws
        RemoteException;
}

