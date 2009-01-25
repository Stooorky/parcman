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
import parcmanagent.UpdateList;
import parcmanagent.RemoteParcmanAgentClient;

/**
 * Interfaccia remota del ParcmanClient per l'agente remoto.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanClientAgent
	extends Remote, Serializable
{
    /**
     * Restituisce true se il ParcmanClient ha bisogno di eseguire un
     * update, cioe' l'UpdateList per la versione fornita non e' vuota.
     *
     * @param versione Versione della SharingList
     * @return true se il ParcmanClient ha bisogno di eseguire un update
     * della lista dei file condivisi
     * @throws RemoteException Eccezione remota
     */
    public boolean haveAnUpdate(int version) throws
        RemoteException;

    /**
     * Permette il trasferimento di un agente remoto.
     * Questa funzione lancia il metodo run dell'agente ricevuto come
     * parametro.
     *
     * @param parcmanAgent Stub dell'agente remoto
     * @throws RemoteException Eccezione remota
     */
    public void transferAgent(RemoteParcmanAgentClient parcmanAgent) throws
        RemoteException;

    /**
     * Restituisce la lista di Update a partire dalla versione.
     *
     * @param version Versione della SharingList
     * @return UpdateList per la versione data
     * @throws RemoteException Eccezione remota
     */
    public UpdateList getUpdateList(int version) throws
        RemoteException;
}

