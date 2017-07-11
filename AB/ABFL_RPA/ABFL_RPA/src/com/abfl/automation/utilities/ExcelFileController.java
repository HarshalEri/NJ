package com.abfl.automation.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelFileController {

	
	public static void readXlsxFile(String fileName){
		try {
			File myFile = new File(".\\TransactionalDocs\\"+fileName+".xlsx");
//			FileInputStream fis = new FileInputStream(myFile);

			// Get the workbook instance for XLSX file
	        Workbook wb = new XSSFWorkbook(new FileInputStream(myFile));

	        // Get first sheet from the workbook
	        XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0);

	        Row row;
	        Cell cell;

	        // Iterate through each rows from first sheet
	        Iterator<Row> rowIterator = sheet.iterator();

	        while (rowIterator.hasNext()) 
	        {
	                row = rowIterator.next();

	                // For each row, iterate through each columns
	                Iterator<Cell> cellIterator = row.cellIterator();
	                
	                while (cellIterator.hasNext()) 
	                {
	                cell = cellIterator.next();

	                switch (cell.getCellType()) 
	                {

	                case Cell.CELL_TYPE_BOOLEAN:
	                        System.out.println(cell.getBooleanCellValue());
	                        break;

	                case Cell.CELL_TYPE_NUMERIC:
	                        System.out.println(cell.getNumericCellValue());
	                        break;

	                case Cell.CELL_TYPE_STRING:
	                        System.out.println(cell.getStringCellValue());
	                        break;

	                case Cell.CELL_TYPE_BLANK:
	                        System.out.println(" ");
	                        break;

	                default:
	                        System.out.println(cell);

	                }
	                }
	        }			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
