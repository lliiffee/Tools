package com.feng.easymock;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class ExampleTest {
	    @TestSubject
	    private ClassUnderTest classUnderTest = new ClassUnderTest(); // 2
	    @Mock
	    private Collaborator mock; // 1        
	    @Before
	    public void setUp() {
	      //  mock = createMock(Collaborator.class); // 1
	      //  classUnderTest = new ClassUnderTest();
	        classUnderTest.setListener(mock);
	    }
	    
	    @Test
	    public void testRemoveNonExistingDocument() {
	        // 2 (we do not expect anything)
	    	mock.documentRemoved("test");
	        replay(mock); // 3
	        classUnderTest.removeDocument("Does not exist");
	        verify(mock);
	    }
}
