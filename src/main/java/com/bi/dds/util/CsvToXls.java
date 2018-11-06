package com.bi.dds.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 总结：
 * 1变量名遵循Camel case
 * 2system.out.println() 换成logger
 * 3给各个方法写注释
 * 4finally块中关闭资源
 */

/**
 *  CsvToXlsFinal类 实现将csv文件转化为xls文件
 */
public class CsvToXls
{
	// 表示指到的cell单元格
	public int point = 0;
	//列数
	public int colCount=0; 
	// 行数
	public int rowCount = 0;
	// 用于存储每个cell单元
	public List<String> temp = new ArrayList();

	private static final Logger logger = Logger.getLogger(CsvToXls.class);
	
	public static String codeSet = GetProDatas.getCodeSet();

	public static int checkFile(File file) {
		List list =new ArrayList();
		BufferedReader bufferedreader = null;
		if (file.exists()) {
			//判断csv文件是否有值
			try {
				/*bufferedreader = new BufferedReader(new FileReader(file));
				String stemp;
			
				while((stemp = bufferedreader.readLine()) != null){
					if(stemp.contains("0 rows affected")){
						break;
					}
					       list.add(stemp);
					       if(list.size()>2){
					    	   return 1;//文件存在且，不为空
					       }
				}*/
				if(true)
					return 1;
			} catch (Exception e) {
				logger.error(e);
				return -998;//转换出错
			}
			return 2;//文件存在，但是数据为空
		}else {
			return 0;//文件不存在
		}
	}
	
	public static int checkSendFile(File file,String isSendEmpty) {
		List list =new ArrayList();
		BufferedReader bufferedreader = null;
		if (file.exists()) {
			//判断csv文件是否有值
			try {
				if("1".equals(isSendEmpty)){
					return 1;
				}else{
					bufferedreader = new BufferedReader(new FileReader(file));
					String stemp;
					
					while((stemp = bufferedreader.readLine()) != null){
						if(stemp.contains("0 rows affected")){
							logger.info("文件为空，邮件可能不会发送");
							break;
						}
						list.add(stemp);
						if(list.size()>2){
							return 1;//文件存在且，不为空
						}
					}
				}
			} catch (IOException e) {
				logger.error(e);
				return -998;//转换出错
			}finally{
				try {
					if(bufferedreader != null)
						bufferedreader.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
			
			return 2;//文件存在，但是数据为空
		}else {
			return 0;//文件不存在
		}
	}
	

	/**
	 * @param outXls xls文件的File对象
	 * @param sheetName	sheetname参数
	 * @return 
	 * 若存在sheetname的sheet返回TRUE
	 * 否则返回false
	 */
	public static boolean checkSheet(File outXls, String sheetName) {
		boolean ifExits = false;
		FileInputStream fis = null; 
		try {
			fis = new FileInputStream(outXls);
			//HSSFWorkbook wb = new HSSFWorkbook(fis);//--2003
			XSSFWorkbook wb = new XSSFWorkbook(fis); //--2007

			// 得到存在几个sheet
			int count = wb.getNumberOfSheets();
			//logger.log(Level.INFO, "存在:" + count + "个Sheet");
			
			for (int i = 0; i < count; i++) {
				//HSSFSheet st = wb.getSheetAt(i);//--2003
			    XSSFSheet st = wb.getSheetAt(i); //--2007
				String sheetNameTemp = st.getSheetName();
				//logger.log(Level.INFO, sheetNameTemp);

				if (sheetNameTemp.equalsIgnoreCase(sheetName)) 
				{
					//logger.log(Level.INFO, "该sheet已经存在...覆盖");
					ifExits = true;
					//找到直接结束方法
					//否则继续改变ifExits的值
					//这是个遍历
					return true;
				} 
				else
				{
					//logger.log(Level.INFO, "该sheet不存在...创建新的sheet");
					ifExits = false;
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return ifExits;

	}

	
	/**
	 * @param xls Xls文件的File对象
	 * @param newSheetName 新建的sheetName参数
	 * 实现在原有xls文件中追加创建以newSheetName命名的sheet
	 */
	public void newSheet(File xls, String newSheetName,String csv) {
		FileInputStream readFile = null;
		FileOutputStream writeFile = null;
		BufferedReader br = null;
		try {
			readFile = new FileInputStream(xls);
			//HSSFWorkbook wb = new HSSFWorkbook(readFile);
			//HSSFSheet st = wb.createSheet(newSheetName);
			XSSFWorkbook wb = new XSSFWorkbook(readFile);//07
			XSSFSheet st = wb.createSheet(newSheetName);//07

			/*String getCell = "";

			for (int j = 0; j < rowCount; j++) {
				st.createRow(j);
				for (int i = 0; i < colCount; i++) {
					getCell = temp.get(point);
					st.getRow(j).createCell(i).setCellValue(getCell);
					point++;// 指向下一个List元素
				}
			}*/
			//----
			//InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), codeSet);//春江
			InputStreamReader isr = new InputStreamReader(new FileInputStream(csv));//春江
		        br = new BufferedReader(isr);
				String line = "";
				int rownum = 0;
				while ((line = br.readLine()) != null) {
					if(line.contains("0 rows affected")){
						break;
					}
					
					String [] str = line.split("\\|\\+\\|");
					
					st.createRow(rownum);
					for (int i = 0; i < str.length; i++) {
						st.getRow(rownum).createCell(i).setCellValue(str[i]);
					}
					rownum++;
				}
			//----
			writeFile = new FileOutputStream(xls);// "c:/result.xls"
			wb.write(writeFile);
			/*Workbook wb = new XSSFWorkbook(readFile);
			Sheet st = wb.createSheet(newSheetName);

			String getCell = "";

			for (int j = 0; j < rowCount; j++) {
				st.createRow(j);
				for (int i = 0; i < colCount; i++) {
					getCell = temp.get(point);
					st.getRow(j).createCell(i).setCellValue(getCell);
					point++;// 指向下一个List元素
				}
			}

			writeFile = new FileOutputStream(xls);// "c:/result.xls"
			wb.write(writeFile);*/
		} catch (FileNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				readFile.close();
				writeFile.close();
				br.close();
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param csv csv文件的File对象
	 * 实现读取File对象指定的csv文件中的数据并放入list集合中
	 */
	public void readCsv(File csv) {
		// 读CSV////////////////////////////////
		BufferedReader br = null;
		try {
			// CSV文件 "C://cy.csv"

			InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), codeSet);//春江
			//InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), "gbk");//旭辉
	        br = new BufferedReader(isr);

			
			//br = new BufferedReader(new FileReader(csv));
			// 读取直到最后一行
			String line = "";
			// 存储一个单元格
			String str = "";

			while ((line = br.readLine()) != null) {
				// 把一行数据分割成多个字段
				// StringTokenizer是分词器 类 可用 split代替
				/*StringTokenizer st = new StringTokenizer(line, "\\|\\+\\|");
					
				colCount=st.countTokens();

				while (st.hasMoreTokens()) {
					// 每一行的多个字段用TAB隔开表示
					str = st.nextToken();// 得到一个单元并指向下一个，
					// 关键代码！
					// 将读到的cell放入
					temp.add(str);

					// 是否要打印到控制台
					// System.out.print(str + "\t");
				}*/
				if(line.contains("0 rows affected")){
					break;
				}
				String [] st = line.split("\\|\\+\\|");
				
				colCount=st.length;

				for (int i = 0; i < st.length; i++) {
					temp.add(st[i]);
				}
			}
			// 集合大小
			rowCount = temp.size() / colCount;
			
			if(rowCount==1){
				
			}

		} catch (FileNotFoundException e) {
			// 捕获File对象生成时的异常
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			// 捕获BufferedReader对象关闭时的异常
			logger.error(e);
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param CSVpath
	 *            CSV文件路径
	 * @param XLSpath
	 *            转换后XLS文件路径
	 * @param SheetName
	 *            Sheet名称
	 *  实现根据csv文件生成xls文件
	 *  xls文件如果存在 判断sheet是否存在
	 *  sheet存在则不覆盖          
	 *  
	 */
	public int transform(String csvPath, String xlsPath, String sheetName,String isSendEmpt) 
	{
		int r = 0;
		File csvFile = null;
		File xlsFile = null;
		try {
			csvFile = new File(csvPath);
			xlsFile = new File(xlsPath);

			logger.info("csv文件名称："+csvPath);
			logger.info("xls文件名称："+xlsPath);
			logger.info("sheet名称："+sheetName);
			int res = checkFile(xlsFile);
			if (res==1||res==2) {
				if (checkSheet(xlsFile, sheetName)){
					coverSheet(xlsFile, sheetName,csvPath);
				}else {
					newSheet(xlsFile, sheetName,csvPath);
				}
			} else {
				writeXls(xlsPath, sheetName,csvPath);
			}
			
			if(checkSendFile(csvFile,isSendEmpt)==2){
//				if("1234".equals(sheetName)){
					r = -999;//表示文件数据为空,数据为空不发送邮件
					return r;
//				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			logger.info("csv文件名称："+csvPath);
			logger.info("xls文件名称："+xlsPath);
			logger.info("sheet名称："+sheetName);
			logger.error(e);
			e.printStackTrace();
			r = -998 ;
		}
		
		return r;
	}

	private void coverSheet(File xls, String coverSheetName,String csv)
	{
		FileInputStream readFile = null;
		FileOutputStream writeFile = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			readFile = new FileInputStream(xls);
			

			//HSSFWorkbook wb = new HSSFWorkbook(readFile);
			
			XSSFWorkbook wb = new XSSFWorkbook(readFile);//07
            
			//删除原有sheet
			int remove=wb.getSheetIndex(coverSheetName);
			wb.removeSheetAt(remove);
			
			
			//HSSFSheet st = wb.createSheet(coverSheetName);
			XSSFSheet st = wb.createSheet(coverSheetName);//07

			/*String getCell = "";

			for (int j = 0; j < rowCount; j++) {
				st.createRow(j);
				for (int i = 0; i < colCount; i++) {
					getCell = temp.get(point);
					st.getRow(j).createCell(i).setCellValue(getCell);
					point++;// 指向下一个List元素
				}
			}*/
			//----
				//isr = new InputStreamReader(new FileInputStream(csv), codeSet);//春江
			isr = new InputStreamReader(new FileInputStream(csv));//春江
		        br = new BufferedReader(isr);
				String line = "";
				int rownum = 0;
				while ((line = br.readLine()) != null) {
					if(line.contains("0 rows affected")){
						break;
					}
					
					String [] str = line.split("\\|\\+\\|");
					
					st.createRow(rownum);
					for (int i = 0; i < str.length; i++) {
						st.getRow(rownum).createCell(i).setCellValue(str[i]);
					}
					rownum++;
				}
			//----
			writeFile = new FileOutputStream(xls);// "c:/result.xls"
			//BufferedWriter writeFile1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xls), "UTF-8"));
			 

			wb.write(writeFile);
			/*Workbook wb = new XSSFWorkbook(readFile); 
			//删除原有sheet
			int remove=wb.getSheetIndex(coverSheetName);
			wb.removeSheetAt(remove);
			
			
			Sheet  st = wb.createSheet(coverSheetName);

			String getCell = "";

			for (int j = 0; j < rowCount; j++) {
				st.createRow(j);
				for (int i = 0; i < colCount; i++) {
					getCell = temp.get(point);
					st.getRow(j).createCell(i).setCellValue(getCell);
					point++; 
				}
			}

			writeFile = new FileOutputStream(xls); 
			 

			wb.write(writeFile);*/
		} catch (FileNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				readFile.close();
				writeFile.close();
				br.close();
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}

		
	}

	/**
	 * @param xlsPath xls文件的输出路径
	 * @param SheetName	新生成的sheet名称
	 * 实现根据xlsPath和SheetName生成新的xls文件
	 */
	public int writeXls(String xlsPath, String SheetName,String csv) {
		// 写xls//////////////////////////////////////////
		int r = 0 ;
		FileOutputStream writeFile = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			File xlsFile = new File(xlsPath);

			//HSSFWorkbook wb = new HSSFWorkbook();//--2003
			XSSFWorkbook wb = new XSSFWorkbook(); //--2007
			//HSSFSheet st = wb.createSheet(SheetName);// "待传入参数"--2003
			XSSFSheet  st = wb.createSheet(SheetName);// "待传入参数"--2007

			//----
			//isr = new InputStreamReader(new FileInputStream(csv), codeSet);//春江
			isr = new InputStreamReader(new FileInputStream(csv));//春江
		        br = new BufferedReader(isr);
				String line = "";
				int rownum = 0;
				while ((line = br.readLine()) != null) {
					if(line.contains("0 rows affected")){
						break;
					}
					
					String [] str = line.split("\\|\\+\\|");
					
					st.createRow(rownum);
					for (int i = 0; i < str.length; i++) {
						st.getRow(rownum).createCell(i).setCellValue(str[i]);
					}
					rownum++;
				}
			//----
			//	logger.info(rownum);	
			writeFile = new FileOutputStream(xlsFile);// "c:/result.xls"
			wb.write(writeFile);
			r=0;
			 
		} catch (Exception e) {
			// TODO: handle exception
			r=-998;
			logger.info("csv文件名称："+csv);
			logger.info("xls文件名称："+xlsPath);
			logger.info("sheet名称："+SheetName);
			e.printStackTrace();
		}finally {
			try {
				if(writeFile!=null){
					writeFile.close();
				}
				if(isr != null){
					isr.close();
				}
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return r;
	}
	
 
	
	
	public static void main(String[] args) 
	{
//		String cvsPath=args[0];
//		String xlsPath=args[1];
//		String sheetName=args[2];
//		BufferedReader br=null;
//		br=new BufferedReader(new InputStreamReader(System.in));
//		
//		 
//		
//		logger.info(cvsPath);
//		logger.info(xlsPath);
//		logger.info(sheetName);
//		
//		CsvToXls test = new CsvToXls();
//		test.transform(cvsPath, xlsPath, sheetName);
//				test.transform("D:\\script\\20120607\\oracle41.csv", "D:\\script\\20120607\\test.xls", "test","1");
		
		int a = 5;
		System.out.println((float)a + "aaa");

	}


}
