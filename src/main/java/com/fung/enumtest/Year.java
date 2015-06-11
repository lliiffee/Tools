package com.fung.enumtest;

public enum Year{
	JAN(MonType.BIG), FEB(MonType.SPECIAL), MAR(MonType.BIG), APR(MonType.SMALL), MAY(  
	        MonType.BIG), JUN(MonType.SMALL);//剩余月份略掉;  
	
	
	Year(MonType type) {  this.type = type; }
	
	private final MonType type;
	
	   public int ContainDays(int year) {  
	      return type.countResult(year);  
	}  
	private enum MonType implements Count {  
	    BIG {  
	        public int countResult(int num) {  
	            return 31;  
	        }  
	    },  
	    SMALL {  
	        public int countResult(int num) {  
	            return 30;  
	        }  
	    },  
	    SPECIAL {  
	        public int countResult(int num) {  
	            if ((num % 4 == 0 && num % 100 != 0) || (num % 400 == 0)) {  
	                      return 28;  
	            } else {  
	                return 29;  
	            }                 
	        }  
	    }  
	}    
}