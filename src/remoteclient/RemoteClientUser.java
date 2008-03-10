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
public class RemoteClientUser implements RemoteClient
{
	private static final String LOGIN_SERVER_ADRESS = "//gamma10:1098/LoginServer";
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

        System.out.println("Parcman, The Italian Arcade Network v1.0.");
        System.out.println("Bootstrap del Client avvenuto correttamente.");

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
                String newPassword = new String(myInput.readLine());

                System.out.print("\tReinserisci la password per conferma: ");
                String newPasswordR = new String(myInput.readLine());

                if (!newPassword.equals(newPasswordR))
                {
                    System.out.print("Le password non corrispondono.\n");
                    return;
                }

                try
                {
    			    // Faccio la lookup al server remoto di login
                    RemoteLoginServer loginServer = (RemoteLoginServer)Naming.lookup(this.LOGIN_SERVER_ADRESS);
                    
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
			    // Inserimento della password
			    System.out.print("\tInserisci la password: ");
			    password = new String(myInput.readLine());
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
			RemoteLoginServer loginServer = (RemoteLoginServer)Naming.lookup(this.LOGIN_SERVER_ADRESS);
			RemoteParcmanClient parcmanServer = (RemoteParcmanClient)loginServer.login(userName, password);
			
			if (parcmanServer == null)
			{
				System.out.println("Spiacente, nome utente o password errati.");
				return;	
			}
            
            // Esporto il server remoto
            UnicastRemoteObject.exportObject(parcmanServer);
			// Avvio il MobileServer
            parcmanServer.startConnection();
		}
		catch(Exception e)
		{
			PLog.err(e, "RemoteClientUser.run", "Impossibile eseguire il Login.");
            e.printStackTrace();
		}
	}
}

