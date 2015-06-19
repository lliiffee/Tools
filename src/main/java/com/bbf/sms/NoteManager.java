package com.bbf.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** 
 * 短信管理器类 
 */  
public class NoteManager{  
    /** 
     * 存放短信处理器列表 
     */  
    private Map processorMap;  
  
    public void setProcessorMap(Map processorMap) {  
        this.processorMap = processorMap;  
    }  
  
    /** 
     * 根据短信前缀调用相应的处理器处理短信 
     *  
     * 此处应用了策略模式 
     *  
     * @param smsid 短信流水号 
     * @param msisdn 手机号码 
     * @param content 短信内容 
     */  
    private void doInProcessor(long smsid, String msisdn, String content){  
        boolean done = false;  
          
        if(processorMap!=null && processorMap.keySet().size() > 0){  
        	
        	
            for(Iterator it=processorMap.keySet().iterator();it.hasNext();){  
                String prefix = (String)it.next();  
                AbstractNoteProcessor processor = (AbstractNoteProcessor)processorMap.get(prefix);  
                  
                if(content.startsWith(prefix)){  
                    processor.process(prefix, smsid, msisdn, content);  
                    done = true;  
                    break;  
                }  
            }  
        }  
          
        if(!done){  
            System.out.println("短信格式不支持！");  
        }  
    }  
      
    public void run(){  
        List dataList = getTestData();  
        if(dataList==null || dataList.size()<=0) return;  
          
        for(int i=0;i<dataList.size();i++){  
            Map map = (Map)dataList.get(i);  
            long smsid = ((Long)map.get("smsid")).longValue();  
            String msisdn = (String)map.get("msisdn");  
            String content = (String)map.get("content");  
              
            doInProcessor(smsid, msisdn, content);  
              
        }  
    }  
      
    /** 
     * 获取测试数据的方法 
     */  
    private List getTestData(){  
        List dataList = new ArrayList();  
          
        Map map1 = new HashMap();  
        map1.put("smsid", new Long(1));  
        map1.put("msisdn", "13763387426");  
        map1.put("content", "91#3070301,123456,221#40,222#20");  
        dataList.add(map1);  
          
        Map map2 = new HashMap();  
        map2.put("smsid", new Long(2));  
        map2.put("msisdn", "13763387427");  
        map2.put("content", "66#307030102#13826532222");  
        dataList.add(map2);  
          
        Map map3 = new HashMap();  
        map3.put("smsid", new Long(3));  
        map3.put("msisdn", "13763387428");  
        map3.put("content", "88#13826532223#cl");  
        dataList.add(map3);  
          
        return dataList;  
    }  
}  