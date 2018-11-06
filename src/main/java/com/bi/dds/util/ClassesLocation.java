package com.bi.dds.util;

import java.io.File;

public class ClassesLocation {
	private static String classesPath=null;
	/**
	 * @param args
	 */
	public static synchronized String getLocation(){
		if(classesPath!=null) return classesPath;		
		classesPath=calLocation();
		return classesPath;
	}
	public static String calLocation(){		
		String classesPath=new ClassesLocation().getClass().getResource("ClassesLocation.class").getPath();
		if(classesPath.toUpperCase().indexOf("/WEB-INF/")>0){
			//webroot
			classesPath=classesPath.substring(0,classesPath.toUpperCase().indexOf("/WEB-INF/"));
			File file=new File(classesPath);
			if(file.exists() && file.getParent()!=null){
				//beside of webroot
				classesPath= file.getPath()+file.separator+"WEB-INF/classes/properties";
			}
			file=new File(classesPath);
			if(!file.exists() ){
				String configFile = System.getProperty("user.dir")+ File.separator+ "WEB-INF/classes/properties";
				file=new File(configFile);
				if(file.exists()){
					classesPath=configFile;
				}
			}
		}
		else if(classesPath.indexOf("classes/")>0){
			classesPath=classesPath.substring(0,classesPath.indexOf("classes/")+"classes/".length());
			classesPath= classesPath+File.separator+"db";
		}else if(classesPath.indexOf("/lib/")>0){
			classesPath=classesPath.substring(0,classesPath.indexOf("/lib/")+"/lib/".length());
		}
		return classesPath;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(ClassesLocation.getLocation());
	}

}
