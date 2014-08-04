package com.fung.partern.observer;

/**
 * defination much observer object
 * @author Administrator
 *
 */
public interface Observer {
    //defined a update method,parameter include temp,humidity,pressure
    public void update(float temp,float humidity,float pressure);
}
