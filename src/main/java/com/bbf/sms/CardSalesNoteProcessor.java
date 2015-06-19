package com.bbf.sms;

public class CardSalesNoteProcessor extends DefaultNoteProcessor {  
    protected void proceed() {  
        System.out.println("卡品批售业务短信已处理！" + "send sms... to:"+this.getMsisdn());  
    }  
}  