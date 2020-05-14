package com.deesite.utilities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deesite.constants.Constants;



public class DeviceConfiguration {
	Logger logger = LoggerFactory.getLogger(DeviceConfiguration.class);
	CommandPrompt cmd = new CommandPrompt();
	Map<String, String> devices = new HashMap<>();
	public static String newline = System.getProperty("line.separator");
	/**
	 * This method start adb server
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void startADB() throws InterruptedException, IOException {
		String output = cmd.runCommand("adb start-server");
		String[] lines = output.split(File.separator+"n");
		if (lines.length == 1)
			logger.info("adb service already started");
		else if (lines[1].equalsIgnoreCase("* daemon started successfully"))
			logger.info("adb service started");
		else if (lines[0].contains("internal or external command")) {
			logger.info("adb path not set in system varibale");
			System.exit(0);
		}
	}
	
	/**
	 * This method stop adb server
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void stopADB() throws InterruptedException, IOException {
		cmd.runCommand("adb kill-server");
	}
	
	/**
	 * This method return connected android devices
	 * 
	 * @return hashmap of connected android devices information
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public Map<String, String> getAndroidDevices() throws InterruptedException, IOException {

		startADB();
		String output = cmd.runCommand("adb devices");
		String[] lines = output.split("/n");

		if (lines.length <= 1) {
			logger.info(Constants.NO_DEVICE_CONNECTED);
			System.out.println(Constants.NO_DEVICE_CONNECTED);
			stopADB();
			System.exit(0);
		}

		for (int i = 1; i < lines.length; i++) {
			lines[i] = lines[i].replaceAll("\\s+", "");

			if (lines[i].contains("device")) {
				lines[i] = lines[i].replaceAll("device", "");
				String deviceID = lines[i];
				String model = cmd
						.runCommand(Constants.COMMAND_ADB_S + deviceID + " shell getprop ro.product.model")
						.replaceAll("/n", "");
				String brand = cmd
						.runCommand(Constants.COMMAND_ADB_S + deviceID + " shell getprop ro.product.brand")
						.replaceAll("/n", "");
				String osVersion = cmd
						.runCommand(
								Constants.COMMAND_ADB_S + deviceID + " shell getprop ro.build.version.release")
						.replaceAll("/n", "");
				String deviceName = brand + " " + model;

				devices.put(Constants.DEVICE_ID + i, deviceID);
				devices.put(Constants.DEVICE_NAME + i, deviceName);
				devices.put(Constants.DEVICE_OS_VERSION + i, osVersion);

				logger.info("Following device is connected");
				logger.info(deviceID + " " + deviceName + " " + osVersion + "\n");
			} else if (lines[i].contains("unauthorized")) {
				lines[i] = lines[i].replaceAll("unauthorized", "");
				String deviceID = lines[i];

				logger.info("Following device is unauthorized");
				logger.info(deviceID + "\n");
			} else if (lines[i].contains("offline")) {
				lines[i] = lines[i].replaceAll("offline", "");
				String deviceID = lines[i];

				logger.info("Following device is offline");
				logger.info(deviceID + "\n");
			}
		}
		return devices;
	}
}
