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
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;

import com.wpmassociates.comcast.validation.*;
import com.wpmassociates.comcast.constants.*;
import com.wpmassociates.comcast.service.*;
import com.wpmassociates.comcast.domain.*;

public class AdServlet extends HttpServlet {

	private PrintWriter printWriter;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private AdService service = null;
	private int integerValue = 0;
	private boolean isInteger = false;
	private HttpSession session = null;

	@Override
	public void init() {
		ServletContext context = getServletConfig().getServletContext();
		logger.debug("Call init method. Show context parameters");
		logger.debug("Name\tValue\tis integer");
		Enumeration<String> enumeration = context.getInitParameterNames();
		while (enumeration.hasMoreElements()) {
				String name = enumeration.nextElement();
				String value = context.getInitParameter(name);
				if (value.matches("[0-9]+")) {
					isInteger = true;
					integerValue = Integer.parseInt(value);
				}
				logger.debug(name + "\t" + value + "\tInteger\t" + isInteger);		
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// checkHeaders(request, "GET");
		service = new AdService();
		validateGETInput(response, request);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// checkHeaders(request, "POST");
		getServletContext().setAttribute("period", integerValue);
		session = request.getSession();
		session.setAttribute("post", "value for post");
		performServiceCall(request, response);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// checkHeaders(request, "PUT");
		performServiceCall(request, response);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// checkHeaders(request, "DELETE");
		performServiceCall(request, response);
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
	
	private void createResponse(PersistenceResult result, HttpServletResponse response) {
		String resultValue = result.getResult();
		String id = result.getPartnerId();
		switch (resultValue) {
			case Constants.EXISTS:
			response.setHeader("Response", HttpServletResponse.SC_CONFLICT + " " + Constants.ALREADY_EXISTS + " " + id);
			break;
			
			case Constants.ID_ADDED:
			response.setHeader("Response", HttpServletResponse.SC_OK + " " + Constants.ADDED + " " + id);
			break;
		
			default:
			response.setHeader("Response", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " unknown problem, but " + Constants.NOT_ADDED + " " + id);
			break;
		}
	}
	
	private void validateGETInput(HttpServletResponse response, HttpServletRequest request) throws IOException {
	
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
	
	private void performServiceCall(HttpServletRequest request, HttpServletResponse response) throws IOException {
		service = new AdService();
		PersistenceResult result = null;
		printWriter = response.getWriter();
		BufferedReader reader = request.getReader();
		HttpSession session = request.getSession();
		session.setAttribute("performanceCall", "this is the value for attribute");
		String attributeValue = (String)session.getAttribute("performanceCall");logger.debug("Session attribute value is now " + attributeValue);
		session.setAttribute("performanceCall", "change value of attribute");
		attributeValue = (String)session.getAttribute("performanceCall");
		logger.debug("Session attribute value change to " + attributeValue);
		response.setContentType("text/plain,charset=UTF-8");
		result = service.processData(reader);
		createResponse(result, response);
	}			
 }
