package com.fung.partern.listener;

import java.util.EventListener;

/** 
 * DemoEvent事件监听器接口 
 * @author administrator 
 * 
 */  
public interface DemoEventListener extends EventListener {  
  
    public void processEvent(DemoEvent demoEvent);  
  
}  