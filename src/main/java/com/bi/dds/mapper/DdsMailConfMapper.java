package com.bi.dds.mapper;

import com.bi.dds.model.DdsMailConf;
import com.bi.dds.model.DdsMailConfExample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DdsMailConfMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    int countByExample(DdsMailConfExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    int deleteByExample(DdsMailConfExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    int insert(DdsMailConf record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    int insertSelective(DdsMailConf record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    List<DdsMailConf> selectByExample(DdsMailConfExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    DdsMailConf selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DdsMailConf record, @Param("example") DdsMailConfExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DdsMailConf record, @Param("example") DdsMailConfExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DdsMailConf record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_mail_conf
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DdsMailConf record);

	int deleteLogicByPrimaryKey(int parseInt);
	
	List<DdsMailConf> queryMailInfoList(Map<String,String> map);
	
	int countMailInfoList(Map<String,String> map);
}