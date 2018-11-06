package com.bi.dds.service.impl;

import com.bi.dds.model.DdsMailConf;
import com.bi.dds.service.DdsMailConfService;
import com.bi.dds.service.JobZeusCheckService;
import com.bi.dds.system.MultipleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingshi on 2016/4/19.
 */
@Service(value = "jobZeusCheckService")
public class JobZeusCheckServiceImpl implements JobZeusCheckService{

    @Autowired
    private MultipleDataSource multipleDataSource;

    @Autowired
    private DdsMailConfService ddsMailConfService;

    public boolean isJobCompleted(String mailId){
        DdsMailConf mailbean = ddsMailConfService.findMailConfById(mailId);
        String zeusIds = "";
        if(mailbean != null && mailbean.getRuleZeusTable() != null && mailbean.getRuleZeusTable().length()> 1){
            zeusIds = mailbean.getRuleZeusTable();
        }else{
            return true;
        }
        MultipleDataSource.setDataSourceKey("zeusDataSource");

        Connection conn = null;
        try {

            String[] tmpIds = zeusIds.split(",");
            List<String> ids = new ArrayList<String>( );
            for (String s :tmpIds){
                String [] idt = s.split(";");
                for(String id : idt){
                    ids.add(id);
                }
            }
            zeusIds = zeusIds.replace(";", ",");
            conn = multipleDataSource.getConnection();

            String sql = "select job_id,max(CONCAT_WS('~',start_time,IFNULL(end_time,''),`status`)) sss from " +
                    "zeus2.zeus_job_history t " +
                    "where job_id in ("+zeusIds+") " +
                    " and date(start_time) = date(now()) " +
                    "group by job_id";
            //String sql = "select count(1) from zeus2.zeus_job_history a where a.job_id in("+zeusIds+") and ";
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            int successCount = 0;
            while(rs.next()) {
                String res = rs.getString(2);
                try{
                    String []  args = res.split("~");
                    String args2 = args[1];
                    String args3 = args[2];
                    if(args2 != null && args2.length() > 5 && "success".equals(args3)){
                        successCount ++;
                    }
                }catch(Exception e){

                }
            }
            if(successCount == ids.size()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {

        }catch(Exception e2){

        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
            MultipleDataSource.setDataSourceKey("dataSource");
        }
        return true;
    }

}
