package com.fung.partern.observer;

/**
 * 气象站
 * @author Administrator
 *
 */
public class WeatherStation {
    public static void main(String[] args) {
        WeatherData weatherData=new WeatherData();
        //先往Subject中添加所有注册的主题
        CurrentConditionsDisplay currentDisplay=new CurrentConditionsDisplay(weatherData);
        StatisticsDisplay statistic=new StatisticsDisplay(weatherData);
        ForecastDisplay foreCast=new ForecastDisplay(weatherData);
        //再调用方法从主题中获取数据，从而在布告板中及时显示
        weatherData.setMeasurements(50, 80, 66);
        weatherData.setMeasurements(48, 80, 67);
        weatherData.setMeasurements(52, 80, 65);
    }
}
