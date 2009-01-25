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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import database.exceptions.ParcmanDBUserInvalidStatusException;
import database.xmlhandlers.*;

/**
 * Bean DataBase Utenti.
 *
 * @author Parcman Tm
 */
public class UserBean 
	implements Serializable
{
	/**
	 * Serial Version UID per il check di compatibilita'.
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Nome utente.
	 */
	private String name;

	/**
	 * Password utente.
	 */
	private String password;

	/**
	 * Privilegi utente.
	 */
	private String privilege;

	/**
	 * Stato dell'utente.
	 */
	private String status;

	/**
	 * Restituisce il nome utente.
	 *
	 * @return Nome utente.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Assegna il nome utente.
	 *
	 * @param name Nome utente.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Restituisce la password utente.
	 *
	 * @return Nome utente.
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Assegna la password utente.
	 *
	 * @param password Password utente.
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * Restituisce i privilegi utente.
	 *
	 * @return Privilegi utente.
	 */
	public String getPrivilege()
	{
		return privilege;
	}

	/**
	 * Assegna i privilegi utente.
	 *
	 * @param privilege Privilegi utente.
	 */
	public void setPrivilege(String privilege)
	{
		this.privilege = privilege;
	}

	/**
	 * Restituisce lo stato dell'utente.
	 * Le possibilta` sono:
	 * <ul>
	 * <li><tt>READY</tt>: indica che le informazioni relative all'utente sono pronte per essere usate.</li>
	 * <li><tt>BLACKLIST</tt>: indica che l'utente e` stato inserito nella black-list.</li>
	 * <li><tt>WAITING</tt>: indica che l'utente non e` stato ancora autorizzato.</li>
	 * </ul>
	 *
	 * @return lo stato dell'utente.
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Assegna lo stato dell'utente.
	 * Le possibilta` sono:
	 * <ul>
	 * <li><tt>READY</tt>: indica che le informazioni relative all'utente sono pronte per essere usate.</li>
	 * <li><tt>BLACKLIST</tt>: indica che l'utente e` stato inserito nella black-list.</li>
	 * <li><tt>WAITING</tt>: indica che l'utente non e` stato ancora autorizzato.</li>
	 * </ul>
	 *
	 * @param status lo stato dell'utente.
	 */
	public void setStatus(String status) throws 
		ParcmanDBUserInvalidStatusException
	{
		Pattern pattern = Pattern.compile("READY|BLACKLIST|WAITING");
		Matcher matcher = pattern.matcher(status);
		if (!matcher.matches())
			throw new ParcmanDBUserInvalidStatusException();
		this.status = status;
	}


	/**
	 * Controlla di equivalenza.
	 *
	 * @param obj Oggetto da confrontare
	 * @return True se obj e' un'istanza di UserBean identica a this, false altrimenti.
	 */
	public boolean equals(Object obj)
	{
		if (!(obj instanceof UserBean))
		{
			return false;
		}

		UserBean bean = (UserBean) obj;
		if (!this.name.equals(bean.getName())
			|| !this.password.equals(bean.getPassword())
			|| !this.privilege.equals(bean.getPrivilege())
			|| !this.status.equals(bean.getStatus()))
		{
			return false;
		}

		return true;
	}

	/**
	 * Controlla la validita' dei dati contenuti nello UserBean.
	 * I campi devono essere non vuoti.
	 *
	 * @return True se i dati sono validi, False altrimenti.
	 */
	public boolean validate()
	{
		if (this.name == null || this.name.equals(""))
			return false;
		if (this.password == null || this.password.equals(""))
			return false;
		if (this.privilege == null || this.privilege.equals(""))
			return false;
		if (this.status == null || this.status.equals(""))
			return false;

		return true;
	}

	/**
	 * Reimplementazione del metodo readObject della classe Serializable.
	 *
	 * @param aInputStream Stream in lettura dell'oggetto
	 * @throws ClassNotFoundException Impossibile ottenere la definizione della classe
	 * @throws IllegalArgumentException I dati dell'oggetto non sono validi
	 * @throws IOException Errore di IO
	 */
	private void readObject(ObjectInputStream aInputStream) throws
		ClassNotFoundException,
		IllegalArgumentException,
		IOException
	{
		aInputStream.defaultReadObject();

		if (!(this.validate()))
			throw new IllegalArgumentException("I dati contenuti nello UserBean non sono validi.");
	}

	/**
	 * Reimplementazione del metodo writeObject della classe Serializable.
	 *
	 * @param aOutputStream Stream in scrittura dell'oggetto
	 * @throws IOException Errore di IO
	 */
	private void writeObject(ObjectOutputStream aOutputStream) throws
		IOException
	{
		aOutputStream.defaultWriteObject();
	}

	public String toString()
	{
		String out = "\n{\n" 
			+ "\tname: '" + this.name + "',\n"
			+ "\tpassword: '" + this.password + "',\n"
			+ "\tprivilege: '" + this.privilege + "',\n"
			+ "\tstatus: '" + this.status + "',\n"
			+ "}\n";

		return out;
	}
}

