package indexingserver;

import java.io.*;
import java.rmi.server.*;

import parcmanserver.RemoteParcmanServer;
import databaseserver.RemoteDBServer;
import logserver.RemoteLogServer;

/**
 * Dati di sessione per la riattivazione dell'IndexingServer.
 *
 * @author Parcman Tm
 */
public class IndexingServerAtDate
implements Serializable
{
	/**
	 * Stub del DBServer.
	 */
	private RemoteDBServer dBServer;

	/**
	 * Stub del ParcmanServer.
	 */
	private RemoteParcmanServer parcmanServer;

	/**
	 * Stub del LogServer.
	 */
	private RemoteLogServer logServer;


	private boolean isRun;

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
	public IndexingServerAtDate(
		RemoteParcmanServer parcmanServer,
		RemoteDBServer dBServer,
		RemoteLogServer logServer,
		boolean isRun)
	{
		this.dBServer = dBServer;
		this.parcmanServer = parcmanServer;
		this.logServer = logServer;
		this.isRun = isRun;
	}

	/**
	 * Ritorna lo Stub del ParcmanServer.
	 *
	 * @return Stub del PArcmanServer
	 */
	public RemoteParcmanServer getParcmanServerStub()
	{
		return this.parcmanServer;
	}

	/**
	 * Assegna lo Stub del ParcmanServer.
	 *
	 * @param parcmanServerStub Stub del ParcmanServer
	 */
	public void setParcmanServerStub(RemoteParcmanServer parcmanServer)
	{
		this.parcmanServer = parcmanServer;
	}

	/**
	 * Ritorna lo Stub del DBServer.
	 *
	 * @return Stub del DBServer
	 */
	public RemoteDBServer getDBServerStub()
	{
		return this.dBServer;
	}

	/**
	 * Assegna lo Stub del DBServer.
	 *
	 * @param dBServerStub Stub del DBServer
	 */
	public void setDBServerStub(RemoteDBServer dBServer)
	{
		this.dBServer = dBServer;
	}

	/**
	 * Ritorna lo Stub del LogServer.
	 *
	 * @return Stub del LogServer
	 */
	public RemoteLogServer getLogServerStub()
	{
		return this.logServer;
	}

	/**
	 * Assegna lo Stub del LogServer.
	 *
	 * @param LogServerStub Stub del LogServer
	 */
	public void setLogServerStub(RemoteLogServer logServer)
	{
		this.logServer = logServer;
	}

	/**
	 * Ritorna lo stato del sistema di indicizzazione.
	 *
	 * @return stato del sistema di indicizzazione
	 */
	public boolean getIsRun()
	{
		return this.isRun;
	}
}

