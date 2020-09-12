package BasicExamples;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LoggerExample {

	
	//private static 

	public static void main(String[] args) {
		// TODO: find a way to use log4j.properties instead of this line
		BasicConfigurator.configure();
		Logger logger = Logger.getLogger(LoggerExample.class);
		
		logger.info("info");
		logger.warn("warn");

		logger.error("error");
		logger.fatal("fatal");

	}
}
