<1>if, else, elseif: 
<#if x == 1> 
  x is 1 
<#elseif x == 2> 
  x is 2 
<#elseif x == 3> 
  x is 3 
<#elseif x == 4> 
  x is 4 
<#else> 
  x is not 1 nor 2 nor 3 nor 4 
</#if> 

<2>switch, case, default, break: 
<#switch y> 
  <#case "small"> 
     This will be processed if it is small 
     <#break> 
  <#case "medium"> 
     This will be processed if it is medium 
     <#break> 
  <#case "large"> 
     This will be processed if it is large 
     <#break> 
  <#default> 
     This will be processed if it is neither 
</#switch> 

<3>list, break: 
<#assign seq = ["winter", "spring", "summer", "autumn"]> 
<#list seq as x> 
  ${x_index + 1}. ${x}<#if x_has_next>,</#if> 
</#list> 