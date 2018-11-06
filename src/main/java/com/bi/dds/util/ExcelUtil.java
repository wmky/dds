package com.bi.dds.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	private static final Logger logger = Logger.getLogger(ExcelUtil.class);
	
	public static int parse(List<String[]> results, String[] heads,String xmlsName, String sheetName,String filepath) throws IOException
			  {
			try {
				
				File file = new File(xmlsName);
			
				if(!file.exists()){
					if( !new File(filepath).exists()){
						new File(filepath).mkdirs();
					}
					file.createNewFile();
					Workbook wb = new XSSFWorkbook();
					Sheet s = wb.createSheet(sheetName);
					Row r = null;
					Cell c = null;
					CellStyle cs = wb.createCellStyle();
					DataFormat df = wb.createDataFormat();
					Font f = wb.createFont();
					f.setFontHeightInPoints((short) 12);
					f.setColor(IndexedColors.RED.getIndex());
					f.setBoldweight(Font.BOLDWEIGHT_BOLD);
					cs.setFont(f);
					cs.setDataFormat(df.getFormat("#,##0.0"));

					r = s.createRow(0);
					if(heads != null){
						for (int cellnum = 0; cellnum < heads.length; cellnum ++) {
							c = r.createCell(cellnum);
							c.setCellValue(heads[cellnum]);
						}
						for (int rownum = 0; rownum < results.size(); rownum++) {
						    r = s.createRow(rownum + 1);
						    String[] result = results.get(rownum);
							for (int cellnum = 0; cellnum < result.length; cellnum ++) {
								c = r.createCell(cellnum);
								c.setCellValue(result[cellnum]);
							}
						}
					}else{
						for (int rownum = 0; rownum < results.size(); rownum++) {
						    r = s.createRow(rownum);
						    String[] result = results.get(rownum);
							for (int cellnum = 0; cellnum < result.length; cellnum ++) {
								c = r.createCell(cellnum);
								c.setCellValue(result[cellnum]);
							}
						}
					}
					
					
					// Save
//					if(!file.getParentFile().exists()){
//						file.getParentFile().mkdirs();
//					}
					FileOutputStream out = new FileOutputStream(xmlsName);
					wb.write(out);
					out.close();
				}else{
					Workbook wb = new XSSFWorkbook(new FileInputStream(file));
					Sheet sheet = wb.getSheet(sheetName);
					if( sheet != null){
						 int remove = wb.getSheetIndex(sheetName);
						 wb.removeSheetAt(remove);
					}
					Sheet s = wb.createSheet(sheetName);
					Row r = null;
					Cell c = null;
					CellStyle cs = wb.createCellStyle();
					DataFormat df = wb.createDataFormat();
					Font f = wb.createFont();
					f.setFontHeightInPoints((short) 12);
					f.setColor(IndexedColors.RED.getIndex());
					f.setBoldweight(Font.BOLDWEIGHT_BOLD);
					cs.setFont(f);
					cs.setDataFormat(df.getFormat("#,##0.0"));

					r = s.createRow(0);
					if(heads != null){
						for (int cellnum = 0; cellnum < heads.length; cellnum ++) {
							c = r.createCell(cellnum);
							c.setCellValue(heads[cellnum]);
						}
						for (int rownum = 0; rownum < results.size(); rownum++) {
						    r = s.createRow(rownum + 1);
						    String[] result = results.get(rownum);
							for (int cellnum = 0; cellnum < result.length; cellnum ++) {
								c = r.createCell(cellnum);
								c.setCellValue(result[cellnum]);
							}
						}
					}else{
						for (int rownum = 0; rownum < results.size(); rownum++) {
						    r = s.createRow(rownum);
						    String[] result = results.get(rownum);
							for (int cellnum = 0; cellnum < result.length; cellnum ++) {
								c = r.createCell(cellnum);
								c.setCellValue(result[cellnum]);
							}
						}
					}
					// Save
//					if(!file.getParentFile().exists()){
//						file.getParentFile().mkdirs();
//					}
					FileOutputStream out = new FileOutputStream(xmlsName);
					wb.write(out);
					out.close();
				}
				
			} catch (IOException e) {
				String msg = String.format("生成excel表失败，表名为%s，sheet名为%s", xmlsName, sheetName);
				logger.error("生成exel失败",e);
				throw new IOException(msg, e);
			}
			return 0;

	}
	

}
