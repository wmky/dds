package com.bi.dds.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.FileWriter;
import java.io.BufferedWriter;

public class HiveShellUtil {
	public static String kk_log_dir="/home/hadoop/apache_projects/tomcat-dds/logs_sqlid/";
	// 注意前提是路径一定要已经存在,否则会报错--数据结果为空

	private static Logger log =Logger.getLogger(HiveShellUtil.class);
	
	public static  List<String[]> sshHive(String sql,String sqlid) {

		List<String[]> resultList = new ArrayList<String[]>();
		JSch jsch = new JSch();
		Session session = null;
		Channel channel = null;
		boolean isBlank = true;
		StringBuffer errorlog = new StringBuffer();
        OutputStream out = null;
        
		try {
			if(GetProDatas.getSsh_needKey().equals("true")){
				String identitypath = ClassesLocation.getLocation()+ File.separator+ "Identity".replace('/', File.separatorChar);
				log.info("identitypath:"+identitypath);
				jsch.addIdentity( identitypath);
			}
			
			System.out.println(GetProDatas.getSsh_host());
			
			
		
			BufferedReader reader = null;
			session = jsch.getSession(GetProDatas.getSsh_username(), GetProDatas.getSsh_host(), GetProDatas.getSsh_port());
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect(30000);
			channel = session.openChannel("exec");
			
			sql = SqlDateFormatUtils.parseCommand(sql);
//			String cmd = "hive -S -i /home/hadoop/juanpi_workspace/software/conf/hive/init.hql  -e " + "\"" + sql + "\"";
			String cmd = "hive -e " + "\"" + sql + "\"";
//			改写log4j打印输出过程.
			log.info("Hql query:"+cmd);
			
			((ChannelExec) channel).setCommand(cmd);
			channel.setInputStream(null);
			
			File file = new File(kk_log_dir+"sqlid_"+sqlid);
			
			// 若存在创建则直接使用,若不存在则创建.
			if(!file.exists()){
				file.createNewFile();
			}
			 System.out.println(file.getAbsolutePath());
			 FileWriter fileWritter = new FileWriter(file.getAbsoluteFile(),true);
    		 System.out.println(file.getAbsoluteFile());
             BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
             bufferWritter.write(cmd);
             bufferWritter.write("\n");
             bufferWritter.close();
 
             System.out.println("Done");
			
            File file1 = new File(kk_log_dir+"sqlid_"+sqlid+"tmp");
			out = new FileOutputStream(file1);
			((ChannelExec) channel).setErrStream(out);
			
			
//			((ChannelExec) channel).setErrStream(System.err);  
//			从console输出hive执行日志
			channel.connect();
			InputStream in = channel.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in,Charset.forName("UTF-8")));
	
			String buf = null;
			while ((buf = reader.readLine()) != null) {
				//此处只是为了过滤结果集中带了WARN的信息.
			     
				
				if(buf.contains("WARN")){
					errorlog.append(buf).append("\n");
					continue;
				}
				isBlank = false;
				// 此处的buf就是Hql query的结果集
				log.info(buf);
				String[] results = buf.split("\\t+");
				resultList.add(results);
				
			}
			try{
				  final String fromFile =kk_log_dir+"sqlid_"+sqlid+"tmp";
				  final String toFile=kk_log_dir+"sqlid_"+sqlid;
				  BufferedReader read=new BufferedReader(new FileReader(new File(fromFile)));
				  FileWriter write = new FileWriter(new File(toFile),true);
				  String temp;
				  while((temp = read.readLine()) != null){
					write.write(temp);
					write.write("\n");
				  }
				  write.close();
				  read.close();
				  System.out.println("already copy");
				  } catch(IOException e){
					e.printStackTrace();
				}
			if(file1.exists())
				file1.delete();
		} catch (JSchException e) {
			log.error("连接到hive服务器失败，identity error",e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("连接到hive服务器失败，io error",e);
			e.printStackTrace();
		} finally {
			
			try {
				if(out != null){
					 out.close();
				}
            } catch (IOException e) {
                log.error("",e);
            }
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
 		
			
			
		}
		System.out.println(isBlank);
		if(isBlank){
			log.error("执行hql语句" + sql + "为空，请检查原因:" + errorlog.toString());
			// log.error的捕捉日志会在log4j配置的地方找到.
			return null;
		}
		return resultList;
	}
		public static void main(String[] args) throws JSchException, IOException {
			
			List<String[]> results = sshHive("select page_id,page_name from dw.dim_page where page_id=250","100");
			String[] heads = {"page_id","page_name"};
//			ExcelUtil.parse(results, heads,"2016/dim_page.xlsx","dim_page","2016");
			System.out.println(1298877/7-201341);
			
		}

}
