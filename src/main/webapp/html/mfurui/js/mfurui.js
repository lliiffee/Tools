// JavaScript Document
$(document).ready(function(){
	init();
	var mySwiper = new Swiper('.swiper-container',{
		speed:500,
		followFinger:false,
    	mode: 'vertical',
		resistance: '100%',
		simulateTouch: 'false',
		onSlideChangeStart: function(swiper){
		  var p = swiper.activeIndex + 1;
		  if(p == 7){
		     $(".goNext").hide(); 
		  }
		  else{
			  $(".goNext").show(); 
		  } 
		},
		onSlideChangeEnd: function(swiper){
			var p = swiper.activeIndex + 1;
			if(p == 5){
				$(".p5 .pBody").show(); 
			}
		}
    });
	$(".popClose").on("click",function(){
		$(".fixedPop, .film").hide();
	});
	$(".pd").on("click",function(){
		$(".fixedPop, .film").show();
		var pd = $(this).attr("id");
		if(pd == "pd1"){
			$(".cpd").text("口腔溃疡含漱液").attr("choose",pd);
		}
		else if(pd == "pd2"){
			$(".cpd").text("爽口喷剂").attr("choose",pd);
		}
		else{
			$(".cpd").text("润口喷剂").attr("choose",pd);
		}
	});
});



window.onload = function(){
	$(".loading").hide();
}

window.onresize = function(){
	init();
}

function init(){
	var getWidth = document.documentElement.clientWidth;
	var getHeight = document.documentElement.clientHeight;
	$(".swiper-container").css("width",getWidth);
	$(".swiper-container").css("height",getHeight);
	$(".loading").css("width",getWidth);
	$(".loading").css("height",getHeight);
	$(".spinner").show();
	$(".swiper-wrapper").show();
	$(".swiper-slide").each(function() {
        $(this).css("width",getWidth);
		$(this).css("height",getHeight);
    });
	$(".goNext").css("left",(getWidth - 21)/2);
	$("#fixedJd").css("width",getWidth).css("height",getHeight);
	var rate = 436 / 16;
    var rootFont = getHeight / rate + (getHeight - 436)/4;
    $("html").css("font-size",rootFont + "px");
}