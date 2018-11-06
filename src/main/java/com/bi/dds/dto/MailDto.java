package com.bi.dds.dto;

import com.bi.dds.model.DdsMailConf;

public class MailDto extends DdsMailConf{
	private String ccMail;

	public String getCcMail() {
		return ccMail;
	}

	public void setCcMail(String ccMail) {
		this.ccMail = ccMail;
	}

	public MailDto(DdsMailConf ddsMailConf) {
		this.setCreateTime(ddsMailConf.getCreateTime());
		this.setCrontab(ddsMailConf.getCrontab());
		this.setExcelPath(ddsMailConf.getExcelPath());
		this.setId(ddsMailConf.getId());
		this.setIsAvild(ddsMailConf.getIsAvild());
		this.setIsDeleted(ddsMailConf.getIsDeleted());
		this.setIsDependent(ddsMailConf.getIsDependent());
		this.setIsSendEmpty(ddsMailConf.getIsSendEmpty());
		this.setIsSendMs(ddsMailConf.getIsSendMs());
		this.setMobileNum(ddsMailConf.getMobileNum());
		this.setMsContent(ddsMailConf.getMsContent());
		this.setRuleIdList(ddsMailConf.getRuleIdList());
		this.setRuleTable(ddsMailConf.getRuleTable());
		this.setRuleZeusTable(ddsMailConf.getRuleZeusTable());
		String sendMail = ddsMailConf.getSendMail();
		String cc = "";
		if(sendMail.contains("-")){
			String[] send = sendMail.split("-");
			sendMail = send[0];
			cc = send[1];
		}
		this.setCcMail(cc);
		this.setSendMail(sendMail);
		
		this.setSqlParam(ddsMailConf.getSqlParam());
		this.setSubject(ddsMailConf.getSubject());
		
		
		
	}
	
	
	
}
