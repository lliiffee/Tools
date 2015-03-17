/*
                  桥接模式的作用在于将实现部分和抽象部分分离开来， 以便两者可以独立的变化。在实现api的时候， 桥接模式特别有用
*/


 

//例子一
var forEach = function( ary, fn ){
  for ( var i = 0, l = ary.length; i < l; i++ ){
    var c = ary[ i ];
    if ( fn.call( c, i, c ) === false ){
      return false;
    }
   }
}



forEach( [1,2,3], function( i, n ){
 
    alert ( n*2 );
 
} );
 
forEach( [1,2,3], function( i, n ){
 
  alert ( n*3 );
 
} )


//例子二

var singleton = function( fn ){
    var result;
    return function(){
        return result || ( result = fn .apply( this, arguments ) );
    }
}
 
var createMask = singleton( function(){
 
return document.body.appendChild( document.createElement('div') );
 
 })

//　　singleton是抽象部分， 而createMask是实现部分。 他们完全可以独自变化互不影响。 如果需要再写一个单例的createScript就一点也不费力.

