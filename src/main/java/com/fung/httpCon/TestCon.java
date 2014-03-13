package com.fung.httpCon;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TestCon {

	public static void main(String[] args)
	{
		String urlstr;
		
		urlstr = "http://o.yiqifa.com/servlet/handleCpsInterIn?dt=m&interId=5191fd5fe03bbcaa579e8b03&wi=NDA4MjkzfDBfMF8wXzQ4MDVfMThDZzEw&ta=1&sd=2014-03-0313:52:02&on=9521581260993970&pn=403714&pp=219.0&cid=5270&ct=1";
	
		

	    try {
	    	CloseableHttpClient httpclient = HttpClients.createDefault();
	    	HttpGet httpGet = new HttpGet(urlstr);
	    	CloseableHttpResponse response1 = httpclient.execute(httpGet);
	    	// The underlying HTTP connection is still held by the response object
	    	// to allow the response content to be streamed directly from the network socket.
	    	// In order to ensure correct deallocation of system resources
	    	// the user MUST either fully consume the response content  or abort request
	    	// execution by calling CloseableHttpResponse#close().

	    	try {
	    	    System.out.println(response1.getStatusLine());
	    	    HttpEntity entity1 = response1.getEntity();
	    	    // do something useful with the response body
	    	    // and ensure it is fully consumed
	    	    EntityUtils.consume(entity1);
	    	} finally {
	    	    response1.close();
	    	}

	    } catch (Exception e) {
	      System.err.println("Fatal protocol violation: " + e.getMessage());
	      e.printStackTrace();
	    } finally {
	      // Release the connection.
	     // method.releaseConnection();
	    }  
             
	    
	    
	    try {
			URL url = new URL(urlstr);
			  //返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。        
	        HttpURLConnection uc = (HttpURLConnection)url.openConnection();    
	        uc.setRequestProperty("user-agent","mozilla/4.0 (compatible; msie 6.0; windows 2000)");
	        int responseCode = uc.getResponseCode();
	        System.out.print(responseCode);
	        //打开的连接读取的输入流。        
	        InputStream in = uc.getInputStream();        
	        int c;        
	        while ((c = in.read()) != -1)        
	                System.out.print(c);        
	        in.close(); 
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
	    

	}

}
