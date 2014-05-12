package com.fung.freemarker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FirTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		FirTest ft=new FirTest();
		// TODO Auto-generated method stub
		HashMap t2root = new HashMap<String, String>();  
		t2root.put("user", "RenSanNing"); 
		
		try {
			ft.process("test.tpl",t2root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, Object> t3root = new HashMap<String, Object>();  
		List<Food> menu = new ArrayList<Food>();  
		menu.add(new Food("iText in Action", 98));  
		menu.add(new Food("iBATIS in Action", 118));  
		menu.add(new Food("Lucene in Action", 69));  
		t3root.put("menu", menu);  
		
		try {
			ft.process("list.tpl",t3root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//逻辑判断（IF,SWITCH) 
		Map<String, Object> t4root = new HashMap<String, Object>();
		t4root.put("x", 2);
		t4root.put("y", "medium");
		
		try {
			ft.process("op.tpl",t4root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//自定义函数
				Map<String, Object> t5root = new HashMap<String, Object>();
				try {
					ft.process("sfFunc.tpl",t5root);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		//定义变量
				try {
					ft.process("localAttr.tpl",t5root);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		//定义宏macro 
				
				try {
					ft.process("macro.tpl",t5root);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//引用
				
				try {
					ft.process("include.tpl",t5root);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
// namespace引用
				
				try {
					ft.process("namespace_include.tpl",t5root);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//自定义指令Directive 
//<#assign checkPermission= "com.beyondsoft.common.util.FtlCheckPermissionMethod"?new()>  
				
				Map<String, Object> t10root = new HashMap<String, Object>();
				
				t10root.put("systemdate", new SystemDateDirective());
	//设置。。。也可以在模板上设置   <#assign checkPermission= "com.beyondsoft.common.util.FtlCheckPermissionMethod"?new()>  
				t10root.put("text_cut", new TextCutDirective());  
				
				try {
					ft.process("sl_dir.tpl",t10root);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}
	
	 
	public  void process(String template, Map<String, ?> data) throws Exception {
		String FILE_DIR=this.getClass().getResource("/").getPath();
		System.out.println(FILE_DIR);
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(FILE_DIR+"ftl"));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		
		//设置字符集
		cfg.setDefaultEncoding("UTF-8");
		
		//设置尖括号语法和方括号语法,默认是自动检测语法
		//  自动 AUTO_DETECT_TAG_SYNTAX
		//  尖括号 ANGLE_BRACKET_TAG_SYNTAX
		//  方括号 SQUARE_BRACKET_TAG_SYNTAX
		cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);

		Writer out = new OutputStreamWriter(new FileOutputStream(FILE_DIR+"ftl/" + template+ ".txt"),"UTF-8");
		Template temp = cfg.getTemplate(template);
		temp.process(data, out);
		out.flush();
	}

}
