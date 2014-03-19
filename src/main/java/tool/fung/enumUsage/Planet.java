package tool.fung.enumUsage;

public enum Planet {
	
	EARTH(5.975E+24,6.378e6);
	
	private final double  mass;//in kilograms
	private final double radius; //inmeters
	private final double surfaceGravity;//in m/s^2
	private static final double G=6.67300E-11;
	Planet(double mass,double radius)
	{
		this.mass=mass;
		this.radius=radius;
		surfaceGravity=G*mass/(radius*radius);
	}
	
	public double mass(){return mass;}
	public double radius(){return radius;}
	public double surfaceGravity(){return surfaceGravity;}
	
	public double surfaceWeight(double mass){
		return mass*surfaceGravity;
	}

}
