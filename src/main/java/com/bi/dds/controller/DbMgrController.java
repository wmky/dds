package com.bi.dds.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bi.dds.model.DdsDBConf;
import com.bi.dds.service.DdsDBConfService;

@Controller
@RequestMapping(value="/dbmgr")
public class DbMgrController {
	private static final Logger logger = Logger.getLogger(DbMgrController.class);
	
	@Autowired
	private DdsDBConfService ddsDBConfService;
	
	@RequestMapping(value="/init")
	public ModelAndView init(){
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("sql/db_mgr");
		return mav;
	}

//	queryDbInfo
	
	@RequestMapping("/queryDbInfo")
	@ResponseBody
	public Map<String,Object> queryDbInfo(String query_id,String query_dbname,String query_dburl,String start,String limit){
		
		int count = ddsDBConfService.countDBInfoList(query_id, query_dbname, query_dburl);
		Map<String,Object> map = new HashMap<String, Object>();
		List<DdsDBConf> list = new ArrayList<DdsDBConf>();
		if(count > 0){
			 list = ddsDBConfService.queryDBInfoList(query_id, query_dbname, query_dburl, start, limit);
		}
		
		map.put("totalCount", count);
		map.put("rows", list);
		return map;
	}
	@RequestMapping("/addOrEditDb")
	@ResponseBody
	public Map<String,Object> addOrEditDb(String id, String connname,String dbip,String dbport,String dbname,String username,String password,String databaseId){
		logger.info("addOrEditDb");
		Map<String,Object> map = new HashMap<String, Object>();
		DdsDBConf dbConf = new DdsDBConf();
		try {
			if(id == null || "".equals(id)){
				if("mysql".equals(databaseId)){
					dbConf.setDbUrl("jdbc:mysql://"+dbip+":"+dbport+"/" + dbname);
					dbConf.setConnName(connname);
					dbConf.setDbDriver("com.mysql.jdbc.Driver");
					dbConf.setDbUser(username);
					dbConf.setDbPassword(password);
					
					dbConf.setDbHost(dbip);
					dbConf.setDbPort(dbport);
					dbConf.setDbType(databaseId);
					dbConf.setDbName(dbname);
				}else{
					dbConf.setDbUrl("error connname");
				}
				
				ddsDBConfService.addDdsDBConf("", dbConf);
			}else{
				dbConf = ddsDBConfService.findDdsDBConf("", id);
				if("mysql".equals(databaseId)){
					dbConf.setDbUrl("jdbc:mysql://"+dbip+":"+dbport+"/" + dbname);
					dbConf.setConnName(connname);
					dbConf.setDbDriver("com.mysql.jdbc.Driver");
					dbConf.setDbUser(username);
					dbConf.setDbPassword(password);
					
					dbConf.setDbHost(dbip);
					dbConf.setDbPort(dbport);
					dbConf.setDbType(databaseId);
					dbConf.setDbName(dbname);
				}else{
					dbConf.setDbUrl("error connname");
				}
				ddsDBConfService.updateDdsDBConf("", dbConf);
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("testConn")
	@ResponseBody
	public Map<String,Object> testConn(String id, String connname,String dbip,String dbport,String dbname,String username,String password,String databaseId){
		Map<String,Object> map = new HashMap<String, Object>();
		if("mysql".equals(databaseId)){
			String dbUrl = "jdbc:mysql://"+dbip+":"+dbport+"/" + dbname;
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(dbUrl,username,password);
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}finally{
				if(conn == null){
					map.put("success",false);
				}else {
					try {
						map.put("success",true);
						conn.close();
					} catch (SQLException e) {
						logger.error(e);
					}
				}
			}
		}
		return map;
	}
	
	@RequestMapping("queryAllDbList")
	@ResponseBody
	public Map<String,Object> queryAllDbList(String id, String connname,String dbip,String dbport,String dbname,String username,String password,String databaseId){
		logger.info("queryAllDbList");
		Map<String,Object> map = new HashMap<String, Object>();
		
		List<DdsDBConf> list = ddsDBConfService.selectAllAvildDBConfs();
		map.put("totalCount", list.size());
		map.put("rows", list);
		
		return map;
	}
}
