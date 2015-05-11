package com.fung.partern.mediator;

public class Students {
	 private String name;
	 private double exSalary;//渴望工资
	 private Mediator jobMediator;//保存注册的中介对象
	 public void sendMes(String comName,String info){
	//学生向公司发送求职意向信息
	  jobMediator.sendMes(this,comName,info);
	 }
	 public boolean isOk(double offerSalary){
	//判断工资条件是否符合求职者要求
	  return offerSalary>exSalary;
	 }
	 public String getName() {
	  return name;
	 }
	 public void setName(String name) {
	  this.name = name;
	 }
	 public double getExSalary() {
	  return exSalary;
	 }
	 public void setExSalary(double exSalary) {
	  this.exSalary = exSalary;
	 }
	 public Mediator getJobMediator() {
	  return jobMediator;
	 }
	 public void setMediator(Mediator jobMediator) {
	  this.jobMediator = jobMediator;
	 }
}
