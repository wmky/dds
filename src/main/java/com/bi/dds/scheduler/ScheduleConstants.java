package com.bi.dds.scheduler;

/**
 * Quartz Constants
 *
 * @author:Slan
 */
public class ScheduleConstants {
	
    /**
     * 频繁执行类型
     */
    public static final String JOB_TYPE_FREQ = "F";

    /**
     * 按计划来执行的
     */
    public static final String JOB_TYPE_SCHEDULED = "S";
    
    /**
     * 手动执行计划
     */
    public static final String JOB_HAND_SCHEDULED = "H";
    
    /**
     * 任务状态启用
     */
    public static final String JOB_STATUS_USE = "U";

    /**
     * 任务状态暂停
     */
    public static final String JOB_STATUS_STOP = "S";

    /**
     * 任务状态已删除
     */
    public static final String JOB_STATUS_DEL = "D";

     /**
     * 任务执行成功
     */
    public static final String RESULT_SUCCESS = "S";

    /**
     * 任务执行失败
     */
    public static final String RESULT_FAIL = "F";
    
    /**
     * 系统启动自动调度
     */
    public static final String START_AUTORUN = "A";
    
    
    /**
     * 规则错误报警数
     */
    public static int ALERT_COUNT = 0;
    
    /**
     * 所有规则
     */
    public static int ALL_RULE = -1;
    
    /**
     * 已启动的规则
     */
    public static int START_RULE = 1;
    
    /**
     * 已停止的规则
     */
    public static int STOP_RULE = 0;
    
    /**
     * 所有邮件
     */
    public static int ALL_MAIL = -1;
    
    /**
     * 已启动的邮件
     */
    public static int START_MAIL = 1;
    
    /**
     * 已停止的邮件
     */
    public static int STOP_MAIL = 0;
    
    
    /**
     * job的类型(sql)
     */
    public static String MY_JOB_SQL = "sql";
    
    /**
     * job的类型(邮件:mail)
     */
    public static String MY_JOB_MAIL = "mail";
    
    /**
     * job的类型(删除以前的文件夹:deleteFolder)
     */
    public static String MY_JOB_DeleteFolder = "deleteFolder";
    /**
     * job的类型(etl发送邮件:etlSendMail)
     */
    public static String MY_JOB_EtlSendMail = "etlSendMail";
    
    /**
     * 将csv文件转成xls文件报错
     */
    public static int JOB_ERROR1 = 91;
    
    /**
     * 执行sql报错，sql自身的问题
     */
    public static int JOB_ERROR2 = 81;
    
    /**
     * 系统运行时错误
     */
    public static int JOB_ERROR3 = 71;
    
    /**
     * 执行存储过程时报错
     */
    public static int JOB_ERROR4 = 61;
    
    /**
     * ls命令执行结果分隔 字符串
     */
    public static String lsSplitStr = "|+|";
    /**
     * ls命令执行结果分隔 字符串转义
     */
    public static String lsSplitStrZhuanyi = "\\|\\+\\|";
    
    /**
     * 生成的待发送邮件文件夹后缀
     */
    public static String mailFolderPostfix = ".dir";
    
    /**
     * 手机号码之间的分割符
     */
    public static String mobileNumSplit = ";";
    
}
