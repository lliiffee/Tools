
  function A( name ){
 
              this.name = name;
 
       }
 
   function ObjectFactory(){
 
              var obj = {},
 
               Constructor = Array.prototype.shift.call( arguments );
 
              obj.__proto__ =  typeof Constructor.prototype === 'number'  ? Object.prototype:Constructor.prototype;
 
              var ret = Constructor.apply( obj, arguments );
 
              return typeof ret === 'object' ? ret : obj;
 
       }
 
       var a = ObjectFactory( A, 'svenzeng' );
 
       alert ( a.name );  //svenzeng

　//　这段代码来自es5的new和构造器的相关说明， 可以看到，所谓的new， 本身只是一个对象的复制和改写过程，
//而具体会生成什么是由调用ObjectFactory时传进去的参数所决定的。
