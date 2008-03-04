package tests;

import java.io.*;
import java.util.*;

import database.*;
import database.beans.*;
import database.xmlhandlers.*;
import database.exceptions.*;

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
				db2.addUser("User1", "Password1", "Privilege1");
				db2.addUser("User1", "Password2", "Privilege2");
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
/*
	public void testDBServer()
	{
		System.out.println("\ttest DBServer...");

		// Test ramo DB  esistente
        try
        {
            DB db = new DB("./dbDirectory");
			try
			{
				db.addUser("User1", "Password1", "Privilege1");
				db.addUser("User2", "Password2", "Privilege2");
			}
			catch (ParcmanDBUserExistException e)
			{
				System.out.println("\tfailed.");
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
		
		ServerDB serverDB = new ServerDB("./dbDirectory");
		
		
		// Elimino i files e la cartella utilizzati nel test
		File[] flist = (new File("./dbDirectory")).listFiles();
		for (int i=0; i<flist.length; i++)
			flist[i].delete();
		(new File("./dbDirectory")).delete();

	}
*/
}

