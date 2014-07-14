//优惠图片控制内容长度
function limit(obj){
	obj.each(function(){
		var objString = common.fTrim(jQuery(this).text());
		var objLength = objString.length;
		var _num = jQuery(this).attr("vlimit");
		if(objLength > _num){
			jQuery(this).attr("title",objString);
			objString = jQuery(this).text(objString.substring (0,_num) + "...");
		}
	});
}; 

// JavaScript Document
$(document).ready(function(){

	limit(jQuery("[vlimit]")); 
	
	$(".index_scroll li").eq(0).show(); 
	
	
	//左右手势滑动
	document.getElementById("scrollBox").addEventListener("touchstart", touchStart, false);
	document.getElementById("scrollBox").addEventListener("touchmove", touchMove, false);
    document.getElementById("scrollBox").addEventListener("touchend", touchEnd, false);
	
	common.staticLog();
	
	//判断是否登录，显示不同菜单
	if (sessionCache.getItem('user_id') != null && 'undefined' != sessionCache.getItem('user_id')) {
		$("#loggedDiv").show();//我的八百方，退出登录
		$("#noLoggedDiv").hide();//登录，注册
	} else {
		$("#noLoggedDiv").show();//登录，注册
		$("#loggedDiv").hide();//我的八百方，退出登录
	}
	
    $('#searchHeadContent').keydown(function(event){
        if(event.keyCode==13){
		    var searchKeyWord=$("#searchHeadContent").val();
		    sessionCache.itemUpdate("search_key",searchKeyWord);
			window.location="http://www.800pharm.com/shop/m/prodSearchList.html?search_key="+searchKeyWord+'&ref=out';
			return false;
        }
    });
    
    $('#searchTailContent').keydown(function(event){
        if(event.keyCode==13){
		    var searchKeyWord = $("#searchTailContent").val();
		    sessionCache.itemUpdate("search_key",searchKeyWord);
			window.location="http://www.800pharm.com/shop/m/prodSearchList.html?search_key="+searchKeyWord+'&ref=out';
			return false;
        }
    });
	
	//搜索提示框
	jQuery(function(){
		var line = 0;//定义一个全局的line以用于记录当前显示的是哪一行
		var keywords = "";//用于记录用户输入的关键字,因为在上下键操作的过程中会改变搜索框的值，用户在通过键盘上下键操作时应该还可以返回到他最初的状态
		var keycontrol = 0; //用于控制当弹出框还未显示或失去焦点时，上下键取值的问题，如果失去焦点，上下键将不取li里的值,0表示不取值，1表示可取值
		var keyStr = 0;
		var vkeyword ="";
		var time;
		jQuery(".main_input").keyup(function(e){
				vkeyword=jQuery(".main_input").val();
				keycontrol = 1;//将状态变为1，供下面上下键取li里的值使用
				if(vkeyword !=""){
					clearTimeout(time);//清除定时器，防止操作频繁造成过多查询的情况
					time = setTimeout(function(){getResult(vkeyword)},100);
				}else{
					none();
				}
		});
		//失去焦点之后关闭
		jQuery(document.documentElement).click(function () {
			keycontrol = 0;//将keycontrol的值初始化为0,防止上下键取li里的值
			none();
		});
	});
});

function logout(){
	var url="/shop/member/memberLogout_m.html";
	asnyTask(url ,"", callbackLogout);
}

function callbackLogout(response){
	sessionCache.clearAll();
	TempCache.removeItem("login_username");
	TempCache.removeItem("login_password");
	TempCache.removeItem("login_username_display");
	$("#noLoggedDiv").show();
	$("#loggedDiv").hide();
}

function searchHead(){
	var searchKeyWord=$("#searchHeadContent").val();
	sessionCache.itemUpdate("search_key",searchKeyWord);
	window.location="http://www.800pharm.com/shop/m/prodSearchList.html?search_key="+searchKeyWord+'&ref=out';
}

function searchTail(){
	var searchKeyWord = $("#searchTailContent").val();
	sessionCache.itemUpdate("search_key",searchKeyWord);
	window.location="http://www.800pharm.com/shop/m/prodSearchList.html?search_key="+searchKeyWord+'&ref=out';
}

//焦点图切换
var t = self.setInterval(scrollbox,3000);
function scrollbox() {
	var n = $(".index_scroll li").filter(":visible").index();
	if (n==4)
	{
		$(".index_scroll li").filter(":visible").hide(); 
		$(".index_scroll li:first-child").show();
		$(".scrollNav li").filter(".cur").removeClass();
		$(".scrollNav li:first-child").addClass("cur");
	}
	else
	{	
		$(".index_scroll li").filter(":visible").hide().next().show(); 
		$(".scrollNav li").filter(".cur").removeClass().next().addClass("cur");
	}
}

var startX, endX;
function touchStart(event) {
	event.preventDefault();
	var touch = event.touches[0];
	startX = touch.pageX;
	t = window.clearInterval(t);
}
function touchMove(event) {
	event.preventDefault();
	var touch = event.touches[0];
	endX = (startX - touch.pageX);
}
function touchEnd(event) {
	event.preventDefault();
	if (endX > 100) {
		var n = $(".index_scroll li").filter(":visible").index();
		if (n==4)
		{
			$(".index_scroll li").filter(":visible").hide(); 
			$(".index_scroll li:first-child").show();
			$(".scrollNav li").filter(".cur").removeClass();
			$(".scrollNav li:first-child").addClass("cur");
		}
		else
		{	
			$(".index_scroll li").filter(":visible").hide().next().show(); 
			$(".scrollNav li").filter(".cur").removeClass().next().addClass("cur");
		}
	}
	if (endX < -100) {
		var n = $(".index_scroll li").filter(":visible").index();
		if (n==0)
		{
			$(".index_scroll li").filter(":visible").hide(); 
			$(".index_scroll li:last-child").show();
			$(".scrollNav li").filter(".cur").removeClass();
			$(".scrollNav li:last-child").addClass("cur");
		}
		else
		{	
			$(".index_scroll li").filter(":visible").hide().prev().show(); 
			$(".scrollNav li").filter(".cur").removeClass().prev().addClass("cur");
		}
	}
	t = self.setInterval(scrollbox,5000);
}

//关闭
function none(){
    jQuery(".autocomplete").empty().hide();
    line = 0;
}

//异步请求数据
function getResult(val){
	jQuery.ajax({
		type : "get",
		cache : false,//不缓存
		async:false,
		url: "http://www.800pharm.com/shop/searchKWTip.html?searchValue="+encodeURIComponent(val),
		dataType : "jsonp",
		jsonp: "callbackparam",//服务端用于接收callback调用的function名的参数
		jsonpCallback:"callback",//callback的function名称
		success : function(json){
			jQuery(".autocomplete ul").remove();//增加节点前先清空
			var litag ="";
			var n = 0;
			if(json.length > 5)
				n = 5;
			else
				n = json.length;
			for(var i=0;i<n;i++){
				var url = 'http://www.800pharm.com/shop/m/prodSearchList.html?search_key='+encodeURIComponent(json[i].kw)+'&ref=out';
				litag = litag + "<li><a href='"+url+"'>"+json[i].kw+"</a></li>";
			}
			litag = litag +  "<li class='closeComplete'><a href='javascript:closeComplete()'>关闭</a></li>";
			jQuery(".autocomplete").append("<ul>"+litag +"</ul>");
			var length = jQuery(".autocomplete li").length;
			if(length>0){
				jQuery(".autocomplete").show();
			}else{
				none();
			}
		},
		error:function(){
		}
	});
}

function closeComplete(){
	$("body").click();
}