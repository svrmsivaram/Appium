package utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public abstract class Reporter {

	public static ExtentHtmlReporter html;
	public static ExtentReports extent;
	public ExtentTest test, suiteTest;
	public String testCaseName, testNodes, testDescription;
	public Boolean appendExisting = false;

	private static Map<RemoteWebDriver, ExtentTest> testDriver;

	public void reportStep(String desc, String status, boolean bSnap) {

		MediaEntityModelProvider img = null;
		if (bSnap && !status.equalsIgnoreCase("INFO")) {

			long snapNumber = 100000L;
			snapNumber = takeSnap();
			try {
				img = MediaEntityBuilder.createScreenCaptureFromPath("./../reports/images/" + snapNumber + ".jpg")
						.build();
			} catch (IOException e) {
			}
		}

		// Write if it is successful or failure or information
		if (status.toUpperCase().equals("PASS")) {
			testDriver.get(getDriver()).pass(desc, img);
		} else if (status.toUpperCase().equals("FAIL")) {
			testDriver.get(getDriver()).fail(desc, img);
			throw new RuntimeException("FAILED");
		} else if (status.toUpperCase().equals("INFO")) {
			testDriver.get(getDriver()).info(desc);
		} else if (status.toUpperCase().equals("WARN")) {
			testDriver.get(getDriver()).warning(desc, img);
		}
	}

	public void reportStep(String desc, String status) {
		reportStep(desc, status, true);
	}

	public abstract long takeSnap();

	public void startResult() {
		try {
			testDriver = new HashMap<RemoteWebDriver, ExtentTest>();
			html = new ExtentHtmlReporter("./reports/result.html");
			html.setAppendExisting(false);
			extent = new ExtentReports();
			extent.attachReporter(html);
			if (!appendExisting) {
				FileUtils.cleanDirectory(new File("./reports/images/"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized ExtentTest startTestModule(String testCaseName, String testDescription) {
		return testDriver.put(getDriver(), extent.createTest(testCaseName, testDescription));
	}

	public synchronized ExtentTest startTestCase(String testNodes) {
		return testDriver.put(getDriver(), suiteTest.createNode(testNodes));
	}

	public void endResult() {
		extent.flush();
	}

	public abstract RemoteWebDriver getDriver();

}