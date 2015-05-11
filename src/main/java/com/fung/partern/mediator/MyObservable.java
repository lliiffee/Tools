package com.fung.partern.mediator;

import java.util.Vector;

public class MyObservable {
	//相当于java.util.Observable   类
    private boolean changed = false;   
    private Vector obs;     
    //创建被观察者时就创建一个它持有的观察者列表，注意，这个列表是需要同步的。   
    public MyObservable() {   
    obs = new Vector();   
    }    
      
    public synchronized void addObserver(MyObserver o) {   
        if (o == null)   
            throw new NullPointerException();   
    if (!obs.contains(o)) {   
        obs.addElement(o);   
    }   
    }    
      
    public synchronized void deleteObserver(MyObserver o) {   
        obs.removeElement(o);   
    }    
      
    public void notifyObservers() {   
    notifyObservers(null);   
    }    
      
    public void notifyObservers(Object arg) {      
        Object[] arrLocal;    
    synchronized (this) {   
        if (!changed)   
                return;   
            arrLocal = obs.toArray();   
            clearChanged();   
        }   
  
        for (int i = arrLocal.length-1; i>=0; i--)
         if(((Students)arrLocal[i]).isOk(((Company)arg).getOfferSalary())){
          //遍历观察者（已注册的学生）看公司的条件是否达到学生的要求，满足则向学生发送信息。
          //这里简单起见只是以工资为标准的。
            ((MyObserver)arrLocal[i]).update(this, arg);  
         } 
    }
 private void clearChanged() {
     // TODO Auto-generated method stub
     changed=false;
 }   
 public void setChanged(){
      changed=true;
 }
}
