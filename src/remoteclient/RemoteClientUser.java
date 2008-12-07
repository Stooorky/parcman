package remoteclient;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

import plog.*;
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
	 * Avvia il Client remoto.
	 */
	public void run()
	{
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader myInput = new BufferedReader(input);
		String userName = null;
		String password = null;

		String loginServerAdress = System.getProperty("remoteclient.loginserveradress");

		System.out.println("Parcman, The Italian Arcade Network v1.0.");
		System.out.println("Bootstrap del Client avvenuto correttamente. LoginServerAdress: " + loginServerAdress);

		try
		{
			// Inserimento del nome utente
			System.out.print("\tInserisci il nome utente (Oppure NUOVO per creare un nuovo account): ");
			userName = new String(myInput.readLine());

			if (userName.equals("NUOVO"))
			{
				System.out.print("\tInserisci il nome utente per il nuovo account: ");
				String newUserName = new String(myInput.readLine());

				System.out.print("\tInserisci la password: ");
				this.echo(false);
				String newPassword = new String(myInput.readLine());
				this.echo(true);

				System.out.print("\n\tReinserisci la password per conferma: ");
				this.echo(false);
				String newPasswordR = new String(myInput.readLine());
				this.echo(true);
				System.out.print("\n");

				if (!newPassword.equals(newPasswordR))
				{
					System.out.print("Le password non corrispondono.\n");
					return;
				}

				try
				{
					// Faccio la lookup al server remoto di login
					RemoteLoginServer loginServer = (RemoteLoginServer)Naming.lookup(loginServerAdress);

					loginServer.createAccount(newUserName, newPassword);

					System.out.println("Account creato con successo, verra' attivato quanto prima.");
					return;
				}
				catch(ParcmanDBServerUserExistRemoteException e)
				{
					System.out.println("Registrazione fallita, questo nome utente e' gia' in uso.");
				}
				catch(ParcmanDBServerUserNotValidRemoteException e)
				{
					System.out.println("Registrazione fallita, nome utente o password non validi.");
				}
				catch(Exception e)
				{
					System.out.println("La rete Parcman non e' al momento raggiungibile.");
				}

				return;
			}
			else
			{
				this.echo(false); 
				// Inserimento della password
				System.out.print("\tInserisci la password: ");
				password = new String(myInput.readLine());
				this.echo(true);
				System.out.print("\n");
			}
		}
		catch(IOException e)
		{
			PLog.err(e, "RemoteClientUser.run", "Impossibile leggere dallo STDIN.");
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
				System.out.println("Richiesta di login rifiutata, l'utente e` stato inserito nella blacklist. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserFailedRemoteException)
			{
				System.out.println("Richiesta di login rifiutata, username non corretto. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserOrPasswordFailedRemoteException)
			{
				System.out.println("Richiesta di login rifiutata, username o password non corratti. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserPrivilegeFailedRemoteException)
			{
				System.out.println("Richiesta di login rifiutata, l'utente non ha i privilegi adeguati. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserIsConnectRemoteException)
			{
				System.out.println("Richiesta di login rifiutata, l'utente e` gia` connesso alla rete.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerClientHostUnreachableRemoteException)
			{
				System.out.println("Impossibile eseguire il login. Il Login Server risulta irraggiungibile. (1)");
				// e.printStackTrace();
			}
			else
			{
				// e.printStackTrace();
			}
		}
		catch(RemoteException e)
		{
			System.out.println("Impossibile eseguire il Login. Il Login Server risulta irraggiungibile. (0)");
			e.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("Impossibile eseguire il Login. Il Login Server risulta irraggiungibile. (1)");
			e.printStackTrace();
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
			PLog.err("RemoteClientUser.echo", "Impossibile disattivare l'echo.");
		}
		catch (InterruptedException e)
		{
			PLog.err("RemoteClientUser.echo", "Impossibile disattivare l'echo.");
		}
	}
}

