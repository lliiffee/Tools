package com.fung.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Testing {
	 public static void main(String[] args) {  
		 
		 
		 String[] ary = {"test", "abc", "apple", "PEar"};
			
			System.out.println("Before sorted, the array is: " + Arrays.toString(ary));
			
			Arrays.sort(ary);
			
			System.out.println("After sorted, the new array is: " + Arrays.toString(ary));
			
			ArrayList<String> list=new ArrayList<String>();
			list.add("1436941547");
					list.add("0f138d82-e67d-49be-85a4-ee3e0353ec1f");
							list.add("1436941547");
									list.add("m7RQzjA_ljjEkt-JCoklRLQbep88l1R3KB3GLVpy7ZX3wqcyn081Nu_94qfGT2CPuGqsQUNos1RwS44eohiChg");
											list.add("pTNesjoVRGdEks7dONU2WEd4mANw");
				String str="";
				Collections.sort(list);
				for(String s: list)
				{
					str+=s;
				}
				System.out.println(str);
				
											
		  
	        HashMap<String,Double> map = new HashMap<String,Double>();  
	        ValueComparator bvc =  new ValueComparator(map);  
	        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);  
	  
	        map.put("A",99.5);  
	        map.put("B",67.4);  
	        map.put("C",67.4);  
	        map.put("D",67.3);  
	  
	        System.out.println("unsorted map: "+map);  
	  
	        sorted_map.putAll(map);  
	  
	        System.out.println("results: "+sorted_map);  
	    }  
	}  
	  
	class ValueComparator implements Comparator<String> {  
	  
	    Map<String, Double> base;  
	    public ValueComparator(Map<String, Double> base) {  
	        this.base = base;  
	    }  
	  
	    // Note: this comparator imposes orderings that are inconsistent with equals.      
	    public int compare(String a, String b) {  
	        if (base.get(a) >= base.get(b)) {  
	            return -1;  
	        } else {  
	            return 1;  
	        } // returning 0 would merge keys  
	    }
}
