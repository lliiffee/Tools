package com.fung.partern.mediator;

public interface MyObserver {
	//这个相当于java.util.Observer 接口 
    void update(MyObservable o, Object arg);
}
