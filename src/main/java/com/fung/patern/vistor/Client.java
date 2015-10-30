package com.fung.patern.vistor;

import java.util.ArrayList;
import java.util.Collection;

public class Client {

	/**
	 * 为何使用Visitor?
		Java的Collection(包括Vector和Hashtable)是我们最经常使用的技术,可是Collection好象是个黑色大染缸,本来有各种鲜明类型特征的对象一旦放入后,再取出时,
		这些类型就消失了.那么我们势必要用If来判断,如:
		
		
		Iterator iterator = collection.iterator()
		while (iterator.hasNext()) {
		　　 Object o = iterator.next();
		　　 if (o instanceof Collection)
		　　 　　 messyPrintCollection((Collection)o);
		　　 else if (o instanceof String)
		　　 　　 System.out.println("'"+o.toString()+"'");
		　　 else if (o instanceof Float)
		　　 　　 System.out.println(o.toString()+"f");
		　　 else
		　　 　　 System.out.println(o.toString());
		}
		在上例中,我们使用了 instanceof来判断 o的类型.
		
		很显然,这样做的缺点代码If else if 很繁琐.我们就可以使用Visitor模式解决它.
	 */
	public static void main(String[] args) {
		Visitor visitor = new ConcreteVisitor();

		StringElement stringE = new StringElement("I am a String");
		visitor.visit(stringE);

		Collection list = new ArrayList();
		list.add(new StringElement("I am a String1")); 
		list.add(new StringElement("I am a String2")); 
		list.add(new FloatElement(new Float(12))); 
		list.add(new StringElement("I am a String3")); 
		visitor.visit(list);

	}

}


/*
使用Visitor模式的前提
使用访问者模式是对象群结构中(Collection) 中的对象类型很少改变。

在两个接口Visitor和Visitable中,确保Visitable很少变化,也就是说，确保不能老有新的Element元素类型加进来，可以变化的是访问者行为或操作，也就是Visitor的不同子类可以有多种,这样使用访问者模式最方便.

如果对象集合中的对象集合经常有变化, 那么不但Visitor实现要变化，Visistable也要增加相应行为，GOF建议是,不如在这些对象类中直接逐个定义操作，无需使用访问者设计模式。

但是在Java中，Java的Reflect技术解决了这个问题，因此结合reflect反射机制，可以使得访问者模式适用范围更广了。

Reflect技术是在运行期间动态获取对象类型和方法的一种技术,具体实现参考Javaworld的英文原文.
*/