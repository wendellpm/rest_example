package com.wpmassociates.comcast.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import java.util.Date;

import com.wpmassociates.comcast.persistence.*;
import com.wpmassociates.comcast.domain.*;
import com.wpmassociates.comcast.constants.*;
import com.wpmassociates.comcast.validation.*;
import com.wpmassociates.comcast.utility.*;

import static java.lang.System.currentTimeMillis;

public class AdService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private StoreData persist;
	private String jsonString = null;
	private Properties properties = PropertyReader.readProperties();
	private String useMap = null;

	public AdService() {

		useMap = properties.getProperty("useMap");
		if (useMap.equals("yes")) {
			logger.info("Log to in memory map");
			persist = StoreDataMap.getInstance();
		} else {
			logger.info("Log to database");
			persist = new StoreInDatabase(properties);
		}
	}	
	
	public String retrieveData(int partnerId) {
		JSONMapStorageObject adObject = null;
		boolean exists = checkId(partnerId);
		logger.info("Partner id exists " + exists);
		if (!exists)
			return Constants.NO_RECORD + " " + partnerId;
		adObject = persist.retrieveData(partnerId);
		if (adObject == null)
			return "Add object is null";
		Date date = adObject.getEntryTime();
		long duration = adObject.getDuration();
		logger.info("Entry time is " + date + " duration is " + duration);
		if (currentTimeMillis() > date.getTime() + duration)
			return Constants.EXCEEDS;
		else 
			jsonString = adObject.getJsonString();
		return jsonString;
	}
	
	public PersistenceResult processData(BufferedReader reader) {
		StringBuffer buffer = new StringBuffer();
		String stringId = "";
		String line = null;
		try {
			while ((line = reader.readLine()) != null)
				buffer.append(line);
		} catch (Exception exception) {}
		jsonString = buffer.toString();
		int duration = 0;
		String adContent = null;
		int partnerId = 0;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			stringId = jsonObject.getString("partner_id");
			boolean validated = Validator.checkForNumeric(stringId);
			if (!validated)
				return new PersistenceResult(stringId, Constants.NOT_NUMERIC);
			
			partnerId = Integer.parseInt(stringId);
			duration = Integer.parseInt(jsonObject.getString("duration"));
			adContent = jsonObject.getString("ad_content");
		} catch (JSONException exception) {}
		long milliseconds = duration * Constants.DAYINMILLISECONDS;
		JSONMapStorageObject storageObject = new JSONMapStorageObject(new Date(), jsonString, partnerId, milliseconds,  adContent);	
		logger.info("json storage object " + storageObject.toString());
		if (checkId(partnerId))
			return new PersistenceResult(stringId, "exists");
		else
			if (persist.storeData(partnerId, storageObject))
				return new PersistenceResult(stringId, "added");
			else 
				return new PersistenceResult(stringId, "problem");
	}
		
	private boolean checkId(int partnerId) {
		return persist.checkForPartnerId(partnerId);
	}
 }
