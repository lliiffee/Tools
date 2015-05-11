package com.fung.partern.mediator;

import java.util.HashMap;

public class JobMediator extends MyObservable implements Mediator {
	//实现Mediator接口作为中介者
	//扩展MyObservable 类，作为观察者模式中的被观察者
	 HashMap comHash;
	 HashMap stuHash;
	 public JobMediator(){
	  comHash=new HashMap();
	  stuHash=new HashMap();
	 }
	
	 public void comRegister(Company com) {
	  // TODO Auto-generated method stub
	  if(!comHash.containsKey(com.getName())){
	   comHash.put(com.getName(), com);
	   com.setJobMediator(this);
	   setChanged();
	   notifyObservers(com);
	  }else{
	   System.out.println(" 这个用户名已经存在了~~~");
	  }
	 }
	
	 public void sendMes(Students s, String comName, String info) {
	  // TODO Auto-generated method stub
	  Company com=(Company) comHash.get(comName);
	  if(com!=null)
	   com.receive(s,info);//当被观察者上面，有新公司注册时向符合条件的求职者发送信息
	 }
	 
	 public void stuRegister(Students stu) {
	  if(!stuHash.containsKey(stu.getName())){
	   stuHash.put(stu.getName(), stu);
	   stu.setMediator(this);
	  }else{
	   System.out.println(" 这个用户名已经存在了~~~");
	  }
	 }
	 public static void main(String []args){
	  JobMediator jobM=new JobMediator();
	  CollegeStu stu1=new CollegeStu();
	  CollegeStu stu2=new CollegeStu();
	  stu1.setExSalary(2000.5);
	  stu1.setName("zhansan");
	  jobM.addObserver(stu1);
	  jobM.stuRegister(stu1);
	  stu2.setExSalary(1000.5);
	  stu2.setName("lisi");
	  jobM.addObserver(stu2);
	  jobM.stuRegister(stu2);
	  Company com=new Company();
	  com.setName("Oracle");
	  com.setOfferSalary(1985);
	  jobM.comRegister(com);
	  //Company com=new Company();
	  com.setName("HP");
	  com.setOfferSalary(3000);
	  jobM.comRegister(com);
	 }
}