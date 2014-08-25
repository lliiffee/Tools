package com.fung.jsoup;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TBScore {

	
	public static void main(String[] args){
		System.out.println("begin");
		for(int i=1;i<39;i++)
		{
			//http://www.okooo.com/soccer/league/8/schedule/8578/1-36- //西甲 14-15
			//http://www.okooo.com/soccer/league/17/schedule/8186/1-1- //英超 14-15
			getHistoryData("http://www.okooo.com/soccer/league/35/schedule/1557/1-42-"+i);
		}
		
	}
	
	
	public static void getHistoryData(String url)
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
					  System.out.println(result);
				  }
		  }catch (IOException e) {
				// TODO Auto-generated catch block
				 
			}
		  
	}
	/**
	 * @param args
	 */
	public static void getDailyData() {
		// TODO Auto-generated method stub
   String url="http://caipiao.taobao.com/lottery/order/lottery_jczq_spf.htm?spm=a2126.6847361.0.0.j1ZOmJ";
		//http://www.okooo.com/soccer/league/17/schedule/2/1-1-1/
		 Document doc;
		 
		List<ScoreBean> list=new ArrayList<ScoreBean>();	 
		
				try {
				doc = Jsoup.connect(url).get();
				 
				 Elements table = doc.select("#raceMain");   
				 
				// System.out.println(table.html());
				 
				 Elements trs=table.select("tr");
				  for(Element eltr:trs)
				  {
					  StringBuffer rateStr=new StringBuffer("0");
					
							 if(eltr.select("td.league").text().length()>0)
							 {
								 ScoreBean bean=new ScoreBean();
								  
									
								  bean.setLeagueName(eltr.select("td.league").text());
								  bean.setTeamHome(eltr.select("td.homeTeam").select("a").attr("title"));
								  bean.setTeamAway(eltr.select("td.awayTeam").select("a").attr("title"));
									 
								  Elements rateArray=eltr.select("td.bet>ul>li");
								
								  for(Element li:rateArray)
								  {  
									 rateStr.append("#"+li.text());
								  }
								  
								  
							 	  String[] rateAr=rateStr.toString().split("#");
								  
								  
								  bean.setRateHomeWin(new BigDecimal(rateAr[1]));
									 
							 
								  bean.setRateHomeDraw(new BigDecimal(rateAr[2]));
							   
								  bean.setRateHomeLose(new BigDecimal(rateAr[3]));
								  
								  if(bean.getRateHomeWin().compareTo(bean.getRateHomeLose())==-1)
								  {
									  bean.setTeamToWin(bean.getTeamHome());
									  bean.setTeamToLose(bean.getTeamAway());
									  bean.setRateTeamWin(bean.getRateHomeWin());
									 
								  }else
								  {
									  bean.setTeamToWin(bean.getTeamAway());
									  bean.setTeamToLose(bean.getTeamHome());
									  bean.setRateTeamWin(bean.getRateHomeLose());
								 
								  }
								  
								  bean.setRateTeamDaw(bean.getRateHomeDraw());
								  
								  list.add(bean);
								  
							  }
					 }
					 
				   
				  BigDecimal base=new BigDecimal(2);
				  
				  for(ScoreBean team:list)
				  {
					  //西甲,巴列卡诺,马竞,6.20,4.20,1.38
					  for(ScoreBean team2:list)
					  {
						  if(!team.getTeamHome().equals(team2.getTeamHome()))
						  {
						  System.out.println(team.getTeamToWin()+":"+team.getTeamToLose()
								  +"-"+team2.getTeamToWin()+":"+team2.getTeamToLose());
						   
						   System.out.println("w-w:"+ team.getRateTeamWin().multiply(team2.getRateTeamWin()).multiply(base));
						   
						   System.out.println("w-d:"+ team.getRateTeamWin().multiply(team2.getRateTeamDaw()).multiply(base));
						   
						   System.out.println("d-d:"+ team.getRateTeamDaw().multiply(team2.getRateTeamDaw()).multiply(base));
						   
						   System.out.println("d-w:"+ team.getRateTeamDaw().multiply(team2.getRateTeamWin()).multiply(base));
						  
						  }
						  
						  
					  }
					    
				  }
				 
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					 
				}
		 
				
				
			
	}

}
