package com.abfl.automation.testscripts;

import static org.junit.Assert.fail;
import java.util.Hashtable;

import org.junit.BeforeClass;
import org.junit.Test;

import com.abfl.automation.page.KotakMahendraBankPage;
import com.abfl.automation.utilities.BrowserDriver;
import com.abfl.automation.utilities.CommonUI;
import com.abfl.automation.utilities.Log4J;
import com.abfl.automation.utilities.LogController;
import com.abfl.automation.utilities.TestDataController;


public class EscrowFundTransaction {
	private static String testScriptName = EscrowFundTransaction.class.getSimpleName();

	static Hashtable <String, String> testData = null;
	static TestDataController testDataController = null;
	static LogController logController = null;

	static CommonUI commonUI = null;
	static String startTime = null;
	static String endTime = null;
	private static KotakMahendraBankPage bankPage;
	
	@BeforeClass
	public static void setConfigurations(){

		try {
			// Set Log4J properties
			testScriptName = EscrowFundTransaction.class.getSimpleName();
			Log4J.setLog4JProperties();

			// Get test data
			testData = new Hashtable<String, String>();
			testDataController = new TestDataController();
			testData.putAll(testDataController.getTestData(testScriptName));

			// Get browser driver
			commonUI = new CommonUI();
			logController = new LogController(testScriptName);
			startTime = commonUI.getCurrentTime();

			bankPage = new KotakMahendraBankPage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}
	
	// Test case specific data
	@Test
	public void EscrowFundTransaction_TestScript() throws Exception {
		try	{
			// Step 1 : 
			
			// Step 2 : 
			
			// Step 3 : Navigate to kotak net banking url
			BrowserDriver.setBrowserDriver("chrome");
			bankPage.navigateToURL(testData.get("NetBankingUrl"));
			logController.writeLog("Process :", "Navigate to kotak net banking url", "Passed");
			Log4J.getlogger("ABFL Logs :").info("Navigate to kotak net banking url");			
			
			// Login to kotak banking
			bankPage.enterLoginCredentials(testData.get("LoginID"),testData.get("Password"));
			bankPage.clickOnLSecureLoginButton();
			bankPage.waitForUserProfilePage();
			logController.writeLog("Process :", "Login to kotak net banking", "Passed");
			Log4J.getlogger("ABFL Logs :").info("Login to kotak net banking");
			
			// Step 4 : Go to CMS netIT > File Upload > Import instruction 
			bankPage.selectMenuOption();
			logController.writeLog("Process :", "Go to CMS netIT > File Upload > Import instruction", "Passed");
			Log4J.getlogger("ABFL Logs :").info("Go to CMS netIT > File Upload > Import instruction");
			
			bankPage.selectFileUploadSubMenu();
			
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		}catch(Exception exception) {
			commonUI.takeScreenShot(testScriptName);
			fail();
		} catch(AssertionError assertionError) {
			commonUI.takeScreenShot(testScriptName);
			fail();
		}finally	{
			// Quit Browser
			//commonUI.quitAllBrowser();
			endTime = commonUI.getCurrentTime();
			logController.writeLog("Execution Time", commonUI.getTimeDifference(startTime, endTime), "");
			// Close report log writer
			logController.closeLogWriter();
			}
	}
}