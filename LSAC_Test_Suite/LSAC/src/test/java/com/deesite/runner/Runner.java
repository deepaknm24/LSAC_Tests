package com.deesite.runner;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.deesite.constants.ConfigConstants;
import com.deesite.utilities.DynamicTestNG;

public class Runner {
	Map<String, String> testngParams = new HashMap<>();
	public static int retry = ConfigConstants.DEFAULT_RETRY_COUNT;
	public static String installFlag = ConfigConstants.DEFAULT_FLAG;
	public static String platform = System.getProperty("platform");

	@Test
	public void runTestNG() {
		DynamicTestNG dTestNG = new DynamicTestNG();

		testngParams.put("platform", platform);

		dTestNG.runTestNGTest(testngParams);
	}
}
