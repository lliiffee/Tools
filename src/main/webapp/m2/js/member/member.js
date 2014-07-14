// JavaScript Document
$(document).ready(function(){
	$(".myOrder_tit").click(function(){
		var c = $(this).find("b").attr("class");
		if(c == "down_icon"){
			$(this).find(".orderMenu").slideDown();
			$(this).find("b").attr("class","up_icon");	
		}
		else{
			$(this).find(".orderMenu").slideUp();
			$(this).find("b").attr("class","down_icon");		
		}
		
	});
	$(".b_input").find(".main_input").focusin(function(){
		$(this).attr("placeholder","");
	});
	$(".b_input").find(".main_input").focusout(function(){
		$(this).attr("placeholder","商品名称/功效/症状/厂家名");
	});
	$(".transpList").find(".check").each(function(){
		$(this).click(function(){
			var v = $(this).text();
			if(v == "查看物流"){
				$(this).parent().css("border","none");
				$(this).parent().next().children().slideDown();
				$(this).find("a").text("收起信息");
			}
			else{
				$(this).parent().next().children().slideUp();
				$(this).find("a").text("查看物流");
			}
		});
	});
	$(".cardList").find(".value").each(function(){
		var v = $(this).find("p").text().replace("￥","");
		if(v<10)
			$(this).css("background-color","#8962e7");
		else if(v<15)
			$(this).css("background-color","#03c6f0");
		else if(v<20)
			$(this).css("background-color","#ff41a4");
		else if(v<25)
			$(this).css("background-color","#ca902b");
		else if(v<30)
			$(this).css("background-color","#fc6b2b");
		else if(v<50)
			$(this).css("background-color","#dabd00");
		else if(v<100)
			$(this).css("background-color","#00cd9a");
		else
			$(this).css("background-color","#ff3232");	
	});
	$(".myWelfare").find(".abort:visible").each(function(){
		$(this).parent().prev().css("background-color","#a1a1a1");
	});
	$(".add_Address").find("select").each(function(){
        $(this).change(function(){
			var v = $(this).val();
			$(this).prev().text(v);	
		});
    });
});
