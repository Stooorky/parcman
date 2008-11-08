package tests.database;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Ignore;
import static org.junit.Assert.*;

import java.io.File;

import database.beans.ShareBean;

public class TestShareBean
{
	private static ShareBean bean0, bean1, bean2, bean3;
	private static File f;
	private static String url;
	//private static String file = "file:/tmp/ShareBeanTestFile";

	@BeforeClass
	public static void init()
		throws Exception
	{
		System.out.println("\nTestShareBean:");
		bean0 = new ShareBean();
		bean1 = new ShareBean();
		bean2 = new ShareBean();
		bean3 = new ShareBean();

		// create the test file
		f = File.createTempFile("TestShareBean", null);
		url = "file:" + f.getPath();
		f.deleteOnExit();
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
		ShareBean bean = new ShareBean();
		assertNotNull(bean);
	}

	@Test 
	public void id()
		throws Exception
	{
		System.out.println("\tid: ");
		assertEquals(0, bean0.getId());
		bean0.setId(1);
		assertEquals(1, bean0.getId());
	}

	@Test 
	public void hash()
		throws Exception
	{
		System.out.println("\thash: ");
		assertNull(bean0.getHash());
		bean0.setHash("hash");
		assertEquals(bean0.getHash(), "hash");
	}

	@Test
	public void owner()
		throws Exception
	{
		System.out.println("\towner: ");
		assertNull(bean0.getOwner());
		bean0.setOwner("owner");
		assertEquals(bean0.getOwner(), "owner");
	}

	@Test
	public void url()
		throws Exception
	{
		System.out.println("\turl: ");
		assertNull(bean0.getUrl());
		bean0.setUrl(url);
		assertEquals(bean0.getUrl().getPath(), f.getPath());
	}

	@Test 
	public void keywords()
		throws Exception
	{
		System.out.println("\tkeywords: ");
		assertEquals(bean0.getKeywords().size(), 0);
		bean0.addKeyword("key1");
		assertTrue(bean0.hasKeyword("key1"));
		assertFalse(bean0.hasKeyword("key2"));
		bean0.addKeyword("key2");
		assertTrue(bean0.hasKeyword("key1"));
		assertTrue(bean0.hasKeyword("key2"));
		java.util.Vector<String> v = new java.util.Vector<String>();
		v.add("key1");
		v.add("key2");
		assertEquals(bean0.getKeywords(), v);
	}

	@Test 
	public void equalityEmpty()
		throws Exception
	{
		System.out.println("\tequalityEmpty: ");
		assertEquals(bean1, bean2);
		assertEquals(bean1, bean3);
		assertEquals(bean2, bean3);
	}

	@Test
	public void equalityValid()
		throws Exception 
	{
		System.out.println("\tequalityValid: ");
		bean1.setId("id");
		bean1.setHash("hash");
		bean1.setOwner("owner");
		bean1.setUrl(url);
		bean1.addKeyword("key");

		bean2.setId("id");
		bean2.setHash("hash");
		bean2.setOwner("owner");
		bean2.setUrl(url);
		bean2.addKeyword("key");

		assertEquals(bean1, bean2);
	}

	@Test(expected = java.lang.AssertionError.class)
	public void equalityInvalid1To3()
		throws Exception 
	{
		System.out.println("\tequalityInvalid1To3: ");

		bean3.setId("id3");
		bean3.setHash("hash");
		bean3.setOwner("owner");
		bean3.setUrl(url);
		bean3.addKeyword("key");

		assertEquals(bean1, bean3);
	}
	
	@Test(expected = java.lang.AssertionError.class)
	public void equalityInvalid2To3()
		throws Exception 
	{
		System.out.println("\tequalityInvalid2To3: ");
		assertEquals(bean2, bean3);
	}

	@Test
	public void validityEmpty()
		throws Exception
	{
		System.out.println("\tvalidityEmpty: ");

		ShareBean bean = new ShareBean();
		assertFalse(bean.validate());	
	}

	@Test
	public void validityValid()
		throws Exception
	{
		System.out.println("\tvalidityValid: ");
		assertTrue(bean1.validate());
	}
}
