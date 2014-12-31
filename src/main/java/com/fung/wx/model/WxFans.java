package com.fung.wx.model;



@SuppressWarnings({"serial"})
public class WxFans {

	private int id;
	private String openid;//	用户的唯一标识
	private String nickname;//	用户昵称
	private int gender;//	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	private String province;//	用户个人资料填写的省份
	private String city;//	普通用户个人资料填写的城市
	private String country;//	国家，如中国为CN
	private String headimgurl;//	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
	private String privilege;//	用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	private String unionid	;//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
	
	private int subscribe; //用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
	private long subscribeTime; //用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}
	public long getSubscribeTime() {
		return subscribeTime;
	}
	public void setSubscribeTime(long subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	 
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	 
}
