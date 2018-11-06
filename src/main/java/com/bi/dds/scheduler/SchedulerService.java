package com.bi.dds.scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.bi.dds.model.DdsMailConf;
import com.bi.dds.scheduler.jobimpl.DeleteFolderJob;
import com.bi.dds.scheduler.jobimpl.RunMailJob;
import com.bi.dds.util.GetProDatas;


/**
 * 调度服务类
 * @author ChaiYing
 *
 */
public class SchedulerService {
	
	private static Logger log = Logger.getLogger(SchedulerService.class);
	
	protected Scheduler schedulerCore;//调度内核对像
	
	
	/**
	 * 初始化
	 * @throws SchedulerException
	 */
	public void init(){
		try{
			if(schedulerCore==null){
				schedulerCore=StdSchedulerFactory.getDefaultScheduler();
			}
		} catch ( Exception e ){
			log.error("创建调度服务实例时发生错误 调度服务初始化失败",e);
		}

	}
	
	
	
	/**
	 * 关闭调度服务
	 * @throws SchedulerException
	 */
	public  void shutdown(){
		try {
			if((schedulerCore!=null)&&(schedulerCore.isStarted())){
				log.info("关闭调度服务");
				schedulerCore.shutdown();
				log.info("释放调度服务对象");
				schedulerCore=null;
			}
		} catch ( Exception e ) {
			log.error("关闭调度服务 出现错误 关闭调度服务 失败",e);
		}

	}
	
	/**
	 * 加入调度任务
	 * @param newJob
	 * @throws SchedulerException
	 */
	public void addSheduleJob(BaseJob newJob){
		try {
			log.info("建立 触发器 与 任务行为 对象");
			log.info("加入到调度服务中");
			schedulerCore.scheduleJob(newJob.getJob(),newJob.getTrigger() );
		} catch ( Exception e ) {
			log.error("加入调度任务 出现错误 加入调度任务 失败",e);
		}
	}
	
	/**
	 * 删除调度任务
	 * @param newJob
	 * @throws SchedulerException
	 */
	public void delSheduleJob(BaseJob delJob){
		try {
			boolean unscheduleJobResult=schedulerCore.unscheduleJob(delJob.getTrigger().getKey());
			log.debug("删除任务"+delJob+"结果为："+unscheduleJobResult);
		} catch ( Exception e ) {
			log.error("删除调度任务 出现错误 删除调度任务 失败",e);
		}
	}
	
	
	
	/**
	 * 把JOB的配置信息，转变为调度的JOB
	 * 
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static BaseJob Conf2ScheduleJob(Object obj, String type) {
		BaseJob confJob = null;
		try {
			String jobId = "";
			String cronExpression = "";
			String id = "";
			JobDetail jobDetail = null;
			if (type.equals(ScheduleConstants.MY_JOB_MAIL)) {
				DdsMailConf mail = (DdsMailConf) obj;
				jobId = type + mail.getId().toString();
				cronExpression = mail.getCrontab();
				id = mail.getId().toString();
				
				jobDetail = newJob(RunMailJob.class).withIdentity(jobId,
						Scheduler.DEFAULT_GROUP).build();
				
				confJob = new RunMailJob();
				
			}
			if (type.equals(ScheduleConstants.MY_JOB_DeleteFolder)) {
				jobId = type;
				cronExpression = GetProDatas.getDeleteCron();
				jobDetail = newJob(DeleteFolderJob.class).withIdentity(jobId,
						Scheduler.DEFAULT_GROUP).build();
				confJob = new DeleteFolderJob();
			}
			if (jobDetail != null) {
				JobDataMap jdm = jobDetail.getJobDataMap();

				jdm.put("job.jobId", jobId);
				jdm.put("job.id", id);
				jdm.put("job.type", type);
				jdm.put("job.jobType", ScheduleConstants.JOB_TYPE_SCHEDULED);

				cronExpression = convertOraCrontabToQuartz(cronExpression);

				CronExpression exp = new CronExpression(cronExpression);

				CronTrigger trigger = newTrigger()
						.withIdentity(jobId, Scheduler.DEFAULT_GROUP)
						.withSchedule(cronSchedule(cronExpression)).build();
				confJob.setJob(jobDetail);
				confJob.setTrigger(trigger);
			}
		} catch (Exception e) {
			log.error(e);
		}
		return confJob;
		
	}

	// 格式化时间
	protected String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(date);
	}

	// 将mysql的cron转化成quartz的
	protected static String convertOraCrontabToQuartz(String cronExpression) {
		String[] tempstr = cronExpression.split(" ");
		if (tempstr.length < 6) {// 说明是以前配的oracle的cron
			cronExpression = "0 " + cronExpression;
		}
		if (tempstr[tempstr.length - 1].equals(tempstr[tempstr.length - 3])) {// 说明日期和星期可能同时是*
			cronExpression = cronExpression.substring(0,
					cronExpression.lastIndexOf("*"))
					+ "?";
		}
		return cronExpression;
	}
	/**
	 * 启动调度服务
	 * @throws SchedulerException
	 */
	public void startUp(ServletContext arg0) {
		try {
			log.info("判断调度服务对象是否为空");
			if(schedulerCore==null){
				try {
					log.info("调度服务对象为空 进行调度服务初始化");
					init();
				} catch ( Exception e ) {
					log.error("进行调度服务初始化 发生错误 启动调度服务 失败",e);
				}

			} else {
				try{
					
					log.info("判断调度服务状态是否为关闭");
					if(schedulerCore.isShutdown()){
						log.info("调度服务状态为关闭 重置调度服务 并进行初始化");
						schedulerCore=null;
						init();
					} 
					
					if(schedulerCore.isStarted()){
						log.error("当前调度服务已经启动无法重新启动 启动调度服务失败");
					}
					
				} catch ( Exception e ) {
					log.error("判断调度服务状态是否为关闭 重置调度服务 并进行初始化 时 出现错误 启动调度服务失败",e);
				}

			}
			
			try {
				log.info("判断调度服务状态是否为预备状态");
				if(schedulerCore.isInStandbyMode()){
					log.info("调度服务状态为预备状态 启运调度服务");
					schedulerCore.start();
				}
			} catch ( Exception e ) {
				log.error("启动调度服务 出现错误 启动调度服务失败",e);
			}

		} catch ( Exception e ) {
			log.error("启动调度服务 出现错误 启动调度服务失败",e);
		}
	}
	
	
}
