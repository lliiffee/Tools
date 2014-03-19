package tool.fung.annotation;

import java.lang.reflect.Method;

public class RunTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		int tests=0;
		int passed=0;
		
		Class testClass=Class.forName("tool.fung.annotation.Sample");
		for(Method m:testClass.getDeclaredMethods())
		{
			if(m.isAnnotationPresent(Test.class))
			{
				tests++;
				try{
					m.invoke(null);
					passed++;
				}catch(Exception e)
				{

			    }
			}
		 }
	}

}
