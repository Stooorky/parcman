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

package indexingserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;
import parcmanagent.UpdateList;
import parcmanagent.ClientDataForAgent;

/**
 * Interfaccia remota del ParcmanServer per il ParcmanAgent.
 *
 * @author Parcman Tm
 */
public interface RemoteIndexingServerAgent
extends Remote
{
    /**
     * Esegue l'update della lista di file condivisi a partire dalla
     * mappa updateLists.
     *
     * @param updateLists contiene i dati per l'aggiornamento delle
     * liste di file condivisi dagli utenti
     * @param validity Periodo di validita' del ParcmanAgent chiamante
     * @throws RemoteException Eccezione Remota
     */
    public void sendUpdateLists(Map<String, ClientDataForAgent> updateLists, long validity, int identify) throws
        RemoteException;

	/**
	 * Ping.
	 *
	 * @throws RemoteException Eccezione Remota
	 */
	public void ping() throws 
		RemoteException;
}

