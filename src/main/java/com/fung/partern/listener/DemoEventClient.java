package com.fung.partern.listener;

/** 
 * 测试监听器处理过程 
 * @author administrator 
 * 
 */  
public class DemoEventClient {  
    //java中文网:http://www.javaweb.cc  
  
    public static void main(String args[]) {  
  
        //定义事件源  
        EventSource eventSource = new EventSource();  
  
        //定义并向事件源中注册事件监听器  
        FirstEventListener firstEventListener = new FirstEventListener();  
        eventSource.addDemoListener(firstEventListener);  
  
        //定义并向事件源中注册事件监听器  
        SecondEventListener secondEventListener=new SecondEventListener();  
        eventSource.addDemoListener(secondEventListener);  
  
        //事件通知  
        eventSource.notifyDemoEvent();  
    }  
}  