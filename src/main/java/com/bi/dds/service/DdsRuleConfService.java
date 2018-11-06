package com.bi.dds.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bi.dds.model.DdsDBConf;
import com.bi.dds.model.DdsMailConf;
import com.bi.dds.model.DdsRuleConf;
import com.bi.dds.model.DdsRuleConfExample;

public interface DdsRuleConfService {
	
	public int testSql(String sqlSentence);
	
	public DdsRuleConf findDdsRuleById(String loginId);
	
	public int addDdsRuleConf(String loginId, DdsRuleConf info);
	
	public int UpdateDdsRuleConf(String loginId, DdsRuleConf info); 
	
	public int DeleteDdsRuleById(String loginId, String id);
	
	public List<DdsRuleConf> queryRuleConfList(String loginId, DdsRuleConfExample info);


	public DdsRuleConf getRuleConfListSize(String loginId, DdsRuleConfExample info);
	
	public int countByQueryRuleInfoList(String databaseId,String queryId,String subject,String sqlrule,String creatdate1,String creatdate2,String start,String limit);
	
	public List<DdsRuleConf> queryRuleInfoList(String databaseId,String queryId,String subject,String sqlrule,String creatdate1,String creatdate2,String start,String limit) ;
	
	
	public int testSql(String sqlSentence,String databaseId);
	
	public int testSql(String sqlSentence,String sqlParam,String databaseId,DdsDBConf dbConf) throws Exception;
//	public List<DdsRuleConf> queryRuleConfList(String loginId, String sendto,
//			String subject, String sqlrule, String creatdate1,
//			String creatdate2, String start, String limit, String queryId);
//
//
//	public DdsRuleConf getRuleConfListSize(String loginId, String sendto,
//			String subject, String sqlrule, String creatdate1);
	
	
//	public int bathExeSql(DdsRuleConf bean);


//	public List<DdsRuleConf> getAllDdsRuleConf(int sTART_RULE);

}
