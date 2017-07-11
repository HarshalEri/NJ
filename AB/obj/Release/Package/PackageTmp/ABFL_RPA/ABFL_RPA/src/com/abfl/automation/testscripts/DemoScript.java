package com.abfl.automation.testscripts;

import static org.junit.Assert.fail;

import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Test;

import com.abfl.automation.utilities.CommonUI;
import com.abfl.automation.utilities.EmailController;
import com.abfl.automation.utilities.ExcelFileController;
import com.abfl.automation.utilities.Log4J;
import com.abfl.automation.utilities.LogController;
import com.abfl.automation.utilities.TestDataController;

public class DemoScript {

	private String testScriptName = DemoScript.class.getSimpleName();
	private String packageAddress = this.getClass().getCanonicalName();
	Hashtable <String, String> testData = null;

	TestDataController testDataController = null;
	LogController logController = null;


	
	@Test
	public void DemoScript_TestScript() throws Exception {
		try {
		
			// Set Log4J properties
			Log4J.setLog4JProperties();
			Log4J.getlogger(packageAddress).info(testScriptName +" test case started");
	
			// Get test data from test case test data and configuration property file
			testData=new Hashtable<String,String>();
			testDataController=new TestDataController();
			//testData.putAll(testDataController.getTestData(null));

			
			ExcelFileController.readXlsxFile("BenifiaryExcelMaster");
			// Get browser driver
			//BrowserDriver.setBrowserDriver(testData.get("Browser"));				
			//Log4J.getlogger(packageAddress).info("Get browser driver");
	
			EmailController.init();
			Assert.assertTrue("Mail has not been not received", EmailController.isEmailReceived("Test Email", false, null));
			logController.writeLog("Verify that Email has been received", "Verified that Email has been received", "Passed");
			Log4J.getlogger(packageAddress).info("Verification 3: Verify that Email has been received");
			
		} catch (Exception exception) {
			logController.writeLog(testScriptName + " test case",
					"Failed to execute " + testScriptName + " test case. Please check SMTUIAutomation.log for failure",
					"Failed");
			Log4J.getlogger(packageAddress).error(exception.getMessage());
			fail();
		} catch (AssertionError assertionError) {
			logController.writeLog("Test case verification", assertionError.getMessage(), "Failed");
			Log4J.getlogger(packageAddress).error(assertionError.getMessage());
			fail();
		} finally {

			// Quit browser
		//	commonUI.quitAllBrowser();
			Log4J.getlogger(packageAddress).info("Quit browser");


			// Close report log writer
			logController.closeLogWriter();
			Log4J.getlogger(packageAddress).info(testScriptName + " test case completed");

		}

	}

}
