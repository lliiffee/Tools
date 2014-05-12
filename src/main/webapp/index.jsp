<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

System.out.println(request.getParameter("detail_desc"));

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	 <script type="text/javascript" charset="utf-8" src="js/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="js/ueditor/ueditor.all.min.js"> </script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="js/ueditor/lang/zh-cn/zh-cn.js"></script>
	
	
    
  </head>
  
  <body>
  
 
  <form action="index.jsp">
  
   <fieldset>
   
   <section>
      <label for="content_type">类型：</label>
      <select id="content_type"> 
          <option value="0">资讯</option>
          <option value="1">产品</option>
      </select>
     </section>
     
    <section>
      <label for="content_title">文章主题：</label>
      <input type="text" id="content_title" name="content_title" value"" />
     </section>
     
      <section>
      <label>文章主题图片：</label>
       <input  type="button" value="上传图片">
     </section>
    
    <section>
      <label>链接：</label>
       <input type="text" id="content_title" name="content_title" value"" />
     </section>
     
      <section>
      <label for="content_title">概述内容：</label>
      <textarea > </textarea>
     </section>
                                 
   </fieldset>
  <label>文章内容：</label>
    <span  id="detail_area" style="height:auto;">
		<script id="detail_desc" name="detail_desc" type="text/plain" style="width:654px;height:500px;">
				                                                          
   	  </script>

   </span>
     <input type="submit" value="test" />
     
     
 </form>
 
  </body>
  
  
  <script type="text/javascript">
		    //实例化编辑器
		    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
		    UE.getEditor('detail_desc');
 </script>
 
		    
</html>
