package com.deesite.utilities;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

public class DriverManager {
	public static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	public static WebDriver getDriver() {
		return driver.get();
	}

	public static void setDriver(WebDriver driver) {
		DriverManager.driver.set(driver);
	}
	
	public static void maximizeBrowser(WebDriver driver) 
	{
		driver.manage().window().maximize();

	}

	public static void setImplicitWait(WebDriver driver, int time) 
	{
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	}

}
