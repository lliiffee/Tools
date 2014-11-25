package com.fung.wx.service;

import com.alibaba.fastjson.JSONObject;
import com.fung.wx.util.TokenUtil;
import com.fung.wx.util.WeiXinUtil;

public class WxCouponService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	private static JSONObject couponAction(String postUrl,String json)
	{
		
		String appid="wxbf261f64e52a3ceb";
		String secret="6ee4bd7cb3c20c5c53a1015f3fc37edf";
		 
		 
		TokenUtil tk=new TokenUtil();
		
		return WeiXinUtil.httpsRequest(postUrl.replace("ACCESS_TOKEN",tk.getToken(appid,secret) ) ,"POST", json);
	}
	 
	
	//1. 创建卡券
	private static String createCoupon()
	{
		String card_id="";
		String createCouponUrl="https://api.weixin.qq.com/card/create?access_token=ACCESS_TOKEN";
		String postData="";
		
		JSONObject retObj=couponAction(createCouponUrl,postData);
		return card_id;
		
	}
	//2批量导入门店信息   https://api.weixin.qq.com/card/location/batchadd?access_token=TOKEN
	//3拉取门店列表    //https://api.weixin.qq.com/card/location/batchget?access_token=TOKEN
	//4  获取颜色列表接口  //https://api.weixin.qq.com/card/getcolors?access_token=TOKEN
	
	//5 生成卡券二维码 https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN 
	     //-->获取二维码ticket 后，开发者可用ticket 换取二维码图片。换取指引参考：
	
	//6.添加到卡包（openCardDetail）JSAPI
	
	//code 解码接口  //https://api.weixin.qq.com/card/code/decrypt?access_token=TOKEN
	//--->.商家获取choos_card_info 后，将card_id 和encrypt_code 字段通过解码接口，获取真实code。
	//--->.卡券内跳转外链的签名中会对code 进行加密处理，通过调用解码接口获取真实code。
	
	//消耗code  //https://api.weixin.qq.com/card/code/consume?access_token=TOKEN
	
	//拉起卡券列表（chooseCard）JSAPI ?/  调用卡券JS API的页面url里需要带有参数“wechat_card_js=1”。如 http://www.qq.com/123/456?a=2&wechat_card_js=1&b=3
	
	//删除卡券接口允许商户删除任意一类卡券。删除卡券后，该卡券对应已生成的领取用二维码、添加
	//到卡包JS API 均会失效。
	//注意：如用户在商家删除卡券前已领取一张或多张该卡券依旧有效。即删除卡券不能删除已被用户
	//领取，保存在微信客户端中的卡券。
	
	//查询code   
	
	//批量查询卡列表
	
	//事件推送
	
	//卡券通过审核、卡券被用户领取、卡券被用户删除均会触发事件推送，该事件将发送至开发者填写的URL（登陆公众平台进入开发者中心设置）。
	
	
	//查询卡券详情
	
}
