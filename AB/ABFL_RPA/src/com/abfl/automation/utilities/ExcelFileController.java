package com.abfl.automation.utilities;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileController {

	public static XSSFSheet readXlsxFile(String fileName) {
		try {
			XSSFWorkbook wb = ExcelFileController.readFile(".\\TransactionalDocs\\" + fileName + ".xlsx");
			return wb.getSheetAt(0);

		} catch (Exception ex) {
			System.out.println("Test " + ex.getMessage());
		}
		return null;
	}
			
	/**
	 * creates an {@link HSSFWorkbook} with the specified OS filename.
	 */
	private static XSSFWorkbook readFile(String filename) throws IOException {
	    FileInputStream fis = new FileInputStream(filename);
	    try {
	        return new XSSFWorkbook(fis);
	    } catch (Throwable ex){
	    	System.out.println("");
	    }
	    finally {
	        fis.close();
	    }
		return null;
	}
}
