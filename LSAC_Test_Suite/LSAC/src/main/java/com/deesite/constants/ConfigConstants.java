package com.deesite.constants;

import java.io.File;

public class ConfigConstants {
	// Browsers
	public static final String BROWSER_WEB_CHROME = "chrome";

	// System paths
	public static final String PROJECT_ROOT_DIR = "user.dir";

	public static final String DRIVERS_DIR = System.getProperty("user.dir") + File.separator + "src" + File.separator
			+ "test" + File.separator + "resources" + File.separator + "drivers" + File.separator;

	public static final String PROPERTIES_FILE_DIR = System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + File.separator + "test" + File.separator + "resources" + File.separator + "propertyfiles"
			+ File.separator;
	
	public static final String PROPERTIES_FOLDER_PATH = File.separator + "src" + File.separator + "test"
			+ File.separator + "resources" + File.separator + "propertyfiles" + File.separator;

	// RETRY COUNT
	public static final int DEFAULT_RETRY_COUNT = 0;

	// FLAGS
	public static final String FLAG_YES = "y";
	public static final String FLAG_NO = "n";
	public static final String DEFAULT_FLAG = "y";
}
