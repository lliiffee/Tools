package com.fung.partern.listener;

/** 
 * 第二个具体的事件监听器 
 * @author administrator 
 * 
 */  
public class SecondEventListener implements DemoEventListener {  
  
   
    public void processEvent(DemoEvent demoEvent) {  
        System.out.println("Second event listener process event...");  
  
    }  
  
}  
