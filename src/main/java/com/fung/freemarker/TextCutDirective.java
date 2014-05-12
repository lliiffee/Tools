package com.fung.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TextCutDirective implements TemplateDirectiveModel {  
    public static final String PARAM_S = "s";  
    public static final String PARAM_LEN = "len";  
    public static final String PARAM_APPEND = "append";  
  
    @SuppressWarnings("unchecked")  
    public void execute(Environment env, Map params, TemplateModel[] loopVars,  
            TemplateDirectiveBody body) throws TemplateException, IOException {  
        String s = getString(PARAM_S, params);  
        Integer len = getInt(PARAM_LEN, params);  
        String append = getString(PARAM_APPEND, params);  
        if (s != null) {  
            Writer out = env.getOut();  
            if (len != null) {  
                out.append(textCut(s, len, append));  
            } else {  
                out.append(s);  
            }  
        }  
    }  
    
    
    private String textCut(String s, int len,String append)
    {
    	return s.substring(0, len)+append;
    }
    
    private Integer getInt(String s,Map params)
    {
    	if(params!=null)
    	{
    		return (params.get(s)!=null?Integer.parseInt(params.get(s).toString()):0);
    	}else
    	{
    		return 0;
    	}
    }
    
    private String getString(String s,Map params)
    {
    	if(params!=null)
    	{
    		return (params.get(s)!=null?params.get(s).toString():"");
    	}else
    	{
    		return "";
    	}
    }
    
}
