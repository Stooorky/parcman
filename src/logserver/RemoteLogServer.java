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
	public void error(String msg) throws 
		RemoteException;

	public void error(String[] msg) throws 
		RemoteException;

	public void error(String msg, Throwable e) throws 
		RemoteException;

	public void error(String[] msg, Throwable e) throws 
		RemoteException;

	public void warning(String msg) throws 
		RemoteException;

	public void warning(String[] msg) throws 
		RemoteException;

	public void warning(String msg, Throwable e) throws 
		RemoteException;

	public void warning(String[] msg, Throwable e) throws 
		RemoteException;

	public void debug(String msg) throws 
		RemoteException;

	public void debug(String[] msg) throws 
		RemoteException;

	public void debug(String msg, Throwable e) throws 
		RemoteException;

	public void debug(String[] msg, Throwable e) throws 
		RemoteException;

	public void info(String msg) throws 
		RemoteException;

	public void info(String[] msg) throws 
		RemoteException;

	public void info(String msg, Throwable e) throws 
		RemoteException;

	public void info(String[] msg, Throwable e) throws 
		RemoteException;

	public void log(String msg) throws 
		RemoteException;

	public void log(String[] msg) throws 
		RemoteException;

	public void log(String msg, Throwable e) throws 
		RemoteException;

	public void log(String[] msg, Throwable e) throws 
		RemoteException;
}

