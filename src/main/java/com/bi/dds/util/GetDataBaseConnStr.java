package com.bi.dds.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class GetDataBaseConnStr {
	public static final String configFile = ClassesLocation.getLocation()
	+ File.separator
	+ "db_info.properties".replace('/', File.separatorChar);
	public static Properties p = new Properties();
	static {
		try {
			p.load(new FileInputStream(configFile));
		} catch (Exception e) {
		//	System.out.println("无法读取配置文件路径");
			e.printStackTrace();
		}
	}
	public static String getOraUrl() {
		return p.getProperty("ora_url", "");
	}
	public static String getOraDriver() {
		return p.getProperty("ora_driver", "");
	}

	public static String getOraUser() {
		return p.getProperty("ora_user", "");
	}
	public static String getOraPass() {
		return p.getProperty("ora_password", "");
	}
	
	public static String getOra52Url() {
		return p.getProperty("ora52_url", "");
	}
	public static String getOra52Driver() {
		return p.getProperty("ora52_driver", "");
	}

	public static String getOra52User() {
		return p.getProperty("ora52_user", "");
	}
	public static String getOra52Pass() {
		return p.getProperty("ora52_password", "");
	}
	
	public static String getTdUrl() {
		return p.getProperty("td_url", "");
	}
	public static String getTdDriver() {
		return p.getProperty("td_driver", "");
	}
	
	public static String getTdUser() {
		return p.getProperty("td_user", "");
	}
	public static String getTdPass() {
		return p.getProperty("td_password", "");
	}
	public static String getOraUrlExe() {
		return p.getProperty("ora_url_exe", "");
	}
	public static String getOraDriverExe() {
		return p.getProperty("ora_driver_exe", "");
	}
	
	public static String getOraUserExe() {
		return p.getProperty("ora_user_exe", "");
	}
	public static String getOraPassExe() {
		return p.getProperty("ora_password_exe", "");
	}
	
	public static String getTdUrlExe() {
		return p.getProperty("td_url_exe", "");
	}
	public static String getTdDriverExe() {
		return p.getProperty("td_driver_exe", "");
	}
	
	public static String getTdUserExe() {
		return p.getProperty("td_user_exe", "");
	}
	public static String getTdPassExe() {
		return p.getProperty("td_password_exe", "");
	}
	public static void main(String[] args) {
		String s = getOraUrl();
	//	System.out.println(s);
	}
}
