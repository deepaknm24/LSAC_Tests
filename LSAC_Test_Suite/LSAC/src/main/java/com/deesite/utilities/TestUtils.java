package com.deesite.utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.DataProvider;

import com.deesite.base.BaseTest;

public class TestUtils extends BaseTest {
	public static String screenshotPath;
	public static String screenshotName;

	public static void captureScreenshot() {
		screenshotPath = "./screenshots/"; 
		Date d = new Date();
		screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".png";

		TakesScreenshot ts = AppiumDriverManager.getDriver();
		File srcFile = ts.getScreenshotAs(OutputType.FILE);
		File destFile = new File(screenshotPath + screenshotName);
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			System.out.println(e.fillInStackTrace());
		}
	}

	@DataProvider(name = "dp")
	public Object[][] getData(Method method) {
		String sheetName = "TestData";
		int rowCount = excel.getRowCount(sheetName);
		int colCount = excel.getColumnCount(sheetName);
		int dataSetWithRunMode = getDataSetWithRunMode();
		Object[][] data = new Object[dataSetWithRunMode][1];
		Hashtable<String, String> table = null;
		int i=0;
		for (int rowNum = 2; rowNum <= rowCount; rowNum++) {
			if (excel.getCellData(sheetName, 0, rowNum).equalsIgnoreCase("Y")) {

				table = new Hashtable<String, String>();
				for (int colNum = 0; colNum < colCount; colNum++) {
					table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));
				}
				data[i][0] = table;
				i++;
			}
		}
		return data;
	}
	
	
	@DataProvider(name="dpss")
	public Object[][] dataFromSingleSheet(Class c, Method m)
	{
		String sheetName = "TestData";
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);
		System.out.println("Rows: "+ rows);
		System.out.println("Cols: "+ cols);
		
		int testNameRow = 1;
		for(;testNameRow<=rows;testNameRow++)
		{
			String testName = excel.getCellData(sheetName, 0, testNameRow);
			if(testName.equals(c.getSimpleName()))
			{
				break;
			}
		}
		
		// Checking total rows in test case
		int testHeaderRow = testNameRow+1;
		int testDataStartRow = testNameRow+2;
		int counter = 0;
		int testRows = 0;
		while(!excel.getCellData(sheetName, 0, testDataStartRow+counter).equals(""))
		{
			if(excel.getCellData(sheetName, 0, testDataStartRow+counter).equalsIgnoreCase("Y")) {
				testRows++;	
			}
			counter++;
		}
		System.out.println("Total rows of data are : " + testRows);
		
		// Checking total cols in test case
		int testHeaderCol = testNameRow + 1;
		int testCols = 0;
		while (!excel.getCellData(sheetName, testCols, testHeaderCol).equals("")) 
		{
			testCols++;
		}
		System.out.println("Total cols are : " + testCols);
		Object[][] data = new Object[testRows][1];
		Hashtable<String, String> hashTable = null;
		int i=0;
		for(int testDataRow=testDataStartRow;testDataRow<(testDataStartRow+testRows);testDataRow++)
		{
			System.out.println("Row Num: "+testDataRow);
			hashTable = new Hashtable<String, String>();
			for(int colNum=0; colNum<testCols;colNum++)
				{
					String testData = excel.getCellData(sheetName, colNum, testDataRow);
					String testHeader = excel.getCellData(sheetName, colNum, testHeaderRow);
					System.out.println("Col Num: "+colNum +" - "+ testHeader + " - " + testData);
					hashTable.put(testHeader, testData);
				}
				data[i][0] = hashTable;
				i++;
			}
			return data;
	}
	
	public int getDataSetWithRunMode() {
		String sheetName = "TestData";
		int rowCount = excel.getRowCount(sheetName);
		int colCount = excel.getColumnCount(sheetName);
		int dataSetWithRunMode = 0;
		for(int rowNum = 2;rowNum<= rowCount; rowNum++) {
			if(excel.getCellData(sheetName, 0, rowNum).equalsIgnoreCase("Y")) {
				dataSetWithRunMode++;
			}
		}
		return dataSetWithRunMode;
	}

}
