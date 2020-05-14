package com.deesite.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.deesite.excelManager.ExcelReader;

public class DynamicTestNG {
	ExcelReader excel = new ExcelReader( System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testdata" + File.separator + "TestData.xlsx");
	String sheetName = "TestConfig";
	int numOfRows;

	public void runTestNGTest(Map<String, String> testngParams) {
		Logger logger = LoggerFactory.getLogger(Class.class.getName());

		TestNG myTestNG = new TestNG();
		List<Class<? extends ITestNGListener>> listenerClasses = new ArrayList<>();

		XmlSuite mySuite = new XmlSuite();
		mySuite.setName("MySuite");

		ArrayList<String> listeners = new ArrayList<String>();
		listeners.add("com.deesite.customlisteners.CustomListeners");
		mySuite.setListeners(listeners);

		XmlTest myTest = new XmlTest(mySuite);
		myTest.setName("MyTest");
		myTest.setPreserveOrder(true);

		myTest.setParameters(testngParams);

		// Create a list which can contain the classes that you want to run.
		List<XmlClass> myClasses = new ArrayList<>();
		int dataEndRow = excel.getRowCount(sheetName, 0, "EndOfRows");
		String platform = System.getProperty("platform");
		for (int rowNum = 3; rowNum <= dataEndRow; rowNum++) {
			String className = excel.getCellData(sheetName, 1, rowNum);
			String classRunMode = excel.getCellData(sheetName, 2, rowNum);
			if (classRunMode.equalsIgnoreCase("Y")) {
				XmlClass testClass = new XmlClass();
				switch (testngParams.get("platform")) {
				case "web":
					testClass.setName("com.deesite.tests." + className);
					break;
				case "android_web":
					testClass.setName("com.deesite.tests." + className);
					break;
				case "android":
					testClass.setName("com.deesite.tests." + className);
					break;
				default:
					testClass.setName("com.deesite.tests." + className);
				}
				List<XmlInclude> methodsToRun = constructIncludes(className);
				testClass.setIncludedMethods(methodsToRun);
				myClasses.add(testClass);
			}
		}

		myTest.setXmlClasses(myClasses);

		List<XmlTest> myTests = new ArrayList<>();
		myTests.add(myTest);
		mySuite.setTests(myTests);

		List<XmlSuite> mySuites = new ArrayList<>();
		mySuites.add(mySuite);
		myTestNG.setXmlSuites(mySuites);
		myTestNG.setListenerClasses(listenerClasses);
		mySuite.setFileName("myTemp.xml");
		mySuite.setThreadCount(3);

		logger.info(mySuite.toXml());
		myTestNG.run();

		for (XmlSuite suite : mySuites) {
			createXmlFile(suite);
		}

		Map<String, String> params = myTest.getAllParameters();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			System.out.println(entry.getKey() + " => " + entry.getValue());
		}
	}

	public int startRow(String sheetName, int colNum, String testCase) {
		int testcaserownumber = 0;
		int totalrow = excel.getRowCount(sheetName);

		for (int i = 0; i < totalrow; i++) {
			if (excel.getCellData(sheetName, colNum, i).contains(testCase)) {
				testcaserownumber = i;
				break;
			}
		}

		return testcaserownumber;
	}

	public String[] getTestCasesOfSpecificClass(String className) {
		String[] testCasesWithRunModeasY = new String[numOfRows];
		int startingRow = startRow(sheetName, 0, className);
		String nextClassName;
		String testCaseName;
		String testCaseRunMode;
		int counter = 0;

		for (int i = startingRow; i <= numOfRows; i++) {
			nextClassName = excel.getCellData(sheetName, 1, i);
			testCaseName = excel.getCellData(sheetName, 4, i);
			testCaseRunMode = excel.getCellData(sheetName, 5, i);

			if (nextClassName.length() > 0 && !nextClassName.equalsIgnoreCase(className)) {
				break;
			}

			if ((nextClassName == "" || nextClassName.equalsIgnoreCase(className))
					&& testCaseRunMode.equalsIgnoreCase("Y")) {
				testCasesWithRunModeasY[counter] = testCaseName;
				System.out.println(className + "	-	" + testCaseName);
				counter++;
			}

		}
		String[] testCasesForXml = new String[counter];

		for (int i = 0; i < counter; i++) {
			testCasesForXml[i] = testCasesWithRunModeasY[i];
		}

		return testCasesForXml;
	}

	public List<XmlInclude> constructIncludes(String className) {
		int dataEndRow = excel.getRowCount(sheetName, 0, "EndOfRows");
		int dataStartRow = startRow(sheetName, 1, className);
		// int dataStartRow = excel.getRowCount(sheetName);
		List<XmlInclude> includes = new ArrayList<>();
		for (int rowNum = dataStartRow; rowNum <= dataEndRow; rowNum++) {
			String testClassName = excel.getCellData(sheetName, 1, rowNum);
			String testCaseName = excel.getCellData(sheetName, 4, rowNum);
			String testCaseRunMode = excel.getCellData(sheetName, 5, rowNum);

			if (testClassName.length() > 0 && !testClassName.equalsIgnoreCase(className)) {
				break;
			} else if ((testClassName == "" || testClassName.equalsIgnoreCase(className))
					&& testCaseRunMode.equalsIgnoreCase("Y")) {
				includes.add(new XmlInclude(testCaseName));
				System.out.println(className + "	-	" + testCaseName);
			}
		}
		return includes;
	}

	// This method will create an Xml file based on the XmlSuite data
	public void createXmlFile(XmlSuite mSuite) {
		FileWriter writer;
		try {
			writer = new FileWriter(new File("myTemp.xml"));
			writer.write(mSuite.toXml());
			writer.flush();
			writer.close();
			System.out.println(new File("myTemp.xml").getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Main Method
	public static void main(String args[]) {
		DynamicTestNG dtestNg = new DynamicTestNG();

		// This Map can hold your testng Parameters.
		Map<String, String> testngParams = new HashMap<>();
		testngParams.put("device1", "Desktop");
		testngParams.put("device2", "Android");
		testngParams.put("device3", "iOS");
		dtestNg.runTestNGTest(testngParams);
	}

}
