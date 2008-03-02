package tests;

import dbmanager.*;
import dbmanager.beans.*;
import dbmanager.xmlhandlers.*;

public class testNewDBManager
{
	public void test1()
	{
		try
		{
			String dbDirectory = "./files/"; 
			String dbUsersfile = "users.xml";
			NewDBManager man = new NewDBManager(dbDirectory); // creo un dbmanager
			man.add("USERS", new NewDBUsers(dbUsersfile));
			man.save();
			UserBean user1 = new UserBean();
			user1.setName("pippo");
			user1.setPassword("pippo");
			user1.setPrivilege("pippo");
			Object[] args = { user1 };
			man.call("USERS", "addUser", args);
			man.save();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String args[])
	{
		testNewDBManager tests = new testNewDBManager();
		tests.test1();
	}
}
