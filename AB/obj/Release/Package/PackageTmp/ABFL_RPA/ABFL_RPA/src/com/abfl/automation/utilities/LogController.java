package com.abfl.automation.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Class contains methods to write logs into the CSV file
 * @author Harshal.e
 *
 */
public class LogController {
	File file = null;
	FileWriter fw = null;
	BufferedWriter bw = null;
	TestDataController testDataController;
	static final Logger logger = Logger.getLogger(LogController.class);	
	
	public LogController(String fileName) {
		try {			
			testDataController = new TestDataController();
			PropertyConfigurator.configure(".\\ElementRepository\\log4j.properties");
			if(fileName != null) {
				file = new File(".\\TestReport\\" + fileName + "Logs.csv");
				if(file.exists())
					file.delete();
				fw = new FileWriter(".\\TestReport\\" + fileName + "Logs.csv");
				bw = new BufferedWriter(fw);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to write log in the CSV file
	 * @param step - String type contains test case step / activity 
	 * @param discription - String type contains test case step description
	 * @param testStatus - String type contains test case status
	 */
	public void writeLog(String step, String discription, String testStatus){
		try {
			bw.write(step + "," + discription + "," + testStatus);
			bw.newLine();
		}catch(Exception exception) {
			System.out.println("Unable to write log: " +exception.getMessage());
		}
	}
	
	/**
	 * Method to close log writer
	 */
	public void closeLogWriter() {
		try {
			bw.close();
			fw.close();
		}catch(Exception exception) {
			System.out.println("Unable to close log writer instance: " +exception.getMessage());
		}
	}
	
	/**
	 * Method to write information mess
	 * @param msg
	 */
	public void info(String msg) {
		logger.info(msg);
	}
	
	/**
	 * Method to write error message into the log
	 * @param stackTraceElements
	 */
	public void error(String msg) {
		logger.error(msg);
	}
}
