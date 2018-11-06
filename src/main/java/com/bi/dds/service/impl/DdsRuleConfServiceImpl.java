package com.bi.dds.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bi.dds.mapper.DdsRuleConfMapper;
import com.bi.dds.model.DdsDBConf;
import com.bi.dds.model.DdsRuleConf;
import com.bi.dds.model.DdsRuleConfExample;
import com.bi.dds.service.DdsRuleConfService;
import com.bi.dds.util.DBFactory2;
import com.bi.dds.util.StringUtil;

@Service(value = "ddsRuleConfService")
public class DdsRuleConfServiceImpl implements DdsRuleConfService {

	private static final Logger logger = Logger
			.getLogger(DdsRuleConfServiceImpl.class);

	@Autowired
	private DdsRuleConfMapper ddsRuleConfMapper;

	@Override
	public DdsRuleConf findDdsRuleById(String id) {
		return ddsRuleConfMapper.selectByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public int addDdsRuleConf(String loginId, DdsRuleConf info) {
		return ddsRuleConfMapper.insert(info);
	}

	@Override
	public int UpdateDdsRuleConf(String loginId, DdsRuleConf info) {
		return ddsRuleConfMapper.updateByPrimaryKey(info);

	}

	@Override
	public int DeleteDdsRuleById(String loginId, String id) {
		return ddsRuleConfMapper.DeleteLogicByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public List<DdsRuleConf> queryRuleConfList(String loginId,
			DdsRuleConfExample info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DdsRuleConf getRuleConfListSize(String loginId,
			DdsRuleConfExample info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int testSql(String sqlSentence) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<DdsRuleConf> queryRuleInfoList(String databaseId,
			String queryId, String subject, String sqlrule, String creatdate1,
			String creatdate2, String start, String limit) {

		Map<String, String> map = new HashMap<String, String>();

		map.put("databaseId", databaseId);
		map.put("queryId", queryId);
		map.put("subject", subject);
		map.put("sqlrule", sqlrule);
		map.put("creatdate1", creatdate1);
		map.put("creatdate2", creatdate2);
		map.put("start", start);
		map.put("limit", limit);

		return ddsRuleConfMapper.queryRuleInfoList(map);

	}
	
	public int countByQueryRuleInfoList(String databaseId,String queryId,String subject,String sqlrule,String creatdate1,String creatdate2,String start,String limit){
		
		Map<String, String> map = new HashMap<String, String>();

		map.put("databaseId", databaseId);
		map.put("queryId", queryId);
		map.put("subject", subject);
		map.put("sqlrule", sqlrule);
		map.put("creatdate1", creatdate1);
		map.put("creatdate2", creatdate2);
		map.put("start", start);
		map.put("limit", limit);
		
		return ddsRuleConfMapper.countByQueryRuleInfoList(map);
	}
	
	public int testSql(String sqlSentence,String databaseId){

		int result = 0;
		Connection conn = null;
		try {
			boolean b = checkSqlRule(sqlSentence);
			if(!b){
				result = -2;
			}else{
				conn = DBFactory2.getConnection(databaseId.trim());
				result = checkExpressionDataSource(sqlSentence,conn);
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.debug("跳出[SqlMgrItfImpl][testSql]");
		return result;
	
	}
	
	public int testSql(String sqlSentence,String sqlParam,String databaseId,DdsDBConf dbConf) throws Exception{
		int result = 0;
		Connection conn = null;
		try {
			sqlSentence = StringUtil.paramConvert(sqlSentence, sqlParam);
			boolean b = checkSqlRule(sqlSentence);
			if(!b){
				result = -2;
			}else{
				conn = DriverManager.getConnection(dbConf.getDbUrl(),dbConf.getDbUser(),dbConf.getDbPassword());
				result = checkExpressionDataSource(sqlSentence,conn);
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			throw e;
		}
		logger.debug("跳出[SqlMgrItfImpl][testSql]");
		return result;
	}
	
	public static boolean checkSqlRule(String sql) {
		//只允许执行Select语句
		if (sql.toLowerCase().startsWith("select")) {
			/*CCJSqlParserManager parserManager = new CCJSqlParserManager();
			try {
				PlainSelect plainSelect = (net.sf.jsqlparser.statement.select.PlainSelect)((Select) parserManager.parse(new StringReader(sql))).getSelectBody();
				return true;
			} catch (JSQLParserException e) {
				return false;
			}*/
			return true;
		}
		return false;
	}
	

	/**
	 * 对Sql语句进行数据库访问校验
	 * @param list
	 * @param expression
	 * @return
	 */
	public static int checkExpressionDataSource(String sql,Connection conn) throws Exception{
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		sql = "select count(1) from ( "+sql+" ) test";
		sql = sql.toLowerCase().replaceAll("order\\s+by\\s[^\\)]*$", "");		//替换掉所有的order by 加快执行速度
		sql = sql.toLowerCase().replaceAll(";", "");		//替换掉所有的order by 加快执行速度
		sql = StringUtil.convert(sql);
		int fr = -1;
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery(); 
			while(rs.next()){
				String thers = rs.getString(1);
				fr = 1;
			}
			return fr;
			
		} catch(Exception e) {
			logger.error(e);
			throw e;
			//System.out.println(e.getMessage());
			//return -1;
		}finally{
			try{	
				if(rs!=null)
					rs.close();
				if (psmt != null)
					psmt.close();
				if(conn != null){
						conn.close();
				}
				 
			} catch (Exception e)
			{
				logger.error(e);
				e.printStackTrace();
			}
		}
	}
}
