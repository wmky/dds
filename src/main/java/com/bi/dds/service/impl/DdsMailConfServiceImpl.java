package com.bi.dds.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bi.dds.mapper.DdsMailConfMapper;
import com.bi.dds.model.DdsMailConf;
import com.bi.dds.model.DdsMailConfExample;
import com.bi.dds.service.DdsMailConfService;

@Service(value = "ddsMailConfService")
public class DdsMailConfServiceImpl implements DdsMailConfService {

	private static final Logger logger = Logger.getLogger(DdsMailConfServiceImpl.class);
	
	@Autowired
	private DdsMailConfMapper ddsMailConfMapper;

	@Override
	public int addDdsMailConf(String loginId, DdsMailConf info) {
		// TODO Auto-generated method stub
		return ddsMailConfMapper.insert(info);
	}

	@Override
	public int updateDdsMailConf(String loginId, DdsMailConf info) {
		// TODO Auto-generated method stub
		return ddsMailConfMapper.updateByPrimaryKey(info);
	}

	@Override
	public List<DdsMailConf> queryDdsMailConfList(String loginId,
			DdsMailConfExample info) {
		// TODO Auto-generated method stub
		return ddsMailConfMapper.selectByExample(info);
	}

	@Override
	public int getDdsMailConfListSize(String loginId, DdsMailConfExample info) {
		// TODO Auto-generated method stub
		return ddsMailConfMapper.countByExample(info);
	}

	@Override
	public DdsMailConf findDdsMailConf(String loginId, String id) {
		// TODO Auto-generated method stub
		return ddsMailConfMapper.selectByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public int delDdsMailConfById(String loginId, String id) {
		// TODO Auto-generated method stub
		return ddsMailConfMapper.deleteLogicByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public List<DdsMailConf> selectAllAvildMailConfs() {
		// TODO Auto-generated method stub
		DdsMailConfExample example = new DdsMailConfExample();
		example.createCriteria().andIsDeletedEqualTo(Integer.parseInt("0"));
		example.createCriteria().andIsAvildEqualTo(Integer.parseInt("1"));
		return ddsMailConfMapper.selectByExample(example);
	}

	@Override
	public DdsMailConf findMailConfById(String id) {
		return ddsMailConfMapper.selectByPrimaryKey(Integer.parseInt(id));
	}

	
	public List<DdsMailConf> queryMailInfoList(String queryId,String sendto,String isAvild,
			String subject, String ruleIdStr, String start, String limit,
			  String date,String isSuccess){
		Map<String,String> map = new HashMap<String, String>();
		map.put("queryId", queryId);
		map.put("sendto", sendto);
		map.put("isAvild", isAvild);
		map.put("subject", subject);
		map.put("ruleIdStr", ruleIdStr);
		map.put("start", start);
		map.put("limit", limit);
		map.put("date", date);
		map.put("isSuccess", isSuccess);
		
		return ddsMailConfMapper.queryMailInfoList(map);
	}
	
	public int countMailInfoList(String queryId,String sendto,String isAvild,
			String subject, String ruleIdStr, String start, String limit,
			  String date,String isSuccess){
		Map<String,String> map = new HashMap<String, String>();
		map.put("queryId", queryId);
		map.put("sendto", sendto);
		map.put("isAvild", isAvild);
		map.put("subject", subject);
		map.put("ruleIdStr", ruleIdStr);
		map.put("start", start);
		map.put("limit", limit);
		map.put("date", date);
		map.put("isSuccess", isSuccess);
		
		return ddsMailConfMapper.countMailInfoList(map);
	}
	
}
