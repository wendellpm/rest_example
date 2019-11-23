package com.wpmassociates.comcast.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

import com.wpmassociates.comcast.constants.Constants;

public class Validator {
	
	private static Logger logger = Logger.getLogger(Validator.class.getName());

	public static String checkInput(String uri) {
		logger.debug("Input is " + uri);
		int lastIndex = uri.length();
		
		boolean validated = checkForContext(uri);
		if (!validated) return Constants.NOT_CONTEXT;
		uri = uri.substring(9, lastIndex).trim();
		if (checkForNumeric(uri))
		return uri;
		return Constants.NOT_NUMERIC;
	}

	public static boolean checkForNumeric(String input) {	
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(input);
		boolean match = matcher.matches();
		logger.debug("Input is numeric? " + match);
		return match;
	}

	private static boolean checkForContext(String input) {
		String substring = input.substring(0,8);
		logger.debug("Substring " + substring);
		return substring.equals(Constants.COMCAST);
	}

	
}	
	
