package com.abfl.automation.page;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.abfl.automation.utilities.BrowserDriver;
import com.abfl.automation.utilities.CommonUI;
import com.abfl.automation.utilities.ElementController;

public class KotakMahendraBankPage {
	
	ElementController elementController = null;
	CommonUI commonUI = null;
	private String currentWindow = null;

	public KotakMahendraBankPage() {
		elementController = new ElementController(KotakMahendraBankPage.class.getSimpleName());
		commonUI = new CommonUI(KotakMahendraBankPage.class.getSimpleName());
	}
	
	public void navigateToURL(String url) throws Exception {
		try {
			BrowserDriver.getBrowserDriver().navigate().to(url);
		}catch(Exception exception) {
			throw new Exception("Unable to navigate to URL '" + url + "'. [goToURL()]: " + exception.getMessage());
		}
	}	
	public void waitForPageToLoad(){
		try {
			WebDriverWait wait = new WebDriverWait(BrowserDriver.getBrowserDriver(),10);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementController.elementParams.get("LoginLink").get("xpath"))));
		} catch (Exception e){
			
		}
	}
	
	public void clickOnLoginLink(){
		try{
			commonUI.clickOnButton("LoginLink");
			elementController.implicitWait(1);
		}catch(Exception exception){
			
		}
	}
	public void switchToLoginWindow(){
		try {
			commonUI.switchToWindowWithTitle(elementController.elementParams.get("LoginPageTitle").get("pagetitle"));
			
		} catch (Exception e){
			
		}
	}
	
	public void enterLoginCredentials(String loginID, String password){
		try {
			BrowserDriver.getBrowserDriver().switchTo().frame("framemain");
			commonUI.enterTextIntoTextBox("UserIDTxtBox", loginID);
			commonUI.enterTextIntoTextBox("PasswordTxtBox",password);
		}catch(Exception exception){
			
		}
	}
	
	public void clickOnSecureLoginButton(){
		try {
			commonUI.clickOnButton("SecureLoginBtn");
			BrowserDriver.getBrowserDriver().switchTo().defaultContent();
			currentWindow = BrowserDriver.getBrowserDriver().getWindowHandle();
		}catch(Exception exception){
			
		}
	}
	
	public void selectMenuOption(){
		try{
			BrowserDriver.getBrowserDriver().switchTo().frame("iframe_nav");
			commonUI.clickOnButton("CMSnetITMenuOption");
			elementController.implicitWait(1);
			BrowserDriver.getBrowserDriver().switchTo().defaultContent();
		} catch(Exception exception){
			
		}
	}
	
	public void waitForUserProfilePage(){
		try{
			if(currentWindow != null)
				BrowserDriver.getBrowserDriver().switchTo().window(currentWindow);
			BrowserDriver.getBrowserDriver().switchTo().frame("framemain");
			elementController.implicitWait(2);
		} catch(Exception exception){
			
		}
	}
	
	public void selectFileUploadSubMenu(){
		try{
			BrowserDriver.getBrowserDriver().switchTo().defaultContent();
			BrowserDriver.getBrowserDriver().switchTo().frame("framemain");
			BrowserDriver.getBrowserDriver().switchTo().frame("framecontent");
			BrowserDriver.getBrowserDriver().switchTo().frame("contents");
			commonUI.clickOnButton("FileUploadMenu");
			elementController.implicitWait(1);
			this.selectImportInstructionOption();
			this.clickOnChooseFileButton();
		} catch(Exception exception){
			System.out.println(exception.getMessage());
		}
	}
	public void selectImportInstructionOption(){
		try{
			commonUI.clickOnButton("ImportInstructionSubMenu");
			elementController.implicitWait(1);
			BrowserDriver.getBrowserDriver().switchTo().defaultContent();
		} catch(Exception exception){
			
		}
	}
	
	public void clickOnChooseFileButton(){
		try{
			BrowserDriver.getBrowserDriver().switchTo().frame("framemain");
			BrowserDriver.getBrowserDriver().switchTo().frame("framecontent");
			BrowserDriver.getBrowserDriver().switchTo().frame("frameset");
			commonUI.clickOnButton("ChooeseFileButton");
			elementController.implicitWait(1);
			
		} catch(Exception exception){
			
		}
		
	}
	public void logout(){
		try{
			BrowserDriver.getBrowserDriver().switchTo().defaultContent();
			BrowserDriver.getBrowserDriver().switchTo().frame("framemain");
			BrowserDriver.getBrowserDriver().switchTo().frame("iframe_header");
			commonUI.clickOnButton("Logout");
			elementController.implicitWait(1);
		} catch(Exception exception){
			
		}
	}
	

	public void uploadFile(String filePath) {
		try {
			Robot robot = new Robot();
			robot.mouseMove(203, 637);
			Thread.sleep(2000);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			Thread.sleep(2000);
			StringSelection selection = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents
			(selection, null);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(2000);
			robot.keyPress(KeyEvent.VK_ENTER);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveFile(){
		try {
			Robot robot = new Robot();
			Thread.sleep(2000);
			robot.keyPress(KeyEvent.VK_F11);
			Thread.sleep(2000);
			robot.keyPress(KeyEvent.VK_F11);
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

