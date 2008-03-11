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
     * Costruttore.
     *
     * @param host Indirizzo IP o URL
     * @param stub Stub del ParcmanClient
     */
    public ClientData(String host, RemoteParcmanClient stub)
    {
        this.host = host;
        this.stub = stub;
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
     * Assegna lo Stub.
     *
     * @param stub Stub del ParcmanClient
     */
    public void setStub(RemoteParcmanClient stub)
    {
        this.stub = stub;
    }
}

