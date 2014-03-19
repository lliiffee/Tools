package tool.fung.enumUsage;

public enum PayRollDay {
	
	MONDAY(PayType.WEEKDAY),TUESDAY(PayType.WEEKDAY),WEDNSDAY(PayType.WEEKDAY),
	THURSDAY(PayType.WEEKDAY),FRIDAY(PayType.WEEKDAY),SATURDAY(PayType.WEEKEND),
	SUNDAY(PayType.WEEKEND);
	
	private final PayType payType;
	PayRollDay(PayType payType){
		this.payType=payType;
	}
	
	double pay(double hoursWorked,double payRate)
	{
		return this.payType.pay(hoursWorked, payRate);
	}
	
	//�����ڲ�����ö�١�����
	private enum PayType{
		WEEKDAY{
			double overtimePay(double hours,double payRate){
				return hours<=HOURS_PER_SHIFT?0:(hours-HOURS_PER_SHIFT)*payRate/2;
			}
		},
		WEEKEND{
			double overtimePay(double hours,double payRate){
				return (hours-HOURS_PER_SHIFT)*payRate/2;
			}
		};
		
		private static final int HOURS_PER_SHIFT=8;
		
		abstract double overtimePay(double hrs,double payRate);
		
		double pay(double hoursWorked,double payRate){
			double basePay=hoursWorked*payRate;
			return basePay+overtimePay(hoursWorked,payRate);
		}
	}

}
