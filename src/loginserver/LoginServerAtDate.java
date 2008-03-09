package loginserver;

import java.io.*;

import parcmanserver.RemoteParcmanServer;
import databaseserver.RemoteDBServer;

public class LoginServerAtDate
    implements Serializable
{
    private int activationsCount;
    private RemoteParcmanServer parcmanServerStub;
	private RemoteDBServer dBServerStub;
	private static final long serialVersionUID = 42L;

    public LoginServerAtDate(
        int activationsCount,
        RemoteParcmanServer parcmanServerStub,
		RemoteDBServer dBServerStub)
    {
        this.activationsCount = activationsCount;
        this.parcmanServerStub = parcmanServerStub;
		this.dBServerStub = dBServerStub;
    }

    public int getActivationsCount()
    {
        return this.activationsCount;
    }

    public void setActivationsCount(int activationsCount)
    {
        this.activationsCount = activationsCount;
    }

    public RemoteParcmanServer getParcmanServerStub()
    {
        return this.parcmanServerStub;
    }

    public void setParcmanServerStub(RemoteParcmanServer parcmanServerStub)
    {
        this.parcmanServerStub = parcmanServerStub;
    }

	public RemoteDBServer getDBServerStub()
	{
		return this.dBServerStub;
	}

	public void setDBServerStub(RemoteDBServer dBServerStub)
	{
		this.dBServerStub = dBServerStub;
	}
}

