package com.abfl.automation.testscripts;

import static org.junit.Assert.fail;

import java.util.Hashtable;

import org.junit.Test;

import com.abfl.automation.page.KotakMahendraBankPage;
import com.abfl.automation.steps.EscrowFundsTransferSteps;
import com.abfl.automation.utilities.BrowserDriver;
import com.abfl.automation.utilities.CommonUI;
import com.abfl.automation.utilities.Log4J;
import com.abfl.automation.utilities.TestDataController;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ESCROWFundsTransfer {
	private static String testScriptName = ESCROWFundsTransfer.class.getSimpleName();

	static Hashtable<String, String> testData = null;
	static TestDataController testDataController = null;

	static ExtentReports extent;
	static ExtentTest extentTest;

	static CommonUI commonUI = null;
	static String startTime = null;
	static String endTime = null;
	private KotakMahendraBankPage bankPage;
	private EscrowFundsTransferSteps escrowFundsTransferSteps;

	// Test case specific data
	
	@Test
	public void ESCROWFundsTransfer_TestScript() throws Exception {
		try {
			extent = new ExtentReports(".\\TestReport\\" + ESCROWFundsTransfer.class.getSimpleName()+".html", true);
			extentTest = extent.startTest(ESCROWFundsTransfer.class.getSimpleName());

			// Set Log4J properties
			testScriptName = ESCROWFundsTransfer.class.getSimpleName();
			Log4J.setLog4JProperties();

			// Get test data
			testData = new Hashtable<String, String>();
			testDataController = new TestDataController();
			testData.putAll(testDataController.getTestData(testScriptName));

			// Get browser driver
			commonUI = new CommonUI();
			startTime = commonUI.getCurrentTime();
			Log4J.getlogger("ABFL :").info("Launch Browser");

			bankPage = new KotakMahendraBankPage();
			escrowFundsTransferSteps = new EscrowFundsTransferSteps();

			extentTest.log(LogStatus.PASS, "ESCROW Funds Transfer process started");
			Log4J.getlogger("ABFL :").info("ESCROW Funds Transfer process started");
			
			// Step 1 : CPU user will read account number in received excel file from email Escrow_Instruction_Format excel file.
			String accNumberfromEmail = escrowFundsTransferSteps.getColumnValuesFromSheetFromEmail("Escrow Ac no ");
			String beneficiaryAccNo = escrowFundsTransferSteps.getColumnValuesFromSheetFromEmail("Beneficiary AC no / LAN no ");;
			extentTest.log(LogStatus.PASS, "Step 1. Read account number in received excel file from email");
			Log4J.getlogger("ABFL :").info("Step 1. Read account number in received excel file from email");

			// Step 2 : CPU user will check an Escrow Account Number availability for online transaction in Escrow Account Excel Master
			if (escrowFundsTransferSteps.isAccountNumberPresentInMasetSheet(accNumberfromEmail)) ;
				extentTest.log(LogStatus.PASS,"Step 2. Check an Escrow Account Number availability for online transaction");
				Log4J.getlogger("ABFL :").info("Step 2. Check an Escrow Account Number availability for online transaction");
	
			// Step 3. User will search Beneficiary A/c Number (from Email) in the Beneficiary Excel Master File
			if(escrowFundsTransferSteps.searchForBeneficiaryAccNumberInBeneficiaryMasterSheet(beneficiaryAccNo));
			String beneficiaryCode =  escrowFundsTransferSteps.getColumnValuesFromSheetFromBeneficiaryMasterSheet("DRAWER_CODE");
			extentTest.log(LogStatus.PASS,"Step 3. Search Beneficiary A/c Number (from Email) in the Beneficiary Excel Master File");
			Log4J.getlogger("ABFL :").info("Step 3. Search Beneficiary A/c Number (from Email) in the Beneficiary Excel Master File");
				
			// Step 4 : CPU user will prepare an upload file
			escrowFundsTransferSteps.prepareUploadFileFormat(beneficiaryCode,testData.get("uploadFilePath"));
			extentTest.log(LogStatus.PASS, "Step 4 : CPU user will prepare an upload file");
			Log4J.getlogger("ABFL :").info("Step 4 : CPU user will prepare an upload file");
	
			// Step 5 : Navigate to kotak net banking url
			BrowserDriver.setBrowserDriver("chrome");
			bankPage.navigateToURL(testData.get("NetBankingUrl"));
			extentTest.log(LogStatus.PASS, "Step 5 : Navigate to kotak net banking url");
			Log4J.getlogger("ABFL :").info("Step 5 : Navigate to kotak net banking url");
	
			// Step 6 : Login to kotak banking
			bankPage.enterLoginCredentials(testData.get("LoginID"), testData.get("Password"));
			bankPage.clickOnSecureLoginButton();
			bankPage.waitForUserProfilePage();
			extentTest.log(LogStatus.PASS, "Step 6 : Login to kotak net banking");
			Log4J.getlogger("ABFL :").info("Step 6 : Login to kotak net banking");
	
			// Step 7 : Go to CMS netIT > File Upload > Import instruction
			bankPage.selectMenuOption();
			bankPage.selectFileUploadSubMenu();
			extentTest.log(LogStatus.PASS, "Step 7 : Go to CMS netIT > File Upload > Import instruction");
			Log4J.getlogger("ABFL :").info("Step 7 : Go to CMS netIT > File Upload > Import instruction");
	
			// Upload file
			bankPage.uploadFile(testData.get("uploadFilePath"));
			extentTest.log(LogStatus.PASS, "Step 8 : Upload file");
			Log4J.getlogger("ABFL :").info("Step 8 : Upload file");
			
			// Step 9. Click F11 Button till the STAUS & REMARK columns gets updated as “Completed” and Data uploaded successfully” respectively.
			bankPage.saveFile();
			extentTest.log(LogStatus.PASS, "Step 9 : Click F11 button till file gets uploaded");
			Log4J.getlogger("ABFL :").info("Step 9 : Click F11 button till file gets uploaded");
			
			// Step 10 :
			bankPage.waitForUserProfilePage();
			bankPage.logout();
			extentTest.log(LogStatus.PASS, "ESCROW Funds Transfer process completed successfully");
			Log4J.getlogger("ABFL :").info("ESCROW Funds Transfer process completed successfully");
		} catch (Exception exception) {
			commonUI.takeScreenShot(testScriptName);
			fail();
		} catch (AssertionError assertionError) {
			commonUI.takeScreenShot(testScriptName);
			fail();
		} finally {
			endTime = commonUI.getCurrentTime();
			BrowserDriver.getBrowserDriver().quit();
			extent.endTest(extentTest);
			extent.close();
		}
	}

}