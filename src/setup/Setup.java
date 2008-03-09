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
import loginserver.*;

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
     * setup.policyGroup, file di policy dei gruppi di attivazione
     * setup.dbDirectory, path della directory radice del database
     * setup.implCodebase, codebase dei gruppi di attivazione
     * setup.loginServerClass, classe del server di login
     */
    public static void main(String[] args)
    {
        // Lancio il Security Manager
        System.setSecurityManager(new RMISecurityManager());

        String policyGroup = System.getProperty("setup.policyGroup");
		String dbDirectory = System.getProperty("setup.dbDirectory");
        String implCodebase = System.getProperty("setup.implCodebase");
        String loginServerClass = System.getProperty("setup.loginServerClass");

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
            RemoteDBServer dBServer = new DBServer(dbDirectory);

            PLog.debug("Setup", "Creo un'istanza del ParcmanServer.");
            // Creo il ParcmanServer
            RemoteParcmanServer parcmanServer = new ParcmanServer(dBServer);

            PLog.debug("Setup", "Creo e registro il primo gruppo di attivazione.");
            // Creo e registro il primo gruppo di attivazione
            ActivationGroupDesc groupDesc = new ActivationGroupDesc(prop, null);
            ActivationGroupID groupID = ActivationGroup.getSystem().registerGroup(groupDesc);
            PLog.debug("Setup", "L'identificazione del primo gruppo di attivazione e': " + groupID);

            // Creo il MashalledObject per il LoginServer
            LoginServerAtDate loginServerAtDate = new LoginServerAtDate(0, parcmanServer, dBServer);
            ActivationDesc actDesc = new ActivationDesc(groupID, loginServerClass, implCodebase, new MarshalledObject(loginServerAtDate));

            PLog.debug("Setup", "Creo un'istanza del LoginServer.");
            // Creo e registro il LoginServer
            RemoteLoginServer loginServer = (RemoteLoginServer)Activatable.register(actDesc);

            PLog.debug("Setup", "Registro per i Test il DBServer e il ParcmanServer sul registro RMI alla porta 4242.");
            // TODO Togliere la registrazione al registro RMI dei server.
            //reg.rebind("DBServer", dBServer);
            //reg.rebind("ParcmanServer", parcmanServer);
            Naming.rebind("//:1098/LoginServer", loginServer);
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

