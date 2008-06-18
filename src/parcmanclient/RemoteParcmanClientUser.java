package parcmanclient;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import remoteexceptions.*;

/**
 * Interfaccia remota del ParcmanClient a livello utente.
 *
 * @author Parcman Tm
 */
public interface RemoteParcmanClientUser
	extends Remote, Serializable
{
    /**
     * Ritorna il nome utente del proprietario della sessione.
     *
     * @return Nome utente del proprietario della sessione
     * @throws RemoteException Eccezione remota
     */
    public String getUserName() throws
        RemoteException;
}


