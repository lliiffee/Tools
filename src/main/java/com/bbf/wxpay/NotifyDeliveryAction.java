package com.bbf.wxpay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class NotifyDeliveryAction {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		try {
			//sendOrderHandleStatusToWx();
			String[] springConfig  = 
				{	
					"wxpay_appContext.xml" 
				};
	//need spring fastjson mysql_jdbc		 
			System.out.println("begin.....");
			String urlToken="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx7179cc98fb47eff5&secret=823ae7209cb695d4a4a0c71071d6006e";
			String returnRaw=getInternetContent(urlToken, "UTF-8");
	 		JSONObject s = JSON.parseObject(returnRaw);	
	 		
	 		System.out.println("returnRaw:"+returnRaw);
	 		String acc_token=s.getString("access_token");
			
//	 		System.out.println("token:"+acc_token);
//			String acc_token="YUJLHySutF0VRtE_psgELAaSspxxYnzewRUX8ZHrqHHEkzDGSSCeG6I0zPpZ9XE6rb-p9TILfGAbXjMPDIwTiA";
			ApplicationContext ctx = 
					new ClassPathXmlApplicationContext(springConfig);
		  
			WxPayService wxPayService =(WxPayService)ctx.getBean("wxPayService"); 
			List<WxPayNotify> list =wxPayService.getHandleList();
			
			List<WxPayNotify> listSuccess = new ArrayList();
			//发送通知
			for(WxPayNotify bean : list)
			{
				//System.out.println(bean.getAppId()+"-"+bean.getNotifyId());
				sendOrderHandleStatusToWx( acc_token, bean, listSuccess);
			}
			
			//更新处理状态
			if(listSuccess.size()>0)
			{
				 wxPayService.batchUpdateByTranId(listSuccess);
 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final static String APPKEY="OlTWpFgG6TJ4cO9luoXq9QpatmoPPzqeaqgOxGVSMVvG8XQR8zPOnpn5sLyPAvGIV8AHrWe17MU3qBGOcEnbO1TZUNm3vjmPR5X5BDaqiSFahAklK7y0F8gGoIWbUDmu";
	public static void sendOrderHandleStatusToWx(String acc_token,WxPayNotify bean,List<WxPayNotify> listSuccess) throws Exception {
	
		String path="https://api.weixin.qq.com/pay/delivernotify?access_token="+acc_token;
 		
		RequestHandler paySignReqHandler = new RequestHandler();
		String appId=bean.getAppId();
		String openid=bean.getOpenId();
		String transid=bean.getTransactionId();
		String out_trade_no=bean.getOutTradeNo();
		String  deliver_timestamp=TenpayUtil.getTimeStamp();
		String deliver_status="1";
		String deliver_msg="ok";
		paySignReqHandler.setParameter("appkey", APPKEY);//公众号appid对应的密钥
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
	 
		URL url = new URL(path);
	        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
	        httpUrlConnection.setRequestMethod("POST");
	        httpUrlConnection.setDoOutput(true); 
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
           String retMsg=new String(readInputStream(httpUrlConnection.getInputStream()));
           System.out.println("retMsg="+retMsg+",trans_id="+bean.getTransactionId());
			JSONObject s = JSON.parseObject(retMsg);	
	        if("0".equals(s.getString("errcode"))){
	        	WxPayNotify beanFinished = new WxPayNotify();
	        	beanFinished.setTransactionId(bean.getTransactionId());
	        	listSuccess.add(beanFinished);
	        }
		  
		
		 
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

 public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }
}
