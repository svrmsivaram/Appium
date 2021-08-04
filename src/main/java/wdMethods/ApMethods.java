package wdMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.offset.PointOption;
import utils.Reporter;





public class ApMethods extends Reporter implements WdMethods{
	
	protected static final ThreadLocal<ApMethods> driverThreadLocal = new ThreadLocal<ApMethods>();
	public AndroidDriver<WebElement> driver;
	protected Properties prop;
	public boolean andriodMobileWebApplicationTest;
	public String sHost,sPort,appPackage,appActivity,deviceName,environment;
	public int short_wait, long_wait, waitTime;
	
	//public AndroidDriver<?> driver;
	//protected static Properties prop;
	
	

	public void setDriver(ApMethods methods) {
		driverThreadLocal.set(methods);
	}

	public AndroidDriver<?> getDriver() {
		return  driverThreadLocal.get().driver;
	}

	/*public ApMethods(AndroidDriver<?> driver, ExtentTest test) {
		this.driver = driver;
		this.test=test;
	}*/

	public ApMethods() {
	  prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./src/main/resources/config.properties")));
			sHost = prop.getProperty("HOST");
			sPort = prop.getProperty("PORT");	
			short_wait = Integer.parseInt(prop.getProperty("SHORT_WAIT"));
			long_wait =Integer.parseInt(prop.getProperty("LONG_WAIT"));
			waitTime =Integer.parseInt(prop.getProperty("WAIT"));
			environment=prop.getProperty("ENVIRONMENT");
			
			andriodMobileWebApplicationTest = Boolean.valueOf(prop.getProperty("andriodMobileWebApplicationTest"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#launchApp(java.lang.String, java.lang.String, java.lang.String)
	 */
	public synchronized RemoteWebDriver launchApp( ){
		try {
			try {
				DesiredCapabilities dc = new DesiredCapabilities();
				dc.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("android_deviceName"));
				dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
				dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, prop.getProperty("android_platformversion"));
				dc.setCapability("chromedriverExecutableDir", new File(prop.getProperty("Android_Chrome_Driver_Path")).getAbsolutePath());
				dc.setCapability("nativeWebScreenshot", "true");
			
			if(andriodMobileWebApplicationTest) {
				dc.setCapability(MobileCapabilityType.BROWSER_NAME, prop.getProperty("andriodMobileBrowser"));
			}else{
				dc.setCapability("appPackage", prop.getProperty("appPackage"));
				dc.setCapability("appActivity", prop.getProperty("appActivity"));
			}
			System.out.println("Running with Appium Server: http://"+sHost+":"+sPort+"/wd/hub");			
			driver = new AndroidDriver<WebElement>(new URL("http://"+sHost+":"+sPort+"/wd/hub"), dc);
			System.out.println("Script start to executing with "+prop.getProperty("android_deviceName")+" Device" );
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			ApMethods am = new ApMethods();
			am.driver = driver;
			setDriver(am);
			
			//reportStep("The Appication package:" + appPackage + " launched successfully", "INFO");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			reportStep("The Appication package:" + appPackage + " could not be launched", "FAIL");
		}
		
		switch (environment) {
		case "QA":
			getDriver().get(prop.getProperty("QA_URL"));
			break;
		case "STAGE":
			getDriver().get(prop.getProperty("STAGE_URL"));
			break;
		case "DEV":
			getDriver().get(prop.getProperty("DEV_URL"));
			break;
		case "PROD":
			getDriver().get(prop.getProperty("QA_URL"));
			break;
		default:
			System.out.println("Could not find the specified environment "+environment+" please check it.");
			reportStep("Could not find the specified environment "+environment+" please check it.", "FAIL",false);
			break;
		}
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Could not be launched the driver"+ e.getMessage(), "INFO",false);
		}
		
		return getDriver();
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#launchActivity(java.lang.String, java.lang.String)
	 */
	public boolean launchActivity(String appPackage, String appActivity) {
		try {
			
			getDriver().startActivity(new Activity(appPackage, appActivity));
			reportStep("The Appication package:" + appPackage	+ " launched successfully", "PASS");

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			reportStep("The Appication package:" + appPackage + " could not be launched", "FAIL");
		}
		return true;
	}



	/* (non-Javadoc)
	 * @see wrapper.Wrappers#verifyAndInstallApp(java.lang.String, java.lang.String)
	 */
	public boolean verifyAndInstallApp(String appPackage, String appPath) {
		boolean bInstallSuccess = false;

		try {
			if (getDriver().isAppInstalled(appPackage))
				getDriver().removeApp(appPackage);
			getDriver().installApp(appPath);
			
			reportStep("The Application:" + appPackage + " installed successfully", "PASS");

			bInstallSuccess = true;
		} catch (Exception e) {

			reportStep("The Application:" + appPackage + " could not be installed", "FAIL");
			// TODO: handle exception
		}
		return bInstallSuccess;
	}

	
	
	public WebElement locateElement(String locator, String locValue) {
			
		try {
			switch(locator) {
			case("id"): return getDriver().findElement(MobileBy.id(locValue));
			case("xpath"):return getDriver().findElement(MobileBy.xpath(locValue));
			case("name"): return getDriver().findElement(MobileBy.name(locValue));
			case("class"): return getDriver().findElement(MobileBy.className(locValue));
			}
		} catch (NoSuchElementException e) {
			reportStep("The element with locator "+locator+" not found.","FAIL");
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while finding "+locator+" with the value "+locValue, "FAIL");
		}
		return null;
	}
	
	
	public WebElement locateElement(String locValue) {
		 return getDriver().findElement(MobileBy.id(locValue));
	}
	
	
	public void type(WebElement ele, String data) {
		try {
			ele.clear();
			ele.sendKeys(data);
			reportStep("The data: "+data+" entered successfully in the field : "+ele, "PASS");
		} catch (InvalidElementStateException e) {
			reportStep("The data: "+data+" could not be entered in the field :"+ele,"FAIL");
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while entering "+data+" in the field :"+ele, "FAIL");
		}
	}
	
	public void typeAndEnter(WebElement ele, String data) {
		try {
			ele.clear();
			ele.sendKeys(data,Keys.ENTER);
			reportStep("The data: "+data+" entered successfully in the field : "+ele, "PASS");
		} catch (InvalidElementStateException e) {
			reportStep("The data: "+data+" could not be entered in the field :"+ele,"FAIL");
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while entering "+data+" in the field :"+ele, "FAIL");
		}
	}
	
	public void click(WebElement ele) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 10);
			wait.until(ExpectedConditions.elementToBeClickable(ele));			
			ele.click();
			reportStep("The element "+ele+" is clicked", "PASS");
		} catch (InvalidElementStateException e) {
			reportStep("The element: "+ele+" could not be clicked", "FAIL");
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while clicking in the "+ele+" field : "+e.getMessage(), "FAIL");
		} 
	}
	
	public void clickWithNoSnap(WebElement ele) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 10);
			wait.until(ExpectedConditions.elementToBeClickable(ele));	
			ele.click();			
			reportStep("The element :"+ele+"  is clicked.", "PASS",false);
		} catch (InvalidElementStateException e) {
			reportStep("The element: "+ele+" could not be clicked", "FAIL",false);
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while clicking in the field :","FAIL",false);
		} 
	}
	
	public String getText(WebElement ele) {	
		String elementText = "";
		try {
			elementText = ele.getText();
		} catch (WebDriverException e) {
			reportStep("The element: "+ele+" could not be found.", "FAIL");
		}
		return elementText;
	}
	
	public String getAttribute(WebElement ele, String attribute) {		
		String attributeName = "";
		try {
			attributeName=  ele.getAttribute(attribute);
		} catch (WebDriverException e) {
			reportStep("The element: "+ele+" could not be found.", "FAIL");
		} 
		return attributeName;
	}
	
	public void verifyExactText(WebElement ele, String expectedText) {
		try {
			if(getText(ele).equals(expectedText)) {
				reportStep("The text: "+getText(ele)+" matches with the value :"+expectedText,"PASS");
			}else {
				reportStep("The text "+getText(ele)+" doesn't matches the actual "+expectedText,"FAIL");
			}
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while verifying the Text", "FAIL");
		} 

	}
	
	public void verifyPartialText(WebElement ele, String expectedText) {
		try {
			if(getText(ele).contains(expectedText)) {
				reportStep("The expected text "+expectedText+" contains the actual "+getText(ele),"PASS");
			}else {
				reportStep("The expected text "+expectedText+" doesn't contain the actual "+getText(ele),"FAIL");
			}
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while verifying the Text", "FAIL");
		} 
	}
	
	public void verifyIgnoreCaseText(WebElement ele, String expectedText) {
        try {
            if(getText(ele).equalsIgnoreCase(expectedText)) {
                reportStep("The text: "+getText(ele)+" matches with the value :"+expectedText,"PASS");
            }else {
                reportStep("The text "+getText(ele)+" doesn't matches the actual "+expectedText,"FAIL");
            }
        } catch (WebDriverException e) {
            reportStep("Unknown exception occured while verifying the Text", "FAIL");
        } 

    } 
	
	public void verifyExactAttribute(WebElement ele, String attribute, String value) {
		try {
			if(getAttribute(ele, attribute).equals(value)) {
				reportStep("The expected attribute :"+attribute+" value matches the actual "+value,"PASS");
			}else {
				reportStep("The expected attribute :"+attribute+" value does not matches the actual "+value,"FAIL");
			}
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while verifying the Attribute Text", "FAIL");
		} 

	}

	public void verifyPartialAttribute(WebElement ele, String attribute, String value) {
		try {
			if(getAttribute(ele, attribute).contains(value)) {
				reportStep("The expected attribute :"+attribute+" value contains the actual "+value,"PASS");
			}else {
				reportStep("The expected attribute :"+attribute+" value does not contains the actual "+value,"FAIL");
			}
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while verifying the Attribute Text", "FAIL");
		}
	}

	public void verifySelected(WebElement ele) {
		try {
			if(ele.isSelected()) {
				reportStep("The element "+ele+" is selected","PASS");
			} else {
				reportStep("The element "+ele+" is not selected","FAIL");
			}
		} catch (WebDriverException e) {
			reportStep("WebDriverException as occured: In verifySelected: "+e.getMessage(), "FAIL");
		}
	}
	public void verifyEnabled(WebElement ele) {
		try {
			if(ele.isEnabled()) {
				reportStep("The element "+ele+" is Enabled","PASS");
			} else {
				reportStep("The element "+ele+" is not Enabled","FAIL");
			}
		} catch (WebDriverException e) {
			reportStep("WebDriverException as occured: In verifyEnabled"+e.getMessage(), "FAIL");
		}
	}

	public void verifyDisplayed(WebElement ele) {
		try {
			if(ele.isDisplayed()) {
				reportStep("The element "+ele+" is visible","PASS");
			} else {
				reportStep("The element "+ele+" is not visible","FAIL");
			}
		} catch (WebDriverException e) {
			reportStep("WebDriverException as occured: In VerifyDisplayed"+e.getMessage(), "FAIL");
		} 
	}
	
	public boolean isDisplayed(WebElement ele) {
		boolean bReturn =false;
		try {
			if(ele.isDisplayed()) {
				bReturn= true;
			} 
		} catch (WebDriverException e) {
			//reportStep("WebDriverException as occured: In VerifyDisplayed"+e.getMessage(), "FAIL");
		} 
		return bReturn;
	}
	
	public boolean isExists(WebElement ele) {
		if(ele.getSize() != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void isNotDisplayed(WebElement ele) {
		try {
			if(ele.isDisplayed()) {
				reportStep("The element "+ele+" is visible","FAIL");
			} 
		} catch (WebDriverException e) {
			reportStep("The element "+ele+" is not visible"+e.getMessage(), "PASS");
		} 
		
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#sleep(int)
	 */
	public void sleep(int mSec){
		try {
			Thread.sleep(mSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#printContext()
	 */
	public void printContext(){
		try {
			Set<String> contexts = getDriver().getContextHandles();
			for (String string : contexts) {
				System.out.println(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Context could not be found", "FAIL");

		}
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#switchview()
	 */
	public boolean switchview(){
		try {
			Set<String> contexts = getDriver().getContextHandles();
			for (String contextName : contexts) {
				if (contextName.contains("NATIVE"))
					getDriver().context(contextName);
			}

		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Context could not be switched", "FAIL");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#clickByID(java.lang.String)
	 */
	public boolean clickByID(String id){
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 30);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
			getDriver().findElementById(id).click();
			reportStep("The element with id: "+id+" is clicked.", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The element with id: "+id+" could not be clicked.", "FAIL");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#clickByAccessebilityID(java.lang.String)
	 */
	public boolean clickByAccessebilityID(String id){
		try {
			/*WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.presenceOfElementLocated(ByAccessibilityId(ID));*/
			getDriver().findElementByAccessibilityId(id).click();
			reportStep("The element with Accessibility id: "+id+" is clicked.", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The element with Accessibility id: "+id+" could not be clicked.", "FAIL");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#clickByXpath(java.lang.String)
	 */
	public boolean clickByXpath(String xpath){
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 30);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			getDriver().findElementByXPath(xpath).click();
			reportStep("The element with Xpath: "+xpath+" is clicked.", "PASS");

		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The element with Xpath: "+xpath+" is clicked.", "PASS");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#verifyContentDescIsDisplayed(java.lang.String)
	 */
	public boolean verifyContentDescIsDisplayed(String xpath){
		boolean bReturn = false;
		WebDriverWait wait = new WebDriverWait(getDriver(), 30);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		if(getDriver().findElementByXPath(xpath).isDisplayed()){
			bReturn = true;
			reportStep("The element with Xpath: "+xpath+" is displayed.", "PASS");
		}else
		{
			reportStep("The element with Xpath: "+xpath+" is not displayed.", "FAIL");
			bReturn = false;
		}
		return bReturn;

	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#clickByLinkText(java.lang.String)
	 */
	public boolean clickByLinkText(String LinkText){
		boolean bReturn = false;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 30);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(LinkText)));
			getDriver().findElementByLinkText(LinkText).click();
			reportStep("The element with LinkText: "+LinkText+" is clicked.", "PASS");
			bReturn = true;
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The element with LinkText: "+LinkText+" could not be clicked.", "FAIL");
			bReturn = false;
		}
		return bReturn;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#enterTextByID(java.lang.String, java.lang.String)
	 */
	public void enterTextByID(String id,String data){
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 30);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
			getDriver().findElementById(id).clear();
			getDriver().findElementById(id).sendKeys(data);
			reportStep("The data: "+data+" entered successfully in the field with Id:"+id, "PASS");
		} catch (NoSuchElementException e) {
			reportStep("The data: "+data+" could not be entered in the field with Id:"+id, "FAIL");
		} catch (Exception e) {
			reportStep("Unknown exception occured while entering "+data+" in the field with Id:"+id, "FAIL");
		}

	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#pressEnter()
	 */
	public void pressEnter(){
		try {
			getDriver().pressKey(new KeyEvent(AndroidKey.ENTER));
			reportStep("Enter button in the keyboard pressed successfully", "PASS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reportStep("Enter button in the keyboard could not be pressed successfully", "FAIL");
		}
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#enterTextByXpath(java.lang.String, java.lang.String)
	 */
	public void enterTextByXpath(String xpath,String data){
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 30);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			getDriver().findElementByXPath(xpath).clear();
			getDriver().findElementByXPath(xpath).sendKeys(data);
			reportStep("The data: "+data+" entered successfully in the field with Xpath:"+xpath, "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The data: "+data+" could not be entered in the field with Xpath:"+xpath, "FAIL");
		} catch (Exception e) {
			reportStep("Unknown exception occured while entering "+data+" in the field with Xpath:"+xpath, "FAIL");
		}

	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#takeSnap()
	 */
	public long takeSnap(){
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L; 
		try {
			FileUtils.copyFile(getDriver().getScreenshotAs(OutputType.FILE) , new File("./reports/images/"+number+".jpg"));
		} catch (WebDriverException e) {
			reportStep("The browser has been closed.", "FAIL");
		} catch (IOException e) {
			reportStep("The snapshot could not be taken", "WARN");
		}
		return number;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#verifyTextByID(java.lang.String, java.lang.String)
	 */
	public boolean verifyTextByID(String id,String data){
		boolean bReturn = false;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 30);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
			String name = getDriver().findElementById(id).getText();
			if(name.contains(data)){
				bReturn = true;
				reportStep("The text: "+name+" matches with the value :"+data, "PASS");
			}else
				bReturn = false;
			reportStep("The text: "+name+" did not match with the value :"+data, "FAIL");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}
		return bReturn;
	}


	/* (non-Javadoc)
	 * @see wrapper.Wrappers#backButton()
	 */
	public boolean backButton(){
		try {
			getDriver().navigate().back();
			reportStep("The Application screen is moved back", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Application screen could not be moved back", "PASS");
		}
		return true;
	}


	/* (non-Javadoc)
	 * @see wrapper.Wrappers#scrollUsingDesc(java.lang.String)
	 */
	public boolean scrollUsingDesc(String text){
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 30);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.View[contains(@content-desc,'"+text+"')]")));
			Dimension size = getDriver().manage().window().getSize();
			int x0 = (int) (size.getWidth()*0.2);
			int y0 = (int) (size.getHeight()*0.2);		
			TouchActions touch = new TouchActions(getDriver());
			touch.scroll(getDriver().findElementByXPath("//android.view.View[contains(@content-desc,'"+text+"')]"), x0, y0);
			touch.perform();
			reportStep("Successfully scrolled to the text :"+text, "PASS");
		} catch (WebDriverException e) {
			e.printStackTrace();
			reportStep("Could not be scrolled to the text :"+text, "FAIL");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#scrollUpinApp()
	 */
	public boolean scrollUpinApp(){
		try {
			Dimension size = getDriver().manage().window().getSize();
			int x0 = (int) (size.getWidth()*0.2);
			int y0 = (int) (size.getHeight()*0.2);
			int x1 = (int) (size.getWidth()*0.8);
			int y1 = (int) (size.getHeight()*0.8);
			
			TouchActions action = new TouchActions(getDriver());
			action.down(x1, y1);
			action.up(x0, y0);
			action.perform();
			reportStep("Application screen is scrolled up successfully", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Application screen could not be scrolled up", "FAIL");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#scrollHalfinApp()
	 */
	public boolean scrollHalfinApp(){
		try {
			Dimension size = getDriver().manage().window().getSize();
			int x0 = (int) (size.getWidth()*0.2);
			int y0 = (int) (size.getHeight()*0.2);
			int x1 = (int) (size.getWidth()*0.5);
			int y1 = (int) (size.getHeight()*0.5);

			TouchActions action = new TouchActions(getDriver());
			action.down(x1, y1);
			action.up(x0, y0);
			action.perform();
			reportStep("Application screen is scrolled successfully", "PASS");
		} catch (Exception e) {
			reportStep("Application screen could not be scrolled", "FAIL");
			e.printStackTrace();
		}
		return true;
	}


	/* (non-Javadoc)
	 * @see wrapper.Wrappers#zoomInApp()
	 */
	public boolean zoomInApp(){
		try {
			Dimension size = getDriver().manage().window().getSize();
			int middleX = (int) (size.getWidth()*0.5);
			int middleY = (int) (size.getHeight()*0.5);
			int y0 =(int)(middleY*0.5);
			int y1= middleY + y0;
			TouchAction<?> actionOne = new TouchAction<>(getDriver());
			actionOne.press(PointOption.point(middleX, middleY));
			actionOne.moveTo(PointOption.point(middleX, y0));
			actionOne.release();
			TouchAction<?> actionTwo = new TouchAction<>(getDriver());
			actionOne.press(PointOption.point(middleX, middleY));
			actionOne.moveTo(PointOption.point(middleX, y1));
			actionTwo.release();
			MultiTouchAction action = new MultiTouchAction(getDriver());
			action.add(actionOne);
			action.add(actionTwo);
			action.perform();
			reportStep("The Application is zoomed.", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Application could not be zoomed.", "FAIL");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#zoomUsingElement(java.lang.String)
	 */
	public boolean zoomUsingElement(String xpath){
		try {
			
			Dimension size = getDriver().findElement(By.xpath(xpath)).getSize();
			int middleX = (int) (size.getWidth()*0.5);
			int middleY = (int) (size.getHeight()*0.5);
			int y0 =(int)(middleY*0.5);
			int y1= middleY + y0;
			TouchAction<?> actionOne = new TouchAction<>(getDriver());
			actionOne.press(PointOption.point(middleX, middleY));
			actionOne.moveTo(PointOption.point(middleX, y0));
			actionOne.release();
			TouchAction<?> actionTwo = new TouchAction<>(getDriver());
			actionOne.press(PointOption.point(middleX, middleY));
			actionOne.moveTo(PointOption.point(middleX, y1));
			actionTwo.release();
			MultiTouchAction action = new MultiTouchAction(getDriver());
			action.add(actionOne);
			action.add(actionTwo);
			action.perform();
			reportStep("The element with Xpath: "+xpath+" is zoomed.", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The element with Xpath: "+xpath+" could not be zoomed.", "FAIL");
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see wrapper.Wrappers#pinchUsingElement(java.lang.String)
	 */
	public boolean pinchUsingElement(String xpath){
		try {
			
			
			
			Dimension size = getDriver().findElement(By.xpath(xpath)).getSize();
			int middleX = (int) (size.getWidth()*0.5);
			int middleY = (int) (size.getHeight()*0.5);
			int y0 =(int)(middleY*0.5);
			int y1= middleY + y0;
			TouchAction<?> actionOne = new TouchAction<>(getDriver());
			actionOne.press(PointOption.point(middleX, y0));
			actionOne.moveTo(PointOption.point(middleX, middleY));
			actionOne.release();
			TouchAction<?> actionTwo = new TouchAction<>(getDriver());
			actionOne.press(PointOption.point(middleX,y1 ));
			actionOne.moveTo(PointOption.point(middleX, middleY));
			actionTwo.release();
			MultiTouchAction action = new MultiTouchAction(getDriver());
			action.add(actionOne);
			action.add(actionTwo);
			action.perform();
			reportStep("The element with Xpath: "+xpath+" is pinched.", "PASS");
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reportStep("The element with Xpath: "+xpath+" could not be pinched.", "FAIL");
		}
		return true;
	}


	/* (non-Javadoc)
	 * @see wrapper.Wrappers#verifyAttributeTextByXPath(java.lang.String, java.lang.String)
	 */
	public boolean verifyAttributeTextByXPath(String xpath,String text){
		boolean val = false;
		WebDriverWait wait = new WebDriverWait(getDriver(), 30);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		String name = getDriver().findElementByXPath(xpath).getAttribute("content-desc");
		System.out.println(name);
		try {

			if(name.contains(text)){
				val = true;
				reportStep("The text: "+name+" matches with the value :"+text, "PASS");
			}else
				val = false;
			reportStep("The text: "+name+" did not match with the value :"+text, "FAIL");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The text: "+name+" did not match with the value :"+text, "FAIL");
		}
		return val;
	}


	/* (non-Javadoc)
	 * @see wrapper.Wrappers#scrollDownInBrowser(int)
	 */
	public boolean scrollDownInBrowser(int val){
		try {
			JavascriptExecutor jse = (JavascriptExecutor)getDriver();
			jse.executeScript("window.scrollBy(0,"+val+"\")", "");
		} catch (Exception e) {
			//			e.printStackTrace();
		}
		return true;
	}


	//Updated on 31 Jan 2017

	public void enterTextByXpathUsingActions(String xpath,String data){
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 30);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			Actions actions = new Actions(getDriver());
			actions.moveToElement(getDriver().findElement(By.xpath(xpath)));
			actions.click();
			actions.sendKeys(data);
			actions.build().perform();
			reportStep("The data: "+data+" entered successfully in the field with Xpath:"+xpath, "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The data: "+data+" could not be entered in the field with Xpath:"+xpath, "FAIL");
		} catch (Exception e) {
			reportStep("Unknown exception occured while entering "+data+" in the field with Xpath:"+xpath, "FAIL");
		}

	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#CloseApp()
	 */
	public boolean closeApp(){
		try {
			getDriver().closeApp();
			reportStep("The Appication is closed successfully", "PASS");

		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Appication could not be closed", "FAIL");
		}
		return true;
	}

	public boolean screenOrientation(){
		try {


			ScreenOrientation orientation= getDriver().getOrientation();
			System.out.println(orientation.value());

			if(orientation.value().contains("LANDSCAPE"))
			{
				getDriver().rotate(ScreenOrientation.PORTRAIT);
				ScreenOrientation or  = getDriver().getOrientation();
				reportStep("The Screen is in "+or+" ", "PASS");
			}
			else
			{
				getDriver().rotate(ScreenOrientation.LANDSCAPE);				
				ScreenOrientation or =getDriver().getOrientation();
				reportStep("The Screen is in "+or+" ", "PASS");
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("App closed for unknown reason", "FAIL");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#hideKeyBoard()
	 */
	public boolean hideKeyBoard(){
		try {
			getDriver().hideKeyboard();
			reportStep("The Keyboard is hidden successfully", "PASS");

		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Keyboard is still available", "FAIL");
		}
		return true;
	}


	/* (non-Javadoc)
	 * @see wrapper.Wrappers#getNetworkConnection()
	 */
	public boolean getNetworkConnection(){
		try {
			ConnectionState con = getDriver().getConnection();
			reportStep("The Network  is in "+con+" ", "PASS");

		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Phone crashed", "FAIL");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see wrapper.Wrappers#setWIFINetworkConnection()
	 */
	public boolean setWIFINetworkConnection(){
		try {
			getDriver().toggleWifi();
			reportStep("The Network is setted to WIFI connection ", "PASS");
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The WIFI connection could not be established", "FAIL");
		}
		return true;
	}

	public boolean switchWebview(){
		try {		
			Set<String> contextNames = getDriver().getContextHandles();

			for (String contextName : contextNames) {

				if (contextName.contains("WEBVIEW"))
					getDriver().context(contextName);				

			}
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("The Webview couldnot be found", "FAIL");
		}
		return true;
	}
	
	public boolean verifyTitle(String title) {
		boolean bReturn =false;
		try {
			if(getTitle().equals(title)) {
				reportStep("The title of the page matches with the value :"+title,"PASS");
				bReturn= true;
			}else {
				reportStep("The title of the page:"+getDriver().getTitle()+" did not match with the value :"+title, "FAIL");
			}
		} catch (WebDriverException e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		} 
		return bReturn;
	}
	
	public String getTitle() {		
		String appTitle = "";
		try {
			appTitle =  getDriver().getTitle();
		} catch (WebDriverException e) {
			reportStep("Unknown Exception Occured While fetching Title", "FAIL");
		}
		return appTitle; 
		
	}
}
