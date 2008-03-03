package remoteclient;

import plog.*;

/**
 * Client remoto per utenti.
 *
 * @author Parcman Tm
 */
public class RemoteClientUser implements RemoteClient
{
	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Avvia il Client remoto.
	 */
	public void run()
	{
		PLog.debug("RemoteClientUser.run", "Metodo run di RemoteClientUser avviato.");
	}
}
