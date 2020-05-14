package com.deesite.utilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class AppiumDriverManager {
	private static ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<>();
	private static final Logger logger = Logger.getLogger(Class.class.getName());
	static CapabilityManager capabilityManager = new CapabilityManager();

	public static AppiumDriver getDriver() {
		return appiumDriver.get();
	}

	public static void setDriver(AppiumDriver driver) {
		appiumDriver.set(driver);
	}

	private static AppiumDriver<MobileElement> initialiseDriver(String deviceId, String deviceName, String osVersion) {
		AppiumDriver<MobileElement> currentDriverSession = null;

		if (System.getProperty("os.name").contains("Win")) {
			AppiumServerJava appiumServer = new AppiumServerJava();
			if (appiumServer.checkIfServerIsRunnning(4723)) {
				System.out.println("Appium Server already running on Port - " + 4723);
				System.out.println("Stopping already running Appium Server on Port - " + 4723);
				// appiumServer.stopServer();
				AppiumServerJava.killExistingNodeInstances();
				System.out.println("Starting Appium Server on Port - " + 4723);
				appiumServer.startServer();

			} else {
				System.out.println("Starting Appium Server on Port - " + 4723);
				appiumServer.startServer();
			}

		}

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities = (DesiredCapabilities) capabilityManager.setAndroidCapabilities();
		capabilities.setCapability("deviceName", deviceName);
		//capabilities.setCapability("udid", deviceId); // ce0616068aafe04002 FA7AD1A03632 HKL3H4DQ
		capabilities.setCapability("platformVersion", osVersion);
		logger.info("****device information - " + deviceName + " - " + deviceId + "osVersion- " + osVersion);

		try {
			currentDriverSession = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			logger.info(e.getMessage());
		}
		catch (Exception e) {
			logger.info(e.getMessage());
		}

		logger.info("Session Created for Android ---- " + currentDriverSession.getSessionId() + "---"
				+ currentDriverSession.getContext());
		return currentDriverSession;

	}

	public static void startAppiumDriverInstance(String deviceId, String deviceName, String osVersion) {
		AppiumDriver<MobileElement> currentDriverSession;
		currentDriverSession = initialiseDriver(deviceId, deviceName, osVersion);
		AppiumDriverManager.setDriver(currentDriverSession);
	}

	protected void stopAppiumDriver() throws Exception {

		if (AppiumDriverManager.getDriver() != null && AppiumDriverManager.getDriver().getSessionId() != null) {
			logger.info("Session Deleting ---- " + AppiumDriverManager.getDriver().getSessionId() + "---"
					+ AppiumDriverManager.getDriver().getSessionDetail("udid"));
			AppiumDriverManager.getDriver().quit();
		}
	}

}
