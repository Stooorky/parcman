package tests;

import java.io.*;
import java.util.*;
import java.rmi.*;

import databaseserver.*;
import parcmanserver.*;
import remoteexceptions.*;
import database.beans.*;

public class TestSetup implements Test
{
    private static final String URLRmiRegistry = "localhost:4242";
    public void run()
    {
		System.out.println("> TestSetup START");
        this.testParcmanServer();
        this.testDBServer();
		System.out.println("> TestSetup END");
		System.out.println("");
		return;
    }

    public void testParcmanServer()
    {
        System.out.print("\ttest ParcmanServer...");
        String name = "//" + this.URLRmiRegistry + "/ParcmanServer";

        try
        {
            RemoteParcmanServer obj = (RemoteParcmanServer)Naming.lookup(name);
            obj.ping();
            System.out.println("ok");
        }
        catch (Exception e)
        {
            System.out.println("failed");
        }
    }

    public void testDBServer()
    {
        System.out.print("\ttest DBServer...");
        String name = "//" + this.URLRmiRegistry + "/DBServer";

        try
        {
            RemoteDBServer obj = (RemoteDBServer)Naming.lookup(name);
            UserBean user = new UserBean();
            user.setName("Francesco");
            user.setPassword("FrancescoPwd");
            user.setPrivilege("AllPrivilege");
            obj.addUser(user);
            UserBean userget = obj.getUser("Francesco");
            if (!userget.getName().equals("Francesco"))
                System.out.println("failed (userget)");
            System.out.println("ok");
        }
        catch (Exception e)
        {
            System.out.println("failed");
            e.printStackTrace();
        }
    }
}
