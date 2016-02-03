package com.fung.zip;

import java.io.File;

import com.fung.file.IFileOperatiron;

public class UnzipFileOp implements IFileOperatiron{

	@Override
	public void opFile(File file) {
		if(file.getName().endsWith(".rar"))
		{
			System.out.println("start:"+file.getName());
		CompressFile.unRarFile(file.getPath(), "f:\\temp\\5\\");
		}
		else if(file.getName().endsWith(".zip"))
		{System.out.println("start:"+file.getName());
			CompressFile.unZipFiles(file, "f:\\temp\\5\\");
		}
		else
			System.out.println("ignore:"+file.getName());
	}

}
