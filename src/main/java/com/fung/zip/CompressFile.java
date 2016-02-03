package com.fung.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import org.apache.tools.zip.ZipEntry;  //ant jar 里
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
 
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
 
/**
 * <p>
 * Title: 解压缩文件
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: yourcompany
 * </p>
 * 
 * @author yourcompany
 * @version 1.0
 */
public class CompressFile {
     
 
    /**
     * 压缩文件
     * 
     * @param srcfile
     *            File[] 需要压缩的文件列表
     * @param zipfile
     *            File 压缩后的文件
     */
    public static void ZipFiles(java.io.File[] srcfile, java.io.File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                String str = srcfile[i].getName();
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            System.out.println("压缩完成.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * zip解压缩
     * 
     * @param zipfile
     *            File 需要解压缩的文件
     * @param descDir
     *            String 解压后的目标目录
     */
    public static void unZipFiles(java.io.File zipfile, String descDir) {
        try {


        	 System.setProperty("sun.zip.encoding", System.getProperty("sun.jnu.encoding")); //防止文件名中有中文时出错
        	   
        	 
        	 System.out.println(System.getProperty("sun.zip.encoding"));
        	 System.out.println("start....！");
            ZipFile zf = new ZipFile(zipfile,"GBK");
            for (Enumeration entries = zf.getEntries(); entries
                    .hasMoreElements();) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
               
                String zipEntryName = new String (entry.getName().getBytes(),"GBK");
               
                if (entry.isDirectory()) {
		           File fileOut = new File(descDir, entry.getName());
		            if (!fileOut.exists()) {
		              fileOut.mkdirs();
		              System.out.println(fileOut + "目录创建成功");
		            }else{
		              
		              System.out.println(fileOut + "目录已存在");
		            }
		          } else {
		           File fileOut = new File(descDir, entry.getName());
		            if (!fileOut.exists()) {
		              (new File(fileOut.getParent())).mkdirs();
		            }
		            InputStream in = zf.getInputStream(entry);
	                OutputStream out = new FileOutputStream(descDir + zipEntryName);
	                
	                byte[] buf1 = new byte[1024];
	                int len;
	                while ((len = in.read(buf1)) > 0) {
	                    out.write(buf1, 0, len);
	                }
	                in.close();
	                out.close();
	                System.out.println("解压缩完成.");
		          }
 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
       /*
       压zip不够完美，如果zip里面有文件夹里面没文件会报错，正确的方法修改如下
		if (entry.isDirectory()) {
		            fileOut = new File(rootDir, entry.getName());
		            if (!fileOut.exists()) {
		              fileOut.mkdirs();
		              System.out.println(fileOut + "目录创建成功");
		            }else{
		              
		              System.out.println(fileOut + "目录已存在");
		            }
		          } else {
		            fileOut = new File(rootDir, entry.getName());
		            if (!fileOut.exists()) {
		              (new File(fileOut.getParent())).mkdirs();
		            }
		            FileOutputStream out = new FileOutputStream(fileOut);
		            BufferedOutputStream bos = new BufferedOutputStream(
		                out);
		            int b;
		            while ((b = bis.read()) != -1) {
		              bos.write(b);
		            }
		            bos.close();
		            out.close();
		            System.out.println(fileOut + "文件解压成功");
		          }
        */
        
    }
 
     
 
    /** 
    * 根据原始rar路径，解压到指定文件夹下.      
    * @param srcRarPath 原始rar路径 
    * @param dstDirectoryPath 解压到的文件夹      
    */
    public static void unRarFile(String srcRarPath, String dstDirectoryPath) {
        if (!srcRarPath.toLowerCase().endsWith(".rar")) {
            System.out.println("非rar文件！");
            return;
        }
        File dstDiretory = new File(dstDirectoryPath);
        if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
            dstDiretory.mkdirs();
        }
        Archive a = null;
        try {
            a = new Archive(new File(srcRarPath));
            if (a != null) {
                a.getMainHeader().print(); // 打印文件信息.
                FileHeader fh = a.nextFileHeader();
                while (fh != null) {
                    if (fh.isDirectory()) { // 文件夹 
                        File fol = new File(dstDirectoryPath + File.separator
                                + fh.getFileNameString());
                        fol.mkdirs();
                    } else { // 文件
                        File out = new File(dstDirectoryPath + File.separator
                                + fh.getFileNameString().trim());
                        //System.out.println(out.getAbsolutePath());
                        try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压. 
                            if (!out.exists()) {
                                if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录. 
                                    out.getParentFile().mkdirs();
                                }
                                out.createNewFile();
                            }
                            FileOutputStream os = new FileOutputStream(out);
                            a.extractFile(fh, os);
                            os.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    fh = a.nextFileHeader();
                }
                a.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    
    /*
     * 并且注意,压缩之前调用ZipOutputStream的out.setEncoding(System.getProperty("sun.jnu.encoding"));方法,
系统参数sun.jnu.encoding表示获取当前系统中的文件名的编码方式.这里将ZipOutputStream的文件名编码方式
设置成系统的文件名编码方式.
 
解压时,直接使用JDK原来的ZipInputStream即可.
 
但是有个需要注意的地方是,在读取ZIP文件之前,需要设置:
System.setProperty("sun.zip.encoding", System.getProperty("sun.jnu.encoding"));
将系统的ZIP编码格式设置为系统文件名编码方式,否则解压时报异常.
     */
 
}