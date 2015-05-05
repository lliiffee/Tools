package com.feng.first;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/*
 * @BeforeClass 全局只会执行一次，而且是第一个运行
@Before 在测试方法运行之前运行
@Test 测试方法
@After 在测试方法运行之后允许
@AfterClass 全局只会执行一次，而且是最后一个运行
@Ignore 忽略此方法
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
	//限时测试。    
	    @Test(timeout = 1000)

	    public void squareRoot() {

	        calculator.squareRoot(4);

	        assertEquals(2, calculator.getResult());

	 

	    }
//，如果除数是一个0，那么必然要抛出“除0异常”。因此，我们很有必要对这些进行测试
	    @Test(expected = ArithmeticException.class)

	    public void divideByZero() {

	  calculator.divide(0); 

	    }
}
