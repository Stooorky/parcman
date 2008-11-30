package logserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.net.*;

import remoteexceptions.*;

/**
 * Mobile server in esecuzione presso il Client.
 *
 * @author Parcman Tm
 */
public class LogServer
extends UnicastRemoteObject
implements RemoteLogServer, Serializable
{
    public LogServer() throws
        RemoteException
    {
        
    }

    public void log(String str) throws
        RemoteException
    {
        System.out.println(str);
    }
}
