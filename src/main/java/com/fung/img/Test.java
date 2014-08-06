package com.fung.img;

import java.io.IOException;

public class Test {
	public static void main(String[] args) {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("中华人民共和国\n");
			sb.append("中华人民共和国\n");
			
			FileImageCreator creator = new FileImageCreator(new SimpleDrawer(), "e:\\temp\\img.jpeg");
			creator.setWidth(150); //图片宽度
			creator.setHeight(100); //图片高度
			creator.setLineNum(0); //干扰线条数
			creator.setFontSize(18); //字体大小
			creator.setFontName("宋体");
			
			//文字旋转
			//creator.setRadian(30.0); //旋转弧度
			//creator.setRotateX(creator.getWidth()/5);
			//creator.setRotateY(creator.getHeight()*5/10);
			
			creator.generateImage(sb.toString());
			
			System.out.println("ok");
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
}
