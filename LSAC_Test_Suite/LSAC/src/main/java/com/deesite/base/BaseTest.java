package com.deesite.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Maps;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.deesite.constants.ConfigConstants;
import com.deesite.constants.Constants;
import com.deesite.excelManager.ExcelReader;
import com.deesite.reportmanager.ExtentManager;
import com.deesite.utilities.AppiumDriverManager;
import com.deesite.utilities.DeviceConfiguration;
import com.deesite.utilities.DriverFactory;
import com.deesite.utilities.DriverManager;
import com.deesite.utilities.PropertyFileManager;

public class BaseTest {
	Logger logger = LoggerFactory.getLogger(BaseTest.class);
	public static WebDriver driver;
	public static Properties configProperties;
	protected SoftAssert softAssertion = new SoftAssert();
	public static Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();
	protected SoftAssert sAssert = new SoftAssert();

	// Extent Report
	public static ExtentReports extent;
	public static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	public static ExcelReader excel = new ExcelReader( System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testdata" + File.separator + "TestData.xlsx");
	protected static Map<String, String> devices = new HashMap<String, String>();
	protected static DeviceConfiguration deviceConf = new DeviceConfiguration();
	protected int deviceCount;
	protected int numOfDevices;
	protected String port;
	public static String deviceId;
	public static String osVersion;
	public static String deviceName;
	public static String appPath;
	public static String appPackage;
	public static String os = System.getProperty("os.name");
	public static String environment = Constants.DEFAULT_ENV;

	@BeforeSuite
	public void beforeSuite() {
		extent = ExtentManager.getExtentReports();
		System.out.println("Befor Suite");
		PropertyFileManager.setConfigFilePath(ConfigConstants.PROPERTIES_FILE_DIR + "config.properties");

		if (System.getProperty("os.name").contains("Windows")) {
			DriverFactory.setChromeExePath(ConfigConstants.DRIVERS_DIR + "chromedriver.exe");

		} else if (System.getProperty("os.name").contains("Mac")) {
			DriverFactory.setChromeExePath(ConfigConstants.DRIVERS_DIR + "chromedriver");
		}

	}

	@BeforeClass
	public void beforeClass() {
		if (System.getProperty("platform").equalsIgnoreCase("web")) {
			ExtentTest parent = extent.createTest(getClass().getSimpleName());
			parentTest.set(parent);
		} else {

			try {
				devices = deviceConf.getAndroidDevices();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ExtentTest parent = extent
					.createTest(getClass().getSimpleName() + " - (" + devices.get("deviceName1") + ")");
			parentTest.set(parent);
		}

		System.out.println("Before Class");
		configProperties = PropertyFileManager.createConfigPropeties();

	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		System.out.println("Before Method");
		driver = null;
		 if (System.getProperty("platform").equalsIgnoreCase("android_web")) {
			if (driver == null) {
				
				deviceCount = devices.size() / 3;
				logger.info("number of connected android device - {}", deviceCount);
				for (int deviceNumber = 0; deviceNumber < deviceCount; deviceNumber++) {
					deviceId = devices.get("deviceID" + (deviceNumber + 1));
					deviceName = devices.get("deviceName" + (deviceNumber + 1));
					osVersion = devices.get("osVersion" + (deviceNumber + 1));
				}
				AppiumDriverManager.startAppiumDriverInstance(deviceId, deviceName, osVersion);
				AppiumDriverManager.getDriver().navigate().to(configProperties.getProperty("application.url"));
				driver=AppiumDriverManager.getDriver();
			}
		} else {

			if (driver == null) {
				driver = DriverFactory.createDriverInstance(configProperties.getProperty("browser"));
				DriverManager.getDriver().navigate().to(configProperties.getProperty("application.url"));
			}
		}
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {

		extent.flush();
		if (System.getProperty("platform").equalsIgnoreCase("android_web")) {
			AppiumDriverManager.getDriver().quit();
		} else {
			DriverManager.getDriver().quit();
			
		}
	}

}
