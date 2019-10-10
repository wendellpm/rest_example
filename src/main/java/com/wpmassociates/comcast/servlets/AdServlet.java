package com.wpmassociates.comcast.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;


import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import org.apache.log4j.Logger;
import java.util.Enumeration;

import com.wpmassociates.comcast.validation.*;
import com.wpmassociates.comcast.constants.*;
import com.wpmassociates.comcast.service.*;
import com.wpmassociates.comcast.domain.*;

public class AdServlet extends HttpServlet {

	private PrintWriter printWriter;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private AdService service = null;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		checkHeaders(request, "GET");
		service = new AdService();
		printWriter = response.getWriter();
		String uri = request.getRequestURI();
		String validated = Validator.checkInput(uri);
		response.setContentType("text/plain,charset=UTF-8");
		int partnerInteger = 0;
		String responseString = null;
		switch (validated) {
			case Constants.NOT_CONTEXT:
			printWriter.write(validated);
			break;

			case Constants.NOT_NUMERIC:
			printWriter.write(validated);	
			break;

			default:
			partnerInteger = Integer.parseInt(validated);
			responseString = service.retrieveData(partnerInteger);
			printWriter.write(responseString);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		checkHeaders(request, "POST");
		service = new AdService();
		PersistenceResult result = null;
		printWriter = response.getWriter();
		BufferedReader reader = request.getReader();
		response.setContentType("text/plain,charset=UTF-8");
		result = service.processData(reader);
		if (result.getResult().equals("exists")) 
			logger.info(Constants.ALREADY_EXISTS + result.getPartnerId());
		else if (result.getResult().equals("added"))
			logger.info(Constants.ADDED + result.getPartnerId());
		else if (result.getResult().equals("problem"))
			logger.info(Constants.NOT_ADDED + result.getPartnerId());
		else if (result.getResult().equals(Constants.NOT_NUMERIC))
			logger.info(Constants.NOT_NUMERIC + result.getPartnerId());
	}
	
	private void checkHeaders(HttpServletRequest request, String method) throws IOException {
		logger.info("Headers for method " + method);
		Enumeration<String> headerNames = request.getHeaderNames();
		Enumeration<String> headerValues = null;
		logger.info("in checkHeaders()");

		String headerName = null;
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				headerName = headerNames.nextElement();
				headerValues = request.getHeaders(headerName);
				if (headerValues != null) {
					logger.info(headerName + " " + request.getHeader(headerName));
					while (headerValues.hasMoreElements())
						logger.info(headerName + " " + headerValues.nextElement());
				} else {
					return;
				}
			}
		}
		else return;
	
	}


 }
