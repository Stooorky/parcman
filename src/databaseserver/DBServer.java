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

package databaseserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.rmi.*;

import database.*;
import io.Logger;
import database.exceptions.*;
import database.beans.*;
import remoteexceptions.*;

/**
* Server Database
*
* @author Parcman Tm
*/
public class DBServer
	extends UnicastRemoteObject
	implements RemoteDBServer
{
	/**
	 * Logger 
	 */
	private Logger logger;

	/**
	* Database
	*/
	private transient DB db;

	/**
	* SerialVersionUID
	*/
	private static final long serialVersionUID = 42L;

	/**
	* Costruttore.
	*
	* @param dbDirectory Path della directory radice del database
	* @throws ParcmanDBServerErrorRemoteException Impossibile creare il DB
	* @throws RemoteException Eccezione Remota
	*/
	public DBServer(String dbDirectory) throws
		ParcmanDBServerErrorRemoteException,
		RemoteException
	{
		this.logger = Logger.getLogger("server-side");
		try
		{
			db = new DB(dbDirectory);
		}
		catch (ParcmanDBNotCreateException e)
		{
			logger.error("Impossibile istanziare il database.");
			throw new ParcmanDBServerErrorRemoteException();
		}
	}
	
	/**
	 * Esegue il salvataggio del database utenti aggiornandolo.
	 *
	 * @throws ParcmanDBServerErrorRemoteException Errore interno al database.
	 * @throws RemoteException eccezione remota.
	 */
	public void updateUsers() throws 
		ParcmanDBServerErrorRemoteException,
		RemoteException
	{
		try
		{
			db.updateUsers();
		}
		catch (ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
	}

	/**
	* Restituisce i dati di un utente a partire dal nome.
	*
	* @param name Nome Utente
	* @return UserBean contenente i dati utente se esiste, null altrimenti
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerUserNotExistRemoteException Utente non presente nel database
	* @throws RemoteException Eccezione remota
	*/
	public UserBean getUser(String name) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerUserNotExistRemoteException,
		RemoteException
	{
		try
		{
			UserBean user = db.getUser(name);
			return user;
		}
		catch(ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
		catch(ParcmanDBUserNotExistException e)
		{
			throw new ParcmanDBServerUserNotExistRemoteException();
		}
	}

	/**
	* Aggiunge un utente al database.
	* Se l'utente e' gia' presente all'interno del database solleva
	* l'eccezione remota ParcmanDBServerUserExistRemoteException.
	* Se i dati forniti non sono coerenti, cioe' non superano il test
	* UserBean.validate(), solleva l'eccezione ParcmanDBUserNotValidException.
	*
	* @param user UserBean contenente i dati dell'utente
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerUserExistRemoteException L'utente e' gia' presente all'interno del database
	* @throws ParcmanDBServerUserNotValidRemoteException I dati forniti per l'utente non sono validi
	* @throws RemoteException Eccezione remota
	*/
	public void addUser(UserBean user) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerUserExistRemoteException,
		ParcmanDBServerUserNotValidRemoteException,
		RemoteException
	{
		try
		{
			db.addUser(user);
		}
		catch(ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
		catch(ParcmanDBUserExistException e)
		{
			throw new ParcmanDBServerUserExistRemoteException();
		}
		catch(ParcmanDBUserNotValidException e)
		{
			throw new ParcmanDBServerUserNotValidRemoteException();
		}
	}

	/**
	* Aggiorna il database degli utenti.
	*
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws RemoteException Eccezione remota
	*/
	public void reloadUsers() throws
		ParcmanDBServerErrorRemoteException,
		RemoteException
	{
		try
		{
			db.reloadUsers();
		}
		catch (ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
	}

	/**
	* Aggiorna il database dei file condivisi.
	*
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws RemoteException Eccezione remota
	*/
	public void reloadShares() throws
		ParcmanDBServerErrorRemoteException,
		RemoteException
	{
		try
		{
			db.reloadShares();
		}
		catch (ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
	}

	/**
	* Ritorna la lista degli utenti registrati nel database.
	*
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws RemoteException Eccezione remota
	* @return un vettore di <tt>UserBean</tt> che rappresenta la lista degli utenti registrati sul database.
	*/
	public Vector<UserBean> getUsers() throws
		ParcmanDBServerErrorRemoteException,
		RemoteException 
	{
		try
		{
			return db.getUsers();
		} 
		catch (ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
	}

	/**
	* Aggiunge un file condiviso al database.
	* Se il file e` gia` presente all'interno del database solleva l'eccezione
	* remota ParcmanDBShareExistException. Se i dati forniti non sono coerenti
	* solleva l'eccezione ParcmanDBShareNotValidException. 
	*
	* @param share ShareBean contenente i dai del file condiviso.
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerShareExistRemoteException Il file e` gia` presente all'interno del database.
	* @throws ParcmanDBServerShareNotValidException I dati forniti per il file da aggiundere non sono validi.
	* @throws RemoteException Eccezione remota.
	*/
	public void addShare(ShareBean share) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerShareExistRemoteException,
		ParcmanDBServerShareNotValidRemoteException,
		RemoteException
	{
		try
		{
			db.addShare(share);
		}
		catch (ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
		catch (ParcmanDBShareExistException e)
		{
			throw new ParcmanDBServerShareExistRemoteException();
		}
		catch (ParcmanDBShareNotValidException e)
		{
			throw new ParcmanDBServerShareNotValidRemoteException();
		}
	}

	/**
	* Rimuove un file condiviso dal database.
	* Se il file non e` presente all'interno del database solleva l'eccezione
	* remota ParcmanDBShareNotExistException.
	*
	* @param id Id del file condiviso
	* @param owner Proprietario del file
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerShareNotExistRemoteException File non presente all'interno del database
	* @throws RemoteException Eccezione remota
	*/
	public void removeShare(int id, String owner) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerShareNotExistRemoteException,
		RemoteException
	{
		try
		{
			db.removeShare(id, owner);
		}
		catch (ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
		catch (ParcmanDBShareNotExistException e)
		{
			throw new ParcmanDBServerShareNotExistRemoteException();
		}
	}

	/**
	* Ritorna un oggetto ShareBean contenente i dati del file condiviso.
	* Se il file non e` presente all'interno del database solleva una
	* ParcmanDBServerShareNotExistException.
	*
	* @param ownert owner del file condiviso
	* @param id Id del file condiviso
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerShareNotExistRemoteException Il file non e` presente all'interno del database.
	* @throws RemoteException Eccezione remota.
	*/
	public ShareBean getShare(String owner, String id) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerShareNotExistRemoteException,
		RemoteException
	{
		try
		{
			ShareBean share = db.getShare(owner, id);
			return share;
		}
		catch (ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
		catch (ParcmanDBShareNotExistException e)
		{
			throw new ParcmanDBServerShareNotExistRemoteException();
		}
	}

	/**
	* Restituisce la lista Sharings di un utente.
	*
	* @param userName Nome utente
	* @return Vettore di ShareBean contenente la lista dei file condivisi dell'utente
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws ParcmanDBServerUserNotExistRemoteException Utente non presente nel database
	* @throws RemoteException Eccezione remota
	*/
	public Vector<ShareBean> getSharings(String userName) throws
		ParcmanDBServerErrorRemoteException,
		ParcmanDBServerUserNotExistRemoteException,
		RemoteException
	{
		try
		{
			// Controllo l'esistenza dell'utente
			db.getUser(userName);
			Vector<ShareBean> shares = db.getSharings(userName);
			return shares;
		}
		catch(ParcmanDBErrorException e)
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
		catch(ParcmanDBUserNotExistException e)
		{
			throw new ParcmanDBServerUserNotExistRemoteException();
		}
	}

	/**
	* Esegue una ricerca di file sul database.
	*
	* @param keywords Keywords per la ricerca
	* @return Vettore di SearchBean contenente il risultato della ricerca
	* @throws ParcmanDBServerErrorRemoteException Errore interno al database
	* @throws RemoteException Eccezione remota
	*/
	public Vector<SearchBean> searchFiles(String keywords) throws
		ParcmanDBServerErrorRemoteException,
		RemoteException
	{
		try
		{
			return db.searchFiles(keywords);
		}
		catch(Exception e) // Non serve
		{
			throw new ParcmanDBServerErrorRemoteException();
		}
	}

	/**
	* Metodo ping.
	*
	* @throws RemoteException Eccezione remota
	*/
	public void ping() throws
		RemoteException
	{
		try
		{
			logger.info("E' stata ricevuta una richiesta di ping da " + this.getClientHost());
		}
		catch(ServerNotActiveException e)
		{
			logger.error("Errore di rete, ClientHost irraggiungibile.", e);
		}
	}
}

