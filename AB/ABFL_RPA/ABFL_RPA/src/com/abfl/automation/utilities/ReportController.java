package com.abfl.automation.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import com.csvreader.CsvReader;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;

/**
 * Class contains methods to generate the HTML report and send mail to recipients.
 * @author Harshal.e
 *
 */
public class ReportController {

	int stepNo = 0;
	CsvReader csvTestCase;
	String line = "";
	String errorMessage = "";
	int totalNoOfTestCases = 0;
	int totalNoOfTestCasesPassed = 0;
	int totalNoOfTestCasesFailed = 0;
	ArrayList<String> result = new ArrayList<String>();
	ArrayList<String> fileNames = new ArrayList<String>();
	Hashtable<String, String> errorList = new Hashtable<String, String>();
	Hashtable<String, String> testData = new Hashtable<String, String>();
	TestDataController testDataController = null;
	private String packageAddress=this.getClass().getCanonicalName();

	String PORT = "587";
	String STARTTLS = "true";
	String HOST = "smtp.gmail.com";
	String PASSWORD = "samaritan123";
	String FROM = "smtautomation1@gmail.com";
	String USER = "smtautomation1@gmail.com";
	private String TO[] = null;
	private String BCC[] = {"ashish.kulkarni@afourtech.com"};


	/**
	 * gererateHtmlReports() method generates the HTML reports using csv files.
	 * 
	 * @param path			The path where .htm files to be generated
	 * @param enviornment	The environment name on which the test suite is executed
	 * @param browser		the browser on which the test suite is executed
	 * 
	 * @return	void
	 * @throws Exception 
	 */
	public void generateHtmlReports() throws Exception {
		Log4J.setLog4JProperties();
		testDataController = new TestDataController();

		testData.putAll(testDataController.getTestData(null));

		String path = new java.io.File(".").getCanonicalPath();
		path = path +"\\TestReport\\";

		TO = testData.get("Recipients").split(",");

		String environment = testData.get("AMSURL").substring(0,testData.get("AMSURL").length() - 4);

		generateTestCaseIndex(path);

		generatePassedTestCasesIndex(path);

		generateFailedTestCasesIndex(path);

		generateTestCaseSummary(path, environment, testData.get("TestSuite"), testData.get("TestCasePriority"), testData.get("Browser"));

		generateFinalTestPassReport(path);

		generateSummaryPage(path, environment, testData.get("TestSuite"), testData.get("TestCasePriority"), testData.get("Browser"));

		createResultZipFile(path);

		sendMail(path);
	}

	/**
	 * Method to generate individual test case report
	 * @throws Exception 
	 */
	public void generateIndividualTestCaseReport() throws Exception {
		Log4J.setLog4JProperties();
		testDataController = new TestDataController();

		testData.putAll(testDataController.getTestData(null));

		String path = new java.io.File(".").getCanonicalPath();
		path = path +"\\TestReport\\";

		TO = testData.get("Recipients").split(",");

		String environment = testData.get("AMSURL").substring(0,testData.get("AMSURL").length() - 4);

		generateTestCaseIndex(path);

		generateTestCaseSummary(path, environment, testData.get("TestSuite"), testData.get("TestCasePriority"), testData.get("Browser"));

		generateFinalTestPassReport(path);

		generateSummaryPage(path, environment, testData.get("TestSuite"), testData.get("TestCasePriority"), testData.get("Browser"));
	}


	/**
	 * generateTestCaseIndex() method generates the TestCaseList.htm file containing the list of files.
	 * 
	 * @param path	The path TestCaseList.htm file to be generated
	 * 
	 * @return	void
	 */
	private void generateTestCaseIndex(String path) {
		try {
			OutputStream destination = new FileOutputStream(path + "TestCaseList.htm");
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xml = outputFactory.createXMLStreamWriter(destination);

			xml.writeStartDocument();
			xml.writeStartElement("html");
			xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
			xml.writeStartElement("head");
			xml.writeStartElement("title");
			xml.writeCharacters("TestCaseId Index");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("body");
			xml.writeStartElement("table");
			xml.writeAttribute("border", "0");
			xml.writeAttribute("width", "100%");
			xml.writeStartElement("th");
			xml.writeStartElement("tr");
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("strong");
			xml.writeCharacters("TestCaseName");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("center");
			xml.writeStartElement("strong");
			xml.writeCharacters("Result");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			totalNoOfTestCases = countCsvFiles(path);
			for (int i = 0; i < totalNoOfTestCases ; i++) {
				xml.writeStartElement("tr");
				xml.writeAttribute("style","WIDTH:235;BORDER:0;OVERFLOW-Y:scroll;WORD-WRAP:BREAK-WORD;OVERFLOW-X:hidden;padding:  2px 0px 2px 5px");
				xml.writeAttribute("bgColor", "#DDDDDD");
				xml.writeAttribute("padding", "");
				xml.writeAttribute("onMouseover", "this.bgColor='#EEEEEE'");
				xml.writeAttribute("onMouseout", "this.bgColor='#DDDDDD'");
				xml.writeStartElement("td");
				xml.writeStartElement("a");
				xml.writeAttribute("href",fileNames.get(i).substring(0, fileNames.get(i).length() - 8)+ ".htm");
				xml.writeAttribute("target", "targetframe");
				xml.writeCharacters(fileNames.get(i).substring(0,fileNames.get(i).length() - 8));
				xml.writeEndElement();
				xml.writeEndElement();
				xml.writeStartElement("td");
				xml.writeStartElement("center");
				xml.writeStartElement("font");
				if (result.get(i).equals("Pass") || result.get(i).equals(""))
					xml.writeAttribute("color", "GREEN");
				else if(result.get(i).equals("Fail"))
					xml.writeAttribute("color", "RED");
				xml.writeCharacters(result.get(i));
				xml.writeEndElement();
				xml.writeEndElement();
				xml.writeEndElement();
				xml.writeEndElement();				
			}
			xml.writeEndDocument();
			xml.close();
			destination.close();
		} catch (Exception e) {
			//System.out.println("\nError Occured in generateTestCaseIndex() function. \n");
			e.printStackTrace();
		}
	}

	/**
	 * Method to generate html test case list report for passed test cases
	 * @param path
	 */
	private void generatePassedTestCasesIndex(String path) {
		try {
			OutputStream destination = new FileOutputStream(path + "PassedTestCasesList.htm");
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xml = outputFactory.createXMLStreamWriter(destination);

			xml.writeStartDocument();
			xml.writeStartElement("html");
			xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
			xml.writeStartElement("head");
			xml.writeStartElement("title");
			xml.writeCharacters("TestCaseId Index");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("body");
			xml.writeStartElement("table");
			xml.writeAttribute("border", "0");
			xml.writeAttribute("width", "100%");
			xml.writeStartElement("th");
			xml.writeStartElement("tr");
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("strong");
			xml.writeCharacters("TestCaseName");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("center");
			xml.writeStartElement("strong");
			xml.writeCharacters("Result");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			for (int i = 0; i < totalNoOfTestCases ; i++) {
				if(result.get(i).equals("Pass")) {
					xml.writeStartElement("tr");
					xml.writeAttribute("style","WIDTH:235;BORDER:0;OVERFLOW-Y:scroll;WORD-WRAP:BREAK-WORD;OVERFLOW-X:hidden;padding:  2px 0px 2px 5px");
					xml.writeAttribute("bgColor", "#DDDDDD");
					xml.writeAttribute("padding", "");
					xml.writeAttribute("onMouseover", "this.bgColor='#EEEEEE'");
					xml.writeAttribute("onMouseout", "this.bgColor='#DDDDDD'");

					xml.writeStartElement("td");
					xml.writeStartElement("a");
					xml.writeAttribute("href",fileNames.get(i).substring(0, fileNames.get(i).length() - 8)+ ".htm");
					xml.writeAttribute("target", "targetframe");
					xml.writeCharacters(fileNames.get(i).substring(0,fileNames.get(i).length() - 8));
					xml.writeEndElement();
					xml.writeEndElement();

					xml.writeStartElement("td");
					xml.writeStartElement("center");
					xml.writeStartElement("font");
					if (result.get(i).equals("Pass") || result.get(i).equals(""))
						xml.writeAttribute("color", "GREEN");
					else if(result.get(i).equals("Fail"))
						xml.writeAttribute("color", "RED");
					xml.writeCharacters(result.get(i));
					xml.writeEndElement();
					xml.writeEndElement();
					xml.writeEndElement();

					xml.writeEndElement();	
				}
			}
			xml.writeEndDocument();
			xml.close();
			destination.close();
		} catch (Exception e) {
			//System.out.println("\nError Occured in generateTestCaseIndex() function. \n");
			e.printStackTrace();
		}
	}

	/**
	 * Method to generate html test case list report for failed test cases
	 * @param path
	 */
	private void generateFailedTestCasesIndex(String path) {
		try {
			OutputStream destination = new FileOutputStream(path + "FailedTestCasesList.htm");
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xml = outputFactory.createXMLStreamWriter(destination);

			xml.writeStartDocument();
			xml.writeStartElement("html");
			xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
			xml.writeStartElement("head");
			xml.writeStartElement("title");
			xml.writeCharacters("TestCaseId Index");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("body");
			xml.writeStartElement("table");
			xml.writeAttribute("border", "0");
			xml.writeAttribute("width", "100%");
			xml.writeStartElement("th");
			xml.writeStartElement("tr");
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("strong");
			xml.writeCharacters("TestCaseName");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("center");
			xml.writeStartElement("strong");
			xml.writeCharacters("Result");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			for (int i = 0; i < totalNoOfTestCases ; i++) {
				if(result.get(i).equals("Fail")) {
					xml.writeStartElement("tr");
					xml.writeAttribute("style","WIDTH:235;BORDER:0;OVERFLOW-Y:scroll;WORD-WRAP:BREAK-WORD;OVERFLOW-X:hidden;padding:  2px 0px 2px 5px");
					xml.writeAttribute("bgColor", "#DDDDDD");
					xml.writeAttribute("padding", "");
					xml.writeAttribute("onMouseover", "this.bgColor='#EEEEEE'");
					xml.writeAttribute("onMouseout", "this.bgColor='#DDDDDD'");

					xml.writeStartElement("td");
					xml.writeStartElement("a");
					xml.writeAttribute("href",fileNames.get(i).substring(0, fileNames.get(i).length() - 8)+ ".htm");
					xml.writeAttribute("target", "targetframe");
					xml.writeCharacters(fileNames.get(i).substring(0,fileNames.get(i).length() - 8));
					xml.writeEndElement();
					xml.writeEndElement();

					xml.writeStartElement("td");
					xml.writeStartElement("center");
					xml.writeStartElement("font");
					if (result.get(i).equals("Pass") || result.get(i).equals(""))
						xml.writeAttribute("color", "GREEN");
					else if(result.get(i).equals("Fail"))
						xml.writeAttribute("color", "RED");
					xml.writeCharacters(result.get(i));
					xml.writeEndElement();
					xml.writeEndElement();
					xml.writeEndElement();

					xml.writeEndElement();	
				}
			}
			xml.writeEndDocument();
			xml.close();
			destination.close();
		} catch (Exception e) {
			//System.out.println("\nError Occured in generateTestCaseIndex() function. \n");
			e.printStackTrace();
		}
	}

	/**
	 * generateTestCaseSummary() method generates the TestPassSummary.htm page.
	 * 
	 * @param path			The path where TestPassSummary.htm file to be generated
	 * @param enviornment	The environment name on which the test suite is executed
	 * @param browser		the browser on which the test suite is executed
	 * 
	 * @return	void
	 * @throws Exception 
	 */
	private void generateTestCaseSummary(String path, String enviornment, String testSuite, String testCasePriority, String browser) throws Exception {
		try {
			OutputStream destination = new FileOutputStream(path + "TestPassSummary.htm");
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xml = outputFactory.createXMLStreamWriter(destination);

			xml.writeStartDocument();
			xml.writeStartElement("html");
			xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
			xml.writeStartElement("head");
			xml.writeStartElement("title");
			xml.writeCharacters("TestCaseId Summary");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("body");
			xml.writeStartElement("table");
			xml.writeAttribute("border", "0");
			xml.writeAttribute("cellpadding", "0");
			xml.writeAttribute("cellspacing", "0");
			xml.writeAttribute("width", "100%");
			xml.writeStartElement("tr");
			xml.writeAttribute("bgColor", "#DDDDDD");
			xml.writeStartElement("td");

			xml.writeEmptyElement("br");
			xml.writeStartElement("a");
			xml.writeAttribute("href", "TestCaseList.htm");
			xml.writeAttribute("style", "text-decoration: none");
			xml.writeAttribute("target", "TestCaseList");
			xml.writeStartElement("b");
			xml.writeStartElement("font");
			xml.writeAttribute("color", "BLUE");
			xml.writeCharacters("Number of TestCases Executed : ");
			xml.writeCharacters("" + (totalNoOfTestCasesPassed + totalNoOfTestCasesFailed));
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();

			xml.writeEmptyElement("br");
			xml.writeStartElement("a");
			xml.writeAttribute("href", "PassedTestCasesList.htm");
			xml.writeAttribute("style", "text-decoration: none");
			xml.writeAttribute("target", "TestCaseList");
			xml.writeStartElement("b");
			xml.writeStartElement("font");
			xml.writeAttribute("color", "GREEN");
			xml.writeCharacters("Number of TestCases Passed : ");
			xml.writeCharacters("" + totalNoOfTestCasesPassed);
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();

			xml.writeEmptyElement("br");
			xml.writeStartElement("a");
			xml.writeAttribute("href", "FailedTestCasesList.htm");
			xml.writeAttribute("style", "text-decoration: none");
			xml.writeAttribute("target", "TestCaseList");
			xml.writeStartElement("b");
			xml.writeStartElement("font");
			xml.writeAttribute("color", "RED");
			xml.writeCharacters("Number of TestCases Failed : ");
			xml.writeCharacters("" + totalNoOfTestCasesFailed);
			xml.writeEndElement();	
			xml.writeEndElement();
			xml.writeEndElement();

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("Environment : ");
			xml.writeEndElement();
			xml.writeCharacters("" + enviornment);

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("Test Suite : ");
			xml.writeEndElement();
			xml.writeCharacters("" + testSuite);

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("Test Cases Priority : ");
			xml.writeEndElement();
			xml.writeCharacters("" + testCasePriority);

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("Browser :");	
			xml.writeEndElement();
			if(browser.equalsIgnoreCase("ie"))			
				xml.writeCharacters("Internet Explorer");			
			if(browser.equalsIgnoreCase("ie8"))			
				xml.writeCharacters("Internet Explorer 8");
			if(browser.equalsIgnoreCase("ie9"))			
				xml.writeCharacters("Internet Explorer 9");
			if(browser.equalsIgnoreCase("ie10"))			
				xml.writeCharacters("Internet Explorer 10");
			if(browser.equalsIgnoreCase("firefox"))
				xml.writeCharacters("Firefox");
			if(browser.equalsIgnoreCase("chrome"))
				xml.writeCharacters("Chrome");			

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("Host Name : ");
			xml.writeEndElement();
			xml.writeCharacters("" + getHostName());

			xml.writeEmptyElement("br");	
			xml.writeStartElement("b");
			xml.writeCharacters("Start Datetime : ");
			xml.writeEndElement();
			xml.writeCharacters(getDateTime(path, "start"));

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("End Datetime : ");
			xml.writeEndElement();
			xml.writeCharacters(getDateTime(path, "end"));	

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");									
			xml.writeCharacters("Total Execution Time (hh: mm: ss): ");
			xml.writeEndElement();
			xml.writeCharacters("" + getTotalExecutionTime(path));

			xml.writeEmptyElement("br");
			xml.writeEndElement();
			xml.writeStartElement("td");
			xml.writeStartElement("center");
			xml.writeStartElement("img");
			xml.writeAttribute("src", "logo.png");
			xml.writeAttribute("style", "border-style: none");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();

			// Design for the right side
			xml.writeStartElement("td");
			xml.writeStartElement("right");									

			xml.writeEndElement();

			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndDocument();
			xml.close();
			destination.close();
		} catch (Exception e) {
			throw new Exception("Exception occured in generateTestCaseSummary method: " + e.getMessage());
		}
	}

	/**
	 * generateFinalTestPassReport() method generates the index.htm page.
	 *
	 * @param path	The path where index.htm file to be generated.
	 * 
	 * @return	void
	 */
	private void generateFinalTestPassReport(String path) {
		try {
			OutputStream destination = new FileOutputStream(path + "index.htm");
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xml = outputFactory.createXMLStreamWriter(destination);

			xml.writeStartDocument();
			xml.writeStartElement("html");
			xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
			xml.writeStartElement("head");
			xml.writeStartElement("title");
			xml.writeCharacters("Samaritan Automation TestPass Report: ");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("frameset");
			xml.writeAttribute("rows", "39%,*");
			xml.writeAttribute("border", "0");
			xml.writeStartElement("frame");
			xml.writeAttribute("src", "TestPassSummary.htm");
			xml.writeEndElement();
			xml.writeStartElement("frameset");
			xml.writeAttribute("cols", "35%,*");
			xml.writeStartElement("frame");
			xml.writeAttribute("name", "TestCaseList");
			xml.writeAttribute("src", "TestCaseList.htm");
			xml.writeEndElement();
			xml.writeStartElement("frame");
			xml.writeAttribute("name", "targetframe");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndDocument();
			xml.close();
			destination.close();
			System.out.println("HTML Reports Generated Successfully...");
		} catch (Exception e) {
			//			System.out.println("\nError Occured in generateFinalTestPassReport() function. \n");
			//			e.printStackTrace();
		}
	}

	/**
	 * generateAutomationReportInHtml() method generates test case specific .htm files.
	 * 
	 * @param testName	The name of the test
	 * @param file		The Object of CSV file.
	 * @param path		The path where .htm files to be generated.
	 * 
	 * @return	void
	 */
	private void generateAutomationReportInHtml(String testName, File file,String path) {
		try {
			csvTestCase = new CsvReader(file.getCanonicalPath());
			OutputStream destination = new FileOutputStream(path + testName.substring(0, testName.length() - 8) + ".htm");
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xml = outputFactory.createXMLStreamWriter(destination);

			xml.writeStartDocument();
			xml.writeStartElement("html");
			xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
			xml.writeStartElement("head");
			xml.writeStartElement("title");
			xml.writeCharacters("TestCaseId: " + testName.substring(0, testName.length() - 8));
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("body");
			xml.writeStartElement("strong");
			xml.writeStartElement("font");
			xml.writeAttribute("color", "BLUE");
			xml.writeCharacters("TestCase Id: " + testName.substring(0, testName.length() - 8));
			xml.writeEndElement();
			//						xml.writeStartElement("font");
			//							xml.writeAttribute("color", "BLUE");
			//							xml.writeCharacters("  [ Time: " + getTimeTakenByTestCase(path, testName.substring(0, testName.length() - 8))+" ]");
			//						xml.writeEndElement();
			xml.writeStartElement("table");
			xml.writeAttribute("border", "0");
			xml.writeAttribute("width", "100%");
			xml.writeStartElement("th");
			xml.writeStartElement("tr");
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("center");
			xml.writeStartElement("strong");
			xml.writeCharacters("Step No");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("strong");
			xml.writeCharacters("Activity");
			xml.writeEndElement();
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("strong");
			xml.writeCharacters("Description");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("center");
			xml.writeStartElement("strong");
			xml.writeCharacters("Result");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			/*xml.writeStartElement("td");
										xml.writeAttribute("bgColor", "#BBBBBB");
										xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
										xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
										xml.writeStartElement("center");
											xml.writeStartElement("strong");
												xml.writeCharacters("Error");
											xml.writeEndElement();
										xml.writeEndElement();
									xml.writeEndElement();*/
			xml.writeEndElement();
			xml.writeEndElement();
			while (csvTestCase.readRecord()) {
				xml.writeStartElement("tr");
				xml.writeAttribute("style","WIDTH:235;BORDER:0;OVERFLOW-Y:scroll;WORD-WRAP:BREAK-WORD;OVERFLOW-X:hidden;padding:  2px 0px 2px 5px");
				xml.writeAttribute("bgColor", "#DDDDDD");
				xml.writeAttribute("padding", "");
				xml.writeAttribute("onMouseover", "this.bgColor='#EEEEEE'");
				xml.writeAttribute("onMouseout", "this.bgColor='#DDDDDD'");
				xml.writeStartElement("td");
				xml.writeStartElement("center");
				xml.writeCharacters((++stepNo) + "");
				xml.writeEndElement();
				xml.writeEndElement();

				xml.writeStartElement("td");
				xml.writeStartElement("a");
				if (csvTestCase.get(2).equals("Passed") || csvTestCase.get(2).equals("")) {
					if(csvTestCase.get(0).contains("Validation Result")) {
						xml.writeAttribute("href",testName.substring(0, testName.length() - 8)+".html");
						xml.writeAttribute("target", "targetframe");
						xml.writeStartElement("font");
						xml.writeAttribute("color", "GREEN");
						xml.writeCharacters(csvTestCase.get(0));
						xml.writeEndElement();
					}
					else {
						xml.writeStartElement("font");
						xml.writeAttribute("color", "GREEN");
						xml.writeCharacters(csvTestCase.get(0));
						xml.writeEndElement();
					}
				}
				else if(csvTestCase.get(2).equals("Failed")) {
					if(csvTestCase.get(0).contains("Validation Result")) {
						xml.writeAttribute("href",testName.substring(0, testName.length() - 8)+".html");
						xml.writeAttribute("target", "targetframe");
						xml.writeStartElement("font");
						xml.writeAttribute("color", "RED");
						xml.writeCharacters(csvTestCase.get(0));
						xml.writeEndElement();
					}
					else {
						xml.writeStartElement("font");
						xml.writeAttribute("color", "RED");
						xml.writeCharacters(csvTestCase.get(0));
						xml.writeEndElement();
					}
				}
				xml.writeEndElement();
				xml.writeEndElement();

				xml.writeStartElement("td");
				xml.writeStartElement("font");
				if (csvTestCase.get(2).equals("Passed") || csvTestCase.get(2).equals(""))
					xml.writeAttribute("color", "GREEN");	
				else if(csvTestCase.get(2).equals("Failed")) 
					xml.writeAttribute("color", "RED");
				xml.writeCharacters(csvTestCase.get(1));
				xml.writeEndElement();
				xml.writeEndElement();

				xml.writeStartElement("td");
				xml.writeStartElement("center");
				xml.writeStartElement("a");
				if (csvTestCase.get(2).equals("Passed") || csvTestCase.get(2).equals("")) {
					xml.writeStartElement("font");
					xml.writeAttribute("color", "GREEN");
					xml.writeCharacters(csvTestCase.get(2));
					xml.writeEndElement();
				}
				else if(csvTestCase.get(2).equals("Failed")){
					xml.writeAttribute("href",testName.substring(0, testName.length() - 8)+".png");
					xml.writeAttribute("target", "targetframe");
					xml.writeStartElement("font");
					xml.writeAttribute("color", "RED");													
					xml.writeCharacters(csvTestCase.get(2));
					xml.writeEndElement();
				}
				xml.writeEndElement();
				xml.writeEndElement();
				xml.writeEndElement();
				/*xml.writeStartElement("td");
									xml.writeStartElement("center");
										xml.writeStartElement("font");
									if (csvTestCase.get(2).equals("Passed"))
											xml.writeAttribute("color", "GREEN");	
									else
											xml.writeAttribute("color", "RED");
											xml.writeCharacters(csvTestCase.get(3));
										xml.writeEndElement();
									xml.writeEndElement();
								xml.writeEndElement();*/
				xml.writeEndElement();
			}
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndDocument();
			xml.close();
			destination.close();
			csvTestCase.close();
			stepNo = 0;
		} catch (Exception e) {
			//			System.out.println("\nError in generateAutomationReportInHtml() function. \n");
			//			e.printStackTrace();
		}
	}

	/**
	 * countCsvFiles() method counts the no. of csv files present under the specified path.
	 * 
	 * @param path	The path where csv files are present.
	 * 
	 * @return	numOfFiles	Returns No. of CSV files
	 */
	private int countCsvFiles(String path){
		int numOfFiles = 0;
		boolean flag = false;
		try {
			File f = new File(path);
			if (f.isDirectory()) {
				File[] children = f.listFiles((FileFilter) FileFileFilter.FILE);

				//Arrays.sort(children,LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
				Arrays.sort(children,NameFileComparator.NAME_COMPARATOR);

				for (int i = 0; i < children.length; i++) {
					if (children[i].getName().contains(".csv")) {
						//						if (!children[i].getName().toString().equalsIgnoreCase("TestTimeLogs.csv")) {
						fileNames.add(children[i].getName());
						FileReader fr = new FileReader(children[i]);
						BufferedReader br = new BufferedReader(fr);
						while ((line = br.readLine()) != null) {
							if (line.contains("Passed")) {
								flag = true;
							} else if(line.contains("Failed")) {
								flag = false;
								generateAutomationReportInHtml(children[i].getName(), children[i],path);
								break;
							}
							generateAutomationReportInHtml(children[i].getName(), children[i],path);
						}
						br.close();
						fr.close();
						if (flag == true) {
							totalNoOfTestCasesPassed++;
							result.add("Pass");
						} else {
							totalNoOfTestCasesFailed++;
							result.add("Fail");
						}
						numOfFiles++;
						//						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Oops, Something went wrong...\n" + e.getMessage());
		}
		return numOfFiles;
	}


	/**
	 * displayFiles() method prints the names of files in the directory.
	 * 
	 * @param files		Array of object of Files
	 * 
	 * @return	void
	 */
	public void displayFiles(File[] files) {
		for (File file : files) {
			System.out.printf("File: %-20s Last Modified:" + new Date(file.lastModified()) + "\n", file.getName());			
		}
	}

	/**
	 * generateSummaryPage() method generates the summary of the test automation execution.
	 * 
	 * @param path			The path where to store generated Summary.htm file
	 * @param environment	The environment where the test automation executed
	 * @param browser		The browser on which the test automation executed
	 * 
	 * @return	void
	 * @throws Exception 
	 */
	private void generateSummaryPage(String path, String environment, String testSuite, String testCasePriority, String browser) throws Exception
	{
		try {
			OutputStream destination = new FileOutputStream(path + "Summary.htm");
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xml = outputFactory.createXMLStreamWriter(destination);

			xml.writeStartDocument();
			xml.writeStartElement("html");
			xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
			xml.writeStartElement("head");
			xml.writeStartElement("title");
			xml.writeCharacters("TestCaseId Index");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("body");
			xml.writeStartElement("p");
			xml.writeStartElement("b");
			xml.writeCharacters("Hi,");
			xml.writeStartElement("br");
			xml.writeCharacters("Here is a summary of the test automation execution. For more details please check index.htm file in the attachment.");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("table");
			xml.writeAttribute("border", "0");
			xml.writeAttribute("cellpadding", "0");
			xml.writeAttribute("cellspacing", "0");
			xml.writeAttribute("width", "100%");
			xml.writeStartElement("tr");
			xml.writeAttribute("bgColor", "#DDDDDD");
			xml.writeStartElement("td");

			xml.writeStartElement("b");
			xml.writeStartElement("font");
			xml.writeAttribute("color", "BLUE");
			xml.writeCharacters("Number of TestCases Executed : ");
			xml.writeCharacters("" + (totalNoOfTestCasesPassed + totalNoOfTestCasesFailed));
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeStartElement("font");
			xml.writeAttribute("color", "GREEN");
			xml.writeCharacters("Number of TestCases Passed : ");
			xml.writeCharacters("" + totalNoOfTestCasesPassed);
			xml.writeEndElement();
			xml.writeEndElement();

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeStartElement("font");
			xml.writeAttribute("color", "RED");
			xml.writeCharacters("Number of TestCases Failed : ");
			xml.writeCharacters("" + totalNoOfTestCasesFailed);
			xml.writeEndElement();
			xml.writeEndElement();

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("Environment : ");
			xml.writeEndElement();
			xml.writeCharacters("" + environment);

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("Test Suite : ");
			xml.writeEndElement();
			xml.writeCharacters("" + testSuite);

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("Test Cases Priority : ");
			xml.writeEndElement();
			xml.writeCharacters("" + testCasePriority);

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("Browser : ");	
			xml.writeEndElement();
			if(browser.equalsIgnoreCase("ie"))			
				xml.writeCharacters("Internet Explorer");			
			if(browser.equalsIgnoreCase("ie8"))			
				xml.writeCharacters("Internet Explorer 8");
			if(browser.equalsIgnoreCase("ie9"))			
				xml.writeCharacters("Internet Explorer 9");
			if(browser.equalsIgnoreCase("ie10"))			
				xml.writeCharacters("Internet Explorer 10");
			if(browser.equalsIgnoreCase("firefox"))
				xml.writeCharacters("Firefox");
			if(browser.equalsIgnoreCase("chrome"))
				xml.writeCharacters("Chrome");							

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("Host Name : ");
			xml.writeEndElement();
			xml.writeCharacters("" + getHostName());

			xml.writeEmptyElement("br");	
			xml.writeStartElement("b");
			xml.writeCharacters("Start Datetime : ");
			xml.writeEndElement();
			xml.writeCharacters(getDateTime(path, "start"));

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");
			xml.writeCharacters("End Datetime : ");
			xml.writeEndElement();
			xml.writeCharacters(getDateTime(path, "end"));	

			xml.writeEmptyElement("br");
			xml.writeStartElement("b");									
			xml.writeCharacters("Total Execution Time (hh: mm: ss): ");
			xml.writeEndElement();
			xml.writeCharacters("" + getTotalExecutionTime(path));
			xml.writeEndElement();
			xml.writeStartElement("td");
			xml.writeStartElement("center");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeStartElement("table");
			xml.writeAttribute("border", "0");
			xml.writeAttribute("width", "100%");
			xml.writeStartElement("th");
			xml.writeStartElement("tr");
			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("strong");
			xml.writeCharacters("TestCaseName");
			xml.writeEndElement();
			xml.writeEndElement();

			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("center");
			xml.writeStartElement("strong");
			xml.writeCharacters("Result");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();

			xml.writeStartElement("td");
			xml.writeAttribute("bgColor", "#BBBBBB");
			xml.writeAttribute("onMouseover", "this.bgColor='#DDDDDD'");
			xml.writeAttribute("onMouseout", "this.bgColor='#BBBBBB'");
			xml.writeStartElement("center");
			xml.writeStartElement("strong");
			xml.writeCharacters("Execution Time (hh: mm: ss)");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();

			xml.writeEndElement();
			xml.writeEndElement();
			for (int i = 0; i < totalNoOfTestCases ; i++) {
				xml.writeStartElement("tr");
				xml.writeAttribute("style","WIDTH:235;BORDER:0;OVERFLOW-Y:scroll;WORD-WRAP:BREAK-WORD;OVERFLOW-X:hidden;padding:  2px 0px 2px 5px");
				xml.writeAttribute("bgColor", "#DDDDDD");
				xml.writeAttribute("padding", "");
				xml.writeAttribute("onMouseover", "this.bgColor='#EEEEEE'");
				xml.writeAttribute("onMouseout", "this.bgColor='#DDDDDD'");

				xml.writeStartElement("td");
				xml.writeStartElement("font");
				xml.writeAttribute("Color", "BLUE");
				xml.writeCharacters(fileNames.get(i).substring(0,fileNames.get(i).length() - 8));
				xml.writeEndElement();
				xml.writeEndElement();

				xml.writeStartElement("td");
				xml.writeStartElement("center");
				xml.writeStartElement("font");
				if (result.get(i).equals("Pass") || result.get(i).equals(""))
					xml.writeAttribute("color", "GREEN");
				else if(result.get(i).equals("Fail"))
					xml.writeAttribute("color", "RED");
				xml.writeCharacters(result.get(i));
				xml.writeEndElement();
				xml.writeEndElement();
				xml.writeEndElement();

				xml.writeStartElement("td");
				xml.writeStartElement("center");
				xml.writeStartElement("font");
				if (result.get(i).equals("Pass"))
					xml.writeAttribute("color", "GREEN");
				else
					xml.writeAttribute("color", "RED");
				xml.writeCharacters(getTimeTakenByTestCase(path, fileNames.get(i).substring(0,fileNames.get(i).length() - 8)));
				xml.writeEndElement();
				xml.writeEndElement();
				xml.writeEndElement();

				xml.writeEndElement();				
			}
			xml.writeEndDocument();
			xml.writeStartElement("br");
			xml.writeStartElement("p");
			xml.writeStartElement("b");
			xml.writeCharacters("Thanks & Regards,");
			xml.writeStartElement("br");
			xml.writeCharacters("Automation Team");
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.writeEndElement();
			xml.close();
			destination.close();
		} catch (Exception e) {
			throw new Exception("Exception occured in generateSummaryPage method: " + e.getMessage());
		}		
	}

	/**
	 * sendMail() method sends the Summary.htm and the Result.zip file to the recipients specified in TO
	 * 
	 * @param path		The path where Summary.htm and Result.zip files are present
	 * 
	 * @return	void
	 * @throws Exception 
	 */
	private void sendMail(String resultsPath) throws Exception {
		try {
			TestDataController data = new TestDataController();
			Hashtable<String, String> table=data.getSMTConfigurationProperty();


			System.out.println("Sending mail...");
			Properties props = new Properties();
			props.put("mail.smtp.port", PORT);
			props.setProperty("mail.user", USER);
			props.setProperty("mail.host", HOST);
			props.setProperty("mail.password", PASSWORD);
			props.put("mail.smtp.starttls.enable", STARTTLS);
			props.setProperty("mail.transport.protocol", "smtp");

			Session mailSession = Session.getDefaultInstance(props, null);
			MimeMessage message = new MimeMessage(mailSession);
			for(int i=0;i<TO.length;i++) {
				message.addRecipient(RecipientType.TO, new InternetAddress(TO[i]));
			}
			for(int i=0;i<BCC.length;i++) {
				message.addRecipient(RecipientType.BCC, new InternetAddress(BCC[i]));
			}
			message.addFrom(new InternetAddress[] { new InternetAddress(FROM) });
			Multipart mp = new MimeMultipart();

			MimeBodyPart mbp = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(resultsPath + "Summary.htm");
			mbp.setDataHandler(new DataHandler(fds));
			mp.addBodyPart(mbp);

			MimeBodyPart mbp1 = new MimeBodyPart();
			FileDataSource fds1 = new FileDataSource(resultsPath + "Result_"+testData.get("TestSuite")+".zip");
			mbp1.setDataHandler(new DataHandler(fds1));
			mbp1.setFileName("Result_"+testData.get("TestSuite")+".zip");
			mp.addBodyPart(mbp1);

			message.setContent(mp);
			message.setSentDate(new Date());
			Date sentDate = message.getSentDate();
			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-YYYY");
			String date = format.format(sentDate);
			InetAddress addr = InetAddress.getLocalHost();
			String hostName = addr.getHostName();
			//message.setSubject("["+hostName+"]"+" Automation Test Pass Report ["+table.get("TestSuite")+"] - " + sentDate.getDate()+" "+sentDate.getgetMonth()+" "+sentDate.getYear());
			message.setSubject("["+hostName+"]"+"Samaritan Automation Report ["+table.get("TestSuite")+"] - " + date);

			Transport transport1 = mailSession.getTransport("smtp");
			transport1.connect(HOST, USER, PASSWORD);
			transport1.sendMessage(message,message.getRecipients(Message.RecipientType.TO));			
			transport1.sendMessage(message,message.getRecipients(Message.RecipientType.BCC));
			for(int i = 0; i<TO.length; i++) {
				System.out.println("Mail Send to "+TO[i]);
				Log4J.getlogger(packageAddress).info("Mail Send to "+TO[i]);
			}
			for(int j = 0; j<BCC.length; j++) {
				System.out.println("Mail Send to "+BCC[j]);
				Log4J.getlogger(packageAddress).info("Mail Send to "+BCC[j]);
			}
			transport1.close();
		} catch (Exception e) {
			throw new Exception("Exception occured in sendMail method:" + e.getMessage());
		}
	}

	/**
	 * createResultZipFile() method creates a zip file of the result .htm files.
	 * 
	 * @param path		The path where to store the created Result.zip file
	 * 
	 * @return	void
	 * @throws Exception 
	 */
	private void createResultZipFile(String path) throws Exception {
		try {
			String zipFile = path + "Result_"+testData.get("TestSuite")+".zip";
			String sourceDirectory = path;

			byte[] buffer = new byte[1024];
			FileOutputStream fout = new FileOutputStream(zipFile);
			ZipOutputStream zout = new ZipOutputStream(fout);
			File dir = new File(sourceDirectory);
			if (!dir.isDirectory()) {
				System.out.println(sourceDirectory + " is not a directory");
			} else {
				File[] files = dir.listFiles();
				FileInputStream fin = null;
				for (int i = 0; i < files.length; i++) {
					if(files[i].getName().contains(".htm") || files[i].getName().contains(".png") || files[i].getName().contains(".jpeg") || files[i].getName().contains(".log"))
					{
						fin = new FileInputStream(files[i]);
						zout.putNextEntry(new ZipEntry(files[i].getName()));
						int length;
						while ((length = fin.read(buffer)) > 0) {
							zout.write(buffer, 0, length);
						}
					}
				}
				zout.closeEntry();
				fin.close();
			}
			zout.close();
			System.out.println("Results Zip file has been created...");
			Log4J.getlogger(packageAddress).info("Results Zip file has been created...");
		} catch (Exception e) {
			throw new Exception("Exception occured in createResultZipFile method: " + e.getMessage());
		}
	}


	/**
	 * getHostName() method returns the host name of the machine.
	 * 
	 * @return host name of the machine
	 * @throws Exception 
	 */
	private String getHostName() throws Exception
	{
		String hostName="";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception e) {
			throw new Exception("Exception occured in getHostName method: " + e.getMessage());
		}
		return hostName;
	}

	/**
	 * getDateTime() method returns the date in the csv file.
	 * 
	 * @param path		The path of the csv file
	 * @param datetype	The type of date i.e start / end
	 * 
	 * @return	date	The date in the string format
	 */
	public String getDateTime(String path, String datetype) {
		String endDate = null;
		String startDate = null;
		String date = null;
		try {
			File file1 = new File(path + "SMTUIAutomation_TestSetUpLogs.csv");
			csvTestCase = new CsvReader(file1.getAbsolutePath());
			while (csvTestCase.readRecord()) {
				if(csvTestCase.get(0).equalsIgnoreCase("Start Time")) {
					startDate = csvTestCase.get(1);
					break;
				}
			}
			File file2 = new File(path + "SMTUIAutomation_TestCleanUpLogs.csv");
			csvTestCase = new CsvReader(file2.getAbsolutePath());
			while (csvTestCase.readRecord()) {
				if(csvTestCase.get(0).equalsIgnoreCase("End Time")) {
					endDate = csvTestCase.get(1);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		if (datetype == "start")
			date = startDate;
		else
			date = endDate;

		return date;
	}

	/**
	 * Method to return time taken by test case read from TestTimeLogs.csv
	 * @param path
	 * @param testCase
	 * @return
	 */
	public String getTimeTakenByTestCase(String path, String testCase) {
		CsvReader csvReaderTestCases = null;
		String timeTaken = null;
		try {
			File file1 = new File(path + testCase + "Logs.csv");
			csvReaderTestCases = new CsvReader(file1.getAbsolutePath());
			while (csvReaderTestCases.readRecord()) {
				if(csvReaderTestCases.get(0).equalsIgnoreCase("Execution Time")) {
					timeTaken = csvReaderTestCases.get(1);
					break;
				}
			}
		}catch(Exception exception) {

		}
		csvReaderTestCases.close();
		return timeTaken;
	}


	/**
	 * getTimeDifference() method return the time difference between two date-time.
	 * 
	 * @return	time difference between two date-time.
	 * @throws Exception 
	 */
	public String getTotalExecutionTime(String path) throws Exception
	{
		CommonUI commonUI = new CommonUI();
		String startDateTime = getDateTime(path, "start");
		String endDateTime = getDateTime(path, "end");
		return commonUI.getTimeDifference(startDateTime, endDateTime);
	}
}