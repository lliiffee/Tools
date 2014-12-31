package com.fung.serialize;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Test {

	//序列化对象到文件   
    public static void serialize(String fileName){   
        try  
        {   
            //创建一个对象输出流，讲对象输出到文件   
            ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(fileName));   
  
            
            DrugBase db=new DrugBase();
            db.setProductCatelogBase(new ProductCatelogBase());
            
            Product p=new Product();
            p.setBargainpro(new Bargainpro());
            p.setDrugBase(db);
            p.setLspm(new LimitSaleProductMap());
            
            
            CartRecord user=new CartRecord(); 
            user.setAdminaccount("222");
            user.setProduct(p);
            CartRecord user2=new CartRecord(); 
            user2.setAdminaccount("1111");
            user2.setProduct(new Product());
            ArrayList<CartRecord> list=new ArrayList<CartRecord>();
            list.add(user);
            list.add(user2);
            out.writeObject(list);  //序列化一个会员对象   
  
            out.close();   
        }   
        catch (Exception x)   
        {   
            System.out.println(x.toString());   
        }   
           
    }   
    //从文件反序列化到对象   
    public static void deserialize(String fileName){   
        try  
        {   
            //创建一个对象输入流，从文件读取对象   
            ObjectInputStream in=new ObjectInputStream(new FileInputStream(fileName));   
  
            //读取UserInfo对象并调用它的toString()方法   
            ArrayList<CartRecord> list=( ArrayList<CartRecord>)(in.readObject());             
            System.out.println(list.toString());   
  
            in.close();   
        }   
        catch (Exception x)   
        {   
            System.out.println(x.toString());   
        }   
           
    }   
  
    public static void main(String[] args) {       
  
        serialize("D:\\test.txt");   
        System.out.println("序列化完毕");   
           
        deserialize("D:\\test.txt");   
        System.out.println("反序列化完毕");   
    }   

}
