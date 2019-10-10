package com.wpmassociates.comcast.client;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.logging.Logger;
import java.io.IOException;
import static java.lang.System.in;
import static java.lang.System.out;

import com.wpmassociates.comcast.constants.Constants;

public class HttpClientRequestCommandLine {
	
	private static Logger logger = Logger.getLogger(HttpClientRequestCommandLine.class.getName());
	
	public static void main(String...inputs) {
		String[] keys = {Constants.DURATION, Constants.PARTNERID, Constants.ADCONTENT};
		Scanner scanner = null;
		int partnerId = 0;
		int duration = 0;
		String adContent = null;
		Map<String, String> partnerAd = new TreeMap<String, String>(CommonCode.getComparator());
		scanner = new Scanner(in);
		out.print("Enter partnerId ");
		partnerId = scanner.nextInt();
		out.print("Enter duration as days ");
		duration = scanner.nextInt();
		out.print("Enter ad content ");
		adContent = scanner.next();
		partnerAd.put(keys[2], adContent);
		partnerAd.put(keys[1], String.valueOf(partnerId));
		partnerAd.put(keys[0], String.valueOf(duration));
		scanner.close();
		Set<String> keySet = partnerAd.keySet();
		for (String key : keySet) 
			logger.info("Key " + key + " value " + partnerAd.get(key));
		
		String response = CommonCode.makePostRequest(partnerAd);
		logger.info("Response is " + response);
	}
}
