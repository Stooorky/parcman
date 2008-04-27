package tests.database;

import java.io.File;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import database.DBManager;
import database.beans.ShareBean;
import database.DBSharings;

public class TestDBManager
{
	private static DBSharings dbSharings;
	private static String dbSharingsName;
	private static File dbSharingsFile;
	private static ShareBean bean;

	@BeforeClass
	public static void init()
		throws Exception 
	{
		System.out.println("\nTestDBMananger");
		String filename = "./dbSharings.xml";
		dbSharings = new DBSharings("./dbSharings.xml");
		dbSharingsName = "sharings";
		dbSharingsFile = new File(filename);

		bean = new ShareBean();
		bean.setId("id1");
		bean.setUrl("file:/tmp/DBSharing1.tmp");
		bean.setOwner("owner1");
		bean.setHash("hash1");
		bean.addKeyword("key1");
		bean.addKeyword("key2");

		dbSharings.addShare(bean);
	}
	
	@AfterClass
	public static void end()
		throws Exception 
	{
		System.out.println();
	}

	@Test
	public void creation()
		throws Exception
	{
		System.out.println("\tcreation: ");
		assertNotNull(DBManager.getInstance());
	}

	@Test 
	public void singleton()
		throws Exception
	{
		System.out.println("\tsingleton: ");
		assertEquals(DBManager.getInstance(), DBManager.getInstance());
	}

	@Test
	public void clear()
		throws Exception
	{
		System.out.println("\tclear: ");
		DBManager.getInstance().clear();
		assertEquals(0, DBManager.getInstance().list().size());
	}

	@Test
	public void add()
		throws Exception
	{
		System.out.println("\tadd: ");
		DBManager.getInstance().add(dbSharingsName, dbSharings);
		assertEquals(1, DBManager.getInstance().list().size());
		assertEquals(dbSharingsName, DBManager.getInstance().list().firstElement());
		DBManager.getInstance().clear();
	}

	@Test
	public void list()
		throws Exception
	{
		System.out.println("\tlist: ");
		assertEquals(0, DBManager.getInstance().list().size());
		DBManager.getInstance().add(dbSharingsName, dbSharings);
		assertEquals(1, DBManager.getInstance().list().size());
		DBManager.getInstance().clear();	
	}

	@Test 
	public void saveAndLoad()
		throws Exception
	{
		System.out.println("\tsaveAndLoad: ");
		DBManager.getInstance().add(dbSharingsName, dbSharings);
		assertFalse(dbSharingsFile.exists());
		DBManager.getInstance().save();
		assertTrue(dbSharingsFile.exists());

		DBManager.getInstance().load();
		assertEquals(1, dbSharings.getSize());

		dbSharings.getSharings().clear();

		dbSharingsFile.delete();
		DBManager.getInstance().clear();
	}

	@Test
	public void saveAndLoadByName()
		throws Exception
	{
		System.out.println("\tsaveAndLoadByName: ");
		DBManager.getInstance().add(dbSharingsName, dbSharings);
		assertFalse(dbSharingsFile.exists());
		DBManager.getInstance().save(dbSharingsName);
		assertTrue(dbSharingsFile.exists());

		DBManager.getInstance().load(dbSharingsName);

		assertEquals(1, dbSharings.getSize());

		dbSharings.getSharings().clear();

		dbSharingsFile.delete();
		DBManager.getInstance().clear();
	}

	@Test 
	public void drop()
		throws Exception
	{
		System.out.println("\tdrop: ");
		DBManager.getInstance().drop(dbSharingsName);
		assertEquals(0, DBManager.getInstance().list().size());
	}
}
