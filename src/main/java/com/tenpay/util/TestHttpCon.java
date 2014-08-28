package com.tenpay.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestHttpCon {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	
	public static byte[] sendPostRequestByForm(String path, String params) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");// �ύģʽ
        // conn.setConnectTimeout(10000);//���ӳ�ʱ ��λ����
        // conn.setReadTimeout(2000);//��ȡ��ʱ ��λ����
        conn.setDoOutput(true);// �Ƿ��������
        byte[] bypes = params.toString().getBytes();
        conn.getOutputStream().write(bypes);// �������
        InputStream inStream=conn.getInputStream();
        return readInputStream(inStream);
    }
 
 public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//��ҳ�Ķ���������
        outStream.close();
        inStream.close();
        return data;
    }
	
 
}
