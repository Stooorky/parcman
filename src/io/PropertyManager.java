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
