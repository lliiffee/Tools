// JavaScript Document  https://github.com/whxaxes
var nums = 0;
var canvas = document.getElementById("cvs");
var ctx = canvas.getContext("2d");
var x1,y1,a=20,timeout,totimes = 100,jiange = 30;
canvas.width = document.documentElement.clientWidth;
canvas.height = document.documentElement.clientHeight;
var img = new Image();
img.src = "images/p1.jpg";//封面图片
img.onload = function(){
	ctx.drawImage(img,0,0,canvas.width,canvas.height);
	tapClip();
	$("#scrollBox, .goNext, .goPrev").show();
}

//通过修改globalCompositeOperation来达到擦除的效果
function tapClip(){
		var hastouch = "ontouchstart" in window?true:false,
		tapstart = hastouch?"touchstart":"mousedown",
		tapmove = hastouch?"touchmove":"mousemove",
		tapend = hastouch?"touchend":"mouseup";
		
	ctx.lineCap = "round";
	ctx.lineJoin = "round";
	ctx.lineWidth = a*2;
	ctx.globalCompositeOperation = "destination-out";
	
	canvas.addEventListener(tapstart , function(e){
		clearTimeout(timeout)
		e.preventDefault();
		
		x1 = hastouch?e.targetTouches[0].pageX:e.clientX-canvas.offsetLeft;
		y1 = hastouch?e.targetTouches[0].pageY:e.clientY-canvas.offsetTop;
		
		ctx.save();
		ctx.beginPath()
		ctx.arc(x1,y1,1,0,2*Math.PI);
		ctx.fill();
		ctx.restore();
		
		canvas.addEventListener(tapmove , tapmoveHandler);
		canvas.addEventListener(tapend , function(){
			canvas.removeEventListener(tapmove , tapmoveHandler);					
			timeout = setTimeout(function(){
				if(nums>60){
				   //canvas.className = "noOp";
				   canvas.style.display = "none";
				   //$("#media").get(0).play();
				   //$("#audio_btn").show().attr("class","on");
				};
			},totimes)
		});
		function tapmoveHandler(e){
			nums++;
			clearTimeout(timeout)
			e.preventDefault()
			x2 = hastouch?e.targetTouches[0].pageX:e.clientX-canvas.offsetLeft;
			y2 = hastouch?e.targetTouches[0].pageY:e.clientY-canvas.offsetTop;
			
			ctx.save();
			ctx.moveTo(x1,y1);
			ctx.lineTo(x2,y2);
			ctx.stroke();
			ctx.restore()
			
			x1 = x2;
			y1 = y2;
		}
	})
}

//使用clip来达到擦除效果
function otherClip(){
	var hastouch = "ontouchstart" in window?true:false,
		tapstart = hastouch?"touchstart":"mousedown",
		tapmove = hastouch?"touchmove":"mousemove",
		tapend = hastouch?"touchend":"mouseup";
	
	canvas.addEventListener(tapstart , function(e){
		clearTimeout(timeout)
		e.preventDefault();
		
		x1 = hastouch?e.targetTouches[0].pageX:e.clientX-canvas.offsetLeft;
		y1 = hastouch?e.targetTouches[0].pageY:e.clientY-canvas.offsetTop;
		
		ctx.save()
		ctx.beginPath()
		ctx.arc(x1,y1,a,0,2*Math.PI);
		ctx.clip()
		ctx.clearRect(0,0,canvas.width,canvas.height);
		ctx.restore();
		
		canvas.addEventListener(tapmove , tapmoveHandler);
		canvas.addEventListener(tapend , function(){
			canvas.removeEventListener(tapmove , tapmoveHandler);
			
			timeout = setTimeout(function(){
				var imgData = ctx.getImageData(0,0,canvas.width,canvas.height);
				var dd = 0;
				for(var x=0;x<imgData.width;x+=jiange){
					for(var y=0;y<imgData.height;y+=jiange){
						var i = (y*imgData.width + x)*4;
						if(imgData.data[i+3] >0){
							dd++
						}
					}
				}

				if(dd/(imgData.width*imgData.height/(jiange*jiange))<0.5){
					//canvas.className = "noOp";
					canvas.style.display = "none";
				}
			},totimes)
		
		});
		
		function tapmoveHandler(e){
			e.preventDefault()
			x2 = hastouch?e.targetTouches[0].pageX:e.clientX-canvas.offsetLeft;
			y2 = hastouch?e.targetTouches[0].pageY:e.clientY-canvas.offsetTop;
			
			var asin = a*Math.sin(Math.atan((y2-y1)/(x2-x1)));
			var acos = a*Math.cos(Math.atan((y2-y1)/(x2-x1)));
			var x3 = x1+asin;
			var y3 = y1-acos;
			var x4 = x1-asin;
			var y4 = y1+acos;
			var x5 = x2+asin;
			var y5 = y2-acos;
			var x6 = x2-asin;
			var y6 = y2+acos;
			
			ctx.save()
			ctx.beginPath()
			ctx.arc(x2,y2,a,0,2*Math.PI);
			ctx.clip()
			ctx.clearRect(0,0,canvas.width,canvas.height);
			ctx.restore();
			
			ctx.save()
			ctx.beginPath()
			ctx.moveTo(x3,y3);
			ctx.lineTo(x5,y5);
			ctx.lineTo(x6,y6);
			ctx.lineTo(x4,y4);
			ctx.closePath();
			ctx.clip()
			ctx.clearRect(0,0,canvas.width,canvas.height);
			ctx.restore();
			
			x1 = x2;
			y1 = y2;
		}
	})
}
		