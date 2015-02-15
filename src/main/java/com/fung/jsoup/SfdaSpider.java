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

public class SfdaSpider {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String id =getIDbyPzwh("H20130742");
		
		
		PrintWriter	 pwPending=null ;
		PrintWriter	 pwResultNormal=null  ;
		PrintWriter	 pwResultProblem=null  ;
		 FileInputStream  fis=null  ;
		 
		try {
			
			pwPending = new PrintWriter(new FileWriter("/home/ana_data/sfda/pending.csv",true));
			pwResultNormal = new PrintWriter(new FileWriter("/home/ana_data/sfda/resultNormal.csv",true));
			pwResultProblem = new PrintWriter(new FileWriter("/home/ana_data/sfda/resultProblem.csv",true));
			
			fis  =   new  FileInputStream("/home/ana_data/sfda/pzwh.csv");
			 
	         InputStreamReader isr  =   new  InputStreamReader(fis); 
	         BufferedReader br  =   new  BufferedReader(isr); 
	         String line  =   null ; 
	          while  ((line  =  br.readLine())  !=   null ) { 
	        	  String pzwh=line.split(",")[1];
	        	  
	        	  String id =getIDbyPzwh(pzwh);
	      		  if(!"".equals(id))
	      		  {
	      			 if(pzwh.startsWith("国药准字")){
	      				getDetail(id,1,pwResultNormal,pwResultProblem,pwPending,line);
	      			  }else
	      			  {
	      				getDetail(id,2,pwResultNormal,pwResultProblem,pwPending,line);
	      			  }
	      			 
	      		  }else
	      		  {
	      			pwPending.println(line);
	      		  }
	      		  
	      		pwPending.flush();
	      		pwResultNormal.flush();
	      		pwResultProblem.flush();  
	          }
	          
		} catch (IOException e2) {
			e2.printStackTrace();
		}finally{
			pwPending.close();
			pwResultNormal.close();
			pwResultProblem.close();
			try {
				fis.close();
			} catch (IOException e) {
				 fis=null;
			}
		}
		
		
		
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
	
	
	public static void getDetail(String id,int type,PrintWriter	 pwResultNormal,PrintWriter	 pwResultProblem,PrintWriter	 pwPending,String orgStr)
	{
		 
		String url="http://app1.sfda.gov.cn/datasearch/face3/"+id;
		 
		  Document doc;
		  
		  Map map=new HashMap();
		 
		  
			try {
				doc = Jsoup.connect(url)
						.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36")
						.referrer("http://app1.sfda.gov.cn/datasearch/face3/base.jsp")
						
						.post();
				
				
				Elements els = doc.getElementsByTag("td");
//				for(Element el:els)
//				{
//					System.out.println(el.html());
//				}
				
				String[] orgStrArray=orgStr.split(",");
				
				
				
				if(type==1)
				{
					String rs= orgStr+","+
							els.get(2).text()+","   //pzwh
							+els.get(20).text()+","  //原批准文号
							+els.get(4).text()+","   //商品名
							+els.get(10).text()+","   //jixing
				        //	+els.get(24).text()       //本位码
							 ;
					if( !orgStrArray[3].equals(els.get(4).text())  ||  !orgStrArray[1].equals(els.get(2).text()) )
					{
						pwResultProblem.println(rs);
					}else
					{
						pwResultNormal.println(rs);
					}
					
				}else
				{
					String rs= orgStr+","+
							els.get(2).text()+","   //pzwh
							+els.get(4).text()+","  //原批准文号
							+els.get(22).text()+","   //商品名
							+els.get(30).text()+","   //jixing
				      //  	+els.get(64).text()       //本位码
							 ;
					
					if( !orgStrArray[3].equals(els.get(22).text())  ||  !orgStrArray[1].equals(els.get(2).text()) )
					{
						pwResultProblem.println(rs);
					}else
					{
						pwResultNormal.println(rs);
					}
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pwPending.println(orgStr);
			}
			
			 
	}
}
