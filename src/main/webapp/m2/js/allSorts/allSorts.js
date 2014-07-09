// JavaScript Document
$(document).ready(function(){
	$(".as_bd").find("dt").each(function(){
		$(this).click(function(){
			$(this).next().slideToggle(500);
			var c = $(this).find("span").attr("class");
			if( c=="icon_up" ){
				$(this).find("span").attr("class","icon_down");
			}
			else
			{
				$(this).find("span").attr("class","icon_up");
				$(".as_bd").find("dt").not(this).find(".icon_up").parent().click();
			}
		});	
	});
});