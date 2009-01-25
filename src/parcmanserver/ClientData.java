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

package parcmanserver;

import java.io.Serializable;

import parcmanclient.RemoteParcmanClient;

/**
 * Contenitore per i dati di sessione di un Client.
 *
 * @author Parcman Tm
 */
public class ClientData
	implements Serializable
{
    /**
     * Indirizzo IP o URL del Client.
     */
    private String host;

    /**
     * Nome utente.
     */
    private String name;

    /**
     * Stub del ParcmanClient.
     */
    private RemoteParcmanClient stub;

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
     * @param stub Stub del ParcmanClient
     * @param isAdmin Privilegi utente
     */
    public ClientData(String host, String name, RemoteParcmanClient stub, boolean isAdmin)
    {
        this.host = host;
        this.name = name;
        this.stub = stub;
        this.isAdmin = isAdmin;
        this.version = 0;
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
     * Ritorna lo Stub.
     *
     * @return Stub del ParcmanClient
     */
    public RemoteParcmanClient getStub()
    {
        return stub;
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
     * Assegna lo Stub.
     *
     * @param stub Stub del ParcmanClient
     */
    public void setStub(RemoteParcmanClient stub)
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
}

