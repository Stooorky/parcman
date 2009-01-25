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

package parcmanserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import indexingserver.RemoteIndexingServer;

/**
 * Interfaccia remota del ParcmanServer.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanServer
	extends RemoteParcmanServerUser, Remote, Serializable
{
	/**
	 * Esegue l'aggiunta di un nuovo client alla lista dei tentativi di connessione.
	 *
	 * @param username Nome utente
	 * @param host Host del client
	 * @throws RemoteException Eccezione remota
	 */
	public void connectAttemp(String username, String host) throws
		RemoteException;

	/**
	 * Setta la versione dei file condivisi di un utente.
	 *
	 * @param username Nome utente
	 * @param version Versione
	 * @throws RemoteException Eccezione remota
	 */
	public void setShareListVersionOfUser(String username, int version) throws
		RemoteException;

	/**
	 * Forza la riconnessione di un utente.
	 *
	 * @param username Nome utente
	 * @throws RemoteException Eccezione remota
	 */
	public void forceUserToReconnect(String username) throws
		RemoteException;

	/**
	 * Restituisce la lista degli utenti connessi al sistema
	 * E' necessario possedere lo stub del server di indicizzazione per poter fare questa richiesta.
	 *
	 * @param ris Stub del server di indicizzazione
	 * @throws remoteexception Eccezione remota
	 */
	public Map<String, ClientData>  getConnectedUsers(RemoteIndexingServer ris) throws 
		RemoteException;

	/**
	 * Ritorna lo stato del sistema di gestione degli agenti remoti. 
	 * Lo stato puo` essere attivo (true) oppure non attivo (false).
	 * 
	 * @param ris Stub dell'<tt>IndexingServer</tt>.
	 * @return un boolean che rappresenta lo stato. attivo = true, non attivo = false.
	 * @throws RemoteException 
	 */
	public boolean getAgentSystemStatus(RemoteIndexingServer ris) throws 
		RemoteException;

}

