package com.fung.img;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import sun.misc.BASE64Encoder;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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
		graphics.drawOval(150, 60, 50, 50);
		graphics.drawOval(367, 60, 50, 50);
		graphics.setFont(new Font("微软雅黑", Font.BOLD, 38));
		graphics.drawString("No: ", 60, 200);
		graphics.setFont(new Font("微软雅黑", Font.BOLD, 46));
		graphics.drawString(id, 130, 200);

		graphics.setFont(new Font("宋体", Font.PLAIN, 32));
		graphics.drawString("雅韵茗家古树茶 ", 70, 260);

		String parm = "";

		try { 
			parm = "http://www.yymjt.com/TreeFile/" + id + ".htm";

//			Qrcode qrcode = new Qrcode();
//			// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
//			qrcode.setQrcodeErrorCorrect('H');
//			qrcode.setQrcodeEncodeMode('B');
//			// 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
//			qrcode.setQrcodeVersion(12);
//			byte[] d = parm.getBytes("utf-8");
//			// 图片尺寸
//			int imgSize = 250;
//			BufferedImage image = new BufferedImage(imgSize, imgSize,
//					BufferedImage.TYPE_BYTE_BINARY);
//			Graphics2D g = image.createGraphics();
//			g.setBackground(Color.WHITE);
//			g.clearRect(0, 0, imgSize, imgSize);
//			g.setColor(Color.BLACK);
//			// System.out.print("------>"+d.length);
//			if (d.length > 0 && d.length < 140) {
//				boolean[][] s = qrcode.calQrcode(d);
//				for (int i = 0; i < s.length; i++) {
//					for (int j = 0; j < s.length; j++) {
//						if (s[j][i]) {
//							g.fillRect(j * 2 + 3, i * 2 + 3, 2, 2);
//						}
//					}
//				}
//			}
//			g.dispose();
//			image.flush();

			
             MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		     
		     Map hints = new HashMap();
		     hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  
		   //  hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		     BitMatrix bitMatrix;
			 
				bitMatrix = multiFormatWriter.encode(parm, BarcodeFormat.QR_CODE, 220, 220,hints);
			 
				

		     BufferedImage image=  MatrixToImageWriter.toBufferedImage(bitMatrix);
		     
		     
		     
		 //    Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
				// 指定编码格式
				// hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
				// 指定纠错级别(L--7%,M--15%,Q--25%,H--30%)
			//	hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
				// 编码内容,编码类型(这里指定为二维码),生成图片宽度,生成图片高度,设置参数
			//	BitMatrix bitMatrix = new MultiFormatWriter().encode(new String(content.getBytes(charset == null ? "UTF-8": charset), "ISO-8859-1"), BarcodeFormat.QR_CODE, width, height, hints);
				// 生成的二维码图片默认背景为白色,前景为黑色,但是在加入logo图像后会导致logo也变为黑白色,至于是什么原因还没有仔细去读它的源码
				// 所以这里对其第一个参数黑色将ZXing默认的前景色0xFF000000稍微改了一下0xFF000001,最终效果也是白色背景黑色前景的二维码,且logo颜色保持原有不变
			//	MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
				// 这里要显式指定MatrixToImageConfig,否则还会按照默认处理将logo图像也变为黑白色(如果打算加logo的话,反之则不须传MatrixToImageConfig参数)
			//	MatrixToImageWriter.writeToFile(bitMatrix, imagePath.substring(imagePath.lastIndexOf(".") + 1), new File(imagePath), config);
				
		     
			graphics.drawImage(image, 320, 110, null);

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

		}

		// 改成这样:
		// BufferedImage bimg = null;
		// try {
		// bimg = javax.imageio.ImageIO.read(new java.io.File(imgurl));
		// } catch (Exception e) {
		// }
		//
		// if (bimg != null)
		// graphics.drawImage(bimg, 230, 0, null);
		graphics.dispose();

		createImage("E:\\temp\\8x\\" + id + ".jpg");

	}

	public static void main(String[] args) {
		ChartGraphic cg = new ChartGraphic();
		 
           
		String fileName ="E:\\temp\\list2.txt";
 String encoding="utf-8";
		
		try {
			 FileInputStream fis  =   new  FileInputStream(fileName); 
	            InputStreamReader isr  =   new  InputStreamReader(fis, encoding); 
	            BufferedReader br  =   new  BufferedReader(isr); 
	            String line  =   null ; 
	             while  ((line  =  br.readLine())  !=   null ) { 
	            	 cg.graphicsGeneration("", line, "12", "E:\\temp\\b2t.png");
	             }
	                
			 
		} catch (Exception e) {
			e.printStackTrace();
		}

//		java.awt.GraphicsEnvironment eq = java.awt.GraphicsEnvironment
//				.getLocalGraphicsEnvironment();
//		String[] fontNames = eq.getAvailableFontFamilyNames();
//		for (int i = 0; i < fontNames.length; i++) {
//			System.out.println(fontNames[i]);
//		}

	}

	public static void test(String id) {
		String parm = "";
		ByteArrayOutputStream out = null;
		try {
			parm = "http://www.yymjt.com/TreeFile/" + id + ".htm";

			Qrcode qrcode = new Qrcode();
			// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcode.setQrcodeErrorCorrect('M');
			qrcode.setQrcodeEncodeMode('B');
			// 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
			qrcode.setQrcodeVersion(12);
			byte[] d = parm.getBytes("utf-8");
			// 图片尺寸
			int imgSize = 320;
			BufferedImage image = new BufferedImage(imgSize, imgSize,
					BufferedImage.TYPE_BYTE_BINARY);
			Graphics2D g = image.createGraphics();
			g.setBackground(Color.WHITE);
			g.clearRect(0, 0, imgSize, imgSize);
			g.setColor(Color.BLACK);
			// System.out.print("------>"+d.length);
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
			BASE64Encoder encode = new BASE64Encoder();
			out.close();

			// ImageIO.write(image, "jpg", response.getOutputStream());

			//

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
			} finally {
				out = null;
			}
		}
	}
}
