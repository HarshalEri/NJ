package com.abfl.automation.utilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Class contains the method to write automation framework logs
 * @author Harshal.e
 *
 */
public class Log4J {
	
	static TestDataController testDataController;
	static String baseDirPath;
	static Logger logger = null;
	
	private Log4J() {}
	
	/**
	 * Method to set Log4J property file
	 */
	public static void setLog4JProperties() {
		testDataController = new TestDataController();
		PropertyConfigurator.configure(".\\ElementRepository\\log4j.properties");		
	}
	
	/**
	 * Get Method to get Logger class instance
	 * @param packageAddress
	 * @return
	 */
	public static Logger getlogger(String packageAddress) {
		logger = Logger.getLogger(packageAddress);
		return logger;
	}
}
