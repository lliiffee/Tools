package com.fung.crypt;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		try {
			
			 
			
		 System.out.println( new String( Base64.encodeBase64("bbf-jRXx-hunM-GYbp".getBytes())) );
			
			
			System.out.println(
					new String(Base64.decodeBase64("YmJmLWpSWHgtaHVuTS1HWWJw".getBytes("UTF-8")))
		);
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
