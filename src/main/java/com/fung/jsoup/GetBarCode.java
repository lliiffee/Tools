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

public class GetBarCode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//getData("e:\\tmp\\20141219.csv","e:\\tmp\\20141219_new.csv");
		getData(args[0],args[1]);
	}
//http://search.anccnet.com/searchResult2.aspx?keyword=6920190331095
	
	
	private static void getData(String reandfileName,String writelistName)
	{
		FileInputStream fis=null;
		PrintWriter	 pw=null;
		try {
			// pw = new PrintWriter(new FileWriter("e:\\temp\\818_list.txt"));
			pw = new PrintWriter(new FileWriter(writelistName));
			
			  fis  =   new  FileInputStream(reandfileName); 
	            InputStreamReader isr  =   new  InputStreamReader(fis,"utf-8"); 
	            BufferedReader br  =   new  BufferedReader(isr); 
	            String line  =   null ; 
	            int i=0;
	            String oldGin="6910672061060";
	             while  ((line  =  br.readLine())  !=   null ) { 
	            	 if(line!=null && line.length()>10)
	            	 {
	            		
	            	   // if(i<1000)
	            	    {
	            	    	if(i%20==0)
		            		 Thread.sleep(1000);
	            	    	if(i%200==0)
	            	    		Thread.sleep(1500);
	            	    	
	            	    	if(i%500==0)
	            	    		Thread.sleep(2010);
	            	    	
	            	    	if(i%1000==0)
	            	    		Thread.sleep(5120);
	            	    	
		            	      String dtm[]=line.split(",");
		            	      String gin=dtm[0];
		            	      if(gin.startsWith("6")){
		            		  getProdPage("http://search.anccnet.com/searchResult2.aspx?keyword="+gin,pw,line,oldGin);
		            		  oldGin=gin; 
		            	    i++;
		            	    }
	            	    }
	            	   
	            	    pw.flush();
	            	 
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
	
	
	
	private static void getProdPage(String url,PrintWriter	 pw,String line,String oldGin)
	{
		// String url = "http://www.818.com/DrugList.shtml";
		    //    print("Fetching %s...", url);
		 String data="";
	         try{
	        	
		        Document doc = Jsoup.connect(url).timeout(60000)
		        		.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36")
		        		.header("Origin", "http://search.anccnet.com")
		        		.header("Referer", "http://search.anccnet.com/searchResult2.aspx?keyword="+oldGin)
		        		
		        		.get();
		      //  System.out.println(doc.text());
		        
		       // Elements links = doc.getElementsByClass("img_box").select("a[href]");
		        Elements elements= doc.getElementsByClass("result");
		        
		       
		       // for (Element link : links) {
		        if(elements.size()>0)
		        {
		        	Element fistResult=elements.get(0);
		        	Elements res=fistResult.select(".p-info > dd");
		        	Elements suplis=fistResult.select(".p-supplier > dd");
		        	//规格，名称，生产企业，商标
		        	data=line+","+res.get(2).text().replace(",", ";")+","+res.get(1).text()+","+suplis.get(1).text()+","+suplis.get(0).text();
		        }else
		        {
		        	data=line+",,,,";
		        }
		      // System.out.println(data);
		       
	         }catch(Exception e )
	         {
	        	 e.printStackTrace();
	        	 data=line+",,,,";
	         } 
	         
	         pw.println(data.replaceAll("\n", "").replaceAll("\r", ""));
	         
	}
	
	
}
