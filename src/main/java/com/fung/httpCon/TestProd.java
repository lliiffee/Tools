package com.fung.httpCon;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestProd {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 
		//	test("100084","386026");
		//	Thread.sleep(1000);
		//	test("100068","375463");
		//	Thread.sleep(1000);
		//	test("83000","267972");
		
		/*
			String encoding="UTF-8";
			  FileInputStream fis  =   null; 
	            InputStreamReader isr  =   null; 
	            FileOutputStream fos  =   null; 
	            FileOutputStream fos2  =  null; 

	            PrintWriter wf=  null; 
	            PrintWriter wf2=  null; 
	           
			 try  { 

					 fis  =   new  FileInputStream("e:\\temp\\detail2.txt"); 
					
					 isr  =   new  InputStreamReader(fis); 
		            
		              fos  =   new  FileOutputStream("e:\\temp\\detail_result.csv"); 
		              fos2  =   new  FileOutputStream("e:\\temp\\detail_result2.csv"); 
	 
		              wf=new  PrintWriter(fos);
		              wf2=new  PrintWriter(fos2);
		         //   osw.write(fileContent); 
		         //   osw.flush(); 
		            
		            
		            BufferedReader br  =   new  BufferedReader(isr); 
		            String line  =   null ; 
		             while  ((line  =  br.readLine())  !=   null ) { 
		               String[] l =  line.split(",");
		               System.out.println(l[0]);
		                 test(l[0] ,l[1], wf,wf2);
		                 Thread.sleep(1000);
		                 wf.flush();
		                 wf2.flush();
		            } 
		        }  catch  (Exception e) { 
		            e.printStackTrace(); 
		        } finally
		        {
		        	try
		        	{
		        		   fis.close();
		        	}catch(Exception e )
		        	{}
		        	try
		        	{
		 	              isr.close();    
		        }catch(Exception e )
	        	{}
			 try
	        	{
		 	              fos .close(); 
				}catch(Exception e)
				{}
			 try
	        	{
		 	              fos2.close();    }catch(Exception e)
				        	{}
			 try
	        	{
		 	              wf.close();  }catch(Exception e)
				        	{}
			 try
	        	{
		 	              wf2.close(); 
		 	                       
	        	}catch(Exception e)
				        	{}
		        	
		        }
    
			 */
		 
		getWuliuJson();

	}
	
	static String getWuliuJson()
	{
		String content="";
		try {
		String m8="http://localhost/shop/mHome/wuliu-278177.html";
		String urlAna ="{'del_pay':25,'name':'申通快递','nu':'868099473303','scores':30,'style_id':10000,'urlstr':'http://www.kuaidi100.com/kuaidiresult?id=8596331','username':''}";
		
		
		
		
		JSONObject g = JSON.parseObject(urlAna);
		Document doc = Jsoup.connect(g.getString("urlstr")).userAgent("Mozilla/5.0 &#40;Windows NT 6.1&#41; AppleWebKit/537.1 &#40;KHTML. like Gecko&#41; Chrome/21.0.1180.89 Safari/537.1")
				  .timeout(3000)
				  .get();
		String[] arrayStr=doc.data().split("\"");
	//	System.out.println(get(g.getString("urlstr") )  );
	//	String test="";
	//	System.out.println("http://www.kuaidi100.com/query?id=1&type="+arrayStr[1]+"&postid="+arrayStr[3]+"&valicode=&temp="+Math.random()   );
		
		content=get("http://www.kuaidi100.com/query?id=1&type="+arrayStr[1]+"&postid="+arrayStr[3]+"&valicode=&temp="+Math.random() )  ;
		System.out.println(content);
		JSONObject s = JSON.parseObject(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		return "";
		
	}

	private static String get(String urlstr)
	{  String content="";
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
		        
		        d= new BufferedReader(new InputStreamReader(in,"UTF-8"));
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
	
	private static void test(String shopCode,String pid,PrintWriter wf, PrintWriter wf2)throws Exception 
	{
		 
		 CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	String url="http://www.800pharm.com/shop/nm_product-"+shopCode+"-"+pid+".html";
        	System.out.println(url);
            HttpGet httpget = new HttpGet("http://www.800pharm.com/shop/nm_product-"+shopCode+"-"+pid+".html");

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
          //  System.out.println(responseBody);
            JSONObject g = JSON.parseObject(responseBody);
            
          //  JSONObject detail= g.getJSONObject("detailPro");
            JSONObject page= g.getJSONObject("page");
            try{
            	
            	  if(pid.equals(g.getString("pid"))&& page.getJSONArray("items").size() >0)
                  {
                  	wf.println(pid+","+shopCode+","+url+".html");
                  }else if (pid.equals(g.getString("pid"))&& page.getJSONArray("items").size() ==0)
                  {
                  	wf2.println(pid+","+shopCode+","+url+".html");
                  	System.out.println(pid+","+shopCode+","+url+".html");
                  }
            }catch(Exception e )
            {
            	wf2.println(pid+","+shopCode+","+url+".html");
            }
          
         //   System.out.println(pid+" : "+g.getString("pid"));
            
         
         //   System.out.println(page.getJSONArray("items").size());
            
        }catch(Exception e) 
        {e.printStackTrace();}
        finally {
           httpclient.close();
        }
		 
	}
	
	
	private static void test(String shopCode,String pid )throws Exception 
	{
		 
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	String url="http://www.800pharm.com/shop/nm_product-"+shopCode+"-"+pid+".html";
            HttpGet httpget = new HttpGet();

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
            JSONObject g = JSON.parseObject(responseBody);
            
          //  JSONObject detail= g.getJSONObject("detailPro");
            JSONObject page= g.getJSONObject("page");
            if(pid.equals(g.getString("pid"))&& page.getJSONArray("items").size() >0)
            {
            	System.out.println(pid+","+shopCode+","+url+".html");
            }else if (pid.equals(g.getString("pid"))&& page.getJSONArray("items").size() ==0)
            {
            	 
            	System.out.println(pid+","+shopCode+","+url+".html");
            }
         //   System.out.println(pid+" : "+g.getString("pid"));
            
         
         //   System.out.println(page.getJSONArray("items").size());
            
        } finally {
            httpclient.close();
        }
		 
	}
}
