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

