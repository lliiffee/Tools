package com.fung.jsoup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetEval {
	
	//http://zone.suning.com/review/pro_review/125319782-0-35--.html
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		 getDetail2( );
		 
		// TODO Auto-generated method stub
		//String id =getIDbyPzwh("H20130742");
		
		
//		PrintWriter	 pwPending=null ;
//		PrintWriter	 pwResultNormal=null  ;
//		PrintWriter	 pwResultProblem=null  ;
//		 FileInputStream  fis=null  ;
		 
//		try {
			
//			pwPending = new PrintWriter(new FileWriter("/home/ana_data/sfda/pending.csv",true));
//			pwResultNormal = new PrintWriter(new FileWriter("/home/ana_data/sfda/resultNormal.csv",true));
//			pwResultProblem = new PrintWriter(new FileWriter("/home/ana_data/sfda/resultProblem.csv",true));
//			
//			fis  =   new  FileInputStream("/home/ana_data/sfda/pzwh.csv");
//			 
//	         InputStreamReader isr  =   new  InputStreamReader(fis); 
//	         BufferedReader br  =   new  BufferedReader(isr); 
//	         String line  =   null ; 
//	          while  ((line  =  br.readLine())  !=   null ) { 
//	        	  String pzwh=line.split(",")[1];
//	        	  
//	      		pwPending.flush();
//	      		pwResultNormal.flush();
//	      		pwResultProblem.flush();  
//	          }
//	          
//		} catch (IOException e2) {
//			e2.printStackTrace();
//		}finally{
//			pwPending.close();
//			pwResultNormal.close();
//			pwResultProblem.close();
//			try {
//				fis.close();
//			} catch (IOException e) {
//				 fis=null;
//			}
//		}
		
		
		
	}

   //根据批准文号获得id	
	public static String getIDbyPzwh(String pzwh)
	{
		
		 
				
		 String result="";
		String url="http://app1.sfda.gov.cn/datasearch/face3/search.jsp";
		 
		  Document doc;
		  
		  Map map=new HashMap();
		 
		  if(pzwh.startsWith("国药准字"))
		  {//国药准字H20052237
			    map.put("bcId", "118102890099723943731486814455");
				 map.put("State", "1");
				 map.put("tableId", "25");
				 System.out.println(pzwh);
		  }else
		  {//H20130742
			  map.put("bcId", "124356651564146415214424405468");
				 map.put("State", "1");
				 map.put("tableId", "36");
		  }
		  
		     
			 try {
				map.put("keyword", URLEncoder.encode(pzwh,"UTF-8") );
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				doc = Jsoup.connect(url)
						.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36")
						.referrer("http://app1.sfda.gov.cn/datasearch/face3/base.jsp")
						.data(map)
						.post();
				
			//	System.out.println(doc.html());
				Elements links = doc.getElementsByAttributeValueStarting("href", "javascript:commitForECMA");
				 for (Element link : links) {
			        	String href=link.attr("href");
			        	String[] dataArray=href.split(",");
			        	if(dataArray.length>0){
			        		//System.out.println(href.split(",")[1].replaceAll("'", ""));
			        		result=href.split(",")[1].replaceAll("'", "");
			        	}
			        	
			        }
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
			
			 
	}
	//p-comment
	public static void getDetail2( )
	{
		 
		 
		
		 
		  Document doc;
		  
		  Map map=new HashMap();
		 
		  
			try {
				for(int i=0;i<=10;i++)
				{
					String url="http://zone.suning.com/review/pro_review/124852728-0-"+i+"--.html";
					
					doc = Jsoup.connect(url)
							.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36")
							.referrer("http://app1.sfda.gov.cn/datasearch/face3/base.jsp")
							
							.post();
					
					//System.out.println(doc.select(".detail > li > div.content > p").text());
					
					 Elements els =doc.select("div.content");
					 
					 for(Element e:els)
					 {
						 System.out.println(e.getElementsByTag("p").get(0).text().split("201")[0]);
					 }
					 
				}
				
 
				 
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 
			}
			
			 
	}
	
	
	public static void getDetail( )
	{
		 
		 
		
		 
		  Document doc;
		  
		  Map map=new HashMap();
		 
		  
			try {
				for(int i=1;i<=37;i++)
				{
					String url="http://zone.suning.com/review/pro_review/125319782-0-"+i+"--.html";
					
					doc = Jsoup.connect(url)
							.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36")
							.referrer("http://app1.sfda.gov.cn/datasearch/face3/base.jsp")
							
							.post();
					
					//System.out.println(doc.select(".detail > li > div.content > p").text());
					
					 Elements els =doc.select("div.content");
					 
					 for(Element e:els)
					 {
						 System.out.println(e.getElementsByTag("p").get(0).text().split("201")[0]);
					 }
					 
				}
				
 
				 
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 
			}
			
			 
	}

}
