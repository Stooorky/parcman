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
public class UserBean 
	implements Serializable
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
				|| !this.privilege.equals(bean.getPrivilege()))
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
}

