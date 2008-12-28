package logserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.net.*;
import io.Logger;
import io.PropertyManager;

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
	private Logger logger;

	public LogServer() throws
		RemoteException
	{
		this.logger = Logger.getLogger("agent-side", PropertyManager.getInstance().get("logger"), 3);
	}

	public void error(String msg) throws 
		RemoteException 
	{
		logger.error(msg);
	}

	public void error(String[] msg) throws 
		RemoteException 
	{
		logger.error(msg);
	}

	public void error(String msg, Throwable e) throws 
		RemoteException 
	{
		logger.error(msg, e);
	}

	public void error(String[] msg, Throwable e) throws 
		RemoteException 
	{
		logger.error(msg, e);
	}

	public void warning(String msg) throws 
		RemoteException 
	{
		logger.warning(msg);
	}

	public void warning(String[] msg) throws 
		RemoteException 
	{
		logger.warning(msg);
	}

	public void warning(String msg, Throwable e) throws 
		RemoteException 
	{
		logger.warning(msg, e);
	}

	public void warning(String[] msg, Throwable e) throws 
		RemoteException 
	{
		logger.warning(msg, e);
	}

	public void debug(String msg) throws 
		RemoteException 
	{
		logger.debug(msg);
	}

	public void debug(String[] msg) throws 
		RemoteException 
	{
		logger.debug(msg);
	}

	public void debug(String msg, Throwable e) throws 
		RemoteException 
	{
		logger.debug(msg, e);
	}

	public void debug(String[] msg, Throwable e) throws 
		RemoteException 
	{
		logger.debug(msg, e);
	}

	public void info(String msg) throws 
		RemoteException 
	{
		logger.info(msg);
	}

	public void info(String[] msg) throws 
		RemoteException 
	{
		logger.info(msg);
	}

	public void info(String msg, Throwable e) throws 
		RemoteException 
	{
		logger.info(msg, e);
	}

	public void info(String[] msg, Throwable e) throws 
		RemoteException 
	{
		logger.info(msg, e);
	}

	public void log(String msg) throws 
		RemoteException 
	{
		logger.log(msg);
	}

	public void log(String[] msg) throws 
		RemoteException 
	{
		logger.log(msg);
	}

	public void log(String msg, Throwable e) throws 
		RemoteException 
	{
		logger.log(msg, e);
	}

	public void log(String[] msg, Throwable e) throws 
		RemoteException 
	{
		logger.log(msg, e);
	}


}
