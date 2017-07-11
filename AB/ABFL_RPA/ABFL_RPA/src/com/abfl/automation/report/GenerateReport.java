package com.abfl.automation.report;

import org.junit.Test;

import com.abfl.automation.utilities.Log4J;
import com.abfl.automation.utilities.ReportController;

/**
 * Class contains methods to generate report
 * @author Harshal.e
 *
 */

public class GenerateReport {
	
	ReportController reportController = null;
	private String packageAddress=this.getClass().getCanonicalName();
	
	@Test
	/**
	 * Method to generate report
	 * @throws Exception
	 */
	public void generateReport() throws Exception {
		try {
			Log4J.setLog4JProperties();
			reportController = new ReportController();
			reportController.generateHtmlReports();
		} catch (Exception e) {
			Log4J.getlogger(packageAddress).info("Exception occured in generateReport method: " + e.getMessage());
		}
	}
}
