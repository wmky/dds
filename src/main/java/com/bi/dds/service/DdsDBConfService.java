package com.bi.dds.service;

import java.util.List;

import com.bi.dds.model.DdsDBConf;

public interface DdsDBConfService {
	
	public int addDdsDBConf(String loginId,DdsDBConf info);
	
	public int updateDdsDBConf(String loginId,DdsDBConf info);
	
	public DdsDBConf findDdsDBConf(String loginId,String id);

	public int delDdsDBConfById(String loginId,String id);
	
	public List<DdsDBConf> selectAllAvildDBConfs();

	public DdsDBConf findMailConfById(String id);
	
	public List<DdsDBConf> queryDBInfoList(String queryId,String connName,String dbUrl, String start, String limit);
	
	public int countDBInfoList(String queryId,String connName,String dbUrl);
//
//	public Object getMailInfoListSize(String loginId, String sendto,
//			String subject, String ruleIdStr, String queryId, String isAvild,
//			String date);


}
