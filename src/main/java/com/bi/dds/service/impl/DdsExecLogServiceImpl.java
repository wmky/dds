package com.bi.dds.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bi.dds.mapper.DdsExecLogMapper;
import com.bi.dds.model.DdsExecLog;
import com.bi.dds.service.DdsExecLogService;




@Service(value = "ddsExecLogService")
public class DdsExecLogServiceImpl implements DdsExecLogService {
	
	private static final Logger logger = Logger.getLogger(DdsExecLogServiceImpl.class);
	
	@Autowired
	private DdsExecLogMapper ddsExecPlanMapper;

	@Override
	public void insertExecLog(DdsExecLog planBean) {
		ddsExecPlanMapper.insert(planBean);
	}

	@Override
	public void updateExecLog(DdsExecLog planBean) {
		ddsExecPlanMapper.updateByPrimaryKey(planBean);
		
	}
	
	
}
