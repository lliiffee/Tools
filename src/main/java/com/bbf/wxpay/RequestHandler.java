package com.bbf.wxpay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

 

/**
 * �������� ��������̳д��࣬��дcreateSign�������ɡ�
 * 
 * @author miklchen
 * 
 */
public class RequestHandler {

	/** ���url��ַ */
	private String gateUrl;
	/** ��Կ */
	private String key;
	/** ����Ĳ��� */
	private SortedMap parameters;
	/** debug��Ϣ */
	private String debugInfo;
 
	 
	
	public RequestHandler() {
	//	this.request = request;
	//	this.response = response;
		this.gateUrl = "https://gw.tenpay.com/gateway/pay.htm";
		this.key = "";
		this.parameters = new TreeMap();
		this.debugInfo = "";
	}

	/**
	 * ��ʼ������
	 */
	public void init() {
		// nothing to do
	}

	 
	
  

	public String getPaySign() throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
//		String enc = TenpayUtil.getCharacterEncoding(
//				this.getHttpServletRequest(), this.getHttpServletResponse());
		Set es = this.getAllParameters().entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!"spbill_create_ip".equals(k)) {
				sb.append(k.toLowerCase() + "=" + v + "&");
			} else {
				sb.append(k.toLowerCase() + "=" + v.replace(".", "%2E") + "&");
			}
		}
		// ȥ�����һ��&
		String reqPars = sb.substring(0, sb.lastIndexOf("&"));
		String sha1sign = Sha1Util.getSha1(reqPars);
		this.setDebugInfo("gen sha1str:" + reqPars + " => sha1sign:" + sha1sign);
		return sha1sign;
	}

	/**
	 * ��ȡ��ڵ�ַ,�������ֵ
	 */
	public String getGateUrl() {
		return gateUrl;
	}

	/**
	 * ������ڵ�ַ,�������ֵ
	 */
	public void setGateUrl(String gateUrl) {
		this.gateUrl = gateUrl;
	}

	/**
	 * ��ȡ��Կ
	 */
	public String getKey() {
		return key;
	}

	/**
	 * ������Կ
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @param parameter �������
	 * @return String
	 */
	public String getParameter(String parameter) {
		String s = (String) this.parameters.get(parameter);
		return (null == s) ? "" : s;
	}

	/**
	 * ���ò���ֵ
	 * @param parameter �������
	 * @param parameterValue ����ֵ
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if (null != parameterValue) {
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
	 * ��ȡdebug��Ϣ
	 */
	public String getDebugInfo() {
		return debugInfo;
	}

	/**
	 * ����debug��Ϣ
	 */
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}

	 

}
