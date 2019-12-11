package com.wpmassociates.comcast.client;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.logging.Logger;
import java.io.IOException;
import static java.lang.System.in;
import static java.lang.System.out;
import java.util.regex.Pattern;

import com.wpmassociates.comcast.constants.Constants;

public class HttpClientRequestCommandLine {
	
	private static final Logger logger = Logger.getLogger(HttpClientRequestCommandLine.class.getName());
	
	private static final Scanner scanner = new Scanner(in);
	
	public static void main(String...inputs) {
		out.println("Enter data, after each entry press the [Enter] key");
		
		String partnerId = null;
		String duration = null;
		String adContent = null;
		out.println("Partner id ");
		partnerId = scanner.next();
		out.println("Length in days that text active ");
		duration = scanner.next();
		out.println("Text ");
		scanner.useDelimiter("\r\n");
		adContent = scanner.next();
		out.println("POST enter 1, PUT enter 2 ");
		int method = scanner.nextInt();
		Map<String, String> partnerAd = new TreeMap<String, String>(CommonCode.getComparator());
		String[] keys = {Constants.DURATION, Constants.PARTNERID, Constants.ADCONTENT};
		partnerAd.put(keys[2], adContent);
		partnerAd.put(keys[1], partnerId);
		partnerAd.put(keys[0], duration);
		scanner.close();
		
		Set<String> keySet = partnerAd.keySet();
		for (String key : keySet) 
			logger.info("Key " + key + " value " + partnerAd.get(key));
		
		String response = null;
		
		switch(method) {
			case 1:
			response = CommonCode.makePostRequest(partnerAd);
			break;
			
			case 2:
			response = CommonCode.makePutRequest(partnerAd);
			break;
			
			default:
			break;
		}
		logger.info("Response is " + response);
	}
	
	public enum Method {
			PUT("put"), POST("post"), GET("get"), DELETE("delete");
		
			private String property = null;
			
			public String getProperty() {
				return property;
			}
			
			Method(String property) {
				this.property = property;
			}		
	}
}
