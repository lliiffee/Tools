package com.fung.img;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import sun.misc.BASE64Encoder;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.swetake.util.Qrcode;

public class ChartGraphic {

	BufferedImage image;

	void createImage(String fileLocation) {
		try {
			FileOutputStream fos = new FileOutputStream(fileLocation);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
			encoder.encode(image);
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void graphicsGeneration(String name, String id, String classname,
			String imgurl) {
		// 100像素=3.53厘米
		int imageWidth = 567;// 图片的宽度

		int imageHeight = 359;// 图片的高度

		image = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, imageWidth, imageHeight);
		graphics.setColor(Color.BLACK);
		// Ellipse2D ellipse = new Ellipse2D.Double(6, 6, 6 , 6);
		graphics.drawOval(150, 60, 40, 40);
		graphics.drawOval(377, 60, 40, 40);
		graphics.setFont(new Font("微软雅黑", Font.BOLD, 30));
		graphics.drawString("No: ", 60, 200);
		graphics.setFont(new Font("微软雅黑", Font.BOLD, 36));
		graphics.drawString(id, 120, 200);

		graphics.setFont(new Font("宋体", Font.PLAIN, 24));
		graphics.drawString("雅韵茗家古树茶 ", 70, 260);

		
		String parm="";

		try {
			parm = "http://www.yymjt.com/TreeFile/"+id+".htm" ;
		     
		    
			 	Qrcode qrcode = new Qrcode();
				// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小  
				qrcode.setQrcodeErrorCorrect('M');
				qrcode.setQrcodeEncodeMode('B');
				// 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
				qrcode.setQrcodeVersion(10);
				byte[] d = parm.getBytes("utf-8");
				// 图片尺寸  
		        int imgSize = 120;  
				BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_BYTE_BINARY);
				Graphics2D g = image.createGraphics();
				g.setBackground(Color.WHITE);
				g.clearRect(0, 0, imgSize, imgSize);
				g.setColor(Color.BLACK);
			//	System.out.print("------>"+d.length);
				if (d.length > 0 && d.length < 500) {
					boolean[][] s = qrcode.calQrcode(d);
					for (int i = 0; i < s.length; i++) {
						for (int j = 0; j < s.length; j++) {
							if (s[j][i]) {
								g.fillRect(j * 2 + 3, i * 2 + 3, 2, 2);
							}
						}
					}
				}
				g.dispose();
				image.flush();

		        graphics.drawImage(image, 350, 150, null);

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}finally
		{
			
		}
		
		// 改成这样:
//		BufferedImage bimg = null;
//		try {
//			bimg = javax.imageio.ImageIO.read(new java.io.File(imgurl));
//		} catch (Exception e) {
//		}
//
//		if (bimg != null)
//			graphics.drawImage(bimg, 230, 0, null);
		graphics.dispose();

		createImage("E:\\temp\\b3.jpg");

	}

	public static void main(String[] args) {
		ChartGraphic cg = new ChartGraphic();
		try {
			cg.graphicsGeneration("", "112233", "12", "E:\\temp\\b2t.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void test(String id)
	{
		String parm="";
		ByteArrayOutputStream out=null;
		try {
			parm = "http://www.yymjt.com/TreeFile/"+id+".htm" ;
		     
		    
			 	Qrcode qrcode = new Qrcode();
				// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小  
				qrcode.setQrcodeErrorCorrect('Q');
				qrcode.setQrcodeEncodeMode('B');
				// 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
				qrcode.setQrcodeVersion(20);
				byte[] d = parm.getBytes("utf-8");
				// 图片尺寸  
		        int imgSize = 220;  
				BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_BYTE_BINARY);
				Graphics2D g = image.createGraphics();
				g.setBackground(Color.WHITE);
				g.clearRect(0, 0, imgSize, imgSize);
				g.setColor(Color.BLACK);
			//	System.out.print("------>"+d.length);
				if (d.length > 0 && d.length < 500) {
					boolean[][] s = qrcode.calQrcode(d);
					for (int i = 0; i < s.length; i++) {
						for (int j = 0; j < s.length; j++) {
							if (s[j][i]) {
								g.fillRect(j * 2 + 3, i * 2 + 3, 2, 2);
							}
						}
					}
				}
				g.dispose();
				image.flush();
				
				 out = new ByteArrayOutputStream();
		        boolean flag = ImageIO.write(image, "jpg", out);
		        byte[] b = out.toByteArray();
		        BASE64Encoder encode=new BASE64Encoder();
		        out.close();
		       
//				ImageIO.write(image, "jpg", response.getOutputStream());
				
		 

//				

		
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally
		{
			try{
				if(out!=null)
				{
					out.close();
				}
			}catch(Exception e){}
			finally
			{
				out=null;
			}
		}
	}
}
