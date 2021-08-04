/**
 * 
 */
package testcases;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.SignInPage;
import wdMethods.ProjectMethods;

/**
 * @author sivaram
 *
 */
public class Sigin_Test extends ProjectMethods {
	private SignInPage signInPage ;
	private DashboardPage dashboardPage;
	
	public Sigin_Test() {
		testCaseName = "SignIn Test";
		testDescription = "Testing the SignIn module functionality";
	}
	
	@BeforeTest
	public void setUp() {		
		signInPage = new SignInPage();
		dashboardPage = new DashboardPage();
	}
	
	@Test()
	public void signInToTheApplication() throws InterruptedException {
		signInPage.signIn("testingha02@mailinator.com", "Test@1234");
		dashboardPage.accessMyAccountPage();
	}
	
	@Test()
	public void signInToTheApplication2() throws InterruptedException {
		signInPage.signIn("testingha01@mailinator.com", "Test@1234");
		dashboardPage.accessMyAccountPage();
	}
}
