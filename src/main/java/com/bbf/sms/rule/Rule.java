package com.bbf.sms.rule;

import com.bbf.sms.processor.AbstractNoteProcessor;

public interface Rule {
	public boolean check(AbstractNoteProcessor processor);  
}
