package loginserver;

import java.io.*;

import parcmanserver.RemoteParcmanServer;

public class LoginServerAtDate
    implements Serializable
{
    private int activationsCount;
    private RemoteParcmanServer remoteParcmanServerStub;

    public LoginServerAtDate(
        int activationsCount,
        RemoteParcmanServer remoteParcmanServerStub)
    {
        this.activationsCount = activationsCount;
        this.remoteParcmanServerStub = remoteParcmanServerStub;
    }

    public int getActivationsCount()
    {
        return this.activationsCount;
    }

    public void setActivationsCount(int activationsCount)
    {
        this.activationsCount = activationsCount;
    }

    public RemoteParcmanServer getRemoteParcmanServerStub()
    {
        return this.remoteParcmanServerStub;
    }

    public void setRemoteParcmanServerStub(RemoteParcmanServer remoteParcmanServerStub)
    {
        this.remoteParcmanServerStub = remoteParcmanServerStub;
    }
}

