<#function fact n> 
  <#if n == 0> 
    <#return 1 /> 
  <#else> 
    <#return fact(n - 1) * n /> 
  </#if> 
</#function> 

<#list 0..10 as i> 
  ${i}! => ${fact(i)} 
</#list>