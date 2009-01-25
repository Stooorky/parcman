/*
 * Parcman Project, the Italian Arcade Network
 * Copyright (C) 2008-2009 Parcman Tm (Marchi Sirio, Marcantoni Francesco, Emanuele Dona')
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/.
 *
 * E-Mail:	ramyel [at] gmail [dot] com
 * 			sirio.marchi [at] gmail [dot] com
 * 			emanuele.dona [at] gmail [dot] com
 */

package remoteclient;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

import io.Logger;
import io.PropertyManager;
import io.IO;
import io.IOProperties;
import io.IOColor;
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
	 * Utility input/output
	 */
	private IO io;

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
			PropertyManager.getInstance().register("io", "io-client.properties");
			PropertyManager.getInstance().register("logger", "logger-client.properties");
		}
		catch (Exception e)
		{}

		io = new IO(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out), PropertyManager.getInstance().get("io"));
		logger = Logger.getLogger("client-side", PropertyManager.getInstance().get("logger"));

		//InputStreamReader input = new InputStreamReader(System.in);
		//BufferedReader myInput = new BufferedReader(input);
		String userName = null;
		String password = null;

		String loginServerAdress = System.getProperty("remoteclient.loginserveradress");

		println("Parcman, The Italian Arcade Network v1.0.");
		logger.debug("Bootstrap del Client avvenuto correttamente. LoginServerAdress: " + loginServerAdress);

		try
		{
			// Inserimento del nome utente
			print("Inserisci il nome utente (Oppure NUOVO per creare un nuovo account): ");
			userName = new String(io.readLine());

			if (userName.equals("NUOVO"))
			{
				print("Inserisci il nome utente per il nuovo account: ");
				String newUserName = new String(io.readLine());

				print("Inserisci la password: ");
				this.echo(false);
				String newPassword = new String(io.readLine());
				this.echo(true);
				println("");

				print("Reinserisci la password per conferma: ");
				this.echo(false);
				String newPasswordR = new String(io.readLine());
				this.echo(true);
				println("");

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

					println("Account creato con successo, verra' attivato quanto prima.");
					return;
				}
				catch(ParcmanDBServerUserExistRemoteException e)
				{
					error("Registrazione fallita, questo nome utente e' gia' in uso.");
					logger.error("Registrazione fallita, questo nome utente e' gia' in uso.");
				}
				catch(ParcmanDBServerUserNotValidRemoteException e)
				{
					error("Registrazione fallita, nome utente o password non validi.");
					logger.error("Registrazione fallita, nome utente o password non validi.");
				}
				catch(Exception e)
				{
					error("La rete Parcman non e' al momento raggiungibile.");
					logger.error("La rete Parcman non e' al momento raggiungibile.");
				}

				return;
			}
			else
			{
				this.echo(false); 
				// Inserimento della password
				print("Inserisci la password: ");
				password = new String(io.readLine());
				this.echo(true);
				println("");
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
				error("Richiesta di login rifiutata, l'utente e` stato inserito nella blacklist. Ritenta.");
				logger.error("Richiesta di login rifiutata, l'utente e` stato inserito nella blacklist. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserFailedRemoteException)
			{
				error("Richiesta di login rifiutata, username non corretto. Ritenta.");
				logger.error("Richiesta di login rifiutata, username non corretto. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserOrPasswordFailedRemoteException)
			{
				error("Richiesta di login rifiutata, username o password non corratti. Ritenta.");
				logger.error("Richiesta di login rifiutata, username o password non corratti. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserPrivilegeFailedRemoteException)
			{
				error("Richiesta di login rifiutata, l'utente non ha i privilegi adeguati. Ritenta.");
				logger.error("Richiesta di login rifiutata, l'utente non ha i privilegi adeguati. Ritenta.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserIsConnectRemoteException)
			{
				error("Richiesta di login rifiutata, l'utente e` gia` connesso alla rete.");
				logger.error("Richiesta di login rifiutata, l'utente e` gia` connesso alla rete.");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerClientHostUnreachableRemoteException)
			{
			e.printStackTrace();
			e.getCause().printStackTrace();
				error("Impossibile eseguire il login. Il Login Server risulta irraggiungibile. (1)");
				logger.error("Impossibile eseguire il login. Il Login Server risulta irraggiungibile. (1)");
				// e.printStackTrace();
			}
			else if (e.getCause() instanceof LoginServerUserNotAuthorizedRemoteException)
			{
				error("Richiesta di login rifiutata, l'utente non e` autorizzato. Ritenta.");
				logger.error("Richiesta di login rifiutata, l'utente non e` autorizzato. Ritenta.");
				// e.printStackTrace();
			}
			else
			{
				// e.printStackTrace();
			}
		}
		catch(RemoteException e)
		{
			e.printStackTrace();
			e.getCause().printStackTrace();
			error("Impossibile eseguire il Login. Il Login Server risulta irraggiungibile. (2)");
			logger.error("Impossibile eseguire il Login. Il Login Server risulta irraggiungibile. (2)", e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			e.getCause().printStackTrace();
			error("Impossibile eseguire il Login. Il Login Server risulta irraggiungibile. (3)");
			logger.error("Impossibile eseguire il Login. Il Login Server risulta irraggiungibile. (3)", e);
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

	/**
	 * Wrapper per il metodo print
	 */
	protected void print(String msg)
	{
		io.print(PropertyManager.getInstance().getProperty("io", IOProperties.PROP_TAB_SPACE) + msg);
	}

	/** 
	 * Wrapper per il metodo println
	 */
	protected void println(String msg)
	{
		io.println(PropertyManager.getInstance().getProperty("io", IOProperties.PROP_TAB_SPACE) + msg);
	}

	/**
	 * Wrapper per stampare un errore.
	 */
	protected void error(String msg)
	{
		io.println(	PropertyManager.getInstance().getProperty("io", IOProperties.PROP_TAB_SPACE) 
				+ msg, 
				IOColor.getColor(PropertyManager.getInstance().getProperty("io", IOProperties.PROP_COLOR_ERROR)));
	}
}

