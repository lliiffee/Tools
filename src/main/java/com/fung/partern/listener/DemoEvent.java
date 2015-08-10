package com.fung.partern.listener;

import java.util.EventObject;

/** 
 * 事件对象的定义 
 * @author administrator 
 * 
 */  
public class DemoEvent extends EventObject {  
    private static final long serialVersionUID = 1L;  
  
    public DemoEvent(Object source) {  
        super(source);  
    }  
}  
