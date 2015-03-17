
var singleton = function( fn ){
    var result;
    return function(){
        return result || ( result = fn.apply( this, arguments ) );
    }
}
 
var createMask = singleton( 
	function()
	{
				return document.body.appendChild( document.createElement('div') );
 	}
 );

/*
用一个变量来保存第一次的返回值, 如果它已经被赋值过, 那么在以后的调用中优先返回该变量. 而真正创建遮罩层的代码是通过回调函数的方式传人到singleton包装器中的. 
这种方式其实叫桥接模式. 关于桥接模式, 放在后面一点点来说.
*/