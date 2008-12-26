package io;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.LinkedHashMap;

public class LoggerTest
{
	public static void main(String[] args)
	{
		IO io = new IO(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out));
		Logger log = Logger.getLogger("test");
		LoggerTest.testException(io, log);
		LoggerTest.testPrintTable(io, log);
	}

	public static void testException(IO io, Logger log)
	{
		try
		{
			throw new Exception();
		}
		catch (Exception e)
		{
			log.error("Questo e` un errore", e);
		}
	}

	public static void testPrintTable(IO io, Logger log)
	{
		LinkedHashMap<String, Vector<String>> table = new LinkedHashMap<String, Vector<String>>();
		Vector<String> c1 = new Vector<String>();
		Vector<String> c2 = new Vector<String>();
		Vector<String> c3 = new Vector<String>();
		int i=0; int j=10; int k=100;
		while (i < 10)
		{
			c1.add(""+i); c2.add(""+j); c3.add(""+k);
			i++; j++; k++; 
		}
		table.put("COL1", c1); table.put("COL2", c2); table.put("COL3", c3);
		io.printTable(table, "TEST");
	}
}
