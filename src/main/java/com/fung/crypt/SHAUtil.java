package com.fung.crypt;

import java.security.MessageDigest;

public class SHAUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
          System.out.println(SHA1Encode("123456"));
	}
	
	
	//sha1加密
		public static String SHA1Encode(String sourceString) {
			String resultString = null;
			try {
			   resultString = new String(sourceString);
			   MessageDigest md = MessageDigest.getInstance("SHA-1");
			   resultString = byte2hexString(md.digest(resultString.getBytes()));
			} catch (Exception ex) {
			}
			return resultString;
		}
		public static final String byte2hexString(byte[] bytes) {
			StringBuffer buf = new StringBuffer(bytes.length * 2);
			for (int i = 0; i < bytes.length; i++) {
				if (((int) bytes[i] & 0xff) < 0x10) {
			    	buf.append("0");
			   	}
				buf.append(Long.toString((int) bytes[i] & 0xff, 16));
			}
			return buf.toString().toUpperCase();
		}

}
