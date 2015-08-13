package com.fung.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tenpay.util.HttpClientUtil;

public class MapUtil {
		public static <K, V extends Comparable<? super V>> Map<K, V>   
	    sortByValue( Map<K, V> map )  
	{  
	    List<Map.Entry<K, V>> list =  
	        new LinkedList<Map.Entry<K, V>>( map.entrySet() );  
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()  
	    {  
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )  
	        {  
	            return (o1.getValue()).compareTo( o2.getValue() );  
	        }  
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();  
	    for (Map.Entry<K, V> entry : list)  
	    {  
	        result.put( entry.getKey(), entry.getValue() );  
	    }  
	    return result;  
	} 
		
		public Map<String, String> sortMapByKey(Map<String, String> oriMap) {  
		    if (oriMap == null || oriMap.isEmpty()) {  
		        return null;  
		    }  
		    Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>() {  
		        public int compare(String key1, String key2) {  
		            int intKey1 = 0, intKey2 = 0;  
		            try {  
		                intKey1 = getInt(key1);  
		                intKey2 = getInt(key2);  
		            } catch (Exception e) {  
		                intKey1 = 0;   
		                intKey2 = 0;  
		            }  
		            return intKey1 - intKey2;  
		        }});  
		    sortedMap.putAll(oriMap);  
		    return sortedMap;  
		} 
		
		public Map<String, String> sortMapByValue(Map<String, String> oriMap) {  
		    Map<String, String> sortedMap = new LinkedHashMap<String, String>();  
		    if (oriMap != null && !oriMap.isEmpty()) {  
		        List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());  
		        Collections.sort(entryList,  
		                new Comparator<Map.Entry<String, String>>() {  
		                    public int compare(Entry<String, String> entry1,  
		                            Entry<String, String> entry2) {  
		                        int value1 = 0, value2 = 0;  
		                        try {  
		                            value1 = getInt(entry1.getValue());  
		                            value2 = getInt(entry2.getValue());  
		                        } catch (NumberFormatException e) {  
		                            value1 = 0;  
		                            value2 = 0;  
		                        }  
		                        return value2 - value1;  
		                    }  
		                });  
		        Iterator<Map.Entry<String, String>> iter = entryList.iterator();  
		        Map.Entry<String, String> tmpEntry = null;  
		        while (iter.hasNext()) {  
		            tmpEntry = iter.next();  
		            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
		        }  
		    }  
		    return sortedMap;  
		}
		
		private int getInt(String str) {  
		    int i = 0;  
		    try {  
		        Pattern p = Pattern.compile("^\\d+");  
		        Matcher m = p.matcher(str);  
		        if (m.find()) {  
		            i = Integer.valueOf(m.group());  
		        }  
		    } catch (NumberFormatException e) {  
		        e.printStackTrace();  
		    }  
		    return i;  
		}  
		
		
		
		private  Map queryString2Map(String queryString) {
			if(null == queryString || "".equals(queryString)) {
				return null;
			}
			
			Map m = new HashMap();
			String[] strArray = queryString.split("&");
			for(int index = 0; index < strArray.length; index++) {
				String pair = strArray[index];
				HttpClientUtil.putMapByPair(pair, m);
			}
			
			return m;
			
		}
		
		private  String mapToMsgString(Map<String , String> map){
			StringBuffer sb=new StringBuffer();
			for (Entry<String, String> entry: map.entrySet()) {
				
				String k = (String)entry.getKey();
				String value = entry.getValue();
					sb.append(k + "=" +value + "&");
			}
			return sb.toString();
		}
}
