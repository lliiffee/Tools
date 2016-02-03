package com.fung.partern.threadloacl;

import java.util.Random;

public class ThreadLocalDemo implements Runnable {  
    //创建线程局部变量studentLocal，在后面你会发现用来保存Student对象  
    private final static ThreadLocal studentLocal = new ThreadLocal();  
   
    public static void main(String[] agrs) {  
        ThreadLocalDemo td = new ThreadLocalDemo();  
        Thread t1 = new Thread(td, "a");  
        Thread t2 = new Thread(td, "b");  
        t1.start();  
        t2.start();  
    }  
   
    public void run() {  
        accessStudent();  
    }  
   
    /** 
     * 示例业务方法，用来测试 
     */  
    public void accessStudent() {  
        //获取当前线程的名字  
        String currentThreadName = Thread.currentThread().getName();  
        System.out.println(currentThreadName + " is running!");  
        //产生一个随机数并打印  
        Random random = new Random();  
        int age = random.nextInt(100);  
        System.out.println("thread " + currentThreadName + " set age to:" + age);  
        //获取一个Student对象，并将随机数年龄插入到对象属性中  
        SessionSingleStudent student = SessionSingleStudent.getInstance();  
        student.setAge(age);  
        System.out.println("thread " + currentThreadName + " first read age is:" + student.getAge());  
        try {  
            Thread.sleep(500);  
        }  
        catch (InterruptedException ex) {  
            ex.printStackTrace();  
        }  
        System.out.println("thread " + currentThreadName + " second read age is:" + student.getAge());  
    }  
   
//    protected Student getStudent() {  
//        //获取本地线程变量并强制转换为Student类型  
//        Student student = (Student) studentLocal.get();  
//        //线程首次执行此方法的时候，studentLocal.get()肯定为null  
//        if (student == null) {  
//            //创建一个Student对象，并保存到本地线程变量studentLocal中  
//            student = new Student();  
//            studentLocal.set(student);  
//        }  
//        return student;  
//    }  
}  

/*
ThreadLocal使用场合主要解决多线程中数据数据因并发产生不一致问题。ThreadLocal为每个线程的中并发访问的数据提供一个副本，通过访问副本来运行业务，这样的结果是耗费了内存，
但大大减少了线程同步所带来性能消耗，也减少了线程并发控制的复杂度。
 
ThreadLocal不能使用原子类型，只能使用Object类型。ThreadLocal的使用比synchronized要简单得多。
 
ThreadLocal和Synchonized都用于解决多线程并发访问。但是ThreadLocal与synchronized有本质的区别。synchronized是利用锁的机制，使变量或代码块在某一时该只能被一个线程访问。
而ThreadLocal为每一个线程都提供了变量的副本，使得每个线程在某一时间访问到的并不是同一个对象，这样就隔离了多个线程对数据的数据共享。而Synchronized却正好相反，它用于在多个线程间通信时能够获得数据共享。
 
Synchronized用于线程间的数据共享，而ThreadLocal则用于线程间的数据隔离。
 
当然ThreadLocal并不能替代synchronized,它们处理不同的问题域。Synchronized用于实现同步机制，比ThreadLocal更加复杂。


ThreadLocal使用的一般步骤


1、在多线程的类（如ThreadDemo类）中，创建一个ThreadLocal对象threadXxx，用来保存线程间需要隔离处理的对象xxx。
2、在ThreadDemo类中，创建一个获取要隔离访问的数据的方法getXxx()，在方法中判断，若ThreadLocal对象为null时候，应该new()一个隔离访问类型的对象，并强制转换为要应用的类型。
3、在ThreadDemo类的run()方法中，通过getXxx()方法获取要操作的数据，这样可以保证每个线程对应一个数据对象，在任何时刻都操作的是这个对象。
*/
 