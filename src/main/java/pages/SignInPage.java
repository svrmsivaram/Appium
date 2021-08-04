package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import wdMethods.ProjectMethods;

public class SignInPage extends ProjectMethods {

	public SignInPage() {
		PageFactory.initElements(getDriver(), this);
	}

	/*
	 * -----------------------------------------------------------
	 * ------------------- SignIn page elements ------------------
	 * ----------------------------------------------------------
	 */

	@FindBy(how = How.XPATH, using = "//div/input[@formcontrolname ='email']")
	public WebElement eleEmailFiled;

	@FindBy(xpath = "//div/input[@formcontrolname ='password']")
	public WebElement elePasswordFiled;

	@FindBy(xpath = "//button/span/span[text()='Sign In']")
	public WebElement eleSignInButton;

	@FindBy(xpath = "//a/strong[text()='Create My Account']")
	public WebElement eleCreateMyAccountLink;

	@FindBy(xpath = "//a[text()='Forgot Password?']")
	public WebElement eleForgetPasswordLink;

	@FindBy(xpath = "//h2[contains(.,'A Fresh Approach to Preventive Health')]")
	public WebElement eleHeaderText;

	/*
	 * -----------------------------------------------------------
	 * ------------------- SignIn page Functions ------------------
	 * ----------------------------------------------------------
	 */

	/**
	 * ========================================================================
	 * Author: Sivaramakrishnan Selvakumar Date:01/09/2019 Method Name = signIn
	 * Description: = This method signs user into EHEandME Digital Application.
	 * Input: String emailAddress, String password
	 * ========================================================================
	 * 
	 * @throws InterruptedException
	 */

	public void signIn(String emailAddress, String password) throws InterruptedException {
		type(eleEmailFiled, emailAddress);
		type(elePasswordFiled, password);
		click(eleSignInButton);
		Thread.sleep(15000);
		waitForLoaderToBeGone();
		verifyTitle("Dashboard - EHE");
	}

	public void clickCreateMyAccountLink() {
		click(eleCreateMyAccountLink);
		
	}

}
