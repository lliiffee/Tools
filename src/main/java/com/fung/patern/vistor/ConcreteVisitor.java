package com.fung.patern.vistor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

public class ConcreteVisitor implements Visitor{

	//在本方法中,我们实现了对Collection的元素的成功访问
	 public void visitCollection(Collection collection) {
	  Iterator iterator = collection.iterator();
	  while (iterator.hasNext()) {
	   Object o = iterator.next();
	   if (o instanceof Visitable)
	    ((Visitable)o).accept(this);
	  } 
	 }

	 public void visitStringElement(StringElement stringE) {
	  System.out.println("'"+stringE.getValue()+"'");
	 } 
	 public void visitFloatElement(FloatElement floatE){
	  System.out.println(floatE.getValue().toString()+"f");
	 }

	 public void defaultVisit(Object o)
	   {
	      System.out.println(o.toString());
	   }
	 
	 public void visit(Object o)  {
	      // Class.getName() returns package information as well.
	      // This strips off the package information giving us
	      // just the class name
	      String methodName = o.getClass().getName();
	      methodName = "visit"+
	                   methodName.substring(methodName.lastIndexOf('.')+1);
	      System.out.println(methodName);
	      // Now we try to invoke the method visit<methodName>
	      try {
	         // Get the method visitFoo(Foo foo)
	         Method m = getClass().getMethod(methodName,
	            new Class[] { o.getClass() });
	         // Try to invoke visitFoo(Foo foo)
	         m.invoke(this, new Object[] { o });
	      } catch (NoSuchMethodException e) {
	         // No method, so do the default implementation
	    	  defaultVisit(o);
	      } catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }

	
//	 protected Method getMethod(Class c) {
//		   Class newc = c;
//		   Method m = null;
//		   // Try the superclasses
//		   while (m == null && newc != Object.class) {
//		      String method = newc.getName();
//		      method = "visit" + method.substring(method.lastIndexOf('.') + 1);
//		      try {
//		         m = getClass().getMethod(method, new Class[] {newc});
//		      } catch (NoSuchMethodException e) {
//		         newc = newc.getSuperclass();
//		      }
//		   }
//		   // Try the interfaces.  If necessary, you
//		   // can sort them first to define 'visitable' interface wins
//		   // in case an object implements more than one.
//		   if (newc == Object.class) {
//		      Class[] interfaces = c.getInterfaces();
//		      for (int i = 0; i < interfaces.length; i++) {
//		         String method = interfaces[i].getName();
//		         method = "visit" + method.substring(method.lastIndexOf('.') + 1);
//		         try {
//		            m = getClass().getMethod(method, new Class[] {interfaces[i]});
//		         } catch (NoSuchMethodException e) {}
//		      }
//		   }
//		   if (m == null) {
//		      try {
//		         m = thisclass.getMethod("visitObject", new Class[] {Object.class});
//		      } catch (Exception e) {
//		          // Can't happen
//		      }
//		   }
//		   return m;
//		}

}
