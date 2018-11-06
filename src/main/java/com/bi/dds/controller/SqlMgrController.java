package com.bi.dds.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bi.dds.dto.MailDto;
import com.bi.dds.mail.MailService;
import com.bi.dds.model.BiUser;
import com.bi.dds.model.DdsDBConf;
import com.bi.dds.model.DdsMailConf;
import com.bi.dds.model.DdsRuleConf;
import com.bi.dds.scheduler.ScheduleConstants;
import com.bi.dds.scheduler.SchedulerService;
import com.bi.dds.service.DdsDBConfService;
import com.bi.dds.service.DdsMailConfService;
import com.bi.dds.service.DdsRuleConfService;
import com.bi.dds.system.SystemCore;
import com.bi.dds.util.ProcessException;
import com.bi.dds.util.StringUtil;

@Controller
@RequestMapping("/sqlmgr")
public class SqlMgrController {
	// 对象
	private static final Logger logger = Logger
			.getLogger(SqlMgrController.class);

	@Autowired
	private DdsMailConfService ddsMailConfService;

	@Autowired
	private DdsRuleConfService ddsRuleConfService;

	@Autowired
	private DdsDBConfService ddsDBConfService;

	/**
	 * 页面跳转
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	@RequestMapping("/init")
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException {
		logger.debug("进入[SqlMgrController][init]");
		String url = request.getParameter("url");
		logger.debug("url:" + url);
		logger.debug("跳出[SqlMgrController][init]");

		return new ModelAndView(url);
	}

	/**
	 * 到配置发件人的页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	@RequestMapping(value = "/goMail")
	public ModelAndView goMail(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException {

		logger.debug("进入[SqlMgrController][goMail]");
		String url = request.getParameter("url");
		logger.debug("url:" + url);
		logger.debug("跳出[SqlMgrController][goMail]");

		return new ModelAndView(url);
	}

	/**
	 * 到预警发邮件监控页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	public ModelAndView goAlarm(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException {

		logger.debug("进入[SqlMgrController][goAlarm]");
		String url = request.getParameter("url");
		logger.debug("url:" + url);
		logger.debug("跳出[SqlMgrController][goAlarm]");

		return new ModelAndView(url);
	}

	/*
	 * sql增删改查方法
	 */

	/**
	 * 查询sql
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	@RequestMapping(value = "/querySqlInfo")
	@ResponseBody
	public Map<String, Object> querySqlInfo(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException {
		logger.debug("进入[SqlMgrController][querySqlInfo]");

		// result 用来拼字符串;rev 用来传到页面
		StringBuffer result = new StringBuffer();
		String rev = "";

		result.append("{'totalProperty':");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String para = request.getParameter("para");

		// logger.debug("para:"+para);
		// logger.debug("start:"+start);
		// logger.debug("limit:"+limit);

		if (null == start) {
			start = "1";
		}
		if (null == limit) {
			limit = "16";
		}

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			// 获得登录人员信息
			HttpSession session = request.getSession(true);
			BiUser user = (BiUser) session.getAttribute("user");
			String loginId = user.getId();

			// 得到查询的参数
			String sendto = request.getParameter("query_sendto"); // 发送者
			String subject = request.getParameter("query_subject");// 主题
			String queryId = request.getParameter("query_id");// id
			String isAvild = request.getParameter("query_isAvild");// 是否有效
			String sqlrule = request.getParameter("query_sql");// 规则语句包含字段
			String creatdate1 = request.getParameter("query_date1");// 查询开始时间
			String creatdate2 = request.getParameter("query_date2");// 查询结束时间
			// 去掉ext时间控件的T及T后面的时间字串
			creatdate1 = creatdate1.split("T")[0];
			creatdate2 = creatdate2.split("T")[0];
			// 获得查询数据库的类型
			String databaseId = request.getParameter("query_databaseId");// 查询数据库的id
			// System.out.println("databaseId:"+databaseId);
			// 分页查询区域信息

			int count = ddsRuleConfService.countByQueryRuleInfoList(databaseId,
					queryId, subject, sqlrule, creatdate1, creatdate2, start,
					limit);
			List<DdsRuleConf> list = new ArrayList<DdsRuleConf>();
			if (count > 0) {
				list = ddsRuleConfService.queryRuleInfoList(databaseId,
						queryId, subject, sqlrule, creatdate1, creatdate2,
						start, limit);
			}

			map.put("totalCount", count);
			map.put("rows", list);
			return map;
			// 判断查询结果是否为空

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.debug("跳出[SqlMgrController][querySqlInfo]");
		return null;
	}

	/**
	 * 查询ora_dqc_mail表的记录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	@RequestMapping(value = "/queryMailInfo")
	@ResponseBody
	public Map<String, Object> queryMailInfo(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException {
		logger.debug("进入[SqlMgrController][queryMailInfo]");

		// result 用来拼字符串;rev 用来传到页面
		StringBuffer result = new StringBuffer();
		String rev = "";

		result.append("{'totalProperty':");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String para = request.getParameter("para");

		// logger.debug("para:"+para);
		// logger.debug("start:"+start);
		// logger.debug("limit:"+limit);

		if (null == start) {
			start = "1";
		}
		if (null == limit) {
			limit = "16";
		}

		try {

			// 获得登录人员信息
			HttpSession session = request.getSession(true);
			BiUser user = (BiUser) session.getAttribute("user");
			String loginId = user.getId();

			// 得到查询的参数
			String queryId = request.getParameter("query_id"); // id
			String isAvild = request.getParameter("query_isAvild"); // isAvild
			String sendto = request.getParameter("query_sendto"); // 发送者
			String subject = request.getParameter("query_subject");// 主题
			String ruleIdStr = request.getParameter("query_sqlIdList");// sqlId
			String date = request.getParameter("query_date");//
			String isSuccess = request.getParameter("query_isSuccess");//

			int count = ddsMailConfService.countMailInfoList(queryId, sendto,
					isAvild, subject, ruleIdStr, start, limit, date, isSuccess);
			List<DdsMailConf> list = new ArrayList<DdsMailConf>();
			if (count > 0) {
				list = ddsMailConfService.queryMailInfoList(queryId, sendto,
						isAvild, subject, ruleIdStr, start, limit, date,
						isSuccess);
			}

			List<MailDto> list2 = new ArrayList<MailDto>();
			if (list.size() > 0) {
				for (DdsMailConf conf : list) {
					MailDto dto = new MailDto(conf);
					list2.add(dto);
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("totalCount", count);
			map.put("rows", list2);
			return map;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.debug("跳出[SqlMgrController][queryMailInfo]");
		return null;
	}

	/**
	 * 新增或修改sql邮件信息表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	/*
	 * /** 新增或修改sql邮件信息表
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return
	 * 
	 * @throws ProcessException
	 */
	@RequestMapping(value = "addOrEditMailInfo")
	public ModelAndView addOrEditMailInfo(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException {
		logger.debug("进入[SqlMgrController][addOrEditMailInfo]");
		// 返回结果

		String result = "0";
		try {

			// 获得登录人员信息
			HttpSession session = request.getSession(true);
			BiUser user = (BiUser) session.getAttribute("user");
			String loginId = user.getId();

			String id = request.getParameter("id");

			String subject = request.getParameter("subject");
			String sendto = request.getParameter("sendto");
			sendto = sendto.replace(" ", "");
			String cc = request.getParameter("cc");
			cc = cc.replace(" ", "");
			String ruleTable = request.getParameter("ruleTable");
			String ruleZeusTable = request.getParameter("ruleZeusTable");
			String ruleIdList = request.getParameter("ruleIdList");
			String crontab = request.getParameter("crontab");
			String isAvild = request.getParameter("isAvild");
			String ifSendMs = request.getParameter("ifSendMs");
			String mobileNum = request.getParameter("mobileNum");
			String msContent = request.getParameter("msContent");
			String isSend = request.getParameter("isSend");
			String isAutomation = request.getParameter("isAutomation");
			String isSuccess = request.getParameter("isSuccess");
			logger.info("id:" + id);

			// 修改
			if (id != null && !"".equals(id)) {
				DdsMailConf info = ddsMailConfService.findMailConfById(id);
					// 停止job
					SystemCore.getSystemCore().getSchedulerService()
							.delSheduleJob(SchedulerService.Conf2ScheduleJob(info,ScheduleConstants.MY_JOB_MAIL));
				if (subject != null && !"".equals(subject)) {
					info.setSubject(subject);
				}

				if (sendto != null && !"".equals(sendto)) {
					if (cc != null && !"".equals(cc)) {
						sendto += "-";
						sendto += cc;
					}
					info.setSendMail(sendto);
				}

				if (ruleIdList != null && !"".equals(ruleIdList)) {
					info.setRuleIdList(ruleIdList);

				}
				if (ruleTable != null && !"".equals(ruleTable)) {
					info.setRuleTable(ruleTable);
				}
				if (isAvild != null && !"".equals(isAvild)) {
					info.setIsAvild(Integer.parseInt(isAvild));
				}
				if (crontab != null && !"".equals(crontab)) {
					info.setCrontab(crontab);
				}
				if (ifSendMs != null && !"".equals(ifSendMs)) {
					info.setIsSendMs(Integer.parseInt(ifSendMs));
				}
				if (mobileNum != null && !"".equals(mobileNum)) {
					info.setMobileNum(mobileNum);
				}
				if (msContent != null && !"".equals(msContent)) {
					info.setMsContent(msContent);
				}
				if (isSend != null && !"".equals(isSend)) {
					info.setIsSendEmpty(Integer.parseInt(isSend));
				}
				if (ruleZeusTable != null ){
					info.setRuleZeusTable(ruleZeusTable);
				}
				
				ddsMailConfService.updateDdsMailConf(loginId, info);
				if(info.getIsAvild() == 1){
					SystemCore.getSystemCore().getSchedulerService()
					.addSheduleJob(SchedulerService.Conf2ScheduleJob(info,ScheduleConstants.MY_JOB_MAIL));
				}
			

				result = "2";// 修改成功

			} else {// 新增

				DdsMailConf info = new DdsMailConf();
				if (subject != null && !"".equals(subject)) {
					info.setSubject(subject);
				}
				if (sendto != null && !"".equals(sendto)) {
					if (cc != null && !"".equals(cc)) {
						sendto += "-";
						sendto += cc;
					}
					info.setSendMail(sendto);
				}
				if (ruleIdList != null && !"".equals(ruleIdList)) {
					info.setRuleIdList(ruleIdList);
				}
				if (isAvild != null && !"".equals(isAvild)) {
					info.setIsAvild(Integer.parseInt(isAvild));
				}
				if (crontab != null && !"".equals(crontab)) {
					info.setCrontab(crontab);
				}
				if (ifSendMs != null && !"".equals(ifSendMs)) {
					info.setIsSendMs(Integer.parseInt(ifSendMs));
				}
				if (ruleTable != null && !"".equals(ruleTable)) {
					info.setRuleTable(ruleTable);
				}
				if (mobileNum != null && !"".equals(mobileNum)) {
					info.setMobileNum(mobileNum);
				}
				if (msContent != null && !"".equals(msContent)) {
					info.setMsContent(msContent);
				}
				if (isSend != null && !"".equals(isSend)) {
					info.setIsSendEmpty(Integer.parseInt(isSend));
				}
				if (ruleZeusTable != null && !"".equals(ruleZeusTable)){
					info.setRuleZeusTable(ruleZeusTable);
				}

				info.setIsDeleted(0);
				int ids = ddsMailConfService.addDdsMailConf(loginId, info);
				
				if(info.getIsAvild() == 1){
					SystemCore.getSystemCore().getSchedulerService()
					.addSheduleJob(SchedulerService.Conf2ScheduleJob(info,ScheduleConstants.MY_JOB_MAIL));
				}
				result = "1";// 新增成功

			}
			logger.debug("result:" + result);
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			out = response.getWriter();
			out.print(result);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.debug("跳出[SqlMgrController][addOrEditMailInfo]");
		return null;
	}

	/**
	 * 新增或修改sql
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	@RequestMapping(value = "addOrEditSql")
	public ModelAndView addOrEditSql(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException {
		logger.debug("进入[SqlMgrController][addOrEditSql]");
		// 返回结果
		String result = "0";
		try {

			// 获得登录人员信息
			HttpSession session = request.getSession(true);
			BiUser user = (BiUser) session.getAttribute("user");
			String loginId = user.getId();

			String databaseId = request.getParameter("databaseId");

			String id = request.getParameter("id");
			String subject = request.getParameter("subject");
			String ruleSql = request.getParameter("ruleSql");
			String sqlParam = request.getParameter("sqlParam");
			sqlParam = sqlParam == null ? null : sqlParam.trim();
			String isAvild = request.getParameter("isAvild");
			String system = request.getParameter("system");
			String proName = request.getParameter("proName");
			String excelName = request.getParameter("excelName");
			String sheetName = request.getParameter("sheetName");
			logger.info("id:" + id);

			logger.info("databaseId:" + databaseId);
			logger.info("subject:" + subject);
			logger.info("ruleSql:" + ruleSql);
			logger.info("isAvild:" + isAvild);
			logger.info("system:" + system);
			logger.info("proName:" + proName);
			logger.info("excelName:" + excelName);
			logger.info("sheetName:" + sheetName);
			int newId = -1;
			// 修改
			if (id != null && !"".equals(id)) {
				// DdsRuleConf info = ddsMailConfService.findOraDqcRule(loginId,
				// Long.parseLong(id),databaseId);
				DdsRuleConf info = ddsRuleConfService.findDdsRuleById(id);
				if (subject != null && !"".equals(subject)) {
					info.setSubject(subject);
				}
				if (ruleSql != null && !"".equals(ruleSql)) {
					if (ruleSql.length() < 3000) {
						info.setRuleSql(ruleSql);
					}

				}
				if (sqlParam != null) {
					info.setSqlParam(sqlParam);
				}
				if (isAvild != null && !"".equals(isAvild)) {
					info.setIsAvild(Integer.parseInt(isAvild));
				}
				if (system != null && !"".equals(system)) {
					info.setSysName(system);
				}
				if (proName != null) {
					info.setProName(proName);
				}
				if (excelName != null) {
					info.setExcelName(excelName);
				}
				if (sheetName != null) {
					info.setSheetName(sheetName);
				}
				if (databaseId != null) {
					info.setDbName(databaseId);
				}
				if (ruleSql != null && !"".equals(ruleSql)) {
					ruleSql = StringUtil.convert(ruleSql);
					info.setRuleSql(ruleSql);
				}
				ddsRuleConfService.UpdateDdsRuleConf(loginId, info);
				result = "2";// 修改成功

			} else {// 新增

				DdsRuleConf info = new DdsRuleConf();
				if (subject != null && !"".equals(subject)) {
					info.setSubject(subject);
				}
				if (ruleSql != null && !"".equals(ruleSql)) {
					if (ruleSql.length() < 4000) {
						ruleSql = StringUtil.convert(ruleSql);
						info.setRuleSql(ruleSql);
					}
				}
				if (sqlParam != null) {
					info.setSqlParam(sqlParam);
				}
				if (isAvild != null && !"".equals(isAvild)) {
					info.setIsAvild(Integer.parseInt(isAvild));
				}
				if (system != null && !"".equals(system)) {
					info.setSysName(system);
				}
				if (proName != null && !"".equals(proName)) {
					info.setProName(proName);
				}
				if (excelName != null && !"".equals(excelName)) {
					info.setExcelName(excelName);
				}
				if (sheetName != null && !"".equals(sheetName)) {
					info.setSheetName(sheetName);
				}
				if (databaseId != null && !"".equals(databaseId)) {
					info.setDbName(databaseId);
				}
				if (ruleSql != null && !"".equals(ruleSql)) {
					ruleSql = StringUtil.convert(ruleSql);
					info.setRuleSql(ruleSql);
				}
				info.setCreateTime(new Date());
				newId = ddsRuleConfService.addDdsRuleConf(loginId, info);
				result = "1";// 新增成功

			}
			logger.debug("result:" + result);
			outPrintJSON("{id: " + newId + ",r: '" + result + "'}", response);

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.debug("跳出[SqlMgrController][addOrEditSql]");
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	/*
	 * public ModelAndView delOraDqcMail(HttpServletRequest request,
	 * HttpServletResponse response)throws ProcessException{
	 * logger.debug("进入[SqlMgrController][delOraDqcMail]"); //返回结果 String result
	 * = "-1"; try{
	 * 
	 * //获得登录人员信息 HttpSession session = request.getSession(true); BiUser user =
	 * (BiUser)session.getAttribute("user"); String loginId = user.getId();
	 * 
	 * String id = request.getParameter("id"); //id
	 * 
	 * DdsMailConf info = ddsMailConfService.findOraDqcMailInfo(loginId,
	 * Long.parseLong(id));
	 * 
	 * //删除 ddsMailConfService.delOraDqcMail(loginId, info); result = "1";
	 * 
	 * logger.debug("result:"+result); PrintWriter out = null;
	 * response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码 out =
	 * response.getWriter(); out.print(result);
	 * 
	 * }catch (Exception e) { logger.error(e); e.printStackTrace(); }
	 * 
	 * logger.debug("跳出[SqlMgrController][delOraDqcMail]"); return null; }
	 */

	@RequestMapping(value = "updateDeletedDqcRule")
	public ModelAndView updateDeletedDqcRule(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException {
		logger.debug("进入[SqlMgrController][delOraDqcMail]");
		// 返回结果
		String result = "-1";
		try {
			String id = request.getParameter("id");

			DdsMailConf mail = ddsMailConfService.findMailConfById(id);

			if (mail != null) {
				SystemCore
						.getSystemCore()
						.getSchedulerService()
						.delSheduleJob(
								SchedulerService.Conf2ScheduleJob(mail,
										ScheduleConstants.MY_JOB_MAIL));
			}

			ddsMailConfService.delDdsMailConfById("", id);
			// 删除
			result = "1";

			logger.debug("result:" + result);
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			out = response.getWriter();
			out.print(result);

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}

		logger.debug("跳出[SqlMgrController][delOraDqcMail]");
		return null;
	}

	/**
	 * 将前台传入的日期进行判断转换
	 * 
	 * @param date
	 * @return
	 */
	/*
	 * public String dateFormattransform(String date){
	 * logger.debug("进入[SqlMgrController][dateFormattransform]"); String datestr
	 * = ""; Pattern pattern1 = Pattern.compile("[0-9]{4}-[0-9]{2}");//年份，月份
	 * Pattern pattern2 = Pattern.compile("[0-9]{4}-");//无月份 Pattern pattern3 =
	 * Pattern.compile("-[0-9]{2}");//无年份
	 * if(date==null||date.equals("")||date.equals("-")){ date=null; datestr =
	 * date; return datestr; }else{ Matcher ma1 = pattern1.matcher(date);
	 * Matcher ma2 = pattern2.matcher(date); Matcher ma3 =
	 * pattern3.matcher(date); while(ma1.find()){ datestr = date; return
	 * datestr; } while(ma2.find()){//输入的日期没有月份dqcMail datestr =
	 * date.substring(0, date.length()-1); return datestr; }
	 * while(ma3.find()){//输入的日期可能没有年份或者没有月份 datestr = date.replaceFirst("-",
	 * ""); return datestr; } }
	 * logger.debug("跳出[SqlMgrController][dateFormattransform]"); return
	 * datestr; }
	 */

	/**
	 * 测试sql
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	@RequestMapping(value = "/testSql")
	public ModelAndView testSql(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException {
		logger.debug("进入[SqlMgrController][testSql]");
		// 返回结果
		String result = "0";
		try {

			// 获得登录人员信息
			HttpSession session = request.getSession(true);
			BiUser user = (BiUser) session.getAttribute("user");
			String loginId = user.getId();

			String sqlSentence = request.getParameter("sqlSentence");
			String sqlParam = request.getParameter("sqlParam");
			String databaseId = request.getParameter("databaseId");

			logger.info("sqlSentence:" + sqlSentence);
			logger.info("databaseId:" + databaseId);

			DdsDBConf dbConf = ddsDBConfService.findDdsDBConf("", databaseId);

			int r = ddsRuleConfService.testSql(sqlSentence, sqlParam,
					databaseId, dbConf);
			// 测试sql
			// int r = ddsRuleConfService.testSql(sqlSentence, databaseId);
			result = String.valueOf(r);// 测试成功

			logger.debug("result:" + result);
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			out = response.getWriter();
			out.print(result);

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			try {
				out = response.getWriter();
				out.print(e.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		logger.debug("跳出[SqlMgrController][testSql]");
		return null;
	}

	/**
	 * 根据id查询sql信息
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	@RequestMapping(value = "getOraDqcRule")
	@ResponseBody
	public DdsRuleConf getOraDqcRule(HttpServletRequest request,
			HttpServletResponse response) {

		logger.debug("进入[SqlMgrController][getOraDqcRule]");
		// 返回的JSON字符串
		StringBuilder resultSb = new StringBuilder();
		try {
			// 获得登录人员信息
			HttpSession session = request.getSession(true);
			BiUser user = (BiUser) session.getAttribute("user");
			String loginId = user.getId();

			String id = request.getParameter("id"); // ora_dqc_rule的id

			String databaseId = request.getParameter("databaseId"); // ora_dqc_rule的id

			// 打印日志
			logger.info("id: === " + id);

			if (id != null && !"".equals(id)) {
				DdsRuleConf info = ddsRuleConfService.findDdsRuleById(id);
				return info;
			}
			logger.debug("resultSb: === " + resultSb.toString());

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.debug("跳出[SqlMgrController][getOraDqcRule]");
		return null;
	}

	/**
	 * 输出相关的字符串
	 * 
	 * @param retStr
	 *            要返回的字符串
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 */
	private void outPrintJSON(String retStr, HttpServletResponse response)
			throws IOException {
		PrintWriter out = null;
		response.setContentType("text/text;charset=UTF-8");
		out = response.getWriter();
		out.print(retStr);
	}

	/**
	 * 立即发送邮件的方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 * @throws IOException
	 */
	@RequestMapping("/sendMail")
	public ModelAndView sendMail(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException, IOException {
		logger.debug("进入[SqlMgrController][sendMail]");
		// 返回结果
		String result = "0";
		PrintWriter out = null;
		response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
		out = response.getWriter();
		try {

			// 获得登录人员信息
			HttpSession session = request.getSession(true);
			BiUser user = (BiUser) session.getAttribute("user");
			String loginId = user.getId();

			String id = request.getParameter("id");
			String mailstr = request.getParameter("mailstr");
			String sqlParam = request.getParameter("sqlParam");

			logger.info("id:" + id);
			logger.info("mailstr:" + mailstr);
			DdsMailConf mailBean = null;
			try {
				mailBean = ddsMailConfService.findMailConfById(id);
			} catch (Exception e) {
			}
			try {
				MailService mailService = SystemCore.getSystemCore()
						.getMailService();
				if(id != null){
					String[] ids = id.split(",");
					for(String ide : ids){
						mailService.sendMail(ide, mailstr, sqlParam,true);
					}
				}
				
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
			// 测试sql
			// result = String.valueOf(r);//测试成功
			logger.debug("result:MailID=" + id);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			out.print(result);
		}
		logger.debug("跳出[SqlMgrController][sendMail]");
		return null;
	}

	/**
	 * 连接刷新后台shell
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 * @throws IOException
	 */
	public ModelAndView updateShell(HttpServletRequest request,
			HttpServletResponse response) throws ProcessException, IOException {
		logger.debug("进入[SqlMgrController][updateShell]");
		// 返回结果
		String result = "0";
		PrintWriter out = null;
		response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
		out = response.getWriter();
		try {

			// 获得登录人员信息
			HttpSession session = request.getSession(true);
			BiUser user = (BiUser) session.getAttribute("user");
			String loginId = user.getId();
			// RmtShellExecutor exe = new
			// RmtShellExecutor(GetProDatas.getMailUrl(),
			// GetProDatas.getMailUser(), GetProDatas.getMailPass());
			// exe.exec(GetProDatas.getMailCommand3());
			result = "1";

			logger.debug("result:" + result);

			out.print(result);

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			result = "0";
			out.print(result);
		}
		logger.debug("跳出[SqlMgrController][updateShell]");
		return null;
	}

	/**
	 * 开启一个Job作业
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/scheduleJob")
	public ModelAndView scheduleJob(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int r = 0;
		try {
			String id = request.getParameter("jobId");
			String type = request.getParameter("type");
			String db = request.getParameter("db");
			String jobId = "";
			DdsMailConf mail = ddsMailConfService.findMailConfById(id);

			if (mail != null) {
				SystemCore
						.getSystemCore()
						.getSchedulerService()
						.addSheduleJob(
								SchedulerService.Conf2ScheduleJob(mail,
										ScheduleConstants.MY_JOB_MAIL));
				r = 1;
				mail.setIsAvild(1);
				ddsMailConfService.updateDdsMailConf("", mail);
			} else {
				r = 0;
			}

		} catch (Exception e) {
			r = 0;
			logger.error("Action DataAccessException Error:", e);
			throw e;

		}
		outPrintJSON("{r: " + r + "}", response);
		return null;
	}

	/**
	 * 停止一个已启动的Job
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "removeJob")
	public ModelAndView removeJob(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int r = 0;
		try {
			String jobId = request.getParameter("jobId");
			String type = request.getParameter("type");
			String db = request.getParameter("db");
			// jobManageService.stopJobs(jobId); //执行调度后变更Job的状态值为U开启状态

			DdsMailConf mail = ddsMailConfService.findMailConfById(jobId);

			if (mail != null) {
				SystemCore
						.getSystemCore()
						.getSchedulerService()
						.delSheduleJob(
								SchedulerService.Conf2ScheduleJob(mail,
										ScheduleConstants.MY_JOB_MAIL));
				r = 1;
				mail.setIsAvild(0);
				ddsMailConfService.updateDdsMailConf("", mail);
			} else {
				r = 0;
			}
		} catch (Exception e) {
			logger.error("Action Runtime Error:", e);
			throw e;
		}
		outPrintJSON("{r: " + r + "}", response);
		return null;
	}
}
