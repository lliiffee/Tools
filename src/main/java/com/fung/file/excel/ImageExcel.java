package com.fung.file.excel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ImageExcel {
	
	  
	    public static void main(String[] args) {  
	         FileOutputStream fileOut = null;     
	         BufferedImage bufferImg = null;     
	        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray    
	        try {  
	            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
	            bufferImg = ImageIO.read(new File("C:/Users/Administrator/Workspaces/MyEclipse 10/trunk/WebContent/upload_news_img/tmp/20150604164333001.png"));     
	            ImageIO.write(bufferImg, "jpg", byteArrayOut);  
	              
	            HSSFWorkbook wb = new HSSFWorkbook();     
	            HSSFSheet sheet1 = wb.createSheet("test picture");    
	            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
	            HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();    
	            
	            HSSFRow firstrow = sheet1.createRow(0); // 下标为0的行开始
	            HSSFCell[] firstcell = new HSSFCell[5];
	            String[] names = new String[5];
	            names[0] = "学号";
	            names[1] = "姓名";
	            names[2] = "学院";
	            names[3] = "课程名";
	            names[4] = "成绩";
	            for (int j = 0; j < 5; j++) {
	                firstcell[j] = firstrow.createCell(j);
	                firstcell[j].setCellValue(new HSSFRichTextString(names[j]));
	            }
	            
	            //anchor主要用于设置图片的属性  
	            //这里dx1、dy1定义了该图片在开始cell的起始位置，dx2、dy2定义了在终cell的结束位置。col1、row1定义了开始cell、col2、row2定义了结束cell。
	            //HSSFClientAnchor(int dx1,int dy1,int dx2,int dy2,short col1,int row1,short col2, int row2);
	            /*
	             * col1：the column (0 based) of the first cell。
      row1：the row (0 based) of the first cell。
      col2：the column (0 based) of the second cell。
      row2：the row (0 based) of the second cell。
	             */                                                              //(col,row)
	            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,(short) 1, 1, (short) 5, 8);     
	            anchor.setAnchorType(3);     
	            //插入图片    
	            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));  
	            
	            HSSFClientAnchor anchor2 = new HSSFClientAnchor(0, 0, 0,0,(short) 1, 9, (short)5, 16); 
	            
	            //插入图片  
	    
	            patriarch.createPicture(anchor2, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
	            
	            
	            fileOut = new FileOutputStream("D:/testExcel.xls");     
	            // 写入excel文件     
	             wb.write(fileOut);     
	             System.out.println("----Excle文件已生成------");  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }finally{  
	            if(fileOut != null){  
	                 try {  
	                    fileOut.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	    }  
	 
}
