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

package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del ParcmanClient a livello utente.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanClientUser
	extends Remote, Serializable
{
	/**
	 * Ritorna il nome utente del proprietario della sessione.
	 *
	 * @return Nome utente del proprietario della sessione
	 * @throws RemoteException Eccezione remota
	 */
	public String getUserName() throws
		RemoteException;

	/**
	 * Trasmette il file richiesto come stream di byte.
	 *
	 * @param id Id del file da trasmettere
	 * @return Stream di byte
	 * @throws ParcmanClientFileNotExistsRemoteException File non
	 * presente tra i file condivisi
	 * @throws ParcmanClientFileErrorRemoteException Errore nella
	 * creazione del buffer
	 * @throws RemoteException Eccezione remota
	 */
	public byte[] getFile(int id) throws
		ParcmanClientFileNotExistsRemoteException,
		ParcmanClientFileErrorRemoteException,
		RemoteException;
	/**
	 * Disconnette il client. 
	 *
	 * @throws RemoteException Eccezione Remota.
	 */
	public void disconnect(String message) throws 
		RemoteException;
}


