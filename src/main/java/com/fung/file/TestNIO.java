package com.fung.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestNIO {
	
	  public static void main(String[] args) throws Exception {  
	        String infile = "C:\\copy.sql";  
	        String outfile = "C:\\copy.txt";  
	        // 获取源文件和目标文件的输入输出流  
	        FileInputStream fin = new FileInputStream(infile);  
	        FileOutputStream fout = new FileOutputStream(outfile);  
	        // 获取输入输出通道  
	        FileChannel fcin = fin.getChannel();  
	        FileChannel fcout = fout.getChannel();  
	        // 创建缓冲区  
	        ByteBuffer buffer = ByteBuffer.allocate(1024);  
	        while (true) {  
	            // clear方法重设缓冲区，使它可以接受读入的数据  
	            buffer.clear();  
	            // 从输入通道中将数据读到缓冲区  
	            int r = fcin.read(buffer);  
	            // read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1  
	            if (r == -1) {  
	                break;  
	            }  
	            // flip方法让缓冲区可以将新读入的数据写入另一个通道  
	            buffer.flip();  
	            // 从输出通道中将数据写入缓冲区  
	            fcout.write(buffer);  
	        }  
	    }  

}




/*
 * 1.   基本 概念
IO 是主存和外部设备 ( 硬盘、终端和网络等 ) 拷贝数据的过程。 IO 是操作系统的底层功能实现，底层通过 I/O 指令进行完成。
所有语言运行时系统提供执行 I/O 较高级别的工具。 (c 的 printf scanf,java 的面向对象封装 )
2.    Java 标准 io 回顾
Java 标准 IO 类库是 io 面向对象的一种抽象。基于本地方法的底层实现，我们无须关注底层实现。 InputStream\OutputStream( 字节流 ) ：一次传送一个字节。 Reader\Writer( 字符流 ) ：一次一个字符。
3.    nio 简介
nio 是 java New IO 的简称，在 jdk1.4 里提供的新 api 。 Sun 官方标榜的特性如下：
–     为所有的原始类型提供 (Buffer) 缓存支持。
–     字符集编码解码解决方案。
–     Channel ：一个新的原始 I/O 抽象。
–     支持锁和内存映射文件的文件访问接口。
–     提供多路 (non-bloking) 非阻塞式的高伸缩性网络 I/O 。
本文将围绕这几个特性进行学习和介绍。
4.   Buffer&Chanel
Channel 和 buffer 是 NIO 是两个最基本的数据类型抽象。
Buffer:
–        是一块连续的内存块。
–        是 NIO 数据读或写的中转地。
Channel:
–        数据的源头或者数据的目的地
–        用于向 buffer 提供数据或者读取 buffer 数据 ,buffer 对象的唯一接口。
–         异步 I/O 支持
 */
