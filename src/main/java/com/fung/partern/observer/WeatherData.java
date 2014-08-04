package com.fung.partern.observer;

import java.util.ArrayList;

/**
 * 模拟气象站的具体主题Subject
 * @author Administrator
 * 
 */
public class WeatherData implements Subject {
    private ArrayList observers;
    private float temp;
    private float humidity;
    private float pressure;
    public WeatherData() {
        observers=new ArrayList();
    }

    public void registerObserver(Observer obj) {
        observers.add(obj);
    }


    public void removeObserver(Observer obj) {
        int i=observers.indexOf(obj);
        if(i>=0){
            observers.remove(i);
        }
    }


    public void notifyAllObserver() {
        for (int i = 0; i < observers.size(); i++) {
            Observer o=(Observer) observers.get(i);
            o.update(temp, humidity, pressure);
        }
    }
    //当从气象站获取更新数据的时候，我们通知观察着
    public void measurementsChange(){
        notifyAllObserver();
    }
    //读取气象站数据
    public void setMeasurements(float temp,float humidity,float pressure){
        this.temp=temp;
        this.humidity=humidity;
        this.pressure=pressure;
        measurementsChange();
    }
}