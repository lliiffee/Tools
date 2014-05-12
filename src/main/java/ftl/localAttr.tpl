<#-- 1、本地变量 -->[BR] 
<#function partg n lst> 
  <#local ans = []> 
  <#list lst as x> 
    <#if (x >= n)> 
      <#local ans = ans + [x]> 
    </#if> 
  </#list> 
  <#return ans> 
</#function> 

<#assign ls = [10, 2, 4, 5, 8, 1, 3]> 
<#list partg(4, ls) as x>${x} </#list> 

<#-- 2、变量域测试 -->[BR] 
<#macro test> 
    03. ${x} 
    <#global x = "global2"> 
    04. ${x} 
    <#assign x = "assign2"> 
    05. ${x} 
    <#local x = "local1"> 
    06. ${x} 
    <#list ["循环1"] as x> 
        07. ${x} 
        <#local x = "local2"> 
        08. ${x} 
        <#assign x = "assign3"> 
        09. ${x} 
    </#list> 
    10. ${x} 
</#macro> 

<#global x = "global1" /> 
01. ${x} 
<#assign x = "assign1" /> 
02. ${x} 
<@test /> 
11. ${x} 