package com.wpmassociates.comcast.servlets;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

@WebListener
public class ContextAttributeListener implements ServletContextAttributeListener {

	private DateFormat formatter = null;

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		 Class<?> thisClass = event.getSource().getClass(); 
		 String className = thisClass.getName();
		 logger.info("Event name " + event.getName() + " value " + event.getValue()	+ " source of event " + className);  
		 Method[] methods = thisClass.getDeclaredMethods();
		 StringBuilder builder = new StringBuilder();
		 for (Method method : methods) {
			 TypeVariable<Method>[] methodParameters = method.getTypeParameters();
			 for (TypeVariable<Method> methodParameter : methodParameters) {
				 String parameterName = methodParameter.getName();
				 builder.append("\tType name " + parameterName + "\n");
			 }
			 builder.append(method.getName() + "\n");
		 }
		 String methodsOfSource = builder.toString(); 
		 logger.info("Methods and parameters\n" + methodsOfSource);
		 logger.info("Time " + getDate() + " " + getClass().getName() + " attribute created");
	}
	
	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		 logger.info("Time " + getDate() + " " + getClass().getName() + " attribute replaced");
	}
	
	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		 logger.info("Time " + getDate() + " " + getClass().getName() + " attribute removed");
	}
	
	
	private String getDate() {
		formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:SS");
		return formatter.format(new Date());
	}
	
 }
