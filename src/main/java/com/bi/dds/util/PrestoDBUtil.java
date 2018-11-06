package com.bi.dds.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class PrestoDBUtil {
	private static Logger log =Logger.getLogger(PrestoDBUtil.class);
	
	
	public static void main(String[] args) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:presto://192.168.16.94:8090/hive/dw","hadoop","hadoop");  
        connection.setCatalog("hive");  
        Statement stmt = connection.createStatement();  
        String sql = "select  date_id,month_of_year from  test.dim_date a"
        		+ " where a.date_id in ('2015-06-01', '2015-09-15')";
  
        
        
        ResultSet rs = stmt.executeQuery(sql); 
       
//      ResultSet rs = stmt.executeQuery("select count(1) from dw.dim_city");
        
        ResultSetMetaData rss = rs.getMetaData();
        
        while (rs.next()) {  
      	  
      	  System.out.println(rss.getColumnName(1));  
            System.out.println(rs.getString(1));   
            System.out.println(rss.getColumnName(2));  
            System.out.println(rs.getString(2));
 
        }  
        
        
        rs.close();  
        connection.close();  
	}
	
	
	public static  List<String[]> prestoExecute(String sql, String[] heads, String diver, String url, String user, String password) {
		
		List<String[]> resultList = new ArrayList<String[]>();
		log.info("执行presto sql:" + sql);
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName(diver);
			connection = DriverManager.getConnection(url,user,password);
			connection.setCatalog("hive");  
	        stmt = connection.createStatement();  
	        rs = stmt.executeQuery(sql); 
	        
	        ResultSetMetaData rss = rs.getMetaData();
	        
	        int columnNum = rss.getColumnCount();
	        
	        if(heads == null){
	        	heads = new String[columnNum];
	        	 for(int i = 0 ;i < columnNum ; i ++){
	 	        	heads[i] = rss.getColumnName(i + 1);
	 	      	}
	        	 resultList.add(heads);
	        }
	        while (rs.next()) { 
	        	String[] body = new String[columnNum];
	        	for(int i = 0 ;i < columnNum ; i ++){
	        		body[i] = 	rs.getString(i + 1);
	      	  	}
	        	resultList.add(body);
	        }  
	        
	        rs.close();  
	        connection.close();  
			
			
		} catch (Exception e) {
			log.error(e);
			if(rs != null ){
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}  
			}
			if(connection != null ){
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}  
			}
		}
		
		return resultList;
		
	}

}
