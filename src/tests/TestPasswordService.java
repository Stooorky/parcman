package tests;

import loginserver.*;

public class TestPasswordService implements Test
{
	public void run()
	{
		System.out.println("> TestPasswordEqual START");
		this.testEqual();
		System.out.println("> TestPasswordEqual END");
		System.out.println("");
	}


	public void testEqual()
	{
		System.out.print("\ttest equals... ");
		String password = "0q9w8e7r6t5y4u3i2o1p";
		String encPass1 = "";
		String encPass2 = null;
		try 
		{
			encPass1 = PasswordService.getInstance().encrypt(password);
			encPass2 = PasswordService.getInstance().encrypt(password);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("failed.");
		}
		if (encPass1.equals(encPass2))
		{
			System.out.println("ok.");
		}
		else
		{
			System.out.println("failed.");
		}
	}
}
