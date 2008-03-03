package remoteclient;

import plog.*;

/**
 * Client remoto per utenti amministratori.
 *
 * @author Parcman Tm
 */
public class RemoteClientAdmin implements RemoteClient
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
		PLog.debug("RemoteClientAdmin.run", "Metodo run di RemoteClientAdmin avviato.");
	}
}
