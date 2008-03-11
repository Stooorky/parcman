package loginserver;

import java.io.*;

import parcmanserver.RemoteParcmanServer;
import databaseserver.RemoteDBServer;

/**
 * Dati di sessione per la riattivazione del server di Login.
 *
 * @author PAarcman Tm
 */
public class LoginServerAtDate
implements Serializable
{
	/**
	 * Contatore del numero di attivazioni.
	 */
	private int activationsCount;

	/**
	 * Stub del ParcmanServer.
	 */
	private RemoteParcmanServer parcmanServerStub;

	/**
	 * Stub del DBServer.
	 */
	private RemoteDBServer dBServerStub;

	/**
	 * serialVersionUID per la serializzazione.
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Costruttore.
	 *
	 * @param activationsCount Numero di attivazioni del LoginServer
	 * @param parcmanServerStub Stub del ParcmanServer
	 * @param dBServerStub Stub del database Server
	 */
	public LoginServerAtDate(
		int activationsCount,
		RemoteParcmanServer parcmanServerStub,
		RemoteDBServer dBServerStub)
	{
		this.activationsCount = activationsCount;
		this.parcmanServerStub = parcmanServerStub;
		this.dBServerStub = dBServerStub;
	}

	/**
	 * Ritorna il valore del contatore del numero di attivazioni.
	 *
	 * @return Numero di attivazioni del LoginServer
	 */
	public int getActivationsCount()
	{
		return this.activationsCount;
	}

	/**
	 * Assegna un valore al numero di attivazioni.
	 *
	 * @param activationsCount Numero di attivazioni
	 */
	public void setActivationsCount(int activationsCount)
	{
		this.activationsCount = activationsCount;
	}

	/**
	 * Ritorna lo Stub del ParcmanServer.
	 *
	 * @return Stub del PArcmanServer
	 */
	public RemoteParcmanServer getParcmanServerStub()
	{
		return this.parcmanServerStub;
	}

	/**
	 * Assegna lo Stub del ParcmanServer.
	 *
	 * @param parcmanServer Stub del ParcmanServer
	 */
	public void setParcmanServerStub(RemoteParcmanServer parcmanServerStub)
	{
		this.parcmanServerStub = parcmanServerStub;
	}

	/**
	 * Ritorna lo Stub del DBServer.
	 *
	 * @return Stub del DBServer
	 */
	public RemoteDBServer getDBServerStub()
	{
		return this.dBServerStub;
	}

	/**
	 * Assegna lo Stub del DBServer.
	 *
	 * @param dBServerStub Stub del DBServer
	 */
	public void setDBServerStub(RemoteDBServer dBServerStub)
	{
		this.dBServerStub = dBServerStub;
	}
}
