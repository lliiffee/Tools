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
		getData("e:\\tmp\\20141219.csv","e:\\tmp\\20141219_new.csv");
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
	             while  ((line  =  br.readLine())  !=   null ) { 
	            	 if(line!=null && line.length()>10)
	            	 {
	            		
	            	    if(i<1)
	            	    {
	            		//  System.out.println(line);
	            		  getProdPage("http://search.anccnet.com/searchResult2.aspx?keyword=6920190331095",pw);
	            		  
	            	    i++;
	            	    }else{
	            	    	break;
	            	    }
	            	   
	            	   // pw.flush();
	            	 
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
	
	
	
	private static void getProdPage(String url,PrintWriter	 pw)
	{
		// String url = "http://www.818.com/DrugList.shtml";
		    //    print("Fetching %s...", url);
		
	         try{
	        	
		        Document doc = Jsoup.connect(url).get();
		        System.out.println(doc.text());
		        
		       // Elements links = doc.getElementsByClass("img_box").select("a[href]");
		        Elements elements= doc.getElementsByClass("result");
		        
		        String data="";
		       // for (Element link : links) {
		        if(elements.size()>0)
		        {
		        	data=elements.get(0).select(".p-info > dd").get(2).text();
		        }
		        System.out.println(data);
		        //pw.println(data);
	         }catch(Exception e )
	         {
	        	 e.printStackTrace();
	         } 
	}
	
	
}
