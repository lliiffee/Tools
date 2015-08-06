package com.fung.file;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerNIODemo {

	public static void main(String[] args) {  
        ServerSocketChannel serverChannel;  
        Selector selector;  
          
        try{  
            //服务端的准备  
            serverChannel= ServerSocketChannel.open();  
            ServerSocket serverSocket=serverChannel.socket();  
            serverSocket.bind(new InetSocketAddress(12345));  
            serverChannel.configureBlocking(false);  
            selector=Selector.open();  
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);  
        } catch (IOException e){  
            e.printStackTrace();  
            return;  
        }  
          
        while(true){  
            try {  
                selector.select();  
            } catch (IOException e) {  
                e.printStackTrace();  
                break;  
            }  
            Set<SelectionKey> readKeys = selector.selectedKeys();  
            Iterator<SelectionKey> iterator = readKeys.iterator();  
              
            while(iterator.hasNext()){  
                SelectionKey key=iterator.next();  
                iterator.remove();  
                  
                try {  
                    if(key.isAcceptable()){  
                        System.out.println("=============accept");  
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();  
                        //接受客户端的请求  
                        SocketChannel client=server.accept();  
                        //客户端 服务端 全都设置为 非阻塞  
                        client.configureBlocking(false);  
                        client.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocate(100));  
                    }  
                    if(key.isReadable()){  
                        System.out.println("=============read");  
                        SocketChannel client=(SocketChannel) key.channel();  
                        ByteBuffer buffer=ByteBuffer.allocate(10);  
                        WritableByteChannel out = Channels.newChannel(System.out);  
                          
                        while(true){  
                            int res=client.read(buffer);  
                            if(res==-1||res==0){  
                                break;  
                            }else if(res>0){  
                                buffer.flip();  
                                out.write(buffer);  
                                buffer.clear();  
                            }  
                        }  
                        System.out.println();  
                        key.interestOps(SelectionKey.OP_WRITE);  
                    }  
                    if(key.isWritable()){  
                        System.out.println("=============write");  
                        SocketChannel client=(SocketChannel) key.channel();  
                        ByteBuffer buffer=(ByteBuffer) key.attachment();  
                          
                        String message="send a message:服务端发消息了！";  
                        byte[] context=message.getBytes();  
  
                        buffer.clear();  
                        buffer.put(context);  
                        buffer.flip();  
                        client.write(buffer);  
                        key.cancel();  
                        System.out.println("=============send");  
                        try {  
                            //取消键后仍可以得到键的通道  
                            key.channel().close();  
                        } catch (IOException e1) {  
                            e1.printStackTrace();  
                        }  
                    }  
                } catch (IOException e) {  
                    e.printStackTrace();  
                      
                }  
            }  
        }  
    }  

}
