package com.fung.partern.observer;

/**
 * 天气预报布告板的现场预报板块
 * @author Administrator
 *
 */
public class ForecastDisplay implements DisplayElement, Observer {
    private float currentPressure = 29.92f;  
    private float lastPressure;
    private Subject weatherData;
    
    public ForecastDisplay(WeatherData weatherData) {
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }

    public void update(float temp, float humidity, float pressure) {
        lastPressure = currentPressure;
        currentPressure = pressure;
        display();
    }

    public void display() {
        System.out.print("Forecast: ");
        if (currentPressure > lastPressure) {
            System.out.println("Improving weather on the way!");
        } else if (currentPressure == lastPressure) {
            System.out.println("More of the same");
        } else if (currentPressure < lastPressure) {
            System.out.println("Watch out for cooler, rainy weather");
        }
    }

}
