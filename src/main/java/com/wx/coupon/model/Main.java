package com.wx.coupon.model;
import java.util.ArrayList;
import java.util.Calendar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jackylian
 */
public class Main {
    public static void main(String[] args) {
        WxCardGroupon card = new WxCardGroupon();
        WxCardBaseInfo baseInfo = card.getBaseInfo();
        baseInfo.setLogoUrl("123");  //卡券的商户logo，尺寸为300*300。
        baseInfo.setDateInfoTimeRange(Calendar.getInstance().getTime(), Calendar.getInstance().getTime()); //使用日期，有效期的信息
         
        baseInfo.setBrandName("brandname"); //商户名字,字数上限为12 个汉 字。（填写直接提供服务的商户 名， 第三方商户名填写在 source 字段）
        baseInfo.setTitle("title");//券名，字数上限为9 个汉字。(建 议涵盖卡券属性、服务及金额)
        baseInfo.setSubTitle("subTitle");//券名的副标题，字数上限为18个汉字。
        baseInfo.setBindOpenid(false);//是否指定用户领取，填写true 或false。不填代表默认为否。
        baseInfo.setCanGiveFriend(false);//卡券是否可转赠，填写true 或 false,true 代表可转赠。默认可 转赠。
        baseInfo.setCanShare(true);//领取卡券原生页面是否可分享， 填写true 或false，true 代表 可分享。默认可分享。
        baseInfo.setCodeType(WxCardBaseInfo.CODE_TYPE_QRCODE);  //code 码展示类型
        baseInfo.setColor("Color010"); //券颜色。按色彩规范标注填写 Color010-Color100
        baseInfo.setDescription("desc");//使用说明。长文本描述，可以分行，上限为1000 个汉字。
        baseInfo.setGetLimit(3);//每人最大领取次数，不填写默认 等于quantity。
        baseInfo.setUseCustomCode(false);//是否自定义code 码。填写true 或false，不填代表默认为 false。（该权限申请及说明详   见Q&A)
        baseInfo.setNotice("notice");//使用提醒，字数上限为9 个汉 字。（一句话描述，展示在首页，    示例：请出示二维码核销卡券）
        baseInfo.setServicePhone("phone");//客服电话
        baseInfo.addLocationIdList(123123); //门店位置ID。商户需在mp 平   台上录入门店信息或调用批量  导入门店信息接口获取门店位    置ID。
        baseInfo.addLocationIdList(5345);
        baseInfo.setUseLimit(5);
        baseInfo.setQuantity(10000000);//上架的数量。(不支持填写0 或      无限大)
        //custom_url //商户自定义url 地址，支持卡券 页内跳转,跳转页面内容需与自      定义cell 名称保持一致
        //source //第三方来源名，例如同程旅游、 格瓦拉。
        //url_name_type 商户自定义cell 名称
        System.out.println(baseInfo.toJsonString());
        baseInfo.setLogoUrl("435345");
        ArrayList<Integer> locationIdList = new ArrayList<Integer>();
        locationIdList.add(809809);
        locationIdList.add(423532);
        card.setDealDetail("团购详情啊啊啊啊啊！！！");
        
        System.out.println(locationIdList.getClass().isArray());
        baseInfo.setLocationIdList(locationIdList);
        System.out.println(card.toJsonString());
    } 
}


/* #####sample data####
"groupon": {
"base_info": {
"logo_url":
"http:\/\/www.supadmin.cn\/uploads\/allimg\/120216\/1_120216214725_1.jpg",
"brand_name": "海底捞",
"code_type":"CODE_TYPE_TEXT",
"title": "132 元双人火锅套餐",
"sub_title": "",
"color": "Color010",
"notice": "使用时向服务员出示此券",
"service_phone": "020-88888888",
"description": "不可与其他优惠同享\n 如需团购券发票，请在消费时向商户提出\n 店内均可
使用，仅限堂食\n 餐前不可打包，餐后未吃完，可打包\n 本团购券不限人数，建议2 人使用，超过建议人
数须另收酱料费5 元/位\n 本单谢绝自带酒水饮料",
"date_info": {
"type": 1,
"begin_timestamp": 1397577600,
"end_timestamp": 1399910400
},
"sku": {
"quantity": 50000000
},
"use_limit": 1,
"get_limit": 3,
"use_custom_code": false,
"bind_openid": false,
"can_share": true,
"can_give_friend"：true,
"location_id_list" : [123, 12321, 345345]，
"url_name_type": "URL_NAME_TYPE_RESERVATION",
"custom_url": "http://www.qq.com",
"source": "大众点评"
},
"deal_detail": "以下锅底2 选1（有菌王锅、麻辣锅、大骨锅、番茄锅、清补凉锅、酸菜鱼锅可
选）：\n 大锅1 份12 元\n 小锅2 份16 元\n 以下菜品2 选1\n 特级肥牛1 份30 元\n 洞庭鮰鱼卷1 份
20 元\n 其他\n 鲜菇猪肉滑1 份18 元\n 金针菇1 份16 元\n 黑木耳1 份9 元\n 娃娃菜1 份8 元\n 冬
瓜1 份6 元\n 火锅面2 个6 元\n 欢乐畅饮2 位12 元\n 自助酱料2 位10 元"}
}}
*/

/* ########返回##########
 * {
"errcode":0,
"errmsg":"ok",
"card_id":"p1Pj9jr90_SQRaVqYI239Ka1erkI"
}
 */
