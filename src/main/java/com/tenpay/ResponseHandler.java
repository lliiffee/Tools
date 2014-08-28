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
 * Ӧ������
 * Ӧ������̳д��࣬��дisTenpaySign�������ɡ�
 * @author miklchen
 *
 */
public class ResponseHandler { 
	
	/** ��Կ */
	private String key;
	
	/** ΢����Կ */
	private String appkey;
	
	/** Ӧ��Ĳ��� */
	private SortedMap parameters; 
	
	/** ΢��Ӧ����� */
	private SortedMap wxparameters;
	
	/** Ӧ��ԭʼ���� */
	private String content;
	
	/** debug��Ϣ */
	private String debugInfo;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String uriEncoding;
	
	/**
	 * ���캯��
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
	*��ȡ��Կ
	*/
	public String getKey() {
		return key;
	}

	/**
	*������Կ
	*/
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * ��ȡ����ֵ
	 * @param parameter ��������
	 * @return String 
	 */
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter); 
		return (null == s) ? "" : s;
	}
	
	/**
	 * ���ò���ֵ
	 * @param parameter ��������
	 * @param parameterValue ����ֵ
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}
	
	/**
	 * �������еĲ���
	 * @return SortedMap
	 */
	public SortedMap getAllParameters() {
		return this.parameters;
	}
	
	/**
	 * �Ƿ�Ƹ�ͨǩ��,������:����������a-z����,������ֵ�Ĳ������μ�ǩ����
	 * @return boolean
	 */
	public boolean isTenpaySign() {
		/** GET ���ղƸ�ͨ������*/
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
		//���ժҪ
		String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
		String sign = MD5Util.MD5Encode(sb.toString(), enc).toUpperCase();
		String tenpaySign = this.getParameter("sign").toUpperCase();
		//debug��Ϣ
		this.setDebugInfo(sb.toString() + " => sign:" + sign +
				" tenpaySign:" + tenpaySign);
		
		return tenpaySign.equals(sign);
	}
	
	/**
	 * �Ƿ�Ƹ�ͨǩ��,������:����������a-z����,������ֵ�Ĳ������μ�ǩ���� ֪ͨ��ѯ��֤���ز�����֤�Ƿ�Ƹ�ͨǩ��
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
		//���ժҪ
		String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
		String sign = MD5Util.MD5Encode(sb.toString(), enc).toUpperCase();
		System.out.println(sign);
		String tenpaySign = this.getParameter("sign").toUpperCase();
		System.out.println(tenpaySign);
		//debug��Ϣ
		this.setDebugInfo(sb.toString() + " => sign:" + sign +
				" tenpaySign:" + tenpaySign);
		
		return tenpaySign.equals(sign);
	}
	
	/**
	 * �Ƿ�΢��ǩ��,������:����������a-z����,������ֵ�Ĳ������μ�ǩ����
	 * @return boolean
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public boolean isWeixinSign() throws IOException, JDOMException{
		//��ȡӦ������
		String xmlContent = HttpClientUtil.InputStreamTOString(this.request.getInputStream(),TenpayUtil.getCharacterEncoding(request, response)); 
		//����xml,�õ�map
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
		
		//ȥ�����һ��&
		String signPars = sb.substring(0, sb.lastIndexOf("&"));
		String sign = Sha1Util.getSha1(signPars);
		String weixinSign = this.getWxparameter("appsignature").toLowerCase();
		return sign.equals(weixinSign);
	}
	
	/**
	 * ����XML���ݵ�  wxparameters
	 */
	protected void doParse(String xmlContent) throws JDOMException, IOException {
		//����xml,�õ�map
		Map m = XMLUtil.doXMLParse(xmlContent);
		
		//���ò���
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			String k = (String) it.next();
			String v = (String) m.get(k);
			this.setWxparameter(k.toLowerCase(), v);
		}
		
	}
	
	/**
	 * ����XML���ݵ�  parameters
	 */
	protected void _doParse(String xmlContent) throws JDOMException, IOException {
		//����xml,�õ�map
		Map m = XMLUtil.doXMLParse(xmlContent);
		
		//���ò���
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
	 * ���ش��������Ƹ�ͨ��������
	 * @param msg: Success or fail��
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
	 * ��ȡuri����
	 * @return String
	 */
	public String getUriEncoding() {
		return uriEncoding;
	}

	/**
	 * ����uri����
	 * @param uriEncoding
	 * @throws UnsupportedEncodingException
	 */
	public void setUriEncoding(String uriEncoding)
			throws UnsupportedEncodingException {
		if (!"".equals(uriEncoding.trim())) {
			this.uriEncoding = uriEncoding;

			// ����ת��
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
	*��ȡdebug��Ϣ
	*/
	public String getDebugInfo() {
		return debugInfo;
	}
	
	/**
	*����debug��Ϣ
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
	 * ��ȡ΢�Ų���ֵ
	 * @param parameter ��������
	 * @return String 
	 */
	public String getWxparameter(String wxparameter) {
		String s = (String)this.wxparameters.get(wxparameter); 
		return (null == s) ? "" : s;
	}
	
	/**
	 * ����΢�Ų���ֵ
	 * @param parameter ��������
	 * @param parameterValue ����ֵ
	 */
	public void setWxparameter(String wxparameter, String wxparameterValue) {
		String v = "";
		if(null != wxparameterValue) {
			v = wxparameterValue.trim();
		}
		this.wxparameters.put(wxparameter, v);
	}
	
	/**
	 * ����΢�����еĲ���
	 * @return SortedMap
	 */
	public SortedMap getAllWxparameters() {
		return this.wxparameters;
	}
	
}
