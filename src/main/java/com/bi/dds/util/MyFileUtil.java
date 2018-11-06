package com.bi.dds.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MyFileUtil {
    public static String daysStr = GetProDatas.getDaysFileDelete();
	public static void zipFile(File inFile, ZipOutputStream zos, String dir) throws IOException {
        
	     if (inFile.isDirectory()) {                                                             
	                                                                                             
	         File[] files = inFile.listFiles();                                                  
	                                                                                             
	         for (File file:files)                                                               
	                                                                                             
	             zipFile(file, zos, dir + File.separator + inFile.getName());                              
	                                                                                             
	     } else {                                                                                
	                                                                                             
	         String entryName = null;                                                            
	                                                                                             
	         if (!"".equals(dir))                                                                
	                                                                                             
	             entryName = dir + File.separator + inFile.getName();                                      
	                                                                                             
	         else                                                                                
	                                                                                             
	             entryName = inFile.getName();                                                   
	                                                                                             
	         ZipEntry entry = new ZipEntry(entryName);                                           
	                                                                                             
	         zos.putNextEntry(entry);                                                            
	                                                                                             
	         InputStream is = new FileInputStream(inFile);                                       
	                                                                                             
	         int len = 0;                                                                        
	                                                                                             
	         while ((len = is.read()) != -1)                                                     
	                                                                                             
	             zos.write(len);                                                                 
	                                                                                             
	         is.close();                                                                         
	                                                                                             
	     }                                                                                       
	                                                                                             
	                                                                                             
	                                                                                             
	 }                                                                                           

	public  void deleteFile(File file){ 
        if(file.exists()){                    //判断文件是否存在
         if(file.isFile()){                    //判断是否是文件
          file.delete();                       //delete()方法 你应该知道 是删除的意思;
         }else if(file.isDirectory()){              //否则如果它是一个目录
          File files[] = file.listFiles();               //声明目录下所有的文件 files[];
          for(int i=0;i<files.length;i++){            //遍历目录下所有的文件
           this.deleteFile(files[i]);             //把每个文件 用这个方法进行迭代
          } 
         } 
         file.delete(); 
        }else{ 
         System.out.println("所删除的文件不存在！"+'\n'); 
        } 
     } 
    public static String getDateFileStrAgo(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -Integer.valueOf(daysStr));
        cal.setTime(cal.getTime());
        Date date = cal.getTime();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        String datefile = sd.format(date);
        return datefile;
    }
}
