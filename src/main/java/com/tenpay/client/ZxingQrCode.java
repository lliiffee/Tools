package com.tenpay.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ZxingQrCode {

	/**
	 * 生成二维码
	 * @param content 二维码内容
	 * @param charset 编码二维码内容时采用的字符集(传null时默认采用UTF-8编码)
	 * @param imagePath 二维码图片存放路径(含文件名)
	 * @param width 生成的二维码图片宽度
	 * @param height 生成的二维码图片高度
	 * @throws Exception 
	 * @throws  
	 */

	public static void encodeQRCodeImage(String content, String charset,
			String imagePath, int width, int height) throws Exception {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 指定编码格式
		// hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 指定纠错级别(L--7%,M--15%,Q--25%,H--30%)
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 编码内容,编码类型(这里指定为二维码),生成图片宽度,生成图片高度,设置参数
		BitMatrix bitMatrix = new MultiFormatWriter().encode(new String(content.getBytes(charset == null ? "UTF-8": charset), "ISO-8859-1"), BarcodeFormat.QR_CODE, width, height, hints);
		// 生成的二维码图片默认背景为白色,前景为黑色,但是在加入logo图像后会导致logo也变为黑白色,至于是什么原因还没有仔细去读它的源码
		// 所以这里对其第一个参数黑色将ZXing默认的前景色0xFF000000稍微改了一下0xFF000001,最终效果也是白色背景黑色前景的二维码,且logo颜色保持原有不变
		MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
		// 这里要显式指定MatrixToImageConfig,否则还会按照默认处理将logo图像也变为黑白色(如果打算加logo的话,反之则不须传MatrixToImageConfig参数)
		MatrixToImageWriter.writeToFile(bitMatrix, imagePath.substring(imagePath.lastIndexOf(".") + 1), new File(imagePath), config);
	}

}
