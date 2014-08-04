package com.fung.partern.observer;

/**
 * 气象站主题，其对应着多个Observer对象
 * @author Administrator
 *
 */
public interface Subject {
    //注册一个Observer
    public void registerObserver(Observer obj);
    //remove一个Observer
    public void removeObserver(Observer obj);
    //当主题对象发生改变的时候，通知所有的Observer
    public void notifyAllObserver();
}