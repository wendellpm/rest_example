package com.wpmassociates.comcast.constants;

public interface Constants { 
	final long DAYINMILLISECONDS = 1000 * 60 * 60 * 24;
	final String EXCEEDS = "Campaign no longer active, duration exceeded";
	final String NO_RECORD = "No record for partner identifier";
	final int ID_LOCATION = 9;
	final String ALREADY_EXISTS = "Partner already exists with id ";
	final String ADDED = "Partner added with id ";
	final String NOT_ADDED = "Partner not added with id ";
	final String NOT_NUMERIC = "Partner id must be numeric ";
	final String PARTNERID = "partner_id";
	final String DURATION = "duration";
	final String ADCONTENT = "ad_content";
	final String COMCAST = "/comcast";
	final String NOT_CONTEXT = "Context root must be /comcast";
  }
