package com.wpmassociates.comcast.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

import com.wpmassociates.comcast.constants.Constants;

public class Validator {
	
	private static Logger logger = Logger.getLogger(Validator.class.getName());

	public static String checkInput(String uri) {
		logger.info("Input is " + uri);
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
		logger.info("There is a match " + match);
		return match;
	}

	private static boolean checkForContext(String input) {
		String substring = input.substring(0,8);
		logger.info("Substring " + substring);
		return substring.equals(Constants.COMCAST);
	}

	
}	
	
