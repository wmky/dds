package com.bi.dds.mapper;

import com.bi.dds.model.DdsExecLog;
import com.bi.dds.model.DdsExecLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DdsExecLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    int countByExample(DdsExecLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    int deleteByExample(DdsExecLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    int insert(DdsExecLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    int insertSelective(DdsExecLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    List<DdsExecLog> selectByExample(DdsExecLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    DdsExecLog selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DdsExecLog record, @Param("example") DdsExecLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DdsExecLog record, @Param("example") DdsExecLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DdsExecLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dds_exec_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DdsExecLog record);
}