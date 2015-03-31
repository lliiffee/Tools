// inherit() returns a newly created object that inherits properties from the
// prototype object p.  It uses the ECMAScript 5 function Object.create() if
// it is defined, and otherwise falls back to an older technique.
function inherit(p) {
    if (p == null) throw TypeError(); // p must be a non-null object
    if (Object.create)                // If Object.create() is defined...
        return Object.create(p);      //    then just use it.
    var t = typeof p;                 // Otherwise do some more type checking
    if (t !== "object" && t !== "function") throw TypeError();
    function f() {};                  // Define a dummy constructor function.
    f.prototype = p;                  // Set its prototype property to p.
    return new f();                   // Use f() to create an "heir" of p.
}



function inherit(p) {
    if (p == null) throw TypeError(); 
    if (Object.create)                
        return Object.create(p);      
    var t = typeof p;                 
    if (t !== "object" && t !== "function") throw TypeError();
    function f() {};                 
    f.prototype = p;                  
    return new f();                  
}

for (p in o ){ //跳过继承的属性
	if (!o.hasownProperty(p)) continue;
}

for(p in o){
	if(typeof o[p] === "function") continue; //跳过方法。
}

function extend(o,p){ //在ie 中有bug.
	for (prop in p)   //历遍p所有属性
		{
			o[prop]=p[prop]; // 将属性添加到o
		}
	return o; 
}




/*
 * Add a nonenumerable extend() method to Object.prototype.
 * This method extends the object on which it is called by copying properties
 * from the object passed as its argument.  All property attributes are
 * copied, not just the property value.  All own properties (even non-
 * enumerable ones) of the argument object are copied unless a property
 * with the same name already exists in the target object.
 */
Object.defineProperty(Object.prototype,
    "extend",                  // Define Object.prototype.extend
    {
        writable: true,
        enumerable: false,     // Make it nonenumerable
        configurable: true,
        value: function(o) {   // Its value is this function
            // Get all own props, even nonenumerable ones
            var names = Object.getOwnPropertyNames(o);
            // Loop through them
            for(var i = 0; i < names.length; i++) {
                // Skip props already in this object
                if (names[i] in this) continue;
                // Get property description from o
                var desc = Object.getOwnPropertyDescriptor(o,names[i]);
                // Use it to create property on this
                Object.defineProperty(this, names[i], desc);
            }
        }
    });

##########
var t ={
        a:1,
  	  get a_value(){ return this.a;},
  	  set a_value(n) {return n;}
   }
//获取四个配置属性 ： Object {value: 1, writable: true, enumerable: true, configurable: true}
Object.getOwnPropertyDescriptor({x:1},"x");

Object.getOwnPropertyDescriptor({x:1, set next(n){this.x=n;}},"x");

