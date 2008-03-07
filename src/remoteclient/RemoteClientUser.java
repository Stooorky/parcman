package remoteclient;

import java.io.*;

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
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader myInput = new BufferedReader(input);

		try
		{
			// Inserimento del nome utente
			System.out.println("Inserisci il nome utente:");
			String userName = new String(myInput.readLine());

			// Inserimento della password
			System.out.println("Inserisci la Password: ");
			String password = new String(myInput.readLine());
		}
		catch(IOException e)
		{
			PLog.err(e, "RemoteClientUser.run", "Impossibile leggere dallo STDIN.");
			System.exit(1);
		}
	}
}
