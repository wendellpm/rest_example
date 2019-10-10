package com.wpmassociates.comcast.utility;


import java.util.Map;
import java.util.Collection;

public class CheckNullEmpty {
	
	/**
	 * Utility class and method to check an object for null or empty
	 * First checks whether null. If not null checks
	 * for the most generic object types that have an isEmpty() method:
	 * String, Map, Collection.
	 * Finally checks whether the object is an array. Then if the array
	 * has at least one element.
	 *
	 * @param object any object provided for checking
	 * @return boolean true if the object provided is null or empty, false otherwise
	 */	
	public static boolean checkForNullOrEmpty(Object object) {
		
		boolean nullOrEmpty = true;
		String string;
		Map<?, ?> map;
		Collection<?> collection;
		
		if (object == null)
			return nullOrEmpty; 
		
		else if (object instanceof String) 
		{
			string = (String)object;
			if (string.isEmpty()) 
				return nullOrEmpty;									
			return false;				
		} 
		else if (object instanceof Map) 
		{				
			map = (Map<?,?>)object;
			if (map.isEmpty())
				return nullOrEmpty;
			return false;
		} 
		else if (object instanceof Collection) 
		{
			collection = (Collection<?>)object;
			if (collection.isEmpty()) 
				return nullOrEmpty;
			return false;				
		} 
		else 
		{
			if ((object.getClass()).isArray())
			{
				Object[] objectArray = (Object[])object;
				if (objectArray.length == 0)
					return nullOrEmpty;
				return false;
			}
		}
		return false;
	}
}
