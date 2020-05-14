package com.deesite.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileManager {
	public static Properties configProperties = new Properties();
	private static String configFilePath;
	private static File file;
	private static FileInputStream fis;

	public static Properties createConfigPropeties() {
		file = new File(configFilePath);
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			configProperties.load(fis);
		} catch (IOException e) {
			System.out.println(e.fillInStackTrace());

			e.printStackTrace();
		}

		return configProperties;

	}
	
	public static String getConfigFilePath() {
		return configFilePath;
	}

	public static void setConfigFilePath(String configFilePath) {
		PropertyFileManager.configFilePath = configFilePath;
	}

}
