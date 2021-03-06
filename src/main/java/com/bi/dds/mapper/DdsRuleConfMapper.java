package com.bi.dds.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bi.dds.model.DdsRuleConf;
import com.bi.dds.model.DdsRuleConfExample;

public interface DdsRuleConfMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    int countByExample(DdsRuleConfExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    int deleteByExample(DdsRuleConfExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    int insert(DdsRuleConf record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    int insertSelective(DdsRuleConf record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    List<DdsRuleConf> selectByExample(DdsRuleConfExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    DdsRuleConf selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DdsRuleConf record, @Param("example") DdsRuleConfExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DdsRuleConf record, @Param("example") DdsRuleConfExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DdsRuleConf record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_rule_conf
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DdsRuleConf record);

	int DeleteLogicByPrimaryKey(Integer id);
	
	List<DdsRuleConf> queryRuleInfoList(Map<String,String> map);
	
	int countByQueryRuleInfoList(Map<String,String> map);
}