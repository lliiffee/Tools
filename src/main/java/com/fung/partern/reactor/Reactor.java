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


/*
import java.io.IOException;  
import java.net.InetSocketAddress;  
import java.nio.ByteBuffer;  
import java.nio.channels.SelectionKey;  
import java.nio.channels.Selector;  
import java.nio.channels.ServerSocketChannel;  
import java.nio.channels.SocketChannel;  
import java.util.Iterator;  
  
public class Reactor implements Runnable {  
  
    private ServerSocketChannel serverSocketChannel = null;  
  
    private Selector            selector            = null;  
  
    public Reactor() {  
        try {  
            selector = Selector.open();  
            serverSocketChannel = ServerSocketChannel.open();  
            serverSocketChannel.configureBlocking(false);  
            serverSocketChannel.socket().bind(new InetSocketAddress(8888));  
            SelectionKey selectionKey = serverSocketChannel.register(selector,  
                SelectionKey.OP_ACCEPT);  
            selectionKey.attach(new Acceptor());  
            System.out.println("服务器启动正常!");  
        } catch (IOException e) {  
            System.out.println("启动服务器时出现异常!");  
            e.printStackTrace();  
        }  
    }  
  
    public void run() {  
        while (true) {  
            try {  
                selector.select();  
  
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();  
                while (iter.hasNext()) {  
                    SelectionKey selectionKey = iter.next();  
                    dispatch((Runnable) selectionKey.attachment());  
                    iter.remove();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    public void dispatch(Runnable runnable) {  
        if (runnable != null) {  
            runnable.run();  
        }  
    }  
  
    public static void main(String[] args) {  
        new Thread(new Reactor()).start();  
    }  
  
    class Acceptor implements Runnable {  
        public void run() {  
            try {  
                SocketChannel socketChannel = serverSocketChannel.accept();  
                if (socketChannel != null) {  
                    System.out.println("接收到来自客户端（"  
                                       + socketChannel.socket().getInetAddress().getHostAddress()  
                                       + "）的连接");  
                    new Handler(selector, socketChannel);  
                }  
  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}  
  
class Handler implements Runnable {  
  
    private static final int READ_STATUS  = 1;  
  
    private static final int WRITE_STATUS = 2;  
  
    private SocketChannel    socketChannel;  
  
    private SelectionKey     selectionKey;  
  
    private int              status       = READ_STATUS;  
  
    public Handler(Selector selector, SocketChannel socketChannel) {  
        this.socketChannel = socketChannel;  
        try {  
            socketChannel.configureBlocking(false);  
            selectionKey = socketChannel.register(selector, 0);  
            selectionKey.interestOps(SelectionKey.OP_READ);  
            selectionKey.attach(this);  
            selector.wakeup();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void run() {  
        try {  
            if (status == READ_STATUS) {  
                read();  
                selectionKey.interestOps(SelectionKey.OP_WRITE);  
                status = WRITE_STATUS;  
            } else if (status == WRITE_STATUS) {  
                process();  
                selectionKey.cancel();  
                System.out.println("服务器发送消息成功!");  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void read() throws IOException {  
        ByteBuffer buffer = ByteBuffer.allocate(1024);  
        socketChannel.read(buffer);  
        System.out.println("接收到来自客户端（" + socketChannel.socket().getInetAddress().getHostAddress()  
                           + "）的消息：" + new String(buffer.array()));  
    }  
  
    public void process() throws IOException {  
        String content = "Hello World!";  
        ByteBuffer buffer = ByteBuffer.wrap(content.getBytes());  
        socketChannel.write(buffer);  
    }  
}  



 protected void readDataFromSocket(SelectionKey key) throws Exception {  
        SocketChannel socketChannel = (SocketChannel) key.channel();  
        int count;  
        buffer.clear(); // Empty buffer  
        // Loop while data is available; channel is nonblocking  
        while ((count = socketChannel.read(buffer)) > 0) {  
            buffer.flip(); // Make buffer readable  
            // Send the data; don't assume it goes all at once  
            while (buffer.hasRemaining()) {  
                socketChannel.write(buffer);  
            }  
            // WARNING: the above loop is evil. Because   注意出于演示的目的，没有注册写事件，这样的话会导致一个问题就如上面注释中提到的邪恶代码
            // it's writing back to the same nonblocking  
            // channel it read the data from, this code can  
            // potentially spin in a busy loop. In real life  
            // you'd do something more useful than this.  
            buffer.clear(); // Empty buffer  
  
        }  
        if (count < 0) {  
            // Close channel on EOF, invalidates the key  
            socketChannel.close();  
        }  
    }  

这段代码摘自JAVA NIO这本书，代码做了很简单的几件事
起一个server socket监听子1234端口
起一个selector
将server socket注册到epoll,感兴趣的事件为SelectionKey.OP_ACCEPT，即来了新的连接
开始一个轮询过程，不断的通过selector来探测到底有没有新的网络事件
如果有监听事件，那么取出连接socket，然后给将这个连接socket注册到epoll,感兴趣的事件为​ SelectionKey.OP_READ
如果有读事件，那么就把这个读的内容写回
注意出于演示的目的，没有注册写事件，这样的话会导致一个问题就如上面注释中提到的邪恶代码
​3） selector的机制

selector最关键的上个点就是初始化/注册/以及select过程，以上面的代码为例，分别说明这3个关键点

3.1）selector的初始化
 Selector selector = Selector.open();
根据操作系统实例化不同Selector(通常见sun.nio.ch.DefaultSelectorProvider.create())
常见的Linux且 kernels >= 2.6，会使用sun.nio.ch.EPollSelectorImpl
实例化EPollSelectorImpl
实例化EPollArrayWrapper
调用epollCreate产生epoll FD
实例化AllocatedNativeObject，得到上文提到的pollArray
3.2）注册
    ServerSocketChannel.register​
如果该通道曾经注册过那么
SelectionKeyImpl.interestOps[SelectionKey.OP_ACCEPT] -->
SelectionKeyImpl.nioInterestOps[SelectionKey.OP_ACCEPT]-->
ServerSocketChannelImpl.translateAndSetInterestOps[SelectionKey.OP_ACCEPT]--> :
		将SelectionKey.OP_ACCEPT转化为PollArrayWrapper.POLLIN
EPollSelectorImpl.putEventOps[PollArrayWrapper.POLLIN]-->
EPollArrayWrapper.setInterest[fd,PollArrayWrapper.POLLIN] :加入updateList
如果没有注册过
EPollSelectorImpl.register-->：仅仅是将key所对应的fd加入epoll
EPollSelectorImpl.implRegister-->
EPollArrayWrapper.add：加入updateList
将该key加入到keys集合中
SelectionKeyImpl.interestOps：调用栈见上面，功能和上面一样就是更新fd感兴趣的事件
抛开上面的代码细节，注册会
往EPollArrayWrapper的updateList添加记录，updateList会在select的时候使用
如果没有注册过，会将该key加入到keys集合中即所有注册过的key都会在keys中，除非以后取消掉了
应用这边感兴趣的事件为
SelectionKey.OP_READ
SelectionKey.OP_WRITE
SelectionKey.OP_CONNECT
SelectionKey.OP_ACCEPT
底层的epoll接受的事件
PollArrayWrapper.POLLIN
PollArrayWrapper.POLLOUT
PollArrayWrapper.POLLERR
PollArrayWrapper.POLLHUP
PollArrayWrapper.POLLNVAL
PollArrayWrapper.POLLREMOVE​
由于存在上面提到的两种事件类型：应用级别和系统（epoll）级别，所以需要转换一下，见SocketChannelImpl及ServerSocketChannelImpl的translateAndSetInterestOps和translateReadyOps方法，
前者是将应用-->系统，后者是系统->应用
注意到上面的注册实际上分两步
​​现将key转换成一个内部数据结构EPollArrayWrapper$Updator添加到updateList,此时事件为空
再更新EPollArrayWrapper$Updator的事件为感兴趣的事件
为什么要分两步？？
​
3.3）selector.select()
EPollSelectorImpl.doSelect
注销cancelledKeys【已取消的键的集合】中的key
EPollArrayWrapper.poll
EPollArrayWrapper.updateRegistrations:遍历上面的updateList,调用epollCtl真正到向epoll fd注册
调用epollWait等待事件发生,可能会阻塞，返回更新的事件
此时telnet 127.0.0.1 1234发起连接，上面的方法返回
再次注销cancelledKeys【已取消的键的集合】中的key
EPollSelectorImpl.updateSelectedKeys
如果selectedKeys【已选择的键的集合】包含该键
ServerSocketChannelImpl.translateReadyOps
将PollArrayWrapper.POLLIN转化为SelectionKey.OP_ACCEPT
更新readyOps
判断老的readyOps是否和新的readyOps，如果不一致事件数+1
如果selectedKeys【已选择的键的集合】不包含该键
ServerSocketChannelImpl.translateReadyOps
将该键填入selectedKeys
事件数+1
如果事件数>1,遍历事件对应的key，开始相应处理。。。
Java NIO这本书提到“一旦键 被放置于选择器的已选择的键的集合中，它的 ready 集合（即readyOps）将是累积的。比特位只会被设置，不会被 清理”，实际并非如此，
就我的理解和观察的结果，readyOps并不会积累而是每次更新，不明白为什么作者会这么说？？？，无论如何对整体的程序理解不会有影响
java使用epoll默认会使用水平触发，即如果有事件发生，如果你不处理，那么下次还会触发
但经过java 中EPollSelectorImpl实现之后有了小小的变化，如果某个事件发生，你不做任何处理，那么下次调用select的时候，虽然底层epoll仍然会返回事件，
但上面的代码会判断本次事件和上次事件是否一致，如果是一样，java认为没有事件发生，如果要做到一致，必须将selectedKeys中的key删掉，否则会有差别，
所以请注意selectedKeys删除的重要性！否则会死循环！
上面已经解释了ServerSocketChannel的注册，以及select过程，代码样例还有SocketChannel的注册和select，其实和前面很雷同就不再赘述了

*/