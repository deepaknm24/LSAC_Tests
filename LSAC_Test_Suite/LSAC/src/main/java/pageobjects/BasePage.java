package pageobjects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.deesite.base.BaseTest;
import com.deesite.utilities.TestUtils;

import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.HidesKeyboard;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public abstract class BasePage<T> extends BaseTest {

	public T openPage(Class<T> pageClass) {
		T page = null;
		AjaxElementLocatorFactory ajaxElementLocatorFactory = new AjaxElementLocatorFactory(driver, 20);
		try {
			page = PageFactory.initElements(driver, pageClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PageFactory.initElements(ajaxElementLocatorFactory, page);

		ExpectedCondition pageLoadCondition = ((BasePage) page).getPageLoadCondition();
		waitForPageLoad(pageLoadCondition);
		return page;
	}

	public abstract ExpectedCondition getPageLoadCondition();

	static Logger logger = LoggerFactory.getLogger(Class.class.getName());

	public void waitForPageLoad(ExpectedCondition expectedCondition) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(expectedCondition);
	}

	public void type(WebElement element, String value, String nameOfElement) {

		try {
			if (isElementVisible(element)) {
				element.sendKeys(value);
				hideKeyboard();
				reportInfo(logger, String.format("Entered <b> %s </b> as %s", value, nameOfElement));
			} else {
				int i = 0;
				do {
					swipe(driver, DIRECTION.UP, 500);
					i++;
				}

				while (i < scrollCounter
						&& !isElementVisible(element)); /*
														 * { swipe(driver, DIRECTION.UP, 500); i++; }
														 */
				element.sendKeys(value);
				hideKeyboard();
				reportInfo(logger, String.format("Entered <b> %s </b> as %s", value, nameOfElement));

			}

		} catch (Exception e) {
			logger.info("Exception occured while dealing with " + nameOfElement, e);
			throw e;
		}
	}

	public int verifyListOfElements(List<WebElement> elements, String value, String nameOfElement) {
		int i = 0;
		try {
			if (elements.isEmpty()) {
				reportInfo(logger, String.format("No results found for given %s: %s", nameOfElement, value));
			} else if (elements.size() == 1) {
				reportInfo(logger, String.format("One results found for given %s: %s", nameOfElement, value));
				i = 1;
			} else {
				reportInfo(logger, String.format("More than one results found for given %s: %s", nameOfElement, value));
				i++;
			}

		} catch (

		Exception e) {
			logger.info("Exception occured while dealing with list of " + nameOfElement, e);
			throw e;
		}
		return i;
	}

	public void hideKeyboard() {
		if (driver instanceof AndroidDriver) {
			if (((HasOnScreenKeyboard) driver).isKeyboardShown()) {
				((HidesKeyboard) driver).hideKeyboard();
			}
		}
	}

	static int scrollCounter = 1;

	public void click(WebElement element, String nameOfElement) {

		try {
			if (isElementVisible(element)) {
				element.click();
				reportInfo(logger, String.format("Clicked on <b> %s </b>", nameOfElement));
			} else {
				int i = 0;
				while (!isElementVisible(element) && i < scrollCounter) {
					swipe(driver, DIRECTION.UP, 500);
					i++;
				}
				element.click();
				reportInfo(logger, String.format("Clicked on <b> %s </b>", nameOfElement));
			}

		} catch (Exception e) {
			logger.info("Exception occured while dealing with " + nameOfElement, e);
			throw e;
		}
	}

	public void clickFromList(List<WebElement> element, int index, String nameOfElement) {

		try {
			if (isElementVisible(element.get(index))) {
				element.get(index).click();
				reportInfo(logger, String.format("Clicked on <b> %s </b>", nameOfElement));
			} else {
				int i = 0;
				while (!isElementVisible(element.get(index)) && i < scrollCounter) {
					swipe(driver, DIRECTION.UP, 500);
					i++;
				}
				element.get(index).click();
				reportInfo(logger, String.format("Clicked on <b> %s </b>", nameOfElement));
			}

		} catch (Exception e) {
			logger.info("Exception occured while dealing with " + nameOfElement + "in the list", e);
			throw e;
		}
	}

	public boolean isElementVisible(WebElement element) {
		try {
			if (element.isDisplayed()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean isElementFound(WebElement element, String nameOfElement) {
		boolean flag = false;
		try {
			if (isElementVisible(element)) {
				logger.info("<b> {} </b> field is displayed.", nameOfElement);
				test.get().log(Status.INFO, "<b>" + nameOfElement + "</b>" + " field is displayed.");
				flag = true;
			} else {
				int i = 0;
				while (!isElementVisible(element) && i < scrollCounter) {
					swipe(driver, DIRECTION.UP, 500);
					i++;
				}
				element.isDisplayed();
				logger.info("<b> {} </b> field is displayed.", nameOfElement);
				test.get().log(Status.INFO, "<b>" + nameOfElement + "</b>" + " field is displayed.");
				flag = true;
			}

		} catch (Exception e) {
			logger.info("Exception occured while dealing with " + nameOfElement, e);
			throw e;
		}
		return flag;

		/*
		 * boolean flag = false; try { if (element.isDisplayed()) {
		 * logger.info("<b> {} </b> field is displayed.", nameOfElement);
		 * test.get().log(Status.INFO, "<b>" + nameOfElement + "</b>" +
		 * " field is displayed."); flag = true; } } catch (NoSuchElementException e) {
		 * 
		 * if (scrollCounter == 1) {
		 * logger.info("Element not found <b> {} </b> trying scroll: {}", nameOfElement,
		 * scrollCounter);
		 * 
		 * scrollUp(element,scrollCounter); scrollCounter++; isElementFound(element,
		 * nameOfElement); } else {
		 * 
		 * String text = "Following element not found to perform action: " +
		 * nameOfElement; logger.info(text); Markup m = MarkupHelper.createLabel(text,
		 * ExtentColor.RED); test.get().fail(m); flag = false; } }
		 * 
		 * catch (Exception e) { e.printStackTrace(); }
		 * 
		 * return flag;
		 */
	}

	public void fieldVerifications(HashMap<WebElement, ArrayList<String>> hashmap) {

		String fieldName = "Field Name";
		String fieldType = "Field Type";
		String fieldState = "Field State";
		String language = "French";
		String englishLanguage = "English Language";
		String otherLanguage = language + " Language";

		StringBuilder html = new StringBuilder(
				"<style> #mytable, #myth, #mytd{border: 1px solid black;}</style> <table id=mytable>");
		html.append("<tr>");
		html.append("<th id=myth>" + fieldType + "</th>");
		html.append("<th id=myth>" + fieldName + "</th>");
		html.append("<th id=myth>" + fieldState + "</th>");
		html.append("<th id=myth>" + englishLanguage + "</th>");
		// html.append("<th id=myth>" + otherLanguage + "</th>");

		for (Entry<WebElement, ArrayList<String>> entry : hashmap.entrySet()) {

			try {
				WebElement element = entry.getKey();
				System.out.print("key :" + element + " \nvalue :");
				html.append("<tr>");
				ArrayList<String> expectedValues = entry.getValue();

				String elementType = element.getTagName();
				html.append("<td id=mytd>" + elementType + "</td>");
				for (String ev : expectedValues) {

					Integer i = expectedValues.indexOf(ev);
					switch (i) {
					case 0:
						String nameOfElement = ev;
						html.append("<td id=mytd>" + nameOfElement + "</td>");
						break;
					case 1:
						String isEnabled = Boolean.parseBoolean(element.getAttribute("enabled")) == true ? "Enabled"
								: "Disabled";
						if (!isEnabled.equals(ev)) {
							isEnabled = "<font color=\"red\">" + isEnabled + "</font>";
						}
						html.append("<td id=mytd>" + isEnabled + "</td>");
						sAssert.assertEquals(isEnabled, ev);
						break;
					case 2:
						String englishText = element.getText();
						/*if(englishText!=expectedValues.get(2)){
							englishText = "<font color=\"red\">" + englishText + "</font>";
						}*/
						try {
							
							sAssert.assertEquals(englishText, expectedValues.get(2));
							//Assert.assertEquals(englishText, expectedValues.get(2));	
							//html.append("<td id=mytd>" + englishText + "</td>");
						}
						
						catch (Throwable t) {
							
							
							
							
							
							TestUtils.captureScreenshot();
							String exceptionMessage = "" ;
							exceptionMessage = exceptionMessage + " " + t.getLocalizedMessage();
							
							exceptionMessage = exceptionMessage+	" - <a href=\"" + TestUtils.screenshotPath + TestUtils.screenshotName + "\"" + "target=\"_blank\">Screenshot</a>";
							
							
							
							BaseTest.test.get().log(Status.FAIL,
									"<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured:Click to see"
											+ "</font>" + "</b >" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>");
							
							logger.info(exceptionMessage);
								
						
						
						
						
						
						
							/*TestSetup.test.get().fail("<b>" + "<font color=" + "red>" + "ScreenShot of failure" + "</font>" + "</b>",
									MediaEntityBuilder.createScreenCaptureFromPath(TestUtils.screenshotPath + TestUtils.screenshotName)
									.build());*/
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							//TestUtils.captureScreenshot();
							englishText =	"<a href=\"" + TestUtils.screenshotPath + TestUtils.screenshotName + "\"" + "target=\"_blank\">"+ "<font color=\"red\">" + englishText + "</font>"+"</a>";
							
							
							//englishText = "<font color=\"red\">" + englishText + "</font>";
							
							//ErrorCollector.addVerificationFailure(t);
							//html.append("<td id=mytd>" + englishText + "</td>");
						}
						//englishText = "<font color=\"red\">" + englishText + "</font>";
						html.append("<td id=mytd>" + englishText + "</td>");
						
						/*if (!englishText.equals(expectedValues.get(2))) {
							englishText = "<font color=\"red\">" + englishText + "</font>";
						}
						html.append("<td id=mytd>" + englishText + "</td>");*/

					}

				}
				html.append("</tr>");
			} catch (Throwable t) {
				System.out.println("Error occured");
			}

		}

		html.append("</table>");
		//sAssert.assertAll();
		test.get().log(Status.INFO, html.toString());

	}

	public String fieldValidation(WebElement element, String nameOfElement, String expectedState, String expectedText) {
		String isEnabled = Boolean.parseBoolean(element.getAttribute("enabled")) == true ? "Enabled" : "Disabled";
		if (!isEnabled.equals(expectedState)) {
			isEnabled = "<font color=\"red\">" + isEnabled + "</font>";
		}

		String englishText = element.getText();
		if (!englishText.equals(expectedText)) {
			englishText = "<font color=\"red\">" + englishText + "</font>";
		}

		StringBuilder htmlRow = new StringBuilder(
				"<style> #mytable, #myth, #mytd{border: 1px solid black;}</style> <table id=mytable>");
		htmlRow.append("<tr>");
		htmlRow.append("<td id=mytd>" + nameOfElement + "</td>");
		htmlRow.append("<td id=mytd>" + element.getTagName() + "</td>");
		htmlRow.append("<td id=mytd>" + isEnabled + "</td>");
		htmlRow.append("<td id=mytd>" + element.getText() + "</td>");
		htmlRow.append("</tr>");
		return htmlRow.toString();

	}

	public void softAssert(String expected, String actual) {
		SoftAssert softAssertion = new SoftAssert();
		System.out.println("softAssert Method Was Started");
		softAssertion.assertEquals(expected, actual);
		System.out.println("softAssert Method Was Executed");
	}

	public void assertElementDisplayedTrue(WebElement element, String nameOfElement) {
		Assert.assertTrue(element.isDisplayed());
		logger.info("<b> {} </b> field is displayed.", nameOfElement);
		test.get().log(Status.INFO, "<b>" + nameOfElement + "</b>" + " field is displayed.");
	}

	public void assertExpectedTextTrue(WebElement element, String nameOfElement, String expectedText) {
		Assert.assertEquals(waitForElement(element).getText(), expectedText);
		logger.info("<b> {} </b> field is displayed - {}.", nameOfElement, expectedText);
		test.get().log(Status.INFO, "<b>" + nameOfElement + "</b>" + " field is displayed - " + expectedText);
	}

	public void waitForPageToLoad(WebElement id) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.elementToBeClickable(id));
	}

	public void waitForElementToDisAppear(String id) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));
	}

	public void waitForElementsToAppear(List<WebElement> id) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfAllElements(id));
	}

	public WebElement waitForElement(WebElement arg) {
		waitForPageToLoad(arg);
		WebElement el = arg;
		return el;
	}

	public enum DIRECTION {
		DOWN, UP, LEFT, RIGHT;
	}

	@SuppressWarnings("rawtypes")
	public static void swipe(WebDriver driver, DIRECTION direction, long duration) {
		Dimension size = driver.manage().window().getSize();

		int startX = 0;
		int endX = 0;
		int startY = 0;
		int endY = 0;

		switch (direction) {
		case RIGHT:
			startY = (int) (size.height / 2);
			startX = (int) (size.width * 0.05);
			endX = (int) (size.width * 0.90);
			new TouchAction((PerformsTouchActions) driver).press(PointOption.point(startX, startY))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
					.moveTo(PointOption.point(endX, startY)).release().perform();
			break;

		case LEFT:
			startY = (int) (size.height / 2);
			startX = (int) (size.width * 0.90);
			endX = (int) (size.width * 0.05);
			new TouchAction((PerformsTouchActions) driver).press(PointOption.point(startX, startY))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
					.moveTo(PointOption.point(endX, startY)).release().perform();

			break;

		case UP:
			endY = (int) (size.height * 0.30);
			startY = (int) (size.height * 0.70);
			startX = (size.width / 2);
			new TouchAction((PerformsTouchActions) driver).press(PointOption.point(startX, startY))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
					.moveTo(PointOption.point(startX, endY)).release().perform();
			break;

		case DOWN:
			startY = (int) (size.height * 0.70);
			endY = (int) (size.height * 0.30);
			startX = (size.width / 2);
			new TouchAction((PerformsTouchActions) driver).press(PointOption.point(startX, startY))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
					.moveTo(PointOption.point(startX, endY)).release().perform();

			break;

		}
	}

	// This method scrolls downwards / swipe upwards only
	public static void scrollUp(WebElement element, int maxScroll) {
		boolean found = false;
		int i = 1;
		while (!found && maxScroll >= i) {
			try {

				element.isDisplayed();
				found = true;
			} catch (Exception e) {
				swipe(driver, DIRECTION.UP, 500);
				i++;
			}
		}
	}

	// This method scrolls downwards / swipe upwards only
	public void scrollDown(WebElement element) {
		boolean found = false;
		while (!found) {
			try {
				element.isDisplayed();
				found = true;
			} catch (Exception e) {
				swipe(driver, DIRECTION.DOWN, 500);
			}
		}
	}

	// scroll to element text - used for Logout function

	@SuppressWarnings("unchecked")
	public MobileElement scrollToElementUsingText(final String uiSelector) {
		try {
			if (((FindsByAndroidUIAutomator<MobileElement>) driver).findElementByAndroidUIAutomator(
					"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""
							+ uiSelector + "\").instance(0))")
					.isDisplayed()) {
				logger.info("Following text is visible: " + uiSelector);
				return ((FindsByAndroidUIAutomator<MobileElement>) driver).findElementByAndroidUIAutomator(
						"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""
								+ uiSelector + "\").instance(0))");
			}

		} catch (Exception e) {
			logger.info("Failed to scroll and find element ", e.getStackTrace());

		}
		return ((FindsByAndroidUIAutomator<MobileElement>) driver).findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""
						+ uiSelector + "\").instance(0))");
	}

	public static String getCurrentMethodName(StackTraceElement[] stacktrace) {
		return stacktrace[1].getMethodName();
	}

	public static String getCallingMethodName(StackTraceElement[] stacktrace) {
		return stacktrace[2].getMethodName();
	}

	// **** custome logger methods Start****

	// Logging
	public static void reportInfo(Logger logger, String informationalMessage) {
		logger.info(informationalMessage);
		test.get().log(Status.INFO, informationalMessage);
	}

	public static void reportFail(Logger logger, String failureMessage) {
		logger.error(failureMessage);
		Markup m = MarkupHelper.createLabel(failureMessage, ExtentColor.RED);
		test.get().fail(m);
	}
	// **** custome logger methods End****
}
