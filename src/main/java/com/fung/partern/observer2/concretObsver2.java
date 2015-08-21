package com.fung.partern.observer2;

import java.util.Observable;
import java.util.Observer;

public class concretObsver2 implements Observer  
{  
    @Override  
    public void update(Observable o, Object arg)  
    {  
        if(((Integer)arg).intValue() <= 5)  
        {  
            System.out.println("Watcher2's count: " + arg);  
        }  
    }  
}  
  
