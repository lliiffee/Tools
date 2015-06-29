package com.bbf.sms.processor;

public abstract  class AbstractNoteProcessor {
	    private String prefix;  
	    private long smsid;  
	    private String msisdn;  
	    private String content;  
	      
	   
	  

		/** 
	     * 此处应用了模板方法模式 
	     *  
	     * @param prefix 短信前缀 
	     * @param smsid 短信流水号 
	     * @param msisdn 手机号码 
	     * @param content 短信内容 
	     */  
	    public void process(String prefix, long smsid, String msisdn, String content){  
	        this.prefix = prefix;  
	        this.smsid = smsid;  
	        this.msisdn = msisdn;  
	        this.content = content;  
	          
	        boolean b = validate();  
	        if(b){  
	            proceed();  
	        }  
	          
	    }  
	      
	    protected abstract boolean validate();  
	      
	    /** 
	     * 在该方法控制事务 
	     */  
	    protected abstract void proceed(); 
	    
	    
	    
	    
	    
	    

	    public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public long getSmsid() {
			return smsid;
		}

		public void setSmsid(long smsid) {
			this.smsid = smsid;
		}

		public String getMsisdn() {
			return msisdn;
		}

		public void setMsisdn(String msisdn) {
			this.msisdn = msisdn;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
}
