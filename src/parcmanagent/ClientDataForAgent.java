package parcmanagent;

import java.io.Serializable;
import parcmanclient.RemoteParcmanClientAgent;

/**
 * Contenitore per i dati di un Client trasportati dal ParcmanAgent.
 *
 * @author Parcman Tm
 */
public class ClientDataForAgent
implements Serializable
{
	/**
	 * Serial Version UID per il check di compatibilita`.
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Nome utente.
	 */
	private String name;

	/**
	 * Stub del ParcmanClient.
	 */
	private RemoteParcmanClientAgent stub;

	/**
	 * Versione della lista dei file condivisi.
	 */
	private int version;

	/** 
	 * Oggetto UpdateList relativo.
	 */
	private UpdateList updateList;

	/** 
	 * Status 
	 */
	private int status;

	/**
	 * Costruttore.
	 *
	 * @param name Nome utente
	 * @param stub Stub del ParcmanClient
	 * @param version Versione della lista dei file condivisi
	 */
	public ClientDataForAgent(String name, RemoteParcmanClientAgent stub, int version, int status)
	{
		this.version = version;
		this.name = name;
		this.stub = stub;
        this.status = status;
        this.updateList = null;
        System.out.println("CACAAAAAAAAAAAA " + status);
	}

	/**
	 * Ritorna lo Stub.
	 *
	 * @return Stub del ParcmanClient
	 */
	public RemoteParcmanClientAgent getStub()
	{
		return stub;
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
	 * Assegna lo Stub.
	 *
	 * @param stub Stub del ParcmanClient
	 */
	public void setStub(RemoteParcmanClientAgent stub)
	{
		this.stub = stub;
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

	/**
	 * Assegna lo status del contenitore <tt>ClientDataForAgent</tt>.
	 *
	 * @param status stato del contenitore. Lo stato e` di tipo <tt>ClientDataForAgentStatus</tt>.
	 */
	public void setStatus(int status)
	{   
		this.status = status;
	}

	/**
	 * Ritorna lo stato del contenitore <tt>ClientDataForAgent</tt>.
	 *
	 * @return Lo stato attuale del contenitore.
	 */
	public int getStatus()
	{
		return this.status;
	}

	/**
	 * Assegna la lista di aggiornamento di tipo <tt>UpdateList</tt>.
	 *
	 * @param updateList La lista di aggiornamento.
	 */
	public void setUpdateList(UpdateList updateList)
	{
		this.updateList = updateList;
	}

	/**
	 * Ritorna la lista degli aggiornamenti attualmente il possesso del contentitore.
	 *
	 * @return La lista degli aggiornamenti <tt>UpdateList</tt>.
	 */
	public UpdateList getUpdateList()
	{
		return this.updateList;
	}

}

