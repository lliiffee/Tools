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
           File unzipFolder= new File("E:\\BaiduYunDownload\\oc\\Objective-C基础加强视频vedio\\OC加强第05天_对象的拷贝_知识补充");
           of.opFileByCallback(unzipFolder, up);
           
	}
}
