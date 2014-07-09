var common = {
	realize : function() {
		alert('该功能尚未实现');
	},
	GetUrlParam : function(paramName) {
		var oRegex = new RegExp('[\?&]' + paramName + '=([^&]+)', 'i');
		var oMatch = oRegex.exec(window.location.search);
		if (oMatch && oMatch.length > 1)
			return oMatch[1];
		else
			return '';
	},
	// 空格验证
	fTrim : function(str) {
		return str.replace(/(^\s*)|(\s*$)/g, "");
    },
    staticLog:function(){
		var _umy = encodeURIComponent(document.location.href)+"&&&"+encodeURIComponent(document.referrer||"")+"&&&"+encodeURIComponent(location.search||"")+"&&&"+new Date().getTime();
		var static_url=unescape("/shop/js/staticxxx.jsx%3F"+_umy);
		$.ajax({url : static_url.replace("\#","$")});
	}
};

function asnyTask( url,dataObj,callBackFunc){
	$.ajax({
		type : "POST",
		url : url,
		data : dataObj,
		contenttype : "application/x-www-form-urlencoded;charset=utf-8",
		dataType : "json",
		success : callBackFunc,
		timeout : 30000,
		error : errorHandler
	});
}

function errorHandler(jqXHR, textStatus, errorThrown){
	console.log("Error, textStatus: 11" + textStatus + " errorThrown: "+ errorThrown);
    $("#divTip").html("请检查网络！");
	$.mobile.hidePageLoadingMsg();

	//show error message
	$( "<div class='ui-loader ui-overlay-shadow ui-body-e ui-corner-all'><h1>"+ $.mobile.pageLoadErrorMessage +"</h1></div>" )
	.css({ "display": "block", "opacity": 0.96, "top": 100 })
	.appendTo( $.mobile.pageContainer )
	.delay( 800 )
	.fadeOut( 1000, function() {
		$( this ).remove();
	});
}

$(document).ready(function(){
	$(".b_input").find(".main_input").focusin(function(){
		$(this).attr("placeholder","");
	});
	$(".b_input").find(".main_input").focusout(function(){
		$(this).attr("placeholder","商品名称/功效/症状/厂家名");
	});
});