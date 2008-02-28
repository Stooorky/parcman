package dbmanager;

import java.io.*;
import java.util.*;

import plog.*;

/**
 * Gestore DataBase utenti.
 *
 * @author Parcman Tm
 */
public class DBUsers
{
	/**
	 * Path del file DB su disco.
	 */
	private String dbFile;

	/**
	 * Vettore nomi utenti.
	 */
	private Vector userName;

	/**
	 * Vettore password utenti.
	 */
	private Vector userPassword;

	/**
	 * Vettore privilegi utenti.
	 */
	private Vector userPrivilege;

	/**
	 * Costruttore.
	 *
	 * @param db_file Path del file DB da gestire
	 */
	public DBUsers(String dbFile)
	{
		this.dbFile = dbFile;

		userName = new Vector();
		userPassword = new Vector();
		userPrivilege = new Vector();
	}

	/**
	 * Esegue il salvataggio del DataBase Utenti su disco.
	 */
	public void save()
	{
		File f = new File(dbFile);
		FileOutputStream fos;

		try
		{
			fos = new FileOutputStream(f);
		}
		catch(FileNotFoundException e)
		{
			PLog.err(e, "Errore durante il salvataggio del DB Utenti");
			return;
		}

		PrintStream ps = new PrintStream(fos);

		// DTD
		ps.println("<?xml version=\"1.0\"?>");
		ps.println("<!DOCTYPE DB_USERS [");
		ps.println("<!ELEMENT DB_USERS (USER)*>");
		ps.println("<!ELEMENT USER (NAME, PASSWORD, PRIVILEGE)>");
		ps.println("<!ELEMENT NAME (#PCDATA)>");
		ps.println("<!ELEMENT PASSWORD (#PCDATA)>");
		ps.println("<!ELEMENT PRIVILEGE (#PCDATA)>");
		ps.println("]>");

		// XML
		ps.println("<DB_USERS>");

		for (int i=0; i<=this._getSize(); i++)
		{
			ps.println("<USER>");
			ps.println("<NAME>" + _getUserName(i) + "</NAME>");
			ps.println("<PASSWORD>" + _getUserPassword(i) + "</PASSWORD>");
			ps.println("<PRIVILEGE>" + _getUserPrivilege(i) + "</PRIVILEGE>");
			ps.println("<USER>");
		}

		ps.println("</DB_USERS>");
	}

	/**
	 * Aggiunta di utente al DataBase Utenti.
	 *
	 * @param name Nome Utente
	 * @param password Password Utente
	 * @param privilege Privilegi Utente
	 */
	public void addUser(String name, String password, String privilege)
	{
		userName.add(name);
		userPassword.add(password);
		userPrivilege.add(privilege);
	}

	/**
	 * Rimuove un utente dal DataBase Utenti.
	 *
	 * @param index Index dell'utente da rimuovere
	 */
	public void removeUserAt(int index) throws ArrayIndexOutOfBoundsException
	{
		if (index >= 0 && index < _getSize())
		{
			userName.removeElementAt(index);
			userPassword.removeElementAt(index);
			userPrivilege.removeElementAt(index);
		}

		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Assegna il campo dbFile.
	 *
	 * @param dbFile Il Path del file DB su disco
	 */
	public void setDbFile(String dbFile)
	{
		this.dbFile = dbFile;
	}

	/**
	 * Restituisce il valore del campo dbFile.
	 *
	 * @return Il Path del file DB su disco
	 */
	public String getDbFile()
	{
		return dbFile;
	}

	/**
	 * Restituisce il nome dell'Utente di indice index.
	 *
	 * @param index Indice dell'utente
	 * @return Nome utente
	 */
	private String _getUserName(int index) throws ArrayIndexOutOfBoundsException
	{
		if (index >= 0 && index < userName.size())
			return (String)userName.elementAt(index);

		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Restituisce la password dell'Utente di indice index.
	 *
	 * @param index Indice dell'utente
	 * @return Password utente
	 */
	private String _getUserPassword(int index) throws ArrayIndexOutOfBoundsException
	{
		if (index >= 0 && index < userPassword.size())
			return (String)userPassword.elementAt(index);

		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Restituisce i privilegi dell'Utente di indice index.
	 *
	 * @param index Indice dell'utente
	 * @return Privilegi utente
	 */
	private String _getUserPrivilege(int index) throws ArrayIndexOutOfBoundsException
	{
		if (index >= 0 && index < userPrivilege.size())
			return (String)userPrivilege.elementAt(index);

		throw new ArrayIndexOutOfBoundsException();
	}

	/** Restituisce il numero di Utenti nel DB.
	 *
	 * @return Numero di Utenti registrati nel DataBase Utenti
	 */
	private int _getSize()
	{
		// I tre vettori userName, userPassword e userPrivilege sono equivalenti
		return userName.size();
	}
}
