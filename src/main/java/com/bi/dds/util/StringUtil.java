package com.bi.dds.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtil {
	public static String convert(String s){
		
		if(s == null)
			return null;
//		s = s.toLowerCase().replaceAll(";", "");
//		s = s.toLowerCase().replaceAll("&gt", ">");
//		s = s.toLowerCase().replaceAll("&lt", "<");
		s = s.replaceAll("&gt;", ">");
		s = s.replaceAll("&lt;", "<");
		s = s.replaceAll("&quot;","\"");

		return s;
	}

	/**
	 * 默认存入#date# 为昨日日期yyyy-mm-dd
	 * @param sql
	 * @param param
	 * @return
	 */
	public static String paramConvert(String sql,String param){
		String tmpSql = "";
		if(param == null || "".equals(param)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date beginDate = new Date();
			Calendar date = Calendar.getInstance();
			date.setTime(beginDate);
			date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
			Date endDate = null;
			try {
				endDate = sdf.parse(sdf.format(date.getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String date2 = sdf.format(endDate);
			tmpSql = sql.replaceAll("#date#", date2);
		}else{
			tmpSql= sql.replaceAll("#date#", param.trim());
		}
		return tmpSql;
	}
	
	public static void main(String[] args) {
//		System.out.println(paramConvert("select date_id,year_name,month_of_year_name from test.dim_date where date_id >'#date#' limit 10",""));
		System.out.println(Integer.parseInt("29") == 29);
		
//		System.out.println(convert2("where a.date_id &gt; ("));
	}
}
