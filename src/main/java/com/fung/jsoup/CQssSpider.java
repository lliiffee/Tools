package com.fung.jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CQssSpider {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url="http://data.shishicai.cn/cqssc/haoma/2014-09-04/";
		  Document doc;
			 
			 try { 	
				 Map map=new HashMap();
				 map.put("lottery", "4");
				 map.put("date", "2014-09-044");
				doc = Jsoup.connect(url).data(map).post();
				System.out.println(doc.html());
			 } catch (IOException e) {
					// TODO Auto-generated catch block
					 
				}

	}

}
