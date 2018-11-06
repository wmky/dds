package com.bi.dds.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

import org.apache.log4j.Logger;

import com.bi.dds.scheduler.ScheduleConstants;

public class JdbcDao {
	
	/*
	 * 查找执行状态
	 */
	private static final Logger logger = Logger.getLogger(JdbcDao.class);
	
	public String localIPAdd(){
		InetAddress addr;
		
		String ip = "";
		//windows下获取IP地址  如下代码就够用了
//		try {
//			addr = InetAddress.getLocalHost();
//			ip = addr.getHostAddress().toString();//获得本机IP
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//Linux下获取IP地址
		Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();   
		    while (interfaces.hasMoreElements()) {   
		        NetworkInterface ni = interfaces.nextElement();   
		        Enumeration<InetAddress> ips = ni.getInetAddresses();   
		        while (ips.hasMoreElements()) {
		           String ipWhole = ips.nextElement().getHostAddress().toString();
		           String ipHead = ipWhole.substring(0,3);
		           if("10.".equals(ipHead)){
		        	   ip = ipWhole;
		        	   return ip;
		           }
		        }
		    }   
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return ip;
	}
	
	public String getPlanTableName(){
		String ip = localIPAdd();
		String tablename = "";
//		if(ip == "10.4.0.11"||"10.4.0.11".equals(ip)){
//			tablename = "ORA_DQC_PLAN_LARGE";
//		}else if (ip == "10.1.0.179"||"10.1.0.179".equals(ip)){
			tablename = "dds_exec_plan";
//		}
		//本地测试
//		else if (ip == "10.161.165.34"||"10.161.165.34".equals(ip)){
//			tablename = "ORA_DQC_PLAN_NEW";
//		}
//		else {
//			tablename = "ORA_DQC_PLAN_LARGE";
//		}
		return tablename;
	}
	public String getMailTableName(){
		String ip = localIPAdd();
		String tablename = "";
//		if(ip == "10.4.0.11"|| "10.4.0.11".equals(ip)){
//			tablename = "ORA_DQC_MAIL_LARGE";
//		}else if (ip == "10.1.0.179" ||"10.1.0.179".equals(ip)){
			tablename = "dds_main_conf";
//		}
		//本地测试
//		else if (ip == "10.161.165.34"||"10.161.165.34".equals(ip)){
//			tablename = "DDS_mAIL_CONF";
////		}
//		else {
//			tablename = "ORA_DQC_MAIL_LARGE";
//		}
		return tablename;
	}
	//获取已经完成的作业
	public String findDoneJobID() throws Exception{
		String sql = "with aaa(id, "
			+"ETL_System, "
			+"ETL_Job, "
			+"last_jobstatus, "
			+"Last_TXDate, "
			+"last_send_date ) as "
			+"(SELECT a.report_id AS id, "
			+"c.ETL_System, "
			+"c.ETL_Job, "
			+"c.last_jobstatus, "
			+"c.Last_TXDate, "
			+"a.last_send_date "
			+"FROM ETL.Report_Table_Mapping a "
			+"INNER JOIN ETL.etl_table_mapping b "
			+"ON a.Table_Schema = b.Table_Schema "
			+"and a.TABLE_NAME = b.TABLE_NAME "
			+"INNER JOIN ETL.ETL_Job c "
			+"ON b.ETL_System=c.ETL_System "
			+"and b.ETL_Job = c.ETL_Job) "
			+"SELECT distinct id "
			+"FROM aaa aa "
			+"WHERE aa.id NOT IN( SELECT bb.id "
			+"FROM aaa bb "
			+"WHERE bb.last_jobstatus <> 'Done' OR last_send_date >= date OR last_txdate < date)" 
			+"And aa.id in ("+findAllMailBeanId2()+")" 
			+"sample 1 ";
		System.out.println(sql);
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		String id = "";
		try {
//			con = DBFactory2.getConnection(DataBaseSorts.TD);
//			con.setAutoCommit(false);
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(sql.toString());
//			// int indexNo = 1;
//			while (rs != null && rs.next()) { 
//				id = rs.getString("id");
//			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if(stmt != null){
				stmt.close();
			}
			if (con != null)
				con.close();
		}
		return id;
	}
	
	public void updateLastSendDate(String id) throws Exception{
		String sql = "update etl.report_table_mapping set last_send_date = date where report_id = " + id;
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBFactory2.getConnection(DataBaseSorts.TD);
			con.setAutoCommit(false);
			stmt = con.createStatement();
			stmt.executeUpdate(sql.toString());
			System.out.println(sql.toString());
			con.commit();
			// int indexNo = 1;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			
			if (con != null)
				con.close();
		}
	}
	
	
	public boolean findStatus(long id) throws Exception{
	 
	String sql = " select Last_JobStatus from ETL.ETL_JOB ej, ETL.ETL_Table_Mapping etm ,ETL.Report_Table_Mapping rtm " +
                 " where ej.etl_job = etm.etl_job and upper(ej.ETL_System) = upper(etm.ETL_System) and upper(etm.Table_Name) = upper(rtm.Table_Name) " +
	             " and Last_TXDate = date and rtm.report_id = " +id;
	
	ResultSet rs = null;
	Connection con = null;
	Statement stmt = null;
	boolean flag = true;
	try {
		con = DBFactory2.getConnection(DataBaseSorts.TD);
		con.setAutoCommit(false);
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql.toString());
		System.out.println(sql.toString());
        
		while (rs != null && rs.next()) { 
			System.out.println(rs.getString("Last_JobStatus"));
			if(!"Done".equals(rs.getString("Last_JobStatus")))  {
				flag = false;
			}
		
		}
	  } catch (Exception e) {
		  logger.error(e);
		e.printStackTrace();
		throw e;
	   } finally {
		if (rs != null)
			rs.close();
		if(stmt != null){
			stmt.close();
		}
		if (con != null)
			con.close();
	    }
	    return flag;
	 }
	public void updateMail(String id) throws Exception{
		String sql = " update "+getMailTableName()+" set is_done = 1 where id in ("+id+")";
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBFactory2.getConnection(DataBaseSorts.MYSQL);
			con.setAutoCommit(false);
			stmt = con.createStatement();
			stmt.executeUpdate(sql.toString());
			System.out.println(sql.toString());
			con.commit();
			// int indexNo = 1;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			
			if (con != null)
				con.close();
		}
	}
	
	
	public void updateAll() throws Exception{
		String sql = "  update "+getMailTableName()+" m set m.is_done= 0 , m.is_success = 0 where m.is_automation = 1  and rule_table is not null";
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBFactory2.getConnection(DataBaseSorts.MYSQL);
			con.setAutoCommit(false);
			stmt = con.createStatement();
			stmt.executeUpdate(sql.toString());
			con.commit();
			// int indexNo = 1;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			
			if (con != null)
				con.close();
		}
	}
	
	
	
	/**
	 * 根据JOBID更新郵件開始時間 
	 * @param id
	 * @return OraDqcRule
	 * @throws Exception 
	 * @throws Exception
	 */
	
	public void updateStartMail(long id) throws Exception{
		String sql = " update "+getMailTableName()+" set start_time = sysdate(), is_success=1 where id = " + id;
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBFactory2.getConnection(DataBaseSorts.MYSQL);
			stmt = con.createStatement();
			stmt.executeUpdate(sql.toString());
			con.commit();
			// int indexNo = 1;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			
			if (con != null)
				con.close();
		}
	}
	/**
	 * 根据JOBID更新郵結束時間 
	 * @param id
	 * @return OraDqcRule
	 * @throws Exception 
	 * @throws Exception
	 */
	
	public void updateEndMail(long id) throws Exception{
		String sql = " update "+getMailTableName()+" set end_time = sysdate() , is_success=2 where id = " + id;
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBFactory2.getConnection(DataBaseSorts.MYSQL);
			stmt = con.createStatement();
			stmt.executeUpdate(sql.toString());
			con.commit();
			// int indexNo = 1;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			
			if (con != null)
				con.close();
		}
	
	
}
	/**
	 * 根据oracle數
	 * @param id
	 * @return OraDqcRule
	 * @throws Exception 
	 * @throws Exception
	 */
	public String findAllMailBeanId() throws Exception{
		
		String sql = "select @x:=ifnull(@x,0)+1 as rownum,id from "+getMailTableName()+" where is_avild = 1 and is_deleted = 0 "+
                     "and (trim(substr(CRONTAB, length(CRONTAB) - 1)) = '?' or  trim(regexp_replace(crontab, '(^[0-9]+ +[0-9]+ +)([0-9*?]+)(.*$)',  '\\2')) = '*'  or "+
                     " trim(substr(CRONTAB, length(CRONTAB) - 1)) = to_char(sysdate(), 'd')  " +
                     " or trim(regexp_replace(crontab, '(^[0-9]+ +[0-9]+ +)([0-9*?]+)(.*$)', '\\2')) = to_char(to_number(to_char(sysdate(), 'dd'))))" +
                     " and is_automation = 1 and is_success = 0 and is_done = 0 and rownum <= 1";
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		String id = "";
		try {
			con = DBFactory2.getConnection(DataBaseSorts.MYSQL);
			
			con.setAutoCommit(false);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());
			System.out.println(sql.toString());
	        
			// int indexNo = 1;
			while (rs != null && rs.next()) { 
				id += rs.getLong("ID") + ",";
				/*bean.setExcelName(rs.getString("EXCEL_NAME"));
				bean.setSheetName(rs.getString("SHEET_NAME"));*/
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if(stmt != null){
				stmt.close();
			}
			if (con != null)
				con.close();
		}
		String ids = "";
		System.out.println(id);
		if(id.length()>=1){
		 ids = id.substring(0, id.length()-1); 
		}
		return ids;
	}
	
public String findAllMailBeanId2() throws Exception{
		
		String sql = "select id from "+getMailTableName()+" where is_deleted = 0 ";
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		String ids = "";
		String id = "";
		try {
			con = DBFactory2.getConnection(DataBaseSorts.MYSQL);
			con.setAutoCommit(false);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());
			System.out.println(sql.toString());
			
			int i = 0;
			while (rs != null && rs.next()) { 
					id = rs.getString("ID");
				if(i==0){
					ids = id;
				}else{
					ids = ids + ", " + id;
				}
				i++;
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if(stmt != null){
				stmt.close();
			}
			if (con != null)
				con.close();
		}
		return ids;
	}
	
	public int  execPro(String sql,String db) throws Exception {
		 	int r = 0 ;
			Connection con = null;
			CallableStatement proc= null;
			try {
				con = DBFactory2.getConnection(db);
				con.setAutoCommit(true);
				String[] sqlArray = sql.split("/");
				String chashu ="?";
				
				for(int i = 0; i<sqlArray.length-2;i++){
					chashu += ",?";
				}
				String call = "{call "+sqlArray[0]+"("+chashu+")}"; 
				proc=con.prepareCall(call);
				java.util.Date curDate = new java.util.Date();
				java.sql.Date date = null;
				if(sqlArray.length>1){
				  date = new java.sql.Date(curDate.getTime()+1000*60*60*24*(Integer.valueOf(sqlArray[1])));
				}else{
				  date = new java.sql.Date(curDate.getTime());
				}
//				int index = 1;
//				proc.setDate(index++, date);
//				for(int i = 0; i<sqlArray.length-2;i++){
//					proc.setString(index++, sqlArray[sqlArray.length-1]);
//				}
				
				for(int i = 1; i<sqlArray.length;i++){
					proc.setString(i, sqlArray[i]);
				}
				if(sqlArray.length==1){
					proc.setDate(1, date);
				}
				proc.execute(); 
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
				r = ScheduleConstants.JOB_ERROR4;
			} finally {
				if (proc != null)
					proc.close();

				if (con != null)
					con.close();
	
			}
			return r;
	}
	
	public int  execProDymaic(String sql,String dbUrl,String username,String password) throws Exception {
	 	int r = 0 ;
		Connection con = null;
		CallableStatement proc= null;
		try {
			//con = DBFactory2.getConnection(db);
			con = DriverManager.getConnection(dbUrl,username,password);
			con.setAutoCommit(true);
			String[] sqlArray = sql.split("/");
			String chashu ="?";
			
			for(int i = 0; i<sqlArray.length-2;i++){
				chashu += ",?";
			}
			String call = "{call "+sqlArray[0]+"("+chashu+")}"; 
			proc=con.prepareCall(call);
			java.util.Date curDate = new java.util.Date();
			java.sql.Date date = null;
			if(sqlArray.length>1){
			  date = new java.sql.Date(curDate.getTime()+1000*60*60*24*(Integer.valueOf(sqlArray[1])));
			}else{
			  date = new java.sql.Date(curDate.getTime());
			}
//			int index = 1;
//			proc.setDate(index++, date);
//			for(int i = 0; i<sqlArray.length-2;i++){
//				proc.setString(index++, sqlArray[sqlArray.length-1]);
//			}
			
			for(int i = 1; i<sqlArray.length;i++){
				proc.setString(i, sqlArray[i]);
			}
			if(sqlArray.length==1){
				proc.setDate(1, date);
			}
			proc.execute(); 
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			r = ScheduleConstants.JOB_ERROR4;
		} finally {
			if (proc != null)
				proc.close();

			if (con != null)
				con.close();

		}
		return r;
}
	
}
