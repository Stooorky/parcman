package tests;

public class RunTests
{
	public static void main(String args[])
	{
		TestDBManager testDBManager = new TestDBManager();
		testDBManager.run();
        TestSetup testSetup = new TestSetup();
        testSetup.run();
	}
}
