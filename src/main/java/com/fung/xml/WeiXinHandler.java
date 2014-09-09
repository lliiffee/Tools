package com.fung.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdom.JDOMException;



 


public class WeiXinHandler {
	private  HttpServletRequest final_request; 
	private  HttpServletResponse final_response;
	private String TOKEN="fung51206token";
	
	/** 应答的参数 */
	private SortedMap parameters; 
	
	/** 微信应答参数 */
	private SortedMap wxparameters;
	
	public WeiXinHandler(HttpServletRequest request, HttpServletResponse response)
	{
		this.final_request=request;
		this.final_response=response;
		this.parameters = new TreeMap();
		this.wxparameters = new TreeMap();
	}

	
	public void init()
	{
		 String xmlContent="";  
		 BufferedReader reader = null;
		 String enc="UTF-8";
	        try{  
	            //同样使用缓冲流  
	            reader = new BufferedReader(new InputStreamReader(final_request.getInputStream(),enc));  
	            StringBuffer sb = new StringBuffer();  
	            String lines;  
	            while((lines = reader.readLine())!=null){  
	                sb.append(lines);  
	            }  
	           
	            System.out.println(sb.toString());
	            //客户端使用了UTF-8编码，这里使用同样使用UTF-8解码       //utf-8 会报 java.io.UTFDataFormatException: Invalid byte 2 of 2-byte UTF-8 sequence
	            xmlContent = URLDecoder.decode(sb.toString(),enc); 
	            
	          //解析xml,得到map
	          //  this.doParse(xmlContent);
	            doParse(final_request);
	        }catch(Exception e){  
	            e.printStackTrace();  
	        }finally
	        {
	        	try{
	        		reader.close(); 
	        	}catch(Exception e)
	        	{
	        		
	        	}finally
	        	{
	        		reader = null; 
	        	} 
	        	 
	        }
		
	        
		
	}
	//自动回复内容
	public void responseMsg(){
		try {
			final_request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final_response.setCharacterEncoding("UTF-8");
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
            
		   
            if(null!=keyword&&!keyword.equals(""))
            {
            	if(keyword.startsWith("cx"))
            	{
            		try {
						System.out.println("1:"+new String(keyword.getBytes(),"UTF-8")) ;
						System.out.println("2:"+new String(keyword.getBytes(),"GBK")) ;
						System.out.println("3:"+new String(keyword.getBytes("GBK"),"UTF-8")) ;
						System.out.println("4:"+new String(keyword.getBytes("UTF-8"),"GBK")) ;
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
		+"<ArticleCount>2</ArticleCount> "
		+"<Articles> "
		+"<item> "
		+"<Title><![CDATA[%5$s]]></Title>  "
		+"<Description><![CDATA[description1]]></Description> "
		+"<PicUrl><![CDATA[http://img.800pharm.com/images/product/base/20101214/EZA022002G1.jpg]]></PicUrl> "
		+"<Url><![CDATA[http://www.800pharm.com/shop/m/prodDetail.html?prod_id=81963&shop_code=80000&ref=main]]></Url> "
		+"</item> "
		+"<item> "
		+"<Title><![CDATA[title2]]></Title> "
		+"<Description><![CDATA[description2]]></Description> "
		+"<PicUrl><![CDATA[http://img.800pharm.com/images/product/99999/20131031/lists_1383186782556.JPG]]></PicUrl> "
		+"<Url><![CDATA[http://www.800pharm.com/shop/m/prodDetail.html?prod_id=485056&shop_code=99999&ref=main]]></Url> "
		+"</item> "
		+"</Articles> "
		+"</xml>  ";
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
					reader=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 解析XML内容到  wxparameters
	 */
	protected void doParse(String xmlContent) throws JDOMException, IOException {
		//解析xml,得到map
		Map m = XMLUtil.doXMLParse(xmlContent);
		
		//设置参数
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			String k = (String) it.next();
			String v = (String) m.get(k);
			//this.setWxparameter(k.toLowerCase(), v);
			this.setWxparameter(k, v);
		}
		
	}
	
	
	private void doParse(HttpServletRequest request)
	{
		String postStr=null;
		try{
			postStr=this.readStreamParameter(request.getInputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(postStr);
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
			Iterator it  = root.elementIterator();
			while(it.hasNext())
			{
				Element e=(Element)it.next();
				String k = e.getName();
				String v = e.getText();
				this.setWxparameter(k, v);
			}
		}
			
	}
		
	
	/**
	 * 获取微信参数值
	 * @param parameter 参数名称
	 * @return String 
	 */
	public String getWxparameter(String wxparameter) {
		String s = (String)this.wxparameters.get(wxparameter); 
		return (null == s) ? "" : s;
	}
	
	/**
	 * 设置微信参数值
	 * @param parameter 参数名称
	 * @param parameterValue 参数值
	 */
	public void setWxparameter(String wxparameter, String wxparameterValue) {
		String v = "";
		if(null != wxparameterValue) {
			v = wxparameterValue.trim();
		}
		this.wxparameters.put(wxparameter, v);
	}
	
	/**
	 * 返回微信所有的参数
	 * @return SortedMap
	 */
	public SortedMap getAllWxparameters() {
		return this.wxparameters;
	}
	
}
