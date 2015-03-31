/*  游戏中隆需要接受键盘的事件, 来完成相应动作.
　　于是我写了一个keyManage类. 其中在游戏主线程里监听keyManage的变化.
　　JavaScript
*/
var keyMgr = keyManage();
 
keyMgr.listen( ''change', function( keyCode ){
 
   console.log( keyCode );
 
});
/*
　　图片里面隆正在放升龙拳, 升龙拳的操作是前下前+拳. 但是这个keyManage类只要发生键盘事件就会触发之前监听的change函数. 这意味着永远只能取得前，后，前，拳这样单独的按键事件，而无法得到一个按键组合。
　　好吧，我决定改写我的keyManage类, 让它也支持传递按键组合. 但是如果我以后写个html5版双截龙，意味着我每次都得改写keyManage. 我总是觉得, 这种函数应该可以抽象成一个更底层的方法, 让任何游戏都可以用上它.
　　所以最后的keyManage只负责映射键盘事件. 而隆接受到的动作是通过一个代理对象处理之后的.
　　JavaScript
*/
var keyMgr = keyManage();
 
keyMgr.listen( 'change', proxy( function( keyCode ){
 
   console.log( keyCode );  //前下前+拳
 
)} );

