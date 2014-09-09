package com.fung.jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestJsoup {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			//File input = new File("e:\\temp\\input.html");
			//Document doc = Jsoup.parse(input, "UTF-8", "http://www.kuaidi100.com/kuaidiresult?id=8596331");
			Document doc = Jsoup.connect("http://baike.baidu.com/view/4788470.htm?fr=aladdin").userAgent("Mozilla/5.0 &#40;Windows NT 6.1&#41; AppleWebKit/537.1 &#40;KHTML. like Gecko&#41; Chrome/21.0.1180.89 Safari/537.1")
					  .timeout(3000)
					  .get();
			System.out.println(doc.html());
//			Elements scriptes = doc.select("script"); //带有href属性的a元素
//			 for(Element sc:scriptes)
//			 {
//				 System.out.println(sc.text());
//			 }
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		

	}
	
	
	static String getWuliuJson() throws Exception
	{
		
		String m8="http://localhost/shop/mHome/wuliu-278177.html";
		String urlAna ="{'del_pay':25,'name':'申通快递','nu':'868099473303','scores':30,'style_id':10000,'urlstr':'http://www.kuaidi100.com/kuaidiresult?id=8596331','username':''}";
		JSONObject g = JSON.parseObject(urlAna);
		Document doc = Jsoup.connect(g.getString("urlstr")).userAgent("Mozilla/5.0 &#40;Windows NT 6.1&#41; AppleWebKit/537.1 &#40;KHTML. like Gecko&#41; Chrome/21.0.1180.89 Safari/537.1")
				  .timeout(3000)
				  .get();
		System.out.println(doc.data());
		return "";
		
	}

	private static String get(String urlstr)
	{  String content="";
	System.out.println("test");
		  try {
				URL url = new URL(urlstr);
				  //返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。        
		        HttpURLConnection uc = (HttpURLConnection)url.openConnection();    
		        uc.setRequestProperty("user-agent","Mozilla/5.0 &#40;Windows NT 6.1&#41; AppleWebKit/537.1 &#40;KHTML. like Gecko&#41; Chrome/21.0.1180.89 Safari/537.1");
		        int responseCode = uc.getResponseCode();
		        
		        //打开的连接读取的输入流。        
		        InputStream in = uc.getInputStream(); 
		        
		        BufferedReader d= new BufferedReader(new InputStreamReader(in));
		        String tmp;
		       
		        while( (tmp=d.readLine())!=null)
		        {
		        	content+=d.readLine();
		        }
				
				
		         
		        in.close(); 
		        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        
		  return content;
	}

}
