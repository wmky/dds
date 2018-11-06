package com.bi.dds.service;

import java.util.List;

import com.bi.dds.model.DdsMailConf;
import com.bi.dds.model.DdsMailConfExample;

public interface DdsMailConfService {
	
	public int addDdsMailConf(String loginId,DdsMailConf info);
	
	public int updateDdsMailConf(String loginId,DdsMailConf info);
	
	public List<DdsMailConf> queryDdsMailConfList(String loginId, DdsMailConfExample info);
		
	public int getDdsMailConfListSize(String loginId, DdsMailConfExample info);
	
	public DdsMailConf findDdsMailConf(String loginId,String id);

	public int delDdsMailConfById(String loginId,String id);
	
	public List<DdsMailConf> selectAllAvildMailConfs();

	public DdsMailConf findMailConfById(String id);

	
	public List<DdsMailConf> queryMailInfoList(String queryId,String sendto,String isAvild,
			String subject, String ruleIdStr, String start, String limit,
			  String date,String isSuccess);
	
	public int countMailInfoList(String queryId,String sendto,String isAvild,
			String subject, String ruleIdStr, String start, String limit,
			  String date,String isSuccess);
//
//	public Object getMailInfoListSize(String loginId, String sendto,
//			String subject, String ruleIdStr, String queryId, String isAvild,
//			String date);


}
