package com.bi.dds.util;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHelper {
	static Map<Integer,String> errorMap = new HashMap<Integer, String>();
	
	static{
		int i = -1;
		errorMap.put(i++, "abc");
		errorMap.put(i++, "def");
		
	}
	
	public static String getExceptionReason(String errorCode){
		return errorMap.get(Integer.parseInt(errorCode));
	}
}
