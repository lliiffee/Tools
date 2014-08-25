package com.fung.httpCon;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestHttpCon {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	public static void sendOrderHandleStatusToWx() throws Exception {
		String new_batchOId = "108027021870361966";
		
		String pyid = "1219895801201408253163215011";
		//保存订单状态
		String acc_token="egKIwntXiVW0-W6tlK15IUKsamj2qyb-e7vzZ8xMarZFls4h-tfd_zr3ps-5PE-euhf1iPVo4JWjODDVpAZUUg";
		 
		 /*
			String urlToken="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx7179cc98fb47eff5&secret=823ae7209cb695d4a4a0c71071d6006e";
			String returnRaw=getInternetContent(urlToken, "UTF-8");
	 		JSONObject s = JSON.parseObject(returnRaw);	
	 		
	 		acc_token=s.getString("access_token");
	 	
	 	
		System.out.println(acc_token);
		*/
		
		String path="https://api.weixin.qq.com/pay/delivernotify?access_token="+acc_token;
 						 
		JSONObject obj = new JSONObject();
		obj.put("appid", "wx7179cc98fb47eff5");
        
		obj.put("openid", "wx7179cc98fb47eff5");
		obj.put("transid", "1219895801201408253163215011");
		obj.put("out_trade_no", "108027021870361966");
		obj.put("deliver_timestamp", System.currentTimeMillis());
		obj.put("deliver_status", "1");
		obj.put("deliver_msg", "ok");
		obj.put("app_signature", "wx7179cc98fb47eff5");
		obj.put("sign_method", "sha1");
		

		   URL url = new URL(path);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");// �ύģʽ
	        // conn.setConnectTimeout(10000);//���ӳ�ʱ ��λ����
	        // conn.setReadTimeout(2000);//��ȡ��ʱ ��λ����
	        conn.setDoOutput(true);// �Ƿ��������
	       
		   // 得到对象输出流
	        ObjectOutputStream	objOutputStrm = getObjOutStream(conn);
	        objOutputStrm.writeObject(obj.toString());
	     // 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
	       objOutputStrm.flush();
	     // 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
	     // 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
	       objOutputStrm.close();
		/*
		 * {
"appid" : "wwwwb4f85f3a797777",
"openid" : "oX99MDgNcgwnz3zFN3DNmo8uwa-w",
"transid" : "111112222233333",
"out_trade_no" : "555666uuu",
"deliver_timestamp" : "1369745073",
"deliver_status" : "1",
"deliver_msg" : "ok",
"app_signature" : "53cca9d47b883bd4a5c85a9300df3da0cb48565c",
"sign_method" : "sha1"
}
		 */
	
		 
	}

	
	 private static ObjectOutputStream getObjOutStream(
			 HttpURLConnection httpUrlConnection) throws IOException
			 {
			 OutputStream outStrm;// 得到HttpURLConnection的输出流
			 ObjectOutputStream objOutputStrm;// 对象输出流
			 // 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
			 // 所以在开发中不调用上述的connect()也可以)。
			 outStrm = httpUrlConnection.getOutputStream();

			   // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
			 // 使用JSON传值
			 objOutputStrm = new ObjectOutputStream(outStrm);
			 return objOutputStrm;
			 }
	
	
	public static String getInternetContent(String urlstr,String enc)
	{  String content="";
	 //enc="UTF-8";
	   InputStream in=null;
	   BufferedReader d=null;
		  try {
				URL url = new URL(urlstr);
				  //返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。        
		        HttpURLConnection uc = (HttpURLConnection)url.openConnection();    
		        uc.setRequestProperty("user-agent","Mozilla/5.0 &#40;Windows NT 6.1&#41; AppleWebKit/537.1 &#40;KHTML. like Gecko&#41; Chrome/21.0.1180.89 Safari/537.1");
		        int responseCode = uc.getResponseCode();
		        
		        //打开的连接读取的输入流。        
		         in = uc.getInputStream(); 
		        
		        d= new BufferedReader(new InputStreamReader(in,enc));
		        String tmp;
		       
		        while( (tmp=d.readLine())!=null)
		        {
		        	content+=tmp;
		        }
				
		        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally
			{
				try
				{
					if(in!=null)
					{
						in.close();
						d.close();
					}
				}catch(Exception e)
				{
					
				}finally{
					in=null;
					d=null;
				}
				
			}
		  return content;
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
        byte[] data = outStream.toByteArray();//��ҳ�Ķ��������
        outStream.close();
        inStream.close();
        return data;
    }
	
 
}
