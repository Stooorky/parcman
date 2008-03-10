package tests;

import java.io.*;
import java.util.*;
import java.rmi.*;

import database.*;
import database.beans.*;
import database.xmlhandlers.*;
import database.exceptions.*;
import databaseserver.*;
import remoteexceptions.*;

public class TestDBManager implements Test
{
	public void run()
	{
		System.out.println("> TestDBManager START");
		this.testSingleton();
		this.testSave();
		this.testCall();
		this.testDrop();
		this.testLoad();
		this.testDB();
		this.testDBServer();
		System.out.println("> TestDBManager END");
		System.out.println("");
	}

	public void testSingleton()
	{
		System.out.print("\ttest singleton... ");
		String dbDirectory = "./dbfiles/";
		DBManager manager1 = DBManager.getInstance();
		DBManager manager2 = DBManager.getInstance();
		if (!manager1.equals(manager2))
		{
			System.out.println("failed.");
		}
		else 
		{
			System.out.println("ok.");
		}
	}

	public void testSave()
	{
		System.out.print("\ttest call... ");
		String dbDirectory = "./dbfiles/";
		String dbUsersFile = "users.xml";
		DBManager manager = DBManager.getInstance();
		manager.add("USERS", new DBUsers(dbUsersFile));
		manager.save();
		File f = new File(dbUsersFile);
		if (f.exists())
		{
			System.out.println("ok.");
			f.delete();
		}
		else
		{
			System.out.println("failed.");
		}
	}

	public void testCall()
	{
		System.out.print("\ttest save... ");
		String dbDirectory = "./dbfiles/";
		String dbUsersFile = "users.xml";
		DBManager manager = DBManager.getInstance();
		manager.add("USERS", new DBUsers(dbUsersFile));
		UserBean user = new UserBean();
		user.setName("pippo");
		user.setPassword("pippo");
		user.setPrivilege("pippo");
		Object[] args = { user };
		try
		{
			manager.call("USERS", "addUser", args);
			System.out.println("ok.");
		} 
		catch (Exception e)
		{
			System.out.println("failed.");
		}
	}

	public void testDrop()
	{
		System.out.print("\ttest drop... ");
		String dbDirectory = "./dbfiles/";
		String dbUsersFile = "users.xml";
		DBManager manager = DBManager.getInstance();
		manager.add("USERS", new DBUsers(dbUsersFile));
		manager.drop("USERS");
		Vector<String> list = manager.list();
		if (list.contains("USERS"))
		{
			System.out.println("failed.");
		}
		else 
		{
			System.out.println("ok.");
		}
	}

	public void testLoad()
	{
		System.out.print("\ttest load...");
		String dbDirectory = "./dbfiles/";
		String dbUsersFile = "users.xml";
		DBManager manager = DBManager.getInstance();
		manager.add("USERS", new DBUsers(dbUsersFile));
		UserBean user = new UserBean();
		user.setName("pluto");
		user.setPassword("pluto");
		user.setPrivilege("pluto");
		Object[] args = { user };
		try
		{
			manager.call("USERS", "addUser", args);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		manager.save();
		File f = new File(dbUsersFile);
		try 
		{
			manager.load();
			//Object[] args1 = { 0 }; 
			//System.out.println(manager.call("USERS", "getUserName", args1));
			Vector<UserBean> users = (Vector<UserBean>) manager.call("USERS", "getUsers", null);
			if (users.size() == 1) 
			{
				System.out.println("ok.");
			} 
			else 
			{
				System.out.println("failed.");
			}
			f.delete();

		}
		catch (Exception e)
		{
			System.out.println("failed.");
			e.printStackTrace();
			f.delete();
		}
	}

    public void testDB()
    {
        System.out.println("\ttest DB...");

		// Test creazione intero ramo DB
        try
        {
            DB db1 = new DB("./dbDirectory");
        }
        catch (ParcmanDBNotCreateException e)
        {
            System.out.println("failed.");
            e.printStackTrace();
        }

		// Test ramo DB  esistente
        try
        {
            DB db2 = new DB("./dbDirectory");
			try
			{   
                UserBean user1 = new UserBean();
                UserBean user2 = new UserBean();

                user1.setName("User1");
                user1.setPassword("Pwd");
                user1.setPrivilege("Priv");
	            user2.setName("User1");
                user2.setPassword("Pwd");
                user2.setPrivilege("Priv");

                db2.addUser(user1);
				db2.addUser(user2);
			}
			catch (ParcmanDBUserExistException e)
			{
				System.out.println("\tok.");
			}
			catch (Exception e)
			{
				System.out.println("\tfailed.");
			}	
        }
        catch (ParcmanDBNotCreateException e)
        {
            System.out.println("\tfailed.");
            e.printStackTrace();
        }
	
		// Elimino i files e la cartella utilizzati nel test
		File[] flist = (new File("./dbDirectory")).listFiles();
		for (int i=0; i<flist.length; i++)
			flist[i].delete();
		(new File("./dbDirectory")).delete();
    }

	public void testDBServer()
	{
		System.out.println("\ttest DBServer...");

		try
		{
			DBServer dBServer = new DBServer("./dbDirectory");
            UserBean newUser = new UserBean();

            newUser.setName("UserServer");
            newUser.setPassword("Pwd");
            newUser.setPrivilege("Priv");
            
            dBServer.addUser(newUser);

			UserBean user = dBServer.getUser("UserServer");
			if (user == null || !user.getName().equals("UserServer"))
				System.out.println("\tfailed.");
			else
				System.out.println("\tok.");
		}
		catch(RemoteException e)
		{
			System.out.println("\tfailed.");
			e.printStackTrace();
		}

		
		// Elimino i files e la cartella utilizzati nel test
		File[] flist = (new File("./dbDirectory")).listFiles();
		for (int i=0; i<flist.length; i++)
			flist[i].delete();
		(new File("./dbDirectory")).delete();
	}
}

