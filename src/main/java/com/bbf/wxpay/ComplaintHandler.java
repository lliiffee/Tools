package com.bbf.wxpay;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ComplaintHandler {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		try {
			//sendOrderHandleStatusToWx();
			String[] springConfig  = 
				{	
					"wxpay_appContext.xml" 
				};
	//need spring fastjson mysql_jdbc		 
			 
		 
			ApplicationContext ctx = 
					new ClassPathXmlApplicationContext(springConfig);
		  
			WxPayService wxPayService =(WxPayService)ctx.getBean("wxPayService"); 
			List<WxPayFeedBack> list =wxPayService.getComplaintList();
			
			List<WxPayFeedBack> listSuccess = new ArrayList();
			//发送通知
			for(WxPayFeedBack bean : list)
			{
				System.out.println(bean.getAppId()+"-");
				
			}
			
			//更新处理状态
			if(listSuccess.size()>0)
			{
				// wxPayService.batchUpdateByTranId(listSuccess);
 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
