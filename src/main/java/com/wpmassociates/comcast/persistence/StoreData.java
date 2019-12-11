package com.wpmassociates.comcast.persistence;

import java.util.Properties;
import javax.servlet.http.HttpSession;

import com.wpmassociates.comcast.domain.*;

public interface StoreData { 
	boolean storeData(int partnerId, JSONMapStorageObject storageObject);
	JSONMapStorageObject retrieveData(int partnerId);
	boolean checkForPartnerId(int partnerId);
	boolean deleteData(int partnerId);
	void setProperties(Properties properties);
 }
