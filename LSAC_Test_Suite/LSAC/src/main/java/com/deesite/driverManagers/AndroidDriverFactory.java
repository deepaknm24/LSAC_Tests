package com.deesite.driverManagers;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.deesite.utilities.DriverFactory;
import com.deesite.utilities.DriverManager;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class AndroidDriverFactory extends DriverFactory{
	

	public static AndroidDriver<MobileElement> createDriverInstance(String browser) {
		AndroidDriver<MobileElement> androidDriver = null;
		DesiredCapabilities androidCaps = null;
		switch (browser.toLowerCase()) {

		case "chrome":
			androidCaps = new DesiredCapabilities();
			androidCaps.setCapability("deviceName", "Galaxy S8");
			androidCaps.setCapability("platformName", "Android");
			androidCaps.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
			androidCaps.setCapability("automationName", "UiAutomator2");
			
			System.setProperty("webdriver.chrome.driver", DriverFactory.getChromeExePath());
			try {
				androidDriver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), androidCaps);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			DriverManager.setDriver(androidDriver);
			DriverManager.setImplicitWait(androidDriver, 20);
			break;

		}

		return androidDriver;

	}

	public static void destroyDriverInstance(AndroidDriver<MobileElement> driver) {
		if (driver != null) {
			driver.quit();
		}
	}

	

}
