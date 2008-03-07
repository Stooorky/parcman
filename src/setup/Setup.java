package setup;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.activation.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.Properties;

import databaseserver.*;
import parcmanserver.*;
import plog.*;
import remoteexceptions.*;

/**
 * Setup globale lato server.
 *
 * Parcman Tm
 */
public final class Setup
{
    /**
     * Main.
     * Specificare le seguenti proprieta':
     * java.security.policy, file di policy
     * setup.dbDirectory, path della directory radice del database
     */
    public static void main(String[] args)
    {
        // Lancio il Security Manager
        System.setSecurityManager(new RMISecurityManager());

        String policyGroup = System.getProperty("setup.policy");
		String dbDirectory = System.getProperty("setup.dbDirectory");

        Properties prop = new Properties();
        prop.put("java.security.policy", policyGroup);
        prop.put("java.class.path", "no_classpath");

        try
        {
            PLog.debug("Setup", "Creo il registro RMI in ascolto sulla porta 4242.");
            // Creo il registro RMI
            Registry reg = LocateRegistry.createRegistry(4242);

            PLog.debug("Setup", "Creo un'istanza del DBServer.");
            // Creo il DBServer
            RemoteDBServer dbServer = new DBServer(dbDirectory);

            PLog.debug("Setup", "Creo un'istanza del ParcmanServer.");
            // Creo il ParcmanServer
            RemoteParcmanServer parcmanServer = new ParcmanServer(dbServer);

            PLog.debug("Setup", "Registro per i Test il DBServer e il ParcmanServer sul registro RMI alla porta 4242.");
            // TODO Togliere la registrazione al registro RMI dei server.
            reg.rebind("DBServer", dbServer);
            reg.rebind("ParcmanServer", parcmanServer);
        } // TODO Creare una funzione da lanciare prima di un return per ripulire.
        catch(ParcmanDBServerErrorRemoteException e)
        {
            PLog.err(e, "Setup", "Impossibile inizializzare il DBServer.");
            return;
        }
        catch(RemoteException e)
        {
            PLog.err(e, "Setup", "Impossibile eseguire il Setup del sistema.");
            return;
        }
        catch(Throwable t)
        {
            PLog.err(new Exception(t), "Setup", "Impossibile eseguire il Setup del sistema.");
            return;
        }
    }
}

