package io;

import java.util.Properties;
import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.FileNotFoundException;

public class PropertyManager
{
	private static PropertyManager instance;
	private Hashtable<Pair<String, String>, Properties> propertiesMap;
	private PropertyManager()
	{
		propertiesMap = new Hashtable<Pair<String, String>, Properties>();
	}

	public static PropertyManager getInstance()
	{
		if (instance == null)
		{
			instance = new PropertyManager();
		}
		return instance;
	}

	public void register(String key, String path) throws 
		FileNotFoundException,
		IOException
	{
		File p = new File(path);
		// TODO: aggiungere controllo per unicita` chiavi: key deve essere unica
		Pair<String, String> keyPair = new Pair<String, String>(key, path);
		if (p.exists()) 
		{
			Properties prop = new Properties();
			prop.load(new FileInputStream(p));
			propertiesMap.put(keyPair, prop);
		}
		else
		{
			System.out.println("non trovo il file " + path);
		}
	}

	public void unregister(String key)
	{
		Set<Pair<String, String>> keys = propertiesMap.keySet();
		Iterator<Pair<String, String>> i =keys.iterator();
		while (i.hasNext())
		{
			Pair<String, String> keyPair = i.next();
			if (keyPair.getFirst().equals(key))
			{
				propertiesMap.remove(keyPair);
				return;
			}
		}
	}

	public void reloadAll() throws 
		FileNotFoundException,
		IOException
	{
		Set<Pair<String, String>> keys = propertiesMap.keySet();
		Iterator<Pair<String, String>> i = keys.iterator();
		while (i.hasNext())
		{
			Pair<String, String> keyPair = i.next();
			propertiesMap.get(keyPair).load(new FileInputStream(keyPair.getSecond()));
		}
	}

	public void reload(String key) throws 
		FileNotFoundException,
		IOException
	{
		Set<Pair<String, String>> keys = propertiesMap.keySet();
		Iterator<Pair<String, String>> i = keys.iterator();
		while (i.hasNext())
		{
			Pair<String, String> keyPair = i.next();
			if (keyPair.getFirst().equals(key))
			{
				propertiesMap.get(keyPair).load(new FileInputStream(keyPair.getSecond()));
				return;
			}
		}
	}

	public Properties get(String key)
	{
		Set<Pair<String, String>> keys = propertiesMap.keySet();
		Iterator<Pair<String, String>> i = keys.iterator();
		while (i.hasNext())
		{
			Pair<String, String> keyPair = i.next();
			if (keyPair.getFirst().equals(key))
				return propertiesMap.get(keyPair);
		}
		return null;
	}

	public String getProperty(String key, String name)
	{
		Set<Pair<String, String>> keys = propertiesMap.keySet();
		Iterator<Pair<String, String>> i = keys.iterator();
		while (i.hasNext())
		{
			Pair<String, String> keyPair = i.next();
			if (keyPair.getFirst().equals(key))
			{
				return propertiesMap.get(keyPair).getProperty(name);
			}
		}
		return null;
	}

	public void setProperty(String key, String name, String value)
	{
		Set<Pair<String, String>> keys = propertiesMap.keySet();
		Iterator<Pair<String, String>> i = keys.iterator();
		while (i.hasNext())
		{
			Pair<String, String> keyPair = i.next();
			if (keyPair.getFirst().equals(key))
			{
				propertiesMap.get(keyPair).setProperty(name, value);
			}
		}
	}
}
