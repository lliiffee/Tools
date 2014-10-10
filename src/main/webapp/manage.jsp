<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page  import="com.fung.xml.*" %>

<%@page import="java.util.Date"%>
<%@page import="org.dom4j.Element"%>
<%@page import="org.dom4j.DocumentHelper"%>
<%@page import="org.dom4j.Document"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.Reader"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.io.UnsupportedEncodingException"%>
 
 <%
	//WeiXinHandler为内部类不能使用非final类型的对象
	final String TOKEN="fung51206token";
	final HttpServletRequest final_request=request; 
	final HttpServletResponse final_response=response;
 %>
<% 
class WeiXinHandler{
	public void valid(){
		String echostr=final_request.getParameter("echostr");
		if(null==echostr||echostr.isEmpty()){
			responseMsg();
		}else{
			if(this.checkSignature()){
				this.print(echostr);
			}else{
				this.print("error");                                                                                                                                                                                                                                                                                                                                         
			}
		}
	}
	//自动回复内容
	public void responseMsg(){
		try {
			final_request.setCharacterEncoding("UTF-8");
			final_response.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		String postStr=null;
		try{
			postStr=this.readStreamParameter(final_request.getInputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("postStr"+postStr);
		if (null!=postStr&&!postStr.isEmpty()){
			Document document=null;
			try{
				document = DocumentHelper.parseText(postStr);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(null==document){
				this.print("");
				return;
			}
			Element root=document.getRootElement();
            String fromUsername = root.elementText("FromUserName");
            String toUsername = root.elementText("ToUserName");
            String keyword = root.elementTextTrim("Content");
            String time = new Date().getTime()+"";
            String textTpl =  newsTpl();          
            
            try {
				System.out.println("1:"+new String(keyword.getBytes(),"UTF-8")) ;
				System.out.println("2:"+new String(keyword.getBytes(),"GBK")) ;
				System.out.println("3:"+new String(keyword.getBytes("GBK"),"UTF-8")) ;
				System.out.println("4:"+new String(keyword.getBytes("UTF-8"),"GBK")) ;
				System.out.println("5:"+new String(keyword.getBytes("ISO-8859-1"),"GBK")) ;
				System.out.println("6:"+new String(keyword.getBytes("ISO-8859-1"),"UTF-8")) ;
				
				System.out.println("7:"+new String(java.net.URLDecoder.decode(keyword).getBytes("iso-8859-1"),"utf-8")); 
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
            
            if(null!=keyword&&!keyword.equals(""))
            {
            	if(keyword.startsWith("cx"))
            	{
            		//System.out.println("1:"+keyword.getBytes(),"UTF-8"));
          		String msgType = "news";
            	String resultStr = textTpl.format(textTpl, fromUsername, toUsername, time, msgType,keyword);
            	this.print(resultStr);
            	}else
            	{
            		this.print("Input something...");
            	}
            }else{
            	this.print("Input something...");
            }
            
            /*
			if(null!=keyword&&!keyword.equals(""))
            {
          		String msgType = "text";
            	String contentStr = "你输入了：" +keyword;
            	String resultStr = textTpl.format(textTpl, fromUsername, toUsername, time, msgType, contentStr);
            	this.print(resultStr);
            }else{
            	this.print("Input something...");
            }*/

	    }else {
	    	this.print("");
	    }
	}
	
	public String newsTpl()
	{
		return "<xml> "         
		+"<ToUserName><![CDATA[%1$s]]></ToUserName> "
		+"<FromUserName><![CDATA[%2$s]]></FromUserName> "
		+"<CreateTime>%3$s</CreateTime> "
		+"<MsgType><![CDATA[%4$s]]></MsgType> "
		
		//+" <ArticleCount>6</ArticleCount> <Articles> <item> <Title><![CDATA[到八百方商城查看更多]]></Title>  <Description><![CDATA[description1]]></Description> <PicUrl><![CDATA[http://test.800pharm.com/shop/m/images/index/logo.png]]></PicUrl> <Url><![CDATA[http://www.800pharm.com/shop/m/prodSearchList.html?search_key=cx清水>]]></Url> </item> <item><Title><![CDATA[百虹水晶清水黑发啫喱(70ml(35ml*2))-广州御采堂化妆品有限公司￥53.90]]></Title> <Description><![CDATA[description2]]></Description> <PicUrl><![CDATA[http://img.800pharm.com/images/product/100119/20131225/1387942716771.jpg]]></PicUrl> <Url><![CDATA[http://www.800pharm.com/shop/m/prodDetail.html?prod_id=508459&shop_code=100119&ref=main]]></Url> </item> <item><Title><![CDATA[【亚宝】 仲景胃灵丸 （12袋装） - 亚宝大同制药(1.2克×12袋 （浓缩丸）)-同药集团大同制药有限公司 ￥19.80]]></Title> <Description><![CDATA[description2]]></Description> <PicUrl><![CDATA[http://img.800pharm.com/images/100146/20140630/20140630135905_673.jpg]]></PicUrl> <Url><![CDATA[http://www.800pharm.com/shop/m/prodDetail.html?prod_id=586861&shop_code=100146&ref=main]]></Url> </item> <item><Title><![CDATA[仲景胃灵丸(1.2克*12袋/盒)-同药集团大同制药有限公司 ￥19.80]]></Title> <Description><![CDATA[description2]]></Description> <PicUrl><![CDATA[http://img.800pharm.com/images/product/99200/20130503/1367544601285.jpg]]></PicUrl> <Url><![CDATA[http://www.800pharm.com/shop/m/prodDetail.html?prod_id=425686&shop_code=99200&ref=main]]></Url> </item> <item><Title><![CDATA[【神光】 仲景胃灵片 （24片装）-深圳中联制药(24片)-深圳市中联制药有限公司 ￥15.80]]></Title> <Description><![CDATA[description2]]></Description> <PicUrl><![CDATA[http://img.800pharm.com/images/product/base/20111231/0104411.jpg]]></PicUrl> <Url><![CDATA[http://www.800pharm.com/shop/m/prodDetail.html?prod_id=503565&shop_code=100103&ref=main]]></Url> </item> <item><Title><![CDATA[【神光】 仲景胃灵片 （24片装）(24片)-深圳市中联制药有限公司 ￥11.80]]></Title> <Description><![CDATA[description2]]></Description> <PicUrl><![CDATA[http://img.800pharm.com/images/product/base/20111231/0104411.jpg]]></PicUrl> <Url><![CDATA[http://www.800pharm.com/shop/m/prodDetail.html?prod_id=390541&shop_code=71000&ref=main]]></Url> </item> </Articles> </xml>  ";
		+ "<ArticleCount><![CDATA[1]]></ArticleCount>  <Articles>    <item>      <Title><![CDATA[更多产品最低4折起]]></Title>      <Description><![CDATA[每逢佳节倍思亲，在这个欢聚一堂的时刻，或许你会回家陪伴父母过一个幸福的节日；或许你由于工作繁忙而不能陪伴在父母的身边，此时此刻，一份祝福父母永远年轻健康的礼物是那么的必要和珍贵。9月1日到10月7日，八百方联合各大商家举办欢度中秋，喜迎国庆活动，更多产品4折起。更有满就送，领红包，领取商家优惠券等活动。]]></Description>      <PicUrl><![CDATA[https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VK7w1NllicynibqWSWTpIkZ9emxKybeEtEK2t8pxv0GnyaIlvUXDSlWvJ7QqpJZw9Bhkj4G5SLChZFg/0]]></PicUrl>      <Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MjM5MzY5MTk4MA==&mid=202328760&idx=1&sn=9fdf58ea5250455ea04a09f307780123#rd]]></Url>    </item>  </Articles></xml>";
	}
	
	public String txtTpl()
	{
		return  "<xml>"+
				"<ToUserName><![CDATA[%1$s]]></ToUserName>"+
				"<FromUserName><![CDATA[%2$s]]></FromUserName>"+
				"<CreateTime>%3$s</CreateTime>"+
				"<MsgType><![CDATA[%4$s]]></MsgType>"+
				"<Content><![CDATA[%5$s]]></Content>"+
				"<FuncFlag>0</FuncFlag>"+
				"</xml>";
	}
	//微信接口验证
	public boolean checkSignature(){
		String signature = final_request.getParameter("signature");
        String timestamp = final_request.getParameter("timestamp");
        String nonce = final_request.getParameter("nonce");
        String token=TOKEN;
        String[] tmpArr={token,timestamp,nonce};
        Arrays.sort(tmpArr);
        String tmpStr=this.ArrayToString(tmpArr);
        tmpStr=this.SHA1Encode(tmpStr);
        if(tmpStr.equalsIgnoreCase(signature)){
			return true;
		}else{
			return false;
		}
	}
	//向请求端发送返回数据
	public void print(String content){
		try{
			final_response.getWriter().print(content);
			final_response.getWriter().flush();
			final_response.getWriter().close();
		}catch(Exception e){
			
		}
	}
	//数组转字符串
	public String ArrayToString(String [] arr){
		StringBuffer bf = new StringBuffer();
		for(int i = 0; i < arr.length; i++){
		 bf.append(arr[i]);
		}
		return bf.toString();
	}
	//sha1加密
	public String SHA1Encode(String sourceString) {
		String resultString = null;
		try {
		   resultString = new String(sourceString);
		   MessageDigest md = MessageDigest.getInstance("SHA-1");
		   resultString = byte2hexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}
	public final String byte2hexString(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
		    	buf.append("0");
		   	}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}
	//从输入流读取post参数
	public String readStreamParameter(ServletInputStream in){
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader=null;
		try{
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String line=null;
			while((line = reader.readLine())!=null){
				buffer.append(line);
	        }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=reader){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}
}
%>
<%
	WeiXinHandler handler=new WeiXinHandler();
	//handler.valid();
	handler.responseMsg();
	//out.print(handler.checkSignature());
%>
