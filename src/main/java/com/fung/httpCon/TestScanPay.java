package com.fung.httpCon;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestScanPay {


	public static void main(String[] args)
	{
		//服务编号 ，必填， 威富通提供的交易服务编号，每个商家唯一(明文，不参与加密)
		String agCode="ylwl_pwd";
		//商户编号 ，必填，威富通商户编号 （如003975552110001）
		String mId="001075552110006";
	  //	加密密码key
		String key="ylwl_pwd";
		//系统地址
		String server="http://www.swiftpass.cn:8080";
		String createOrder="/order/mchdown_create_order";
		//通知url ，必填，威富通回调商家的URL，调用该URL传递参数outOrder_no,通知该订单的成功状态。
		String notify_url="http://www.800pharm.com";

		//商家通知订单号 ，必填，通知商家订单号，该订单号由商家生成，长度固定32位字符，可包含字母，确保在商家系统内唯一，商家也可以根据该通知no查询订单状态.
		String outOrder_no="1234";

		//金额 ，必填，订单金额，单位分。
		String total_fee="100";

		//接口版本 ，非必填，版本号，默认为1.0
		String service_version="1.0";
		//签名  ，必填，原串按照上面字段顺序，例如：“mId=2000000501&notify_url=2013-12-05&outOrder_no=1&total_fee=200”。如字段值为空，则不参与签名。参数签名经过约定的加密方法和加密密码后生成密文。
		String sign="";
		
		//http://www.swiftpass.cn:8080/order/mchdown_create_order?agCode=test&sign=6ae784b861d880660181449aa122db85dc01c3ea423891cad4017181ab637e3bee6743f266c2cbd9a74013578931b9d2
		String parStr="mId="+mId+"&notify_url="+notify_url+"&outOrder_no="+outOrder_no+"&total_fee="+total_fee;
		
		String url= server+createOrder+"?agCode="+agCode
				+"&outOrder_no="+outOrder_no
				+"&sign="+ExcelCrypt.encrypt(parStr, key);
				;
		
      test(url);
	}
	
	
	private static void test(String url) 
	{
		 
		 CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        
            HttpGet httpget = new HttpGet(url);

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
           
            
        }catch(Exception e) 
        {e.printStackTrace();}
        finally {
        	try
        	{
        		 httpclient.close();
        	}catch(Exception e)
        	{}
          
        }
		 
	}
}
