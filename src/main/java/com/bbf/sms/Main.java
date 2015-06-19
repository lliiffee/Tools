package com.bbf.sms;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

 

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] springConfig  = 
			{	
				"sms_application.xml" 
			};
		 
		ApplicationContext ctx = 
				new ClassPathXmlApplicationContext(springConfig);
		
		NoteManager mg=	(NoteManager)ctx.getBean("noteManager"); 
		mg.run();
		
	}

}
