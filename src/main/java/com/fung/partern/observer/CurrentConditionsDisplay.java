package com.fung.partern.observer;

public class CurrentConditionsDisplay implements Observer, DisplayElement {
    private float temp;
    private float humidity;
    private Subject weatherData;

    public void display() {
        System.out.println("current conditions:"+temp+" F degrees and "+humidity+"% humidity");
    }


    public void update(float temp, float humidity, float pressure) {
        this.temp=temp;
        this.humidity=humidity;
        display();
    }
    public CurrentConditionsDisplay(Subject weatherData){
        this.weatherData=weatherData;
        //注册改Observer，从而在后面移除的时候方便关联
        weatherData.registerObserver(this);
    }
}
