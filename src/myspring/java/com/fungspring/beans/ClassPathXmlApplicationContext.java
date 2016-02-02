package com.fungspring.beans;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ClassPathXmlApplicationContext implements BeanFactory{

	 //定义一个容器，用来存放对象
	  private Map<String,Object> beans = new HashMap<String, Object>();

	public ClassPathXmlApplicationContext() throws Exception{
		   SAXBuilder sb = new SAXBuilder();
		    Document doc = sb.build(this.getClass().getClassLoader().getResourceAsStream("beans.xml"));
		    Element root = doc.getRootElement();  //获取根结点
		    List list = root.getChildren("bean");  //取名为bean的所有元素
		    for(int i = 0; i < list.size(); i++) {
		      Element element = (Element) list.get(i);
		      String id = element.getAttributeValue("id");  //取id值
		      String cla = element.getAttributeValue("class"); //取class值
		      Object o = Class.forName(cla).newInstance();
		      System.out.println(id);
		      System.out.println(cla);
		      beans.put(id,o);
		        
		      for(Element propertyElement : (List<Element>)element.getChildren("property")){
		        String name = propertyElement.getAttributeValue("name");  //UserDAO
		        String bean = propertyElement.getAttributeValue("bean");  //u
		        Object beanObject = beans.get(bean);//UserDAOImpl instance
		          
		        //拼凑方法名，实现setUserDAO方法
		        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
		        System.out.println("method name = " + methodName);
		          
		        //利用反射机制获取方法对象
		        Method m = o.getClass().getMethod(methodName, beanObject.getClass().getInterfaces()[0]);
		        m.invoke(o, beanObject);  //调用方法
		      }
		        
		    }
		      
		  }
	
	@Override
	  public Object getBean(String name) {
	    return beans.get("id");
	  }

}
