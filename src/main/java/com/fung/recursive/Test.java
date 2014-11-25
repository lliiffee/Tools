package com.fung.recursive;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       System.out.println(exR2(1));
	}

	
	//Answer : The base case will never be reached. A call to exR2(3) will result in calls to
	//exR2(0), exR2(-3), exR3(-6), and so forth until a StackOverflowError occurs.
	public static String exR2(int n)
	{
		System.out.println(n);
	
	String s = exR2(n-3) + n + exR2(n-2) + n;
	if (n <= 0) return "";
	return s;
	}
	
	
	public static String exR3(int n)
	{
		System.out.println(n);
	if (n <= 0) return "";
	String s = exR2(n-3) + n + exR2(n-2) + n;
	
	return s;
	}
	
	
	//###############2
	public static int mystery(int a, int b)
	{
	if (b == 0) return 0;
	if (b % 2 == 0) return mystery(a+a, b/2);
	return mystery(a+a, b/2) + a;
	}
	
}
