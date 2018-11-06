package com.bi.dds.system;


import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.bi.dds.model.DdsMailConf;
import com.bi.dds.scheduler.ScheduleConstants;
import com.bi.dds.scheduler.SchedulerService;
import com.bi.dds.service.DdsMailConfService;

/**
 * 调度系统启动Servlet
 * @ldm
 */ 
public class SystemInitServlet extends HttpServlet{

	//对象
	private static final Logger logger = Logger.getLogger(SystemInitServlet.class);
		
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 系统启动初始化所有已经设定为启用的作业Job
	 */
	public void init() throws ServletException {
		
		logger.debug("系统初始化开始！");
		
		SystemCore.getSystemCore().startUp(this.getServletContext());
		
		
		DdsMailConfService ddsMailConfService = (DdsMailConfService)SystemCore.getSystemCore().findBean("ddsMailConfService",this.getServletContext());
		
		
		logger.debug("调度JOB初始化 ！开始"); 
		
			logger.debug("将删除文件夹的job加入调度：");
			//将删除文件夹的job加入
			SystemCore.getSystemCore().getSchedulerService().addSheduleJob(SchedulerService.Conf2ScheduleJob(null,ScheduleConstants.MY_JOB_DeleteFolder));
			
			logger.debug("将所有已经开启的mail的job加入调度：");
			//得到所有已经开启的mail的job
			List<DdsMailConf> maillist = ddsMailConfService.selectAllAvildMailConfs();
			if(maillist!=null){
				for(DdsMailConf mail: maillist) {
					if(mail!=null && mail.getIsAvild()==1){
						SystemCore.getSystemCore().getSchedulerService().addSheduleJob(SchedulerService.Conf2ScheduleJob(mail,ScheduleConstants.MY_JOB_MAIL));
					}
				}
			}
		
		logger.debug("调度JOB初始化 ！结束");
		//系统初始化错误记录给常量
		//ScheduleConstants.ALERT_COUNT = jqd.getFailCountByRule();
		
		//System.out.println("FailCount---"+ScheduleConstants.ALERT_COUNT);
		logger.debug("系统初始化完成！");
	}
	
	
	
	


	
}
