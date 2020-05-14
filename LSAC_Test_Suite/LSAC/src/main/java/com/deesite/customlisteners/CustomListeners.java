package com.deesite.customlisteners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.deesite.base.BaseTest;
import com.deesite.utilities.TestUtils;

public class CustomListeners extends BaseTest implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		String description = result.getMethod().getDescription();
		ExtentTest childTest = parentTest.get().createNode(methodName);
		test.set(childTest);
		test.get().info("<b> <font color=" + "blue>" + "Description: -  </b>" + description + "</font>");
		test.get().info("<b>" + "Starting Test: - " + methodName + "</b>");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String methodName=result.getMethod().getMethodName();
		String logText="<b>"+"Test Case:- "+ methodName+ " Passed"+"</b>";		
		Markup m=MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		test.get().pass(m);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		TestUtils.captureScreenshot();
		String exceptionMessage = "" ;
		exceptionMessage = exceptionMessage + " " + result.getThrowable().getCause();
		test.get().log(Status.FAIL,
				"<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured:Click to see"
						+ "</font>" + "</b >" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>");
		
		try {
			test.get().fail("<b>" + "<font color=" + "red>" + "ScreenShot of failure" + "</font>" + "</b>",
					MediaEntityBuilder.createScreenCaptureFromPath(TestUtils.screenshotPath + TestUtils.screenshotName)
					.build());
		} catch (Exception e) {
		}
		
		String text = "This Test case got failed";
		Markup m = MarkupHelper.createLabel(text, ExtentColor.RED);
		test.get().log(Status.FAIL, m);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String text = "This Test case got Skipped";
		Markup m = MarkupHelper.createLabel(text, ExtentColor.AMBER);
		test.get().log(Status.SKIP, m);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override
	public void onStart(ITestContext context) {

	}

	@Override
	public void onFinish(ITestContext context) {

	}

}
