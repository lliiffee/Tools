package com.fung.jsoup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App818Spider {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(args[0]+" "+args[1]);
		getData(args[0],args[1]);
		
	}
	
	private static void getAllPages()
	{

		 String url = "http://www.818.com/DrugList_1_0.shtml";
		       print("Fetching %s...", "begin");
	         try{
		        Document doc = Jsoup.connect(url)
		        		
		        		.header("user-agent","mozilla/4.0 (compatible; msie 8.0; windows NT)")
		        		.referrer("www.baidu.com?key=%ca23%22da%dadd%2323d").get();
		        Elements links = doc.select(".main_nav > li > a");
		       //img_box
		        for (Element link : links) {
		        	String href=link.attr("abs:href");
		            if(href.contains("DrugList_") && !"http://www.818.com/DrugList_6_498.shtml".equals(href)){
		            	 printLevel(href);
		             	//System.out.println(href);
		            }
		        }
		        
	         }catch(Exception e )
	         {
	        	 e.printStackTrace();
	         }
	}
	
	private static void getData(String fileName,String listName)
	{
		FileInputStream fis=null;
		PrintWriter	 pw=null;
		try {
			// pw = new PrintWriter(new FileWriter("e:\\temp\\818_list.txt"));
			pw = new PrintWriter(new FileWriter(listName));
			
			  fis  =   new  FileInputStream(fileName); 
	            InputStreamReader isr  =   new  InputStreamReader(fis); 
	            BufferedReader br  =   new  BufferedReader(isr); 
	            String line  =   null ; 
	             while  ((line  =  br.readLine())  !=   null ) { 
	            	 if(line!=null && line.length()>10)
	            	 {
	            	  for(int i=1;i<=50;i++)
	            	  {  
	            	    getProdPage(line+"&Page="+i,pw);
	            	    pw.flush();
	            	  }
	            	  
	            	 }
	             }
	                
			 
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try {
				fis.close();
				pw.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static void printLevel(String url)throws Exception
	{
		//System.out.println(url);
		 Document doc = Jsoup.connect(url)
				 .header("user-agent","mozilla/4.0 (compatible; msie 8.0; windows NT)")
	        		.referrer(url).get();
		Elements links = doc.select("#dl_Class").select("a[href]");
		//System.out.println(url+"###"+links.size());
    	for (Element link : links) {
    		 
    		String l_str=link.attr("abs:href");
           if(l_str.contains("DrugList"))
           {
        	   Thread.sleep(2000);
        	   
        	  Document doc2 = Jsoup.connect(l_str)
        			  .header("user-agent","mozilla/4.0 (compatible; msie 8.0; windows NT) spider")
       		.referrer(url).get();
		        Elements links2 = doc2.select("#dl_Class").select("a[href]");
		      //  System.out.println(l_str);
		        
		        if(  links2.size()>0)
		        {  
		        	//打印三级
		        	 for (Element l : links2) {
				        	System.out.println(l.attr("abs:href"));
                            }
		        	
		        }else
		        {
		        	//没有下一级打印二级
		        	System.out.println(link.attr(l_str));
		        }
		        
		      }
          
        	  
           }
	}
	private static void getProdPage(String url,PrintWriter	 pw)
	{
		// String url = "http://www.818.com/DrugList.shtml";
		    //    print("Fetching %s...", url);
		
	         try{
	        	
		        Document doc = Jsoup.connect(url).get();
		        
		        
		        
		        Elements links = doc.getElementsByClass("img_box").select("a[href]");
		        
		       //img_box
		        for (Element link : links) {
		        	
		        	try
		        	{
		        	 String url2 = link.attr("abs:href");
		        	 String num=doc.select(".product_td5 > a").text();
			           if(url2.contains("DrugList"))
			           {
			        	   Thread.sleep(4000);
				           
			
				            // fetch the specified URL and parse to a HTML DOM
				            Document doc2 = Jsoup.connect(url2).get();
			                Elements els=doc2.getElementsByClass("wt_detail");
			                
			                String pzwh=els.select("#Approval").text();
			                if(pzwh.contains("复制")){
			                pzwh=pzwh.substring(0,pzwh.indexOf("复制"));
			                }
			                String data=""+els.select("#price_818").text()
			                		//+ ","+ num //els.select("#shopCount").text()
			                		+ ","+els.select("#price_f").text()
			                		+ ","+els.select("#allSale").text()
			                		+ ","+els.select("#GeneralName").text()
			                		+ ","+els.select("#MarkName").text()
			                		+ ","+els.select("#Spec").text()
			                		+ ","+pzwh
			                		+ ","+url2
			                		;
			                pw.println(data);
	                System.out.println(data);
			                
				          //  HtmlToPlainText formatter = new HtmlToPlainText();
				           // String plainText = formatter.getPlainText(doc);
				            
				          //  System.out.println(formatter.getPlainText(doc.getElementsByClass("wt_detail").select("#price_818").first()));
			           }else if(url2.contains("DrugDetails")){
			        	   
			        	   Document doc2 = Jsoup.connect(url2).get();
			        	   //doc.select("strong.f_f60").text()+doc.select(".li_item > del").text()
			        	   
			        	    Element el=doc2.getElementById("uDrugsInfo");
			        	    Element desc=doc.getElementById("c_spsm");
			                String pzwh="";
			                try{
			                desc.select("ul").get(4).select(".td2").text();
			                if(pzwh.contains("复制")){
			                pzwh=pzwh.substring(0,pzwh.indexOf("复制"));
			                }
			                }catch(Exception e)
			                {
			                	
			                }
			                String generalName="";
			                try{
			                	generalName=desc.select("ul").get(1).select("a").text();
			                }catch (Exception e)
			                {
			                	
			                }
			                
			               
			                String markName="";
			                try{
			                	markName=desc.select("ul").get(2).select(".td2").text();
			                }catch (Exception e)
			                {
			                	
			                }
			                
			                String spec="";
			                try{
			                	spec=desc.select("ul").get(3).select(".td2").text();
			                }catch (Exception e)
			                {
			                	
			                }
			                
			                
			                
			                String data=""+el.select("strong.f_f60").text()  //price_818
			                		//+ ","+ "1"
			                		+ ","+el.select(".li_item > del").text() //price_f
			                		+ ","+el.select("ul > li > span > em.f_f60").text() //allSale
			                		+ ","+generalName   //GeneralName
			                		+ ","+markName //MarkName
			                		+ ","+spec //Spec
			                		+ ","+pzwh
			                	
			                		+ ","+url2
			                		;
			                pw.println(data);
			           }
		        	}catch(Exception e)
		        	{
		        		e.printStackTrace();
		        	}
		            
		        }
		        
	         }catch(Exception e )
	         {
	        	 e.printStackTrace();
	         } 
	}
	
	 private static void print(String msg, Object... args) {
	        System.out.println(String.format(msg, args));
	    }

	    private static String trim(String s, int width) {
	        if (s.length() > width)
	            return s.substring(0, width-1) + ".";
	        else
	            return s;
	    }

}
