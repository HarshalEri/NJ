package com.abfl.automation.utilities;

import java.util.Hashtable;
import java.util.List;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class contains methods to find web element on web page
 * @author Harshal.e
 *
 */
public class ElementController {	
	private String packageAddress = null;
	public Hashtable<String, Hashtable<String, String>> elementParams = new Hashtable<String, Hashtable<String, String>>();
	private WebElement webelement = null;
	private List <WebElement> webelements = null;
	private Select select = null;
	private Alert alert = null;
	
	public ElementController(String xmlFileName) {
		if(xmlFileName != null) {
			XmlReaderWriter xmlReader = new XmlReaderWriter();
			elementParams = xmlReader.getElementParamsByTitle(xmlFileName);						
		}
		packageAddress = this.getClass().getCanonicalName();
		Log4J.setLog4JProperties();
	}
	
	/**
	 * Method to search web element on the page according to the tagName.
	 * @param isDynamicXpath - Boolean flag to check tagName value get from repository or it is dynamic
	 * @param tagName			The title in the xml file
	 * @return	WebElement		The WebElement Object
	 * @throws Exception, TimeoutException 
	 */
	private WebElement searchElement(WebDriver driver, Boolean isDynamicXpath, final String tagName) {
			
		if (true != isDynamicXpath) {
			
			// Find web element by XPath			
			try {
				if(elementParams.get(tagName).get("xpath") != null) {	
					
					return waitForElement(driver, "xpath", elementParams.get(tagName).get("xpath"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + e.getMessage());
			}

			// Find web element by Name
			try {
				if(elementParams.get(tagName).get("name") != null) {
					return waitForElement(driver, "name", elementParams.get(tagName).get("name"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by name: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by name: \n" + e.getMessage());
			}
			
			// Find web element by ID
			try {
				if(elementParams.get(tagName).get("id") != null) {
					return waitForElement(driver, "id", elementParams.get(tagName).get("id"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by id: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by id: \n" + e.getMessage());
			}

			// Find web element by Tag Name
			try {
				if(elementParams.get(tagName).get("tagName") != null) {
					return waitForElement(driver, "tagName", elementParams.get(tagName).get("tagName"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by tagNam: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by tagName: \n" + e.getMessage());
			}
			
			// Find web element by Partial Link
			try {
				if(elementParams.get(tagName).get("partiallink") != null) {
					return waitForElement(driver, "partiallink", elementParams.get(tagName).get("partiallink"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by partiallink: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by partiallink: \n" + e.getMessage());
			}
			
			// Find web element by cssSelector
			try {
				if(elementParams.get(tagName).get("cssSelector") != null) {
					return waitForElement(driver, "cssSelector", elementParams.get(tagName).get("cssSelector"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by partiallink: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by partiallink: \n" + e.getMessage());
			}
		} else {
//			Find the elements with given tag instead of going to the repository Search web element by relative XPath
			// Find web element by XPath
			try {
				return waitForElement(driver, "xpath", tagName);
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + e.getMessage());
			}
		}
		return null;
	}	
	
	/**
	 * searchElements() methods searches web elements on the page according to the tagName.
	 * @param isDynamicXpath - Boolean flag to check tagName value get from repository or it is dynamic
	 * @param tagName			The title in the xml file
	 * @param timeout			The wait time
	 * @return	List<WebElement>	The List of WebElements
	 */
	private List<WebElement> searchElements(WebDriver driver, boolean isDynamicXpath, final String tagName){
		// Find web element by XPath
		if (true != isDynamicXpath) {
			try {
				if(elementParams.get(tagName).get("xpath") != null) {					
					return waitForElements(driver, "xpath", elementParams.get(tagName).get("xpath"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + e.getMessage());
			}
			
			// Find web element by Name
			try {
				if(elementParams.get(tagName).get("name") != null) {
					return waitForElements(driver, "name", elementParams.get(tagName).get("name"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by name: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by name: \n" + e.getMessage());
			}

			// Find web element by ID
			try {
				if(elementParams.get(tagName).get("id") != null) {
					return waitForElements(driver, "id", elementParams.get(tagName).get("id"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by id: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by id: \n" + e.getMessage());
			}
			
			// Find web element by Tag Name
			try {
				if(elementParams.get(tagName).get("tagName") != null) {
					return waitForElements(driver, "tagName", elementParams.get(tagName).get("tagName"));
				}
			}catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by tagname: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by tagname: \n" + e.getMessage());
			}

			// Find web element by Partial Link
			try {
				if(elementParams.get(tagName).get("partiallink") != null) {
					return waitForElements(driver, "partiallink", elementParams.get(tagName).get("partiallink"));
				}
			}catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by partiallink: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by partiallink: \n" + e.getMessage());
			}
		} else {
//			Find the elements with given tag instead of going to the repository Search web element by relative XPath
			
			// Find web element by XPath
			try {
				return waitForElements(driver, "xpath", tagName);
			}catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + e.getMessage());
			}
		}
		return null;
	}		
	
	/**
	 * Method to search child element of the given tagName using the additional information available in the repository file	
	 * @param _webElement		The WebElement Object
	 * @param isDynamicXpath - Boolean flag to check tagName value get from repository or it is dynamic
	 * @param tagName			The title in the xml file
	 * @return	WebElement		The WebElement Object
	 */
	private WebElement searchChildElement(WebDriver driver, final WebElement _webElement, boolean isDynamicXpath, final String tagName){

		if (true != isDynamicXpath) {
			
			// Find web element by XPath
			try {
				if(elementParams.get(tagName).get("xpath") != null) {
					return waitForChildElement(_webElement, "xpath", elementParams.get(tagName).get("xpath"));
				}
			}catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + e.getMessage());
			}

			// Find web element by ID
			try {
				if(elementParams.get(tagName).get("id") != null) {
					return waitForChildElement(_webElement, "id", elementParams.get(tagName).get("id"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by id: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by id: \n" + e.getMessage());
			}

			// Find web element by Tag Name
			try {
				if(elementParams.get(tagName).get("tagName") != null) {
					return waitForChildElement(_webElement, "tagName", elementParams.get(tagName).get("tagName"));
				}
			}catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by tagname: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by tagname: \n" + e.getMessage());
			}

			// Find web element by Partial Link
			try {
				if(elementParams.get(tagName).get("partiallink") != null) {
					return waitForChildElement(_webElement, "partiallink", elementParams.get(tagName).get("partiallink"));
				}
			}catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by partiallink: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by partiallink: \n" + e.getMessage());
			}
		} else {
//			Find the elements with given tag instead of going to the repository Search web element by relative XPath
			
			// Find web element by XPath
			try {
				return waitForChildElement(_webElement, "xpath", tagName);
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * Method  to search child elements of the given tagName using the additional information 
	 * available in the repository file.
	 * @param _webElement		The WebElement Object
	 * @param isDynamicXpath - Boolean flag to check tagName value get from repository or it is dynamic
	 * @param tagName			The title in the xml file
	 * @return	List<WebElement>	The List of WebElement
	 */
	private List<WebElement> searchChildElements(WebDriver driver, final WebElement _webElement, boolean isDynamicXpath, final String tagName){

		// Find web element by XPath
		if (true != isDynamicXpath) {
			try {
				if(elementParams.get(tagName).get("xpath") != null) {
					return waitForChildElements(_webElement, "xpath", elementParams.get(tagName).get("xpath"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + e.getMessage());
			}

			// Find web element by ID
			try {
				if(elementParams.get(tagName).get("id") != null) {
					return waitForChildElements(_webElement, "id", elementParams.get(tagName).get("id"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by id: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by id: \n" + e.getMessage());
			}

			// Find web element by Tag Name
			try {
				if(elementParams.get(tagName).get("tagName") != null) {
					return waitForChildElements(_webElement, "tagName", elementParams.get(tagName).get("tagName"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by tagname: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by tagname: \n" + e.getMessage());
			}

			// Find web element by Partial Link
			try {
				if(elementParams.get(tagName).get("partiallink") != null) {
					return waitForChildElements(_webElement, "partiallink", elementParams.get(tagName).get("partiallink"));
				}
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by partiallink: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by partiallink: \n" + e.getMessage());
			}
		} else  {
//			Find the elements with given tag instead of going to the repository Search web element by relative XPath
			
			// Find web element by XPath
			try {
				return waitForChildElements(_webElement, "xpath", tagName);
			} catch (TimeoutException te) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + te.getMessage());
			} catch (Exception e) {
				Log4J.getlogger(packageAddress).error(tagName + " web element not found by xpath: \n" + e.getMessage());
			}
		}
		return null;
	}	
	
	/**
	 * Method to wait for WebElement to be displayed on the page. 
	 * @param _webElement		The WebElement Object
	 * @param isDynamicXpath - Boolean flag to check tagName value get from repository or it is dynamic
	 * @param tagName			The title in the xml file
	 * @return	WebElement		The WebElement Object
	 * @throws Exception 
	 */
	public WebElement waitForWebElement(WebDriver driver, WebElement _webElement, boolean isDynamicXpath, String tagName) throws Exception {
		long mill = 100;
		int waittime = 0;
		WebElement element = null;
		boolean isElementFound = false;
		try {
			if (null != _webElement) {
				element = searchChildElement(driver, _webElement, isDynamicXpath, tagName);
			} else {
				element = searchElement(driver, isDynamicXpath, tagName);
			}
			// This part will wait till the element is visible on the browser
			if (element != null) {
				while (isElementFound == false && waittime < BrowserDriver.getTimeOutForFindingWebElementInSeconds() * 1000) {
					waittime += mill;					
					if(element.isDisplayed()) {
						break;
					}
					else
					{ Thread.sleep(mill); }
				}
			}
		}  catch (Exception e) {
			Log4J.getlogger(packageAddress).error("Exception occured in waitForWebElement method: " + e.getMessage());
		}
		return element;
	}

	/**
	 * Method to wait for list of WebElements to be load on the page. 
	 * @param _webElement -	The WebElement Object
	 * @param isDynamicXpath - Boolean flag to check tagName value get from repository or it is dynamic
	 * @param tagName - The title in the xml file
	 * @return	List<WebElement>	The list of WebElements
	 * @throws Exception 
	 */
	public List<WebElement> waitForWebElements(WebDriver driver, WebElement _webElement, boolean isDynamicXpath, String tagName) {
		List<WebElement> elements = null;
		try {
			if (null != _webElement) {
				elements = searchChildElements(driver, _webElement, isDynamicXpath, tagName);
			} else {
				elements = searchElements(driver, isDynamicXpath, tagName);
			}
		} catch (Exception e) {
			Log4J.getlogger(packageAddress).error("Error Occured in waitForWebElements function:" + e.getMessage());
		}
		return elements;
	}	
		
	/**
	 * Method to fetches xpath from the repository file and generates a new xpath. 
	 * @param tagName - The title in the xml file
	 * @param value - The value to be replaced in the xpath
	 * @return	String - The new xpath
	 * @throws Exception 
	 */
	public String buildXpathFromRepository(String tagName, String value) {
		try {
			String xpath = elementParams.get(tagName).get("xpath").replace("#", value);
			return xpath;
		} catch (Exception e) {
			Log4J.getlogger(packageAddress).error("Tagname not found in the repository file : " + tagName);
			return null;
		}
	}	
	
	/**
	 * Method to wait implicitly up to specified time
	 * @param : WebDriver type contains the browser driver
	 * @param : long type contains the wait time
	 */	
	public void implicitWait(long timeInSecond) {	
		long mill = (timeInSecond +2) * 1000;
		try {
			Thread.sleep(mill);
		} catch (InterruptedException e) {
			System.out.println("Error Occured:" + e.getMessage());
		}
	}
	
	/**
	 * Method to wait for the page to be load on the browser 
	 * @param driver
	 * @param _webElement
	 * @param isDynamicXpath
	 * @param tagName
	 * @return
	 * @throws Exception
	 */
	public String waitForPageToLoad(WebDriver driver, WebElement _webElement, boolean isDynamicXpath, String tagName) throws Exception {
		String records = waitForWebElement(driver, _webElement, isDynamicXpath, tagName).getText();
		int waittime = 0;
		while ((records == null || records.trim().equals("") || records.trim().length() == 0) && waittime < 30000) {
			waittime += 1000;
			implicitWait(10);
			records = waitForWebElement(driver, _webElement, isDynamicXpath, tagName).getText();
		}
		if (records == null || records.trim().equals("") || records.trim().length() == 0) {
			throw new Exception("Exception in waitForPageToLoad function: records string is empty / null");
		}
		return records;
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public String waitForPageToLoad(WebElement element) throws Exception{
		String records = element.getText();
		int waittime = 0;
		while ((records == null || records.trim().equals("") || records.trim().length() == 0) && waittime < 30000) {
			waittime += 1000;
			implicitWait(10);
			records = element.getText();
		}
		if (records == null || records.trim().equals("") || records.trim().length() == 0) {
			throw new Exception("Exception in waitForPageToLoad function: records string is empty / null");
		}
		return records;
	}
	
	/**
	 * Waits for the element to be load, enable or visible
	 * @param elementType id / xpath / linktext / partiallinktext / name
	 * @param elementValue Value of id / xpath / linktext / partiallinktext / name
	 * @return boolean
	 */
	 public WebElement waitForElement(WebDriver driver, String elementType, String elementValue) 
     {
		 WebElement element = null;
         int count = 0;
         do {	
        	 try {                        	  	
                 switch (elementType.toLowerCase()) {
                 
                     case "xpath": element = driver.findElement(By.xpath(elementValue));
                         break;
                     case "id": element = driver.findElement(By.id(elementValue));
                         break;
                     case "name": element = driver.findElement(By.name(elementValue));
                         break;
                     case "classname": element = driver.findElement(By.className(elementValue));
                         break;
                     case "linktext": element = driver.findElement(By.linkText(elementValue));
                         break;
                     case "partiallink": element = driver.findElement(By.partialLinkText(elementValue));
                         break;
                     case "cssselector": element = driver.findElement(By.cssSelector(elementValue));
                         break;
                     case "tagname": element = driver.findElement(By.tagName(elementValue));
                     break;
                 }
                 // Verify if element present or not
                 if (null != element) 
                         return element;
                 else {
                	 count++;
	                 Thread.sleep(500); 
                 }
        	 }catch (Exception e) {
	        	 try {
	    			 count++;
	                 Thread.sleep(500);
	    		 }catch(Exception e1){
	    			 System.out.print("ERROR : "+e1);
	    		 }                     	
             }
         }
         while (count < BrowserDriver.getTimeOutForFindingWebElementInSeconds());
         System.out.println("Timout: "+BrowserDriver.getTimeOutForFindingWebElementInSeconds());
         return element;
     }
	 
	 /**
	 * Waits for the element to be load, enable or visible
	 * @param elementType id / xpath / linktext / partiallinktext / name
	 * @param elementValue Value of id / xpath / linktext / partiallinktext / name
	 * @return boolean
	 */
	 private WebElement waitForChildElement(WebElement webElement, String elementType, String elementValue) 
     {
		 WebElement element = null;	 
         int count = 0;
         do {	
        	 try {                	        	  	
                 switch (elementType.toLowerCase()) {
                 
                     case "xpath": element = webElement.findElement(By.xpath(elementValue));
                         break;
                     case "id": element = webElement.findElement(By.id(elementValue));
                         break;
                     case "name": element = webElement.findElement(By.name(elementValue));
                         break;
                     case "classname": element = webElement.findElement(By.className(elementValue));
                         break;
                     case "linktext": element = webElement.findElement(By.linkText(elementValue));
                         break;
                     case "partiallink": element = webElement.findElement(By.partialLinkText(elementValue));
                         break;
                     case "cssselector": element = webElement.findElement(By.cssSelector(elementValue));
                         break;
                     case "tagname": element = webElement.findElement(By.tagName(elementValue));
                     break;
                 }
                 // Verify if element present or not
                 if (null != element)
                         return element;
                 else {
                	 count++;
	                 Thread.sleep(500); 
                 }
        	 }catch (Exception e) {
        		 try {
        			 count++;
                     Thread.sleep(500);
        		 }catch(Exception e1){ }
             }
         } while (count < BrowserDriver.getTimeOutForFindingWebElementInSeconds());         
         return element;
     }
	 
	 /**
	 * Waits for the element list to be load, enable or visible
	 * @param elementType id / xpath / linktext / partiallinktext / name
	 * @param elementValue Value of id / xpath / linktext / partiallinktext / name
	 * @return boolean
	 */
	 private List<WebElement> waitForElements(WebDriver driver, String elementType, String elementValue)
     {
		 List <WebElement> elements = null;
		 try {				 
	         int count = 0;
	         do {
	        	 try {		                		        	  
 	                 switch (elementType.toLowerCase()) {
	                 
	                     case "xpath": elements = driver.findElements(By.xpath(elementValue));
	                         break;
	                     case "id": elements = driver.findElements(By.id(elementValue));
	                         break;
	                     case "name": 
	                    	 	elements =driver.findElements(By.name(elementValue));
	                         break;
	                     case "classname": elements = driver.findElements(By.className(elementValue));
	                         break;
	                     case "linktext": elements = driver.findElements(By.linkText(elementValue));
	                     	break;
	                     case "partiallink": elements = driver.findElements(By.partialLinkText(elementValue));
	                    	break;
	                     case "tagname": elements = driver.findElements(By.tagName(elementValue));
	                    	break;
	                 }
	                 // Verify if element present or not
	                 if (null != elements) 		                     
	                         return elements;		                     
	                 else {
	                	 count++;
				         Thread.sleep(500);	
	                 }
	         	} catch(Exception e){
	         		count++;
		            Thread.sleep(500);	
	         	}
	         } while (count < BrowserDriver.getTimeOutForFindingWebElementInSeconds());
         
         }catch (Exception e) {}
        return elements;
     }
		 		 
	 /**
	 * Waits for the element list to be load, enable or visible
	 * @param elementType id / xpath / linktext / partiallinktext / name
	 * @param elementValue Value of id / xpath / linktext / partiallinktext / name
	 * @return boolean
	 */
	 private List<WebElement> waitForChildElements(WebElement webElement, String elementType, String elementValue)
     {
		 List <WebElement> elements = null;
		 try {	 
	         int count = 0;
	         do {	   
	        	 try {
	                 switch (elementType.toLowerCase()) {
	                 
	                     case "xpath": elements = webElement.findElements(By.xpath(elementValue));
	                         break;
	                     case "id": elements = webElement.findElements(By.id(elementValue));
	                         break;
	                     case "name": 
	                    	 	elements =webElement.findElements(By.name(elementValue));
	                         break;
	                     case "classname": elements = webElement.findElements(By.className(elementValue));
	                         break;
	                     case "linktext": elements = webElement.findElements(By.linkText(elementValue));
	                     	break;
	                     case "partiallink": elements = webElement.findElements(By.partialLinkText(elementValue));
	                    	break;
	                     case "tagname": elements = webElement.findElements(By.tagName(elementValue));
	                    	break;
	                 }
	                 // Verify if element present or not
	                 if (null != elements) 		                    
	                         return elements;
                     else {
	                	 count++;
				         Thread.sleep(500);	
	                 }
	         	}
	         	catch(Exception e){
		             count++;
			         Thread.sleep(500);
	         	}
	         } while (count < BrowserDriver.getTimeOutForFindingWebElementInSeconds());		         
         }catch (Exception e) { }
        return elements;
     }

	public WebElement getWebelement() {
		return webelement;
	}

	public void setWebelement(WebElement webelement) {
		this.webelement = webelement;
	}

	public List <WebElement> getWebelements() {
		return webelements;
	}

	public void setWebelements(List <WebElement> webelements) {
		this.webelements = webelements;
	}

	public Select getSelect() {
		return select;
	}

	public void setSelect(Select select) {
		this.select = select;
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}
	
	public String waitForPageToLoadWithTitle(WebDriver driver,String tagName){
		WebElement element = (new WebDriverWait(driver, 10))
				   .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementParams.get(tagName).get("xpath"))));
		return element.getText();
	}
}