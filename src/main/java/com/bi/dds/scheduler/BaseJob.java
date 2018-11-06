package com.bi.dds.scheduler;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 * 基础实现任务，所有的任务都必须扩展此类
 *
 * @author:TANTOM
 */
abstract public class BaseJob implements Job {
	
	private static Logger logger=Logger.getLogger(BaseJob.class);
	private String parameter;	  //其他参数
    private Date nextRunTime;	  //下次执行时间
    private String jobType;	  	  //执行方式(自动执行、手动执行)
    private String jobId;		  //作业ID
    private String id;			  //数据库中该记录的id
    private String type;		  //job的类型
    
    private Trigger trigger;//触发器对象
    public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public JobDetail getJob() {
		return job;
	}

	public void setJob(JobDetail job) {
		this.job = job;
	}

	private JobDetail job;//任务明细对象
	
	
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
     * 执行任务
     * @ldm
     * @param context
     * @throws JobExecutionException
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	//先还是查找已有的计划任务，如果当前时间没有获取到计划任务，就查找已经完成的ETL作业
//    	boolean b = true;
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
//        if(!StringUtils.isNotBlank(dataMap.getString("job.id"))){
////        	context.getJobDetail().getJobDataMap().clear();
////        	context.getJobDetail().getJobDataMap().putAll(new FindDoneJob().findJob());
//        	dataMap = new FindDoneJob().findJob();
//        	b = false;
//        }
		String jobId = dataMap.getString("job.jobId");
//		System.out.println(jobId);
		String jobType = dataMap.getString("job.jobType");
		String id = dataMap.getString("job.id");
		String type = dataMap.getString("job.type");
        
        if (StringUtils.isNotBlank(jobId)) {
        	setJobId(jobId);
        }
        if (StringUtils.isNotBlank(id)) {
        	setId(id);
        }
        
        if (StringUtils.isNotBlank(type)) {
        	setType(type);
        }
        setJobType(jobType);
        try {
            /** 执行由具体类所实现的方法 */
        	run();

        } catch (Throwable ex) {
        	logger.error(ex);
            ex.printStackTrace();
        }
    }

	/**
     * 开始执行任务
     * @注意：实现的方法必须自己控制事务与连接的关闭处理
     */
    abstract public void run() throws Exception;
    
    
    /**
     * 格式化时间
     * @param date
     * @return
     */
	protected String formatDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	protected String getParameter() {
        return parameter;
    }

    public void setParameter(String param) {
        parameter = param;
    }
    
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public Date getNextRunTime() {
		return nextRunTime;
	}

	public void setNextRunTime(Date nextRunTime) {
		this.nextRunTime = nextRunTime;
	}

}
