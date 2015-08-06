package com.fung.file;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class ClientNIODemo {

	 public static void main(String[] args) {  
	        try {  
	            //创建客户端通道  
	            SocketChannel client=SocketChannel.open();  
	            Selector selector= Selector.open();  
	            //设置成非阻塞  
	            client.configureBlocking(false);  
	            client.connect(new InetSocketAddress("127.0.0.1", 12345));  
	            //注册监听Connect  
	            client.register(selector, SelectionKey.OP_CONNECT);  
	            boolean complate=true;  
	            while(complate){  
	                try {  
	                    selector.select();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                    break;  
	                }  
	                Set<SelectionKey> keys= selector.selectedKeys();  
	                Iterator<SelectionKey> iterator = keys.iterator();  
	                  
	                while(iterator.hasNext()){  
	                    SelectionKey key=iterator.next();  
	                    iterator.remove();  
	  
	                    if(key.isConnectable()&&client.finishConnect()){  
	                        System.out.println("=============connectable");  
	                        key.interestOps(SelectionKey.OP_WRITE);  
	                    }  
	                    if(key.isReadable()){  
	                        System.out.println("=============readable");  
	                        SocketChannel channel=(SocketChannel) key.channel();  
	                        ByteBuffer buffer=ByteBuffer.allocate(10);  
	                        StringBuilder feedback=new StringBuilder();  
	                          
	                        while(true){  
	                            int res=channel.read(buffer);  
	                            if(res==-1||res==0){  
	                                break;  
	                            }else if(res>0){  
	                                buffer.flip();  
	                                feedback.append(new String(buffer.array(),Charset.defaultCharset()));  
	                                buffer.clear();  
	                            }  
	                        }  
	                          
	                        System.out.println("[client]:"+feedback);  
	                        complate=false;  
	                    }  
	                    if(key.isWritable()){  
	                        System.out.println("=============writeable");  
	                        SocketChannel channel=(SocketChannel) key.channel();  
	                        ByteBuffer buffer=ByteBuffer.allocate(100);  
	                        String message="send a message:客户端发消息了！";  
	                        byte[] context=message.getBytes();  
	                        buffer.put(context);  
	                        buffer.flip();  
	                        channel.write(buffer);  
	                        key.interestOps(SelectionKey.OP_READ);  
	                    }  
	                }  
	            }  
	            client.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }   
	    } 

}


/*
 * 从以上2个列子可以可以看出： 
        对于BIO如果服务端读取客户端发来的信息，如果用while(true)进行循环读取，读到-1时截止，那么必须要将客服端进行关闭，否则客户端和服务端就都等待对方进行发送消息，或者关闭。也就卡死在那了。
        对于NIO来说，不管服务端or客户端，只要没有数据读取直接进行返回，返回为0，如果关闭则返回-1。从而可以进行其他的逻辑。 这也就是非阻塞的概念。

        对比发现，NIO多了 Channel 和 Buffer 这2个重要的类！后续我们将着重进行介绍 “通道”“缓存区”
 */
