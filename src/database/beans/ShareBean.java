package database.beans;

import java.io.Serializable;
import java.util.*;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Bean Database Sharings
 *
 * @author Parcman Tm
 */
public class ShareBean
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
	 * url del file condiviso.
	 */
	private URL url;

	/**
	 * Owner del file condiviso.
	 */
	private String owner;

	/**
	 * Hash del file condiviso.
	 */
	private String hash;

	/**
	 * Keywords del file condiviso.
	 */
	private Vector<String> keywords;

	/**
	 * Costruttore.
	 */
	public ShareBean()
	{
		this.keywords = new Vector<String>();
	}

	/**
	 * Assegna l'id del file.
	 *
	 * @param id L'intero che rappresenta l'id del file che lo identifica
	 * sul client del proprietario.
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * Restituisce l'id del file.
	 *
	 * @return L'id del file che lo indentifica sul client del proprietario.
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Assegna la URL del file.
	 *
	 * @param url La stringa rappesentante la URL del file.
	 */
	public void setUrl(String url)
		throws MalformedURLException
	{
		this.url = new URL(url);
	}

	/**
	 * Restituisce la URL del file.
	 *
	 * @return Una stringa rappresentante su URL del file sul client.
	 */
	public URL getUrl()
	{
		return this.url;
	}

	/**
	 * Restituisce il nome del file. 
	 * Il nome viene estratto dalla URL
	 *
	 * @return Una stringa rappresentante il nome del file.
	 */
	public String getName()
	{
		return this.url.getPath().substring(this.url.getPath().lastIndexOf('/') + 1);
	}

	/**
	 * Assegna il proprietario del file.
	 *
	 * @param owner Stringa rappresentante il nome univoco del
	 * proprietario.
	 */
	public void setOwner(String owner)
	{
		this.owner = owner;
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
	 * Assegna l'hash che rappresenta il file.
	 *
	 * @param hash La stringa che rappresenta il file.
	 */
	public void setHash(String hash)
	{
		this.hash = hash;
	}

	/**
	 * Restituisce la stringa hash che rappresenta il file.
	 *
	 * @return una stringa hash che rappresenta il file.
	 */
	public String getHash()
	{
		return this.hash;
	}

	/**
	 * Assegna le keywords associate al file.
	 *
	 * @param keywords Vector contenente le keywords.
	 */
	public void setKeywords(Vector<String> keywords)
	{
		this.keywords = keywords;
	}

	/**
	 * Aggiunge una keyword al file condiviso.
	 *
	 * @param keyword Striga rappresentante una keyword.
	 */
	public void addKeyword(String keyword)
	{
		this.keywords.add(keyword);
	}

	public int getNumberOfKeywords()
	{
		return this.keywords.size();
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
	 * Controllo di equivalenza.
	 *
	 * @param obj Oggetto da confrontare
	 * @return true se obj e' un istanza di ShareBean identica a this, false altrimenti.
	 */
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ShareBean))
		{
			return false;
		}
		
		ShareBean bean = (ShareBean) obj;
		
		if (!(this.equalsId(bean))
			|| !(this.equalsUrl(bean))
			|| !(this.equalsHash(bean))
			|| !(this.equalsOwner(bean))
			|| !(this.equalsKeywords(bean)))
		{
			return false;
		}
		
		return true;
	}

	private boolean equalsId(ShareBean bean)
	{
		if (this.id == null && bean.getId() == null)
			return true;
		if (this.id.equals(bean.getId()))
			return true;
		return false;
	}

	private boolean equalsHash(ShareBean bean)
	{
		if (this.hash == null && bean.getHash() == null)
			return true;
		if (this.hash.equals(bean.getHash()))
			return true;
		return false;
	}

	private boolean equalsOwner(ShareBean bean)
	{
		if (this.owner == null && bean.getOwner() == null)
			return true;
		if (this.owner.equals(bean.getOwner()))
			return true;
		return false;
	}

	private boolean equalsUrl(ShareBean bean)
	{
		if (this.url == null && bean.getUrl() == null)
			return true;
		if (this.url.equals(bean.getUrl()))
			return true;
		return false;
	}

	private boolean equalsKeywords(ShareBean bean)
	{
		if (this.keywords == null && bean.getKeywords() == null)
			return true;
		if (this.keywords.size() != bean.getNumberOfKeywords())
			return false;
		for (int i=0; i<this.keywords.size(); i++)
			if (!(bean.hasKeyword((String) this.keywords.get(i))))
				return false;
		return true;
	}

	/**
	 * Controlla la validita' dei dati contenuti nello ShareBean.
	 *
	 * @return true se i dati non sono nulli o vuoti, false altrimenti.
	 */
	public boolean validate()
	{
		if (this.id == null || this.id.equals(""))
			return false;
		if (this.url == null)
			return false;
		if (this.hash == null || this.hash.equals(""))
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

	public String toString()
	{
		String out = "\n{\n" 
			+ "\tid: '" + this.id + "',\n"
			+ "\turl: '" + this.url+ "',\n"
			+ "\towner: '" + this.owner + "',\n"
			+ "\thash: '" + this.hash + "',\n"
			+ "\tkeywords: {\n" 
			+ "\t\tlength: " + this.keywords.size() + ",\n";
		for (int i=0; i<this.keywords.size(); i++)
		{
			out += "\t\t" + i + ": '" + this.keywords.get(i);
			if ((i + 1) < this.keywords.size())
				out += ",";
			out += "\n";
		}
		out += "\t}\n}\n";

		return out;
	}
}
