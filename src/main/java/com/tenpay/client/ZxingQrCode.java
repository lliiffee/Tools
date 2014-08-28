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
	 * ���ɶ�ά��
	 * @param content ��ά������
	 * @param charset �����ά������ʱ���õ��ַ���(��nullʱĬ�ϲ���UTF-8����)
	 * @param imagePath ��ά��ͼƬ���·��(���ļ���)
	 * @param width ���ɵĶ�ά��ͼƬ���
	 * @param height ���ɵĶ�ά��ͼƬ�߶�
	 * @throws Exception 
	 * @throws  
	 */

	public static void encodeQRCodeImage(String content, String charset,
			String imagePath, int width, int height) throws Exception {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// ָ�������ʽ
		// hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// ָ��������(L--7%,M--15%,Q--25%,H--30%)
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// ��������,��������(����ָ��Ϊ��ά��),����ͼƬ���,����ͼƬ�߶�,���ò���
		BitMatrix bitMatrix = new MultiFormatWriter().encode(new String(content.getBytes(charset == null ? "UTF-8": charset), "ISO-8859-1"), BarcodeFormat.QR_CODE, width, height, hints);
		// ���ɵĶ�ά��ͼƬĬ�ϱ���Ϊ��ɫ,ǰ��Ϊ��ɫ,�����ڼ���logoͼ���ᵼ��logoҲ��Ϊ�ڰ�ɫ,������ʲôԭ��û����ϸȥ������Դ��
		// ������������һ��������ɫ��ZXingĬ�ϵ�ǰ��ɫ0xFF000000��΢����һ��0xFF000001,����Ч��Ҳ�ǰ�ɫ������ɫǰ���Ķ�ά��,��logo��ɫ����ԭ�в���
		MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
		// ����Ҫ��ʽָ��MatrixToImageConfig,���򻹻ᰴ��Ĭ�ϴ���logoͼ��Ҳ��Ϊ�ڰ�ɫ(��������logo�Ļ�,��֮���봫MatrixToImageConfig����)
		MatrixToImageWriter.writeToFile(bitMatrix, imagePath.substring(imagePath.lastIndexOf(".") + 1), new File(imagePath), config);
	}

}
