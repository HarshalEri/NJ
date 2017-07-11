package com.abfl.automation.utilities;


import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.abfl.automation.utilities.ElementController;

/**
 * Class contains the common method required for all pages
 * @author Harshal.e
 *
 */
public class CommonUI {
	ElementController elementController;
	private static final ElementController commonUIController = new ElementController(CommonUI.class.getSimpleName());
	TestDataController testDataController;
	private String baseDirPath = null;
	private String mainWindow = null;
	private ArrayList<String> taskList = new ArrayList<String>();

	public CommonUI(){		
		testDataController = new TestDataController();
		baseDirPath = testDataController.getTestData(null).get("BaseDirectoryPath");
		elementController = new ElementController(CommonUI.class.getSimpleName());
	}

	public CommonUI(String elementRepository) {
		testDataController = new TestDataController();
		baseDirPath = testDataController.getTestData(null).get("BaseDirectoryPath");
		elementController = new ElementController(elementRepository);
	}

	/**
	 * Method to switch to frame
	 * @throws Exception 
	 */
	public void switchToFrame() throws Exception {
		try {
			elementController.implicitWait(1);
			BrowserDriver.getBrowserDriver().switchTo().frame(elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, "tagframe"));
		}catch(Exception exception) {
			throw new Exception("Exception occured in switchFrame method: " + exception.getMessage());
		}
	}

	/**
	 * Method to specified frame
	 * @param frameId
	 * @throws Exception
	 */
	public void switchToFrame(String frameId) throws Exception {
		try {
			BrowserDriver.getBrowserDriver().switchTo().frame(elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, frameId));
		}catch(Exception exception) {
			throw new Exception("Unable to switch to frame [switchToFrame]" + exception.getMessage());
		}
	}

	/**
	 * Method to switch to window
	 * @param windowId
	 * @throws Exception
	 */
	public void switchToWindow(String windowId) throws Exception {
		try {
			BrowserDriver.getBrowserDriver().switchTo().window(windowId);
			elementController.implicitWait(1);
		}catch(Exception exception) {
			throw new Exception("Exception occured in switchToWindow method: " + exception.getMessage());
		}
	}

	/**
	 * Method to switch to window with title
	 * @param windowTitle
	 * @return
	 * @throws Exception
	 */
	public String switchToWindowWithTitle(String windowTitle) throws Exception {
		String parentWindow = null;
		boolean isWindowFind = false;
		int count = 0;
		try {
			elementController.implicitWait(1);
			try {
				parentWindow = BrowserDriver.getBrowserDriver().getWindowHandle();
			} catch(Exception exception) {}
			do {
				Set<String> availableWindows = BrowserDriver.getBrowserDriver().getWindowHandles();		
				for(String winHandle : availableWindows){
					try{
						BrowserDriver.getBrowserDriver().switchTo().window(winHandle);
						if(BrowserDriver.getBrowserDriver().getPageSource().contains(windowTitle)) {
							isWindowFind = true;
							return parentWindow;
						}
						else
							elementController.implicitWait(2);
					}catch(Exception e){System.out.println("Exception: "+e.getMessage());}
				}
				count++;
			} while(isWindowFind == false && count < 5);
			return parentWindow;	
		}
		catch(Exception exception) {
			throw new Exception("Exception occured in switchToWindowWithTitle method: " + exception.getMessage());
		}
	}

	public String switchToWindowWithPageTitle(String windowTitle) throws Exception {
		String windowHandle = null;
		int count = 0;

		// If current window contains page title then return the same window handle.
		if (BrowserDriver.getBrowserDriver().getPageSource().contains(windowTitle))
			return BrowserDriver.getBrowserDriver().getWindowHandle();
		try {
			do {
				Set<String> windowHandles = BrowserDriver.getBrowserDriver().getWindowHandles();
				for (String handle : windowHandles) {
					BrowserDriver.getBrowserDriver().switchTo().window(handle);
					if (BrowserDriver.getBrowserDriver().getPageSource().contains(windowTitle))
						return handle;
				}
				Thread.sleep(3000);
				count++;
			} while (count < 10);
		} catch (Exception exception) {
			throw new Exception("Unable to switch to Window with specified title. [switchToWindowWithPageTitle()]:"
					+ exception.getMessage());
		}
		return windowHandle;
	}

	/**
	 * Method to switch to window having a duplicate window
	 * @param windowTitle
	 * @return
	 * @throws Exception
	 */
	public String switchToWindowWithDuplicateTitle(String windowTitle) throws Exception {
		String parentWindow = null;
		boolean isWindowFind = false;
		int count = 0;
		try	{
			elementController.implicitWait(5);
			parentWindow = BrowserDriver.getBrowserDriver().getWindowHandle();
			do {
				Set<String> availableWindows = BrowserDriver.getBrowserDriver().getWindowHandles();	
				for(String winHandle : availableWindows)	{
					BrowserDriver.getBrowserDriver().switchTo().window(winHandle);
					if(BrowserDriver.getBrowserDriver().getPageSource().contains(windowTitle))
						if(!BrowserDriver.getBrowserDriver().getWindowHandle().equals(parentWindow))
							return parentWindow;
				}
				count++;
			} while(isWindowFind == false && count < 10);
			return parentWindow;
		} catch(Exception exception) {
			throw new Exception("Exception occured in switchToWindowWithTitle method: " + exception.getMessage());
		}
	}

	/**
	 * Method to switch to main window
	 * @throws Exception
	 */
	public void switchToMainWindow() throws Exception {
		try {
			BrowserDriver.getBrowserDriver().switchTo().window(mainWindow);
			switchToFrame();
		}catch(Exception exception) {
			throw new Exception("Exception occured in switchToMainWindow method: " + exception.getMessage());
		}
	}

	/**
	 * Method to  take screen shot
	 * @param screenShotName
	 */
	public void takeScreenShot(String screenShotName) {
		try {
			/*File screenshot = ((TakesScreenshot) BrowserDriver.getBrowserDriver()).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(baseDirPath + "\\TestReport\\" + screenShotName + ".jpeg"));*/

			// Take the full screen shot
			Robot robot = new Robot();
			BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenShot, "PNG", new File(baseDirPath + "\\TestReport\\" + screenShotName + ".png"));
		} catch (Exception exception) { 
			//throw exception;
		}
	}

	/**
	 * Method to quit browser
	 * @throws Exception 
	 */
	public void quitAllBrowser() throws Exception {
		try {
			// Remove all window handles from the map
			BrowserDriver.getBrowserDriver().quit();
			elementController.implicitWait(1);
			killBrowserInstance(BrowserDriver.browser);
			elementController.implicitWait(1);
		}catch(Exception exception) {
			throw new Exception("Exception occured in quitAllBrowser method: " + exception.getMessage());			
		}
	}	

	/**
	 * Method to enter text into text box
	 * @param textBoxId
	 * @param text
	 * @throws Exception
	 */
	public void enterTextIntoTextBox(String textBoxId, String text) throws Exception {
		try {
			if(text != null) {
				elementController.setWebelement(elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, textBoxId));	
				elementController.getWebelement().click();
				elementController.getWebelement().clear();
				elementController.getWebelement().sendKeys(text);
			}
		}catch(Exception exception) {
			throw new Exception("Failed to enter text into text field with element ID " + textBoxId + ": " + exception.getMessage());
		}
	}

	/**
	 * Method to enter text into text box
	 * @param WebElement
	 * @param text
	 * @throws Exception
	 */
	public void enterTextIntoTextBox(WebElement element, String text) throws Exception {
		try {
			if(text != null) {
				element.click();
				element.sendKeys(text);
			}
		}catch(Exception exception) {
			throw new Exception("Failed to enter text into text field with element :" + exception.getMessage());
		}
	}
	
	/**
	 * New method to enter text into text box
	 * @param textBoxId
	 * @param text
	 * @throws Exception
	 */
	public void enterTextIntoTextBoxNew(String textBoxId, String text) throws Exception {
		try {
			if(text != null) {
				elementController.setWebelement(elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, textBoxId));	
				elementController.getWebelement().clear();
				elementController.getWebelement().sendKeys(text);
			}
		}catch(Exception exception) {
			throw new Exception("Failed to enter text into text field with element ID " + textBoxId + ": " + exception.getMessage());
		}
	}

	/**
	 * Method to select item from the drop down
	 * @param selectBoxID
	 * @param option
	 * @throws Exception
	 */
	public void selectItemFromDropDownBox(String selectBoxID, String option) throws Exception {
		try {
			Select select = new Select(elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, selectBoxID));
			select.selectByVisibleText(option);
		}catch(Exception exception) {
			throw new Exception("Failed to select " + option + " from drop down box with element ID " + selectBoxID + " [selectItemFromDropDownBox()]: " + exception.getMessage());
		}
	}

	/**
	 * Generic method to click on button
	 * @param button - String type contains the button xpath 
	 */
	public void clickOnButton(String button) throws Exception {
		try {			
			if(button != null)
				elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, button).click();
		}catch(Exception exception) {
			throw new Exception("Failed to click on button with element ID " + button + " [clickOnButton()]: "  + exception.getMessage());
		}
	}

	/**
	 * Generic method to click on button using JavaScript
	 * @param button - String type contains the button xpath 
	 */
	public void clickOnButtonUsingJS(String button) throws Exception {
		try {			
			if(button != null)
			{
				WebElement element = elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, button);
				JavascriptExecutor executor = (JavascriptExecutor)BrowserDriver.getBrowserDriver();
				executor.executeScript("arguments[0].click();", element);
			}
		}catch(Exception exception) {
			throw new Exception("Failed to click on button with element ID " + button + " [clickOnButton()]: "  + exception.getMessage());
		}
	}
	
	
	/**
	 * Generic method to asynchronously click on button using JavaScript 
	 * @param button - String type contains the button xpath 
	 */
	public void clickOnButtonUsingJSAsync(String button) throws Exception {
		try {			
			if(button != null)
			{
				WebElement element = elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, button);
				JavascriptExecutor executor = (JavascriptExecutor)BrowserDriver.getBrowserDriver();
				executor.executeScript("var el = arguments[0];setTimeout(function(){el.click();}, 0);", element);
			}
		}catch(Exception exception) {
			throw new Exception("Failed to click on button with element ID " + button + " [clickOnButton()]: "  + exception.getMessage());
		}
	}
	
	
	/**
	 * Method to click on OK button for the dialog
	 * @throws Exception
	 */
	public void clickOnAcceptButton() throws Exception {
		try {
			elementController.implicitWait(5);
			Alert alt=BrowserDriver.getBrowserDriver().switchTo().alert();
			alt.accept();		
			elementController.implicitWait(1);
			//BrowserDriver.getBrowserDriver().switchTo().defaultContent();
			if(!BrowserDriver.getBrowserDriver().getPageSource().contains("Please wait while the page loads"))
				elementController.implicitWait(5);
			BrowserDriver.getBrowserDriver().switchTo().defaultContent();
/*			while(BrowserDriver.getBrowserDriver().getPageSource().contains("Please wait while the page loads")) {
				elementController.implicitWait(1);
			}
*/			
		}catch(Exception exception) {
			throw new Exception("Exception occured in clickOnAcceptButton method: " + exception.getMessage());
		}
	}

	/**
	 * Method to click on Cancel button for the dialog
	 * @throws Exception
	 */
	public void clickOnDismissButton() throws Exception {
		try {
			Alert alt=BrowserDriver.getBrowserDriver().switchTo().alert();
			alt.dismiss();		  		   
			BrowserDriver.getBrowserDriver().switchTo().defaultContent();
		}catch(Exception exception) {
			throw new Exception("Exception occured in clickOnDismissButton method: " + exception.getMessage());
		}
	}

	/**
	 * Method to get and handle the popup dialog
	 * @return
	 * @throws Exception
	 */
	public String getAlertMessage() throws Exception {
		String msg = null;
		waitForPopUpMessage();
		try {
			Alert alt=BrowserDriver.getBrowserDriver().switchTo().alert();
			msg = alt.getText().toString().trim();
			alt.accept();
			BrowserDriver.getBrowserDriver().switchTo().defaultContent();			
			return msg;
		}catch(Exception exception) {
			throw new Exception("Exception occured in getPopUpMessage method: " + exception.getMessage());
		}
	}

	/**
	 * Method to get and handle the popup dialog
	 * @return
	 * @throws Exception
	 */
	public String getPopUpMessage() throws Exception {
		try {
			return this.getWebDialogPopUpMessage();
		}catch(Exception exception) {
			throw new Exception("Exception occured in getPopUpMessage method: " + exception.getMessage());
		}
	}


	
	/**
	 * Method to wait until pop-up appears on the browser 
	 * @throws Exception
	 */
	public void waitForPopUpMessage() throws Exception {
		try {
			int wait = 0;
			// Loop to wait until pop-up appears on the screen.
			while (wait < BrowserDriver.getTimeOutForFindingWebElementInSeconds()) {
				try {  
					//Switch to pop up	
					BrowserDriver.getBrowserDriver().switchTo().alert();
					break;
				} catch (NoAlertPresentException Ex) { 
					//wait for 1 second
					elementController.implicitWait(1);
					wait++;
				}
			}

		} catch (Exception  exception) {
			throw new Exception("Exception occured in waitForgetPopUpMessage method: " + exception.getMessage());
		}
	}

	/**
	 * Method to click on check box
	 * @param checkBoxID
	 * @param checkOrUncheck
	 * @throws Exception
	 */
	public void clickOnCheckBox(String checkBoxID, Boolean checkOrUncheck) throws Exception {
		try {
			if(null != checkOrUncheck) {
				elementController.setWebelement(elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, checkBoxID));
				if(checkOrUncheck == true) {
					if(!elementController.getWebelement().isSelected())
						elementController.getWebelement().click();				
				} else {
					if(elementController.getWebelement().isSelected())
						elementController.getWebelement().click();		
				}
			}
		}catch(Exception exception) {
			throw new Exception("Exception occured in clickOnCheckBox method: " + exception.getMessage());
		}
	}

	/**
	 * Method to move the mouse pointer to a particular web element
	 * @param webElement
	 * @throws Exception
	 */
	public void moveMousePointToWebElement(WebElement webElement) throws Exception {
		try {
			Point point =webElement.getLocation();
			Robot robot = new Robot();
			robot.mouseMove(point.getX()+80, point.getY()+78);
		}catch(Exception exception) {
			throw new Exception("Exception occured in moveMousePointToWebElement method: " + exception.getMessage());
		}
	}

	/**
	 * Method to close current window
	 * @throws Exception
	 */
	public void closeCurrrentWindow() throws Exception {
		try {
			((JavascriptExecutor) BrowserDriver.getBrowserDriver()).executeScript("window.close();");
			//			BrowserDriver.getBrowserDriver().close();
		} catch(Exception exception) {
			throw new Exception("Unable to close the current window [closeCurrrentWindow()]:"+ exception.getMessage());
		}
	}

	/**
	 * Method to scroll the web element
	 * @param webElement
	 * @throws Exception
	 */
	public void scrollUpDownWebElement(WebElement webElement) throws Exception {
		try {
			Robot robot = new Robot();
			Point point = webElement.getLocation();
			robot.mouseMove(point.getX()+100, point.getY()+100);
			robot.mouseWheel(100);
		} catch(Exception exception) {
			throw new Exception("Unable to scroll the web element [scrollUpDownWebElement()]:" + exception.getMessage());
		}
	}

	/**
	 * Method to scroll up or down
	 * @param upOrDown
	 * @throws Exception
	 */
	public void scrollUpDown(String upOrDown) throws Exception {
		try {
			Actions actions = new Actions(BrowserDriver.getBrowserDriver());
			if(upOrDown.equalsIgnoreCase("UP")) 
				actions.keyDown(Keys.CONTROL).sendKeys(Keys.HOME).perform();
			else
				actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
		} catch(Exception exception) {
			throw new Exception("Failed to scroll up or down [scrollUpDown()]:" + exception.getMessage());
		}
	}

	/**
	 * Method to get current time
	 * @return
	 * @throws Exception 
	 */
	public String getCurrentTime() throws Exception {
		String time = null;
		try {
			Date startDate = new Date();  		  
			SimpleDateFormat df = new SimpleDateFormat("E yyyy-MM-dd HH: mm: ss a");	
			time = df.format(startDate);
		}catch(Exception exception) {
			throw new Exception("Unable to get current time [getCurrentTime()]:" + exception.getMessage());
		}
		return time;
	}

	/**
	 * Method to get time start and end time difference
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public String getTimeDifference(String startDateTime, String endDateTime) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("E yyyy-MM-dd HH: mm: ss");  
		Date endDate = null;
		Date startDate = null;
		Calendar endCalender = null;
		Calendar startCalender = null;
		String totalExecutionTime = "";
		long diff, diffSeconds, diffMinutes, diffHours, remainder = 0;
		try {
			startDate = df.parse(startDateTime);
			endDate = df.parse(endDateTime);

			startCalender = Calendar.getInstance();
			endCalender = Calendar.getInstance();

			startCalender.setTime(startDate);
			endCalender.setTime(endDate);

			diff = endCalender.getTimeInMillis() - startCalender.getTimeInMillis();

			// Get Hours
			diffHours = diff / (60 * 60 * 1000);

			// Get Minutes
			remainder = diff % (60 * 60 * 1000);
			diffMinutes = remainder / (60*1000);

			// Get Seconds
			remainder = diff % (60*1000);
			diffSeconds = remainder / 1000;

			if(diffHours == 0)
				totalExecutionTime = "00:" + diffMinutes + ":" + diffSeconds;
			else if(diffMinutes == 0)
				totalExecutionTime = diffHours + ":00:" + diffSeconds;
			else if(diffMinutes == 0)
				totalExecutionTime = diffHours + ":" + diffMinutes + ":00";
			else
				totalExecutionTime = diffHours + ":" + diffMinutes + ":" + diffSeconds;
			return totalExecutionTime;
		}catch(Exception exception) {
			throw new Exception("Unable to get current time [getCurrentTime()]"+exception.getMessage());
		}
	}

	/**
	 * Method to find option in drop down list
	 * @param dropDownListID
	 * @param option
	 * @throws Exception
	 */
	public boolean isOptionPresentinDropDownList(String dropDownListID, String option) throws Exception {
		try {
			elementController.setSelect(new Select(elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, dropDownListID)));
			List<WebElement> allOptions = elementController.getSelect().getOptions();
			for (WebElement currentOption: allOptions) {
				if(currentOption.getText().toString().trim().contains(option)){
					return true;
				}
			}
		}catch(Exception exception) {
			throw new Exception("Unable to find option in drown down list []: " + exception.getMessage());
		}
		return false;
	}

	/**
	 * Method to get page source code
	 * @return
	 * @throws Exception
	 */
	public String getPageSourceCode() throws Exception {
		try {
			return BrowserDriver.getBrowserDriver().getPageSource();
		} catch(Exception exception) {
			throw new Exception("Failed to get page source code [getPageSourceCode()]: " + exception.getMessage());
		}
	}

	/**
	 * Method to get current page URL
	 * @return
	 * @throws Exception
	 */
	public String getCurrentPageURL() throws Exception {
		try {
			return BrowserDriver.getBrowserDriver().getCurrentUrl();
		} catch(Exception exception) {
			throw new Exception("Failed to get page URL [getPageURL()]: " + exception.getMessage());
		}
	}

	/**
	 * Method to dismiss dialog box / Click on cancel button
	 * @return
	 * @throws Exception
	 */
	public boolean dismissDialogModal()throws Exception {
		try{
			elementController.setAlert(BrowserDriver.getBrowserDriver().switchTo().alert());
			elementController.getAlert().accept();
			elementController.implicitWait(4);
			return true;
		}catch(NoAlertPresentException noAlertPresentException) {
			return false;
		}
	}

	/**
	 * Method to get current window id
	 * @throws Exception
	 */
	public String getCurrentWindowID() throws Exception {
		try {
			return  BrowserDriver.getBrowserDriver().getWindowHandle().toString();
		} catch(Exception exception) {
			throw new Exception("Failed to get current window ID [getCurrentWindowID]: " + exception.getMessage());
		}
	}

	/**
	 * Method to check web element is present
	 * @param elementID
	 * @return
	 * @throws Exception
	 */
	public boolean isWebElementPresent(String elementID)throws Exception{
		try{
			if(null == elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, elementID))
				return false;
		}catch(Exception exception) {
			return false;
		}
		return true;
	}

	/**
	 * Method to wait until window get closed
	 * @throws Exception 
	 */
	public void waitForCurrentWindowToClose() throws Exception {
		String currentWindow = null;
		boolean hasWindowClosed = true;
		try {
			elementController.implicitWait(1);
			try {
				currentWindow = BrowserDriver.getBrowserDriver().getWindowHandle();
			} catch(Exception exception) {}
			do {
				hasWindowClosed = true;
				Set<String> availableWindows = BrowserDriver.getBrowserDriver().getWindowHandles();				  
				for(String winHandle : availableWindows) {
					if(winHandle.equalsIgnoreCase(currentWindow)) {
						hasWindowClosed = false;
					}
				}
				if(hasWindowClosed) {
					elementController.implicitWait(1);
				}
			} while(hasWindowClosed == false);
		}catch(Exception exception) {
			throw new Exception("Failed to wait for window to close [waitForWindowToClose]: " + exception.getMessage());
		}
	}

	/**
	 * Method to press 'Tab' key 
	 * @throws Exception 
	 */
	public void pressTabKey(WebElement webElement) throws Exception {
		try {
			webElement.sendKeys(Keys.TAB);	        
		} catch(Exception exception) {
			throw new Exception("Failed to press 'Tab' key [pressTabKey()]: " + exception.getMessage());
		}
	}

	/**
	 * Enter date into the field
	 * @param elementID
	 * @param day
	 * @param month
	 * @param year
	 * @throws Exception
	 */
	public void enterDate(String elementID, String day, String month, String year) throws Exception {
		try {
			String date = day + " " + month + " " + year;
			enterTextIntoTextBox(elementID, date);
		} catch(Exception exception) {
			throw new Exception("Failed to enter date into the field [enterDate()]: " + exception.getMessage());
		}
	}

	/**
	 * Method to enter date with mm/dd/yyyy format
	 * @param elementID
	 * @param month
	 * @param day
	 * @param year
	 * @throws Exception
	 */
	public void enterDateWithFormat(String elementID, String month, String day, String year) throws Exception {
		try {
			String date = month + "/" + day + "/" + year;
			enterTextIntoTextBox(elementID, date);
		} catch(Exception exception) {
			throw new Exception("Failed to enter date into the field [enterDateWithFormat()]: " + exception.getMessage());
		}
	}	

	/**
	 * killBrowserInstance() method kills the browser instance if it is running.
	 * 
	 * @param browser	The browser name
	 * 
	 * @return void
	 */
	public void killBrowserInstance(String browser) {
		String line = "";
		Process process = null;

		try {
			process = Runtime.getRuntime().exec("tasklist");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = reader.readLine()) != null) {
				// For Debug Purpose : Prints information of running processes under windows.
				// System.out.println(line);
				taskList.add(line);
			}
			if (browser.contains("ie")) {
				if (isProcessRunning("iexplore.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
				}
				if (isProcessRunning("IEDriverServer.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
				}
			} else if (browser.contains("firefox")) {
				if (isProcessRunning("firefox.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
				}
			} else if (browser.contains("chrome")) {
				if (isProcessRunning("chrome.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
				}
				if (isProcessRunning("chromedriver.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
				}
			}
		} catch (Exception e) {	}
	}

	/**
	 * isProcessRunning() method tells whether the process is running or not.
	 * 
	 * @param process	The name of a process.
	 * 
	 * @return return true if process is running else returns false
	 */
	public boolean isProcessRunning(String process) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).contains(process))
				return true;
		}
		return false;
	}

	/**
	 * Method to check check box is check or not
	 * @param checkBoxId
	 * @return
	 * @throws Exception
	 */
	public boolean isCheckBoxIsChecked(String checkBoxId) throws Exception {
		try {
			return elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, checkBoxId).isSelected();
		} catch(Exception exception) {
			throw new Exception("Failed to check check box is checked or not [isCheckBoxIsChecked()]: " + exception.getMessage());
		}
	}

	/**
	 * Method to get text from text box
	 * @param textBoxId
	 * @throws Exception
	 */
	public String getTextFromTextBox(String textBoxId) throws Exception {
		String text = null;
		try {
			if(textBoxId != null) {
				elementController.setWebelement(elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, textBoxId));	
				text = elementController.getWebelement().getText();
			}
		}catch(Exception exception) {
			throw new Exception("Failed to get text from text field with element ID " + textBoxId + ": " + exception.getMessage());
		}
		return text;
	}

	/**
	 * Method to switch to default content
	 * @throws Exception
	 */
	public void switchToDefaultContent() throws Exception {
		try {
			BrowserDriver.getBrowserDriver().switchTo().defaultContent();			
		} catch (Exception exception) {
			throw new Exception("Unable to switch to default contenet [switchToDefaultContent()]: "+exception.getMessage());
		}
	}
	
	/**
	 * Method to get all current window handles
	 * @throws Exception
	 */
	public Set<String> getAllWindowHandles() throws Exception {
		Set <String> allWindowHandles = null; 
		try {
			allWindowHandles = BrowserDriver.getBrowserDriver().getWindowHandles();	
			return allWindowHandles;
		} catch (Exception exception) {
			throw new Exception("Unable to get all window handles [getAllWindowHandles()]: "+exception.getMessage());
		}
		
	}
	
	/**
	 * Method to wait till new window is opened
	 * @param Set<String> beforeWindowHandles
	 * @throws Exception
	 */
	public void waitForNewWindowToOpen(Set<String> beforeWindowHandles) throws Exception {
		int handlesBefore = 0 , handlesAfter = 0 , timeInSec = 0;
		try {
			handlesBefore = beforeWindowHandles.size();
			do{
				handlesAfter = getAllWindowHandles().size();	
				elementController.implicitWait(2);
				timeInSec = timeInSec+2;
			}
			while((handlesBefore >= handlesAfter) && (timeInSec<30));
			
		} catch (Exception exception) {
			throw new Exception("Error while waiting for new window to opem "+exception.getMessage());
		}
		
	}
	
	/**
	 * Generic method to click on button using JavaScript
	 * @param button - String type contains the button xpath 
	 */
	public void doubleClickOnTheElement(WebElement ele) throws Exception {
		try {			
			if(ele != null)
			{
				Actions action = new Actions(BrowserDriver.getBrowserDriver());
				action.doubleClick(ele).perform();
			}
		}catch(Exception exception) {
			throw new Exception("Failed to double click on element with element ID " + ele + " [doubleClickOnTheElement()]: "  + exception.getMessage());
		}
	}
	
	/**
	 * Method to enter  click on escape button
	 * @throws Exception
	 */
	public void clickEscapeButton(String elementId) throws Exception {
		
		try {
			String element = elementController.elementParams.get(elementId).get("xpath");
			BrowserDriver.getBrowserDriver().findElement(By.xpath(element)).sendKeys(Keys.ESCAPE);
		} catch (Exception e) {
			 throw new Exception("Unable to click on Escape button :"+e.getMessage());
		}
	}

	/**
	 * Method to wait until pop-up appears on the browser 
	 * @throws Exception
	 */
	public void waitForWebDialogPopUpMessage() throws Exception {
		try {
			int wait = 0;
			// Loop to wait until pop-up appears on the screen.
			while (wait < BrowserDriver.getTimeOutForFindingWebElementInSeconds()) {
				try {  
					commonUIController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false ,"WebDialogPopUp");
					break;
				} catch (NoAlertPresentException Ex) { 
					//wait for 1 second
					commonUIController.implicitWait(1);
					wait++;
				}
			}

		} catch (Exception  exception) {
			throw new Exception("Exception occured in waitForgetPopUpMessage method: " + exception.getMessage());
		}
	}

	/**
	 * Method to get and handle the popup dialog
	 * @return
	 * @throws Exception
	 */
	public String getWebDialogPopUpMessage() throws Exception {
		String msg = null;
		this.waitForWebDialogPopUpMessage();
		try {
			commonUIController.setWebelement(commonUIController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, "WebDialogPopUpText"));
			msg = commonUIController.getWebelement().getText();
			clickWebDialogButton("OK");
			return msg;
		}catch(Exception exception) {
			throw new Exception("Exception occured in getPopUpMessage method: " + exception.getMessage());
		}
	}

	/**
	 * Method to handle the popup web dialog
	 * @return
	 * @throws Exception
	 */
	public void clickWebDialogButton(String ButtonText) throws Exception {
		try {
			elementController.implicitWait(2);
			String xpath =	commonUIController.elementParams.get("WebDialogPopUpButtons").get("xpath");
			xpath = xpath.replace("#", ButtonText);
			BrowserDriver.getBrowserDriver().findElement(By.xpath(xpath)).click();
			elementController.implicitWait(2);	
		}catch(Exception exception) {
			throw new Exception("Web Dialogue Box Not displayed [clickWebDialogButton()]: " + exception.getMessage());
		}
	}

	/**
	 * Method to find selected option in drop down list
	 * @param dropDownListID
	 * @throws Exception
	 */
	public String getSelectedOptioninDropDown(String dropDownListID) throws Exception {
		try {
			elementController.setSelect(new Select(elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, dropDownListID)));
			String selectedOption = elementController.getSelect().getFirstSelectedOption().getText();
			return selectedOption;
					
		}catch(Exception exception) {
			throw new Exception("Unable to find option in drown down list []: " + exception.getMessage());
		}
	}

	public void refreshCurrerntWindowF5() throws Exception {
		try {
			BrowserDriver.getBrowserDriver().navigate().refresh();
			elementController.implicitWait(1);
			Alert alert = BrowserDriver.getBrowserDriver().switchTo().alert();
			alert.accept();
			BrowserDriver.getBrowserDriver().switchTo().defaultContent();
			elementController.implicitWait(5);
		} catch (Exception e) {
			throw new Exception("Unable to refresh window" + e.getMessage());
		}
	}
	
	public void refreshCurrerntWindow() throws Exception {
		WebElement refreshButton; 
		try {
			refreshButton = BrowserDriver.getBrowserDriver().findElement(By.xpath("//input[@name='refresh']"));
			if(refreshButton != null){
				refreshButton.click();
				elementController.implicitWait(5);
				WebDriverWait wait = new  WebDriverWait(BrowserDriver.getBrowserDriver(),10);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='refresh']")));
			}
			
		} catch (Exception e) {
			Log4J.getlogger("[RefreshCurrentWindow()]").info("Refresh Button is not available");;
		}
		
	}
	
	/**
	 * Generic method to click on button using JavaScript
	 * @param button - String type contains the button xpath 
	 */
	public WebElement clickOnButtonUsingJavaScriptExecutor(String button) throws Exception {
		try {	
			WebElement element = null;
			if(button != null)
			{
				element = elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, button);
				JavascriptExecutor executor = (JavascriptExecutor)BrowserDriver.getBrowserDriver();
				executor.executeScript("arguments[0].click();", element);
				return element;
			}
			else
				throw new Exception("Failed to click on button with element ID " + button + " [clickOnButton()]");
			
		}catch(Exception exception) {
			throw new Exception("Failed to click on button with element ID " + button + " [clickOnButton()]: "  + exception.getMessage());
		}
	}

	/**
	 * Method to check if check box is enabled or disabled
	 * @param checkBoxId
	 * @return
	 * @throws Exception
	 */
	public boolean isCheckBoxIsEnabled(String checkBoxId) throws Exception {
		boolean flag =false;
		try {
			if (checkBoxId !=null){
				WebDriverWait wait = new WebDriverWait(BrowserDriver.getBrowserDriver(), 2);
				flag = wait.until(ExpectedConditions.elementSelectionStateToBe(By.xpath(elementController.elementParams.get(checkBoxId).get("xpath")), false));	
			}
		} catch(Exception exception) {
			throw new Exception("Failed to check check box is checked or not [isCheckBoxIsChecked()]: " + exception.getMessage());
		}
		return flag;
	}

	/**
	 * Method to check no of items/ values present in the drop down box
	 * @param selectBoxID - DropDownBoxId
	 * @return No Of Items/ Values present in the dropdown box 
	 * @throws Exception
	 */
	public int getCountOfItemsInDropDownBox(String selectBoxID) throws Exception {
		int noOfItems=0;
		try {
			Select select = new Select(elementController.waitForWebElement(BrowserDriver.getBrowserDriver(), null, false, selectBoxID));
			List<WebElement> webElement= select.getOptions();
			noOfItems=webElement.size();
		}catch(Exception exception) {
			throw new Exception("Failed to find no of items present in the drop down box " + selectBoxID + " [selectNoOfItemsInDropDownBox()]: " + exception.getMessage());
		}
		return noOfItems;
	}

}