package com.deesite.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class DriverFactory {
	private static String chromeExePath;
	private static String ieExePath;
	private static String firefoxExePath;

	public static WebDriver createDriverInstance(String browser) {
		WebDriver driver = null;

		switch (browser.toLowerCase()) {

		case "chrome":
			System.setProperty("webdriver.chrome.driver", DriverFactory.getChromeExePath());
			driver = new ChromeDriver();
			DriverManager.setDriver(driver);
			DriverManager.maximizeBrowser(DriverManager.getDriver());
			DriverManager.setImplicitWait(driver, 20);
			break;

		case "ie":
			System.setProperty("webdriver.ie.driver", DriverFactory.getIeExePath());
			driver = new InternetExplorerDriver();
			DriverManager.setDriver(driver);
			DriverManager.maximizeBrowser(DriverManager.getDriver());
			DriverManager.setImplicitWait(driver, 20);
			break;

		case "firefox":
			System.setProperty("webdriver.gecko.driver", DriverFactory.getFirefoxExePath());
			driver = new FirefoxDriver();
			DriverManager.setDriver(driver);
			DriverManager.maximizeBrowser(DriverManager.getDriver());
			DriverManager.setImplicitWait(driver, 20);
			break;

		default:

		}

		return driver;

	}

	public static void destroyDriverInstance(WebDriver driver) {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	public static String getChromeExePath() {
		return chromeExePath;
	}

	public static void setChromeExePath(String chromeExePath) {
		DriverFactory.chromeExePath = chromeExePath;
	}

	public static String getIeExePath() {
		return ieExePath;
	}

	public static void setIeExePath(String ieExePath) {
		DriverFactory.ieExePath = ieExePath;
	}

	public static String getFirefoxExePath() {
		return firefoxExePath;
	}

	public static void setFirefoxExePath(String firefoxExePath) {
		DriverFactory.firefoxExePath = firefoxExePath;
	}

}
