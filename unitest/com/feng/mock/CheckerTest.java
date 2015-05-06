package com.feng.mock;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
public class CheckerTest {

	MockSystemEnvironment env;
	long t1;
	
	@Before
	public void setUp() throws Exception {
		 env =new MockSystemEnvironment();
		 Calendar cal=Calendar.getInstance();
		 cal.set(Calendar.YEAR, 2013);
		 cal.set(Calendar.MONTH, 10);
		 cal.set(Calendar.DAY_OF_MONTH, 1);
		 cal.set(Calendar.HOUR_OF_DAY, 16);
		 cal.set(Calendar.MINUTE, 55);
		  t1=cal.getTimeInMillis();
		 env.setTime(t1);
		 
	}
	
	@Test
	public void testReminder() {
		Checker ch=new Checker(env);
		ch.reminder();
		assertFalse(ch.wavWasPlayed());
		//assertTrue(ch.wavWasPlayed());
		t1+=(5*60*1000);
		env.setTime(t1);
		ch.reminder();
		
		assertTrue(ch.wavWasPlayed());
		
		ch.reset();
		t1+=(3*60*60*1000);
		env.setTime(t1);
		ch.reminder();
		//assertFalse(ch.wavWasPlayed());
		assertTrue(ch.wavWasPlayed());
	}

}
