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
import io.PropertyManager;
import io.Logger;
import remoteexceptions.*;
import loginserver.*;
import indexingserver.*;
import logserver.*;


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

		// Inizializzo le properties
		try
		{
			PropertyManager.getInstance().register("io", "io.properties");
			PropertyManager.getInstance().register("logger", "logger-server.properties");
			System.out.println(PropertyManager.getInstance().get("logger"));
		}
		catch (Exception e)
		{
			// do nothing
		}

		Logger logger = Logger.getLogger("server-side", PropertyManager.getInstance().get("logger"));

		Properties prop = new Properties();
		prop.put("java.security.policy", policyGroup);
		prop.put("java.class.path", "no_classpath");
		prop.put("java.rmi.dgc.leaseValue", 60000);

		try
		{
	            	// Creo il LogServer
        		RemoteLogServer logServer = new LogServer();
			logger.info("Ho creato un'instanza del LogServer.");
			PLog.info("Setup", "Ho creato un'istanza del LogServer.");

			// Creo il DBServer
			RemoteDBServer dBServer = new DBServer(dbDirectory);
			logger.info("Ho creato un'istanza del DBServer.");
			PLog.info("Setup", "Ho creato un'istanza del DBServer.");

			// Creo il ParcmanServer
			RemoteParcmanServer parcmanServer = new ParcmanServer(dBServer);
			logger.info("Ho creato un'istanza del ParcmanServer.");
			PLog.info("Setup", "Ho creato un'istanza del ParcmanServer.");

			// Creo l'IndexingServer
			RemoteIndexingServer indexingServer = new IndexingServer(dBServer, parcmanServer, logServer);
			logger.info("Ho creato un'istanza dell'IndexingServer.");
			PLog.info("Setup", "Ho creato un'istanza dell'IndexingServer.");

			// Creo e registro il primo gruppo di attivazione
			ActivationGroupDesc groupDesc = new ActivationGroupDesc(prop, null);
			ActivationGroupID groupID = ActivationGroup.getSystem().registerGroup(groupDesc);
			logger.info("Ho creato e registrato il primo gruppo di attivazione.");
			logger.info("L'identificazione del primo gruppo di attivazione e': " + groupID + ".");
			PLog.info("Setup", "Ho creato e registrato il primo gruppo di attivazione.");
			PLog.info("Setup", "L'identificazione del primo gruppo di attivazione e': " + groupID);

			// Creo il MashalledObject per il LoginServer
			LoginServerAtDate loginServerAtDate = new LoginServerAtDate(0, parcmanServer, dBServer);
			ActivationDesc actDesc = new ActivationDesc(groupID, loginServerClass, implCodebase, new MarshalledObject(loginServerAtDate));

			// Creo e registro il LoginServer
			RemoteLoginServer loginServer = (RemoteLoginServer)Activatable.register(actDesc);
			logger.info("Ho creato un'istanza del LoginServer e l'ho registrata al primo gruppo di attivazione.");
			PLog.info("Setup", "Ho creato un'istanza del LoginServer e l'ho registrata al primo gruppo di attivazione.");

			// Registro il LoginServer sul registro RMI avviato dal servizio Rmid
			Naming.rebind("//:1098/LoginServer", loginServer);
			logger.info("Ho registrato il LoginServer sul registro RMI.");
			PLog.info("Setup", "Ho registrato il LoginServer sul registro RMI");
			
		}
		catch(ParcmanDBServerErrorRemoteException e)
		{
			logger.info("Impossibile inizializzare il DBServer. (0)");
			PLog.err(e, "Setup", "Impossibile inizializzare il DBServer. (0)");
			return;
		}
		catch(RemoteException e)
		{
			logger.info("Impossibile eseguire il Setup del sistema. (1)");
			PLog.err(e, "Setup", "Impossibile eseguire il Setup del sistema. (1)");
			return;
		}
		catch(Throwable t)
		{
			logger.info("Impossibile eseguire il Setup del sistema. (2)");
			PLog.err(new Exception(t), "Setup", "Impossibile eseguire il Setup del sistema. (2)");
			return;
		}
	}
}

