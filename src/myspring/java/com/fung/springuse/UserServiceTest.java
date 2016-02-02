package com.fung.springuse;

import org.junit.Test;

import com.fungspring.beans.BeanFactory;
import com.fungspring.beans.ClassPathXmlApplicationContext;

public class UserServiceTest {
	@Test
	  public void testAdd() throws Exception {
	    BeanFactory applicationContext = new ClassPathXmlApplicationContext();
	    UserService service = (UserService)applicationContext.getBean("userService");
	    
	    User u = new User();
	    u.setUsername("yuwl");
	    u.setPassword("123456");
	    service.add(u);
	  }
	
	
	/*
	 * # define beans 
Bean.target=AOP.aop_di.RemoteReportCreator 
Bean.targetProxy=AOP.aop_di.ProxyHandler 
Bean.logBeforeAdvice=AOP.aop_di.LogBeforeAdvice 
 
# define DI 
DI.targetProxy.target=target 
DI.targetProxy.beforeAdvice=logBeforeAdvice 


// 测试 
public class AOP_DI { 
    public static void main(String[] args) { 
        // 初始化环境配置 
        ProxyFactory proxyFactory = new ProxyFactory("config.properties"); 
        // 获取被代理后的Target对象 
        ReportCreator reportCreator = (ReportCreator) proxyFactory 
                .getProxy("targetProxy", "target"); 
        // 使用被代理后的target对象提供的服务 
        reportCreator.getHtmlReport("http://code.google.com/file/..."); 
    } 
} 

	 */
}
