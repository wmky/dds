package com.bi.dds.util;

import java.sql.Connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;



/**
 * 采用dbcp连接池
 * @author yhd2
 *
 */
public class DBFactory2
{
	private static final Logger logger = Logger.getLogger(DBFactory2.class);
	private static BasicDataSource dbcpds = null;
	private static BasicDataSource tdds = null;
	
	static{
		
		/*
		try {
			dbcpds = new BasicDataSource();
			dbcpds.setDriverClassName(GetDataBaseConnStr.getOraDriver());
			dbcpds.setUrl(GetDataBaseConnStr.getOraUrl());   
			dbcpds.setUsername(GetDataBaseConnStr.getOraUser());   
			dbcpds.setPassword(GetDataBaseConnStr.getOraPass());   
			dbcpds.setInitialSize(2);  
			dbcpds.setMinIdle(30);   
			dbcpds.setMaxActive(100);   
			dbcpds.setLogAbandoned(true);
			dbcpds.setRemoveAbandoned(false);
			dbcpds.setRemoveAbandonedTimeout(10000);
		} catch (Exception e2) {
			logger.error(e2);
			e2.printStackTrace();
		}*/
		 /*try{
	        	tdds = new BasicDataSource();
		        tdds.setDriverClassName(GetDataBaseConnStr.getTdDriver());
		        tdds.setUrl(GetDataBaseConnStr.getTdUrl());
		        tdds.setUsername(GetDataBaseConnStr.getTdUser());
		        tdds.setPassword(GetDataBaseConnStr.getTdPass());
		        tdds.setInitialSize(1);
		        tdds.setMaxActive(20);
		        tdds.setMaxIdle(5);
		        //tdds.setMaxWait(20);
		        tdds.setDefaultAutoCommit(true);
		        tdds.setRemoveAbandoned(true);
		        tdds.setRemoveAbandonedTimeout(120);
		        tdds.setValidationQuery("select count(1) from YHD_DW.DIM_B2B_REGION");
		        tdds.setTestOnBorrow(true);
		        tdds.setTestWhileIdle(true);
		        tdds.setTestOnReturn(true);
		        tdds.setLogAbandoned(true);
		        //tdds.setLoginTimeout(10);
//		        System.out.println("url:"+GetDataBaseConnStr.getTdUrl());
//	        	System.out.println("username:"+GetDataBaseConnStr.getTdUser());
//	        	System.out.println("pass:"+GetDataBaseConnStr.getTdPass());
//	        	System.out.println("driver:"+GetDataBaseConnStr.getTdDriver());
	        }catch(Exception ee){
//	        	System.out.println("url:"+GetDataBaseConnStr.getTdUrl());
//	        	System.out.println("username:"+GetDataBaseConnStr.getTdUser());
//	        	System.out.println("pass:"+GetDataBaseConnStr.getTdPass());
//	        	System.out.println("driver:"+GetDataBaseConnStr.getTdDriver());
//	        	System.out.println("td coonect start error");
	        	ee.printStackTrace();
	        }*/

	}
	
	
	public static Connection getConnection(String db) throws Exception{
		try{
			if (DataBaseSorts.MYSQL.equals(db)){
				/*System.out.println("jdbc connect 52!");
				Class.forName(GetDataBaseConnStr.getOraDriver());
			return DriverManager.getConnection(GetDataBaseConnStr.getOraUrl(),GetDataBaseConnStr.getOraUser(),GetDataBaseConnStr.getOraPass());*/
			return dbcpds.getConnection();
			}else if (DataBaseSorts.TD.equals(db)){
				//return dbcpds2.getConnection();
				System.out.println("jdbc connect td!");
					Class.forName(GetDataBaseConnStr.getTdDriver());
				return null;
//						DriverManager.getConnection(GetDataBaseConnStr.getTdUrl(),GetDataBaseConnStr.getTdUser(),GetDataBaseConnStr.getTdPass());
				/*System.out.println("dbcp connect td");*/
			//return tdds.getConnection();
				//return null;
			} 
		}
		catch(Exception e){
			logger.error(e);
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
	/**
 	 * 
 	 *
 	 *@param conn Connection  
 	 */
	public static void close(Connection conn){
		try{
			if(conn != null&&!conn.isClosed())
				conn.close();
		}
		catch(Exception e){
			logger.error(e);
			e.printStackTrace();	
		}
	}
	
}