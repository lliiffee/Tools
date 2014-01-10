package com.fung.partern.calPrice;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/** 
 * 反射类 
 * @author LLS 
 * 
 */  
public class Reflection{  
      
    //采用饿汉式单利模式，可能占内存  
    private static Reflection instance=new Reflection();  
      
    //系统缺省配置文件名称  
    private final String sysconfig="sys-config.xml";  
      
    //保存具体策略键值对  
    private Map strategyMap =new HashMap();  
      
    //读取出来的document对象  
    private Document doc;  
      
    private  Reflection()  
    {  
        try {  
            doc=new SAXReader().read(Thread.currentThread().getContextClassLoader().getResourceAsStream(sysconfig));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    //得到实例 的方法  
    public static Reflection getInstance() {  
        return instance;  
    }  
      
    /** 
     * 根据策略 编号，取得的具体策略 
     * @param beanId 
     * @return 
     */  
    public synchronized Object  getStrategyObject(Class c)  
    {  
        //判断serviceMap里面是否有service对象，没有创建，有返回  
        if (strategyMap.containsKey(c.getName())) {  
            return strategyMap.get(c.getName());  
        }  
        //返回指定ID的Element对象  
        Element strategyElement=(Element)doc.selectSingleNode("//strategy[@id=\""+c.getName()+"\"]");  
          
        String className=strategyElement.attributeValue("class");  
          
        Object strategyObject=null;  
        try {  
            strategyObject = Class.forName(className).newInstance();  
            strategyMap.put(c.getName(), strategyObject);  
                
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException();  
        }  
          
        return strategyObject;  
    }  
}
