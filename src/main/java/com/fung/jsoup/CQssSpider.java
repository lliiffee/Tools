package com.fung.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fung.wx.model.AccessToken;
import com.fung.wx.util.WeiXinUtil;

public class CQssSpider {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CQssSpider cs=new CQssSpider();
		// TODO Auto-generated method stub
		//String url="http://data.shishicai.cn/cqssc/haoma/2014-11-05/";
		String url="http://m.hg006.com/ssc/cq/Result_Data.aspx?act=load";
		  Document doc;
			 
			 try { 	
				 Map map=new HashMap();
				 map.put("lottery", "4");
				 map.put("date", "2014-11-05");
				doc = Jsoup.connect(url).data(map).post();
				String data=doc.data();
				String[] dan=data.split("\n");
				List<String> list_danshuang=new ArrayList<String>();
				List<String> list_daxiao=new ArrayList<String>();
				List<String> list_Num=new ArrayList<String>();
				List<String> list_Sum=new ArrayList<String>();
				for(String s:dan )
				{
					 
					if(s.contains("parent.Result"))
					{
						String dd=s.split("\\(")[1].replaceAll("\\);", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll("'", "");
						
						String[] ds=dd.split(",");
						int sum=Integer.parseInt(ds[6])+Integer.parseInt(ds[7])+Integer.parseInt(ds[8]);
						String danShung= (sum%2==0)?"s":"d";
						String daxiao=(sum>13)?"B":"L";
						String result=dd+"----->"+((sum>9)?sum:"0"+sum)+"-"+danShung+"-"+daxiao;
						System.out.println(result);
						list_Num.add(ds[2].split(" ")[1]+","+ds[6]+","+ds[7]+","+ds[8] );
						list_daxiao.add(daxiao);
						list_danshuang.add(danShung);
						list_Sum.add(((sum>9)?""+sum:"0"+sum));
					}
				
				}
				
				String result="";
				if(list_Num.size()>0){
					
					int nowDaxiaoNum=0;
					int b4DaxiaoNum=0;
					String tmpNow=list_daxiao.get(0);
					String tmpB4="";
					for(int i=1;i<list_daxiao.size();i++)
					{
						 if(list_daxiao.get(i).equals(tmpNow))
						 {
							 nowDaxiaoNum=nowDaxiaoNum+1;
						 }else
						 {
							 tmpB4=list_daxiao.get(i);
						 }
						 
						 if(!tmpB4.equals(""))
						 {
							 if(list_daxiao.get(i).equals(tmpB4))
							 {
								 b4DaxiaoNum=b4DaxiaoNum+1;
							 }else
							 {
								 break;
							 }
						 }
						 
						 
					}
					
					
					
 
					
					int nowDanshuangNum=0;
					int b4DanshuangNum=0;
					String tmpNowds=list_danshuang.get(0);
					String tmpB4ds="";
					for(int i=1;i<list_danshuang.size();i++)
					{
						 if(list_danshuang.get(i).equals(tmpNowds))
						 {
							 nowDanshuangNum=nowDanshuangNum+1;
						 }else
						 {
							 tmpB4ds=list_danshuang.get(i);
						 }
						 
						 if(!tmpB4ds.equals(""))
						 {
							 if(list_danshuang.get(i).equals(tmpB4ds))
							 {
								 b4DanshuangNum=b4DanshuangNum+1;
							 }else
							 {
								 break;
							 }
						 }
						 
						 
					}
					
					
					
					if( (nowDaxiaoNum>1 && b4DaxiaoNum>=3)||(nowDanshuangNum>1 && b4DanshuangNum>=3) ||  b4DanshuangNum>=5)
					{
						 
						 
						for(int j=0;j<list_Num.size();j++)
						{
							if(j<=26)
							result=result+list_Num.get(j)+"-"+list_Sum.get(j)+"-"+list_danshuang.get(j)+"-"+list_daxiao.get(j)+"\n";
						}
						 
					}
					
					
					
					
					
				}
				
		if(!result.equals(""))		
		{
			String json ="{"
					 +"   \"touser\":\"TOUSER\","
					 +"   \"msgtype\":\"text\","
					   +"  \"text\":"
					  +"   {"
					  +"   \"content\":\"CCQ_MSG\""
					         +"   }"
					         +"}";
			cs.setToken();
			
			String fung="otwmFuFWazuAWWP1HrFhJJ3yagIg";
			String freeman="otwmFuAEiplEh22PryPkTfplIaJw";
			
			String psUrl="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
			WeiXinUtil.httpsRequest(psUrl.replace("ACCESS_TOKEN",cs.getAt().getToken() ) ,"POST", json.replace("TOUSER", fung).replace("CCQ_MSG", result));
			WeiXinUtil.httpsRequest(psUrl.replace("ACCESS_TOKEN",cs.getAt().getToken() ) ,"POST", json.replace("TOUSER", freeman).replace("CCQ_MSG", result));
			
			
		}
				
				
				
			 
				
			 } catch (IOException e) {
					// TODO Auto-generated catch block
					 
				}

	}

	private AccessToken at;
	
	public AccessToken getAt() {
		return at;
	}

	public void setToken()
	{
		//7200 过期
		if(this.at==null)
		{
			  at = WeiXinUtil.getAccessToken("wxbf261f64e52a3ceb", "6ee4bd7cb3c20c5c53a1015f3fc37edf ");  
	    	  at.setTimeL(System.currentTimeMillis());
		}else
		{
			if(System.currentTimeMillis()-at.getTimeL()/1000 >7200)
			{
				 at = WeiXinUtil.getAccessToken("wxbf261f64e52a3ceb", "6ee4bd7cb3c20c5c53a1015f3fc37edf ");  
		    	  at.setTimeL(System.currentTimeMillis());
			}
		}
		  
	}

}
