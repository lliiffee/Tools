package com.fung.httpCon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSONObject;
import com.tenpay.RequestHandler;
import com.tenpay.util.TenpayUtil;

public class TestHttpCon {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
//		try {
//			sendOrderHandleStatusToWx();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		sendP();
		
	}
	
	
	public static void sendP()
	{
		String path="http://www.800pharm.com/shop/productSyncsetting/synchronizeProduct.html";
		String params="shopcode=99919&productcode=ZK100285&storage=49&price=43.9000&marketprice=45.0000&alive=1&datetime=2013-07-10%2007:07:50&sign=2c572a11a313d8a855a4945e86fbd9cb&returnurl=http://www.zk100.com/bbf/ProductData.aspx";
				
		try {
			 URL url = new URL(path);
		        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
		        httpUrlConnection.setRequestMethod("POST");
		        httpUrlConnection.setDoOutput(true);// �Ƿ��������
				//允许输出流
				httpUrlConnection.setDoOutput(true);
				//允许写入流
				httpUrlConnection.setDoInput(true);
				//post方式提交不可以使用缓存
				httpUrlConnection.setUseCaches(false);
				//请求类型
				httpUrlConnection.connect();
				
				
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpUrlConnection.getOutputStream(),"UTF-8"));
			//	 byte[] bypes = params.toString().getBytes();
				writer.write(params);// �������
				writer.flush();
				writer.close();
				writer = null;
				
			   // 得到对象输出流
		       // ObjectOutputStream	objOutputStrm = getObjOutStream(httpUrlConnection);
		//        objOutputStrm.writeObject(obj.toJSONString());
		     // 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
//		       objOutputStrm.flush();
		     // 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
		     // 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
		       
//		       objOutputStrm.close();
		 
		       System.out.println(new String(readInputStream(httpUrlConnection.getInputStream())) );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendOrderHandleStatusToWx() throws Exception {
		String new_batchOId = "108027021870361966";
		
		String pyid = "1219895801201408253163215011";
		//保存订单状态
		String acc_token="vHRsgTOGSPjwTqUBjBlgDVnFtE09jDnkWa8K12w_6uq8K0xJ_C1leF3H0-slHF3Msqs9IRAyq1G_ZQvvIP7RXA";
		 
		 /*
			String urlToken="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx7179cc98fb47eff5&secret=823ae7209cb695d4a4a0c71071d6006e";
			String returnRaw=getInternetContent(urlToken, "UTF-8");
	 		JSONObject s = JSON.parseObject(returnRaw);	
	 		
	 		acc_token=s.getString("access_token");
	 	
	 	*/
		
		System.out.println(acc_token);
		
		
		String path="https://api.weixin.qq.com/pay/delivernotify?access_token="+acc_token;
 		
		RequestHandler paySignReqHandler = new RequestHandler();
		String appId="wx7179cc98fb47eff5";
		String openid="oTNesjrqNt4ZIZ8zRe2-WVnt98M0";
		String transid="1219895801201408253163209310";
		String out_trade_no="105166022949677129";
		String  deliver_timestamp=TenpayUtil.getTimeStamp();
		String deliver_status="1";
		String deliver_msg="ok";
		paySignReqHandler.setParameter("appkey", "OlTWpFgG6TJ4cO9luoXq9QpatmoPPzqeaqgOxGVSMVvG8XQR8zPOnpn5sLyPAvGIV8AHrWe17MU3qBGOcEnbO1TZUNm3vjmPR5X5BDaqiSFahAklK7y0F8gGoIWbUDmu");//公众号appid对应的密钥
		paySignReqHandler.setParameter("appid", appId);//公众号appid
		paySignReqHandler.setParameter("openid", openid);//
		paySignReqHandler.setParameter("transid", transid);
		paySignReqHandler.setParameter("out_trade_no", out_trade_no);
		paySignReqHandler.setParameter("deliver_timestamp", deliver_timestamp);
		paySignReqHandler.setParameter("deliver_status", deliver_status);
		paySignReqHandler.setParameter("deliver_msg", deliver_msg);
		
		
		
		String app_signature=paySignReqHandler.getPaySign();
		
		System.out.println(paySignReqHandler.getDebugInfo());
		JSONObject obj = new JSONObject();
		obj.put("appid", appId);
        
		obj.put("openid",openid);
		obj.put("transid",transid);
		obj.put("out_trade_no", out_trade_no);
		obj.put("deliver_timestamp",deliver_timestamp);
		obj.put("deliver_status", deliver_status);
		obj.put("deliver_msg", "ok");
		obj.put("app_signature", app_signature);
		obj.put("sign_method", "sha1");
System.out.println(path);	
System.out.println(obj.toJSONString());
		   URL url = new URL(path);
	        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
	        httpUrlConnection.setRequestMethod("POST");
	        httpUrlConnection.setDoOutput(true);// �Ƿ��������
			//允许输出流
			httpUrlConnection.setDoOutput(true);
			//允许写入流
			httpUrlConnection.setDoInput(true);
			//post方式提交不可以使用缓存
			httpUrlConnection.setUseCaches(false);
			//请求类型
			httpUrlConnection.connect();
			
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpUrlConnection.getOutputStream(),"UTF-8"));

			writer.write(obj.toJSONString());
			writer.flush();
			writer.close();
			writer = null;
			
		   // 得到对象输出流
	       // ObjectOutputStream	objOutputStrm = getObjOutStream(httpUrlConnection);
	//        objOutputStrm.writeObject(obj.toJSONString());
	     // 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
//	       objOutputStrm.flush();
	     // 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
	     // 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
	       
//	       objOutputStrm.close();
	 
	       System.out.println(new String(readInputStream(httpUrlConnection.getInputStream())) );
		 
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
