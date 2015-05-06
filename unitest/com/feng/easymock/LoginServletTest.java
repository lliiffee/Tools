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
 * EasyMock.createMock()：Creates a mock object that implements the given interface, 
 * order checking isdisabledby default. EasyMock.createNiceMock() ： Creates a mock object 
 * that implements the given interface, orderchecking isenabledby default. 发现strict mock方式
 * 下默认是开启调用顺序检测的，而普通的mock方式则默认不开启调用顺序检测
 再看一下createNiceMock()：

Creates a mock object that implements the given interface, order checking is disabled 
by default, andthe mock object will return 0, null or false for unexpected invocations
和createMock()相同的是默认不开启调用顺序检测，另外有一个非常有用的功能就是对于意料之外的调用将返回0,null 
或者false.之所以说有用，是因为在我们的实际开发过程中，有时候会有这样的需求：对于某个mock对象的调用
(可以是部分，也可以是全部)，我们完全不介意调用细节，包括是否调用和调用顺序，参数，返回值，我们只要求mock对象容许
程序可以继续而不是抛出异常报告说 unexpected invocations 。nicemock在这种情况下可以为我们节省大量的工作量，
非常方便
 */


