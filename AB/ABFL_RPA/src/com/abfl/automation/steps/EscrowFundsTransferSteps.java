package com.abfl.automation.steps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.abfl.automation.utilities.ExcelFileController;

public class EscrowFundsTransferSteps {

	public String getColumnValuesFromSheetFromEmail(String columnName ) {
		try {
			XSSFSheet mailContents = ExcelFileController.readXlsxFile("Escrow_Instructions_format");
	
			// Return Account Number
			return mailContents.getRow(1).getCell(this.getColumnNumber(mailContents,columnName)).toString();
			
		}catch(Exception exception){
			
		}
		return null;
	}
	
	public int getColumnNumber(XSSFSheet sheet,String columnName) {
		try {
			
			int totalRowCount = sheet.getPhysicalNumberOfRows();
			for (int currentRowNum = 0; currentRowNum < totalRowCount; currentRowNum++) {
				XSSFRow currentRow = sheet.getRow(currentRowNum);
				if (currentRow == null) continue;

				// Get Column cell number of column
				for (int cellNum = 0; cellNum < currentRow.getLastCellNum(); cellNum++){
					XSSFCell currentColumn = currentRow.getCell(cellNum); //
					if(currentColumn.getStringCellValue().toString().equalsIgnoreCase(columnName)){
						return cellNum;
					}
				}
			}
			
		}catch(Exception exception){
			
		}
		return 0;
	}

	public boolean isAccountNumberPresentInMasetSheet(String accNumber) {
		XSSFSheet masterData = ExcelFileController.readXlsxFile("Master Escrow");
		int columnNo = this.getColumnNumber(masterData, "EscrowAccount No");

		int totalRowCount = masterData.getPhysicalNumberOfRows();
		for (int currentRowNum = 0; currentRowNum < totalRowCount; currentRowNum++) {
			XSSFRow currentRow = masterData.getRow(currentRowNum);
			if (currentRow == null) continue;
		
			XSSFCell currentColumn = currentRow.getCell(columnNo); //Escrow Ac no
			if(currentColumn.getStringCellValue().toString().equalsIgnoreCase(accNumber)){
				return true;
			}
		}
		return false;
	}
	
	public boolean searchForBeneficiaryAccNumberInBeneficiaryMasterSheet(String beneficiaryAccNumber) {
		try {
			XSSFSheet benifiaryExcelMaster = ExcelFileController.readXlsxFile("BenifiaryExcelMaster");
			int columnNo = this.getColumnNumber(benifiaryExcelMaster, "BENEF_ACCT_NMBR");
			
			int totalRowCount = benifiaryExcelMaster.getPhysicalNumberOfRows();
			for (int currentRowNum = 0; currentRowNum < totalRowCount; currentRowNum++) {
				XSSFRow currentRow = benifiaryExcelMaster.getRow(currentRowNum);
				if (currentRow == null) continue;
			
				XSSFCell currentColumn = currentRow.getCell(columnNo); //Escrow Ac no 
				if(currentColumn.getStringCellValue().toString().equalsIgnoreCase(beneficiaryAccNumber)){
					return true;
				}
			}
			return false;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	} 
	
	
	public String getColumnValuesFromSheetFromBeneficiaryMasterSheet(String columnName ) {
		try {
			XSSFSheet beneficiaryMasterSheet = ExcelFileController.readXlsxFile("BenifiaryExcelMaster");
	
			// Return value
			return beneficiaryMasterSheet.getRow(1).getCell(this.getColumnNumber(beneficiaryMasterSheet,columnName)).toString();
			
		}catch(Exception exception){
			
		}
		return null;
	}

	public void prepareUploadFileFormat(String beneficiaryCode, String filePath) {
		try {
			/*BufferedReader br = new BufferedReader(new FileReader(filePath));
			try {
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			 //String everything = sb.toString();
			} finally {
			    br.close();
			}*/
		} catch (Exception exception) {
			
		}
		
	}
}
