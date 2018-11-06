package com.bi.dds.util;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wmky_kk on 2017-07-10.
 */
public class PostgreSqlDBUtil {
    private static Logger log = Logger.getLogger(PostgreSqlDBUtil.class);


    public static List<String[]> postgresqlExecute(String sql,String[] heads,String driver, String url, String user, String password){
        List<String[]> resultList = new ArrayList<String[]>();

        log.info("执行postgreSql sql:" + sql);

        Connection connection = null;

        Statement stmt = null;

        ResultSet rs = null;

        try{
            Class.forName(driver);
            connection = DriverManager.getConnection(url,user,password);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rss = rs.getMetaData();

            int columnNum = rss.getColumnCount();

            if(heads == null){
                heads = new String[columnNum];
                for(int i = 0 ;i < columnNum ; i ++){
                    heads[i] = rss.getColumnName(i + 1);
                }
                resultList.add(heads);
            }
            while (rs.next()) {
                String[] body = new String[columnNum];
                for(int i = 0 ;i < columnNum ; i ++){
                    body[i] = 	rs.getString(i + 1);
                }
                resultList.add(body);
            }

            rs.close();
            connection.close();

        }
        catch (Exception e){
            log.error(e);
            if(rs != null ){
                try {
                    rs.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            if(connection != null ){
                try {
                    connection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return resultList;
    }

    public static void main(String[] args) throws SQLException {
        try{
            Class.forName("org.postgresql.Driver");
        }

        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","123456");
        Statement stmt = connection.createStatement();
        String sql = "select * from \"dim_user\"";

        ResultSet rs = stmt.executeQuery(sql);

        ResultSetMetaData rss = rs.getMetaData();

        while (rs.next()) {

            System.out.println(rss.getColumnName(1));
            System.out.println(rs.getString(1));
            System.out.println(rss.getColumnName(2));
            System.out.println(rs.getString(2));

        }

        rs.close();
        connection.close();
    }
}
