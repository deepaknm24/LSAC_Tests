package com.deesite.utilities;

import java.net.ServerSocket;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class AppiumServerJava {
	private AppiumDriverLocalService service;
	private AppiumServiceBuilder builder;
	private DesiredCapabilities cap;
	static Logger logger = LoggerFactory.getLogger(Class.class);
	static CommandPrompt cp = new CommandPrompt();

	public void startServer() {
		// Set Capabilities
		cap = new DesiredCapabilities();
		cap.setCapability("noReset", "false");

		// Build the Appium service
		builder = new AppiumServiceBuilder();
		builder.withIPAddress("127.0.0.1");
		builder.usingPort(4723);
		builder.withCapabilities(cap);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");

		// Start the server with the builder
		service = AppiumDriverLocalService.buildService(builder);
		service.start();
	}

	public void stopServer() {
		service.stop();
	}

	public boolean checkIfServerIsRunnning(int port) {

		boolean isServerRunning = false;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.close();
		} catch (Exception e) {
			// If control comes here, then it means that the port is in use
			isServerRunning = true;
		} finally {
			serverSocket = null;
		}
		return isServerRunning;
	}

	public static void main(String[] args) {
		AppiumServerJava appiumServer = new AppiumServerJava();

		int port = 4723;
		if (!appiumServer.checkIfServerIsRunnning(port)) {
			appiumServer.startServer();
			appiumServer.stopServer();
		} else {
			System.out.println("Appium Server already running on Port - " + port);
		}
	}

	public static void killExistingNodeInstances() {
		String scriptToKillExistingNodeInstances = null;
		if (System.getProperty("os.name").contains("Win")) {
			scriptToKillExistingNodeInstances = System.getProperty("user.dir")
					+ "/shellscripts/Kill-Node-Instances.bat";
		} else if (System.getProperty("os.name").contains("Mac")) {
			scriptToKillExistingNodeInstances = System.getProperty("user.dir")
					+ "/shellscripts/Kill-Node-Instances.bat";
		}

		logger.info(scriptToKillExistingNodeInstances);
		cp.runCommand(scriptToKillExistingNodeInstances);
		logger.info("Killed Existing Node Instances");
		System.out.println("Killed Existing Node Instances");
	}
}
