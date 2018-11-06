package com.bi.dds.service;

import java.util.List;


import com.bi.dds.model.DdsExecLog;
import com.bi.dds.model.DdsMailConf;

public interface DdsExecLogService {

//	public int sendMail(String id, String mailstr);
	
	
//	public int sendMailDo(DdsMailConf bean,String mailstr);
	
//	/**
//	 * 发送邮件的方法
//	 * @throws MessagingException
//	 */
//	public void sendWrongMail(DdsMailConf bean,int wrongtype);
//	public int bathExeSql(DdsMailConf bean);

	public void insertExecLog(DdsExecLog planBean);
	
	public void updateExecLog(DdsExecLog planBean);

//	public int testSql(String sqlSentence);
}
