package com.bi.dds.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class GetProDatas {
	public static final String configFile = ClassesLocation.getLocation()
	+ File.separator
	+ "user_info.properties".replace('/', File.separatorChar);
	
	public static Properties p = new Properties();
	static {
		try {
			p.load(new FileInputStream(configFile));
		} catch (Exception e) {
		//	System.out.println("无法读取配置文件路径");
			e.printStackTrace();
		}
	}
	public static String getUserName() {
		return p.getProperty("user_name", "");
	}
	public static String getPass() {
		return p.getProperty("user_pass", "");
	}
	public static String getUserName2() {
	    return p.getProperty("user_name2", "");
	}
	public static String getPass2() {
	    return p.getProperty("user_pass2", "");
	}

	public static String getDept() {
		return p.getProperty("user_dept", "");
	}
	public static String getEmail() {
		return p.getProperty("user_email", "");
	}
	
	public static String getSkype() {
		return p.getProperty("user_skype", "");
	}
	public static String getTel() {
		return p.getProperty("user_tel", "");
	}
	
	public static String getMobile() {
		return p.getProperty("user_mobile", "");
	}
	public static String getUserId() {
		return p.getProperty("user_id", "");
	}
	public static String getMailUrl() {
		return p.getProperty("mail_url", "");
	}
	public static String getMailUser() {
		return p.getProperty("mail_usr", "");
	}
	public static String getMailPass() {
		return p.getProperty("mail_pass", "");
	}
	public static String getMailCommand1() {
		return p.getProperty("mail_command1", "");
	}
	public static String getMailCommand2() {
		return p.getProperty("mail_command2", "");
	}
	public static String getMailCommand3() {
		return p.getProperty("mail_command3", "");
	}
	public static String getCsvFilePath(){
		return p.getProperty("csv_filepath","");
	}
	public static String getXlsFilePath(){
		return p.getProperty("xls_filepath","");
	}
	public static String getCc() {
		// TODO Auto-generated method stub
		return p.getProperty("mail_cc","");
	}
	public static String getWrongMail() {
		// TODO Auto-generated method stub
		return p.getProperty("mail_wrong","");
	}
	
	public static String getDd() {
		// TODO Auto-generated method stub
		return p.getProperty("mail_dd","");
	}
	public static String getMailFrom() {
		// TODO Auto-generated method stub
		return p.getProperty("mail_from","");
	}
	public static String getCodeSet() {
		// TODO Auto-generated method stub
		return p.getProperty("code_set","");
	}
	public static String getDaysFileDelete() {
        return p.getProperty("days_file_delete","");
    }
	public static String getDeleteCron() {
	    return p.getProperty("delete_cron","");
	}
	public static String getEtlUrl() {
        return p.getProperty("etl_url", "");
    }
    public static String getEtlUser() {
        return p.getProperty("etl_usr", "");
    }
    public static String getEtlPass() {
        return p.getProperty("etl_pass", "");
    }
    public static String getEtlMailFolder() {
        return p.getProperty("etl_mail_folder", "");
    }
    public static String getEtlHistoryFolder() {
        return p.getProperty("etl_history_folder", "");
    }
    public static String getEtlCrontab() {
        return p.getProperty("etl_crontab", "");
    }
    public static String getSmsLevel() {
        return p.getProperty("sms_level", "");
    }
    public static String getSmsSource() {
        return p.getProperty("sms_source", "");
    }
	public static void main(String[] args) {
//		System.out.println(configFile);
//		System.out.println(ClassesLocation.getLocation());
//		System.out.println(File.separator);
//		System.out.println("user_info.properties".replace('/', File.separatorChar));
		System.out.println(getSsh_needKey());
	}
    
	
	public static String getSsh_host() {
        return p.getProperty("ssh_host", "192.168.19.17");
    }
    public static int getSsh_port() {
        return Integer.parseInt(p.getProperty("ssh_port", "22"));
    }
    public static String getSsh_username() {
        return p.getProperty("ssh_username", "hadoop");
    }
	
    public static String getSsh_needKey() {
        return p.getProperty("ssh_needKey", "yes");
    }
}

