package wdMethods;



import org.openqa.selenium.remote.RemoteWebDriver;

public interface WdMethods {


	public RemoteWebDriver launchApp();

	boolean launchActivity(String appPackage, String appActivity);

	boolean verifyAndInstallApp(String appPackage, String appPath);

	void sleep(int mSec);

	void printContext();

	boolean switchview();

	boolean clickByID(String id);

	boolean clickByAccessebilityID(String id);

	boolean clickByXpath(String xpath);

	boolean verifyContentDescIsDisplayed(String xpath);

	boolean clickByLinkText(String LinkText);

	void enterTextByID(String id, String data);

	void pressEnter();

	void enterTextByXpath(String xpath, String data);

	long takeSnap();

	boolean verifyTextByID(String id, String data);

	boolean scrollDownInBrowser(int val);

	boolean backButton();

	boolean scrollUsingDesc(String text);

	boolean scrollUpinApp();

	boolean scrollHalfinApp();

	boolean zoomInApp();

	boolean zoomUsingElement(String xpath);

	boolean pinchUsingElement(String xpath);

	boolean verifyAttributeTextByXPath(String xpath, String text);

	void enterTextByXpathUsingActions(String id, String data);

	boolean closeApp();

	boolean screenOrientation();
	
	boolean hideKeyBoard();
	
	boolean getNetworkConnection();
	
	boolean setWIFINetworkConnection();
	
	boolean switchWebview();



}