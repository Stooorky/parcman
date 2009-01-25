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

package loginserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;
import parcmanclient.RemoteParcmanClient;

/**
 * Interfaccia remota del LoginServer.
 *
 * @author Parcman Tm
 */
public interface RemoteLoginServer 
	extends Remote
{
	/**
	 * Esegue il login di un Client alla rete Parcman.
	 * Ritorna un MobileServer esportato nel caso in cui il login abbia successo.
	 * 
	 * @param name Nome utente
	 * @param password Password utente
	 * @return Un MobileServer di tipo ParcmanClient se il login ha successo, null altrimenti
	 * @throws RemoteException Eccezione Remota
	 */
	public RemoteParcmanClient login(String name, String password) throws
		RemoteException;

	/**
	 * Esegue la registrazione di un nuovo account.
	 * 
	 * @param name Nome utente
	 * @param password Password utente
	 * @throws ParcmanDBServerUserExistRemoteException Utente gia' presente nel database
	 * @throws RemoteException Eccezione Remota
	 */
	public void createAccount(String name, String password) throws
		RemoteException,
		ParcmanDBServerUserExistRemoteException,
		ParcmanDBServerUserNotValidRemoteException;

	/**
	 * Ping.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void ping() throws RemoteException;
}

