/*
 * Parcman Project, the Italian Arcade Network
 * Copyright (C) 2008-2009 Parcman Tm (Marchi Sirio, Marcantoni Francesco, Emanuele Dona')
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 *
 * E-Mail:	ramyel [at] gmail [dot] com
 * 			sirio.marchi [at] gmail [dot] com
 * 			emanuele.dona [at] gmail [dot] com
 */

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
