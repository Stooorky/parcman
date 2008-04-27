package tests.database;

import java.io.*;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import database.DBSharings;
import database.beans.ShareBean;

public class TestDBSharings
{
	private static String filename = "./dbSharings.xml";
	private static File f;
	private static DBSharings db;
	private static ShareBean bean1, bean2;

	@BeforeClass
	public static void init()
		throws Exception
	{
		System.out.println("\nTestDBSharings:");
		f= new File(filename);

		db = new DBSharings(filename);

		bean1 = new ShareBean();
		bean1.setId("id1");
		bean1.setUrl("file:/tmp/DBSharing1.tmp");
		bean1.setOwner("owner1");
		bean1.setHash("hash1");
		bean1.addKeyword("key1");
		bean1.addKeyword("key2");

		bean2 = new ShareBean();
		bean2.setId("id2");
		bean2.setUrl("file:/tmp/DBSharing2.tmp");
		bean2.setOwner("owner2");
		bean2.setHash("hash2");
		bean2.addKeyword("key2");
		bean2.addKeyword("key3");
	}

	@AfterClass
	public static void end()
		throws Exception 
	{
		System.out.println();
		if (f.exists())
			f.delete();
	}

	@Test
	public void creation()
		throws Exception
	{
		System.out.println("\tcreation: ");
		DBSharings dbCreation = new DBSharings(filename);
		assertNotNull(dbCreation);
	}

	@Test
	public void addShare()
		throws Exception 
	{
		System.out.println("\taddShare: ");
		db.addShare(bean1);
		assertEquals(1, db.getSize());
		db.addShare(bean2);
		assertEquals(2, db.getSize());
	}

	@Test 
	public void saveAndLoad()
		throws Exception
	{
		System.out.println("\tsaveAndLoad: ");
		db.save();
		assertTrue(f.exists());
		db.load();
		assertEquals(2, db.getSize());
	}

	@Test 
	public void getShareByName()
		throws Exception 
	{
		System.out.println("\tgetShareByName: ");
		String name = "DBSharing1.tmp";
		ShareBean bean = db.getShareByName(name);
		assertNotNull(bean);
		assertEquals(bean.getName(), name);
	}

	@Test 
	public void getShareById()
		throws Exception
	{
		System.out.println("\tgetShareById: ");
		String id = "id2";
		ShareBean bean = db.getShareById(id);
		assertNotNull(bean);
		assertEquals(bean.getId(), id);
	}

	@Test
	public void getShareByTag()
		throws Exception 
	{
		System.out.println("\tgetShareByTag: ");
		String tag1 = "key1";
		String tag2 = "key2";
		ShareBean bean = db.getShareByTag(tag1);
		assertNotNull(bean);
		assertTrue(bean.hasKeyword(tag1));
		assertTrue(bean.hasKeyword(tag2));
	}

	@Test 
	public void removeShareByIndex()
		throws Exception 
	{
		System.out.println("\tremoveShareByIndex: ");
		int index = 0;
		assertEquals(2, db.getSize());
		db.removeShareByIndex(index);
		assertEquals(1, db.getSize());
	}

	@Test
	public void removeShareById()
		throws Exception
	{
		System.out.println("\tremoveShareById: ");
		String id = "id2";
		assertEquals(1, db.getSize());
		db.removeShareById(id);
		assertEquals(0, db.getSize());
	}
}
