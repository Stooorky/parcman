package tests;

import java.io.*;
import java.util.*;

import dbmanager.*;
import dbmanager.beans.*;
import dbmanager.xmlhandlers.*;

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
		System.out.println("> TestDBManager END");
		System.out.println("");
	}

	public void testSingleton()
	{
		System.out.print("\ttest singleton... ");
		String dbDirectory = "./dbfiles/";
		NewDBManager manager1 = NewDBManager.getInstance(dbDirectory);
		NewDBManager manager2 = NewDBManager.getInstance(dbDirectory);
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
		NewDBManager manager = NewDBManager.getInstance(dbDirectory);
		manager.add("USERS", new NewDBUsers(dbUsersFile));
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
		NewDBManager manager = NewDBManager.getInstance(dbDirectory);
		manager.add("USERS", new NewDBUsers(dbUsersFile));
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
		NewDBManager manager = NewDBManager.getInstance(dbDirectory);
		manager.add("USERS", new NewDBUsers(dbUsersFile));
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
		NewDBManager manager = NewDBManager.getInstance(dbDirectory);
		manager.add("USERS", new NewDBUsers(dbUsersFile));
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
}
