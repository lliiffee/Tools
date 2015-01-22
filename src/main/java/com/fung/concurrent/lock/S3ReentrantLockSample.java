package com.fung.concurrent.lock;
 
import java.util.concurrent.locks.ReentrantLock;

public class S3ReentrantLockSample {

	public static void main(String[] args) {
		testSynchronized();
		//testReentrantLock();
	}

	public static void testReentrantLock() {
		final SampleSupport1 support = new SampleSupport1();
		Thread first = new Thread(new Runnable() {
			public void run() {
				try {
					support.doSomething();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		Thread second = new Thread(new Runnable() {
			public void run() {
				try {
					support.doSomething();
				}
				catch (InterruptedException e) {
					System.out.println("Second Thread Interrupted without executing counter++,beacuse it waits a long time.");
				}
			}
		});

		executeTest(first, second);
	}

	public static void testSynchronized() {
		final SampleSupport2 support2 = new SampleSupport2();

		Runnable runnable = new Runnable() {
			public void run() {
				support2.doSomething();
			}
		};

		Thread third = new Thread(runnable);
		Thread fourth = new Thread(runnable);

		executeTest(third, fourth);
	}

	/**
	 * Make thread a run faster than thread b,
     * then thread b will be interruted after about 1s.
	 * @param a
	 * @param b
	 */
	public static void executeTest(Thread a, Thread b) {
		a.start();
		try {
			Thread.sleep(100);
			b.start(); // The main thread sleep 100ms, and then start the second thread.

			Thread.sleep(1000);
    // 1s later, the main thread decided not to allow the second thread wait any longer.
			b.interrupt(); 
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

abstract class SampleSupport {

	protected int counter;

	/**
	 * A simple countdown,it will stop after about 5s. 
	 */
	public void startTheCountdown() {
		long currentTime = System.currentTimeMillis();
		for (;;) {
			long diff = System.currentTimeMillis() - currentTime;
			if (diff > 5000) {
				break;
			}
		}
	}
}

class SampleSupport1 extends SampleSupport {

	private final ReentrantLock lock = new ReentrantLock();

	public void doSomething() throws InterruptedException {
		lock.lockInterruptibly(); // (1)
		System.out.println(Thread.currentThread().getName() + " will execute counter++.");
		startTheCountdown();
		try {
			counter++;
		}
		finally {
			lock.unlock();
		}
	}
}

class SampleSupport2 extends SampleSupport {

	public synchronized void doSomething() {
		System.out.println(Thread.currentThread().getName() + " will execute counter++.");
		startTheCountdown();
		counter++;
	}
}

/*
 *  在这个例子中，辅助类SampleSupport提供一个倒计时的功能startTheCountdown()，这里倒计时5s左右。SampleSupport1,SampleSupport2
 *  继承其并分别的具有doSomething()方法，任何进入方法的线程会运行5s左右之后counter++然后离开方法释放锁。SampleSupport1是使用ReentrantLock
 *  机制，SampleSupport2是使用synchronized机制。 

    testSynchronized()和testReentrantLock()都分别开启两个线程执行测试方法executeTest()，这个方法会让一个线程先启动，
    另一个过100ms左右启动，并且隔1s左右试图中断后者。结果正如之前提到的第二点：interrupt()对于synchronized是没有作用的,它依然会等
    待5s左右获得锁执行counter++;而ReentrantLock机制可以保证在线程还未获得并且试图获得锁时如果发现线程中断，则抛出异常清除中断标记退出竞争。
    所以testReentrantLock()中second线程不会继续去竞争锁,执行异常内的打印语句后线程运行结束。 
来源：http://yanxuxin.iteye.com/blog/566713
*/
