package com.fung.file.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ProtectExcel {

	public static void main(String[] args) throws IOException { 
	   
	  } 
/*	
	public static void protByJxl(){
		 WritableWorkbook wwb = Workbook.createWorkbook(new File("d:/test.xls")); 
		    WritableSheet ws = wwb.createSheet("Test Sheet 1", 0); 
		    SheetSettings ss = ws.getSettings(); 
		    ss.setPassword("12345678"); 
		    ss.setProtected(true); 
		    wwb.write(); 
		    wwb.close(); 
	}
	*/
	
	public static void protByPoi(String file){
		 FileOutputStream fileOut = null;
		  try {
		   // 创 建一个工作薄
		   HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
		   // 设置密 码 保 护 ·
		   wb.writeProtectWorkbook("password", "owner");
		   // 写入excel文件
		   fileOut = new FileOutputStream(file);
		   wb.write(fileOut);
		   fileOut.close();
		  } catch (IOException io) {
		   io.printStackTrace();
		  } finally {
		   if (fileOut != null) {
		    try {
		     fileOut.close();
		    } catch (IOException e) {
		     e.printStackTrace();
		    }
		   }
		  }
	}
}
