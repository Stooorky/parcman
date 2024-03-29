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

import remoteexceptions.*;
import parcmanclient.RemoteParcmanClient;
import database.beans.SearchBean;
import database.beans.ShareBean;
import database.beans.UserBean;
import parcmanclient.DownloadData;

/**
 * Interfaccia remota del ParcmanServer per gli utenti.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanServerUser
	extends Remote, Serializable
{
	/**
	 * Esegue la connessione di un nuovo RemoteParcmanClient alla rete Parcman.
	 * E' necessario possedere lo Stub dell'utente per poter fare questa richiesta.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @throws RemoteException Eccezione Remota
	 */
	public void connect(RemoteParcmanClient parcmanClientStub, String userName) throws
		RemoteException;

	/**
	 * Esegue la disconnessione di RemoteParcmanClient dalla rete Parcman.
	 * E' necessario possedere lo Stub dell'utente per poter fare questa richiesta.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @throws RemoteException Eccezione Remota
	 */
	public void disconnect(RemoteParcmanClient parcmanClientStub, String userName) throws
		RemoteException;

	/**
	 * Restituisce la lista file condivisi dell'utente.
	 * E' necessario possedere lo stub dell'utente per poter fare questa richiesta.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	 * @throws RemoteException Eccezione Remota
	 */
	public Vector<ShareBean> getSharings(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

	/**
	 * Restituisce il numero di versione dei file condivisi dell'utente.
	 * E' necessario possedere lo stub dell'utente per poter fare questa richiesta.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	 * @throws RemoteException Eccezione Remota
	 */
	public int getSharingsVersion(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

	/**
	 * Restituisce il risultato di una ricerca sul database.
	 * E' necessario possedere lo stub dell'utente per poter fare questa richiesta.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @param keywords Lista di Keyword per la ricerca
	 * @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	 * @throws RemoteException Eccezione Remota
	 */
	public Vector<SearchBean> search(RemoteParcmanClient parcmanClientStub, String userName, String keywords) throws
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

	/**
	 * Restituisce lla lista degli utenti connessi.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @return Vettore contenente la lista dei nomi utente
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi
	 * errati
	 * @throws RemoteException Eccezione remota
	 */
	public Vector<ClientDataUser> getConnectUsersList(RemoteParcmanClient parcmanClientStub, String userName)  throws
		ParcmanServerWrongPrivilegesRemoteException,
		ParcmanServerHackWarningRemoteException,
		RemoteException;

	/**
	 * Inizializza il download di un file.
	 * E` necessario possedere lo stub dell'utente per poter fare questa richiesta.
	 *
	 * @param parcmanClientStub Stub del MobileServer
	 * @param userName Nome utente proprietario della sessione
	 * @param data array di 2 elementi. Il primo e` il proprietario del file, il secondo e` l'ID del file.	
	 * @throws ParcmanServerRequestErrorRemoteException Impossibile esaudire la richiesta
	 * @throws RemoteException Eccezione Remota
	 */
	public DownloadData startDownload(RemoteParcmanClient parcmanClientStub, String userName, String[] fileData) throws 
		ParcmanServerRequestErrorRemoteException,
		ParcmanServerHackWarningRemoteException,
		RemoteException;

	/**
	 * Imposta lo stato del sistema di gestione degli agenti remoti.
	 * Lo stato puo` essere attivo (true) oppure non attivo (false).
	 * 
	 * @param boolean un boolean che rappresenta lo stato. attivo = true, non attivo = false.
	 * @throws RemoteException 
	 */
	public void setAgentSystemStatus(RemoteParcmanClient parcmanClientStub, String userName, boolean status) throws 
		RemoteException;

	/**
	 * Ritorna lo stato del sistema di gestione degli agenti remoti. 
	 * Lo stato puo` essere attivo (true) oppure non attivo (false).
	 * 
	 * @return un boolean che rappresenta lo stato. attivo = true, non attivo = false.
	 * @throws RemoteException 
	 */
	public boolean getAgentSystemStatus(RemoteParcmanClient parcmanClientStub, String userName) throws 
		RemoteException;

	/**
	 * Aggiunge un utente in blacklist.
	 * 
	 * @param parcmanClientStub Lo stub del <tt>ParcmanClient</tt>. 
	 * @param userName La stringa che rappresenta il nome del client che ha fatto la richiesta.
	 * @param userForBlacklist La stringa che rappresenta il nome del client che si vuole inserire nella blacklist.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws ParcmanServerRequestErrorRemoteException si e` verificato un errore nella procedura.
	 * @throws RemoteException Eccezione remota.
	 */
	public void addToBlacklist(RemoteParcmanClient parcmanClientStub, String userName, String userForBlacklist) throws
		ParcmanServerHackWarningRemoteException,
		ParcmanServerWrongPrivilegesRemoteException, 
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

	/**
	 * Rimuove un utente dalla blacklist.
	 * 
	 * @param parcmanClientStub Lo stub del <tt>ParcmanClient</tt>. 
	 * @param userName La stringa che rappresenta il nome del client che ha fatto la richiesta.
	 * @param userForBlacklist La stringa che rappresenta il nome del client che si vuole togliere dalla blacklist.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws ParcmanServerRequestErrorRemoteException si e` verificato un errore nella procedura.
	 * @throws RemoteException Eccezione remota.
	 */
	public void delFromBlacklist(RemoteParcmanClient parcmanClientStub, String userName, String userForBlacklist) throws
		ParcmanServerHackWarningRemoteException,
		ParcmanServerWrongPrivilegesRemoteException, 
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

	/**
	 * Ritorna la lista degli utenti in blacklist.
	 *
	 * @param parcmanClientStub Lo stub del <tt>ParcmanClient</tt>. 
	 * @param userName La stringa che rappresenta il nome del client che ha fatto la richiesta.
	 * @return Vettore di <tt>UserBean</tt>.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws ParcmanServerRequestErrorRemoteException si e` verificato un errore nella procedura.
	 * @throws RemoteException Eccezione remota.
	 */
	public Vector<UserBean> blacklist(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerHackWarningRemoteException,
		ParcmanServerWrongPrivilegesRemoteException, 
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

	/**
	 * Rimuove un utente dalla lista di waiting e lo attiva.
	 * 
	 * @param parcmanClientStub Lo stub del <tt>ParcmanClient</tt>. 
	 * @param userName La stringa che rappresenta il nome del client che ha fatto la richiesta.
	 * @param userForBlacklist La stringa che rappresenta il nome del client che si vuole togliere dalla lista di waiting.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws ParcmanServerRequestErrorRemoteException si e` verificato un errore nella procedura.
	 * @throws RemoteException Eccezione remota.
	 */
	public void delFromWaiting(RemoteParcmanClient parcmanClientStub, String userName, String userStillWaiting) throws
		ParcmanServerHackWarningRemoteException,
		ParcmanServerWrongPrivilegesRemoteException, 
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

	/**
	 * Ritorna la lista degli utenti in stato di waiting
	 *
	 * @param parcmanClientStub Lo stub del <tt>ParcmanClient</tt>. 
	 * @param userName La stringa che rappresenta il nome del client che ha fatto la richiesta.
	 * @return Vettore di <tt>UserBean</tt>.
	 * @throws ParcmanServerWrongPrivilegesRemoteException Privilegi errati.
	 * @throws ParcmanServerHackWarningRemoteException si sta verificando un probabile attacco.
	 * @throws ParcmanServerRequestErrorRemoteException si e` verificato un errore nella procedura.
	 * @throws RemoteException Eccezione remota.
	 */
	public Vector<UserBean> waitinglist(RemoteParcmanClient parcmanClientStub, String userName) throws
		ParcmanServerHackWarningRemoteException,
		ParcmanServerWrongPrivilegesRemoteException, 
		ParcmanServerRequestErrorRemoteException,
		RemoteException;

	/**
	 * Ping.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void ping() throws
		RemoteException;
}

