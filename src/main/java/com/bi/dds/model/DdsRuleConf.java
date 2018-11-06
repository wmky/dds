package com.bi.dds.model;

import java.util.Date;

public class DdsRuleConf{
    private Integer id;

    private String sysName;

    private String ruleSql;
    
    private String sqlParam;

    private String subject;

    private Integer isAvild;

    private Date createTime;

    private String proName;

    private String excelName;
	
    private String sheetName;

    private String dbName;
    
    private DdsDBConf ddsDBConf;
    
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dds_rule_conf.id
     *
     * @param id the value for dds_rule_conf.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dds_rule_conf.sys_Name
     *
     * @return the value of dds_rule_conf.sys_Name
     *
     * @mbggenerated
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dds_rule_conf.sys_Name
     *
     * @param sysName the value for dds_rule_conf.sys_Name
     *
     * @mbggenerated
     */
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dds_rule_conf.rule_sql
     *
     * @return the value of dds_rule_conf.rule_sql
     *
     * @mbggenerated
     */
    public String getRuleSql() {
        return ruleSql;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dds_rule_conf.rule_sql
     *
     * @param ruleSql the value for dds_rule_conf.rule_sql
     *
     * @mbggenerated
     */
    public void setRuleSql(String ruleSql) {
        this.ruleSql = ruleSql;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dds_rule_conf.subject
     *
     * @return the value of dds_rule_conf.subject
     *
     * @mbggenerated
     */
    public String getSubject() {
        return subject;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dds_rule_conf.subject
     *
     * @param subject the value for dds_rule_conf.subject
     *
     * @mbggenerated
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dds_rule_conf.is_avild
     *
     * @return the value of dds_rule_conf.is_avild
     *
     * @mbggenerated
     */
    public Integer getIsAvild() {
        return isAvild;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dds_rule_conf.is_avild
     *
     * @param isAvild the value for dds_rule_conf.is_avild
     *
     * @mbggenerated
     */
    public void setIsAvild(Integer isAvild) {
        this.isAvild = isAvild;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dds_rule_conf.create_time
     *
     * @return the value of dds_rule_conf.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dds_rule_conf.create_time
     *
     * @param createTime the value for dds_rule_conf.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dds_rule_conf.pro_name
     *
     * @return the value of dds_rule_conf.pro_name
     *
     * @mbggenerated
     */
    public String getProName() {
        return proName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dds_rule_conf.pro_name
     *
     * @param proName the value for dds_rule_conf.pro_name
     *
     * @mbggenerated
     */
    public void setProName(String proName) {
        this.proName = proName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dds_rule_conf.excel_name
     *
     * @return the value of dds_rule_conf.excel_name
     *
     * @mbggenerated
     */
    public String getExcelName() {
        return excelName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dds_rule_conf.excel_name
     *
     * @param excelName the value for dds_rule_conf.excel_name
     *
     * @mbggenerated
     */
    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dds_rule_conf.sheet_name
     *
     * @return the value of dds_rule_conf.sheet_name
     *
     * @mbggenerated
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dds_rule_conf.sheet_name
     *
     * @param sheetName the value for dds_rule_conf.sheet_name
     *
     * @mbggenerated
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column dds_rule_conf.db_name
     *
     * @return the value of dds_rule_conf.db_name
     *
     * @mbggenerated
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column dds_rule_conf.db_name
     *
     * @param dbName the value for dds_rule_conf.db_name
     *
     * @mbggenerated
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

	public DdsDBConf getDdsDBConf() {
		return ddsDBConf;
	}

	public void setDdsDBConf(DdsDBConf ddsDBConf) {
		this.ddsDBConf = ddsDBConf;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}
    
    
}