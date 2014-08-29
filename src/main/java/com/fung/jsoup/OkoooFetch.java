package com.fung.jsoup;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OkoooFetch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        //获取赛季杯赛信息
		getLeagueHistory();
	}
	
	
	public static void getLeagueHistory()
	{
		//LotteryList_Data
		//String url="http://www.okooo.com/soccer/league/23/";
		
		String url="http://www.okooo.com/soccer/league/8/";
		//http://www.okooo.com/soccer/league/17/schedule/2/1-1-1/
		 Document doc;
		 
		       //List<ScoreBean> list=new ArrayList<ScoreBean>();	 
		 PrintWriter	 pw ;
				try {
					pw = new PrintWriter(new FileWriter("e:\\temp\\span.txt",true));
				doc = Jsoup.connect(url).get();
				Elements links =doc.select("div.LotteryList_Data>ul>li").select("a[href]");
				for (Element link : links) {
					String l_str=link.attr("abs:href");
			    	if(link.text().contains("赛季") && l_str.contains("schedule"))
			    	{
			    		String league=link.text().substring(0,2).trim();
			    		String st=link.text().substring(2,5).trim();
			    		String end=link.text().substring(6,9).trim();
			    		if(st.startsWith("9"))
			    		{
			    			st="19"+st;
			    		}else
			    		{
			    			st="20"+st;
			    		}
			    		
			    		if(end.startsWith("9"))
			    			
			    		{
			    			end="19"+end;
			    		}else
			    		{
			    			end="20"+end;
			    		}
				    	
			    		System.out.println(l_str);
			    		 
			    		
			    		for(int i=1;i<39;i++)
			    		{
			    			
			    			//http://www.okooo.com/soccer/league/37/schedule/8170/1-39-1///荷甲
			    			//getHistoryData("http://www.okooo.com/soccer/league/37/schedule/8170/1-39-"+i);
			    		//	getHistoryData( l_str+"/1-33-"+i+"/",league,st,end,pw);
			    			getHistoryData( l_str+"/1-36-"+i+"/",league,st,end,pw);
		    				
			    		}
			    		
			    	}
					
				 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					 
				}
	}
	
	
	private static void getHistoryData(String url,String name, String startY, String endY,PrintWriter	 pw)
	{
		//
		 // String url="http://www.okooo.com/soccer/league/17/schedule/2/1-1-1/";
		  Document doc;
 
		  try {
				doc = Jsoup.connect(url).get();
				
				 
				
				 Elements table = doc.select("#team_fight_table");  
				 Elements trs=table.select("tr:not(.LotteryListTitle)");
				  for(Element eltr:trs)
				  {
					  String result="";
					  for(Element td :eltr.select("td"))
					  {
						  result+=","+td.text();
					  }
					  //System.out.println(result);
					  String[] array=result.split(",");
					  String dateStr=array[1];
					  if(dateStr.length()>0)
					  {
						  String time="";
						  if( Integer.parseInt(dateStr.split("-")[0]) >7  )
						  {
							  time= startY+"-"+dateStr;
						  }else
						  {
							  time=endY+"-"+dateStr;
						  }
						  //,08-07 02:45,1,尼斯,0-1,里昂,3.36,3.13,2.03,欧/析
						  String scoreA="";
						  String scoreB="";
						  String[] s =array[4].split("-");
						  if(s.length>1)
						  {
							  scoreA=s[0];
							  scoreB=s[1];
						  }
						  String res=name+","+time+","+array[2]+","+array[3]+","+array[5]
								  +","+array[4]+","+array[6]+","+array[7]+","+array[8]
										  +","+scoreA	  +","+scoreB;
						  System.out.println(res);
						  pw.println(res );
					  }
				  }
				  pw.flush();
		  }catch (IOException e) {
				// TODO Auto-generated catch block
				 
			}finally
			{
				 
			}
		  
	}

}
