package com.fung.partern.listener;

/** 
 * 第一个具体的事件监听器 
 * @author administrator 
 * 
 */  
public class FirstEventListener implements DemoEventListener {  
  
     
    public void processEvent(DemoEvent demoEvent) {  
        System.out.println("First event listener process event...");  
  
    }  
  
}  
