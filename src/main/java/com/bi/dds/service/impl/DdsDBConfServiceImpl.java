package com.bi.dds.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bi.dds.mapper.DdsDBConfMapper;
import com.bi.dds.model.DdsDBConf;
import com.bi.dds.service.DdsDBConfService;

@Service(value = "ddsDBConfService")
public class DdsDBConfServiceImpl implements DdsDBConfService {

	private static final Logger logger = Logger.getLogger(DdsDBConfServiceImpl.class);
	
	@Autowired
	private DdsDBConfMapper ddsDBConfMapper;

	@Override
	public int addDdsDBConf(String loginId, DdsDBConf info) {
		return ddsDBConfMapper.insert(info);
	}

	@Override
	public int updateDdsDBConf(String loginId, DdsDBConf info) {
		return ddsDBConfMapper.updateByPrimaryKey(info);
	}



	@Override
	public DdsDBConf findDdsDBConf(String loginId, String id) {
		return ddsDBConfMapper.selectByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public int delDdsDBConfById(String loginId, String id) {
		// TODO Auto-generated method stub
		return ddsDBConfMapper.deleteLogicByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public List<DdsDBConf> selectAllAvildDBConfs() {
		return ddsDBConfMapper.selectAllAvildDBConfs();
	}

	@Override
	public DdsDBConf findMailConfById(String id) {
		// TODO Auto-generated method stub
		return ddsDBConfMapper.selectByPrimaryKey(Integer.parseInt(id));
	}

	
	public List<DdsDBConf> queryDBInfoList(String queryId,String connName,String dbUrl, String start, String limit){
		Map<String,String> map = new HashMap<String, String>();
		map.put("queryId", queryId);
		map.put("connName", connName);
		map.put("dbUrl", dbUrl);
		map.put("start", start);
		map.put("limit", limit);
		
		return ddsDBConfMapper.queryDBInfoList(map);
	}
	
	public int countDBInfoList(String queryId,String connName,String dbUrl){
		Map<String,String> map = new HashMap<String, String>();
		map.put("queryId", queryId);
		map.put("connName", connName);
		map.put("dbUrl", dbUrl);
		
		return ddsDBConfMapper.countDBInfoList(map);
	}
	
}
