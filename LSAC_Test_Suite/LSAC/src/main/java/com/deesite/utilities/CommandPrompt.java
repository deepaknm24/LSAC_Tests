package com.deesite.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandPrompt {
	Process p;
	ProcessBuilder builder;
	static Logger logger = LoggerFactory.getLogger(CommandPrompt.class);

	/**
	 * This method run command on windows and mac
	 * 
	 * @param command to run.
	 */
	public String runCommand(String command) {
		String os = System.getProperty("os.name");
		logger.info(os);

		// build cmd process according to os.
		if (os.contains("Windows")) {
			builder = new ProcessBuilder("cmd.exe", "/c", command);
			builder.redirectErrorStream(true);
			try {
				p = builder.start();
			} catch (IOException e) {
				logger.info(e.getMessage());
				e.printStackTrace();
			}
		} else if (os.contains("Mac")){
			logger.info("This is Mac Operating System");
			try {
				p = Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				logger.info(e.getMessage());
				e.printStackTrace();
			}
		}
			
			// Get standard output.
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			String allLine = "";
			int i =1;
			try {
				while(((line=r.readLine()) != null) ){
					allLine = allLine + line + "/n";
					if(line.contains("Console LogLevel: debug")) {
						break;
					}
					if(line.contains("Appium REST http interface listener started")) {
						break;
					}
					i++;
					
				}
			} catch (IOException e) {
				logger.info(e.getMessage());
				e.printStackTrace();
			}
		

	return allLine;
}

}
