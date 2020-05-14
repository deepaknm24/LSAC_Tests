package com.deesite.reportmanager;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


public class ExtentManager {
	static Logger logger = LoggerFactory.getLogger(ExtentManager.class);
	private static ExtentHtmlReporter htmlReporter;
	private static ExtentReports extent;
	private static ExtentTest extentChildTest;
	private static String extentReportPath = "./extentReport.html";

	public static ExtentHtmlReporter getHtmlReporter() {
		htmlReporter = new ExtentHtmlReporter(extentReportPath);
		/*
		 * htmlReporter.config().setChartVisibilityOnOpen(true);
		 * htmlReporter.config().setDocumentTitle("IdentHQ Automation Report");
		 * htmlReporter.config().setReportName("Automation");
		 * htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		 */
		//htmlReporter.setAppendExisting(false);
		htmlReporter.loadXMLConfig(System.getProperty("user.dir") + File.separator + "src" + File.separator
				+ File.separator + "test" + File.separator + "resources" + File.separator + "extentConfig"
				+ File.separator + "ReportsConfig.xml");// We can set this path in @BefoureSuit also.

		return htmlReporter;
	}

	public static ExtentReports getExtentReports() {
		if (extent != null) {
			logger.info("			 Using Extent Report			");
			return extent;
		} else {
			
			logger.info("			 Initializing Extent Report			");
			
			extent = new ExtentReports();
			extent.attachReporter(getHtmlReporter());
			extent.setSystemInfo("Host", "Dee");
			extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
			return extent;
		}

	}

}
