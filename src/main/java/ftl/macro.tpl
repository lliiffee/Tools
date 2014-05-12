<#-- 1、无参数 -->[BR] 
<#macro greet> 
Welcome! 
</#macro> 

<@greet /> 

<#-- 2、有参数 -->[BR] 
<#macro greet user> 
${user}, Welcome! 
</#macro> 

<@greet user="RenSanNing"/> 

<#-- 3、有多个参数 -->[BR] 
<#macro table cols rows> 
  <table> 
    <#list 1..rows as row> 
      <tr> 
        <#list 1..cols as col> 
          <td>${row}, ${col}</td> 
        </#list> 
      </tr> 
    </#list> 
  </table> 
</#macro> 

<@table cols=3 rows=2 /> 

<#-- 4、中间跳出 -->[BR] 
<#macro out> 
  显示文字 
  <#return> 
  不显示文字 
</#macro> 

<@out /> 

<#-- 5、嵌套 -->[BR] 
<#macro lprint lst> 
  <#list lst as item> 
  ・${item}<#nested item /> 
  </#list> 
</#macro> 

<@lprint 1..3; x>^2 = ${x * x}</@lprint> 
<@lprint 1..3; x>^3 = ${x * x * x}</@lprint> 
<@lprint ["Let's go", "to the", "land of Medetai"] /> 