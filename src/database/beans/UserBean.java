package database.beans;

import java.io.Serializable;
import java.util.*;
import java.io.*;

import database.xmlhandlers.*;

/**
 * Bean DataBase Utenti.
 *
 * @author Parcman Tm
 */
public class UserBean implements Serializable
{
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
	 * Serial Version UID per il check di compatibilita'.
	 */
	private static final long serialVersionUID = 42L;

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

	public boolean equals(Object obj)
	{
		if (!(obj instanceof UserBean))
		{
			return false;
		}

		UserBean bean = (UserBean) obj;
		if (!this.name.equals(bean.getName())
			|| !this.password.equals(bean.getPassword())
			|| !this.privilege.equals(bean.getPrivilege()))
		{
			return false;
		}

		return true;
	}

	private void readObject(ObjectInputStream aInputStream)
		throws ClassNotFoundException, IOException
	{
		aInputStream.defaultReadObject();

	/*
		if (!(this.name != null && !(this.name.equals(""))))
			throw new IllegalArgumentException("Il nome utente non deve essere nullo o vuoto.");
		if (!(this.password != null && !(this.password.equals(""))))
			throw new IllegalArgumentException("La password utente non deve essere nulla o vuota.");
		if (!(this.privilege != null && !(this.privilege.equals(""))))
			throw new IllegalArgumentException("Il campo privilegi utente non deve essere nullo o vuoto.");
	*/
	}

	private void writeObject(ObjectOutputStream aOutputStream)
		throws IOException
	{
		aOutputStream.defaultWriteObject();
	}
}

