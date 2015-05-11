package com.fung.partern.mediator;

public class Company {
	private JobMediator jobMediator;
	 private String name;
	 private double offerSalary;
	 public JobMediator getJobMediator() {
	  return jobMediator;
	 }
	 public void setJobMediator(JobMediator jobMediator) {
	  this.jobMediator = jobMediator;
	 }
	 public String getName() {
	  return name;
	 }
	 public void setName(String name) {
	  this.name = name;
	 }
	 public double getOfferSalary() {
	  return offerSalary;
	 }
	 public void setOfferSalary(double offerSalary) {
	  this.offerSalary = offerSalary;
	 }
	 public void receive(Students s,String info){
	  System.out.println("收到学生"+s.getName()+"的信息:"+info);
	 }
}
