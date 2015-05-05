package com.feng.first;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/*
 * @BeforeClass ȫ��ֻ��ִ��һ�Σ������ǵ�һ������
@Before �ڲ��Է�������֮ǰ����
@Test ���Է���
@After �ڲ��Է�������֮������
@AfterClass ȫ��ֻ��ִ��һ�Σ����������һ������
@Ignore ���Դ˷���
 */
public class CalculatorTest {

	private static Calculator calculator = new Calculator();
	
	@Before
	public void setUp() throws Exception {
		System.out.println("setUp.....");
		calculator.clear();
	}

	@Test
	public void testAdd() {
		//fail("Not yet implemented");
		calculator.add(2);
        calculator.add(3);
        assertEquals(5, calculator.getResult());
	}

	@Test
	public void testSubstract() {
		//fail("Not yet implemented");
		calculator.add(10);
        calculator.substract(2);
        assertEquals(8, calculator.getResult());
	}
	
	    @Ignore("Multiply() Not yet implemented")
	    @Test
	    public void testMultiply() {
	    }

	    @Test
	    public void testDivide() {
	        calculator.add(8);
	        calculator.divide(2);
	        assertEquals(4, calculator.getResult());
	    }
	//��ʱ���ԡ�    
	    @Test(timeout = 1000)

	    public void squareRoot() {

	        calculator.squareRoot(4);

	        assertEquals(2, calculator.getResult());

	 

	    }
//�����������һ��0����ô��ȻҪ�׳�����0�쳣������ˣ����Ǻ��б�Ҫ����Щ���в���
	    @Test(expected = ArithmeticException.class)

	    public void divideByZero() {

	  calculator.divide(0); 

	    }
}
