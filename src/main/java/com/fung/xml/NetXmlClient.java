package com.fung.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class NetXmlClient {

private static final String POST = "http://www.800pharm.com/shop/pay/weixinPayNotify_m.html";
	
	public static void main(String[] args){
		sendXml();
	}
	
	public static String createXml() {
		String xml = "<xml>"
+"<OpenId><![CDATA[oUpF8uN95-Ptaags6E_roPHg7AG0]]></OpenId>"
+"<AppId><![CDATA[wx2421b1c4370ec43b]]></AppId>"
+"<IsSubscribe>1</IsSubscribe>"
+"<ProductId><![CDATA[103518878431371129]]></ProductId>"
+"<TimeStamp>1400126932</TimeStamp>"
+"<NonceStr><![CDATA[CChC5JQLhRigmGJP]]></NonceStr>"
+"<AppSignature><![CDATA[13b78571a8262e7cd9fdd90caf7742e1c4023771]]></AppSignature>"
+"<SignMethod><![CDATA[sha1]]></SignMethod>"
+"</xml>";
		return xml;
	}
	
	
	public static void sendXml(){
		try {
			URL url = new URL(POST);
			//根据Url地址打开一个连接
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpUrlConnection = (HttpURLConnection)urlConnection;
			//允许输出流
			httpUrlConnection.setDoOutput(true);
			//允许写入流
			httpUrlConnection.setDoInput(true);
			//post方式提交不可以使用缓存
			httpUrlConnection.setUseCaches(false);
			//请求类型
			httpUrlConnection.setRequestProperty("Content-Type", "text/html");
			httpUrlConnection.setRequestMethod("POST");
			//设置连接、读取超时
			httpUrlConnection.setConnectTimeout(30000);
			httpUrlConnection.setReadTimeout(30000);
			httpUrlConnection.connect();
			//使用缓冲流将xml字符串发送给服务器
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpUrlConnection.getOutputStream()));
			writer.write(URLEncoder.encode(createXml(),"UTF-8"));
			writer.flush();
			writer.close();
			writer = null;
			System.out.println("done");
			//关闭连接
			 
			 BufferedReader reader = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String lines;
				while((lines = reader.readLine())!=null){
					sb.append(lines);
				}
				
				System.out.println(sb.toString());
				
				reader.close();
			 
			httpUrlConnection.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public String getXmlText(){
//		//使用的是struts2 ，获取request很方便 O(∩_∩)O~
//		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
//		String xmlText=null;
//		try{
//			//同样使用缓冲流
//			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
//			StringBuffer sb = new StringBuffer();
//			String lines;
//			while((lines = reader.readLine())!=null){
//				sb.append(lines);
//			}
//			//客户端使用了UTF-8编码，这里使用同样使用UTF-8解码
//			xmlText = URLDecoder.decode(sb.toString(),"UTF-8");
//			reader.close();
//			reader = null;
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return xmlText;
//		
//	}
	
	/**
	 * 根据xml文件流对xmlModel赋值
	 * @param xmlText
	 * @param xmlModel
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public static synchronized Object parseXml4Bean(String xmlText,Object xmlModel){
//		try {
//			//解析字符串生成docment对象
//			Document xmlDoc = DocumentHelper.parseText(xmlText);
//			//获取根节点
//			Element element = xmlDoc.getRootElement();
//			List<Map<String,String>> propsList = new ArrayList<Map<String,String>>();
//			//解析xml中的节点名称和节点值，详细见下面parseXml
//			propsList = parseXml(element, propsList);
//			if(propsList.size()==0){
//				return null;
//			}
//			//获取Method对象数组
//			Method[] methods = xmlModel.getClass().getDeclaredMethods();
//			for(int i=0;i<propsList.size();i++){
//				for(int j=0;j<methods.length;j++){
//					Method m = methods[j];
//					if(propsList.get(i).containsKey(m.getName())){
//						try {
//							//执行set方法，将xml中的textNode值赋予Model中
//							m.invoke(xmlModel, new Object[]{propsList.get(i).get(m.getName())});
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//		return xmlModel;
//	}
	
	/**
	 * 获取xml文件中叶结点名称和节点值
	 * @param element
	 * @param propsList
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	private static List<Map<String,String>> parseXml(Element element,List<Map<String,String>> propsList){
//		if(element.isTextOnly()){
//			HashMap<String,String> map = new HashMap<String,String>();
//			String key = element.getName();
//			key = METHOD_SET+key.substring(0,1).toUpperCase().concat(key.substring(1,key.length()));
//			map.put(key,element.getTextTrim());
//			propsList.add(map);
//		}else{
//			List<Element> tmpList = element.elements();
//			for(Element e : tmpList){
//				parseXml(e, propsList);
//			}
//		}
//		return propsList;
//	}
	
}
