package com.fung.concurrent.clhlock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class CLHLock2 {
	  public static class CLHNode {
	        private boolean isLocked = true; // 默认是在等待锁
	    }

	    @SuppressWarnings("unused" )
	    private volatile CLHNode tail ;
	    private static final AtomicReferenceFieldUpdater<CLHLock2, CLHNode> UPDATER = AtomicReferenceFieldUpdater
	                  . newUpdater(CLHLock2.class, CLHNode .class , "tail" );

	    public void lock(CLHNode currentThreadCLHNode) {
	        CLHNode preNode = UPDATER.getAndSet( this, currentThreadCLHNode); // 转载人注释: 把this里的"tail" 值设置成currentThreadCLHNode
	        if(preNode != null) {//已有线程占用了锁，进入自旋
	            while(preNode.isLocked ) {
	            }
	        }
	    }

	    public void unlock(CLHNode currentThreadCLHNode) {
	        // 如果队列里只有当前线程，则释放对当前线程的引用（for GC）。
	        if (!UPDATER .compareAndSet(this, currentThreadCLHNode, null)) {
	            // 还有后续线程
	            currentThreadCLHNode. isLocked = false ;// 改变状态，让后续线程结束自旋
	        }
	    }
}
