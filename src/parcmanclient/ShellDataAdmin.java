package parcmanclient;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.rmi.*;

import pshell.*;
import plog.*;
import remoteexceptions.*;
import parcmanserver.RemoteParcmanServerUser;
import database.beans.ShareBean;
import database.beans.SearchBean;

public class ShellDataAdmin extends ShellData
{
	/**
	 * Stub del ParcmanServer.
	 */
	//private RemoteParcmanServerUser parcmanServerStub;

	/**
	* Nome utente.
	*/
	//private String userName;

	/**
	* ParcmanClient.
	*/
	//private ParcmanClient parcmanClient;

	/**
	* Costruttore.
	*
	* @param parcmanServerStub Stub del MainServer della rete Parcman
    * @param parcmanClient Referenza al ParcmanClient
	* @param userName Nome utente
	*/
	public ShellDataAdmin(RemoteParcmanServerUser parcmanServerStub,
            ParcmanClient parcmanClient,
            String userName)
	{
		super(parcmanServerStub, parcmanClient, userName);
	}
}
