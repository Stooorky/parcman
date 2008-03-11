package parcmanserver;

import parcmanclient.RemoteParcmanClient;

public class ClientData
{
    private String host;
    private RemoteParcmanClient stub;

    public ClientData(String host, RemoteParcmanClient stub)
    {
        this.host = host;
        this.stub = stub;
    }

    public String getHost()
    {
        return this.host;
    }

    public RemoteParcmanClient getStub()
    {
        return stub;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public void setStub(RemoteParcmanClient stub)
    {
        this.stub = stub;
    }
}
