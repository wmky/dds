package com.bi.dds.scheduler.jobimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.bi.dds.scheduler.JobZeusCheck;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.bi.dds.model.DdsExecLog;
import com.bi.dds.model.DdsMailConf;
import com.bi.dds.scheduler.BaseJob;
import com.bi.dds.system.SystemCore;
import com.bi.dds.util.CsvToXls;
import com.bi.dds.util.GetProDatas;


/**
 * 规则日志方法实现类
 * @author Slan By Y.K
 * @since 
 */


public class RunMailJob extends BaseJob{
//	public JdbcDao dao = new JdbcDao();
	public CsvToXls vtx = new CsvToXls();
	public String csvPath = GetProDatas.getCsvFilePath();
	public String xlsPath = GetProDatas.getXlsFilePath(); 
	public String cc = GetProDatas.getCc();
	public String dd = GetProDatas.getDd();
//	private SqlMgrItf sqlMgrItf;
	public String mailFrom = GetProDatas.getMailFrom();
	public static String codeSet = GetProDatas.getCodeSet();
	JavaMailSender sender = null ;
	//日志类
	private static Logger logger = Logger.getLogger(RunMailJob.class);


	public RunMailJob() {
		super();
		// TODO Auto-generated constructor stub
		int i =0;
//		sender = RunJobInitServlet.getJavaMailSender();
	//	System.out.println("mail--------------i:"+i);
	}


	@Override
	public void run() throws Exception {
		// TODO Auto-generated method stub
		//

		if(SystemCore.getSystemCore().getMailService().getJobZeusCheckService().isJobCompleted(this.getId())){
			logger.info("JOB ID" + this.getId() + "  依赖ZEUS作业已完成。");
			SystemCore.getSystemCore().getMailService().sendMail(this.getId(),null,null,false);

		}else{
			logger.info("JOB ID" + this.getId() + "  依赖ZEUS作业尚未完成，将加入到等待队列中，每两分钟去判断一次zeus执行状态");
			JobZeusCheck.WAIT_SEND_JOB.put(this.getId().toString(),new DdsMailConf());
		}

	}
	
}
