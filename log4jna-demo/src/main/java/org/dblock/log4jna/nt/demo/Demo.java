package org.dblock.log4jna.nt.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Demo {
	public static void main(String[] args) {
		try {
			Logger logger = LogManager.getLogger();
			String message = "";
			for(String arg : args) {
				message += arg;
				message += " ";
			}
			logger.debug("debug: " + message);
			logger.info("info: " + message);
		} catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);			
		}
	}
}
