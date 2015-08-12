package com.fung.wx.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.alibaba.fastjson.JSONObject;
import com.fung.wx.util.WeiXinUtil;
import com.wx.coupon.model.WxCardBaseInfo;
import com.wx.coupon.model.WxCardCash;

public class WxCouponService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			System.out.println(createCoupon());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 //	getList();
	//	setWiteList();
	//	createTicket();
//		try {
//			//{"errcode":0,"errmsg":"ok","expire_seconds":31536000,"ticket":"gQE_8ToAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1NYV0pxQkxsOXl1TzJabnVkVjZJAAIEY46fVQMEgDPhAQ==","url":"http://weixin.qq.com/q/SXWJqBLl9yuO2ZnudV6I"}
//
//			System.out.println(URLEncoder.encode("gQE_8ToAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1NYV0pxQkxsOXl1TzJabnVkVjZJAAIEY46fVQMEgDPhAQ==","utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	static String token="rwxHupJ1CGNjigHq8ci1hFSw46tAGnNa4ilTl0QSd43v6Zy07Zk-oCECsXjlxq0wrmi9XBjfYkIYepEChA38NBahk8oV-75jiOafvzctERo";
	
	private static void  getImg(){
		
	}
	private static void setWiteList(){
		 
		String postUrl="https://api.weixin.qq.com/card/testwhitelist/set?access_token=ACCESS_TOKEN";
		String json="{\"openid\": [\"oTNesjrqNt4ZIZ8zRe2-WVnt98M0\"]}";
		System.out.println( WeiXinUtil.httpsRequest(postUrl.replace("ACCESS_TOKEN",token ) ,"POST", json) );
		
	}
	
	private static void  getList(){
		 
		String postUrl="https://api.weixin.qq.com/card/get?access_token=ACCESS_TOKEN";

		String json="{\"card_id\":\"pTNesjlZaADjcQp_7M1aUR3Rm8yI\"}";
	//	String json="{\"card_id\":\"pTNesjlMYuIIn_Oa5nUaX_WSUW48\"}";
		 System.out.println( WeiXinUtil.httpsRequest(postUrl.replace("ACCESS_TOKEN",token ) ,"POST", json) );
		
	}
	
	
	//https://api.weixin.qq.com/card/qrcode/create?access_token=TOKEN
	

	private static void  createTicket(){
		 
		String postUrl="https://api.weixin.qq.com/card/qrcode/create?access_token=ACCESS_TOKEN";

		String json=" {"
				+"\"action_name\": \"QR_CARD\", "
				+"\"action_info\": {"
				+"\"card\": {"
				+"\"card_id\": \"pTNesjlZaADjcQp_7M1aUR3Rm8yI\", "
				+"\"code\": \"198374613512\","
//				+"\"openid\": \"o69Ras_or-q5yt03qtFNWUhzCWHI\","
//				+"\"expire_seconds\": \"1800\","
				+"\"is_unique_code\": false ,"
				+"\"outer_id\" : 1"
				+"  }"
				+" }"
				+"}";
	//	String json="{\"card_id\":\"pTNesjlMYuIIn_Oa5nUaX_WSUW48\"}";
		
		
		 System.out.println( WeiXinUtil.httpsRequest(postUrl.replace("ACCESS_TOKEN",token ) ,"POST", json) );
		
	}
	
	private static JSONObject couponAction(String postUrl,String json)
	{
		return WeiXinUtil.httpsRequest(postUrl.replace("ACCESS_TOKEN",token ) ,"POST", json);
	}
	 
	
	//1. 创建卡券
	private static String createCoupon() throws ParseException
	{
		String card_id="";
		String createCouponUrl="https://api.weixin.qq.com/card/create?access_token=ACCESS_TOKEN";
		String postData="";
		
		
		WxCardCash  card = new WxCardCash();
		 card.getBaseInfo();
		 
		 WxCardBaseInfo baseInfo = card.getBaseInfo();
		 baseInfo.setLogoUrl("https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VLscgaFvXIoOZs468lhjeeJqWrqJSOjjEKJEK5vyjXDgZpPsSTuaMPnLe1CiaeqQbyg59LVqdTM3dA/0");  //卡券的商户logo，尺寸为300*300。
		 baseInfo.setColor("Color100"); //券颜色。按色彩规范标注填写 Color010-Color100
		 

		 String desc="请在红包有效期内，登录八百方商城，消费满300元立减20元；赶快转发这个好消息给你的亲朋好友吧；每人限领取一张；购买后如果退换货，红包不作为退换货的金额；本次活动最终解释权归八百方。";
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 baseInfo.setDateInfoTimeRange(Calendar.getInstance().getTime(), df.parse("2015-08-16 23:59:59")); //使用日期，有效期的信息
		 baseInfo.setTitle("20元代金券");//券名，字数上限为9 个汉字。(建 议涵盖卡券属性、服务及金额) string（27）
		// baseInfo.setSubTitle("满388减20");//券名的副标题，字数上限为18个汉字。
		 
		    baseInfo.setBindOpenid(false);//是否指定用户领取，填写true 或false。不填代表默认为否。
	        baseInfo.setCanGiveFriend(true);//卡券是否可转赠，填写true 或 false,true 代表可转赠。默认可 转赠。
	        baseInfo.setCanShare(true);//领取卡券原生页面是否可分享， 填写true 或false，true 代表 可分享。默认可分享。
	       // baseInfo.setCodeType(WxCardBaseInfo.CODE_TYPE_QRCODE);  //code 码展示类型
	       baseInfo.setBrandName("八百方"); //商户名字,字数上限为12个汉字。
	        baseInfo.setDescription(desc);//使用说明。长文本描述，可以分行，上限为1000 个汉字。
	       
	        baseInfo.setUseCustomCode(false);//是否自定义code 码。填写自定义true  ，不填代表默认为 false。（该权限申请及说明详   见Q&A)
	        baseInfo.setNotice("按下方【点击使用】");//使用提醒，字数上限为9 个汉 字。（一句话描述，展示在首页，    示例：请出示二维码核销卡券）
	        baseInfo.setServicePhone("400-885-5171");//客服电话
	       // baseInfo.addLocationIdList(123123); //门店位置ID。商户需在mp 平   台上录入门店信息或调用批量  导入门店信息接口获取门店位    置ID。
	       // baseInfo.addLocationIdList(5345);
	        baseInfo.setUseLimit(1);
	        baseInfo.setQuantity(100000);//上架的数量。(不支持填写0 或      无限大)
	        
	        baseInfo.setGetLimit(1);//每人最大领取次数，不填写默认 等于quantity。
	         baseInfo.setCustomUrlName("点击使用");
	         baseInfo.setCustomUrl("www.800pharm.com/shop/couponRecord/consumeWxKabao.html");
	         baseInfo.setCustomUrlSubTitle("八百方商城");
	         baseInfo.setCodeType("CODE_TYPE_TEXT"); //CODE_TYPE_TEXT
		 card.setReduceCost(2000);
		 card.setLeastCost(30000);
		 postData=card.toJsonString();
		 System.out.println(postData);
		JSONObject retObj=couponAction(createCouponUrl,postData);
		System.out.println(retObj);
		return card_id;
		
	}
	
	
	
	//1. 创建卡券
		private static String createCoupon_10() throws ParseException
		{
			String card_id="";
			String createCouponUrl="https://api.weixin.qq.com/card/create?access_token=ACCESS_TOKEN";
			String postData="";
			
			
			WxCardCash  card = new WxCardCash();
			 card.getBaseInfo();
			 
			 WxCardBaseInfo baseInfo = card.getBaseInfo();
			 baseInfo.setLogoUrl("https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VLscgaFvXIoOZs468lhjeeJqWrqJSOjjEKJEK5vyjXDgZpPsSTuaMPnLe1CiaeqQbyg59LVqdTM3dA/0");  //卡券的商户logo，尺寸为300*300。
			 baseInfo.setColor("Color100"); //券颜色。按色彩规范标注填写 Color010-Color100
			 

			 String desc="请在红包有效期内，登录八百方商城进行兑换；赶快转发这个好消息给你的亲朋好友吧；每人限领取一张；购买后如果退换货，红包不作为退换货的金额；红包不能充当邮费支付；本次活动最终解释权归八百方。";
			 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 baseInfo.setDateInfoTimeRange(Calendar.getInstance().getTime(), df.parse("2015-08-16 23:59:59")); //使用日期，有效期的信息
			 baseInfo.setTitle("10元现金券");//券名，字数上限为9 个汉字。(建 议涵盖卡券属性、服务及金额) string（27）
			// baseInfo.setSubTitle("满388减20");//券名的副标题，字数上限为18个汉字。
			 
			    baseInfo.setBindOpenid(false);//是否指定用户领取，填写true 或false。不填代表默认为否。
		        baseInfo.setCanGiveFriend(true);//卡券是否可转赠，填写true 或 false,true 代表可转赠。默认可 转赠。
		        baseInfo.setCanShare(true);//领取卡券原生页面是否可分享， 填写true 或false，true 代表 可分享。默认可分享。
		       // baseInfo.setCodeType(WxCardBaseInfo.CODE_TYPE_QRCODE);  //code 码展示类型
		       baseInfo.setBrandName("八百方"); //商户名字,字数上限为12个汉字。
		        baseInfo.setDescription(desc);//使用说明。长文本描述，可以分行，上限为1000 个汉字。
		       
		        baseInfo.setUseCustomCode(false);//是否自定义code 码。填写自定义true  ，不填代表默认为 false。（该权限申请及说明详   见Q&A)
		        baseInfo.setNotice("按下方【点击使用】");//使用提醒，字数上限为9 个汉 字。（一句话描述，展示在首页，    示例：请出示二维码核销卡券）
		        baseInfo.setServicePhone("400-885-5171");//客服电话
		       // baseInfo.addLocationIdList(123123); //门店位置ID。商户需在mp 平   台上录入门店信息或调用批量  导入门店信息接口获取门店位    置ID。
		       // baseInfo.addLocationIdList(5345);
		        baseInfo.setUseLimit(1);
		        baseInfo.setQuantity(100000);//上架的数量。(不支持填写0 或      无限大)
		        
		        baseInfo.setGetLimit(1);//每人最大领取次数，不填写默认 等于quantity。
		         baseInfo.setCustomUrlName("点击使用");
		         baseInfo.setCustomUrl("www.800pharm.com/shop/couponRecord/consumeWxKabao.html");
		         baseInfo.setCustomUrlSubTitle("八百方商城");
		         baseInfo.setCodeType("CODE_TYPE_TEXT"); //CODE_TYPE_TEXT
			 card.setReduceCost(1000);
			// card.setLeastCost(38800);
			 postData=card.toJsonString();
			 System.out.println(postData);
			JSONObject retObj=couponAction(createCouponUrl,postData);
			System.out.println(retObj);
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
