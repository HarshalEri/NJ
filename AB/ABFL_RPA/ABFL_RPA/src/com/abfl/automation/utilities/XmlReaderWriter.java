package com.abfl.automation.utilities;

import java.io.File;
import org.w3c.dom.*;
import java.util.*;

import javax.xml.parsers.*;

/**
 * Class contains methods to read / write xml files / element repository files
 * @author Harshal.e
 *
 */
public class XmlReaderWriter {
	
	TestDataController testDataController = null;
	private String canonicalPath = null;
	private String packageAddress = this.getClass().getCanonicalName();
	
	public XmlReaderWriter(){
		testDataController = new TestDataController();
		canonicalPath = ".\\ElementRepository\\";
		Log4J.setLog4JProperties();		
	}

	/**
	 * getElementParamsByTitle() method reads the xml file line by line and
	 * stores the element as a key-value pair in the hashtable.
	 * 
	 * @param absoluteFilePath		The path of the xml file
	 * @param tagName				The title in the xml file
	 * 
	 * @return elementParams Object of Hashtable
	 */
	public Hashtable<String, Hashtable<String, String>> getElementParamsByTitle(String xmlFileName) {
		Hashtable<String, Hashtable<String, String>> elementParams = new Hashtable<String, Hashtable<String, String>>();
		try {
			File file = new File(canonicalPath + xmlFileName + ".xml");
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = documentBuilder.parse(file);
			NodeList nodeList = doc.getElementsByTagName("Element");
			for (int node = 0; node < nodeList.getLength(); node++) {
				Hashtable<String, String> childElements = new Hashtable<String, String>();
				Element param = (Element) nodeList.item(node);
				if (!param.getAttribute("title").toString().equals("")) {
					for (Node childNode = param.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
						if (childNode.getNodeType() == Node.ELEMENT_NODE) {
							childElements.put(childNode.getNodeName(),childNode.getTextContent());
						}
					}
					elementParams.put(param.getAttribute("title").toString(), childElements);
				}
			}
		} catch (Exception e) {
			Log4J.getlogger(packageAddress).error(e.getMessage());			
		}
		return elementParams;
	}
}