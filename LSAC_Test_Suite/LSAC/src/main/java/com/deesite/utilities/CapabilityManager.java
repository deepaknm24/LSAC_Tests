package com.deesite.utilities;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CapabilityManager {


	private DesiredCapabilities caps = new DesiredCapabilities();

	DeviceConfiguration deviceConfiguration = new DeviceConfiguration();

	Logger logger = LoggerFactory.getLogger(CapabilityManager.class);

	public Capabilities setAndroidCapabilities()  {
		try {
			caps.setCapability("deviceName", "Galaxy S8");
			caps.setCapability("platformName", "Android");
			caps.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
			caps.setCapability("automationName", "UiAutomator2");
			return caps;
		} catch (Exception e) {
			logger.info("Failed to setAndroidCapabilities", e.fillInStackTrace());
		}
		return caps;
	}
}