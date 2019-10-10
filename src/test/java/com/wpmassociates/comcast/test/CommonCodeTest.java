package com.wpmassociates.comcast.test;

import com.wpmassociates.comcast.client.*;
import com.wpmassociates.comcast.utility.*;

import junit.framework.TestCase;

import java.util.Map;
import java.util.TreeMap;
import java.util.Properties;

public class CommonCodeTest extends TestCase {

	private Properties properties = null;

	private Map<String, String> partnerAd = null;
	
	private String partnerId = "10";
	
	public CommonCodeTest() {
		properties = PropertyReader.readProperties();			
		partnerAd = new TreeMap<String, String>(CommonCode.getComparator());	
	}

	public void testMakePostRequest() throws Exception {
		partnerAd.put("partner_id", partnerId);
		partnerAd.put("duration", "1");
		partnerAd.put("ad_content", "This is the ad content");
		String response = CommonCode.makePostRequest(partnerAd);
	}
	
	public void testMakeGetRequest() throws Exception {
		String response = CommonCode.makeGetRequest(partnerId);
	}
	
}

