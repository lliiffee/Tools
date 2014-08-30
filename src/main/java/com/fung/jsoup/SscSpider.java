package com.fung.jsoup;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SscSpider {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  String url = "http://caipiao.taobao.com/lottery/order/lottery_jxssc.htm?spm=a2126.6843133.1151804389.d4913005.X43xul";
	       
	        Document doc;
			 
			 try {
				doc = Jsoup.connect(url).get();
				Map map=new TreeMap();
				 //<span class="wf">001 </span> <span class="kjh"> 31312<br /> </span> <span class="h2 red"> 小单小双 </span> <span class="h3 orange"> 312 </span> </td> 
				 Elements tds = doc.select("table.rules-table").select("td");
					for(Element td:tds)
					{
						String round=td.select("span.wf").text();
						 
						String num=td.select("span.kjh").text();
						String ds=td.select("span.h2.red").text(); 
				       if(round.length()>2&&num.length()>4)
				       {
				    	   map.put(round, round+","+num+","+ds.substring(0,1)+ds.substring(3,4));
				       }
			    		
					}
					
					Set es = map.entrySet();
					Iterator it = es.iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						String k = (String) entry.getKey();
						String v = (String) entry.getValue();
						System.out.println(v);
					}
				 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					 
				}
	}
 
}
