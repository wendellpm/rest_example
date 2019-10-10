package com.wpmassociates.comcast.persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.wpmassociates.comcast.domain.*;

public class StoreDataMap implements StoreData { 

	private static Logger logger = Logger.getLogger(StoreDataMap.class.getName());
	
	private static Map<Integer, JSONMapStorageObject> storageMap;
	private static StoreDataMap storeDataMap;
	
	private StoreDataMap() {
		storageMap = new ConcurrentHashMap<Integer, JSONMapStorageObject>();
	}
	
	public static StoreDataMap getInstance() {
		if (storeDataMap == null) {
			logger.info("Map is empty, create new Map object");
			storeDataMap = new StoreDataMap();
		} else {
			logger.info("Map already exists");
			Set<Map.Entry<Integer, JSONMapStorageObject>> set = storageMap.entrySet();
			if (set != null) {
			logger.info("Entries in current map");
			for (Map.Entry<Integer, JSONMapStorageObject> entry : set) {
				int key = entry.getKey();
				JSONMapStorageObject object = entry.getValue();
				logger.info("Key " + key + " value " + object.toString());
			}
			} else {
				logger.info("No entries in map");
			}
			
		}
		return storeDataMap;
	}
	

	public boolean storeData(int partnerId, JSONMapStorageObject storageObject) {
		logger.info("Partner id " + partnerId + " object " + storageObject.toString());
		storageMap.put(partnerId, storageObject);
		JSONMapStorageObject object = storageMap.get(partnerId);
		logger.info("Data stored " + object.getPartnerId());
		return (object != null);
	}
		
	public JSONMapStorageObject retrieveData(int partnerId) {
		JSONMapStorageObject object = storageMap.get(partnerId);
		logger.info("Data retrieved " + object.getPartnerId());
		return object;
	}
	
	public boolean deleteData(int partnerId) {
		storageMap.remove(partnerId);
		return (storageMap.containsKey(partnerId));
	}
	
	public boolean checkForPartnerId(int partnerId) {
		boolean hasKey = storageMap.containsKey(partnerId);
		logger.info("Has key " + hasKey);
		return hasKey;
	}
	
	public void setProperties(Properties properties)
	{logger.info("No properties to set here, but needed for interface");}
	
 }
