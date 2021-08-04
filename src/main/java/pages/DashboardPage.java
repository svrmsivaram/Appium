package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import wdMethods.ProjectMethods;

public class DashboardPage extends ProjectMethods {
	
	public DashboardPage() {
		PageFactory.initElements(getDriver(), this);
	}
	
	/*
	 * -----------------------------------------------------------
	 * ---------------- Dashboard page elements ------------------
	 * -----------------------------------------------------------
	 */
    @FindBy(how = How.XPATH, using = "//span[@class='profile-name']")
    public WebElement eleProfileNamelbl;
    
    @FindBy(how = How.XPATH, using = "(//div[@class='tutorial']/..//div[text()=' Skip '])[1]")
    public WebElement eleTutorialscreenskipbutton;
    
    @FindBy(how = How.XPATH, using = "//button[contains(text(),'Account')]")
    public WebElement eleAccountMenubtn; 

    @FindBy(xpath = "//button[contains(text(),'Mail')]")
    public WebElement eleMailMenubtn;
    
    @FindBy(xpath = "//button[contains(text(),'Logout')]")
    public WebElement eleLogoutbtn;
    
    @FindBy(xpath = "//p[text()='Get Access to EHE']")
    public WebElement eleGetAccessText;
    
    @FindBy(xpath = "//li[text()='HEALTH PORTFOLIO']")
    public WebElement eleHealthPortfolioLink;
    
    @FindBy(xpath = "//div[@id='header-menu']//li[contains(.,'Browse all')]")
    public WebElement browserAllBtn;
    
    @FindBy(xpath = "//div[@class='container']//a[text()='Health Assessment']")
    public WebElement healthAssementLink;
    
    @FindBy(xpath = "//div[@class='container']//a[text()='Referrals']")
    public WebElement referralsLink;
    
    @FindBy(xpath = "//div[@class='container']//a[text()='Health Documents']")
    public WebElement healthDocumentLink;
    
    @FindBy(xpath = "//div[@class='container']//a[text()='Schedule an Appointment']")
    public WebElement scheduleAnAppointmentLink;
    
    @FindBy(xpath = "//div[@class='container']//a[text()='My Appointments']")
    public WebElement myAppointmentLink;
    
    @FindBy(xpath = "//div[@class='container']//a[text()='INBOX']")
    public WebElement messageInboxLink;

    @FindBy(xpath = "//div[@class='container']//a[text()='COMPOSE']")
    public WebElement messageComposeLink;
    
    @FindBy(xpath = "//div[@class='container']//a[text()='Profile']")
    public WebElement profileLink;
    
    @FindBy(xpath = "//div[@class='container']//a[text()='Log Out']")
    public WebElement logoutLink;

    
    /*
	 * -----------------------------------------------------------
	 * ----------------- Dashboard page Functions ----------------
	 * -----------------------------------------------------------
	 */
    
    /**
	 * ========================================================================
	 * Author: Sivaramakrishnan Selvakumar 
	 * Date:01/09/2019 
	 * Method Name = accessMyAccountPage 
	 * Description: = This method used to access the MyAccount Page. 
	 * Input: none.
	 * ========================================================================
     * @throws InterruptedException 
	 */
	public void accessMyAccountPage() throws InterruptedException{
		click(eleProfileNamelbl);
		Thread.sleep(1000);
		click(eleAccountMenubtn);
		waitForLoaderToBeGone();
		verifyTitle("My Account - EHE");
	}
	
	/**
	 * ========================================================================
	 * Author: Sivaramakrishnan Selvakumar 
	 * Date:01/09/2019 
	 * Method Name = accessMyAccountPage 
	 * Description: = This method used to access the MyAccount Page. 
	 * Input: none.
	 * ========================================================================
	 * @throws InterruptedException 
	 */
	public void accessMessagingPage() throws InterruptedException{
		click(eleProfileNamelbl);
		Thread.sleep(1000);
		click(eleMailMenubtn);
		waitForLoaderToBeGone();
		verifyTitle("Messaging - EHE");
	}
	
	public void verifyUnMatchedUser(){
		waitForLoaderToBeGone();
		skiptutorial();
		verifyDisplayed(eleGetAccessText);
	}
    
	public void verifyMatchedUser(){
		waitForLoaderToBeGone();
		skiptutorial();
		isNotDisplayed(eleGetAccessText);
	}

	public void accessHealthPortfolio(){
		click(eleHealthPortfolioLink);
		waitForLoaderToBeGone();
		verifyTitle("Wellness Summary - EHE");
	}
	
	public void logOut() throws InterruptedException{
		click(eleProfileNamelbl);
		Thread.sleep(1000);
		click(eleLogoutbtn);
		waitForLoaderToBeGone();
		verifyTitle("Login - EHE");
	}
	
	public void accessHAfromBrowseAll() throws InterruptedException{
		click(browserAllBtn);
		 Thread.sleep(1000);
		 click(healthAssementLink);
		 Thread.sleep(4000);
		 verifyTitle("Health Assessment - EHE");
	}
	
	public void accessHealthDocumentfromBrowseAll() throws InterruptedException{
		click(browserAllBtn);
		 Thread.sleep(1000);
		 click(healthDocumentLink);
		 Thread.sleep(4000);
		 verifyTitle("Health Documents - EHE");
	}
	
	public void accessreferralsfromBrowseAll() throws InterruptedException{
		click(browserAllBtn);
		 Thread.sleep(1000);
		 click(referralsLink);
		 Thread.sleep(4000);
		 verifyTitle("Referrals - EHE");
	}
	
	public void accessSchAppfromBrowseAll() throws InterruptedException{
		click(browserAllBtn);
		 Thread.sleep(1000);
		 click(scheduleAnAppointmentLink);
		 Thread.sleep(4000);
		 verifyTitle("Book Appointment - EHE");
		 waitForLoaderToBeGone();
	}
	
	public void accessMyAppfromBrowseAll() throws InterruptedException{
		 click(browserAllBtn);
		 Thread.sleep(1000);
		 myAppointmentLink.click();
		 Thread.sleep(4000);
		 verifyTitle("My Appointments - EHE");
	}
	
	public void accessMessageInboxfromBrowseAll() throws InterruptedException{
		 click(browserAllBtn);
		 Thread.sleep(1000);
		 click(messageInboxLink);
		 Thread.sleep(4000);
		 verifyTitle("Messaging - EHE");
	}
	
	public void accessMessageComposefromBrowseAll() throws InterruptedException{
		 click(browserAllBtn);
		 Thread.sleep(1000);
		 click(messageComposeLink);
		 Thread.sleep(4000);
		 verifyTitle("Messaging - Compose - EHE");
	}
	
	public void accessProfilefromBrowseAll() throws InterruptedException{
		 click(browserAllBtn);
		 Thread.sleep(1000);
		 click(profileLink);
		 Thread.sleep(4000);
		 verifyTitle("My Account - EHE");
	}
	
	public void accessLogOutBrowseAll() throws InterruptedException{
		 click(browserAllBtn);
		 Thread.sleep(1000);
		 click(logoutLink);
		 Thread.sleep(4000);
		 verifyTitle("Login - EHE");
	}
	
	public void  skiptutorial() {
		try{
			if(isDisplayed(eleTutorialscreenskipbutton)){
				click(eleTutorialscreenskipbutton);
			}
		} catch(Exception exception){
			//nothing to do
		}
	}
}
