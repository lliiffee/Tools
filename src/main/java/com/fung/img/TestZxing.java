package com.fung.img;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class TestZxing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
            
		     String content = "120605181003;http://www.cnblogs.com/jtmjx";
		     String path = "e:/temp";
		     
		     MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		     
		     Map hints = new HashMap();
		     hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		     BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);
		     File file1 = new File(path,"test.jpg");
		     MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
		     
		 } catch (Exception e) {
		     e.printStackTrace();
		 }

	}

}
