package com.abfl.automation.utilities;

import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Class contains methods to manage browser driver
 * @author Harshal.e
 *
 */
public class BrowserDriver {

	private static WebDriver driver = null;
	private static ArrayList<String> taskList = new ArrayList<String>();
	private static Robot robot = null;
	public static String browser = null;
	public static int timeOutForFindingWebElementInSeconds = 0;
	static TestDataController testDataController = null;
	
	
	/**
	 * Method to get browser driver
	 * @param _browser - Internet explorer
	 * @return - WebDriver type contains the instance of web driver
	 * @throws Exception 
	 */
	public static void setBrowserDriver(String _browser) throws Exception {
		testDataController = new TestDataController();
		browser = _browser;
		moveMouseOutSide();
		try {	
			switch (_browser.trim().toLowerCase()) {
			
				case "ie":
					killBrowserInstance();
					DesiredCapabilities dCap = DesiredCapabilities.internetExplorer();
					dCap.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
					dCap.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
					dCap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
					dCap.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
					dCap.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);

					//dCap.setCapability("enablePersistentHover", false);
					dCap.setJavascriptEnabled(true);
					//dCap.setCapability("ignoreZoomSetting", true);
					System.setProperty("webdriver.ie.driver", testDataController.baseDirPath + "\\ElementRepository\\IEDriverServer.exe");
					driver = new InternetExplorerDriver(dCap);
					break;
					
				case "chrome":
					killBrowserInstance();
					System.setProperty("webdriver.chrome.driver", testDataController.baseDirPath + "\\Drivers\\chromedriver.exe");
					ChromeOptions options = new ChromeOptions();
					options.addArguments("start-maximized");
					driver = new ChromeDriver(options);
					break;
					
				case "firefox":
					killBrowserInstance();
					FirefoxProfile profile = new FirefoxProfile();
		 			profile.setPreference("dom.successive_dialog_time_limit", 0);
		 			driver = new FirefoxDriver(profile);
					break; 
					
				default:
					System.out.println("Invalid browser, Please enter valid browser");
					break;
			}
			if(!(_browser.equalsIgnoreCase("chrome")))
				driver.manage().window().maximize();			
		}catch(Exception exception) {
			throw new Exception("Unable to set Browser Driver: " + exception.getMessage());
		}		
	}
	
	
	public static WebDriver getBrowserDriver() {
			return driver; 
	}
	
	/**
	 * killBrowserInstance() method kills the browser instance if it is running.
	 * 
	 * @param browser	The browser name
	 * 
	 * @return void
	 */
	public static void killBrowserInstance() {
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
			
			switch (browser) {
			
			case "ie":
				if (isProcessRunning("iexplore.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
				}
				if (isProcessRunning("IEDriverServer.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
				}
				break;
				
			case "chrome":
				if (isProcessRunning("chromedriver.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
				}
				break;
				
			case "firefox":
				if (isProcessRunning("firefox.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
				}
				break;
				
			case "edge":
				if (isProcessRunning("MicrosoftEdge.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM MicrosoftEdge.exe");
				}
				if (isProcessRunning("MicrosoftEdgeCP.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM MicrosoftEdge.exe");
				}
				if (isProcessRunning("MicrosoftWebDriver.exe")) {
					Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
				}
				break;

			default:
				System.out.println("Invalid browser, Please enter valid browser");
				break;
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
	public static boolean isProcessRunning(String process) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).contains(process))
				return true;
		}
		return false;
	}

	/**
	 * Method to move mouse pointer out side the browser window
	 * @throws Exception
	 */
	public static void moveMouseOutSide() throws Exception {
		try{
			robot = new Robot();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screenSize.getWidth();
			int height = (int) screenSize.getHeight();
			robot.mouseMove(height, width);
		}catch(Exception exception) {
			throw new Exception("Unable to move mouse pointer out side the window: "+exception.getMessage());
		}
	}
	
	/**
	 * Set time out for finding web element on browser in seconds
	 * @param timeOutInSeconds
	 */
	public static void setTimeOutForFindingWebElementInSeconds(int timeOutInSeconds) {
		timeOutForFindingWebElementInSeconds = timeOutInSeconds;
	}
	
	/**
	 * Get 	time out for finding web element on browser in seconds
	 * @return time out time
	 */
	public static int getTimeOutForFindingWebElementInSeconds() {
		return timeOutForFindingWebElementInSeconds;
	}

}
