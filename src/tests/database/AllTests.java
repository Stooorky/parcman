package tests.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runner.Description;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestDBSharings.class,
	TestShareBean.class,
	TestDBManager.class
	})
public class AllTests
{
}
