package com.fung.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
				    	   map.put(round, round+","+num+","+ds.substring(0,1)+ds.substring(3,4)
				    			   +","+ds.substring(1,2)+ds.substring(2,3)
				    			   +","+ds.substring(0,1)+ds.substring(2,3)
				    			     +","+ds.substring(1,2)+ds.substring(3,4)
				    			     +","+ ( Integer.parseInt(num.substring(2,3))+ Integer.parseInt(num.substring(3,4)) +Integer.parseInt(num.substring(4,5)))
				    			   );
				       }
			    		
				       
					}
					
					Set es = map.entrySet();
					Iterator it = es.iterator();
					int xs=0,
					    xd=0,ds=0,dd=0,
					    xsNoshow=0,xdNoshow=0,dsNoshow=0,ddNoshow=0,
						counter=0;
					List<String> listXs=new ArrayList<String>();
					List<String> listXd=new ArrayList<String>();
					List<String> listds=new ArrayList<String>();
					List<String> listdd=new ArrayList<String>();
					List<String> listRes=new ArrayList<String>();
					while (it.hasNext()) {
						counter++;
						Map.Entry entry = (Map.Entry) it.next();
						String k = (String) entry.getKey();
						String v = (String) entry.getValue();
						//System.out.println(v);
						
						
						
					    if(v.contains("小双"))
					    {
					    	xdNoshow++;
					    	xsNoshow=0;
					    	ddNoshow++;
					    	dsNoshow++;
					    	listXs.add("小双"+(counter-xs));
					    	xs=counter;	
					    } 
					    if(v.contains("小单"))
					    {
					    	
					    	xdNoshow=0;
					    	xsNoshow++;
					    	ddNoshow++;
					    	dsNoshow++;
					    	
					    	listXd.add("小单"+(counter-xd));
					    	xd=counter;
					    }	
					    if(v.contains("大单"))
					    {
					    	xdNoshow++;
					    	xsNoshow++;
					    	ddNoshow=0;
					    	dsNoshow++;
					    	listdd.add("大单"+(counter-dd));
					    	dd=counter;
					    }	
					    if(v.contains("大双"))
					    {
					    	xdNoshow++;
					    	xsNoshow++;
					    	ddNoshow++;
					    	dsNoshow=0;
					    	listds.add("大双"+(counter-ds));
					    	ds=counter;
					    }	
					    
					    System.out.println("NOshow: "+k+"-小单:"+xdNoshow+" 小双:"+xsNoshow+"-大单："+ddNoshow+"－大双："+dsNoshow);
					    
					    if(v.length()>4)
					    {
					    	listRes.add(v);
					    }
					}
					for(String str:listRes)
					{
						System.out.println(str);
					}
					System.out.println("88888888888888888888888888888888888");
					
					for(String str:listXs)
					{
						System.out.println(str);
					}
					System.out.println("#########################");
					for(String str:listXd)
					{
						System.out.println(str);
					}
					System.out.println("#########################");
					for(String str:listds)
					{
						System.out.println(str);
					}
					System.out.println("#########################");
					for(String str:listdd)
					{
						System.out.println(str);
					}
					
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					 
				}
	}
 
}
