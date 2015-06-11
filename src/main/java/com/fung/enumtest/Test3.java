package com.fung.enumtest;

public class Test3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Color c=Color.RED;
		c.print();
		System.out.println(Food.Coffee.BLACK_COFFEE);

	}
	
	
	interface Behaviour {
        void print();

        String getInfo();
    }

     enum Color implements Behaviour {
        RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);
        // 成员变量
        private String name;
        private int index;

        // 构造方法
        private Color(String name, int index) {
            this.name = name;
            this.index = index;
        }

        // 接口方法

        
        public String getInfo() {
            return this.name;
        }

        // 接口方法
      
        public void print() {
            System.out.println(this.index + ":" + this.name);
        }
    }
    
     
     //使用接口组织枚举
     public interface Food {
         enum Coffee implements Food {
             BLACK_COFFEE, DECAF_COFFEE, LATTE, CAPPUCCINO
         }

         enum Dessert implements Food {
             FRUIT, CAKE, GELATO
         }
     }
     //ava.util.EnumSet和java.util.EnumMap是两个枚举集合。EnumSet保证集合中的元素不重复;EnumMap中的 key是enum类型，而value则可以是任意类型。
     
     //枚举和常量定义的区别
     

}
