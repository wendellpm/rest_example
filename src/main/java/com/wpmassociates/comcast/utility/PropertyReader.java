package com.wpmassociates.comcast.utility;

import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.FileNotFoundException;


public class PropertyReader {

	private static Logger logger = Logger.getLogger(PropertyReader.class.getName());
	private static Properties properties = new Properties();
	private static ClassLoader classLoader = null;
	private static InputStream input = null;

	public static Properties readProperties() {

		try {
			classLoader = Thread.currentThread().getContextClassLoader();
			input = classLoader.getResourceAsStream("default.properties");
			if (input != null)
				properties.load(input);
			else
				throw new FileNotFoundException("Property file not found");
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		} finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException exception) {
					throw new RuntimeException(exception);
				}
        	}
	return properties;
	}

}
