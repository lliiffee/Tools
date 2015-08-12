package com.fung.wx.service;

import com.fung.wx.util.WeiXinUtil;

public class WxSwingService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//applyDevice();
		//getPoiList();
		submitPage();
		//getImgList();
	}

	
/*
https://api.weixin.qq.com/shakearound/device/applyid?access_token=ACCESS_TOKEN
POST数据格式：json
POST数据例子：
{
   "quantity":3,	
   "apply_reason":"测试",	
   "comment":"测试专用",
   "poi_id":1001	
}
*/
	/*
	private static void  getPoiList(){
		String postUrl="https://api.weixin.qq.com/cgi-bin/poi/getpoilist?access_token=ACCESS_TOKEN";

		String json="{\"begin\":0,\"limit\":10}";
		 System.out.println( WeiXinUtil.httpsRequest(postUrl.replace("ACCESS_TOKEN",token ) ,"POST", json) );
		//{"business_list":[{"base_info":{"address":"广东省广州市海珠区赤岗北路118号四季天地广场3楼(近珠江帝景正门)","available_state":3,"avg_price":0,"branch_name":"","business_name":"八百方网上药店","categories":["医疗保健,药房药店"],"city":"广州市","district":"海珠区","introduction":"","latitude":23.0996131897,"longitude":113.327552795,"offset_type":1,"open_time":"","photo_list":[],"poi_id":"292897619","province":"广东省","recommend":"","sid":"","special":"","telephone":"","update_status":0}},{"base_info":{"address":"江南西","available_state":2,"avg_price":0,"branch_name":"","business_name":"报刊亭","categories":["美食,江浙菜"],"city":"广州市","district":"海珠区","introduction":"","latitude":23.0971450806,"longitude":113.27520752,"offset_type":1,"open_time":"","photo_list":[],"poi_id":"292893556","province":"广东省","recommend":"","sid":"","special":"","telephone":"020-37886976","update_status":0}},{"base_info":{"address":"赤岗北路34-44号","available_state":3,"avg_price":0,"branch_name":"赤岗北路","business_name":"金康药房","categories":["美食,江浙菜"],"city":"广州市","district":"海珠区","introduction":"","latitude":23.0978736877,"longitude":113.327667236,"offset_type":1,"open_time":"8:30-22:00","photo_list":[],"poi_id":"212994529","province":"广东省","recommend":"专业药师咨询","sid":"","special":"可用医保卡,送货上门,免费WIFI,中药打粉代煎","telephone":"020-89663365","update_status":0}},{"base_info":{"address":"赤岗北路18号四季天地广场","available_state":4,"avg_price":200,"branch_name":"","business_name":"四季天地","categories":["医疗保健,药房药店"],"city":"广州市","district":"海珠区","introduction":"八百方医药健康网购商城（www.babaifang.com）是全国第二家第三方药品网上零售试点平台。通过聚集全国主流药店，为广大消费者提供“百家药店，全国比价，放心买药、买放心药”的第三方医药网购平台。","latitude":23.10039711,"longitude":113.327919006,"offset_type":1,"open_time":"9:00-21:00","photo_list":[],"poi_id":"278354754","province":"广东省","recommend":"网上买药","sid":"","special":"实惠网上买药","telephone":"020-37886986","update_status":0}}],"errcode":0,"errmsg":"ok","total_count":4}
	
	}
	*/
	//1.获得 门店poi_id:292897619
	//2.为门店申请设备数量。
	static String token="i1qxSlJ6meay2fcoCYvYSovYwirWBgUZ7_TJxd96zqrHDRRpZR3oWkHtuWYd_9gy0znWowHwInZ6rOh_g7AclHULjTHo3F7Huk4Kw7zzFi4";
	private static void  applyDevice(){
		String postUrl="https://api.weixin.qq.com/shakearound/device/applyid?access_token=ACCESS_TOKEN";

		String json="{"
				+"\"quantity\":1,"	
				+"\"apply_reason\":\"测试\","	
				+"\"comment\":\"测试专用\","
				+"\"poi_id\":292897619"	
				+"}";
		 System.out.println( WeiXinUtil.httpsRequest(postUrl.replace("ACCESS_TOKEN",token ) ,"POST", json) );
		//{"business_list":[{"base_info":{"address":"广东省广州市海珠区赤岗北路118号四季天地广场3楼(近珠江帝景正门)","available_state":3,"avg_price":0,"branch_name":"","business_name":"八百方网上药店","categories":["医疗保健,药房药店"],"city":"广州市","district":"海珠区","introduction":"","latitude":23.0996131897,"longitude":113.327552795,"offset_type":1,"open_time":"","photo_list":[],"poi_id":"292897619","province":"广东省","recommend":"","sid":"","special":"","telephone":"","update_status":0}},{"base_info":{"address":"江南西","available_state":2,"avg_price":0,"branch_name":"","business_name":"报刊亭","categories":["美食,江浙菜"],"city":"广州市","district":"海珠区","introduction":"","latitude":23.0971450806,"longitude":113.27520752,"offset_type":1,"open_time":"","photo_list":[],"poi_id":"292893556","province":"广东省","recommend":"","sid":"","special":"","telephone":"020-37886976","update_status":0}},{"base_info":{"address":"赤岗北路34-44号","available_state":3,"avg_price":0,"branch_name":"赤岗北路","business_name":"金康药房","categories":["美食,江浙菜"],"city":"广州市","district":"海珠区","introduction":"","latitude":23.0978736877,"longitude":113.327667236,"offset_type":1,"open_time":"8:30-22:00","photo_list":[],"poi_id":"212994529","province":"广东省","recommend":"专业药师咨询","sid":"","special":"可用医保卡,送货上门,免费WIFI,中药打粉代煎","telephone":"020-89663365","update_status":0}},{"base_info":{"address":"赤岗北路18号四季天地广场","available_state":4,"avg_price":200,"branch_name":"","business_name":"四季天地","categories":["医疗保健,药房药店"],"city":"广州市","district":"海珠区","introduction":"八百方医药健康网购商城（www.babaifang.com）是全国第二家第三方药品网上零售试点平台。通过聚集全国主流药店，为广大消费者提供“百家药店，全国比价，放心买药、买放心药”的第三方医药网购平台。","latitude":23.10039711,"longitude":113.327919006,"offset_type":1,"open_time":"9:00-21:00","photo_list":[],"poi_id":"278354754","province":"广东省","recommend":"网上买药","sid":"","special":"实惠网上买药","telephone":"020-37886986","update_status":0}}],"errcode":0,"errmsg":"ok","total_count":4}
	//{"data":{"apply_id":74965,"audit_comment":"审核中","audit_status":1},"errcode":0,"errmsg":"success."}
	}
	
	//3 新增页面
	private static void  getImgList(){
		String postUrl="https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";
 String json="{\"type\":\"image\",\"offset\":0,\"count\":20}";
		 System.out.println( WeiXinUtil.httpsRequest(postUrl.replace("ACCESS_TOKEN",token ) ,"POST", json) );
		//{"business_list":[{"base_info":{"address":"广东省广州市海珠区赤岗北路118号四季天地广场3楼(近珠江帝景正门)","available_state":3,"avg_price":0,"branch_name":"","business_name":"八百方网上药店","categories":["医疗保健,药房药店"],"city":"广州市","district":"海珠区","introduction":"","latitude":23.0996131897,"longitude":113.327552795,"offset_type":1,"open_time":"","photo_list":[],"poi_id":"292897619","province":"广东省","recommend":"","sid":"","special":"","telephone":"","update_status":0}},{"base_info":{"address":"江南西","available_state":2,"avg_price":0,"branch_name":"","business_name":"报刊亭","categories":["美食,江浙菜"],"city":"广州市","district":"海珠区","introduction":"","latitude":23.0971450806,"longitude":113.27520752,"offset_type":1,"open_time":"","photo_list":[],"poi_id":"292893556","province":"广东省","recommend":"","sid":"","special":"","telephone":"020-37886976","update_status":0}},{"base_info":{"address":"赤岗北路34-44号","available_state":3,"avg_price":0,"branch_name":"赤岗北路","business_name":"金康药房","categories":["美食,江浙菜"],"city":"广州市","district":"海珠区","introduction":"","latitude":23.0978736877,"longitude":113.327667236,"offset_type":1,"open_time":"8:30-22:00","photo_list":[],"poi_id":"212994529","province":"广东省","recommend":"专业药师咨询","sid":"","special":"可用医保卡,送货上门,免费WIFI,中药打粉代煎","telephone":"020-89663365","update_status":0}},{"base_info":{"address":"赤岗北路18号四季天地广场","available_state":4,"avg_price":200,"branch_name":"","business_name":"四季天地","categories":["医疗保健,药房药店"],"city":"广州市","district":"海珠区","introduction":"八百方医药健康网购商城（www.babaifang.com）是全国第二家第三方药品网上零售试点平台。通过聚集全国主流药店，为广大消费者提供“百家药店，全国比价，放心买药、买放心药”的第三方医药网购平台。","latitude":23.10039711,"longitude":113.327919006,"offset_type":1,"open_time":"9:00-21:00","photo_list":[],"poi_id":"278354754","province":"广东省","recommend":"网上买药","sid":"","special":"实惠网上买药","telephone":"020-37886986","update_status":0}}],"errcode":0,"errmsg":"ok","total_count":4}
	//{"data":{"apply_id":74965,"audit_comment":"审核中","audit_status":1},"errcode":0,"errmsg":"success."}
		 //{"item":[{"media_id":"8_OQptZK7_yC-HWpfU5D5cfvRhsqv8uteXdicshKG-g","name":"ba2.png","update_time":1438242465
		 //,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VLcaX2vwVxa4A2saICARL3KuKfgDRNYL4twR7vvPORHKibpL4wNqjfbia42APsia5wUokDO2q3hCKZag/0?wx_fmt=png"
		 //},{"media_id":"OBWcVtEIKsLk8_TDbxFzL05ZP7KKGJMthvGHkyYl60Y","name":"new-2013-09-03-hjm-01.jpg","update_time":1438163762,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mb8DJt19tMFsSTtSmomKJFWVukn2X6AOC4GDhvdtvPjH806HR0ibFsAQ/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL2msjcGAKfo-nn3Fy5w5YlE","name":"640.webp.jpg","update_time":1438163283,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mys7X6zeeQkSsauFyKYnVeOBia45YVk8BhGS5tazOBKH2tK58tfRxwfg/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzLydRUB2mor2lqkrssESUP-g","name":"1414983315546.jpg","update_time":1438162982,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mJowM3hfdIkZ9slEywMCYpS74lLNVI1SknDKCib2ol07Anlzp9xicU7rQ/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL_10bHSf73ShW0rsN_2BOec","name":"229014.jpg","update_time":1438162968,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mPnovNJ1RlxITWdRAqjbyAl0c96Ud0ve2IEEicRtq1mBbemTuRsmhd5A/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL5JveZQTKLYLt62pxPcCGTU","name":"201347932458971.jpg","update_time":1438162946,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mxtmN3BkpuECRxicwiahTYlZGYM0Fr1bmPr8Ss4wzWExtpPia3Ehw6L59w/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL2wKxIGPQOHIi4KdRxY68qA","name":"00e93901213fb80ed71b9bc535d12f2eb83894de.jpg","update_time":1438162925,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9m9CHWafw5VZRxf1WKZcoPTaAqIYNwl6qmet7muF0rcjOPTWUibTYprtQ/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL69kCFAd3fmBexguv7Ixma4","name":"7427ea210c5412a4cee627.jpg","update_time":1438162894,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9myLNtTfU7hqvasU35NsT54Cick8Z9cHnCYiczp5QKsBOdibYiaIWLvHP4CQ/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL67UR9UoznWU_XXmCox6nEk","name":"rBUyIVEthZO7PcCOAABqUcEJINY469.jpg","update_time":1438162894,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mib06DH3MuQptlmAmBYKuW6E1QpqtwYiaibGfmBk3FtXHCSGW8SEfjoicmA/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL7O_svjkHbmxv6__KTn7tuo","name":"QQ截图20150729145435.jpg","update_time":1438162862,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9m5ksOUUj8Qib1vzgvgkdf9Wib4QzIBK5xaicakHyva195VX1kM9Ww1UBiaA/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL1luSbWemlX-hfJkTmkAiVw","name":"QQ截图20150729151719.jpg","update_time":1438162842,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9m0BFAxIhiagbicuic7M9iafGfsPFJKyjyPbtQTyzYjGIxteTtjz5SkqXXng/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzLwPTUOBVhDXdC18Hi7iUY7Y","name":"105.jpg","update_time":1438162822,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mXr1SxSF6EZj0agoO7Qcs3aoiap4IHZIGiaN7YFoP6qDJqZgMUko7Cymg/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzLyEdiLaKm9eEhzG2Ldn_iyI","name":"ab605ec4gc6a8cac1e707&690.jpg","update_time":1438162804,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9myPZfpp6j5IibSj7Hu2vwNljqlIoDRkVEZQ9L4pFCdHFDOsquKwQLrAA/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL9UqBe3fWuBK0lLLYYV_2mI","name":"res01_attpic_brief.jpg","update_time":1438162783,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mh2eIlGgIj0qmySDicemKPDe7grtusoGb4uhZoSfezich3HoPBZdQdJHw/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL5Ficaszt9iTHfl0gdGcNU8","name":"W020111122390170505535.jpg","update_time":1438162722,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mSCHRopSvTxLxlyaC0icBjXicaJL91j8vDYfAXTXvowUiaFjuvpVeyFQiag/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL7nnKpEaLAfteMUPabSjfN4","name":"webwxgetmsgimg.jpg","update_time":1438162679,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mJuqRqHibcVzLLe1wricJSWhNkNA1n8l2S3E2zUm0Ym2cl04AfLa4owHQ/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL9TgcKjgXuWslqY58M2mCD0","name":"QQ截图20150729172002.jpg","update_time":1438161690,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mT4UUGwqV3ezxfbV5JGbckmpQjow1bkHt2VRnC3aQ1LIoXdr0N0aYCg/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL3CQstiIpI8LdW3aTvMYrHs","name":"QQ截图20150729171553.jpg","update_time":1438161466,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mPJohR4xTTxpQrhw0olbZKbFWq4ibZeNASvwWyx20N3THQjQa2xEYfDw/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL6beXrSFl3_iFoB6QoXuQcI","name":"Img395168614.jpg","update_time":1438161347,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9mDcRDrudalAVF5m7vu21o9uFnJrQEAKdfxdBYibdTgoBlKmiaef4wA40A/0?wx_fmt=jpeg"},{"media_id":"OBWcVtEIKsLk8_TDbxFzL2HWQ-yNidK3W2PPnPkLqNQ","name":"65b3f01e89e5020a06fa4124f1115fd8_640_473.jpg","update_time":1438161211,"url":"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VJSU5zoiat8DTRkibQcQ7ian9m04lvlvp2SbjKosLdCHeuUVYHN3n7aSgAvJA2jXsBXpa0HBUibTRygIA/0?wx_fmt=jpeg"}],"item_count":20,"total_count":1654}

	}
	
	
		
	
	private static void  submitPage(){
		String postUrl="https://api.weixin.qq.com/shakearound/page/add?access_token=ACCESS_TOKEN";

		String json="{"
				+"\"title\":\"八百方商城\","   
				+"\"description\":\"八百方商城\","	
				+"\"page_url\":\"http://www.800pharm.com/shop/m/\","	
				//+"\"comment\":\"买药上八百方,正品保证！\","
				+"\"icon_url\":\"https://mmbiz.qlogo.cn/mmbiz/WvhJQP4W7VLcaX2vwVxa4A2saICARL3KuKfgDRNYL4twR7vvPORHKibpL4wNqjfbia42APsia5wUokDO2q3hCKZag/0\""
				+"}";
		System.out.println(json);
		 System.out.println( WeiXinUtil.httpsRequest(postUrl.replace("ACCESS_TOKEN",token ) ,"POST", json) );
		
	}
	
		
	
}
