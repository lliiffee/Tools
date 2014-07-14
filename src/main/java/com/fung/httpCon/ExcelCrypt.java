package com.fung.httpCon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

 

/**
 * 威富通加密算法工具
 */

 /************************************************************
 * <p>#File Name:		ExcelCrypt.java	</p>
 * <p>#Description:		Excel加解密工具			</p>
 * <p>#Create Date:		2013-11-25				</p>
 * <p>#Company:  		swiftpass			 	</p>
 * <p>#Notice:   								</p>
 *            
 * @author		jesse.chen
 * @see			
 * @since		SPEC version
 * @version	    0.1	2013-11-25	jesse.chen
 ************************************************************/
/**
 * <p>Excel加解密</p>
 * @author		jesse.chen
 * @see			
 * @since		SPEC version
 * @version	    0.1	2013-11-25	jesse.chen
 */
public class ExcelCrypt {
	private final static String DES = "DES";
	private final static String separatoren = "|";//加密时使用的分隔符
	private final static String separatorde = "[|]";//解密时分隔符
	public final static String key = "swiftpass.cn";//默认加解密密码
	/**
	 * <p>NeededComponents</p>
	 * @param args
	 * @author	jesse.chen
	 * @version	 0.1	2013-11-25	jesse.chen
	 */
	public static void main(String[] args) {
		// String filename ="E:/1.xls";
		 String keydata = "ylwl_pwd";
		 String strData = "mId=001075552110006&notify_url=123123&outOrder_no=12323&total_fee=100&service_version=1.0";
		 String edata =encrypt(strData,keydata);
		 System.out.println("加密后:" + edata);
		 System.out.println("解密后:" + decrypt(edata,keydata));
		 //encryptExcel(filename,keydata);
//		 decryptExcel(filename,keydata);
//		 encryptExcel(filename);
//		 decryptExcel(filename);
		 
	}
 
	
    /**
     * <p>字符串加密</p>
     * @param src,源字符串
     * @param key,密码
     * @return
     * @author	jesse.chen
     * @version	0.1	2013-11-25	jesse.chen
     */
    public final static String encrypt(String src,String key) {
        try {
            byte[] cipherBytes = encrypt(src.getBytes("UTF-8"), key.getBytes("UTF-8"));
            return byte2hex(cipherBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * <p>字符串解密</p>
     * @param data,源字符串
     * @param key,密码
     * @return
     * @author	jesse.chen
     * @version	0.1	2013-11-25	jesse.chen
     */
    public final static String decrypt(String data,String key) {
        try {
            byte[] cipherBytes = hex2byte(data);
            return new String(decrypt(cipherBytes, key.getBytes("UTF-8")));
        } catch (Exception e) {
        	//Log.error("不合法的签名");
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * <p>加密</p>
	 * @param src
	 * @param key
	 * @return
	 * @throws Exception
	 * @author	jesse.chen
	 * @version	0.1	2013-11-25	jesse.chen
	 */
    private static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 现在，获取数据并加密
        // 正式执行加密操作
        return cipher.doFinal(src);
    }

    /**
     * <p>解密</p>
     * @param src
     * @param key
     * @return
     * @throws Exception
     * @author	jesse.chen
     * @version	0.1	2013-11-25	jesse.chen
     */
    private static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // 现在，获取数据并解密
        // 正式执行解密操作
        return cipher.doFinal(src);
    }
    /**
     * <p>二进制字节转换为16进制表示，以便存入到介质中</p>
     * @param data
     * @return
     * @author	jesse.chen
     * @version	0.1	2013-11-25	jesse.chen
     */
    private final static String byte2hex(byte[] data) {

        String str = "";
        String tmp = "";

        for (int i = 0; i < data.length; i++) {

            tmp = Integer.toHexString(data[i] & 0xFF);

            if (tmp.length() == 1) {
                str += "0" + tmp;
            } else {
                str += tmp;
            }
        }
        return str;
    }

    /**
     * <p>16进制转换为二进制形式字节，用以解密</p>
     * @param data
     * @return
     * @author	jesse.chen
     * @version	0.1	2013-11-25	jesse.chen
     */
    private final static byte[] hex2byte(String data) {

        char[] chData = data.toCharArray();

        byte[] byteData = new byte[chData.length / 2];

        for (int i = 0; i < chData.length; i += 2) {

            byte bHi = hexCh2Byte(chData[i]);
            byte bLow = hexCh2Byte(chData[i + 1]);
            byte bWord = (byte) ((bHi << 4) | bLow);

            byteData[i / 2] = bWord;
        }

        return byteData;
    }

    /**
     * <p>某个16进制数字字符转换成相应的0xXX形式</p>
     * @param hexCh
     * @return
     * @author	jesse.chen
     * @version	0.1	2013-11-25	jesse.chen
     */
    private final static byte hexCh2Byte(char hexCh) {
        byte bResult = 0;

        switch (hexCh) {
            case '0': {
                bResult = 0x00;
                break;
            }
            case '1': {
                bResult = 0x01;
                break;
            }
            case '2': {
                bResult = 0x02;
                break;
            }
            case '3': {
                bResult = 0x03;
                break;
            }
            case '4': {
                bResult = 0x04;
                break;
            }
            case '5': {
                bResult = 0x05;
                break;
            }
            case '6': {
                bResult = 0x06;
                break;
            }
            case '7': {
                bResult = 0x07;
                break;
            }
            case '8': {
                bResult = 0x08;
                break;
            }
            case '9': {
                bResult = 0x09;
                break;
            }
            case 'a':
            case 'A': {
                bResult = 0x0A;
                break;
            }
            case 'b':
            case 'B': {
                bResult = 0x0B;
                break;
            }
            case 'c':
            case 'C': {
                bResult = 0x0C;
                break;
            }
            case 'd':
            case 'D': {
                bResult = 0x0D;
                break;
            }
            case 'e':
            case 'E': {
                bResult = 0x0E;
                break;
            }
            case 'f':
            case 'F': {
                bResult = 0x0F;
                break;
            }
            default:
        }
        return bResult;
    }
}
