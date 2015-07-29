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

import com.tenpay.RequestHandler;
import com.tenpay.util.TenpayUtil;

public class NetXmlClient {

   //private static final String POST = "http://localhost/shop/pay/weixinPayNotify_m.html";
//	private static final String POST = "http://localhost/shop/pay/wxPayNotify.html?notify_id=fRon5-l4lKqNKwCeurlCz3u617mgFhKiuFvwdmnwN53r58yWelLWJMPcqHzCjzvN62AkXb2bdEMBIBmPn9JgWCXwXL69HA56&partner=1219895801&transaction_id=1219895801201408133319316201&sign=7EE3F867643E0C0E0DEF8D51B43B15C0&product_fee=1&total_fee=1&time_end=20140813162348&trade_state=0&out_trade_no=309757004432287&transport_fee=0&fee_type=1&trade_mode=1&sign_type=MD5&input_charset=UTF-8&discount=0&bank_type=0&";	
//	private static final String POST = "http://localhost/shop/pay/weixinPayComp_m.html";
//	private static final String POST = "http://localhost/shop/wx_search.html";
//	private static final String POST = "http://www.800pharm.com/shop/wx_search.html";
	private static final String POST = "http://192.168.0.51/shop/wx_search.html";
//	private static final String POST = "http://localhost/shop/wx_search.html";
//	private static final String POST = "http://www.800pharm.com/shop/synchronizeProduct";
	
//	private static final String POST = "http://localhost/shop/pay/weixinPayServErr_m.html";
	
//	private static final String POST = "http://www.800pharm.com/shop/pay/wftPayNotify.html?transaction_id=dfasdfasdf&outOrder_no=14483368549253&sign=a5becc226bdfff4fe51c311b760dad9d5d26c8c63d84d14294f4bc487139f660";
	public static void main(String[] args){
		//sendXml(pkgReq());
	//	sendXml(retNotify());
		//System.out.println(wiquanReq());
		//sendXml(wiquanReq());
		sendXml(wxSeed());
//		try {
//			sendXml( rtWarn());
//		} catch (Exception e) {
//		
//			e.printStackTrace();
//		}
		
	}
	
	public static String wxSeed()
	{
		try {
			return  "<xml>"
					+"<FromUserName>test1</FromUserName>"
					+"<ToUserName>test3</ToUserName>"
					+"<Content>感冒</Content>"
					+"<MsgType>text</MsgType>"
				//+"<MsgType>event</MsgType>"
				// +"<Event>CLICK</Event>"
				// +"<EventKey>7</EventKey>"
					+"</xml>"; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static String createXml() {
		String xml = 
				"<xml>"
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
	public static String wiquanReq()
	{
		String xml=  "<xml>"  +
				"<OpenId><![CDATA[oDF3iY9P32sK_5GgYiRkjsCo45bk]]></OpenId><AppId><![CDATA[wxf8b4f85f3a794e77]]></AppId>"
				+"<TimeStamp>1393400471</TimeStamp>"
				+"<MsgType><![CDATA[request]]></MsgType>"
				+"<FeedBackId>7197417460812502768</FeedBackId>"
				+"<TransId><![CDATA[1900000109201402143240185685]]></TransId>"
				+"<Reason><![CDATA[质量问题]]></Reason>"
				+"<Solution><![CDATA[换货]]></Solution>"
				+"<ExtInfo><![CDATA[备注 12435321321]]></ExtInfo>"
				+"<AppSignature>"
				+"<![CDATA[e3bc0bab6fa9794e949cd17dab420a7668b664e7]]></AppSignature>"
				+"<SignMethod>"
				+"<![CDATA[sha1]]>"
				+"</SignMethod>"
				+"<PicInfo>"
				+"<item><PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz/49ogibiahRNtOk37iaztwmdgFbyFS9FUrqfodiaUAmxr4hOP34C6R4nGgebMalKuY3H35riaZ5vtzJh25tp7vBUwWxw/0]]></PicUrl>"
				+"</item>"
				+"<item>"
				+"<PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz/49ogibiahRNtOk37iaztwmdgFbyFS9FUrqfn3y72eHKRSAwVz1PyIcUSjBrDzXAibTiaAdrTGb4eBFbib9ibFaSeic3OIg/0]]></PicUrl>"
				+"</item>"
				+"<item>"
				+"<PicUrl><![CDATA[]]></PicUrl></item><item><PicUrl><![CDATA[]]></PicUrl></item><item><PicUrl><![CDATA[]]></PicUrl></item>"
				+"</PicInfo> </xml>";
		
		xml="<xml><OpenId><![CDATA[oTNesjrqNt4ZIZ8zRe2-WVnt98M0]]></OpenId><AppId><![CDATA[wx7179cc98fb47eff5]]></AppId><TimeStamp>1409197790</TimeStamp><MsgType><![CDATA[request]]></MsgType><FeedBackId>10280828770999802559</FeedBackId><TransId><![CDATA[1219895801201408283186787487]]></TransId><Reason><![CDATA[Test]]></Reason><Solution><![CDATA[Test]]></Solution><ExtInfo><![CDATA[Test 13333333333]]></ExtInfo><AppSignature><![CDATA[34ec62fff442f91786b6de8baef9c737bb4cbe9c]]></AppSignature><SignMethod><![CDATA[sha1]]></SignMethod></xml>";
		return xml;
	}
	public static String pkgReq()
	{
		return "<xml><OpenId><![CDATA[oTNesjrqNt4ZIZ8zRe2-WVnt98M0]]></OpenId><AppId><![CDATA[wx7179cc98fb47eff5]]></AppId><IsSubscribe>1</IsSubscribe><ProductId><![CDATA[103518878431371129]]></ProductId><TimeStamp>1407983545</TimeStamp><NonceStr><![CDATA[HemIBJpf35e3zk3A]]></NonceStr><AppSignature><![CDATA[88341548a7d35b6889fd708cdc54e98471e2dfa3]]></AppSignature><SignMethod><![CDATA[sha1]]></SignMethod></xml>";
		
//		return "<xml>"
//				+"<OpenId><![CDATA[oUpF8uN95-Ptaags6E_roPHg7AG0]]></OpenId>"
//				+"<AppId><![CDATA[wx2421b1c4370ec43b]]></AppId>"
//				+"<IsSubscribe>1</IsSubscribe>"
//				+"<ProductId><![CDATA[ph31929767347956]]></ProductId>"
//				+"<TimeStamp>1400126932</TimeStamp>"
//				+"<NonceStr><![CDATA[CChC5JQLhRigmGJP]]></NonceStr>"
//				+"<AppSignature><![CDATA[13b78571a8262e7cd9fdd90caf7742e1c4023771]]></AppSignature>"
//				+"<SignMethod><![CDATA[sha1]]></SignMethod>"
//				+"</xml>";
	}
	
	public static String retNotify()
	{
		return "<xml>"
				+"<OpenId><![CDATA[111222]]></OpenId>"
				+"<AppId><![CDATA[wwwwb4f85f3a797777]]></AppId>"
				+"<IsSubscribe>1</IsSubscribe>"
				+"<TimeStamp> 1369743511</TimeStamp>"
				+"<NonceStr><![CDATA[jALldRTHAFd5Tgs5]]></NonceStr>"
				+"<AppSignature><![CDATA[bafe07f060f22dcda0bfdb4b5ff756f973aecffa]]>"
				+"</AppSignature>"
				+"<SignMethod><![CDATA[sha1]]></SignMethod>"
				+"</xml>";
	}
	
	public static String rtWarn()throws Exception
	{
		String t= TenpayUtil.getTimeStamp();
		String sign= getPaySign(t);
		return "<xml>"
				+"<AppId><![CDATA[wx7179cc98fb47eff5]]></AppId>"
				+"<ErrorType>1001</ErrorType>"
				+"<Description><![CDATA[test]]></Description>"
				+"<AlarmContent><![CDATA[test]]></AlarmContent>"
				+"<TimeStamp>"+t+"</TimeStamp>"
				+"<AppSignature><![CDATA["+sign+"]]></AppSignature>"
				+"<SignMethod><![CDATA[sha1]]></SignMethod>"
				+"</xml>";
	}
	
	public static String getPaySign(String t) throws Exception
	{
		RequestHandler paySignReqHandler = new RequestHandler();
		String appId="wx7179cc98fb47eff5";
		String errortype="1001";
		String description="test";
		String alarmcontent="test";
		String  timestamp=t;
		 
		paySignReqHandler.setParameter("appkey", "OlTWpFgG6TJ4cO9luoXq9QpatmoPPzqeaqgOxGVSMVvG8XQR8zPOnpn5sLyPAvGIV8AHrWe17MU3qBGOcEnbO1TZUNm3vjmPR5X5BDaqiSFahAklK7y0F8gGoIWbUDmu");//公众号appid对应的密钥
		paySignReqHandler.setParameter("appid", appId);//公众号appid
		paySignReqHandler.setParameter("errortype", errortype);//
		paySignReqHandler.setParameter("description", description);
		paySignReqHandler.setParameter("alarmcontent", alarmcontent);
		paySignReqHandler.setParameter("timestamp", timestamp);
	 
		String sign= paySignReqHandler.getPaySign();
		System.out.println(paySignReqHandler.getDebugInfo());
		return sign;
	}
	public static void sendXml(String xmlCt){
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
			httpUrlConnection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
		   httpUrlConnection.setRequestProperty("Accept-Charset	", "UTF-8");
		   httpUrlConnection.setRequestProperty("Charset	", "UTF-8");
			httpUrlConnection.setRequestMethod("POST");
			//设置连接、读取超时
			httpUrlConnection.setConnectTimeout(30000);
			httpUrlConnection.setReadTimeout(30000);
			httpUrlConnection.connect();
			
			
		        
			//使用缓冲流将xml字符串发送给服务器
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpUrlConnection.getOutputStream(),"UTF-8"));
			
//			 StringBuffer params = new StringBuffer();
//		        // 表单参数与get形式一样
//		        params.append("out_trade_no").append("=").append("1111")
//		               .append("&")
//		             .append("out_trade_no").append("=").append("1111")
//		             .append("transaction_id").append("=").append("1111")
//		              .append("trade_state").append("=").append("0")
//		             ;
//		      //  byte[] bypes = params.toString().getBytes();
//		        writer.write(params.toString());// 输入参数
			
			
		//	writer.write(URLEncoder.encode(xmlCt,"UTF-8"));
		//	System.out.println(xmlCt);
			writer.write( xmlCt);
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
