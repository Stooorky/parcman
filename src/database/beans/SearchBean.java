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

package database.beans;

import java.io.Serializable;
import java.util.*;
import java.io.*;
import database.beans.ShareBean;

/**
 * Bean Ricerca File
 *
 * @author Parcman Tm
 */
public class SearchBean
	implements Serializable
{
	/**
	 * Serial Version UID per il check di compatibilita`.
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Id file condiviso.
	 */
	private int id;

	/**
	 * Owner del file condiviso.
	 */
	private String owner;

	/**
	 * Nome del file condiviso.
	 */
	private String name;

	/**
	 * Keywords del file condiviso.
	 */
	private Vector<String> keywords;

	/**
	* Costruttore.
	*
	* @param shareBean ShareBean di un file condiviso
	*/
	public SearchBean(ShareBean shareBean)
	{
		this.id = shareBean.getId();
		this.owner = shareBean.getOwner();
		this.name = shareBean.getName();
		this.keywords = shareBean.getKeywords();
	}

	/**
	 * Restituisce l'id del file.
	 *
	 * @return Una stringa contenente l'id del file che lo indentifica
	 * sul client del proprietario.
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Restituisce il nome del file.
	 *
	 * @return Una stringa rappresentante il nome del file.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Restituisce il proprietario del file.
	 *
	 * @return Una stringa contenente il nome univoco del proprietario.
	 */
	public String getOwner()
	{
		return this.owner;
	}

	/**
	 * Restituisce le keywords associate al file.
	 *
	 * @return un Vector contenente le keywords.
	 */
	public Vector<String> getKeywords()
	{
		return this.keywords;
	}

	/**
	 * Restituisce le keywords associate al file sottoforma di stringa 
	 * separata da virgole.
	 *
	 * @return una stringa del tipo "key1, key2, key3"
	 */
	public String getKeywordsToString()
	{
		StringBuffer buffer = new StringBuffer();
		Iterator i = this.keywords.iterator();
		while (i.hasNext()) 
		{
			buffer.append((String) i.next());
			if(i.hasNext())
			{
				buffer.append(" ,");
			}
		}
		return buffer.toString();
	}

	/**
	 * Verifica che una data keyword sia associata al file condiviso.
	 *
	 * @param keyword Stringa che rappresenta la keyword da verificare.
	 * @return true se la keyword e` associata al file, false altrimenti.
	 */
	public boolean hasKeyword(String keyword)
	{
		return this.keywords.contains(keyword);
	}

	/**
	 * Controlla la validita' dei dati contenuti nello SearchBean.
	 *
	 * @return true se i dati non sono nulli o vuoti, false altrimenti.
	 */
	public boolean validate()
	{
		if (this.name == null || this.name.equals(""))
			return false;
		if (this.owner == null || this.owner.equals(""))
			return false;
		if (this.keywords == null || this.keywords.size() == 0)
			return false;

		return true;
	}

	/**
	 * Reimplementazione del metodo readObject della classe Serializable.
	 *
	 * @param in Stream in lettura dell'oggetto
	 * @throws ClassNotFoundException Impossibile ottenere la definizione della classe
	 * @throws IllegalArgumentException I dati dell'oggetto non sono validi
	 * @throws IOException Errore di IO
	 */
	private void readObject(ObjectInputStream in) throws
		ClassNotFoundException,
		IllegalArgumentException,
		IOException
	{
		in.defaultReadObject();

		if (!this.validate())
			throw new IllegalArgumentException("I dati relativi al file non sono validi.");
	}

	/**
	 * Reimplementazione del metodo writeObject della classe Serializable.
	 *
	 * @param out Stream in scrittura dell'oggetto
	 * @throws IOException Errore di IO
	 */
	private void writeObject(ObjectOutputStream out) throws 
		IOException
	{
		out.defaultWriteObject();
	}
}
