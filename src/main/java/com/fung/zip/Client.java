package com.fung.zip;

import java.io.File;

import com.fung.file.OpFile;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
           OpFile of=new OpFile();
           
           UnzipFileOp up=new UnzipFileOp();
           File unzipFolder= new File("F:\\fung\\待上传\\网页与UI设计从入门到精通视频教程\\");
           of.opFileByCallback(unzipFolder, up);
           
	}
}
