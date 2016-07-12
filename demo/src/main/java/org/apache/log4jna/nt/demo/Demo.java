package org.apache.log4jna.nt.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Demo {
	public static void main(String[] args) {
		try {
//			PropertyConfigurator.configureAndWatch("log4j.properties");
			Logger logger = LogManager.getLogger(Demo.class);
			String message = "";
			for(String arg : args) {
				message += arg;
				message += "\r\n";
			}
			logger.debug("debug: " + message);
			logger.info("info: " + message);
		} catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);			
		}
	}
}
