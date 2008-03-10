package tests;

public class RunTests
{
	public static void main(String args[])
	{
		TestDBManager testDBManager = new TestDBManager();
		testDBManager.run();
		TestPasswordService testPasswordService = new TestPasswordService();
		testPasswordService.run();
		//TestSetup testSetup = new TestSetup();
		//testSetup.run();
	}
}
