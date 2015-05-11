package com.fung.partern.mediator;

public class CollegeStu extends Students implements MyObserver {
	//实现Myobserver接口，作为观察者模式中的观察着
	
	 public void update(MyObservable o, Object arg) {
	  // TODO Auto-generated method stub
	  System.out.println(getName()+" 你好：新公司"+((Company)arg).getName()+" offers "+((Company)arg).getOfferSalary());
	  this.sendMes(((Company)arg).getName(), "你好，我是 "+getName()+" 对你们的公司感兴趣！！");
	 }
}