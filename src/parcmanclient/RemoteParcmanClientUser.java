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

    /**
     * Trasmette il file richiesto come stream di byte.
     *
     * @param id Id del file da trasmettere
     * @return Stream di byte
     * @throws ParcmanClientFileNotExistsRemoteException File non
     * presente tra i file condivisi
     * @throws ParcmanClientFileErrorRemoteException Errore nella
     * creazione del buffer
     * @throws RemoteException Eccezione remota
     */
    public byte[] getFile(int id) throws
        ParcmanClientFileNotExistsRemoteException,
        ParcmanClientFileErrorRemoteException,
        RemoteException;
}


