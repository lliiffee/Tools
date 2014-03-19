package tool.fung.enumUsage;

public enum Operation {

	PLUS("+") {double apply(double x, double y){return x+y;}},
	MINUS("-") {double apply(double x, double y){return x-y;}};
	
	private final String symbol;
	Operation(String symbol)
	{
		this.symbol=symbol;
	}
	//@Override public String toString(){return symbol;}
	
	abstract double apply(double x, double y);
}
