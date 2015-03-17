package com.fung.oom.overflow;

import java.lang.reflect.Field;
import sun.misc.Unsafe;
public class DirectMemoryOOM {

	/**
	 * vm args: -Xmx20M -XX:MaxDirectMemorySize=10M
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		Field unsafeField=Unsafe.class.getDeclaredFields()[0];
		unsafeField.setAccessible(true);
		Unsafe unsafe=(Unsafe)unsafeField.get(null);
		while(true){
			unsafe.allocateMemory(__1MB);
		}

	}
	//Exception in thread main ..OOM 
	private static final int __1MB=1024*1024;
	
	

}
