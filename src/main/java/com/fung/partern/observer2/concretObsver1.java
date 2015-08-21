package com.fung.partern.observer2;

import java.util.Observable;
import java.util.Observer;

public class concretObsver1  implements Observer  
{  
    @Override  
    public void update(Observable o, Object arg)  
    {  
        System.out.println("Watcher1's count: " + arg);  
    }  
}  