package parcmanagent;

import java.io.Serializable;

public class ClientDataForAgentStatus implements Serializable
{
	public static final int READY = 0;
	public static final int IN_UPDATE = 1;
	public static final int UPDATED = 2;
	public static final int NO_NEED_UPDATE = 3;
	public static final int UPDATE_FAILURE = 4;
	public static final int NOT_AVAILABLE = 5;
}

