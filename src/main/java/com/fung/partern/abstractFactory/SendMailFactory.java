package com.fung.partern.abstractFactory;

public class SendMailFactory  implements Provider {  
    
  @Override  
  public Sender produce(){  
      return new MailSender();  
  }  
}
