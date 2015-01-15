package com.fung.gc;

import java.util.HashMap;
import java.util.Map;

public class JstatTest {  
    
  @SuppressWarnings("static-access")  
  public static void main(String[] args)  
  {  
      Map<String,Object> map=new HashMap<String,Object>();  
      for(int i=0;i<1000000;i++){  
          try  
          {  
              map.put(String.valueOf(i), new Integer(i));  
              System.out.println("循环--"+new Integer(i));  
              Thread.currentThread().sleep(10);//这里可以调整为500，50，10  
          }  
          catch (InterruptedException e)  
          {  
              e.printStackTrace();  
          }  
      }  
  }  
}

/*
 命令使用
jstat -gcutil 1300 1000 500
 
 
这里补充一下，在此文的基础上，基本多次YGC 对应一次FullGC，注意这里只有两个Survivor区，可以定义多个，数据如下：
 S0          S1      E        O          P     YGC     YGCT    FGC    FGCT     GCT
100.00   0.00  53.40  74.12   3.06     12         0.170     1        0.033    0.202 
这里进行了12的YGC，也就是说在Survivor 复制了12次，FGC进行了一次，每一次YGC都会往Old中存放对象，一下是数据支持：
 S0          S1      E         O          P      YGC     YGCT    FGC    FGCT     GCT
  0.00    0.00   100.00   0.00     3.06      0        0.000     0       0.000       0.000
  0.00   100.00   4.02    5.94     3.06      1        0.013     0       0.000       0.013
最开始s0和s1都没有，在进行一次YGC之后O里面有内容了，实验得知，每一次往S0或者S1复制对象，都会往old里面添加添加存活较久的对象，并且FGC执行
，不一定非得Old满了，才会进行，在第一组数据里，FGC就进行了，但O区还只有74.12的数据
查看java虚拟机内存情况，也可以使用这个命令：java -stat
 
以下是原理，感谢  http://jefferent.iteye.com/blog/1123677 这里贴上方便查看，也可以直接复制url上他的博客查看
虚拟机中的共划分为三个代：年轻代（Young Generation）、年老点（Old Generation）和持久代（Permanent Generation）。
其中持久代主要存放的是Java类的类信息，与垃圾收集要收集的Java对象关系不大。年轻代和年老代的划分是对垃圾收集影响比较大的。
　　年轻代:
　　所有新生成的对象首先都是放在年轻代的。年轻代的目标就是尽可能快速的收集掉那些生命周期短的对象。年轻代分三个区。一个Eden区，两个 Survivor区(一般而言)。
大部分对象在Eden区中生成。当Eden区满时，还存活的对象将被复制到Survivor区（两个中的一个），当这个 Survivor区满时，此区的存活对象将被复制到另外一个Survivor区，
当这个Survivor去也满了的时候，从第一个Survivor区复制过来的并且此时还存活的对象，将被复制“年老区(Tenured)”。需要注意，Survivor的两个区是对称的，没先后关系
，所以同一个区中可能同时存在从Eden复制过来对象，和从前一个Survivor复制过来的对象，而复制到年老区的只有从第一个Survivor去过来的对象。而且，Survivor区总有一个是空的
。同时，根据程序需要，Survivor区是可以配置为多个的（多于两个），这样可以增加对象在年轻代中的存在时间，减少被放到年老代的可能。
　　年老代:
　　在年轻代中经历了N次垃圾回收后仍然存活的对象，就会被放到年老代中。因此，可以认为年老代中存放的都是一些生命周期较长的对象。
　　持久代:
　　用于存放静态文件，如今Java类、方法等。持久代对垃圾回收没有显著影响，但是有些应用可能动态生成或者调用一些class，例如Hibernate 等，在这种时候需要设置一个比较大的持久代
空间来存放这些运行过程中新增的类。持久代大小通过-XX:MaxPermSize=<N>进行设置。

什么情况下触发垃圾回收：
    由于对象进行了分代处理，因此垃圾回收区域、时间也不一样。GC有两种类型：Scavenge GC和Full GC。
    Scavenge GC
    一般情况下，当新对象生成，并且在Eden申请空间失败时，就会触发Scavenge GC，对Eden区域进行GC，清除非存活对象，并且把尚且存活的对象移动到Survivor区。
    然后整理Survivor的两个区。这种方式的GC是对年轻代的Eden区进行，不会影响到年老代。因为大部分对象都是从Eden区开始的，同时Eden区不会分配的很大，所以Eden区的GC会频繁进行。
    因而，一般在这里需要使用速度快、效率高的算法，使Eden去能尽快空闲出来。
    Full GC
    对整个堆进行整理，包括Young、Tenured和Perm。Full GC因为需要对整个对进行回收，所以比Scavenge GC要慢，因此应该尽可能减少Full GC的次数。在对JVM调优的过程中，
    很大一部分工作就是对于FullGC的调节。有如下原因可能导致Full GC：
年老代（Tenured）被写满
持久代（Perm）被写满
System.gc()被显示调用
上一次GC之后Heap的各域分配策略动态变化
 * */
