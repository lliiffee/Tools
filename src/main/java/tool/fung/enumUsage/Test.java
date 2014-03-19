package tool.fung.enumUsage;

public class Test {
	
	public static void main(String[] args){
		Planet p=Planet.EARTH;
		System.out.println(p.mass());
		Operation minus=Operation.MINUS;
		System.out.println(minus.valueOf("MINUS"));
		System.out.println("x"+minus+"y="+minus.apply(9, 5));
		
	}

	
}
