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

package database;

import java.util.*;
import java.lang.reflect.*;

import io.Logger;
import io.PropertyManager;

/**
 * Gestore dei DataBase.
 *
 * @author Parcman Tm
 */
public class DBManager
{
	/**
	 * Logger
	 */
	private Logger logger;

	/**
	 * Istanza di DBManager.
	 */
	private static DBManager instance;

	/**
	 * Lista dei database
	 */
	private Map<String, DBFile> dbMap;

	/**
	 * Costruttore.
	 */
	private DBManager()
	{
		this.logger = Logger.getLogger("database", PropertyManager.getInstance().get("logger-server.properties"));
		this.dbMap = new HashMap<String, DBFile>();
	}

	/** 
	 * Costruttore.
	 * @param dbMap Map contenente la lista dei database
	 */
	private DBManager(Map<String, DBFile> dbMap)
	{
		this.logger = Logger.getLogger("database", PropertyManager.getInstance().get("logger-server.properties"));
		this.dbMap = dbMap;
	}

	/**
	 * Ritorna un'istanza di DBManager.
	 *
	 * @return Un'istanza di DBManager
	 */
	public static DBManager getInstance()
	{
		return getInstance(new HashMap<String, DBFile>());
	}

	/**
	 * Ritorna un'istanza di DBManager a partire da dbMap.
	 *
	 * @param dbMap Un'istanza (anche vuota) di Map Utenti
	 * @return Un'istanza di DBManager
	 */
	public static DBManager getInstance(Map<String, DBFile> dbMap)
	{
		if (instance == null)
		{
			instance = new DBManager(dbMap);
		}
		return instance;
	}

	/**
	 * Esegue il salvataggio dei database sui relativi files.
	 */
	public void save()
	{
		Set<String> keys = this.dbMap.keySet();
		Iterator i = keys.iterator();
		while (i.hasNext())
		{
			String dbName = (String) i.next();
			DBFile db = this.dbMap.get(dbName);
			db.save();
		}
	}

	/**
	 * Carica i database presenti in dbMap.
	 */
	public void load()
	{
		Set keys = this.dbMap.keySet();
		Iterator i = keys.iterator();
		while (i.hasNext())
		{
			String dbName = (String) i.next();
			DBFile db = this.dbMap.get(dbName);
			db.load();
		}
	}

	/**
	 * Pulisce la mappa dei database registrati nel manager.
	 */
	public void clear() 
	{
		this.dbMap.clear();
	}

	/**
	 * Permette di ottenere la lista dei nomi dei database registrati nel 
	 * manager.
	 *
	 * @return Vector contenente i nomi dei database registrati
	 */
	public Vector<String> list()
	{
		Set<String> keys = this.dbMap.keySet();
		Vector<String> list = new Vector<String>();
		Iterator i = keys.iterator();
		while (i.hasNext()) 
		{
			list.add((String) i.next());
		}
		return list;
	}

	/**
	 * Esegue il salvataggio del database specificato su file.
	 *
	 * @param dbName Nome del database
	 */
	public void save(String dbName)
	{
		DBFile db = this.dbMap.get(dbName);
		db.save();
	}

	/**
	 * Carica in memoria il database specificato.
	 *
	 * @param dbName Nome del database
	 */
	public void load(String dbName)
	{
		DBFile db = this.dbMap.get(dbName);
		db.load();
	}

	/**
	 * Aggiunge un database; se esiste gia` un database con il nome specificato 
	 * quest'ultimo viene sovrascritto.
	 *
	 * @param dbName Nome del database
	 * @param db Database da aggiungere
	 */
	public void add(String dbName, DBFile db)
	{
		this.dbMap.put(dbName, db);
		// altrimenti se torna utile si puo` ottenere il vecchio valore
		// contenuto nella mappa. 
		// prev e` null se alla chiave non era associato nulla
		// DB prev = this.dbMap.put(dbName, db);
	}

	/**
	 * Rimuove il database specificato.
	 *
	 * @param dbName Nome del database
	 */
	public void drop(String dbName)
	{
		this.dbMap.remove(dbName);
		// altrimenti se torna utile si puo` ottenere il vecchio valore
		// contenuto nella mappa. 
		// prev e` null se alla chiave non era associato nulla
		// DB prev = this.dbMap.put(dbName, db);
	}

	/**
	 * Chiama un metodo del database specificato e ne ritorna l'output.
	 *
	 * @param dbName Nome del database richiesto
	 * @param methodName Stringa rappresentante il nome del metodo da invocare
	 * @param args Argomenti da passare al metodo
	 * @return Output del metodo invocato
	 * @throws NoSuchMethodException Il metodo specificato non esiste
	 * @throws InvocationTargetException Eccezioni sollevate dal metodo invocato
	 * @throws SecurityException Invocata solo in presenza di un SecurityManager
	 * @throws IllegalAccessException Impossibile accedere alla definizione della classe
	 * @throws IllegalArgumentException Gli argomenti passati non sono validi
	 */
	public Object call(String dbName, String methodName, Object[] args) throws
		NoSuchMethodException, 
		SecurityException,
		IllegalAccessException,
		IllegalArgumentException,
		InvocationTargetException
	{
		// Prelevo dalla lista il db con nome dbName.
		DBFile db = this.dbMap.get(dbName);

		// Controllo che il db abbia il metodo richiesto.
		Class dbClass = db.getClass();
		Class[] argsTypes;
		if (args == null)
		{
			argsTypes = new Class[0];
			argsTypes = null;
		}
		else 
		{
			argsTypes = new Class[args.length];
			for (int i=0; i<args.length; i++)
			{
				if (args[i] == null)
				{
					argsTypes[i] = null;
				}
				else 
				{
					argsTypes[i] = args[i].getClass();
				}
			}
		}

		try
		{
			// TODO Warning in fase di compilazione. Controllare se e' evitabile.
			Method m = dbClass.getMethod(methodName, argsTypes);
			Object returned = m.invoke(db, args);
			// Ritorno l'output del metodo.
			return returned;

		}
		catch (SecurityException e)
		{
			logger.error(DBConstants.E_DB_METHOD_CALL);
			throw new SecurityException();
		}
		catch (NullPointerException e)
		{
			logger.error(DBConstants.E_DB_METHOD_CALL);
			return new NullPointerException();
		}
		catch (NoSuchMethodException e)
		{
			logger.error(DBConstants.E_DB_METHOD_CALL);
			return new NoSuchMethodException();
		}
		catch (InvocationTargetException e)
		{
			throw e;
		}
	}
}

