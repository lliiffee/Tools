package com.fung.partern.mediator;

public interface  Mediator {
	public void comRegister(Company com);//公司注册
	 void stuRegister(Students stu);//学生注册
	 void sendMes(Students s,String comName,String info);//实现学生向公司发送求职意向信息
}
