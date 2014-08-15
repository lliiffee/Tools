package com.fung.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App818ProdLinks {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  String url = "http://www.818.com/DrugList.shtml?GID=1&SID=522&IsMerge=1&Order=2&TID=526";
	        url="http://www.okooo.com/soccer/league/20/schedule/11692/1-5-1/";
	        url="http://www.818.com/SeoUpDrugsList.aspx?p=";
	        // fetch the specified URL and parse to a HTML DOM
	        
	        Document doc;
			 
			for(int i=1;i<675;i++)
			{
				try {
				doc = Jsoup.connect(url+i).get();
				 
				 Elements links = doc.select("div.mc").select("a[href]");    
				 for (Element link : links) {
		    		 
			    		String l_str=link.attr("abs:href");
			    		if(l_str.contains("DrugDetails"))
			    		System.out.println(l_str);
				 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(url+i);
				}
			}
				
				 
		 

           
	        
	}

}
