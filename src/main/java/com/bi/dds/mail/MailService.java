package com.bi.dds.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipOutputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;

import com.bi.dds.scheduler.JobZeusCheck;
import com.bi.dds.service.*;

import com.bi.dds.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.quartz.Job;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.bi.dds.model.DdsDBConf;
import com.bi.dds.model.DdsExecLog;
import com.bi.dds.model.DdsMailConf;
import com.bi.dds.model.DdsRuleConf;
import com.bi.dds.scheduler.ScheduleConstants;
import com.bi.dds.system.SystemCore;
import com.sun.mail.smtp.SMTPAddressFailedException;

import static sun.misc.Version.println;

public class MailService {

	private static Logger logger = Logger.getLogger(MailService.class);

	private JavaMailSender mailSender;

	private DdsMailConfService ddsMailConfService;

	private DdsRuleConfService ddsRuleConfService;

	private DdsDBConfService ddsDBConfService;

	private DdsExecLogService ddsExecLogService;

	private JobZeusCheckService jobZeusCheckService;

	public String mailFrom;
	public String cc;
	public String wrongMail;
	public CsvToXls vtx;
	public String csvPath;
	public String xlsPath;
	public String codeSet;
	
	public String errorMsg;
	public String mlId;
	public String subject;
	public String[] sendCc;

	public void startUp(ServletContext arg0) {
		
		mailFrom = GetProDatas.getMailFrom();
		wrongMail = GetProDatas.getWrongMail();
		cc = GetProDatas.getCc();
		vtx = new CsvToXls();
		csvPath = GetProDatas.getCsvFilePath();
		xlsPath = GetProDatas.getXlsFilePath();
		codeSet = GetProDatas.getCodeSet();
		mailSender = (JavaMailSender) SystemCore.getSystemCore().findBean(
				"mailSender", arg0);
		ddsMailConfService = (DdsMailConfService) SystemCore.getSystemCore()
				.findBean("ddsMailConfService", arg0);
		ddsRuleConfService = (DdsRuleConfService) SystemCore.getSystemCore()
				.findBean("ddsRuleConfService", arg0);
		ddsExecLogService = (DdsExecLogService) SystemCore.getSystemCore()
				.findBean("ddsExecLogService", arg0);
		ddsDBConfService = (DdsDBConfService) SystemCore.getSystemCore()
				.findBean("ddsDBConfService", arg0);


		jobZeusCheckService =  (JobZeusCheckService)SystemCore.getSystemCore().findBean("jobZeusCheckService", arg0);

	}

	/**
	 * 发送定制的邮件
	 * 
	 * @param mailId
	 *            邮件ID
	 * @param mailstr
	 *            即时发送输入的邮箱
	 * @return
	 */
	public void sendMail(String mailId, String mailstr, String sqlParam,boolean isInstantSend) {
		
		mlId = mailId;
		DdsMailConf mailbean = ddsMailConfService.findMailConfById(mailId);
		if (mailbean != null && mailbean.getIsDeleted() == 1) {
			logger.info("JOB ID" + mailbean.getId() + "  已被删除,直接返回");
			return;
		}

		if(!StringUtils.isBlank(sqlParam)){
			mailbean.setSqlParam(sqlParam);
		}
		

		String send = mailbean.getSendMail();
		String ccTo  = "";
		if(send.contains("-")){
			String sen[] = send.split("-");
			ccTo = sen[1];
		}
//			 设置抄送者
		if (ccTo != null && !"".equals(ccTo)) {
			// messageHelper.setCc(cc);
			sendCc = ccTo.split(";");
		}else{
			sendCc = null;
		}

		
		logger.info("处理调度任务开始!待执行JOB ID：" + mailbean.getId() + "; 待发送报表名称："
				+ mailbean.getSubject());
		
		subject = mailbean.getSubject();
		
		int sucFlag = 0;// 成功标示,1成功0失败
		DdsExecLog planBean = new DdsExecLog();
		planBean.setMailId(mailbean.getId());
		planBean.setMailName(mailbean.getSubject());
		// planBean.setCreateTime(new D);
		planBean.setBeginTime(new Date());
		planBean.setFlag(0);
		// planBean.s
		ddsExecLogService.insertExecLog(planBean);// .updateBeginTimePlanBean(planBean);

		logger.info("当前步骤:查询数据库并生成EXCEL文件!待执行JOB ID：" + mailbean.getId()
				+ "; 待发送报表名称：" + mailbean.getSubject());

		sucFlag = bathExeSql(mailbean);
		
		if (sucFlag != 0) {
			if (sucFlag == -999) {
				planBean.setErrmsg("EXCEL结果集为空,暂不发送");
				planBean.setFlag(sucFlag);
			}
			// 出队列的时候就写一个end_Time但未执行成功，原因尚待考究
			planBean.setEndTime(new Date());
			ddsExecLogService.updateExecLog(planBean);
		}

		logger.info("当前步骤:发送邮件!待执行JOB ID：" + mailbean.getId() + "; 待发送报表名称："
				+ mailbean.getSubject());
		sucFlag = sendMailDo(mailbean, mailstr);
		
		planBean.setEndTime(new Date());
		planBean.setFlag(sucFlag);
		ddsExecLogService.updateExecLog(planBean);
		
		logger.info("处理调度任务结束!待执行JOB ID：" + mailbean.getId() + "; 待发送报表名称："
				+ mailbean.getSubject());
	}
	
	private void sendErrorMsg(String msg) {
		if(sendCc == null){
			return;
		}
		MimeMessage mailMessage = mailSender.createMimeMessage();
		// 设置utf-8或GBK编码，否则邮件会有乱码
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(
					mailMessage, true, "utf-8");
			if(wrongMail!=null){
				messageHelper.setTo(wrongMail.split(";"));
			}
			messageHelper.setFrom(mailFrom);// 发送者
			
			messageHelper.setSubject("邮件平台错误日志");// 主题
			StringBuffer content = new StringBuffer(
					"<html><head></head><body>");

			content.append("邮件id[" + mlId + "]," );
			content.append("邮件标题[" + subject + "]," );
			content.append("错误原因[" + msg + "]" );

			content.append("</body></html>");

			messageHelper.setText(content.toString(), true);
			messageHelper.setTo(sendCc);
			mailSender.send(mailMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
	}

	/**
	 * 批处理执行sql
	 * @param bean
	 * @return
	 */
	public int bathExeSql(DdsMailConf bean) {
		int r = 0;
		try {
			String sqlStr = bean.getRuleIdList();
			if (null != sqlStr && !"".equals(sqlStr.trim())) {
				String[] sqls = sqlStr.split(",");
				int[] resAry = new int[sqls.length];
				for (int i = 0; i < sqls.length; i++) {
					// String idStr = sqls[i];
					String theId = sqls[i];
					// if(idStr.indexOf("ora")==0){
					// String theId = idStr.replaceFirst("ora", "");
					DdsRuleConf odr = ddsRuleConfService.findDdsRuleById(theId);
					
					if(odr != null){
						DdsDBConf dbConf = ddsDBConfService.findDdsDBConf("",
								odr.getDbName());
						if (dbConf != null
								&& dbConf.getDbDriver() != null) {
							
							logger.info("db类型:" + dbConf.getDbType());
							
							if (dbConf.getDbDriver().equals("hive")) {
								r = execHql( DataBaseSorts.HIVE_CLI, odr, bean);
								continue;
							}
							if (dbConf.getDbType().equals("presto")) {
								
								r = execPql( DataBaseSorts.PRESTO, odr, bean, dbConf);
								continue;
							}
							if (dbConf.getDbType().equals("postgresql")) {

								r = execPostgresSql( DataBaseSorts.POSTGRESQL, odr, bean, dbConf);
								continue;
							}

						}
					}

					if (null != odr && "".equals("")) {
						r = antExecSql(DataBaseSorts.MYSQL, odr, bean);
						if (r != 0) {
							if (r == -999) {
								resAry[i] = -999;
								// return -999;
							} else {
								resAry[i] = 0;
							}
							// return 0;
						}
					}
					// }

				}
			}
		} catch (Exception e) {
			String errorMsg = String.format("id为%s的mail发送出错", bean.getId());
			logger.error(errorMsg,e);
			r = ScheduleConstants.JOB_ERROR3;
		}
		return r;
	}

	/**
	 * 执行sql
	 * 
	 * @param db
	 * @param info
	 * @param id
	 * @return
	 */
	public int antExecSql(String db, DdsRuleConf info, DdsMailConf bean) {
		int r = 1; // 执行结果
		String sql = "";
		try {
			String datefile = getDateFileStr();
			SQLExec sqlExec = new SQLExec();
			DdsDBConf dbConf = ddsDBConfService.findDdsDBConf("",
					info.getDbName());
			info.setDdsDBConf(dbConf);

			if (!StringUtils.isBlank(bean.getSqlParam())) {
				info.setSqlParam(bean.getSqlParam());
			}

			info.setRuleSql(StringUtil.paramConvert(info.getRuleSql(),
					info.getSqlParam()));

			sql = info.getRuleSql();

			if (null != info.getProName()
					&& !"".equals(info.getProName().trim())) {// 说明要执行存储过程
				JdbcDao dao = new JdbcDao();
				// r = dao.execPro(info.getProName(), db);
				r = dao.execProDymaic(info.getProName(), dbConf.getDbUrl(),
						dbConf.getDbUser(), dbConf.getDbPassword());
			}
			// 设置数据库参数

			sqlExec.setDriver(dbConf.getDbDriver());
			sqlExec.setUrl(dbConf.getDbUrl());
			sqlExec.setUserid(dbConf.getDbUser());
			sqlExec.setPassword(dbConf.getDbPassword());

			/*
			 * if(db.equals(DataBaseSorts.MYSQL)){
			 * sqlExec.setDriver(GetDataBaseConnStr.getOraDriverExe());
			 * 
			 * sqlExec.setUrl(GetDataBaseConnStr.getOraUrlExe());
			 * 
			 * sqlExec.setUserid(GetDataBaseConnStr.getOraUserExe());
			 * 
			 * sqlExec.setPassword(GetDataBaseConnStr.getOraPassExe()); }
			 * if(db.equals(DataBaseSorts.TD)){
			 * sqlExec.setDriver(GetDataBaseConnStr.getTdDriverExe());
			 * 
			 * sqlExec.setUrl(GetDataBaseConnStr.getTdUrlExe());
			 * 
			 * sqlExec.setUserid(GetDataBaseConnStr.getTdUserExe());
			 * 
			 * sqlExec.setPassword(GetDataBaseConnStr.getTdPassExe()); }
			 */

			sqlExec.addText(sql);
			// sqlExec.setOnerror((SQLExec.OnError)(EnumeratedAttribute.getInstance(SQLExec.OnError.class,
			// "abort")));

			sqlExec.setPrint(true); // 设置是否输出

			// 输出到文件 sql.out 中；不设置该属性，默认输出到控制台
			sqlExec.setCsvColumnSeparator("|+|");

			int nr = newFolder(csvPath + datefile);

			if (nr != 0) {
				return ScheduleConstants.JOB_ERROR3;
			}

			String csvFilePath = csvPath + datefile + db + info.getId()
					+ ".csv";

			sqlExec.setOutput(new File(csvFilePath));
			sqlExec.setEncoding("UTF8");
			sqlExec.setProject(new Project()); // 要指定这个属性，不然会出错

			sqlExec.execute();

			// 生成excel文件

			if (null != info.getExcelName()
					&& !"".equals(info.getExcelName().trim())) {
				int nr2 = newFolder(xlsPath + datefile);

				if (nr2 != 0) {
					logger.error("系统运行出错");
					return ScheduleConstants.JOB_ERROR3;
				}

				String xlsName = xlsPath + datefile + db + info.getExcelName()
						+ "_" + getDateStr() + ".xlsx";

				// r = csvToXls(csvFilePath,xlsName,info.getSheetName());
				r = csvToXls(csvFilePath, xlsName, info.getSheetName(),
						bean.getIsSendEmpty() + "");
				if (r == -998) {// 生成xls文件报错，发送邮件通知
					logger.error("将csv文件转成xls文件报错，文件名为" + xlsName);
					return ScheduleConstants.JOB_ERROR1;
				}
				if (r == -999) {
					return -999;
				}
			}

			r = 0;
		} catch (Exception e) {
			e.printStackTrace();
			sendErrorMsg("执行sql报错,sql为" + sql + "错误为" + e.getMessage());
			logger.error("执行sql报错,sql为" + sql, e);
			r = ScheduleConstants.JOB_ERROR2;
		}

		return r;
	}

	/**
	 * 执行sql
	 * 
	 * @param db
	 * @param info
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public int execHql(String db, DdsRuleConf info, DdsMailConf bean) throws Exception {
		int r = 1; // 执行结果
		try {
			
			String datefile = getDateFileStr();
		

			String xlsName = xlsPath + datefile + db + info.getExcelName()
					+ "_" + getDateStr() + ".xlsx";
			
			if (bean.getSqlParam() != null
					&& !"".equals(bean.getSqlParam().trim())) {
				info.setRuleSql(StringUtil.paramConvert(info.getRuleSql(),bean.getSqlParam()));
			}else{
				info.setRuleSql(StringUtil.paramConvert(info.getRuleSql(),""));
			}
			
		    if( info.getSqlParam() == null){
		    	info.setSqlParam("");
		    }
		    
		    String sqlid=info.getId().toString(); 
		    // kaikai
		    
			String[] heads = info.getSqlParam().split(",");
			String sql = info.getRuleSql();
            List<String[]> results = HiveShellUtil.sshHive(sql,sqlid);
//			List<String[]> results = HiveShellUtil.sshHive(sql);
			if(results == null){
				sendErrorMsg("执行结果为空，请检查原因");
				return 1;
			}
			r = ExcelUtil.parse(results, heads,xlsName,info.getSheetName(),xlsPath + datefile);
			
		} catch (Exception e) {
			
			String msg = String.format("id为%s的sql执行出错", info.getId());
			sendErrorMsg(msg);
			throw new Exception(msg, e);
		}

		return r;
	}
	
	
	/**
	 * 执行presto sql
	 * 
	 * @param db
	 * @param info
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public int execPql(String db, DdsRuleConf info, DdsMailConf bean, DdsDBConf dbconf) throws Exception {
		int r = 1; // 执行结果
		try {
			
			String datefile = getDateFileStr();
		

			String xlsName = xlsPath + datefile + db + info.getExcelName()
					+ "_" + getDateStr() + ".xlsx";
			
			if (bean.getSqlParam() != null
					&& !"".equals(bean.getSqlParam().trim())) {
				info.setRuleSql(StringUtil.paramConvert(info.getRuleSql(),bean.getSqlParam()));
			}else{
				info.setRuleSql(StringUtil.paramConvert(info.getRuleSql(),""));
			}
			
			String[] heads = null;
		    if( info.getSqlParam() == null || info.getSqlParam().equals("")){
		    	info.setSqlParam("");
		    	heads = null;
		    }else{
		    	heads = info.getSqlParam().split(",");
		    }
			
			String sql = info.getRuleSql();
			List<String[]> results = PrestoDBUtil.prestoExecute(sql, heads, dbconf.getDbDriver() ,dbconf.getDbUrl() , dbconf.getDbUser(), dbconf.getDbPassword());
			
			if(results == null){
				sendErrorMsg("执行结果为空，请检查原因");
				return 1;
			}
			r = ExcelUtil.parse(results, heads,xlsName,info.getSheetName(),xlsPath + datefile);
			
		} catch (Exception e) {
			
			String msg = String.format("id为%s的presto sql执行出错", info.getId());
			sendErrorMsg(msg);
			throw new Exception(msg, e);
		}

		return r;
	}


	/**
	 * 执行postgreSql sql
	 *
	 * @param db
	 * @param info
	 * @param  db,info,bean,dbconf,
	 * @return r
	 * @throws Exception
	 */
	public int execPostgresSql(String db, DdsRuleConf info, DdsMailConf bean, DdsDBConf dbconf) throws Exception {
		int r = 1; // 执行结果
		try {

			String datefile = getDateFileStr();


			String xlsName = xlsPath + datefile + db + info.getExcelName()
					+ "_" + getDateStr() + ".xlsx";

			if (bean.getSqlParam() != null
					&& !"".equals(bean.getSqlParam().trim())) {
				info.setRuleSql(StringUtil.paramConvert(info.getRuleSql(),bean.getSqlParam()));
			}else{
				info.setRuleSql(StringUtil.paramConvert(info.getRuleSql(),""));
			}

			String[] heads = null;
			if( info.getSqlParam() == null || info.getSqlParam().equals("")){
				info.setSqlParam("");
				heads = null;
			}else{
				heads = info.getSqlParam().split(",");
			}

			String sql = info.getRuleSql();
			logger.info("wmky_kk sql : " + sql);

			List<String[]> results = PostgreSqlDBUtil.postgresqlExecute(sql, heads, dbconf.getDbDriver() ,dbconf.getDbUrl() , dbconf.getDbUser(), dbconf.getDbPassword());

			logger.info("查询数据条数" + results.size());

			for (String[] result : results) {
				System.out.println(" wmky_kk " + Arrays.toString(result));

			}

			if(results == null){
				sendErrorMsg("执行结果为空，请检查原因");
				return 1;
			}
			r = ExcelUtil.parse(results, heads,xlsName,info.getSheetName(),xlsPath + datefile);

			logger.info("wmky_kk r value is : " + r); // r value=0
		} catch (Exception e) {

			String msg = String.format("id为%s的postgress sql执行出错", info.getId());
			sendErrorMsg(msg);
			throw new Exception(msg, e);
		}

		return r;
	}

	/**
	 * 检查字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public boolean checkNotEmptyStr(String str) {
		boolean b = false;
		if (null != str && !"".equals(str.trim())) {
			b = true;
		} else {
			b = false;
		}
		return b;
	}

	/**
	 * @param file
	 * @return boolean 检查文件是否存在
	 */
	public boolean checkFile(File file) {
		if (file.exists()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 将csv文件转成xls文件
	 * 
	 * @param csvFilePath
	 * @param xlsName
	 * @param sheetName
	 * @return
	 */
	public int csvToXls(String csvFilePath, String xlsName, String sheetName,
			String isSendEmpty) {
		int r = 0;
		try {
			String sheet = "sheet1";
			if (null != sheetName && !"".equals(sheetName)) {
				sheet = sheetName;
			}
			r = vtx.transform(csvFilePath, xlsName, sheet, isSendEmpty);
			// r=1;
		} catch (Exception e) {
			e.printStackTrace();
			r = -998;
		}
		return r;
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * 获得日期字符串
	 * 
	 * @return String
	 */
	public String getDateFileStr() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		cal.setTime(cal.getTime());
		Date date = cal.getTime();
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		String datefile = sd.format(date);
		return datefile + File.separator;
	}

	public String getDateStr() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		cal.setTime(cal.getTime());
		Date date = cal.getTime();
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		String datefile = sd.format(date);
		return datefile;
	}

	/**
	 * 创建新文件夹
	 * 
	 * @param folderPath
	 */
	public int newFolder(String folderPath) {
		int r = 0;
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		try {
			if (myFilePath.isDirectory()) {
				// System.out.println("the directory is exists!");
			} else {
				myFilePath.mkdir();
				// System.out.println("make dir succ");
			}
		} catch (Exception e) {
			r = 1;
			// System.out.println("make dir wrong");
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 
	 * @param mail
	 */
	private int sendMailDo(DdsMailConf bean, String mailstr) {
		int r = 0;
		try {
			// 获得今天日期
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 0);
			cal.setTime(cal.getTime());
			Date date = cal.getTime();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy年MM月dd日");
			String today = sd.format(date);
			// 获取JavaMailSender bean
			MimeMessage mailMessage = mailSender.createMimeMessage();
			// 设置utf-8或GBK编码，否则邮件会有乱码
			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mailMessage, true, "utf-8");

			// 接受者
			
			String send = bean.getSendMail();
			String sendMail = "";
			String ccTo  = "";
			if(send.contains("-")){
				String sen[] = send.split("-");
				sendMail = sen[0];
				ccTo = sen[1];
			}else{
				sendMail = send;
			}
			String[] sendto = sendMail.split(";");
			messageHelper.setTo(sendto);
			if (null != mailstr && !"".equals(mailstr.trim())) {
				sendto = mailstr.split(";");
			}else{
//				 设置抄送者
				if (ccTo != null && !"".equals(ccTo)) {
					// messageHelper.setCc(cc);
					String[] sendCc = ccTo.split(";");
					messageHelper.setCc(sendCc);
				}
			}

			// System.out.println("mail address 1:-------------->:"+sendto[0]);

			messageHelper.setTo(sendto);

			messageHelper.setFrom(mailFrom);// 发送者

			if (bean.getSqlParam() != null && !"".equals(bean.getSqlParam())) {
				messageHelper.setSubject(bean.getSubject() + "      "
						+ bean.getSqlParam());// 主题
			} else {
				messageHelper.setSubject(bean.getSubject() + "      " + today);// 主题
			}



			// 邮件内容，注意加参数true
			StringBuffer content = new StringBuffer(
					"<html><head></head><body>Hi all:<br>");

			r = addContent(messageHelper, bean, content);

			if (r == 0) {// 在加入邮件内容时报错
				return r;
			} else {// 运行正确
			// content.append("<p><font color=\"blue\">温馨提示：<br> 如果您希望继续接受该邮件，请配合我们的一项调查；"
			// +
			// "如果未获取到您的接受要求，我们会在一个月后将该邮件下线！请在有内部网络环境时点击下方链接</font></p>");
			//
			// content.append("<a href=\"javascript:getusername();\"><font color=\"red\">点击继续接受</font></a>");

				content.append("</body>");

				content.append("<br>Reports<br>" + today);

				// content.append("<script type=\"text/javascript\">");
				// content.append("function getusername(){");
				// content.append("var WshNetwork = new ActiveXObject(\"WScript.Network\");");
				// content.append("window.open(\"http://"+dao.localIPAdd()+":8080/dds/count.do?id=9&username=\"+WshNetwork.UserName);}");
				// content.append("</script>");

				content.append("</html>");
				messageHelper.setText(content.toString(), true);
				// mailMessage.setText(messageHelper);

			}
			mailSender.send(mailMessage);
			r = 1;

		} catch (Exception e) {
			if(e.getMessage().contains("Invalid Addresses")){
				sendErrorMsg("收件人地址不正确，请检查是否有离职员工");
				logger.error(e);
				r = 0;
			}else{
				sendErrorMsg(e.getMessage());
				logger.error("sql执行出错，出错原因: " + e.getMessage());
				r = 0;
			}
			
		}
		return r;
	}

	/**
	 * 向邮件正文加入内容
	 * 
	 * @param messageHelper
	 * @param bean
	 * @param content
	 * @return
	 */
	public static void main(String[] args) {
//		MailService mailservice=new MailService();
	}
	public int addContent(MimeMessageHelper messageHelper, DdsMailConf bean,
			StringBuffer content) {
		int r = 0;
		try {
			String datafile = getDateFileStr();
			String sqlStr = bean.getRuleIdList();
			if (null != sqlStr && !"".equals(sqlStr.trim())) {
				String[] sqls = sqlStr.split(",");
				StringBuffer oraExcelNames = new StringBuffer();
				
				for (int i = 0; i < sqls.length; i++) {
					// String idStr = sqls[i];
					String theId = sqls[i];
					// if(idStr.indexOf("ora")==0){
					// String theId = idStr.replaceFirst("ora", "");
					DdsRuleConf odr = ddsRuleConfService.findDdsRuleById(theId);
					
					DdsDBConf dbConf = ddsDBConfService.findDdsDBConf("",odr.getDbName());
					if (dbConf != null
							&& dbConf.getDbDriver() != null) {
						if (dbConf.getDbDriver().equals("hive")) {
							boolean b1 = checkFile(new File(xlsPath
									+ datafile + DataBaseSorts.HIVE_CLI
									+ odr.getExcelName() + "_"
									+ getDateStr() + ".xlsx"));
							if (b1) {
								oraExcelNames.append(odr.getExcelName());
								oraExcelNames.append(":").append(DataBaseSorts.HIVE_CLI);
								oraExcelNames.append(",");
								continue;
							} 
						}
						if (dbConf.getDbType().equals("presto")) {
							boolean b1 = checkFile(new File(xlsPath
									+ datafile + DataBaseSorts.PRESTO
									+ odr.getExcelName() + "_"
									+ getDateStr() + ".xlsx"));
							if (b1) {
								oraExcelNames.append(odr.getExcelName());
								oraExcelNames.append(":").append(DataBaseSorts.PRESTO);
								oraExcelNames.append(",");
								continue;
							} 
						}
						if (dbConf.getDbType().equals("postgresql")) {
							boolean b1 = checkFile(new File(xlsPath
									+ datafile + DataBaseSorts.POSTGRESQL
									+ odr.getExcelName() + "_"
									+ getDateStr() + ".xlsx"));
							if (b1) {
								oraExcelNames.append(odr.getExcelName());
								oraExcelNames.append(":").append(DataBaseSorts.POSTGRESQL);
								oraExcelNames.append(",");
								continue;
							}
						}
					}
					
					if (null != odr) {
						odr.setSqlParam(bean.getSqlParam());
						if (checkNotEmptyStr(odr.getExcelName())) {// 存在excel,结果要存入excel发送
							boolean b = checkFile(new File(csvPath + datafile
									+ DataBaseSorts.MYSQL + odr.getId()
									+ ".csv"));
							if (b) {// 证明生成过csv文件，一般情况下xls文件也是一定存在的,并且因为就算是多个excel文件，也只用add一次，所以，不用在循环里add
								boolean b1 = checkFile(new File(xlsPath
										+ datafile + DataBaseSorts.MYSQL
										+ odr.getExcelName() + "_"
										+ getDateStr() + ".xlsx"));

								if (b1) {
									oraExcelNames.append(odr.getExcelName());
									oraExcelNames.append(":").append(DataBaseSorts.MYSQL);
									oraExcelNames.append(",");
									// break;
								} else {// 说明虽然生成了csv文件但是没有生成xls文件，奇怪的原因
									return 0;// 可以重新设置错误标示
								}
							} else {// 说明发送邮件前sql没有执行成功
									// 重新执行一次sql
								return 0;// 可以重新设置错误标示
							}
						} else {// 说明该sql没有发送excel配置
							boolean b2 = checkFile(new File(csvPath + datafile
									+ DataBaseSorts.MYSQL + odr.getId()
									+ ".csv"));
							if (!b2) {// 说明该sql执行失败

								return 0;// 可以重新设置错误标示

							} else {// 将执行结果加入content
								r = addContentCsv(new File(csvPath + datafile
										+ DataBaseSorts.MYSQL + odr.getId()
										+ ".csv"), content, odr.getSubject());
							}
						}
					}
					// }

				}
				if (r != 0) {// 将结果加入时有错
					return 0;
				}
				if (null != oraExcelNames
						&& !"".equals(oraExcelNames.toString())) {
					String tempStr = oraExcelNames.toString();
					if (tempStr.lastIndexOf(",") > 0) {
						tempStr = tempStr
								.substring(0, tempStr.lastIndexOf(","));
					}
					String[] oraExcelNameStr = tempStr.split(",");

					if (oraExcelNameStr.length > 0) {
						/*
						 * File file = new
						 * File(xlsPath+datafile+DataBaseSorts.MYSQL
						 * +oraExcelNameStr[0]+getDateStr()+".xlsx"); //
						 * 使用MimeUtility.encodeWord()来解决附件名称的中文问题
						 * if(file.length()>5*1024*1024){//大于5m //压缩
						 * ZipOutputStream zos = new ZipOutputStream(new
						 * FileOutputStream
						 * (xlsPath+datafile+DataBaseSorts.MYSQL+
						 * oraExcelNameStr[0]+getDateStr()+".rar"));
						 * MyFileUtil.zipFile(file, zos, ""); zos.close(); File
						 * file1 = new
						 * File(xlsPath+datafile+DataBaseSorts.MYSQL+
						 * oraExcelNameStr[0]+getDateStr()+".rar");
						 * messageHelper
						 * .addAttachment(MimeUtility.encodeWord(oraExcelNameStr
						 * [0]+getDateStr()+".rar"),file1); }else{
						 * messageHelper.
						 * addAttachment(MimeUtility.encodeWord(oraExcelNameStr
						 * [0]+getDateStr()+".xlsx"),file); }
						 */
						Map<String, String> map = new HashMap<String, String>();
						for (String oesdb : oraExcelNameStr) {
							if (map.containsKey(oesdb)) {
								continue;
							} else {
								map.put(oesdb, oesdb);
							}
							String[] oesdbarray = oesdb.split(":");
							String oes = oesdbarray[0];
							String db = oesdbarray[1];
							File file = new File(xlsPath + datafile
									+ db + oes + "_"
									+ getDateStr() + ".xlsx");

							// 使用MimeUtility.encodeWord()来解决附件名称的中文问题
							if (file.length() > 5 * 1024 * 1024) {// 大于5m
								// 压缩
								ZipOutputStream zos = new ZipOutputStream(
										new FileOutputStream(xlsPath + datafile
												+ db + oes
												+ "_" + getDateStr() + ".rar"));

								MyFileUtil.zipFile(file, zos, "");
								zos.close();
								File file1 = new File(xlsPath + datafile
										+ db + oes + "_"
										+ getDateStr() + ".rar");

								if (bean.getSqlParam() != null
										&& !"".equals(bean.getSqlParam())) {
									messageHelper.addAttachment(
											MimeUtility.encodeWord(oes + "_"
													+ bean.getSqlParam()
													+ ".rar"), file1);
								} else {
									messageHelper.addAttachment(
											MimeUtility.encodeWord(oes + "_"
													+ getDateStr() + ".rar"),
											file1);
								}
							} else {
								if (bean.getSqlParam() != null
										&& !"".equals(bean.getSqlParam())) {
									messageHelper.addAttachment(
											MimeUtility.encodeWord(oes + "_"
													+ bean.getSqlParam()
													+ ".xlsx"), file);
								} else {
									messageHelper.addAttachment(
											MimeUtility.encodeWord(oes + "_"
													+ getDateStr() + ".xlsx"),
											file);
								}
							}
						}

					}
				}
//				if (null != tdExcelNames && !"".equals(tdExcelNames.toString())) {
//					String tempStr = tdExcelNames.toString();
//					if (tempStr.lastIndexOf(",") > 0) {
//						tempStr = tempStr
//								.substring(0, tempStr.lastIndexOf(","));
//					}
//					String[] tdExcelNameStr = tempStr.split(",");
//					if (tdExcelNameStr.length > 0) {
//						File file = new File(xlsPath + datafile
//								+ DataBaseSorts.TD + tdExcelNameStr[0]
//								+ getDateStr() + ".xlsx");
//						// 使用MimeUtility.encodeWord()来解决附件名称的中文问题
//						if (file.length() > 5 * 1024 * 1024) {// 大于5m
//							// 压缩
//							ZipOutputStream zos = new ZipOutputStream(
//									new FileOutputStream(xlsPath + datafile
//											+ DataBaseSorts.TD
//											+ tdExcelNameStr[0] + getDateStr()
//											+ ".rar"));
//							MyFileUtil.zipFile(file, zos, "");
//							zos.close();
//							File file1 = new File(xlsPath + datafile
//									+ DataBaseSorts.TD + tdExcelNameStr[0]
//									+ getDateStr() + ".rar");
//							messageHelper.addAttachment(
//									MimeUtility.encodeWord(tdExcelNameStr[0]
//											+ getDateStr() + ".rar"), file1);
//						} else {
//							messageHelper.addAttachment(
//									MimeUtility.encodeWord(tdExcelNameStr[0]
//											+ getDateStr() + ".xlsx"), file);
//						}
//
//					}
//
//				}
			}
			r = 1;
		} catch (Exception e) {
			logger.error("id为" + bean.getId() +"的邮件在加入附件内容时出错，请查看原因",e);
			sendErrorMsg("加入附件内容时出错，请查看原因");
			r = 0;
		}
		return r;
	}
	
	

	/**
	 * 读取csv文件的内容，加入邮件中
	 */
	public int addContentCsv(File csvFile, StringBuffer content, String subject) {
		int r = 0;
		BufferedReader br = null;
		try {

			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					csvFile), codeSet);// 春江
			br = new BufferedReader(isr);
			String line = "";
			content.append("<font face=黑体 style=background-color:yellow><p><br>"
					+ subject + "<br></p></b></font>");
			content.append("<table border=1 cellspacing=0 style=border-collapse:collapse width=1000><font size=2 face=Times>");
			while ((line = br.readLine()) != null) {
				if (line.contains("0 rows affected")) {
					break;
				}
				if (!"".equals(line) && null != line) {
					String[] str = line.split("\\|\\+\\|");
					content.append("<tr>");
					for (int i = 0; i < str.length; i++) {
						content.append("<td>");
						content.append(str[i]);
						content.append("</td>");
					}
					content.append("</tr>");
				}
			}
			content.append("</table>");
		} catch (Exception e) {
			logger.error(e);
			r = ScheduleConstants.JOB_ERROR3;
			sendErrorMsg(e.getMessage());
			e.printStackTrace();

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return r;
	}

	/**
	 * 发送邮件的方法
	 * 
	 * @throws MessagingException
	 */
	public void sendWrongMail(DdsMailConf bean, int wrongtype) {
		try {
			// 获得今天日期
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 0);
			cal.setTime(cal.getTime());
			Date date = cal.getTime();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy年MM月dd日");
			String today = sd.format(date);
			// 获取JavaMailSender bean
			MimeMessage mailMessage = mailSender.createMimeMessage();
			// 设置utf-8或GBK编码，否则邮件会有乱码
			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mailMessage, true, "utf-8");

			// 接受者
			/*
			 * String[] sendto = bean.getSendto().split(";");
			 * messageHelper.setTo(sendto);
			 */
			if(wrongMail!=null){
				messageHelper.setTo(wrongMail.split(";"));
			}
			
			String wrongcontent = "wrong mail for job " + bean.getId() + "  "
					+ bean.getSubject();

			if(wrongMail!=null){
				messageHelper.setTo(wrongMail.split(";"));
			}
			messageHelper.setFrom(mailFrom);// 发送者
			
			String subject = "wrong mail for job " + "      " + today;
			if (null != bean && null != bean.getSubject()
					&& !"".equals(bean.getSubject().trim())) {
				subject = bean.getSubject() + "      " + today;
			}
			messageHelper.setSubject("wrong " + subject);// 主题
			// 设置抄送者
			if (cc != null && !"".equals(cc))
				messageHelper.setCc(cc.split(";"));

			// 邮件内容，注意加参数true
			StringBuffer content = new StringBuffer(
					"<html><head></head><body>Hi all:<br>");

			content.append(wrongcontent);

			content.append("</body></html>");

			messageHelper.setText(content.toString(), true);

			mailSender.send(mailMessage);

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	public JobZeusCheckService getJobZeusCheckService() {
		return jobZeusCheckService;
	}

	public DdsMailConfService getDdsMailConfService() {
		return ddsMailConfService;
	}
}
