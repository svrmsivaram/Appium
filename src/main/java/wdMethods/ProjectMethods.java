package wdMethods;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;



import utils.DataInputProvider;

public class ProjectMethods extends ApMethods {
	
	protected String browserName;
	protected String dataSheetName;
	protected String testCaseName;
	protected String testDescription;
	
	@BeforeSuite
	public void beforeSuite() throws FileNotFoundException, IOException{
		startResult();
		
		
	}
	@BeforeClass
	public void beforeClass(){		
		
	}

	@BeforeTest
	public void beforeTest(ITestContext context){
		testCaseName = getClass().getSimpleName();
		System.out.println(testDescription);
		launchApp();
		startTestModule(testCaseName,testDescription);	
	}
	
	@BeforeMethod
	public void beforeMethod(){
		/*test = startTestCase(testNodes);   //TO-DO
		test.assignCategory(category);
		test.assignAuthor(authors);*/
		
	}
		
	@AfterSuite
	public void afterSuite(){
		endResult();
	}

	@AfterTest
	public void afterTest(){
	}
	
	@AfterMethod
	public void afterMethod(){
		closeApp();
	}
	
	@DataProvider(name="fetchData")
	public Object[][] getData(){
		return DataInputProvider.getSheet(dataSheetName);		
	}
	
	
	
	
	public void waitForLoaderToBeGone() {
		try {
			new WebDriverWait(getDriver(), 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ngx-spinner/div")));
			new WebDriverWait(getDriver(), 20)
					.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//ngx-spinner/div")));

		} catch (Exception e) {

		}

	}

	/**
	 * This method used to generate random number in the given range
	 * 
	 * @author Coda Global
	 * @param low  - lowest number
	 * @param high - highest number
	 * @return
	 * 
	 */
	public int getRandomNumberGivenRange(int low, int high) {
		Random r = new Random();
		int result = r.nextInt(high - low) + low;
		return result;
	}

	/**
	 * This method used to generate random string in small letter with given length
	 * 
	 * @author Coda Global
	 * @param stringlenth - specify required string length.
	 * @return
	 */

	public String generateRandomStringSmallLetter(int stringlenth) {
		String stringValue = "test";
		for (int i = 0; i < stringlenth - 4; i++) {
			stringValue += (char) getRandomNumberGivenRange(97, 122);
		}
		return stringValue;
	}

	/**
	 * This method used to generate random string with capital letter with given
	 * length
	 * 
	 * @author Coda Global
	 * @param stringlenth - specify required string length.
	 * @return
	 */

	public String generateRandomStringCapitalLetter(int stringlenth) {
		String stringValue = "TEST";
		for (int i = 0; i < stringlenth - 4; i++) {
			stringValue += (char) getRandomNumberGivenRange(65, 90);
		}
		return stringValue;
	}

	/**
	 * This method used to generate random 10 digit cell phone number with 10.
	 * 
	 * @author Coda Global
	 * @return
	 */

	public String generateRandomCellPhoneNum() {
		String stringValue = "9";
		for (int i = 0; i < 11; i++) {
			if (i == 2 || i == 6) {
				stringValue += ".";
			} else {
				stringValue += (char) getRandomNumberGivenRange(48, 57);
			}
		}
		return stringValue;
	}

	/**
	 * This method used to generate random email.
	 * 
	 * @author Coda Global
	 * @return
	 */

	public String generateRandomEmail() {
		String stringValue = "test";
		for (int i = 0; i < 5; i++) {
			stringValue += (char) getRandomNumberGivenRange(97, 122);
		}
		return stringValue + "@mailinator.com";
	}

	/**
	 * This method used to get the month name wile passing the month number.
	 * 
	 * @author Coda Global
	 * @param monthNumber - specify the month number.
	 * @return
	 */

	public String getMonthString(String monthNumber) {
		int result = Integer.parseInt(monthNumber);
		String monthString = new DateFormatSymbols().getMonths()[result - 1];
		return monthString;
	}
	
}
