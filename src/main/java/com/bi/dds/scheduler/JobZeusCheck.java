package com.bi.dds.scheduler;

import com.bi.dds.mail.MailService;
import com.bi.dds.model.DdsMailConf;
import com.bi.dds.service.DdsMailConfService;
import com.bi.dds.service.JobZeusCheckService;
import com.bi.dds.system.SystemCore;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xingshi on 2016/4/18.
 */
public class JobZeusCheck extends Thread{
    //private Hashtable<String,DdsMailConf> ZEUS_CHECK_JOB = new Hashtable<String, DdsMailConf>();
    private static Logger logger = Logger.getLogger(JobZeusCheck.class);
    public static ConcurrentHashMap<String,DdsMailConf> WAIT_SEND_JOB = new ConcurrentHashMap<String, DdsMailConf>();
    @Override
    public void run(){
        logger.info("zeus检测线程已经启动。。");
        while(true){
            logger.info("线程执行当前时间：" + System.currentTimeMillis());
            if(WAIT_SEND_JOB.isEmpty()){
                logger.info("当前没有待检测zeus依赖的邮件作业。。");
            }else{
                try{
                    for(Map.Entry<String,DdsMailConf> map : WAIT_SEND_JOB.entrySet()){
                        DdsMailConfService ddsMailConfService= SystemCore.getSystemCore().getMailService().getDdsMailConfService();
                        DdsMailConf dmc = ddsMailConfService.findMailConfById(map.getKey());
                        if(dmc.getRuleZeusTable() != null && dmc.getRuleZeusTable().length()>1){
                            JobZeusCheckService jobZeusCheckService = SystemCore.getSystemCore().getMailService().getJobZeusCheckService();
                            if(jobZeusCheckService.isJobCompleted(map.getKey())){
                                WAIT_SEND_JOB.remove(map.getKey());
                                logger.info("作业JOBID:" + map.getKey() + " zeus依赖均已完成，执行邮件发送。。");
                                SystemCore.getSystemCore().getMailService().sendMail(map.getKey(),null,null,false);
                            }else{
                                logger.info("作业ID:" + map.getKey() + " zeus依赖尚未完成，5分钟后重新检测调度。");
                            }
                        }else{
                            logger.info("作业JOBID:" + map.getKey() + " 貌似作业变更,zeus依赖没了，移除了。。。");
                            WAIT_SEND_JOB.remove(map.getKey());
                        }
                    }
                }catch(Exception e){
                    logger.error("异常了。。" + e.getMessage());
                }

            }
            try{
                Thread.sleep(1000*60*5);
                //Thread.sleep(1000*10);
            }catch(InterruptedException e){
                e.printStackTrace();
                logger.error("异常了。。" + e.getMessage());
            }
        }
    }

    public static void main(String [] args){
        System.out.println("asdf");
    }
}
