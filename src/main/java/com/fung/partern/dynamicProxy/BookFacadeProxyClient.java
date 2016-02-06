package com.fung.partern.dynamicProxy;

public class BookFacadeProxyClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BookFacadeProxyWithJDK proxy = new BookFacadeProxyWithJDK();  
        BookFacade bookProxy = (BookFacade) proxy.bind(new BookFacadeImpl()); //动态生成继承了 bookFacade 的子类。。
        bookProxy.addBook();  

        System.out.println("######################");
        BookFacadeProxyCglib cglib=new BookFacadeProxyCglib();  
        BookFacadeService bookCglib=(BookFacadeService)cglib.getInstance(new BookFacadeService());  
        //bookCglib实际是由cglib动态生成的 BookFacadeService 子类。
        bookCglib.addBook();  
        bookCglib.removeBook();
        
	}

}
