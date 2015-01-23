package com.fung.concurrent.condition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer {  
    // 可重入的互斥锁  
    final Lock lock = new ReentrantLock();  
    // 表示"缓冲区没满"条件  
    final Condition notFull = lock.newCondition();  
    // 表示"缓冲区非空"条件  
    final Condition notEmpty = lock.newCondition();   
    // 存储空间  
    //final Object[] items = new Object[100];   
    final ArrayList<Object> list = new ArrayList<Object>();
    final Queue<Object> queue = new LinkedList<Object>();
    final int maxSize = 10;
    int putptr, takeptr, count;  
  
    public void put(Object x) throws InterruptedException {  
    	System.out.println("put 锁上。。");
        lock.lock();  
        try {  
            while (count == maxSize)  
            {
            	System.out.println("满了满了。。");
                notFull.await(); 
            }
            queue.offer(x);
            ++count;  
              
            notEmpty.signal();  
        } finally {  
        	System.out.println("put 开锁。。");
            lock.unlock();  
        }  
    }  
  
    public Object take() throws InterruptedException {  
        lock.lock();  
        try {  
            while (count == 0)  
            {
            	System.out.println("kong了kong了。。");
                notEmpty.await();
            }
            
            Object x = queue.poll();
            --count;  
            notFull.signal();  
            return x;  
        } finally {  
        	System.out.println("take 开锁。。");
            lock.unlock();  
        }  
    }  
  
    public static void main(String[] args) {  
        int threadCount = 5;  
      
        BoundedBuffer b = new BoundedBuffer();  
                  
          
        for(int i=0;i<threadCount;i++)  
        {  
            //if(i%2 == 0)
        	if(i==2)
            {
                new PutThread(b).start();  
            }
            new TakeThread(b).start();  
        }  
  
    } 
    
    public static class PutThread extends Thread  
    {  
        private final BoundedBuffer b;  
        private int put = 0;  
          
        public PutThread(BoundedBuffer b)  
        {  
            this.b = b;  
        }  
        @Override  
        public void run() {  
            try {  
                String data = null;  
                while(true)  
                {     
                    data = "data" + (++put);  
                    System.out.println(data);  
                    b.put(data);      
                    Thread.sleep(100);  
                }  
            } catch (InterruptedException e) {  
                //e.printStackTrace();  
                Thread.currentThread().interrupt();               
            }  
        }  
    }  
      
    public static class TakeThread extends Thread  
    {  
        private final BoundedBuffer b;  
          
        public TakeThread(BoundedBuffer b)  
        {  
            this.b = b;  
        }  
        @Override  
        public void run() {  
            try {  
                while(true)  
                {                     
                        System.out.println(b.take());                     
                }  
            } catch (InterruptedException e) {  
                //e.printStackTrace();  
                Thread.currentThread().interrupt();               
            }  
        }  
    }  
}

/*
 * Condition的基本使用如下: 
* Condition是个接口，基本的方法就是await()和signal()方法； 
* Condition依赖于Lock接口，生成一个Condition的基本代码是lock.newCondition() 
* 调用Condition的await()和signal()方法，都必须在lock保护之内，就是说必须在lock.lock()和lock.unlock之间才可以 
* 和Object.wait()方法一样，每次调用Condition的await()方法的时候，当前线程就自动释放了对当前锁的拥有权
* */

/*
代码意思不复杂，一个有界的buffer,里面是个数组，可以往里面放数据和取数据； 
由于该buffer被多个线程共享，所以每次放和取操作的时候都用一个lock保护起来； 
每次取数据(take)的时候， 
a. 如果当前个数是0(用一个count计数)， 那么就调用notEmpty.await等待，锁就释放了； 
b. 取数据的索引专门有一个，每次向前一步； 如果到头了就从0开始循环使用 
c.如果有数据，那就取一个数据，将count减1，同时调用notfull.signal()， 

每次放数据(put)的时候 
a.如果count和length相等，也就是满了，那就调用notFull.await等待，释放了锁； 等待有一些take()调用完成之后才会进入 
b. 放数据也有一个索引putptr, 放入数据； 如果到头了也从0开始循环使用 
c. 调用notempty.signal()； 如果有线程在take()的时候await住了，那么就会被通知到，可以继续进行操作
 * */

