package parcmanserver;

import parcmanclient.RemoteParcmanClient;

/**
 * Contenitore per i dati di sessione di un Client.
 *
 * @author Parcman Tm
 */
public class ClientData
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
    public void setIsAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

    /**
     * Ritorna i privilegi utente.
     *
     * @return privilegi utente
     */
    public boolean getIsAdmin()
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

