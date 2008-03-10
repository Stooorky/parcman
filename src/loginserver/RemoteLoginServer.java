package loginserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteexceptions.*;
import parcmanclient.RemoteParcmanClient;

/**
 * Interfaccia remota del LoginServer.
 *
 * @author Parcman Tm
 */
public interface RemoteLoginServer extends Remote
{
	/**
	 * Esegue il login di un Client alla rete Parcman.
	 * Ritorna un MobileServer esportato nel caso in cui il login abbia successo.
	 * 
	 * @param name Nome utente
	 * @param password Password utente
	 * @return Un MobileServer di tipo ParcmanClient se il login ha successo, null altrimenti
	 * @throws RemoteException Eccezione Remota
	 */
 	public RemoteParcmanClient login(String name, String password) throws
		RemoteException;

	/**
	 * Esegue la registrazione di un nuovo account.
	 * 
	 * @param name Nome utente
	 * @param password Password utente
     * @throws ParcmanDBServerUserExistRemoteException Utente gia' presente nel database
	 * @throws RemoteException Eccezione Remota
	 */
 	public void createAccount(String name, String password) throws
		RemoteException,
        ParcmanDBServerUserExistRemoteException,
        ParcmanDBServerUserNotValidRemoteException;

   /**
     * Ping.
     *
     * @throws RemoteException Eccezione Remota
     */
    public void ping() throws RemoteException;
}

