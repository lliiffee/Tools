package test;

import java.io.IOException;

import sun.misc.BASE64Decoder;



public class Test {
	
	public static void main(String[] args)
	{
		//25.12321312
		long lt=2512321312l;
		int	n=-8;
        System.out.println("out="+lt  * Math.pow(10,-8));
				
		Integer supportCodId=2;
		System.out.println(supportCodId.compareTo(2));
		String str="cx中文";
		System.out.println(str.startsWith("cx"));
		System.out.println(str.substring(2));
		
		
		String code = "Z3o3LU9xcVotUkZxYy15RHFt";
		if(code!=null){
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				code=new String(decoder.decodeBuffer(code));
				
				System.out.println(code);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
