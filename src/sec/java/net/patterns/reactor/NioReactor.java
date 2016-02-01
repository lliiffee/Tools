package net.patterns.reactor;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**异步时间多路复用器， 1 . 初始化 Dispatcher.  2 .多个句柄(handles)可以被注册到reactor ，
 * 且他为所有这些句柄事件而阻塞，当任何这些注册了的句柄发生事件，它就会通过disbatcher 发布事件。
 * 
 * This class acts as Synchronous Event De-multiplexer and Initiation Dispatcher of Reactor pattern. Multiple handles
 * i.e. {@link AbstractNioChannel}s can be registered to the reactor and it blocks for events from all these handles.
 * Whenever an event occurs on any of the registered handles, it synchronously de-multiplexes the event which can be any
 * of read, write or accept, and dispatches the event to the appropriate {@link ChannelHandler} using the
 * {@link Dispatcher}.
 * 
 * <p>
 * Implementation: A NIO reactor runs in its own thread when it is started using {@link #start()} method.
 * {@link NioReactor} uses {@link Selector} for realizing Synchronous Event De-multiplexing.
 * 
 * <p>
 * NOTE: This is one of the ways to implement NIO reactor and it does not take care of all possible edge cases which are
 * required in a real application. This implementation is meant to demonstrate the fundamental concepts that lie behind
 * Reactor pattern.
 */

public class NioReactor {
	/*
	 * java NIO采用了双向通道（channel）进行数据传输，而不是单向的流（stream），在通道上可以注册我们感兴趣的事件
	 * 使用selector 实现异步事件多路复用
	 * 服务端和客户端各自维护一个管理通道的对象，我们称之为selector，该对象能检测一个或多个通道 (channel) 上的事件
	 * 服务端和客户端各自维护一个管理通道的对象，我们称之为selector，该对象能检测一个或多个通道 (channel) 上的事件。
	 * 我们以服务端为例，如果服务端的selector上注册了读事件，某时刻客户端给服务端发送了一些数据，阻塞I/O这时会调用read()方法阻塞地读取数据，
	 * 而NIO的服务端会在selector中添加一个读事件。服务端的处理线程会轮询地访问selector，如果访问selector时发现有感兴趣的事件到达，
	 * 则处理这些事件，如果没有感兴趣的事件到达，则处理线程会一直阻塞直到感兴趣的事件到达为止
	 */
	private final Selector selector;  
	/*
	 * Represents the event dispatching strategy.
	 */
	private final Dispatcher dispatcher;
	
	
	  /**
	   * All the work of altering the SelectionKey operations and Selector operations are performed in the context of main
	   * event loop of reactor. 
	   * 
	   * So when any channel needs to change its readability or writability, a new command is added
	   * in the command queue and then the event loop picks up the command and executes it in next iteration.
	   */
	private final Queue<Runnable> pendingCommands = new ConcurrentLinkedQueue<>();
	private final ExecutorService reactorMain = Executors.newSingleThreadExecutor();
	
	public NioReactor (Dispatcher dispatcher)throws IOException
	{
		this.dispatcher=dispatcher;
		this.selector=Selector.open();
	}
	
}
