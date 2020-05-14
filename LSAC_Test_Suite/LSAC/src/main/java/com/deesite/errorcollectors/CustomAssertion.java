package com.deesite.errorcollectors;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.deesite.base.BaseTest;
import com.deesite.utilities.TestUtils;


public class CustomAssertion extends SoftAssert {
	static Logger logger = LoggerFactory.getLogger(Class.class.getName());
	public static String passMessage = "";
	public static String failMessage = ""; 
	
	@Override
	public void onAssertSuccess(IAssert<?> assertCommand) {
		
	}
	
	@Override
	public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
		try {
			TestUtils.captureScreenshot();
			String exceptionMessage = "" ;
			exceptionMessage = exceptionMessage + " " + ex.getLocalizedMessage();
			
			
			BaseTest.test.get().log(Status.FAIL,
					"<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured:Click to see"
							+ "</font>" + "</b >" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>");
			
			logger.info(failMessage);
				
		
		
		
			exceptionMessage = exceptionMessage+	" - <a href=\"" + TestUtils.screenshotPath + TestUtils.screenshotName + "\"" + "target=\"_blank\">Screenshot</a>";
		
		
			/*TestSetup.test.get().fail("<b>" + "<font color=" + "red>" + "ScreenShot of failure" + "</font>" + "</b>",
					MediaEntityBuilder.createScreenCaptureFromPath(TestUtils.screenshotPath + TestUtils.screenshotName)
					.build());*/
		}
		 catch (Exception e) {
			// TODO: handle exception
		}
		
		/*String text = "This Test case got failed";
		Markup m = MarkupHelper.createLabel(text, ExtentColor.RED);
		TestSetup.test.get().log(Status.FAIL, m);*/
		
		
		
		
		
	}

	@Override
	protected void doAssert(IAssert<?> a) {
		onBeforeAssert(a);
		try {
			a.doAssert();
			onAssertSuccess(a);
		} catch (AssertionError ex) {
			onAssertFailure(a, ex);
			BaseTest.m_errors.put(ex, a);
		} finally {
			onAfterAssert(a);
		}
	}

	public void assertAll() {
		if (!BaseTest.m_errors.isEmpty()) {
			StringBuilder sb = new StringBuilder("The following asserts failed:");
			boolean first = true;
			for (Map.Entry<AssertionError, IAssert<?>> ae : BaseTest.m_errors.entrySet()) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append("\n\t");
				sb.append(ae.getKey().getMessage());
			}
			throw new AssertionError(sb.toString());
		}
	}
	
	public void assertTrue(boolean condition, String fMessage, String pMessage) {
		passMessage = pMessage;
		failMessage = fMessage;
		assertTrue(condition);
	}
	
	public void assertEquals(String actual, String expected, String fMessage, String pMessage) {
		passMessage = pMessage;
		failMessage = fMessage;
		assertEquals(actual, expected);
	}
	
	public void assertFalse(boolean condition, String fMessage, String pMessage) {
		passMessage = pMessage;
		failMessage = fMessage;
		assertFalse(condition);
	}
}