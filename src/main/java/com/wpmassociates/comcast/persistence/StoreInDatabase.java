package com.wpmassociates.comcast.persistence;

import java.util.Properties;
import java.sql.*;
import org.json.JSONObject;
import org.json.JSONException;
import javax.servlet.ServletContext;
import java.util.Date;
import org.apache.log4j.Logger;
import java.util.Enumeration;

import com.wpmassociates.comcast.domain.*;
import com.wpmassociates.comcast.constants.*;

public class StoreInDatabase implements StoreData { 

	private Connection connection; 
	private Properties properties;
   	private PreparedStatement preparedStatement;     
    private String insertStatement = null;
	private String queryStatement = null;
	private String deleteStatement = null;
	private Timestamp timestamp = null;
	private ResultSet resultSet = null;
	private JSONMapStorageObject adObject = null;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public StoreInDatabase(Properties properties) {
		Enumeration names = properties.propertyNames();
		while (names.hasMoreElements())
			logger.info(properties.getProperty((String)names.nextElement()));
			setProperties(properties);
		try {
			Class.forName(properties.getProperty("driverName"));
			connection = DriverManager.getConnection(properties.getProperty("mysqlUrl") + "/" + properties.getProperty("databaseName"), properties.getProperty("dbUsername"), properties.getProperty("dbPassword"));
		} catch (SQLException | ClassNotFoundException exception) {
			exception.getMessage();
		}
	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public boolean storeData(int partnerId, JSONMapStorageObject storageObject) {
        logger.info("In storeData method " + partnerId + " " + storageObject.toString());
		int added = 0;
        try { 
		String jsonString = storageObject.getJsonString();
		long entryTime = storageObject.getEntryTime().getTime();
		timestamp = new Timestamp(entryTime);
		logger.info("JSON " + jsonString + " time " + entryTime + " partner id " + partnerId);
            	insertStatement = "insert into json(partner_id, json_string, entry_date) values(?, ?, ?)"; 
            	preparedStatement = connection.prepareStatement(insertStatement);
	    	preparedStatement.setInt(1, partnerId);
            	preparedStatement.setString(2, jsonString);
		preparedStatement.setTimestamp(3, timestamp);	
		added = preparedStatement.executeUpdate();
        } catch (SQLException exception){
			exception.getMessage();
		} finally {                                                       
            if (preparedStatement != null) {
		try {
			preparedStatement.close();                      
		} catch (SQLException exception) {}
		if (connection != null)
			try {
				connection.close();
			} catch (SQLException exception) {}
		}                                          
        } 

        logger.info("Before return added " + added);
		return (added == 1);
	}	
		
	public JSONMapStorageObject retrieveData(int partnerId) {
		String jsonString = null;
		logger.info("Partner id in retrieveData() " + partnerId);
        try { 
         	queryStatement = "select json_string, entry_date from json where partner_id = ?";  
            	preparedStatement = connection.prepareStatement(queryStatement);
		preparedStatement.setInt(1, partnerId);
		resultSet = preparedStatement.executeQuery();
		if (resultSet == null)
			throw new SQLException("ResultSet null");
		resultSet.first();
		jsonString = resultSet.getString("json_string");
		timestamp = resultSet.getTimestamp("entry_date");
		if (jsonString == null || timestamp == null)
			throw new SQLException("Null values returned");
		long entryTime = timestamp.getTime();
		JSONObject jsonObject = new JSONObject(jsonString);
		long duration = Long.valueOf(Integer.parseInt((String)jsonObject.get("duration"))) * Constants.DAYINMILLISECONDS;
		String adContent = (String)jsonObject.get("ad_content");
		adObject = new JSONMapStorageObject(new Date(entryTime), jsonString, partnerId, duration, adContent);
        } catch (SQLException | JSONException exception){
			exception.getMessage();
		} finally {                                                       
           	if (preparedStatement != null) {
			try {
				preparedStatement.close();                      
			} catch (SQLException exception) {}
		}                                          
        } 
		return adObject;
	}		

	public boolean deleteData(int partnerId) {
		//to be implemented
		return true;
	}
	
	public boolean checkForPartnerId(int partnerId) {
		boolean exists = false;
		logger.info("In checkForPartnerId " + partnerId);
		try {
			queryStatement = "select partner_id from json where partner_id = ?";
			preparedStatement = connection.prepareStatement(queryStatement);
			preparedStatement.setInt(1, partnerId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null)
				exists = (resultSet.first());
		} catch (SQLException exception){
			exception.getMessage();
		} 
		logger.info("Exists is " + exists);
	   	return exists;
	}
	
 }
