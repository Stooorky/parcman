package parcmanserver;

import java.io.Serializable;

/**
 * Contenitore remoto per i dati di sessione di un Client
 *
 * @author Parcman Tm
 */
public class ClientDataUser
	implements Serializable
{
	/**
	 * Serial Version UID per il check di compatibilita'.
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Indirizzo IP o URL del Client.
	 */
	private String host;

	/**
	 * Nome utente.
	 */
	private String name;

	/**
	 * Versione della lista dei file condivisi.
	 */
	private int version;

	/**
	 * Privilegi utente.
	 */
	private boolean isAdmin;

	/**
	 * Costruttore.
	 *
	 * @param host Indirizzo IP o URL
	 * @param name Nome utente
	 * @param isAdmin Privilegi utente
	 */
	public ClientDataUser(String host, String name, int version, boolean isAdmin)
	{
		this.host = host;
		this.name = name;
		this.isAdmin = isAdmin;
		this.version = version;
	}

	/**
	 * Ritorna l'Host.
	 *
	 * @return Indirizzo IP o URL
	 */
	public String getHost()
	{
		return this.host;
	}

	/**
	 * Assegna l'Host.
	 *
	 * @param host Indirizzo IP o URL
	 */
	public void setHost(String host)
	{
		this.host = host;
	}

	/**
	 * Ritorna la versione della lista dei file condivisi.
	 *
	 * @return Versione
	 */
	public int getVersion()
	{
		return this.version;
	}

	/**
	 * Assegna il nome utente.
	 *
	 * @param name Nome utente
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Ritorna il nome utente.
	 *
	 * @return Nome utente
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Assegna i privilegi utente.
	 *
	 * @param isAdmin privilegi utente
	 */
	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}

	/**
	 * Ritorna i privilegi utente.
	 *
	 * @return privilegi utente
	 */
	public boolean isAdmin()
	{
		return this.isAdmin;
	}

	/**
	 * Assegna la versione della lista dei file condivisi.
	 *
	 * @param version Versione
	 */
	public void setVersion(int version)
	{
		this.version = version;
	}
}
