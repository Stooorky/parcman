package remoteclient;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

import io.Logger;
import io.PropertyManager;
import loginserver.RemoteLoginServer;
import parcmanclient.RemoteParcmanClient;
import remoteexceptions.*;

/**
 * Client remoto per utenti.
 *
 * @author Parcman Tm
 */
public class RemoteClientUser
	implements RemoteClient
{
	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 42L;

	/**
	 * Logger 
	 */
	private Logger logger;

	/**
	 * Avvia il Client remoto.
	 */
	public void run()
	{
		try 
		{
			PropertyManager.getInstance().register("io", "io.properties");
			PropertyManager.getInstance().register("logger", "logger-client.properties");
		}
		catch (Exception e)
		{}

		logger = Logger.getLogger("client-side", PropertyManager.getInstance().get("logger"));
		System.out.println("registro il logger client-side");

		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader myInput = new BufferedReader(input);
		String userName = null;
		String password = null;

		String loginServerAdress = System.getProperty("remoteclient.loginserveradress");

		logger.log("Parcman, The Italian Arcade Network v1.0.");
		logger.log("Bootstrap del Client avvenuto correttamente. LoginServerAdress: " + loginServerAdress);

		try
		{
			// Inserimento del nome utente
			logger.log("\tInserisci il nome utente (Oppure NUOVO per creare un nuovo account): ");
			userName = new String(myInput.readLine());

			if (userName.equals("NUOVO"))
			{
				logger.log("\tInserisci il nome utente per il nuovo account: ");
				String newUserName = new String(myInput.readLine());

				logger.log("\tInserisci la password: ");
				this.echo(false);
				String newPassword = new String(myInput.readLine());
				this.echo(true);

				logger.log("\n\tReinserisci la password per conferma: ");
				this.echo(false);
				String newPasswordR = new String(myInput.readLine());
				this.echo(true);
				logger.log("\n");

				if (!newPassword.equals(newPasswordR))
				{
					logger.error("Le password non corrispondono.\n");
					return;
				}

				try
				{
					// Faccio la lookup al server remoto di login
					RemoteLoginServer loginServer = (RemoteLoginServer)Naming.lookup(loginServerAdress);

					loginServer.createAccount(newUserName, newPassword);

					logger.log("Account creato con successo, verra' attivato quanto prima.");
					return;
				}
				catch(ParcmanDBServerUserExistRemoteException e)
				{
					logger.error("Registrazione fallita, questo nome utente e' gia' in uso.");
				}
				catch(ParcmanDBServerUserNotValidRemoteException e)
				{
					logger.error("Registrazione fallita, nome utente o password non validi.");
				}
				catch(Exception e)
				{
					logger.error("La rete Parcman non e' al momento raggiungibile.");
				}

				return;
			}
			else
			{
				this.echo(false); 
				// Inserimento della password
				logger.log("\tInserisci la password: ");
				password = new String(myInput.readLine());
				this.echo(true);
				logger.log("\n");
			}
		}
		catch(IOException e)
		{
			logger.error("Impossibile leggere dallo STDIN.", e);
			System.exit(1);
		}

		try
		{
			// Faccio la lookup al server remoto di login
			RemoteLoginServer loginServer = (RemoteLoginServer)Naming.lookup(loginServerAdress);
			RemoteParcmanClient parcmanClient = loginServer.login(userName, password);

			// Elimino la referenza al LoginServer
			loginServer =  null;
			System.gc();

			// Avvio il MobileServer
			parcmanClient.startConnection();
		}
		catch (ServerException e)
		{
			if (e.getCause() instanceof LoginServerUserInBlacklistRemoteException)
			{
				logger.error("Richiesta di login rifiutata, l'utente e` stato inserito nella blacklist. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserFailedRemoteException)
			{
				logger.error("Richiesta di login rifiutata, username non corretto. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserOrPasswordFailedRemoteException)
			{
				logger.error("Richiesta di login rifiutata, username o password non corratti. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserPrivilegeFailedRemoteException)
			{
				logger.error("Richiesta di login rifiutata, l'utente non ha i privilegi adeguati. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserIsConnectRemoteException)
			{
				logger.error("Richiesta di login rifiutata, l'utente e` gia` connesso alla rete.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerClientHostUnreachableRemoteException)
			{
				logger.error("Impossibile eseguire il login. Il Login Server risulta irraggiungibile. (1)");
				// e.printStackTrace();
			}
			else
			{
				// e.printStackTrace();
			}
		}
		catch(RemoteException e)
		{
			logger.error("Impossibile eseguire il Login. Il Login Server risulta irraggiungibile. (0)", e);
		}
		catch(Exception e)
		{
			logger.error("Impossibile eseguire il Login. Il Login Server risulta irraggiungibile. (1)", e);
		}
	}

	/**
	 * Accende o spegne l'echo sul terminale.
	 *
	 * @param on true per accendere l'echo, false per spegnere l'echo
	 */
	public void echo(boolean on)
	{
		try
		{
			String[] cmd = {"/bin/sh", "-c", "/bin/stty " + (on ? "echo" : "-echo") + " < /dev/tty"};
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		}
		catch (IOException e)
		{
			logger.error("Impossibile disattivare l'echo.");
		}
		catch (InterruptedException e)
		{
			logger.error("Impossibile disattivare l'echo.");
		}
	}
}

