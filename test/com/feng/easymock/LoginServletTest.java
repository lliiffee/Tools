package com.feng.easymock;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMockRunner;
import org.easymock.IAnswer;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

//@RunWith(EasyMockRunner.class)
public class LoginServletTest {

	 @TestSubject
	  //  private LoginServlet servlet = new LoginServlet(); // 2
	  //  @Mock
	    private HttpServletRequest request; // 1        
	    @Before
	    public void setUp() {
	        request = createMock(HttpServletRequest.class); // 1
	    }
	    
	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws Exception{
 
        // set Mock Object behavior: 
		//request.getParameter("username");
		
	//	expect(request.getParameter("username")).andReturn("admin");
		 expect(request.getParameter("password")).andReturn("1234");
		 expect(request.getParameter("username")).andAnswer(new IAnswer<String>() {
		        public String answer() throws Throwable {
		            return "admin";
		        }
		    });

		 
	    
	//     expectLastCall().times(3);
        // ok, all behaviors are set!        
         replay(request);         // now start test:  
        LoginServlet servlet = new LoginServlet();      
        try {             
        	servlet.doPost(request, null);   
        	fail("Not caught exception!");        
        	} catch(RuntimeException re) {       
        		assertEquals("Login failed.", re.getMessage());        
        		}         
        // verify:      
        verify(request);     
        
	}

}

/*
 * EasyMock.createMock()��Creates a mock object that implements the given interface, 
 * order checking isdisabledby default. EasyMock.createNiceMock() �� Creates a mock object 
 * that implements the given interface, orderchecking isenabledby default. ����strict mock��ʽ
 * ��Ĭ���ǿ�������˳����ģ�����ͨ��mock��ʽ��Ĭ�ϲ���������˳����
 �ٿ�һ��createNiceMock()��

Creates a mock object that implements the given interface, order checking is disabled 
by default, andthe mock object will return 0, null or false for unexpected invocations
��createMock()��ͬ����Ĭ�ϲ���������˳���⣬������һ���ǳ����õĹ��ܾ��Ƕ�������֮��ĵ��ý�����0,null 
����false.֮����˵���ã�����Ϊ�����ǵ�ʵ�ʿ��������У���ʱ��������������󣺶���ĳ��mock����ĵ���
(�����ǲ��֣�Ҳ������ȫ��)��������ȫ���������ϸ�ڣ������Ƿ���ú͵���˳�򣬲���������ֵ������ֻҪ��mock��������
������Լ����������׳��쳣����˵ unexpected invocations ��nicemock����������¿���Ϊ���ǽ�ʡ�����Ĺ�������
�ǳ�����
 */


