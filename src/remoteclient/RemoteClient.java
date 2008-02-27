package remoteclient;

import java.io.*;

/**
 * Interfaccia dei Client remoti.
 *
 * @author Parcman Tm
 */
public interface RemoteClient extends Runnable, Serializable
{
	/**
	 * Avvia il Client remoto.
	 */
	public void run();
}
