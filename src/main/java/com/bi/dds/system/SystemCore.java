package com.bi.dds.system;

import javax.servlet.ServletContext;

import com.bi.dds.scheduler.JobZeusCheck;
import com.bi.dds.service.JobZeusCheckService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bi.dds.mail.MailService;
import com.bi.dds.scheduler.SchedulerService;


/**
 * 
 * @author Chai
 *
 * 
 */
public class SystemCore {
	
	
	
	private static Logger log=Logger.getLogger(SystemCore.class);
	
	protected static SystemCore systemCore;//系统内核 单例
	
	private SchedulerService schedulerService;//调度服务
	
	
	private MailService mailService;//邮件服务

	private JobZeusCheckService jobZeusCheckService;
	
	private static WebApplicationContext context;
	
	public ApplicationContext ctx = null;
	
	protected SystemCore(){
		
		systemCore=null;
		setSchedulerService(new SchedulerService());
		setMailService(new MailService());
	}
	
	/**
	 * 
	 * @return
	 */
	public static SystemCore getSystemCore(){
		if(systemCore==null){
			log.info("初始化系统内核");
			systemCore=new SystemCore();
		}
		return systemCore;
	}
	
	/**
	 *
	 * @throws BaseException
	 */
	public synchronized void startUp(ServletContext arg0){
		JobZeusCheck jzc = new JobZeusCheck();
		jzc.start();
		log.info("启动 调度服务！");
		this.getSchedulerService().startUp(arg0);
		log.info("启动 邮件服务！");
		this.getMailService().startUp(arg0);

	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public SchedulerService getSchedulerService() {
		return schedulerService;
	}

	public void setSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}

	public JobZeusCheckService getJobZeusCheckService() {
		return jobZeusCheckService;
	}

	public void setJobZeusCheckService(JobZeusCheckService jobZeusCheckService) {
		this.jobZeusCheckService = jobZeusCheckService;
	}

	public Object findBean(String beanName,ServletContext arg0) {
	     if (context == null) {
	    	 context = WebApplicationContextUtils.getWebApplicationContext(arg0);
	     }
	     return context.getBean(beanName);
	}
}
