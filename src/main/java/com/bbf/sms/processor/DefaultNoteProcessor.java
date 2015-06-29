package com.bbf.sms.processor;

import java.util.List;

import com.bbf.sms.rule.Rule;

public class DefaultNoteProcessor extends AbstractNoteProcessor {  
    private List checkRules;  
      
    public void setCheckRules(List checkRules) {  
        this.checkRules = checkRules;  
    }  
  
    /** 
     * 遍历执行每个校验规则，当一个校验规则返回false时，结束继续校验 
     *  
     * 此处应用了访问者模式 
     */  
    protected boolean validate() {  
        if(checkRules == null || checkRules.size() <= 0) return true;  
          
        for(int i=0;i<checkRules.size();i++){  
            Rule rule = (Rule)checkRules.get(i);  
            boolean b = rule.check(this);  
            if(!b) return false;  
        }  
          
        return true;  
    }  
      
    protected void proceed() {  
         
    }  
}  
