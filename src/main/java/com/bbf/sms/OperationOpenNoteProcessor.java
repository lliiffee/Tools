package com.bbf.sms;

public class OperationOpenNoteProcessor  extends DefaultNoteProcessor {  
    protected void proceed() {  
        System.out.println("OperationOpenNoteProcessor短信已处理！" + "send sms... to:"+this.getMsisdn());  
    }  
}  