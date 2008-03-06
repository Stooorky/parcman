package tests;

import java.io.*;
import java.util.*;
import java.rmi.*;

import databaseserver.*;
import parcmanserver.*;
import remoteexceptions.*;

public class TestSetup implements Test
{
    private static final String URLRmiRegistry = "gamma20:4242";
    public void run()
    {
		System.out.println("> TestSetup START");
        this.testParcmanServer();
		System.out.println("> TestSetup END");
		System.out.println("");
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
}
