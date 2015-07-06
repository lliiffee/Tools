package com.fung.partern.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
 

public class Reactor implements Runnable {
	final Selector selector;
	final ServerSocketChannel serverSocket;

	Reactor(int port) throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
		//向selector注册该channel
		SelectionKey sk = serverSocket.register(selector,
				SelectionKey.OP_ACCEPT);
		System.out.println("-->Start serverSocket.register!");

	 //利用sk的attache功能绑定Acceptor 如果有事情，触发Acceptor
		sk.attach(new Acceptor());
		System.out.println("-->attach(new Acceptor()!");
	}

	/*
	 * Alternatively, use explicit SPI provider: SelectorProvider p =
	 * SelectorProvider.provider(); selector = p.openSelector(); serverSocket =
	 * p.openServerSocketChannel();
	 */

	// class Reactor continued
	public void run() { // normally in a new Thread
		try {
			while (!Thread.interrupted()) {
				selector.select();
				Set selected = selector.selectedKeys();
				Iterator it = selected.iterator();
				while (it.hasNext())
					//来一个事件 第一次触发一个accepter线程
			        //以后触发SocketReadHandler
					dispatch((SelectionKey) (it.next()));
				selected.clear();
			}
		} catch (IOException ex) { /* ... */
		}
	}

	////运行Acceptor或SocketReadHandler
	void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment());
		if (r != null)
			r.run();
	}

	// class Reactor continued
	class Acceptor implements Runnable { // inner
		public void run() {
			try {
				SocketChannel c = serverSocket.accept();
				if (c != null)
					//调用Handler来处理channel
			 new Handler(selector, c);
			} catch (IOException ex) { /* ... */
			}
		}
	}

}
