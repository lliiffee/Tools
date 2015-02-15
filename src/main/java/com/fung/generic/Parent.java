package com.fung.generic;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.common.recycler.Recycler.V;

 
//* 类型参数 K 和 V 在类级别的规格说明，表示在声明一个 Parent 类型的变量时指定的类型的占位符。 
//在 get()、put() 和其他方法的方法签名中使用的 K 和 V。 

//1. 泛型定义在类上：如果一个同时用到同一个泛型那么可以把泛型定义到类上 ，可以声明一个或多个   其他方法的方法签名中使用的 K 和 V。 
public class Parent <K,V>{
	
 
//为了赢得使用泛型的好处，必须在定义或实例化Map类型的变量时为K和V提供具体的值。以一种相对直观的方式做这件事： 
		public void put(K key, V value){}; 
		public V get(K key){ return null;}; 
		
		
		//2.泛型定义在方法上：放之前定义       泛型方法 
		//为什么您选择使用泛型方法，而不是将类型 T 添加到类定义呢？（至少）有两种情况应该这样做： 
		                 //2.1   当泛型方法是静态的时，这种情况下不能使用类类型参数。
		//当 T 上的类型约束对于方法真正是局部的时，这意味着没有在相同类的另一个 方法签名中使用相同 类型 T 的约束。通过使得泛型方法的类型参数对于方法是局部的，可以简化封闭类型的签名
		public <T> void test1(T t){}; 
		public static <T> void test2(T t){}; 
		public <T> int test3(T t){ return 0;};
		//public <T> List<T> test4(){ return null;};
		
		
		public   <A> A test6(A e){ return null;};
		
		//3./注意：静态方法不能使用类定义的泛型，而应单独定义泛型
		public static <B> void test7(B e){ };
		public static <EE> void test5(EE e){ };
		
		//4.类型通配符 中引入了类型通配符，这让您可以声明 List<?> 类型的变量
		List<?> lu = null; 
		
		
		/*
		 * 5.有限制类型 
在前一屏泛型方法 的例子中，类型参数V是无约束的或无限制的类型。有时在还没有完全指定类型参数时，需要对类型参数指定附加的约束。 
考虑例子Matrix类，它使用类型参数V，该参数由Number类来限制： 

		 */
		public class Matrix<V extends Number> {  }  
		
		
		
		//6..因此不能使用泛型类型参数所表示的类的构造函数
		//backingArray = new V[DEFAULT_SIZE]; // illegal
		//Set<?> copy = new HashSet<?>(set); // illegal
		//T copy = (T) param.clone(); // illegal    因为 clone()在 Object中是保护访问的
		//T copy = new T(param); // illegal

		public void doSomething(Set<?> set) {

			Set<?> copy = new HashSet<Object>(set); //ok....

			}

		/*
		Collections 类通过一种别扭的方法绕过了这个问题，在 Collections 类编译时会产生类型未检查转换的警告。ArrayList具体实现的构造函数如下： 
		
		还有一种方法就是声明 backingArray为 Object数组，并在使用它的各个地方强制将它转化为 V[]。仍然会看到类型未检查转换警告（与上一种方法一样），
		但是它使一些未明确的假设更清楚了（比如 backingArray不应逃避 ArrayList的实现）。
		
		*
		*/
		
		class ArrayList<V> {

		int DEFAULT_SIZE=10;
		private V[] backingArray;

		public ArrayList() {

		backingArray = (V[]) new Object[DEFAULT_SIZE];

		}

		}

		
		
		
	//	最好的办法是向构造函数传递类文字（Foo.class），这样，该实现就能在运行时知道 T的值。不采用这种方法的原因在于向后兼容性 —— 新的泛型集合类不能与 Collections 
	//	框架以前的版本兼容。

	//	下面的代码中 ArrayList采用了以下方法：
//implements List<V>
	class ArrayList2<V>  {
		int DEFAULT_SIZE=10;
		private V[] backingArray;

		private Class<V> elementType;

		public ArrayList2(Class<V> elementType) {

		elementType = elementType;

		backingArray = (V[]) Array.newInstance(elementType,DEFAULT_SIZE);

		}

		}

		//但是等一等！仍然有不妥的地方，调用 Array.newInstance()时会引起未经检查的类型转换。为什么呢？
		  //同样是由于向后兼容性。Array.newInstance()的签名是：

		//public static Object newInstance(Class<?> componentType, int length)

		//而不是类型安全的：

		//public static<T> T[] newInstance(Class<T> componentType, int length)

		//为何 Array用这种方式进行泛化呢？同样是为了保持向后兼容。要创建基本类型的数组，如 int[]，可以使用适当的包装器类中的 TYPE字段调用

		//Array.newInstance()（对于 int，可以传递 Integer.TYPE作为类文字）。用 Class<T>参数而不是 Class<?>泛化 Array.newInstance()，对于引用类型有更好的类型安全，但是就不能使用 Array.newInstance()创建基本类型数组的实例了。也许将来会为引用类型提供新的 newInstance()版本，这样就两者兼顾了。
		
		
		  
		  
	//	  Collections 框架作为良好类设计的例子被广泛效仿，但是它的设计受到向后兼容性约束，所以这些地方值得您注意，不要盲目效仿。

	//	  首先，常常被混淆的泛型 Collections API 的一个重要方面是 containsAll()、removeAll()和 retainAll()的签名。您可能认为 remove()和 removeAll()的签名应该是：

		  interface Collection<E> {

		  public boolean remove(E e); // not really

		  public void removeAll(Collection<? extends E> c); // not really 
		  }

	//	  但实际上却是：
/*
		  interface Collection<E> {

		  public boolean remove(Object o);

		  public void removeAll(Collection<?> c);

		  }
*/
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  //7. 擦除的实现
/*
		  因为泛型基本上都是在 Java 编译器中而不是运行库中实现的，所以在生成字节码的时候，差不多所有关于泛型类型的类型信息都被“擦掉”了。
		  换句话说，编译器生成的代码与您手工编写的不 用泛型、检查程序的类型安全后进行强制类型转换所得到的代码基本相同。
		  与 C++ 不同，
		  List<Integer>和 List<String>是同一个类（虽然是不同的类型但都是 List<?>的子类型，与以前的版本相比，
		  在 JDK 5.0 中这是一个更重要的区别）。

		  擦除意味着一个类不能同时实现 Comparable<String>和 Comparable<Number>，因为事实上两者都在同一个接口中，
		  指定同一个 compareTo()方法。声明

		  DecimalString类以便与 String与 Number比较似乎是明智的，但对于 Java 编译器来说，这相当于对同一个方法进行了两次声明：

		  public class DecimalString implements Comparable<Number>,

		  Comparable<String>

		  { ... } // nope

		  擦除的另一个后果是，对泛型类型参数是用强制类型转换或者 instanceof毫无意义。下面的代码完全不会改善代码的类型安全性：

		  public <T> T naiveCast(T t, Object o) { return (T) o; }

		  编译器仅仅发出一个类型未检查转换警告，因为它不知道这种转换是否安全。naiveCast()方法实际上根本不作任何转换，T直接被替换为 Object，
		  与期望的相反，传入的对象被强制转换为 Object。

		  擦除也是造成上述构造问题的原因，即不能创建泛型类型的对象，因为编译器不知道要调用什么构造函数。如果泛型类需要构造用泛型类型参数来指定类型的对象，
		  那么构造函数应该接受类文字（Foo.class）并将它们保存起来，以便通过反射创建实例。
*/  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  //8.Class<T>

//类 Class 已经泛型化了，但是很多人一开始都感觉其泛型化的方式很混乱。Class<T> 中类型参数 T 的含义是什么？事实证明它是所引用的类接口。怎么会是这样的呢？那是一个循环推理？如果不是的话，为什么这样定义它？

//在以前的 JDK 中，Class.newInstance() 方法的定义返回 Object，您很可能要将该返回类型强制转换为另一种类型：
class Class { 
Object newInstance(){}; 
} 


//但是使用泛型，您定义 Class.newInstance() 方法具有一个更加特定的返回类型：
class Class2<T> { 
T newInstance(){}; 
} 


/*如何创建一个 Class<T> 类型的实例？就像使用非泛型代码一样，有两种方式：调用方法 Class.forName() 或者使用类常量 X.class。Class.forName()
被定义为返回 Class<?>。另一方面，类常量 X.class 被定义为具有类型 Class<X>，所以 String.class 是 Class<String> 类型的。

让 Foo.class 是 Class<Foo> 类型的有什么好处？大的好处是，通过类型推理的魔力，可以提高使用反射的代码的类型安全。另外，还不需要将 Foo.class.newInstance() 
强制类型转换为 Foo。

考虑一个方法，它从数据库检索一组对象，并返回 JavaBeans 对象的一个集合。您通过反射来实例化和初始化创建的对象，但是这并不意味着类型安全必须完全被抛至脑后。考虑下面这个方法：
 */

public static<T> List<T> getRecords(Class<T> c, Selector s) { 
// Use Selector to select rows 
List<T> list = new ArrayList<T>(); 

for (/* iterate over results */) { 
    T row = c.newInstance(); 
    // use reflection to set fields from result 
    list.add(row); 
} 
return list; 
} 


//可以像下面这样简单地调用该方法：
 
//List<FooRecord> l = getRecords(FooRecord.class, fooSelector); 

/*
编译器将会根据 FooRecord.class 是 Class<FooRecord> 类型的这一事实，推断 getRecords() 的返回类型。您使用类常量来构造新的实例并提供编译器在类型检查中要用到的类型信息。
*/
	


//9。。。所以 extends Enum<E> 部分如何理解？该部分又具有两个部分。第一部分指出，作为 Enum 的类型参数的类本身必须是 Enum 的子类型，所以您不能声明一个类 X 扩展 Enum<Integer>
class Enum<E extends Enum<E>> { } ;

}
























/*
 * 泛型方法 
（在类型参数 一节中）您已经看到，通过在类的定义中添加一个形式类型参数列表，可以将类泛型化。方法也可以被泛型化，不管它们定义在其中的类是不是泛型化的。 
泛型类在多个方法签名间实施类型约束。在List<V>中，类型参数V出现在get()、add()、contains()等方法的签名中。当创建一个Map<K, V>类型的变量时，您就在方法之间宣称一个类型约束。您传递给add()的值将与get()返回的值的类型相同。 
类似地，之所以声明泛型方法，一般是因为您想要在该方法的多个参数之间宣称一个类型约束。例如，下面代码中的ifThenElse()方法，根据它的第一个参数的布尔值，它将返回第二个或第三个参数：

 */

//public class Matrix<V extends Number> { ... } 

//类型通配符 中引入了类型通配符，这让您可以声明 List<?> 类型的变量
//List<?> lu = li; 

/*
 * 注意，在本例中，必须指定两次类型参数。一次是在声明变量map的类型时，另一次是在选择HashMap类的参数化以便可以实例化正确类型的一个实例时。 
编译器在遇到一个Map<String, String>类型的变量时，知道K和V现在被绑定为String，因此它知道在这样的变量上调用Map.get()将会得到String类型。 
除了异常类型、枚举或匿名内部类以外，任何类都可以具有类型参数。 
命名类型参数 
推荐的命名约定是使用大写的单个字母名称作为类型参数。这与C++ 约定有所不同（参阅附录 A：与 C++ 模板的比较），并反映了大多数泛型类将具有少量类型参数的假定。对于常见的泛型模式，推荐的名称是： 

* K —— 键，比如映射的键。 
* V —— 值，比如 List 和 Set 的内容，或者 Map 中的值。 
* E —— 异常类。 
* T —— 泛型。 

泛型不是协变的 
关于泛型的混淆，一个常见的来源就是假设它们像数组一样是协变的。其实它们不是协变的。List<Object>不是List<String>的父类型。 
如果 A 扩展 B，那么 A 的数组也是 B 的数组，并且完全可以在需要B[]的地方使用A[]： 
 */
 