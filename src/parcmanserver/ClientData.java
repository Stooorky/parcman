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
     * Stub del ParcmanClient.
     */
    private RemoteParcmanClient stub;

    /**
     * Versione della lista dei file condivisi.
     */
    private int version;

    /**
     * Costruttore.
     *
     * @param host Indirizzo IP o URL
     * @param stub Stub del ParcmanClient
     */
    public ClientData(String host, RemoteParcmanClient stub)
    {
        this.host = host;
        this.stub = stub;
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

