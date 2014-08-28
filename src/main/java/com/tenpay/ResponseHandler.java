package com.tenpay;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;

import com.tenpay.util.HttpClientUtil;
import com.tenpay.util.MD5Util;
import com.tenpay.util.Sha1Util;
import com.tenpay.util.TenpayUtil;
import com.tenpay.util.XMLUtil;

/**
 * 应答处理类
 * 应答处理类继承此类，重写isTenpaySign方法即可。
 * @author miklchen
 *
 */
public class ResponseHandler { 
	
	/** 密钥 */
	private String key;
	
	/** 微信密钥 */
	private String appkey;
	
	/** 应答的参数 */
	private SortedMap parameters; 
	
	/** 微信应答参数 */
	private SortedMap wxparameters;
	
	/** 应答原始内容 */
	private String content;
	
	/** debug信息 */
	private String debugInfo;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String uriEncoding;
	
	/**
	 * 构造函数
	 * 
	 * @param request
	 * @param response
	 */
	public ResponseHandler(HttpServletRequest request,
			HttpServletResponse response)  {
		this.request = request;
		this.response = response;
		
		this.key = "";
		this.parameters = new TreeMap();
		this.wxparameters = new TreeMap();
		this.debugInfo = "";
		
		this.uriEncoding = "";

	}
	
	/**
	*获取密钥
	*/
	public String getKey() {
		return key;
	}

	/**
	*设置密钥
	*/
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取参数值
	 * @param parameter 参数名称
	 * @return String 
	 */
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter); 
		return (null == s) ? "" : s;
	}
	
	/**
	 * 设置参数值
	 * @param parameter 参数名称
	 * @param parameterValue 参数值
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}
	
	/**
	 * 返回所有的参数
	 * @return SortedMap
	 */
	public SortedMap getAllParameters() {
		return this.parameters;
	}
	
	/**
	 * 是否财付通签名,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * @return boolean
	 */
	public boolean isTenpaySign() {
		/** GET 接收财付通的数据*/
		Map m = this.request.getParameterMap();
		Iterator itt = m.keySet().iterator();
		while (itt.hasNext()) {
			String k = (String) itt.next();
			String v = ((String[]) m.get(k))[0];			
			this.setParameter(k, v);
		}
		
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		
		sb.append("key=" + this.getKey());
		//算出摘要
		String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
		String sign = MD5Util.MD5Encode(sb.toString(), enc).toUpperCase();
		String tenpaySign = this.getParameter("sign").toUpperCase();
		//debug信息
		this.setDebugInfo(sb.toString() + " => sign:" + sign +
				" tenpaySign:" + tenpaySign);
		
		return tenpaySign.equals(sign);
	}
	
	/**
	 * 是否财付通签名,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 通知查询验证返回参数验证是否财付通签名
	 * @return boolean
	 */
	public boolean _isTenpaySign() {
		
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		
		sb.append("key=" + this.getKey());
		System.out.println(sb);
		//算出摘要
		String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
		String sign = MD5Util.MD5Encode(sb.toString(), enc).toUpperCase();
		System.out.println(sign);
		String tenpaySign = this.getParameter("sign").toUpperCase();
		System.out.println(tenpaySign);
		//debug信息
		this.setDebugInfo(sb.toString() + " => sign:" + sign +
				" tenpaySign:" + tenpaySign);
		
		return tenpaySign.equals(sign);
	}
	
	/**
	 * 是否微信签名,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * @return boolean
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public boolean isWeixinSign() throws IOException, JDOMException{
		//获取应答内容
		String xmlContent = HttpClientUtil.InputStreamTOString(this.request.getInputStream(),TenpayUtil.getCharacterEncoding(request, response)); 
		//解析xml,得到map
		this.doParse(xmlContent);
		
		StringBuffer sb = new StringBuffer();
		Set es = this.wxparameters.entrySet();
		this.setWxparameter("appkey", this.getAppkey());
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(!"appsignature".equals(k) && !"signmethod".equals(k)){
				sb.append(k.toLowerCase() + "=" + v + "&");
			}
		}
		
		//去掉最后一个&
		String signPars = sb.substring(0, sb.lastIndexOf("&"));
		String sign = Sha1Util.getSha1(signPars);
		String weixinSign = this.getWxparameter("appsignature").toLowerCase();
		return sign.equals(weixinSign);
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
			this.setWxparameter(k.toLowerCase(), v);
		}
		
	}
	
	/**
	 * 解析XML内容到  parameters
	 */
	protected void _doParse(String xmlContent) throws JDOMException, IOException {
		//解析xml,得到map
		Map m = XMLUtil.doXMLParse(xmlContent);
		
		//设置参数
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			String k = (String) it.next();
			String v = (String) m.get(k);
			this.setParameter(k.toLowerCase(), v);
		}
		
	}
	
	public void setContent(String content) throws Exception {
		this.content = content;
		
		this._doParse(content);
	}
	
	/**
	 * 返回处理结果给财付通服务器。
	 * @param msg: Success or fail。
	 * @throws IOException 
	 */
	public void sendToCFT(String msg) throws IOException {
		String strHtml = msg;
		PrintWriter out = this.getHttpServletResponse().getWriter();
		out.println(strHtml);
		out.flush();
		out.close();

	}
	
	/**
	 * 获取uri编码
	 * @return String
	 */
	public String getUriEncoding() {
		return uriEncoding;
	}

	/**
	 * 设置uri编码
	 * @param uriEncoding
	 * @throws UnsupportedEncodingException
	 */
	public void setUriEncoding(String uriEncoding)
			throws UnsupportedEncodingException {
		if (!"".equals(uriEncoding.trim())) {
			this.uriEncoding = uriEncoding;

			// 编码转换
			String enc = TenpayUtil.getCharacterEncoding(request, response);
			Iterator it = this.parameters.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				String v = this.getParameter(k);
				v = new String(v.getBytes(uriEncoding.trim()), enc);
				this.setParameter(k, v);
			}
		}
	}

	/**
	*获取debug信息
	*/
	public String getDebugInfo() {
		return debugInfo;
	}
	
	/**
	*设置debug信息
	*/
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}
	
	protected HttpServletRequest getHttpServletRequest() {
		return this.request;
	}
	
	protected HttpServletResponse getHttpServletResponse() {
		return this.response;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
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
