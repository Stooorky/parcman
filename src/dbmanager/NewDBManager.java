package dbmanager;

import java.util.*;
import java.lang.reflect.*;
/**
 * Gestore del DataBase.
 *
 * @author Parcman Tm
 */
public class NewDBManager implements DB
{
	/**
	 * Istanza di DBManager.
	 */
	private static NewDBManager instance;

	/**
	 * Directory radice del DataBase
	 */
	// TODO: il path dbDirectory non e` utilizzato attualmente. Utilizzarlo!
	private String dbDirectory;

	/**
	 * Lista dei database
	 */
	private Map<String, DB> dbMap;
	
	/**
	 * Costruttore.
	 *
	 * @param dbDirectory	Path della directory radice del database.
	 */
	private NewDBManager(String dbDirectory)
	{
		this.dbDirectory = dbDirectory;
		this.dbMap = new HashMap<String, DB>();
	}

	/** 
	 * Costruttore.
	 *
	 * @param dbDirectory	Path della directory radice del database.
	 * @param dbMap		Lista dei database.
	 */
	private  NewDBManager(String dbDirectory, Map<String, DB> dbMap)
	{
		this.dbDirectory = dbDirectory;
		this.dbMap = dbMap;
	}

	public static NewDBManager getInstance(String dbDirectory)
	{
		return getInstance(dbDirectory, new HashMap<String, DB>());
		//if (this.instance == null)
		//{
		//	this.instance = new NewDBManager(dbDirectory);
		//}
		//return this.instance;
	}

	public static NewDBManager getInstance(String dbDirectory, Map<String, DB> dbMap)
	{
		if (instance == null)
		{
			instance = new NewDBManager(dbDirectory, dbMap);
		}
		return instance;
	}

	/**
	 * Esegue il salvataggio dei database sui relativi files.
	 */
	public void save()
	{
		//if (this.dbMap.size() == 0)
		//{
		//	throw new Exception("Non ci sono database registrati nel dbmanager.");
		//}
		Set<String> keys = this.dbMap.keySet();
		Iterator i = keys.iterator();
		while (i.hasNext())
		{
			String dbName = (String) i.next();
			DB db = this.dbMap.get(dbName);
			db.save();
		}
	}

	/**
	 * Carica i database presenti in dbMap.
	 */
	public void load()
	{
		//if (this.dbMap.size() == 0)
		//{
		//	throw new Exception("Non ci sono database registrati nel dbmanager.");
		//}
		Set keys = this.dbMap.keySet();
		Iterator i = keys.iterator();
		while (i.hasNext())
		{
			String dbName = (String) i.next();
			DB db = this.dbMap.get(dbName);
			db.load();
		}
	}

	/**
	 * Permette di ottenere la lista dei nomi dei database registrati nel 
	 * manager.
	 *
	 * @return Vector contenente i nomi dei database registrati.
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
	 * Esegue il salvataggio del database su file.
	 *
	 * @param dbName Nome del database.
	 */
	public void save(String dbName)
	{
		DB db = this.dbMap.get(dbName);
		db.save();
	}

	/**
	 * Carica il database specificato da dbName.
	 *
	 * @param dbName Nome del database.
	 */
	public void load(String dbName)
	{
		DB db = this.dbMap.get(dbName);
		db.load();
	}

	/**
	 * Aggiunge un database; se esiste gia` un database con nome dbName 
	 * quest'ultimo viene sovrascritto.
	 *
	 * @param dbName	Nome del database.
	 * @param db		Database da aggiungere.
	 */
	public void add(String dbName, DB db)
	{
		this.dbMap.put(dbName, db);
		// altrimenti se torna utile si puo` ottenere il vecchio valore
		// contenuto nella mappa. 
		// prev e` null se alla chiave non era associato nulla
		// DB prev = this.dbMap.put(dbName, db);
	}

	/**
	 * Rimuove il database dbName.
	 *
	 * @param dbName Nome del database.
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
	 * Chiamo un metodo del database dbName e ne ritorno l'output.
	 *
	 * @param dbName	Nome del database richiesto.
	 * @param methodName	Metodo da invocare.
	 * @param args		Argomenti da passare al metodo incato.
	 *
	 * @return		Output del metodo invocato.
	 */
	public Object call(String dbName, String methodName, Object[] args)
		throws NoSuchMethodException, 
		SecurityException,
		IllegalAccessException,		// lanciata quando il metodo corrente non ha accesso alla definizione della classe richiesta.
		IllegalArgumentException,	// lanciata quando i parametri passati sono inappropriati per il metodo richiesto.
		InvocationTargetException	// lanciata quando viene lanciata un'eccezzione nel metodo chiamato.
	{
		// - prelevo dalla lista il db con nome dbName.
		DB db = this.dbMap.get(dbName);
		// - controllo che il db abbia il metodo richiesto.
		Class dbClass = db.getClass();
		Class[] argsTypes = new Class[args.length];
		for (int i=0; i<args.length; i++)
		{
			argsTypes[i] = args[i].getClass();
		}
		Method m = dbClass.getMethod(methodName, argsTypes);
		// - invoco il metodo.
		Object returned = m.invoke(db, args);
		// - ritorno l'output del metodo.
		return returned;
	}
}
