package com.bbf.sms.rule;

import com.bbf.sms.processor.AbstractNoteProcessor;

/** 
 * 短信格式规则验证类 
 */  
public class FormatRule implements Rule {  
    public boolean check(AbstractNoteProcessor processor) {  
        boolean pass = true;  
          
        String[] arr1 = processor.getContent().split(",");  
          
        //格式判断  
        if(arr1.length < 2 || arr1.length > 7){  
            pass = false;  
        }else{  
            for(int i=0;i<arr1.length;i++){  
                String s = arr1[i];  
                if(i == 0){  
                    if(s.split("#").length != 3){  
                        pass = false;  
                        break;  
                    }  
                }else{  
                    if(s.split("#").length != 2){  
                        pass = false;  
                        break;  
                    }  
                }  
            }  
        }  
          
        if(!pass){  
            System.out.println("短信格式有误，卡品申请短信的正确格式为：" + processor.getPrefix() + "帐号#密码,卡编号1#数量1,卡编号2#数量2,...(最多6组卡品)！");  
        }  
          
        return pass;  
    }  
  
}  